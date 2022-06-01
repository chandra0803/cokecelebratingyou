
package com.biperf.core.service.participant.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.participant.UserCountryChangesDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingAttachmentInfo;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserCountryChanges;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCountryChangesService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.FileExtractUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.CampaignTransferValueBean;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.objectpartners.cms.util.ContentReaderManager;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

public class UserCountryChangesServiceImpl implements UserCountryChangesService
{
  private static final Log logger = LogFactory.getLog( UserCountryChangesServiceImpl.class );

  public static final String NO_CAMPAIGN_MOVE_MESSAGE = "No Campaign move";
  public static final String CAMPAIGN_MOVE_NO_BALANCE_MESSAGE = "Campaign move-No Balance";

  public static final String PROCESS_BEAN_NAME = "campaignTransferProcess";
  public static final String PROCESS_MESSAGE_NAME = "Campaign Changes";

  public static final String UnixFileSeparator = "/";
  public static final String WindowsFileSeparator = "\\";
  public static final String FILE_DELIMITER = ",";

  private UserCountryChangesDAO userCountryChangesDAO;
  private UserDAO userDAO;
  private UserService userService;
  private ParticipantService participantService;
  private AwardBanQServiceFactory awardBanQServiceFactory;
  private MailingService mailingService;
  private MessageService messageService;
  private SystemVariableService systemVariableService;
  private ClaimDAO claimDAO;
  private ActivityDAO activityDAO;

  public List getUsersToMoveBalance()
  {
    return getUserCountryChangesDAO().getUsersToMoveBalance();
  }

  public UserCountryChanges saveUserCountryChanges( UserCountryChanges userCountryChanges ) throws ServiceErrorException
  {
    return getUserCountryChangesDAO().saveUserCountryChanges( userCountryChanges );
  }

  private Participant enrollParticipantInBanq( Participant participant ) throws ServiceErrorException
  {
    try
    {
      return awardBanQServiceFactory.getAwardBanQService().enrollParticipantInAwardBanQWebService( participant );
    }
    catch( JsonGenerationException e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
    catch( JsonMappingException e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
    catch( IOException e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
  }

  /******************************************************************************************
   *  start of campaign move process
   *   
   ******************************************************************************************
   */

  public boolean processCampaignTransfers( UserCountryChanges userCountryChanges ) throws ServiceErrorException
  {
    UserCountryChanges userCChanges = processTransfer( userCountryChanges );
    if ( userCChanges != null )
    {
      return true;
    }
    return false;
  }

  private UserCountryChanges processTransfer( UserCountryChanges userCountryChanges ) throws ServiceErrorException
  {
    // pax who already updated their campaign through update screen
    Participant participant = participantService.getParticipantById( userCountryChanges.getUserId() );
    UserCountryChanges updatedUserCountryChanges = updateNonGlobalAssignee( userCountryChanges, participant );
    return updatedUserCountryChanges;
  }

  private UserCountryChanges updateNonGlobalAssignee( UserCountryChanges userCountryChanges, Participant participant ) throws ServiceErrorException
  {
    UserCountryChanges updatedUserCountryChanges = null;
    // get balance by old award banq number stored in the user country changes table.
    Long balanceToMove = new Long( 0 );
    String message = null;

    // pax with campaign move
    String oldAwardbanqNbrDec = userCountryChangesDAO.getDecryptedValue( userCountryChanges.getOldAwardBanqNbr() );
    balanceToMove = oldAwardbanqNbrDec != null ? getBalanceForNonGlobalAssingee( participant.getId(), oldAwardbanqNbrDec ) : null;
    if ( !isBalanceToMove( balanceToMove ) ) // if there is NO balance to move between campaigns
    {
      message = CAMPAIGN_MOVE_NO_BALANCE_MESSAGE;
    }
    Participant pax = resetAwardBanqInfo( participant );
    updatedUserCountryChanges = updateUserCountryChangesWithBanqInfo( userCountryChanges, balanceToMove, pax, message );

    return updatedUserCountryChanges;
  }

  private boolean isBalanceToMove( Long balanceToMove )
  {
    if ( balanceToMove == null || balanceToMove != null && balanceToMove.longValue() == 0 )
    {
      return false;
    }
    return true;
  }

  private Long getBalanceForNonGlobalAssingee( Long participantId, String accountNumber ) throws ServiceErrorException
  {
    Long balanceToMove = new Long( 0 );
    try
    {
      balanceToMove = awardBanQServiceFactory.getAwardBanQService().getAccountBalanceForParticipantWebService( participantId, accountNumber );
    }
    catch( Exception e )
    {
      logger.error( "Error getting balance from awardbanq for user " + participantId + " Exception: " + e );
    }
    return balanceToMove;
  }

  public Participant resetAwardBanqInfo( Participant participant )
  {
    // store banq info to a placeholder, otherwise hibernate commits participant with empty bank
    // info even there is a fail in the transaction.
    // It doesn't rollback participant bank info
    participant.setAwardBanqNumberStored( participant.getAwardBanqNumber() );
    participant.setCentraxIdStored( participant.getCentraxId() );
    participant.setAwardBanqExtractDateStored( participant.getAwardBanqExtractDate() );

    participant.setAwardBanqNumber( null );
    participant.setCentraxId( null );
    participant.setAwardBanqExtractDate( null );

    return participant;
  }

  private UserCountryChanges updateUserCountryChangesWithBanqInfo( UserCountryChanges userCountryChanges, Long balanceToMove, Participant participant, String message ) throws ServiceErrorException
  {
    boolean isUpdate = false;
    boolean success = true;
    UserCountryChanges updatedUserCountryChanges = null;

    // new country
    Country country = participant.getPrimaryAddress().getAddress().getCountry();

    if ( StringUtils.isEmpty( participant.getAwardBanqNumber() ) )
    {
      UserCountryChanges oldUserCountryChanges = null;
      if ( country != null )
      {
        oldUserCountryChanges = getUserByOldCampaign( country.getCampaignNbr(), participant.getId() );
      }
      if ( oldUserCountryChanges != null )
      {
        isUpdate = true;
      }

      try
      {
        if ( isUpdate )
        {
          participant = updateParticipantWithExistingBanqInfo( participant, oldUserCountryChanges );
        }
        else
        {
          participant = enrollParticipantInBanq( participant );
        }
      }
      catch( ServiceErrorException e )
      {
        logger.error( "Error enrolling/updating participant to AwardBanq, participant id: " + participant.getId() + " exception: ", e );
        success = false;
        // reset these with stored bank info. otherwise hibernate commits even on transaction
        // failure
        participant.setAwardBanqNumber( participant.getAwardBanqNumberStored() );
        participant.setCentraxId( participant.getCentraxIdStored() );
        participant.setAwardBanqExtractDate( participant.getAwardBanqExtractDateStored() );
        if ( e.getServiceErrors() != null )
        {
          String errorMessage = e.getServiceErrors().get( 0 ).toString();
          if ( e.getServiceErrors().get( 0 ).toString().contains( "[-20] and description of [The MS Par # is not unique for this campaign" ) )
          {
            mailingService.submitSystemMailing( "Enrollment Failure for " + participant.getLastName() + "," + participant.getFirstName(), errorMessage, errorMessage );
          }
        }
      }
    }

    if ( success )
    {
      if ( isUpdate )
      {
        participantService.saveParticipant( participant );
      }
      userCountryChanges.setBalanceToMove( balanceToMove );
      userCountryChanges.setProcessed( true );
      userCountryChanges.setNewCountryId( country.getId() );
      userCountryChanges.setNewCampaignNbr( country.getCampaignNbr() );
      String awardbanqNbrEnc = userCountryChangesDAO.getEncryptedValue( participant.getAwardBanqNumber() );
      userCountryChanges.setNewAwardBanqNbr( awardbanqNbrEnc );
      String centraxIdEnc = userCountryChangesDAO.getEncryptedValue( participant.getCentraxId() );
      userCountryChanges.setNewCentraxId( centraxIdEnc );
      userCountryChanges.setMessage( message );
      updatedUserCountryChanges = saveUserCountryChanges( userCountryChanges );
    }
    return updatedUserCountryChanges;
  }

  private Participant updateParticipantWithExistingBanqInfo( Participant participant, UserCountryChanges userCountryChanges ) throws ServiceErrorException
  {
    String oldAwardbanqNbrDec = userCountryChangesDAO.getDecryptedValue( userCountryChanges.getOldAwardBanqNbr() );
    participant.setAwardBanqNumber( oldAwardbanqNbrDec );
    participant.setAwardBanqExtractDate( new Date() );
    String oldCentraxIdDec = userCountryChangesDAO.getDecryptedValue( userCountryChanges.getOldCentraxId() );
    participant.setCentraxId( oldCentraxIdDec );
    try
    {
      return awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( participant );
    }
    catch( JsonGenerationException e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
    catch( JsonMappingException e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
    catch( UniformInterfaceException e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
    catch( ClientHandlerException e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
    catch( IOException e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
      throw new ServiceErrorException( e.getMessage() );
    }
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

  /**
   * Takes in a user and returns a mailing recipient object suitable for mailing service
   * 
   * @param recipient
   * @return a mailingRecipient object
   */
  protected MailingRecipient addRecipient( User recipient )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    String localeCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    if ( recipient.getLanguageType() != null )
    {
      localeCode = recipient.getLanguageType().getCode();
    }
    mailingRecipient.setLocale( localeCode );
    mailingRecipient.setUser( recipient );

    return mailingRecipient;
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

  private UserCountryChanges getUserByOldCampaign( String oldCampaignNbr, Long userId )
  {
    return userCountryChangesDAO.getUserByOldCampaign( oldCampaignNbr, userId );
  }

  public UserDAO getUserDAO()
  {
    return userDAO;
  }

  public void setUserDAO( UserDAO userDAO )
  {
    this.userDAO = userDAO;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public ClaimDAO getClaimDAO()
  {
    return claimDAO;
  }

  public void setClaimDAO( ClaimDAO claimDAO )
  {
    this.claimDAO = claimDAO;
  }

  public ActivityDAO getActivityDAO()
  {
    return activityDAO;
  }

  public void setActivityDAO( ActivityDAO activityDAO )
  {
    this.activityDAO = activityDAO;
  }

  public void setUserCountryChangesDAO( UserCountryChangesDAO userCountryChangesDAO )
  {
    this.userCountryChangesDAO = userCountryChangesDAO;
  }

  public UserCountryChangesDAO getUserCountryChangesDAO()
  {
    return userCountryChangesDAO;
  }

  public CampaignTransferValueBean buildValueBean( UserCountryChanges userCountryChanges, CampaignTransferValueBean valueBean )
  {
    valueBean = new CampaignTransferValueBean();
    User user = userService.getUserById( userCountryChanges.getUserId() );
    valueBean.setUserName( user.getUserName() );
    valueBean.setFirstName( user.getFirstName() );
    valueBean.setLastName( user.getLastName() );
    // oldCountry Id getting set from pax load
    valueBean.setOldCampaignNbr( userCountryChanges.getOldCampaignNbr() );
    String oldAwardbanqNbrDec = userCountryChangesDAO.getDecryptedValue( userCountryChanges.getOldAwardBanqNbr() );
    valueBean.setOldBanqNbr( oldAwardbanqNbrDec );
    valueBean.setNewCampaignNbr( userCountryChanges.getNewCampaignNbr() );
    String newAwardbanqNbrDec = userCountryChangesDAO.getDecryptedValue( userCountryChanges.getNewAwardBanqNbr() );
    valueBean.setNewBanqNbr( newAwardbanqNbrDec );
    valueBean.setBalance( userCountryChanges.getBalanceToMove() );
    valueBean.setMessage( userCountryChanges.getMessage() );
    Set userNodes = userService.getUserNodes( user.getId() );
    String nodePath = "";
    String delimiter = "";
    for ( Iterator iter = userNodes.iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      if ( userNodes.size() > 1 )
      {
        delimiter = nodePath + ",";
      }
      nodePath = delimiter + userNode.getNode().getPath();
    }
    valueBean.setNodePath( nodePath );
    return valueBean;

  }

  public void emailExtract( Collection valueBeanList )
  {
    // If required parameters are missing, write invocation logger and stop.
    if ( valueBeanList == null )
    {
      // No file attachment, just send the email.
      sendMessage( null, null );
      String msg = new String( "No records found to run the Campaign Transfer Process" );
      logger.warn( msg );
    }
    else
    {
      // Get the resultset.
      List rowsReturned = generateEmailExtract( valueBeanList );

      // If good return code and at least 2 rows back (1st row is always column header even though
      // there is no data)
      if ( rowsReturned.size() > 1 )
      {
        // Save the file.
        File savedFile = writeFile( rowsReturned );
        String absolutePath = "";
        long fileLength = 0;
        if ( savedFile != null )
        {
          absolutePath = savedFile.getAbsolutePath();
        }

        if ( savedFile != null && doesFileExists( absolutePath ) )
        {
          // Email the file to the user.
          sendMessage( absolutePath, savedFile.getName() );
        }
        else
        {
          if ( savedFile != null )
          {
            fileLength = savedFile.length();
          }
          String msg = new String( "Problem accessing the file attachment while executing the " + " Report: Campaign Transfer Process" + ". File:" + absolutePath + " File size:"
              + printFileSize( fileLength ) + " The email has not been sent to the participant. Process ended.  " );
          logger.warn( msg );
        }
      }
      else
      {
        // No file attachment, just send the email.
        sendMessage( null, null );

        String msg = new String( "Problems with emailing Campaign Transfer Process - No data found." );
        logger.warn( msg );

      }
    }

  }

  /**
   * Sends an e-mail message to the user that launched this process with the file attachment of the
   * dataextract
   */
  @SuppressWarnings( "unchecked" )
  public void sendMessage( String fullFileName, String attachmentFileName )
  {
    // Set up mailing-level personalization data.
    User runByUser = getUserService().getUserById( UserManager.getUserId() );
    Map objectMap = new HashMap();
    objectMap.put( "firstName", runByUser.getFirstName() );
    objectMap.put( "lastName", runByUser.getLastName() );
    objectMap.put( "processName", PROCESS_BEAN_NAME );
    objectMap.put( "reportName", PROCESS_MESSAGE_NAME );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the e-mail message.
    Mailing mailing = composeMail( MessageService.CAMPAIGN_TRANSFER_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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

      String msg = new String( "Campaign Transfer Process" + " Report: Campaign Transfer Process" + " sent an email message to " + runByUser.getFirstName() + " " + runByUser.getLastName() + "."
          + " (mailing ID = " + mailing.getId() + ")" );

      logger.info( msg );
    }
    catch( Exception e )
    {
      String msg = new String( "An exception occurred while sending a " + " Report: Campaign Transfer Process" + " (mailing ID = " + mailing.getId() + ")" );
      logger.error( msg, e );
    }
  }

  // abstractCamopaign start
  private static final char QUOTE = '"';
  private static final String QUOTE_COMMA_QUOTE = QUOTE + "," + QUOTE;

  /**
   * Constructs and returns a list of users. The first row returned is the header information for
   * the email extract while subsequent rows are user extract information
   * 
   * @param budgetList
   * @return the budget extract information
   */
  @SuppressWarnings( "unchecked" )
  public List generateEmailExtract( Collection<CampaignTransferValueBean> valueBeanList )
  {
    ArrayList results = new ArrayList();
    boolean firstRow = true;
    for ( Iterator valueBeanObjIter = valueBeanList.iterator(); valueBeanObjIter.hasNext(); )
    {
      CampaignTransferValueBean valueBean = (CampaignTransferValueBean)valueBeanObjIter.next();

      String detailRow = "";
      if ( firstRow )
      {
        results.add( getHeaderInformationForExtract( valueBean ) );
        firstRow = false;
      }

      detailRow = QUOTE + nullCheck( valueBean.getUserName() ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( valueBean.getFirstName() ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( valueBean.getLastName() ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( valueBean.getOldCampaignNbr() ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( valueBean.getOldBanqNbr() ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( valueBean.getNewCampaignNbr() ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( valueBean.getNewBanqNbr() ) + QUOTE_COMMA_QUOTE;
      // detailRow += nullCheck( valueBean.getNodePath() ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( valueBean.getBalance() ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( valueBean.getMessage() ) + QUOTE;

      results.add( detailRow );
    }

    return results;
  }

  /**
   * Returns empty string if value is null
   * 
   * @param param
   * @return
   */
  private String nullCheck( String param )
  {
    if ( param == null )
    {
      return new String( "" );
    }
    return param;
  }

  /**
   * Returns empty string if value is null
   * 
   * @param param
   * @return
   */
  @SuppressWarnings( "unused" )
  private String nullCheck( Integer param )
  {
    if ( param == null )
    {
      return new String( "" );
    }
    return param.toString();
  }

  /**
   * Returns empty string if value is null
   * 
   * @param param
   * @return
   */
  private String nullCheck( Long param )
  {
    if ( param == null )
    {
      return new String( "" );
    }
    return param.toString();
  }

  /**
   * Constructs and saves the file to the current directory on the app server.
   * 
   * @return the file saved
   */
  public File writeFile( List results )
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
      failureMsg = new String( "An exception occurred while attempting to write file for " + " Report: Campaign Changes" + " to File:" + extractFile.getAbsolutePath() );

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

        String msg = new String( "Executing the " + " Report: Campaign Changes" + " File:" + extractFile.getAbsolutePath() + " has been saved successfully." + " File size:"
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

  /**
   * This method returns user defined path where the csv file should be saved. i.e. /tmp/ on Unix.
   * It makes sure the file separator (On Windows this is \  On Unix this is /) works with the
   * current operating system
   * 
   * @return a system variable driven path where the extract will be saved
   */
  public String getExtractLocation()
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

  /**
   * Constructs the Header of the user chagnes email extract
   * 
   * @param valueBean
   * @return String
   */

  public String getHeaderInformationForExtract( CampaignTransferValueBean valueBean )
  {
    // Create header string for the CSV or Excel file to be extracted.
    String header = "";

    header = ContentReaderManager.getText( "admin.campaign.transfer", "USER_NAME_LABEL" ) + ",";
    header += ContentReaderManager.getText( "admin.campaign.transfer", "FIRST_NAME_LABEL" ) + ",";
    header += ContentReaderManager.getText( "admin.campaign.transfer", "LAST_NAME_LABEL" ) + ",";
    header += ContentReaderManager.getText( "admin.campaign.transfer", "OLD_CAMPAIGN_LABEL" ) + ",";
    header += ContentReaderManager.getText( "admin.campaign.transfer", "OLD_AWARDBANQ_LABEL" ) + ",";
    header += ContentReaderManager.getText( "admin.campaign.transfer", "NEW_CAMPAIGN_LABEL" ) + ",";
    header += ContentReaderManager.getText( "admin.campaign.transfer", "NEW_AWARDBANQ_LABEL" ) + ",";
    // header += ContentReaderManager.getText( "admin.campaign.transfer", "NODEPATH_LABEL" ) + ",";
    header += ContentReaderManager.getText( "admin.campaign.transfer", "BALANCE_LABEL" ) + ",";
    header += ContentReaderManager.getText( "admin.campaign.transfer", "MESSAGE_NAME_LABEL" ) + ",";

    return header;
  }

  private String generateUniqueFileName()
  {
    StringBuffer fileName = new StringBuffer();
    fileName.append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    fileName.append( "_" );
    fileName.append( "Campaign_Changes" );
    fileName.append( "_" );
    fileName.append( DateUtils.getCurrentDate().getTime() );
    fileName.append( ".csv" );
    return fileName.toString();
  }

  /**
   * @return List of userCountryChanges IDs
   */
  public List<Long> getUCCsWithAccountBalancesToTranfer()
  {
    return userCountryChangesDAO.getUCCsWithAccountBalancesToTranfer();
  }

  /**
   * @param uccId
   * @return boolean
   * @throws ServiceErrorException
   */
  public boolean processAccountBalanceTransfer( Long uccId ) throws ServiceErrorException
  {
    boolean success = false;
    UserCountryChanges ucc = userCountryChangesDAO.getById( uccId );
    if ( ucc == null )
    {
      // should not happen
      logger.error( "No UserCountryChanges record found for uccId=" + uccId );
      throw new ServiceErrorException( "No UserCountryChanges record found for uccId=" + uccId );
    }

    // Account Balance Transfer
    try
    {
      Long retVal = awardBanQServiceFactory.getAwardBanQService().accountTransfer( userCountryChangesDAO.getDecryptedValue( ucc.getOldAwardBanqNbr() ),
                                                                                   userCountryChangesDAO.getDecryptedValue( ucc.getOldCampaignNbr() ),
                                                                                   userCountryChangesDAO.getDecryptedValue( ucc.getNewAwardBanqNbr() ),
                                                                                   userCountryChangesDAO.getDecryptedValue( ucc.getNewCampaignNbr() ) );
      logger.debug( "Balance Transfer for uccId=" + uccId + " retVal=" + retVal );

      // Update UCC
      ucc.setAcctBalanceTransferResultCode( retVal );
      ucc.setAcctBalanceTransferDate( new Date() );
      userCountryChangesDAO.saveUserCountryChanges( ucc );

      if ( ucc.getAcctBalanceTransferResultCode().longValue() == 0 )
      {
        success = true;
      }
    }
    catch( Exception e )
    {
      logger.error( "Unexpected Exception Processing Balance Transfer for uccId=" + uccId + " err=" + e.toString() );
    }

    return success;
  }

}
