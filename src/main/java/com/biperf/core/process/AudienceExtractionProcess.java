/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/AudienceExtractionProcess.java,v $
 */

package com.biperf.core.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.FileExtractUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.FormattedValueBean;

/**
 * AudienceExtractionProcess.
 * 
 * Email to the RunByUser a .csv file containing a list of primary audience of a specific promotion
 * suitable to be used as an import file for Opinio's Import Invitees from File functionality.
 * 
 * This process is automatically triggered by PromotionOverviewAction.markAsComplete() for Survey Promotion
 * but it can be launched/scheduled manually for any promotion.
 * 
 * If a client uses this process, it is recommended to also schedule "MailingAttachmentCleanupProcess"
 * to run periodically to delete the files saved on the App Server as a result of this process.
 * 
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Tammy Cheng</td>
 * <td>Aug 29, 2006</td>
 * <td>1.1</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */
public class AudienceExtractionProcess extends BaseProcessImpl
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The name used to get an object of this class from the Spring application context.
   */
  public static final String BEAN_NAME = "audienceExtractionProcess";

  /**
   * The name of the e-mail message associated with this process.
   */
  public static final String EMAIL_MESSAGE_NAME = "Audience Extraction Process";
  public static final String FILE_DELIMITER = ",";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final Log log = LogFactory.getLog( AudienceExtractionProcess.class );

  private AudienceService audienceService;
  private PromotionService promotionService;
  private ParticipantService participantService;

  /**
   * The ID of the promotion whose audience this process will extract.
   */
  private Long promotionId;

  // ---------------------------------------------------------------------------
  // Process Methods
  // ---------------------------------------------------------------------------

  /**
   * Extracts the audience of a specified promotion and emails the file to the runByUser.
   */
  protected void onExecute()
  {
    Set paxs = new HashSet();

    // Get the specified promotion used in the process parameter.
    Promotion promotion = promotionService.getPromotionById( promotionId );

    if ( promotion != null )
    {
      // Retrieves a list of primary audience participants who are eligible for the given promotion.
      paxs.addAll( participantService.getAllEligiblePaxForPromotion( promotion.getId(), true ) );

      // Save the file that contains the audience to be emailed.
      File savedFile = writeOpinioFile( paxs );

      if ( savedFile != null && doesFileExists( savedFile.getAbsolutePath() ) )
      {
        // Email the audience extract to the user who triggered this process.
        sendMessage( promotion, savedFile.getAbsolutePath(), savedFile.getName() );
      }
      else
      {
        String msg = null;
        if ( savedFile != null )
        {
          msg = new String( "Problem accessing the file attachment while executing the " + EMAIL_MESSAGE_NAME + ". File:" + savedFile.getAbsolutePath() + " File size:"
              + printFileSize( savedFile.length() ) + " The email has not been sent. Process ended.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
          log.warn( msg );
          addComment( msg );
        }
        else
        {
          msg = new String( "Failed to create file while executing the " + EMAIL_MESSAGE_NAME );
        }
      }
    }
    // Invalid promotion specified.
    else
    {
      String msg = new String( EMAIL_MESSAGE_NAME + " ended." + " Promotion used is invalid. Promotion Id: " + promotionId + " (process invocation ID = " + getProcessInvocationId() + ")" );
      log.warn( msg );
      addComment( msg );
    }
  }

  /**
   * Constructs and saves the file to the current directory on the app server.
   * 
   * Note: This is designed per the specs of Opinio acceptable file format (see below).
   * Cautions should be taken when changing this method!
   * 
   * Rules Per Opino, "import invitees from file"
   * 
   * 0. The file must be a simple text file with the following format:
   *    email, name, attribute-name1, attribute-name2, attribute-name3, ...
   *    joe@smth.com, Joe Smith, abc, cde, xyz, ...
   * 1. The first line of the file sets the label of the attributes and is optional.
   * 2. If the line with labels is not included, the first column must be valid emails.
   *    The second column must be invitee names. If names are not included in the data,
   *    an additional delimiter must be included to indicate that names are absent (this
   *    is only required if there are extra attributes in addition to email and name). The
   *    attributes following the names will be automatically labeled "attribute1", "attribute2",
   *    "attribute3" if included. 
   * 3. If a line with labels is included, the labels "email" and "name" are reserved. Label
   *    "email" must be included, and "name" is optional. The position of the name/email can be
   *    anywhere on the line. The position the data in the lines following the labels must match
   *    the order of the labels.
   * 4. There are no limit to the number of attributes when uploading a file with invitees. The
   *    only limitation is that each data element cannot be longner than 255 characters.
   * 5. The invitee data delimiter is comma, although it can be changed before importing in Opino,
   *    this method is expecting the delimiter to be comma.
   * 
   * @return the file saved
   */
  private File writeOpinioFile( Set paxs )
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

      // Writes a file with the participants extracted.
      extractFile = new File( extractLocation, fileName );

      // Build failure message just in case.
      failureMsg = new String( "An exception occurred while attempting to write file for " + EMAIL_MESSAGE_NAME + " Promotion ID: " + promotionId + " to File:" + extractFile.getAbsolutePath()
          + "(process invocation ID = " + getProcessInvocationId() + ")" );

      boolean success = extractFile.createNewFile();
      if ( success )
      {
        BufferedWriter writer = new BufferedWriter( new FileWriter( extractFile ) );

        // header row.
        writer.write( "email" + FILE_DELIMITER + "name" + FILE_DELIMITER + "loginId" );
        writer.newLine();

        for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
        {
          Object temp = iter.next();
          Participant pax = null;
          // paxs might be AudienceParticipant objects or Participants or FormattedValueBeans.
          try
          {
            pax = (Participant)temp;
          }
          catch( ClassCastException cce )
          {
            try
            {
              pax = ( (AudienceParticipant)temp ).getParticipant();
            }
            catch( ClassCastException e )
            {
              try
              {
                pax = participantService.getParticipantById( ( (FormattedValueBean)temp ).getId() );
              }
              catch( ClassCastException ex )
              {
                // the object was not a Participant, FormattedValueBean or AudienceParticipant so we
                // don't care about it
                continue;
              }
            }
          }
          // data row. format: emailAddress,firstName LastName,loginId
          String emailAddress = new String( "" );
          String firstName = new String( "" );
          String lastName = new String( "" );
          String fullName = new String( "" );
          if ( pax != null )
          {
            if ( pax.getPrimaryEmailAddress() != null )
            {
              emailAddress = pax.getPrimaryEmailAddress().getEmailAddr();
            }
            firstName = pax.getFirstName();
            lastName = pax.getLastName();
            fullName = firstName + " " + lastName;
            String row = StringUtil.truncateStringToLength( emailAddress, 254 ) + FILE_DELIMITER + StringUtil.truncateStringToLength( fullName, 254 ) + FILE_DELIMITER + pax.getUserName();
            writer.write( row );
            writer.newLine();
          }
        }
        writer.close();

        String msg = new String( "Executing the " + EMAIL_MESSAGE_NAME + " Promotion ID: " + promotionId + ". File:" + extractFile.getAbsolutePath() + " has been saved successfully." + " File size:"
            + printFileSize( extractFile.length() ) + " (process invocation ID = " + getProcessInvocationId() + ")" );
        log.info( msg );
        addComment( msg );
      }
      else
      {
        log.error( failureMsg );
        addComment( failureMsg );
      }
    }
    catch( IOException e )
    {
      log.error( failureMsg, e );
      addComment( failureMsg );
    }
    catch( Exception e )
    {
      log.error( failureMsg, e );
      addComment( failureMsg );
    }
    return extractFile;
  }

  /**
   * Creates a .csv file name that is unique to: - the client name - the process name - the current datetime.
   * 
   * @return an unique file name
   */
  private String generateUniqueFileName()
  {
    StringBuffer fileName = new StringBuffer();
    fileName.append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    fileName.append( "_" );
    fileName.append( BEAN_NAME );
    fileName.append( "_" );
    fileName.append( DateUtils.getCurrentDate().getTime() );
    fileName.append( ".csv" );
    return fileName.toString();
  }

  /**
   * Sends an e-mail message to the user that launched this process 
   * with a file attachment of a list of audience selected for the given
   * promotion, and that the extract audience process has finished.
   */
  private void sendMessage( Promotion promotion, String fullFileName, String attachmentFileName )
  {
    User runByUser = getRunByUser();

    // Collect parameters for personalization.
    Map objectMap = new HashMap();

    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "firstName", runByUser.getFirstName() );
    objectMap.put( "lastName", runByUser.getLastName() );
    objectMap.put( "promotionName", promotion != null ? promotion.getName() : "" );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the e-mail message.
    Mailing mailing = composeMail( MessageService.AUDIENCE_EXTRACTION_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROMOTION );
    mailing.addMailingRecipient( addRecipient( runByUser ) );

    // Is there a file to attach?
    if ( fullFileName == null )
    {
      objectMap.put( "noDataFound", "true" );
    }
    else
    {
      // Attach the file to the e-mail.
      mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, fullFileName, attachmentFileName ) );
    }

    // Send the e-mail message.
    try
    {
      mailing = mailingService.submitMailingWithoutScheduling( mailing, objectMap );

      // process mailing
      mailingService.processMailing( mailing.getId() );

      log.debug( "------------------------------------------------------------------------------" );
      log.debug( "Process " + BEAN_NAME + " sent an email message to " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." );
      log.debug( "Process " + BEAN_NAME + " was run by user " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." );
      log.debug( "------------------------------------------------------------------------------" );

      addComment( "Process " + BEAN_NAME + " sent an email message to " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending a " + EMAIL_MESSAGE_NAME + " email message. " + "(process invocation ID = " + getProcessInvocationId() + ")", e );

      addComment( "An exception occurred while sending a " + EMAIL_MESSAGE_NAME + " email message. " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }
}
