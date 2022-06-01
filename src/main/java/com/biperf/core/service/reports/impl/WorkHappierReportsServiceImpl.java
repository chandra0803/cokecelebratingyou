
package com.biperf.core.service.reports.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.reports.WorkHappierReportsDAO;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingAttachmentInfo;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.reports.WorkHappierReportsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.FileExtractUtils;
import com.biperf.core.utils.GuidUtils;

public class WorkHappierReportsServiceImpl implements WorkHappierReportsService
{
  private static final Log logger = LogFactory.getLog( WorkHappierReportsServiceImpl.class );
  private static final String UnixFileSeparator = "/";
  private static final String WindowsFileSeparator = "\\";

  private WorkHappierReportsDAO workHappierReportsDAO;
  private SystemVariableService systemVariableService;
  private MailingService mailingService;
  private MessageService messageService;
  private UserService userService;

  public WorkHappierReportsDAO getWorkHappierReportsDAO()
  {
    return workHappierReportsDAO;
  }

  public void setWorkHappierReportsDAO( WorkHappierReportsDAO workHappierReportsDAO )
  {
    this.workHappierReportsDAO = workHappierReportsDAO;
  }

  @Override
  public Map<String, Object> getConfidentialFeedbackReportExtract( Map<String, Object> reportParameters )
  {
    return workHappierReportsDAO.getConfidentialFeedbackReportExtract( reportParameters );
  }

  @Override
  public Map<String, Object> getHappinessPulseReportExtract( Map<String, Object> reportParameters )
  {
    return workHappierReportsDAO.getHappinessPulseReportExtract( reportParameters );
  }

  @Override
  public void generateAndSendEmailWorkHappierReportExtract( Map<String, Object> extractParameters ) throws ServiceErrorException
  {
    Map<String, Object> output = workHappierReportsDAO.getWorkHappierReportExtract( extractParameters );

    List<String> valueBeanList = (List<String>)output.get( "p_out_result_set" );
    try
    {
      if ( valueBeanList != null && !valueBeanList.isEmpty() )
      {
        emailExtract( valueBeanList );
      }
    }
    catch( Exception e )
    {
      logger.error( "Error generating and sending email extract for WorkHappier Report Extract" + e );
    }
  }

  private void emailExtract( List<String> valueBeanList )
  {
    List<String> rowsReturned = valueBeanList;
    if ( rowsReturned.size() > 1 )
    {
      File savedFile = writeFile( rowsReturned );

      String absolutePath = "";
      long fileLength = 0;
      if ( savedFile != null )
      {
        absolutePath = savedFile.getAbsolutePath();
      }

      if ( doesFileExists( absolutePath ) )
      {
        sendMessage( absolutePath, savedFile.getName() );

        // Purge the file after email has been sent
        savedFile.delete();
      }
      else
      {
        if ( savedFile != null )
        {
          fileLength = savedFile.length();
        }
        String msg = new String( "Problem accessing the file attachment while executing the " + " WorkHappier Report Extract" + ". File:" + absolutePath + " File size:" + printFileSize( fileLength )
            + " The email has not been sent to the participant. Process ended.  " );
        logger.warn( msg );
      }
    }
    else
    {
      // No file attachment, just send the email.
      sendMessage( null, null );

      String msg = new String( "Problems with emailing WorkHappier Report Extract - No data found." );
      logger.warn( msg );
    }
  }

  /**
   * Sends an e-mail message to the admin the file attachment of the
   * data extract
   */
  public void sendMessage( String fullFileName, String attachmentFileName )
  {
    Map<String, Object> objectMap = new HashMap<String, Object>();

    Mailing mailing = composeMail( MessageService.WORK_HAPPIER_REPORT_EXTRACT_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    String emailAddress = Environment.ENV_PROD.equals( Environment.getEnvironment() ) ? "WHextracts@biworldwide.com" : "bicore_admin@biworldwide.com";
    mailing.addMailingRecipient( addRecipient( emailAddress ) );

    if ( fullFileName == null )
    {
      objectMap.put( "noDataFound", true );
    }
    else
    {
      mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, fullFileName, attachmentFileName ) );
    }

    try
    {
      mailing = mailingService.submitMailingWithoutScheduling( mailing, objectMap );

      mailingService.processMailing( mailing.getId() );

      String msg = new String( " WorkHappier Report Extract " + " email message sent to " + emailAddress + " (mailing ID = " + mailing.getId() + ")" );

      logger.info( msg );
    }
    catch( Exception e )
    {
      String msg = new String( "An exception occurred while sending a " + " WorkHappier Report Extract " + " (mailing ID = " + mailing.getId() + ")" );
      logger.error( msg, e );
    }
  }

  /**
   * Adds the message by name and mailing type to a mailing object
   * 
   * @param cmAssetCode
   * @param mailingType
   * @return a mailing object that is mostly assembled, except for the mailingRecipient(s)
   */
  protected Mailing composeMail( String cmAssetCode, String mailingType )
  {
    Mailing mailing = composeMail();

    mailing.setMailingType( MailingType.lookup( mailingType ) );

    Message message = messageService.getMessageByCMAssetCode( cmAssetCode );
    mailing.setMessage( message );

    return mailing;
  }

  /**
   * Creates a new mailing object and add Guid, Sender and Delivery Date to it
   * 
   * @return a partially assembled mailing object
   */
  private Mailing composeMail()
  {
    Mailing mailing = new Mailing();

    // Needs Guid due to lack of a business key
    mailing.setGuid( GuidUtils.generateGuid() );

    // Sender
    String sender = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();
    mailing.setSender( sender );

    // Delivery Date - Assumes Now (i.e. immediate delivery)
    Timestamp deliveryDate = new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate().getTime() );
    mailing.setDeliveryDate( deliveryDate );

    return mailing;

  }

  /**
   * Takes in a user and returns a mailing recipient object suitable for mailing service
   * 
   * @param recipient
   * @return a mailingRecipient object
   */
  protected MailingRecipient addRecipient( String emailAddress )
  {
    MailingRecipient mailingRecipient = mailingService.buildPreviewMailingRecipient( emailAddress );

    return mailingRecipient;
  }

  /**
   * @param mailing
   * @param fullFileName - this is the absolute path of the file (internal to the system incl.
   *          directory names)
   * @param attachmentFileName - this is the name used on the file attachment in the email (visible
   *          to users)
   * @return MailingAttachmentInfo
   */
  protected MailingAttachmentInfo addMailingAttachmentInfo( Mailing mailing, String fullFileName, String attachmentFileName )
  {
    MailingAttachmentInfo mailingAttachmentInfo = new MailingAttachmentInfo();
    mailingAttachmentInfo.setFullFileName( fullFileName );
    mailingAttachmentInfo.setAttachmentFileName( attachmentFileName );
    mailingAttachmentInfo.setMailing( mailing );
    return mailingAttachmentInfo;
  }

  private File writeFile( List results )
  {
    String fileName = "";
    String failureMsg = "";
    File extractFile = null;
    String extractLocation = getExtractLocation();
    try
    {
      // Create an unique filename for the extract file.
      fileName = generateUniqueFileName();

      FileExtractUtils.createDirIfNeeded( extractLocation );

      // Writes a file with the resultset from stored proc.
      extractFile = new File( extractLocation, fileName );

      // Build failure message just in case.
      failureMsg = new String( "An exception occurred while attempting to write file for " + " WorkHappier Report Extract " + " to File:" + extractFile.getAbsolutePath() );

      boolean success = extractFile.createNewFile();
      if ( success )
      {
        BufferedWriter writer = new BufferedWriter( new FileWriter( extractFile ) );

        for ( int i = 0; i < results.size(); i++ )
        {
          String row = (String)results.get( i );
          writer.write( row );
          writer.newLine();
        }
        writer.close();

        String msg = new String( "Executing the " + " WorkHappier Report Extract File:" + extractFile.getAbsolutePath() + " has been saved successfully." + " File size:"
            + printFileSize( extractFile.length() ) );
        logger.info( msg );
      }
      else
      {
        logger.error( failureMsg );
      }
    }
    catch( IOException e )
    {
      logger.error( failureMsg, e );
    }
    catch( Exception e )
    {
      logger.error( failureMsg, e );
    }
    return extractFile;
  }

  private String getExtractLocation()
  {
    String extractLocation = null;

    // On Windows this is \ On Unix this is /
    String currentSystemFileSeparator = File.separator;

    // user defined path where the csv file should be saved. i.e. /tmp/ on Unix
    extractLocation = System.getProperty( "appdatadir" );
    // make sure the user defined directory works with the current system
    if ( !StringUtils.isBlank( extractLocation ) )
    {
      // e.g. Developers running on localhosts on Windows
      // but the system variable specifies an Unix file separator
      if ( extractLocation.indexOf( UnixFileSeparator ) >= 0 && currentSystemFileSeparator.equals( WindowsFileSeparator ) )
      {
        extractLocation = extractLocation.replace( '/', '\\' );
      }
      // e.g. QA, PPRD, PROD - CTECH environments running on Unix
      // but the system variable specifies a Windows file separator
      if ( extractLocation.indexOf( WindowsFileSeparator ) >= 0 && currentSystemFileSeparator.equals( UnixFileSeparator ) )
      {
        extractLocation = extractLocation.replace( '\\', '/' );
      }
    }

    return extractLocation;
  }

  private String generateUniqueFileName()
  {
    StringBuffer fileName = new StringBuffer();
    fileName.append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    fileName.append( "_" );
    fileName.append( "WorkHappierReportExtract" );
    fileName.append( "_" );
    fileName.append( DateUtils.getCurrentDate().getTime() );
    fileName.append( ".csv" );
    return fileName.toString();
  }

  /**
   * Returns a formatted string displaying the size in words such as KB/MB/GB/more
   * 
   * @param lengthInBytes
   * @return a string representing the length of the file.
   */
  protected String printFileSize( long lengthInBytes )
  {
    float lengthInFloat = (float)lengthInBytes;
    String fileSize = "";
    final int kilobyte = 1024;
    final int megabyte = 1024 * kilobyte;
    final int gigabyte = 1024 * megabyte;

    if ( lengthInFloat < kilobyte )
    {
      fileSize = lengthInFloat + " Bytes";
    }
    else if ( lengthInFloat < megabyte )
    {
      fileSize = "About " + new Integer( Math.round( lengthInFloat / kilobyte ) ).toString() + " KB";
    }
    else if ( lengthInFloat < gigabyte )
    {
      fileSize = "About " + new Integer( Math.round( lengthInFloat / megabyte ) ).toString() + " MB";
    }
    else if ( lengthInFloat < gigabyte * 1024 )
    {
      fileSize = "About " + new Integer( Math.round( lengthInFloat / gigabyte ) ).toString() + " GB";
    }
    else
    {
      fileSize = "File Too Large - in Terabyte! File size in bytes:" + fileSize;
    }

    return fileSize;
  }

  /**
   * Determine if the file exists in the file system
   * 
   * @param fileName
   * @return true if the file exists on the file system
   */
  protected boolean doesFileExists( String fileName )
  {
    if ( !fileName.equals( "" ) )
    {
      File fileOrDir = new File( fileName );

      if ( fileOrDir.exists() && fileOrDir.isFile() )
      {
        // File exists
        return true;
      }
    }
    // File does not exist or is not a File
    return false;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }
}
