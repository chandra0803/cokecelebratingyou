
package com.biperf.core.service.translation.impl;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.security.ws.rest.ConnectionFactory;
import com.biperf.core.service.TranslatedContent;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.translation.TranslationService;
import com.biperf.core.service.translation.UnexpectedTranslationException;
import com.biperf.core.service.translation.UnsupportedTranslationException;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.crypto.AESUtil;
import com.biperf.core.value.translation.TransResult;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TranslationServiceImpl implements TranslationService
{
  // ==============================================================================
  // ------------------------
  // New Translator Code Start
  // -------------------------
  private static final Log logger = LogFactory.getLog( TranslationServiceImpl.class );
  private SystemVariableService systemVariableService;

  static final String SALT = "JADCXEX5Kyr3SWvnGDtcT9YFebP38cp4";// 32-bit
  static final String PASSWORD = "TransPwd12abc";

  public TranslatedContent translate( LanguageType sourceLanguage, LanguageType targetLanguage, String content ) throws UnsupportedTranslationException, UnexpectedTranslationException
  {
    logger.info( "================ translate starting ===================" );
    TranslatedContent tc = new TranslatedContent( sourceLanguage, content, targetLanguage, null );
    ClientResponse resp = null;
    logger.info( "Before Translation= " + tc.toString() );

    try
    {
      // ---------------
      // Verify Parms
      // ---------------
      // parameters are missing or otherwise invalid - don't try to translate
      if ( sourceLanguage == null || targetLanguage == null || StringUtil.isEmpty( content ) )
      {
        logger.error( "Missing input parms.  Will not attempt to translate." );
        throw new BeaconRuntimeException( "Missing input parms.  Will not attempt to translate." + " sourceLanguage=" + sourceLanguage + " targetLanguage=" + targetLanguage + " content=" + content );
      }

      if ( sourceLanguage.getCode().trim().substring( 0, 2 ).equalsIgnoreCase( "zh" ) && targetLanguage.getCode().trim().substring( 0, 2 ).equalsIgnoreCase( "zh" )
          && sourceLanguage.getCode().trim().substring( 0, 5 ).equalsIgnoreCase( targetLanguage.getCode().trim().substring( 0, 5 ) ) )
      {
        // Both Chinese and match 5 chars
        // source and target language types are the same - don't bother translating
        logger.info( "Same Lang Code (5 chars). Will not attempt to translate" );
        tc = new TranslatedContent( sourceLanguage, content, targetLanguage, content );
        return tc;
      }

      if ( sourceLanguage.getCode().substring( 0, 2 ).equals( targetLanguage.getCode().substring( 0, 2 ) )
          && ! ( sourceLanguage.getCode().trim().substring( 0, 2 ).equalsIgnoreCase( "zh" ) && targetLanguage.getCode().trim().substring( 0, 2 ).equalsIgnoreCase( "zh" ) ) )
      {
        // Not Both Chinese and match 2 chars
        // source and target language types are the same - don't bother translating
        logger.info( "Same Lang Code (2 chars). Will not attempt to translate" );
        tc = new TranslatedContent( sourceLanguage, content, targetLanguage, content );
        return tc;
      }

      // ------------------------------
      // Call Translation Web Service
      // ------------------------------
      MultivaluedMap<String, String> params = new MultivaluedMapImpl();
      params.add( "srcLang", sourceLanguage.getCode() );
      params.add( "trgLang", targetLanguage.getCode() );
      params.add( "text", content );
      params.add( "billCode", systemVariableService.getPropertyByName( SystemVariableService.TRANSLATION_BILL_CODE ).getStringVal() );
      params.add( "mimeType", "text/html" );

      WebResource translationServiceWebResource = buildClient().resource( getTranslationBaseURI() );
      resp = translationServiceWebResource.path( "translation" ).queryParams( params ).type( MediaType.APPLICATION_JSON_TYPE + ";  charset=UTF-8" )
          .header( "Signature", getTranslationServicesEncryptedSignature() ).post( ClientResponse.class, " " );

      // ----------------------------
      // Check the Client Response
      // ----------------------------
      if ( resp == null )
      {
        logger.error( "Error in translation. ClientResponse is null " );
        throw new BeaconRuntimeException( "Error in translation. ClientResponse is null." );
      }
      else if ( resp.getStatus() == 200 )
      {
        logger.info( " ClientResponse is 200 - should be good to check values " );
        ObjectMapper mapper = buildObjectMapper();
        TransResult transResult = null;
        transResult = mapper.readValue( resp.getEntity( String.class ), TransResult.class );
        if ( transResult == null )
        {
          logger.error( "transResult is null." );
          throw new BeaconRuntimeException( "transResult is null" );
        }
        else
        {
          logger.info( transResult.toString() );
          if ( "0".equals( transResult.getResultCode() ) )
          {
            // --------
            // Success
            // --------
            tc = new TranslatedContent( sourceLanguage, content, targetLanguage, transResult.getTranslatedText() );
          }
          else if ( "28".equals( transResult.getResultCode() ) || "29".equals( transResult.getResultCode() ) )
          {
            throw new UnsupportedTranslationException( sourceLanguage, targetLanguage );
          }
          else
          {
            logger.error( "Error occurred during translation." + " sourceLanguage=" + sourceLanguage.getCode() + " targetLanguage=" + targetLanguage.getCode() + " transResult="
                + transResult.toString() );
            throw new BeaconRuntimeException( "Error occurred during translation." + " sourceLanguage=" + sourceLanguage.getCode() + " targetLanguage=" + targetLanguage.getCode() + " transResult="
                + transResult.toString() );
          }
        }
      }
      else
      {
        logger.error( "Error in translation. Bad ClientResponse status:" + resp.getStatus() + "     ClientResponse reason phrase:" + resp.getStatusInfo().getReasonPhrase() );
        throw new BeaconRuntimeException( "Error in translation. Bad ClientResponse status:" + resp.getStatus() + "     ClientResponse reason phrase:" + resp.getStatusInfo().getReasonPhrase() );
      }
    }
    catch( UnsupportedTranslationException ute )
    {
      logger.error( "ute=" + ute.toString() );
      throw ute;
    }
    catch( Throwable t )
    {
      logger.error( "t=" + t.toString() );
      throw new UnexpectedTranslationException( t );
    }
    finally
    {
      if ( resp != null )
      {
        resp.close();
      }
    }
    logger.info( "After Translation= " + tc.toString() );
    logger.info( "================ translate ending ===================" );
    return tc;
  }

  protected Client buildClient()
  {
    URLConnectionClientHandler ch = new URLConnectionClientHandler( new ConnectionFactory() );
    Client client = new Client( ch );
    return client;
  }

  public URI getTranslationBaseURI()
  {
    return UriBuilder.fromUri( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.TRANSLATION_SERVICE_URL_PREFIX ).getStringVal() ).build();
  }

  public static ObjectMapper buildObjectMapper()
  {
    ObjectMapper mapper = new ObjectMapper();
    // this is required if the Collection/List only has one element. Otherwise, this will cause a
    // deserialization Exception
    mapper.configure( DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true );
    mapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
    return mapper;
  }

  public String getTranslationServicesEncryptedSignature()
  {
    String signature = "";

    // To the timestamp we add a 4 digit random number
    int max = 9999;
    int min = 1000;
    Random random = new Random();
    int prefix = random.nextInt( max - min + 1 ) + min;
    int suffix = random.nextInt( max - min + 1 ) + min;
    signature = AESUtil.encryptValue( prefix + buildDateString() + suffix, SALT, PASSWORD );

    logger.info( "signature=" + signature );
    return signature;
  }

  public static String buildDateString()
  {
    SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
    formatter.setTimeZone( TimeZone.getTimeZone( "GMT" ) );
    String dateString = formatter.format( new Date() );

    return dateString;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

}
