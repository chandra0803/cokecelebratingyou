
package com.biperf.core.ui.fileload;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestWrapper;

import com.biperf.core.domain.enums.GlobalFileType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingAttachmentInfo;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.fileprocessing.GlobalFileProcessingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.fileload.FileStageStrategyFactory;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.fileprocessing.OperationResultInfo;
import com.biperf.core.value.fileprocessing.WebDavFileWriteInfo;

public class GlobalFileUploadAction extends BaseDispatchAction
{
  private static final int MAX_FILE_SIZE = 524288000;// 500mMB 78643200
                                                     // ;//75MB 26214400; //
                                                     // 25MB
  private static final String XLSX = "xlsx";
  private static final String XLS = "xls";
  private static final String CSV = "csv";
  private static final String TXT = "txt";

  public ActionForward uploadGlobalFile( ActionMapping anActionMapping, ActionForm anActionForm, HttpServletRequest aRequest, HttpServletResponse aResponse ) throws Exception
  {
    // if this is a multipart request, wrap the HttpServletRequest object
    // with a
    // MultipartRequestWrapper to keep the process sub-methods from failing
    // when checking for
    // certain request parameters for command tokens and cancel button
    // detection
    if ( isMultipartRequest( aRequest ) )
    {
      aRequest = new MultipartRequestWrapper( aRequest );
    }

    GlobalFileUploadForm t_fileForm = (GlobalFileUploadForm)anActionForm;

    FormFile myFile = t_fileForm.getTheFile();

    ActionMessages errors = validate( t_fileForm );
    if ( !errors.isEmpty() )
    {
      saveErrors( aRequest, errors );
      return anActionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    String type = t_fileForm.getFileTypeCriteria();
    String fileName = myFile.getFileName();
    String userId = UserManager.getUserId().toString();
    InputStream inputFile = myFile.getInputStream();
    OperationResultInfo result = getFileProcessingService().process( type, fileName, inputFile, userId );

    // Map PURL template to DEPOSIT load
    if ( GlobalFileType.PURL.equals( type ) )
    {
      type = ImportFileTypeType.DEPOSIT;
    }

    // Load to stage, if no errors
    /*
     * if(result.getRowCountBad() == 0) { getFileStageStrategyFactory().getStrategy( type ).stage(
     * getValidFileOutURL(result.getOutputFileName()) ); }
     */

    String ifileType = null;
    String ifileName = null;
    String outputFileName = null;
    if ( t_fileForm != null )
    {
      ifileType = t_fileForm.getFileTypeCriteria();
      if ( GlobalFileType.PURL.equals( ifileType ) )
      {
        ifileType = ImportFileTypeType.DEPOSIT;
      }
      outputFileName = result.getOutputFileName();
      ifileName = getValidFileOutURL( result.getOutputFileName() );
    }

    PrintWriter pw = aResponse.getWriter();
    boolean stageSuccess = false;
    BigDecimal returnCode = new BigDecimal( "999" );
    try
    {
      if ( result.getRowCountBad() == 0 && isTokenValid( aRequest, true ) )
      {
        Map outParams = getFileStageStrategyFactory().getStrategy( ifileType ).stage( ifileName );
        pw.write( outParams.get( "p_out_returncode" ).toString() );
        returnCode = new BigDecimal( outParams.get( "p_out_returncode" ).toString() );
        if ( returnCode.compareTo( new BigDecimal( "0" ) ) == 0 )
        {
          stageSuccess = true;
        }
      }
      else
      {
        errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
        saveErrors( aRequest, errors );
      }
    }
    catch( Throwable t )
    {
      pw.write( getStackTraceAsString( t ) );
    }

    // Send an email
    sendEmail( UserManager.getUserId(), result.getRowCountBad() != 0, result.getBadOutputFileName(), outputFileName, stageSuccess, returnCode );

    return anActionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private String getValidFileOutURL( String outputFile )
  {
    StringBuilder builder = new StringBuilder();
    builder.append( getWorkWipPath() );
    builder.append( "/" );
    builder.append( getPrefix() );
    builder.append( "/" );
    builder.append( getSubFolderName() );
    if ( !AwsUtils.isAws() )
    {
      builder.append( "/valid" );
    }
    builder.append( "/" );
    builder.append( outputFile );
    log.error( " builder.toString() *****" + builder.toString() );

    return builder.toString();
  }

  private String getWorkWipPath()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.GLOBAL_FILE_PROCESSING_WORKWIP ).getStringVal();
  }

  private String getPrefix()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.GLOBAL_FILE_PROCESSING_PREFIX ).getStringVal();
  }

  private String getSubFolderName()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.GLOBAL_FILE_PROCESSING_SUBFOLDER ).getStringVal();
  }

  private void sendEmail( Long userId, boolean failure, String badFileName, String fileName, boolean stageSuccess, BigDecimal returnCode )
  {
    File file = null;
    if ( fileName != null && fileName.contains( "." ) )
    {
      fileName = fileName.substring( 0, fileName.lastIndexOf( "." ) );
    }
    if ( !StringUtils.isEmpty( badFileName ) )
    {
      file = new File( WebDavFileWriteInfo.getAppDataDir() + File.separator + badFileName );
    }
    User recipient = getUserService().getUserById( userId );
    Map objectMap = new HashMap();

    Mailing mailing = new Mailing();

    // Needs Guid due to lack of a business key
    mailing.setGuid( GuidUtils.generateGuid() );

    // Sender
    String sender = getSystemVariableService().getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();
    mailing.setSender( sender );

    // Delivery Date - Assumes Now (i.e. immediate delivery)
    Timestamp deliveryDate = new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate().getTime() );
    mailing.setDeliveryDate( deliveryDate );
    mailing.setMailingType( MailingType.lookup( MailingType.PROCESS_EMAIL ) );

    Message message = getMessageService().getMessageByCMAssetCode( MessageService.GLOBAL_FILE_UPLOAD_MESSAGE_CM_ASSET_CODE );
    mailing.setMessage( message );

    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    String localeCode = getSystemVariableService().getDefaultLanguage().getStringVal();
    if ( recipient.getLanguageType() != null )
    {
      localeCode = recipient.getLanguageType().getCode();
    }
    mailingRecipient.setLocale( localeCode );
    mailingRecipient.setUser( recipient );
    mailing.addMailingRecipient( mailingRecipient );

    // Is there a file to attach?

    if ( null != file && file.exists() && !AwsUtils.isAws() )
    {
      // Attach the file to the e-mail.
      MailingAttachmentInfo mailingAttachmentInfo = new MailingAttachmentInfo();
      mailingAttachmentInfo.setFullFileName( file.getAbsolutePath() );
      mailingAttachmentInfo.setMailing( mailing );
      mailingAttachmentInfo.setAttachmentFileName( file.getName() );
      mailing.addMailingAttachmentInfo( mailingAttachmentInfo );
    }
    else
    {
      objectMap.put( "success", "true" );
    }
    objectMap.put( "fileName", fileName );
    objectMap.put( "returnCode", returnCode );
    objectMap.put( "stageSuccess", stageSuccess );

    // Send the e-mail message.
    try
    {
      // mailingService.submitMailing( mailing, objectMap );
      getMailingService().submitMailingWithoutScheduling( mailing, objectMap );

      // process mailing
      getMailingService().processMailing( mailing.getId() );
      log.info( "Successfully sent email to  " + recipient.getFirstName() + " " + recipient.getLastName() + "." + " (mailing ID = " + mailing.getId() + ")" );

    }
    catch( Exception e )
    {
      String msg = new String( "An exception occurred while sending email. " + " (mailing ID = " + mailing.getId() + ")" );
      log.error( msg, e );
      throw new BeaconRuntimeException( "Error during submission of mail. The exception caused by: " + e.getCause().getMessage() );
    }

    // Delete file after emailing
    if ( null != file && file.exists() )
    {
      file.delete();
    }
  }

  private boolean isMultipartRequest( HttpServletRequest aRequest )
  {
    String contentType = aRequest.getContentType();
    String method = aRequest.getMethod();
    return contentType != null && contentType.startsWith( "multipart/form-data" ) && method.equals( "POST" );
  }

  private ActionMessages validate( GlobalFileUploadForm form )
  {
    ActionMessages errors = new ActionMessages();

    FormFile myFile = form.getTheFile();

    // Validate Extension
    String fileName = myFile.getFileName();
    if ( !fileName.toLowerCase().startsWith( form.getFileTypeCriteria().toLowerCase() + "_" ) )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "admin.fileload.common.FILE_INVALID" ) );
    }
    if ( fileName.lastIndexOf( '.' ) == -1 )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "admin.fileload.common.FILE_NO_EXTENSION" ) );
    }

    String extensions = fileName.substring( fileName.lastIndexOf( "." ) + 1 );
    if ( !extensions.equalsIgnoreCase( XLSX ) && !extensions.equalsIgnoreCase( XLS ) && !extensions.equalsIgnoreCase( CSV ) && !extensions.equalsIgnoreCase( TXT ) )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "admin.fileload.common.CONTENT_TYPE_INVALID" ) );
    }

    // Validate Size
    if ( myFile.getFileSize() == 0 )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "admin.fileload.common.FILE_SIZE_ZERO" ) );
    }
    else if ( myFile.getFileSize() > MAX_FILE_SIZE )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "admin.fileload.common.FILE_SIZE_EXCEEDED_LIMIT" ) );
    }

    return errors;
  }

  private GlobalFileProcessingService getFileProcessingService()
  {
    return (GlobalFileProcessingService)getService( GlobalFileProcessingService.BEAN_NAME );
  }

  private FileStageStrategyFactory getFileStageStrategyFactory()
  {
    return (FileStageStrategyFactory)BeanLocator.getBean( FileStageStrategyFactory.BEAN_NAME );
  }

  public SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  public UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  public MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

  public MailingService getMailingService()
  {
    return (MailingService)getService( MailingService.BEAN_NAME );
  }

  private String getStackTraceAsString( Throwable e )
  {
    StringWriter stackTrace = new StringWriter();
    e.printStackTrace( new PrintWriter( stackTrace ) );
    return stackTrace.toString();
  }

}
