
package com.biperf.core.ui.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sanselan.ImageReadException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.TcccClaimFileStatusType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PersonalInfoFileUploadValue;
import com.biperf.core.value.client.CokeNominationFileUploadValueBean;
import com.biperf.core.value.client.TcccClaimFileUploadResponse;
import com.biperf.core.value.client.TcccClaimFileUploadResponseProperties;
import com.biperf.core.value.client.TcccClaimFileValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * TcccClaimFileAction.
 * 
 * This class is created as part of Client Customization for WIP #39189
 * 
 * @author dudam
 * @since Nov 16, 2017
 * @version 1.0
 */
public class TcccClaimFileAction extends BaseRecognitionAction
{

  private static final Log logger = LogFactory.getLog(TcccClaimFileAction.class);
  
  public ActionForward uploadClaimFile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    TcccClaimFileUploadForm claimFileUploadForm = (TcccClaimFileUploadForm)form;
    String validationMessage = getValidionMessage( claimFileUploadForm, request );
    TcccClaimFileUploadResponseProperties view = new TcccClaimFileUploadResponseProperties();
    TcccClaimFileUploadResponse properties = new TcccClaimFileUploadResponse();
    List<String> claimFileUrls = null;
    if ( StringUtil.isNullOrEmpty( validationMessage ) )
    {
      String filename = generateUniqueFileName( claimFileUploadForm );
      PersonalInfoFileUploadValue data = new PersonalInfoFileUploadValue();
      int filesize = claimFileUploadForm.getClaimFile().getFileSize();
      TcccClaimFileValueBean claimFileValueBean = new TcccClaimFileValueBean();
      byte[] imageInByte;
      try
      {
        imageInByte = claimFileUploadForm.getClaimFile().getFileData();
        data.setId( UserManager.getUserId() );
        data.setData( imageInByte );
        data.setType( PersonalInfoFileUploadValue.TYPE_CLAIM );
        data.setName( filename );
        data.setSize( filesize );
        data.setInputStream( claimFileUploadForm.getClaimFile().getInputStream() );
        data = getClaimService().uploadClaimFile( data );
        claimFileValueBean.setFileName( filename );
        claimFileValueBean.setUrl( ImageUtils.getImageUploadPath() + data.getFull() );
        claimFileValueBean.setStatus( TcccClaimFileStatusType.lookup( TcccClaimFileStatusType.ACTIVE ) );
        properties.setSuccess( true );
      }
      catch( ImageReadException e )
      {
        log.error( "******Image read exception: " + e.getMessage() );
        properties.setMessage( CmsResourceBundle.getCmsBundle().getString( "client.nominationSubmit.UPLOAD_FAILED" ) );
      }
      catch( ServiceErrorException e )
      {
        log.error( "******Service error exception: " + e.getMessage() );
        properties.setMessage( CmsResourceBundle.getCmsBundle().getString( "client.nominationSubmit.UPLOAD_FAILED" ) );
      }

      catch( FileNotFoundException e )
      {
        log.error( "******File not found exception: " + e.getMessage() );
        properties.setMessage( CmsResourceBundle.getCmsBundle().getString( "client.nominationSubmit.UPLOAD_FAILED" ) );
      }
      catch( IOException e )
      {
        log.error( "******IO exception: " + e.getMessage() );
        properties.setMessage( CmsResourceBundle.getCmsBundle().getString( "client.nominationSubmit.UPLOAD_FAILED" ) );
      }
      if ( request.getSession().getAttribute( CLAIM_FILES ) == null )
      {
        claimFileUrls = new ArrayList<String>();
        claimFileUrls.add( claimFileValueBean.getUrl() );
        request.getSession().setAttribute( CLAIM_FILES, claimFileUrls );
      }
      else
      {
        claimFileUrls = (List<String>)request.getSession().getAttribute( CLAIM_FILES );
        claimFileUrls.add( claimFileValueBean.getUrl() );
        request.getSession().setAttribute( CLAIM_FILES, claimFileUrls );
      }
      properties.setFiles( claimFileUrls );
      view.setProperties( properties );
      writeAsJsonToResponse( view, response );
      return null;
    }
    else
    {
      properties.setMessage( validationMessage );
      properties.setFiles( claimFileUrls );
      view.setProperties( properties );
      writeAsJsonToResponse( view, response );
    }
    return null;
  }

  private String getValidionMessage( TcccClaimFileUploadForm form, HttpServletRequest request )
  {
    CokeNominationFileUploadValueBean valueBean = getCokeClientService().getNominationFileUploadDetails( form.getPromotionId() );
    String validFormats = StringUtil.isNullOrEmpty( valueBean.getAllowedFileTypes() )
        ? getSystemVariableService().getPropertyByName( SystemVariableService.COKE_UPLOAD_FILE_TYPES ).getStringVal()
        : valueBean.getAllowedFileTypes();
    if ( !isValidFileFormat( form.getClaimFile().getContentType(), valueBean.getAllowedFileTypes() ) )
    {
      return MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "client.nominationSubmit.INVALID_FORMAT" ), new Object[] { validFormats } );
    }
    else if ( !isMediaFileSizeValid( SystemVariableService.COKE_UPLOAD_FILE_SIZE_LIMIT, form.getClaimFile().getFileSize() ) )
    {
      return CmsResourceBundle.getCmsBundle().getString( "client.nominationSubmit.INVALID_FILE_SIZE" ) + " " + ImageUtils.getImageSize();
    }
    else
    {
      List<String> claimFileUrls = (List<String>)request.getSession().getAttribute( CLAIM_FILES );
      if ( claimFileUrls != null && claimFileUrls.size() > 0 )
      {
        if ( claimFileUrls.size() >= valueBean.getFileMaxNumber().intValue() )
        {
          return CmsResourceBundle.getCmsBundle().getString( "client.nominationSubmit.MAX_FILES_REACHED" ) + " " + ImageUtils.getImageSize();
        }
      }
    }
    return "";
  }

  private String generateUniqueFileName( TcccClaimFileUploadForm sendRecognitionForm )
  {
    String orginalfilename = sendRecognitionForm.getClaimFile().getFileName().replace( "#", "" );
    String reservedChars = getSystemVariableService().getPropertyByName( SystemVariableService.COKE_UPLOAD_FILE_REPALCE_CHARS ).getStringVal();
    String[] reservedCharsArray = reservedChars.split( "," );
    String newFileName = null;
    for ( String str : reservedCharsArray )
    {
      if ( orginalfilename.contains( str ) )
      {
        if ( str.equals( "+" ) )
        {
          newFileName = orginalfilename.replaceAll( "\\+", "" );
        }
        else if ( str.equals( "$" ) )
        {
          newFileName = orginalfilename.replaceAll( "\\$", "" );
        }
        else if ( str.equals( "^" ) )
        {
          newFileName = orginalfilename.replaceAll( "\\^", "" );
        }
        else if ( str.equals( ":" ) )
        {
          newFileName = orginalfilename.replaceAll( "\\:", "" );
        }
        else if ( str.equals( "*" ) )
        {
          newFileName = orginalfilename.replaceAll( "\\*", "" );
        }
        else if ( str.equals( "|" ) )
        {
          newFileName = orginalfilename.replaceAll( "\\|", "" );
        }
        else if ( str.equals( "?" ) )
        {
          newFileName = orginalfilename.replaceAll( "\\?", "" );
        }
        else
        {
          newFileName = orginalfilename.replaceAll( str, "" );
        }
        orginalfilename = newFileName;
      }
    }

    String fNameExt = orginalfilename;
    String fName = fNameExt.substring( 0, fNameExt.lastIndexOf( '.' ) );
    String fileType = getFileExtension( orginalfilename );
    if ( !StringUtil.isEmpty( fileType ) )
    {
      fileType = fileType.toLowerCase();
    }
    StringBuffer generatedfileName = new StringBuffer();
    generatedfileName.append( DateUtils.getCurrentDate().getTime() );
    generatedfileName.append( "_" );
    generatedfileName.append( fName );
    generatedfileName.append( "." );
    generatedfileName.append( fileType );
    return generatedfileName.toString();
  }

  private boolean isValidFileFormat( String contentType, String promotionSpecificValidFormats )
  {
    String fileExt = contentType;
    String[] fileNext = fileExt.split( "/" );
    String ext = fileNext[1];
    if ( fileNext[1].contains( "excel" ) )
    {
      ext = "xls";
    }
    else if ( fileNext[1].contains( "officedocument" ) || fileNext[1].contains( "msword" ) )
    {
      ext = "doc";
    }
    boolean valid = false;
    String validFormats = StringUtil.isNullOrEmpty( promotionSpecificValidFormats )
        ? getSystemVariableService().getPropertyByName( SystemVariableService.COKE_UPLOAD_FILE_TYPES ).getStringVal()
        : promotionSpecificValidFormats;
    log.error( "\nFile to process: " + ext.toLowerCase() + "\nValid accepted files: " + validFormats.toLowerCase() );
    if ( validFormats.toLowerCase().contains( ext.toLowerCase() ) )
    {
      valid = true;
      log.error( "\n*******File is valid***********" );
    }
    log.error( "\nValid status: " + valid );
    return valid;
  }

  private static boolean isMediaFileSizeValid( String systemVariableKey, int fileSize )
  {
    boolean valid = false;
    Long systemImageSizeLimitInMB = getSystemVariableService().getPropertyByName( systemVariableKey ).getLongVal();
    int systemImageSizeLimitInBytes = ImageUtils.MEGABYTES_TO_BYTES_MULTIPLIER * systemImageSizeLimitInMB.intValue();
    if ( fileSize < systemImageSizeLimitInBytes )
    {
      valid = true;
    }
    return valid;
  }

  public ActionForward deleteClaimFile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    TcccClaimFileUploadResponseProperties mainResponse = new TcccClaimFileUploadResponseProperties();
    TcccClaimFileUploadResponse properties = new TcccClaimFileUploadResponse();
    String deleteFileUrl = (String)request.getParameter( "fileUrl" );
    List<String> claimFileUrls = (List<String>)request.getSession().getAttribute( CLAIM_FILES );
    if ( !StringUtil.isNullOrEmpty( deleteFileUrl ) && claimFileUrls != null )
    {
      try
      {
        getClaimService().deleteClaimFile( deleteFileUrl );
        properties.setSuccess( true );
        claimFileUrls.remove( deleteFileUrl );
        request.getSession().setAttribute( CLAIM_FILES, claimFileUrls );
      }
      catch( Exception e )
      {
        properties.setSuccess( false );
      }
    }
    properties.setFiles( claimFileUrls );
    mainResponse.setProperties( properties );
    writeAsJsonToResponse( mainResponse, response );
    return null;
  }

  private static CokeClientService getCokeClientService()
  {
    return (CokeClientService)getService( CokeClientService.BEAN_NAME );
  }
  
}
