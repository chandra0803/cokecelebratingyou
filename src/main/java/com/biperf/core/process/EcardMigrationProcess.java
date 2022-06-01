/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.process;

import static com.biperf.core.utils.Environment.getEnvironment;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.multimedia.EcardLocale;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.ecard.ECardsRepositoryFactory;
import com.biperf.core.service.multimedia.MultimediaService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.strategy.AmazonClientFactory;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.utils.ProfileAvatarUploadUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.ecard.ECardMigrateView;

public class EcardMigrationProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( EcardMigrationProcess.class );

  public static final String BEAN_NAME = "ecardMigrationProcess";

  public static final String PROTOCOL_HTTP = "http";

  public static final String PROTOCOL_HTTPS = "https";

  private @Autowired ECardsRepositoryFactory eCardsRepositoryFactory;
  private @Autowired MultimediaService multimediaService;
  private @Autowired AmazonClientFactory amazonClientFactory;

  private Long cardId;
  private String companyId = null;
  private Set<String> failedCards = new HashSet<String>();

  public void setCardId( Long cardId )
  {
    this.cardId = cardId;
  }

  @Override
  protected void onExecute()
  {
    Long initialCount = 0L;

    try
    {
      companyId = MeshServicesUtil.getCompanyId();
    }
    catch( Exception exc )
    {
      addComment( " Company set up is invalid or improperly defined. Consequently, the process could not be completed successfully. Check the Application logs for more details." );
      log.error( "Exception while getting CompanyId : ", exc );
      return;
    }

    String meshSecretKey = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_SECRET_KEY ).getStringVal();
    String meshClientId = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.NACKLE_MESH_SERVICES_CLIENT_ID ).getStringVal();

    if ( StringUtils.isNotEmpty( companyId ) && ( StringUtils.isNotEmpty( meshSecretKey ) && !meshSecretKey.equals( "CHANGE ME!" ) )
        && ( StringUtils.isNotEmpty( meshClientId ) && !meshClientId.equals( "CHANGE ME!" ) ) )
    {
      List<Long> cardIdList = null;
      if ( null == cardId )
      {
        cardIdList = multimediaService.getAllECardIds();
      }
      else
      {
        cardIdList = new ArrayList<Long>();
        cardIdList.add( cardId );
      }

      if ( !CollectionUtils.isEmpty( cardIdList ) )
      {
        initialCount = (long)cardIdList.size();
        String defaultLanguage = getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
        String siteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

        cardIdList.stream().forEach( cardId ->
        {
          try
          {
            migrateEcard( cardId, companyId, defaultLanguage, siteUrl );
          }
          catch( ServiceErrorException e )
          {
            logMessage( e, cardId );
          }
          catch( Exception e )
          {
            log.error( "Ecard migration failed cardId : " + cardId + " Cause :" + e.getMessage() );
            if ( null != cardId )
            {
              addComment( "Ecard migration failed Cause: " + e.getMessage() );
            }
          }
        } );

        if ( null == cardId )
        {
          Long finalCount = (long)multimediaService.getAllECardIds().size();
          Long totalCount = initialCount - finalCount;

          if ( initialCount == finalCount )
          {
            addComment( " Eligible records/ecards for Migration : " + initialCount );
            addComment( " Your request failed. Check the Application logs for more details." );
          }
          else if ( finalCount != 0L )
          {
            addComment( " Eligible records/ecards for Migration : " + initialCount );
            addComment( " Your request was processed. " + totalCount + " Ecards migrated successfully while " + finalCount + " Ecards failed migration. Check the Application logs for more details." );
          }
          else
          {
            Long failedCardsCount = 0L;
            Long sucessCardsCount = 0L;

            try
            {
              failedCardsCount = (long)failedCards.size();
              sucessCardsCount = initialCount - failedCardsCount;
            }
            catch( Exception ee )
            {
            }

            if ( failedCardsCount > 0L )
            {
              addComment( " Eligible records/ecards for Migration : " + initialCount );
              addComment( "Your request was processed. " + sucessCardsCount + " records migrated successfully while " + failedCardsCount
                  + " records failed migration. Check the Application logs for more details." );
            }
            else
            {
              addComment( " Eligible records/ecards for Migration : " + initialCount );
              addComment( " Your request was processed successfully. " + initialCount + " Ecards were migrated effectively." );
            }
          }
        }
        else
        {
          addComment( " Process Executed Successfully" );
        }

      }
      else
      {
        addComment( " Your request was processed successfully. However did not find any card to migrate. Check the card URL in Card table." );
        return;
      }

    }
    else
    {
      addComment( " The authorization credentials provided for Kong are invalid. Check the value of the credentials as defined in System Variables(nackle.mesh.services.client.id.<env> & nackle.mesh.services.secret.key.<env>)." );
    }

  }

  private void logMessage( ServiceErrorException e, Long ecardId )
  {
    if ( null != e.getServiceErrors() )
    {
      for ( Iterator iter = e.getServiceErrors().iterator(); iter.hasNext(); )
      {
        ServiceError error = (ServiceError)iter.next();
        log.error( "Ecard migration failed cardId: " + ecardId + " cause: " + error.getKey() );
        if ( null != cardId )
        {
          addComment( "Ecard migration failed cause : " + error.getKey() );
        }
      }

    }
  }

  private void migrateEcard( Long cardId, String companyId, String defaultLanguage, String siteUrl ) throws ServiceErrorException
  {
    ECard ecardBean = multimediaService.getECardById( cardId );
    String baseEndPoint = MeshServicesUtil.getNackleMeshServicesBaseEndPoint();

    String ecardRequestJson = createEcardJsonRequest( ecardBean, companyId, defaultLanguage, siteUrl );

    if ( Objects.nonNull( ecardRequestJson ) )
    {

      List<ECardMigrateView> cardResponseList = eCardsRepositoryFactory.getRepo().getMigrateECards( ecardRequestJson );

      if ( CollectionUtils.isNotEmpty( cardResponseList ) )
      {
        if ( ecardBean.isTranslatable() )
        {
          Set<EcardLocale> localeEcardSet = ecardBean.getEcardLocales();

          cardResponseList.stream().forEach( cardResponse ->
          {
            Iterator<EcardLocale> localeEcardItr = localeEcardSet.iterator();

            while ( localeEcardItr.hasNext() )
            {
              EcardLocale locale = localeEcardItr.next();
              if ( cardResponse.getLocale().equals( locale.getLocale() ) && !StringUtil.isNullOrEmpty( cardResponse.getUploadedName() ) )
              {
                locale.setDisplayName( baseEndPoint + ECard.IMAGE_SERVICE_SMALL_IMG + cardResponse.getUploadedName() );
                break;
              }
            }

          } );
        }

        // For default locale
        for ( ECardMigrateView defaultLocaleResponse : cardResponseList )
        {
          if ( defaultLocaleResponse.getLocale().equals( defaultLanguage ) && !StringUtil.isNullOrEmpty( defaultLocaleResponse.getUploadedName() ) )
          {
            ecardBean.setSmallImageName( baseEndPoint + ECard.IMAGE_SERVICE_SMALL_IMG + defaultLocaleResponse.getUploadedName() );
            ecardBean.setLargeImageName( baseEndPoint + ECard.IMAGE_SERVICE_LARGE_IMG + defaultLocaleResponse.getUploadedName() );
            ecardBean.setFlashName( baseEndPoint + ECard.IMAGE_SERVICE_FLASH_IMG + defaultLocaleResponse.getUploadedName() );
            break;
          }
        }

        multimediaService.saveCard( ecardBean );

      }
    }
  }

  private String createEcardJsonRequest( ECard ecardBean, String companyId, String defaultLanguage, String siteUrl ) throws ServiceErrorException
  {
    String jsonRequestString = null;
    String imageData = getImageData( ecardBean.getLargeImageName(), siteUrl, ecardBean.getId().toString() );

    JSONArray imageArray = new JSONArray();

    // Adding Default Locale
    if ( !StringUtil.isEmpty( imageData ) )
    {
      JSONObject defaultImage = addJsonImageProperty( getLocaleImageName( ecardBean.getLargeImageName(), defaultLanguage ), imageData, defaultLanguage, "Y" );
      imageArray.add( defaultImage );
    }

    if ( CollectionUtils.isNotEmpty( ecardBean.getEcardLocales() ) )
    {
      ecardBean.getEcardLocales().stream().forEach( ecardLocale ->
      {
        String localeImageName = getLocaleImageName( ecardBean.getLargeImageName(), ecardLocale.getLocale() );
        String localeImageData = "";
        try
        {
          localeImageData = getImageData( localeImageName, siteUrl, ecardBean.getId().toString() );
        }
        catch( ServiceErrorException e )
        {
          log.error( "Error getting image data from WebDav/local , Image Name: " + localeImageName );
        }
        if ( !StringUtil.isEmpty( localeImageData ) )
        {
          JSONObject localeImage = addJsonImageProperty( localeImageName, localeImageData, ecardLocale.getLocale(), "N" );
          imageArray.add( localeImage );
        }
      } );
    }

    if ( imageArray.size() > 0 )
    {
      JSONObject ecardRequestObject = new JSONObject();
      ecardRequestObject.put( "cardId", String.valueOf( ecardBean.getId() ) );
      ecardRequestObject.put( "companyId", companyId );
      ecardRequestObject.put( "status", ecardBean.isActive() ? "Y" : "N" );
      ecardRequestObject.put( "visibility", "N" );
      ecardRequestObject.put( "cardName", ecardBean.getName() );
      ecardRequestObject.put( "cardTitle", ecardBean.getName() );

      ecardRequestObject.put( "images", imageArray );
      jsonRequestString = ecardRequestObject.toJSONString();
    }
    return jsonRequestString;
  }

  private String getLocaleImageName( String imageName, String locale )
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append( imageName.split( "\\." )[0] ).append( "_" );
    buffer.append( locale ).append( "." );
    buffer.append( ImageUtils.getFileExtension( imageName ) );
    return buffer.toString();
  }

  private JSONObject addJsonImageProperty( String name, String imageData, String locale, String isDefault )
  {
    JSONObject imageDetailJson = new JSONObject();
    imageDetailJson.put( "name", name );
    imageDetailJson.put( "locale", locale );
    imageDetailJson.put( "default", isDefault );
    imageDetailJson.put( "image", imageData );

    return imageDetailJson;
  }

  private String getImageData( String imageName, String siteUrl, String cardId ) throws ServiceErrorException
  {
    byte[] data = null;
    String imageBase64Value = null;
    String imageData = null;

    String imagePath = ECard.CARDS_FOLDER + imageName;

    data = getFileData( imagePath, siteUrl, cardId );

    if ( null == data )
    {
      throw new ServiceErrorException( "No Image data found" );
    }
    imageBase64Value = Base64.getEncoder().encodeToString( data );
    if ( !StringUtil.isEmpty( imageBase64Value ) )
    {
      imageData = "data:" + ProfileAvatarUploadUtil.getContentType( imageName ) + ";base64," + imageBase64Value;
    }

    return imageData;

  }

  private byte[] getFileData( String filePath, String siteUrl, String cardId ) throws ServiceErrorException
  {

    AmazonS3 s3client = null;
    String urlLocation = null;
    DataInputStream reader = null;
    HttpURLConnection conn = null;
    byte[] data = null;
    String urlKey = null;

    try
    {
      if ( AwsUtils.isAws() )
      {
        s3client = amazonClientFactory.getClient();
        urlKey = getAwsUrlLocationPrefix() + ImageUtils.convertToUrlPath( filePath );
        S3Object object = s3client.getObject( new GetObjectRequest( getAwsBucketName(), urlKey ) );
        data = new byte[(int)object.getObjectMetadata().getContentLength()];
        DataInputStream dataIs = new DataInputStream( object.getObjectContent() );
        dataIs.readFully( data );
        dataIs.close();
        object.close();

      }
      else
      {
        urlLocation = siteUrl + ImageUtils.convertToUrlPath( filePath );

        if ( urlLocation.startsWith( PROTOCOL_HTTPS ) )
        {
          urlLocation = urlLocation.replace( PROTOCOL_HTTPS, PROTOCOL_HTTP );
        }
        URL url = new URL( urlLocation );
        conn = (HttpURLConnection)url.openConnection();
        reader = new DataInputStream( conn.getInputStream() );

        data = IOUtils.toByteArray( reader );
      }

    }
    catch( AmazonServiceException ase )
    {
      if ( ase.getStatusCode() == 404 && ase.getErrorCode().contains( "NoSuchKey" ) )
      {
        log.error( " ====>>>  Ecard Is Missing For The Card Id    : " + cardId + " . The Ecard Url Path Is Exist In DB  : " + filePath + " ,But Not Exist In AWS S3 Bucket (Name) : "
            + getAwsBucketName() + " , The S3 Url Path Is : " + urlKey );

        failedCards.add( cardId );
      }

    }
    catch( IOException e )
    {
      if ( !AwsUtils.isAws() )
      {
        log.error( " ====>>>  Ecard Is Missing For The Card Id   : " + cardId + " . Ecard Url Path Is Exist In DB : " + filePath + " ,But Not Exist In Webdav : " + urlLocation );
        failedCards.add( cardId );
      }

      log.error( "Failed to invoke URL " + e );
    }
    finally
    {
      if ( s3client != null && AwsUtils.isAws() )
      {
        s3client.shutdown();
      }

      if ( conn != null )
      {
        conn.disconnect();
      }
      try
      {
        if ( null != reader )
        {
          reader.close();
        }
      }
      catch( Throwable t )
      {
      }
    }

    return data;
  }

  private String getAwsBucketName()
  {
    return getSystemVariableService().getContextName() + "-" + getEnvironment() + "-static-content";
  }

  private String getAwsUrlLocationPrefix()
  {
    return getSystemVariableService().getContextName();
  }

}
