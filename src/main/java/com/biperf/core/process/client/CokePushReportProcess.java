
package com.biperf.core.process.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.process.BaseProcessImpl;
import com.biperf.core.service.client.CokeProcessService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.value.client.CokePushRptRecipientBean;
import com.biperf.core.utils.EncryptionUtils;
import com.biperf.core.utils.ParamConstants;
import com.biperf.core.value.client.CokePushReportLinkBean;
import java.util.Iterator;

public class CokePushReportProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "cokePushReportProcess";
  public static final String MESSAGE_NAME = "Coke Push Report Process Notification";

  private CokeProcessService cokeProcessService;
  private FileUploadStrategy appDataDirFileUploadStrategy;
  private FileUploadStrategy webdavFileUploadStrategy;

  private static final Log logger = LogFactory.getLog( CokePushReportProcess.class );


  public void onExecute()
  {
    logger.info( "processName: " + BEAN_NAME + " - Calling Coke Push Report Process " );
    addComment( "processName: " + BEAN_NAME + " - Calling Coke Push Report Process " );
    int nbrMessage = 0;
    int nbrError = 0;

    List<CokePushRptRecipientBean> processReportList = cokeProcessService.getPushProcessReports();
    List successRecipientList = new ArrayList();
    List failureRecipientList = new ArrayList();
    Set<Long> userIds = new HashSet<Long>();
    for ( CokePushRptRecipientBean cokePushRptRecipientBean : processReportList )
    {
        String reportFileName = getReportFileName( cokePushRptRecipientBean.getFileName() )  ;
        boolean fileMoved = moveFileToWebdav( reportFileName , cokePushRptRecipientBean.getReportFolder());
        successRecipientList.add( cokePushRptRecipientBean.getPrRecipientId() );
        userIds.add( cokePushRptRecipientBean.getUserId() );
     }
     
     for( Long userId : userIds )
     {
         List userReportDivisionList = cokeProcessService.getPushProcessDivisionsByUserId( userId );
         
         for( Iterator it =  userReportDivisionList.iterator();it.hasNext();) 
         {
         String divisionNumber =  (String)it.next();
         List<CokePushRptRecipientBean> userReportList = cokeProcessService.getPushProcessReportsByUserId( userId, divisionNumber );

       CokePushRptRecipientBean userPushRptRecipientBean  = new CokePushRptRecipientBean();
       List reportLinks = new ArrayList();
       for ( CokePushRptRecipientBean cokePushRptRecipientBean : userReportList )
       {
         userPushRptRecipientBean = cokePushRptRecipientBean;
         
         CokePushReportLinkBean linkBean = new CokePushReportLinkBean();
         linkBean.setReportName( cokePushRptRecipientBean.getReportName() );
         linkBean.setFileName( cokePushRptRecipientBean.getFileName() );
         
         reportLinks.add( linkBean );
       }
       String statusMsg = sendPustRepotMessage( userPushRptRecipientBean , reportLinks );

      if ( statusMsg.equals( "success" ) )
      {
        nbrMessage++;
        
      }
      else
      {
        failureRecipientList.add( userPushRptRecipientBean.getPrRecipientId() );
      }

         }

    }
    
     if( successRecipientList != null && successRecipientList.size() > 0)
     {
    	    int updatedRows = cokeProcessService.updatePushProcessRecipient( successRecipientList, "complete" );
     }
     if( failureRecipientList != null && failureRecipientList.size() > 0)
     {
     int updatedRows = cokeProcessService.updatePushProcessRecipient( successRecipientList, "msg_fail" );

     }
    logger.info( "processName: " + BEAN_NAME + " - Completed Calling Coke Push Report Process " );
    addComment( "processName: " + BEAN_NAME + " - Completed Calling Coke Push Report Process " );
    sendSummaryMessage( nbrMessage, nbrError );
  }

  private boolean moveFileToWebdav( String mediaUrl, String reportFolder )
  {
    try
    {
      byte[] media = appDataDirFileUploadStrategy.getFileDataforPushProcess( mediaUrl );
      String webdavUrl = "pushReport/"+ reportFolder +"/" + getEncryptedFileName( mediaUrl );
      webdavFileUploadStrategy.uploadFileData( webdavUrl, media );
      return true;
    }
    catch( Throwable e )
    {
      logger.error( "======upload failed=====" );
      // Must not have the file in AppDataDir of server executing this process
    }
    return false;
  }
  
  private boolean moveAdminFileToWebdav( String mediaUrl, String reportFolder )
  {
    try
    {
      byte[] media = appDataDirFileUploadStrategy.getFileDataforPushProcess( mediaUrl );
      String webdavUrl = "pushReport/"+ reportFolder +"/" + mediaUrl;
      webdavFileUploadStrategy.uploadFileData( webdavUrl, media );

      return true;
    }
    catch( Throwable e )
    {
      logger.error( "======upload failed=====" );
      // Must not have the file in AppDataDir of server executing this process
    }
    return false;
  }

  public String getReportFileName( String fullPath )
  {
    return fullPath.substring( fullPath.lastIndexOf( "/" ) + 1 );
  }

  public String getEncryptedFileName( String fileName )
  {
    fileName = fileName.substring( 0, fileName.indexOf( ".csv" ) );
    fileName =  EncryptionUtils.encryptValue( fileName, ParamConstants.ENCRYPTION_PASSWORD ) + ".csv";
    
    return fileName;
  }

  public String sendPustRepotMessage( CokePushRptRecipientBean cokePushRptRecipientBean, List<CokePushReportLinkBean> reportLinks )
  {
    String statusMsg = "success";

    Map<String, Object> objectMap = new HashMap<>();
    User reportUser = userService.getUserById( cokePushRptRecipientBean.getUserId() );
    objectMap.put( "reportFolder", cokePushRptRecipientBean.getReportFolder() );
    objectMap.put( "reportPeriod", cokePushRptRecipientBean.getReportPeriod() );
    objectMap.put( "fromDate", DateUtils.toDisplayDateString( cokePushRptRecipientBean.getFromDate(), LocaleUtils.getLocale( cokePushRptRecipientBean.getLanguageId() ) ) );

    objectMap.put( "toDate", DateUtils.toDisplayDateString( cokePushRptRecipientBean.getToDate(), LocaleUtils.getLocale( cokePushRptRecipientBean.getLanguageId() ) ) );
    objectMap.put( "recipientName", cokePushRptRecipientBean.getRecipientName() );
    objectMap.put( "divisionNumber", cokePushRptRecipientBean.getDivisionNumber() );
    objectMap.put( "divisionName", cokePushRptRecipientBean.getDivisionName() );
    //objectMap.put( "reportName", cokePushRptRecipientBean.getReportName() );
    StringBuffer sb = new StringBuffer();
    sb.append( "<ul>" );
    for( CokePushReportLinkBean reportLink : reportLinks)
    {
    sb.append( "<li>" );
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    String reportFileName = getReportFileName(  reportLink.getFileName() );
    String reportUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/' + "pushReport/" + cokePushRptRecipientBean.getReportFolder() + "/"+ getEncryptedFileName( reportFileName );
    logger.error( "reportUrlPath:::" +reportUrlPath );
    sb.append( " <a href=\" " );
    sb.append(reportUrlPath);
    sb.append( "\">" );
    sb.append( reportLink.getReportName());
    sb.append( "</a>" );
    sb.append( "</li>" );
    }


    // https://celebratingyoupprd.coke.com/celebratingyou-cm/cm3dam/pushReport

    sb.append( "</ul>" );
    objectMap.put( "reportLinks", sb );

    Mailing mailing = composeMail( MessageService.COKE_PUSH_REPORT_RECIPIENT_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    MailingRecipient mr = addRecipient( reportUser );
    mr.setLocale( cokePushRptRecipientBean.getLanguageId() );
    mailing.addMailingRecipient( mr );

    try
    {

      mailingService.submitMailing( mailing, objectMap );

      logger.debug( "--------------------------------------------------------------------------------" );
      logger.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      logger.debug( "Run By User: " + reportUser.getFirstName() + " " + reportUser.getLastName() );
      logger.debug( "--------------------------------------------------------------------------------" );

      addComment( "processName: " + BEAN_NAME + " email has been sent to: " + reportUser.getFirstName() + " " + reportUser.getLastName() );
    }
    catch( Exception e )
    {
      logger.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
      statusMsg = "failed";
    }

    return statusMsg;
  }

  private void sendSummaryMessage( int nbrMessages, int nbrErrors )
  {
	    User recipientUser = getRunByUser();    
	    
	    List<CokePushRptRecipientBean> processAdminReportList = cokeProcessService.getPushProcessReportsForAdmin();
	    CokePushRptRecipientBean cokePushRptRecipientBean = null;
	    Long noOfErrors = (long)nbrErrors;
	    String reportUrlPath ="";
	    
	    String reportFileName = "";

	    List adminRecipientList = new ArrayList();
	    if(processAdminReportList != null && processAdminReportList.size() > 0)
	    {
	      
	        cokePushRptRecipientBean = processAdminReportList.get( 0 );
	        reportFileName = getReportFileName( cokePushRptRecipientBean.getFileName() )  ;
	        boolean fileMoved = moveAdminFileToWebdav( reportFileName, cokePushRptRecipientBean.getReportFolder() );  
	        noOfErrors = cokePushRptRecipientBean.getErrorCount();
	        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
	        reportUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/' + "pushReport/" + cokePushRptRecipientBean.getReportFolder() + "/"+ reportFileName;
	        adminRecipientList.add( cokePushRptRecipientBean.getPrRecipientId() );
	        int updatedRows = cokeProcessService.updatePushProcessRecipient( adminRecipientList, "complete" );
	      }
 

    Map<String, Object> objectMap = new HashMap<>();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "noOfEmails", nbrMessages );
    objectMap.put( "noOfErrors", noOfErrors );
    objectMap.put( "reportLinks", reportUrlPath );

    /*
     * if ( success ) { objectMap.put( "success", "Completed without errors." ); } else {
     * objectMap.put( "success", "Completed with errors. " + resultMsg ); }
     */

    Mailing mailing = composeMail( MessageService.COKE_PUSH_REPORT_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    try
    {

      mailingService.submitMailing( mailing, objectMap );

      logger.debug( "--------------------------------------------------------------------------------" );
      logger.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      logger.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      logger.debug( "--------------------------------------------------------------------------------" );

      addComment( "processName: " + BEAN_NAME + " email has been sent to: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      
    }
    catch( Exception e )
    {
      logger.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public CokeProcessService getCokeProcessService()
  {
    return cokeProcessService;
  }

  public void setCokeProcessService( CokeProcessService cokeProcessService )
  {
    this.cokeProcessService = cokeProcessService;
  }

  public void setAppDataDirFileUploadStrategy( FileUploadStrategy appDataDirFileUploadStrategy )
  {
    this.appDataDirFileUploadStrategy = appDataDirFileUploadStrategy;
  }

  public void setWebdavFileUploadStrategy( FileUploadStrategy webdavFileUploadStrategy )
  {
    this.webdavFileUploadStrategy = webdavFileUploadStrategy;
  }

}
