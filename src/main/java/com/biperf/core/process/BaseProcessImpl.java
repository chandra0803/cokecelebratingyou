/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/BaseProcessImpl.java,v $
 */

package com.biperf.core.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.UnableToInterruptJobException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.InitializingBean;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.ProcessParameterInputFormatType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingAttachmentInfo;
import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.domain.mailing.MailingMessageLocale;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.ProcessInvocation;
import com.biperf.core.domain.process.ProcessParameter;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.commlog.CommLogService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessInvocationService;
import com.biperf.core.service.process.impl.ProcessInvocationAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.welcomemail.WelcomeMessageService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.FileExtractUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.MailingBatchHolder;
import com.biperf.core.value.UserValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
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
 * <td>wadzinsk</td>
 * <td>Nov 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class BaseProcessImpl implements BaseProcess, InitializingBean
{
  private static final Log log = LogFactory.getLog( BaseProcessImpl.class );

  // Bug#16395
  public static final String UnixFileSeparator = "/";
  public static final String WindowsFileSeparator = "\\";
  public static final String FILE_DELIMITER = ",";

  protected ProcessInvocationService processInvocationService;
  protected MailingService mailingService;
  protected MessageService messageService;
  protected CommLogService commLogService;
  protected SystemVariableService systemVariableService;
  protected CountryService countryService;
  protected UserService userService;
  protected WelcomeMessageService welcomeMessageService;
  protected AudienceService audienceService;
  protected HierarchyService hierarchyService;
  protected ListBuilderService listBuilderService;
  protected ParticipantService participantService;

  /**
   * Metadata defining the process parameters
   */
  protected Map processParameters = new LinkedHashMap();

  // Instance variables
  protected boolean interrupted = false;
  private Long processInvocationId;
  private String userLocale;

  private Thread currentThread;

  /**
   * @throws UnableToInterruptJobException
   */
  public void interrupt() throws UnableToInterruptJobException
  {
    if ( !interrupted )
    {
      log.warn( "Interrupting process" );
      interrupted = true;
      if ( null != currentThread )
      {
        currentThread.interrupt();
      }
    }
    else
    {
      log.warn( "Process already in interrupted state" );
    }
  }

  public final void execute( Map jobDataMap, Long processInvocationId )
  {
    try
    {
      currentThread = Thread.currentThread();
      this.processInvocationId = processInvocationId;
      if ( jobDataMap.get( "userLocale" ) != null )
      {
        this.userLocale = ( (String[])jobDataMap.get( "userLocale" ) )[0];
      }
      exposeJobParametersAsGetters( jobDataMap );
      onExecute();
    }
    catch( Throwable t )
    {
      log.error( "Error " + t, t );
    }
    finally
    {
      currentThread = null;
    }
  }

  /**
   * Check for Email Batch Enabled and Add description to Mailing Batch and create a new MailingBatch.
   * 
   * @param processName
   * 
   * @return MailingBatch
   */
  protected MailingBatch applyBatch( String processName )
  {
    // BatchEmail
    // Check for batch Email is Enabled?
    if ( getMailingService().isBatchEmailEnabled() )
    {
      MailingBatch mailingBatch = new MailingBatch();
      mailingBatch.setDescription( processName );
      return getMailingService().createMailingBatch( mailingBatch );
    }
    // BatchEmail

    return null;
  }

  protected String getSystemIncentiveEmailAddress()
  {
    return systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS_INCENTIVE ).getStringVal();
  }

  /**
   * get Mailing Batch process comments
   * 
   * @param MailingBatch
   * 
   * @return String - BatchComments
   */
  protected String getMailingBatchProcessComments( MailingBatch mailingBatch )
  {
    StringBuilder additionalBatchComments = new StringBuilder();
    if ( null != mailingBatch )
    {
      additionalBatchComments.append( "Batch Id " );
      additionalBatchComments.append( mailingBatch.getId() );
      additionalBatchComments.append( ": " );
      additionalBatchComments.append( mailingBatch.getDescription() );
      additionalBatchComments.append( "<br/>" );
    }
    return additionalBatchComments.toString();
  }

  protected void logMailingBatchHolderComments( MailingBatchHolder mailingBatchHolder )
  {
    if ( mailingBatchHolder == null )
    {
      return;
    }

    String participantAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPaxAchievedMailingBatch() );
    if ( !StringUtils.isEmpty( participantAchievedBatchEmailComments ) )
    {
      addComment( participantAchievedBatchEmailComments );
    }

    String participantNotAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPaxNotAchievedMailingBatch() );
    if ( !StringUtils.isEmpty( participantNotAchievedBatchEmailComments ) )
    {
      addComment( participantNotAchievedBatchEmailComments );
    }

    String partnerAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPartnerAchievedMailingBatch() );
    if ( !StringUtils.isEmpty( partnerAchievedBatchEmailComments ) )
    {
      addComment( partnerAchievedBatchEmailComments );
    }

    String partnerAchievedNoPayoutBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPartnerAchievedNoPayoutMailingBatch() );
    if ( !StringUtils.isEmpty( partnerAchievedNoPayoutBatchEmailComments ) )
    {
      addComment( partnerAchievedNoPayoutBatchEmailComments );
    }

    String partnerNotAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPartnerNotAchievedMailingBatch() );
    if ( !StringUtils.isEmpty( partnerNotAchievedBatchEmailComments ) )
    {
      addComment( partnerNotAchievedBatchEmailComments );
    }

    String notSelected = getMailingBatchProcessComments( mailingBatchHolder.getGoalNotSelectedBatch() );
    if ( !StringUtils.isEmpty( notSelected ) )
    {
      addComment( notSelected );
    }

    String partnerProgress = getMailingBatchProcessComments( mailingBatchHolder.getPartnerProgressMailingBatch() );
    if ( !StringUtils.isEmpty( partnerProgress ) )
    {
      addComment( partnerProgress );
    }

    String participantProgress = getMailingBatchProcessComments( mailingBatchHolder.getPaxProgressMailingBatch() );
    if ( !StringUtils.isEmpty( participantProgress ) )
    {
      addComment( participantProgress );
    }
  }

  /**
   * Allow job data to be accesible via getters rather than through the job hashmap.
   * 
   * @param jobDataMap
   */
  private void exposeJobParametersAsGetters( Map jobDataMap )
  {
    BeanWrapper bw = new BeanWrapperImpl( this );
    MutablePropertyValues pvs = new MutablePropertyValues();
    pvs.addPropertyValues( jobDataMap );
    bw.setPropertyValues( pvs, true );
  }

  /**
   * @return value of interrupted property
   */
  public boolean isInterrupted()
  {
    return interrupted;
  }

  /**
   * Add a comment, length will be trucated to {@link ProcessInvocationService#COMMENT_COLUMN_SIZE}
   * 
   * @param comment
   */
  public void addComment( String comment )
  {
    processInvocationService.addComment( processInvocationId, comment );
  }

  public User getRunByUser()
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ProcessInvocationAssociationRequest( ProcessInvocationAssociationRequest.USERS ) );
    ProcessInvocation processInvocation = processInvocationService.getProcessInvocationById( processInvocationId, associationRequestCollection );

    return processInvocation.getRunAsUser();
  }

  /**
   * @return value of processParameters property
   */
  public Map getProcessParameters()
  {
    return processParameters;
  }

  /**
   * @param processParameters value for processParameters property
   */
  public void setProcessParameters( Map processParameters )
  {
    this.processParameters = processParameters;
  }

  /**
   * @param processInvocationService value for processInvocationService property
   */
  public void setProcessInvocationService( ProcessInvocationService processInvocationService )
  {
    this.processInvocationService = processInvocationService;
  }

  /**
   * @param mailingService value for mailingService property
   */
  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setCommLogService( CommLogService commLogService )
  {
    this.commLogService = commLogService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return this.systemVariableService;
  }

  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }

  /**
   * @return value of processInvocationId property
   */
  public Long getProcessInvocationId()
  {
    return processInvocationId;
  }

  protected abstract void onExecute();

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  /**
   * Add exception to stdout log and process invocation comment log.
   * @param e
   */
  public void logErrorMessage( Exception e )
  {
    if ( e instanceof ServiceErrorException )
    {
      logServiceErrorMessage( (ServiceErrorException)e );
    }
    else
    {
      log.error( e );
      addComment( "An Exception occurred. " + "\n" + e.toString() + "\n" + " See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  private void logServiceErrorMessage( ServiceErrorException e )
  {
    for ( Iterator iter = e.getServiceErrors().iterator(); iter.hasNext(); )
    {
      ServiceError error = (ServiceError)iter.next();

      String data = CmsResourceBundle.getCmsBundle().getString( error.getKey() );
      StringBuffer sb = new StringBuffer( data );
      if ( !StringUtils.isEmpty( error.getArg1() ) )
      {
        replaceValue( sb, 0, error.getArg1() );
      }
      if ( !StringUtils.isEmpty( error.getArg2() ) )
      {
        replaceValue( sb, 1, error.getArg1() );
      }
      if ( !StringUtils.isEmpty( error.getArg3() ) )
      {
        replaceValue( sb, 2, error.getArg1() );
      }
      log.error( sb.toString() );
      addComment( "An ServiceErrorException occurred.  " + "\n" + sb.toString() + "\n" + " See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId()
          + ")" );
    }
  }

  private void replaceValue( StringBuffer message, int count, String arg )
  {
    boolean found = true;
    while ( found )
    {
      int index = message.indexOf( "{" + count + "}" );
      if ( index > -1 )
      {
        message.replace( index, index + 3, arg );
      }
      else
      {
        found = false;
      }
    }
  }

  public void logErrorMessage2( ServiceErrorException e )
  {
    for ( Iterator iter = e.getServiceErrors().iterator(); iter.hasNext(); )
    {
      ServiceError error = (ServiceError)iter.next();

      String pattern = CmsResourceBundle.getCmsBundle().getString( error.getKey() );
      String[] arguments = error.getArgs();

      log.error( MessageFormat.format( pattern, (Object)arguments ) );

      addComment( "An ServiceErrorException occurred.  " + "\n" + MessageFormat.format( pattern, (Object)arguments ) + "\n" + "See the log file for additional information.  "
          + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
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
   * Composes a single mailing object and attach a single recipient with the message and customized
   * data If you have multiple recipients in one mailing, this method is not recommended.
   * 
   * @param recipient
   * @param cmAssetCode
   * @param personalizationData
   */
  protected void sendMail( User recipient, String cmAssetCode, Map personalizationData )
  {
    // Compose the email
    Mailing mailing = composeMail( cmAssetCode, MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mailingRecipient = addRecipient( recipient );
    mailing.addMailingRecipient( mailingRecipient );

    // Set the mailingMessageLocale
    MailingMessageLocale locale = new MailingMessageLocale();
    locale.setHtmlMessage( mailing.getMessage().getI18nHtmlBody( mailingRecipient.getLocale() ) );
    locale.setPlainMessage( mailing.getMessage().getI18nPlainTextBody( mailingRecipient.getLocale() ) );
    locale.setTextMessage( mailing.getMessage().getI18nTextBody( mailingRecipient.getLocale() ) );
    locale.setSubject( mailing.getMessage().getI18nSubject( mailingRecipient.getLocale() ) );
    locale.setLocale( mailingRecipient.getLocale() );
    mailing.addMailingMessageLocale( locale );

    // Send the e-mail message.
    try
    {
      mailingService.submitMailing( mailing, personalizationData );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending mail.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending mail.  " + "See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
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
   * Adds the message by id and mailing type to a mailing object
   * 
   * @param messageId
   * @param mailingType
   * @return a mailing object that is mostly assembled, except for the mailingRecipient(s)
   */
  protected Mailing composeMail( Long messageId, String mailingType )
  {
    Mailing mailing = composeMail();

    mailing.setMailingType( MailingType.lookup( mailingType ) );

    Message message = messageService.getMessageById( messageId );
    mailing.setMessage( message );

    return mailing;
  }

  /**
   * Creates a new mailing object and add Guid, Sender and Delivery Date to it
   * 
   * @return a partially assembled mailing object
   */
  protected Mailing composeMail()
  {
    Mailing mailing = new Mailing();

    // Needs Guid due to lack of a business key
    mailing.setGuid( GuidUtils.generateGuid() );

    // Sender
    String sender = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_SENDER_EMAIL_ADDRESS ).getStringVal();
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
  protected MailingRecipient addRecipient( User recipient )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setLocale( recipient.getLanguageType() != null
        ? recipient.getLanguageType().getCode()
        : getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal() );
    mailingRecipient.setUser( recipient );
    return mailingRecipient;
  }

  /**
   * Builds a mailingRecipient suitable for mailing with just an email address
   * @param emailAddress - does not perform validation, should be a well-formed email address
   * @return mailingRecipient
   */
  protected MailingRecipient addRecipient( String emailAddress )
  {
    MailingRecipient mailingRecipient = mailingService.buildPreviewMailingRecipient( emailAddress );
    return mailingRecipient;
  }

  /**
   * @param mailing
   * @param fullFileName - this is the absolute path of the file (internal to the system incl. directory names)
   * @param attachmentFileName - this is the name used on the file attachment in the email (visible to users)
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
   * Overridden from
   * 
   * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   * @throws Exception
   */
  public void afterPropertiesSet() throws Exception
  {
    validateProcessParameters( getProcessParameters() );

  }

  /**
   * Validate process parameter definitions to warn of xml misconfiguration.
   */
  private void validateProcessParameters( Map processParameters )
  {
    if ( processParameters != null )
    {
      for ( Iterator iter = processParameters.keySet().iterator(); iter.hasNext(); )
      {
        String paramName = (String)iter.next();
        ProcessParameter processParameter = (ProcessParameter)processParameters.get( paramName );

        // paramNames must match
        if ( !paramName.equals( processParameter.getName() ) )
        {
          throw new BeaconRuntimeException( "Invalid process parameter definition. Parameter name map key value(" + paramName + ") must match the process parameter name(" + processParameter.getName()
              + ")" );
        }

        // name is required
        if ( processParameter.getName() == null )
        {
          throw new BeaconRuntimeException( "Invalid process parameter definition. Parameter name is required" );
        }

        // description is required
        if ( processParameter.getDescription() == null )
        {
          throw new BeaconRuntimeException( "Invalid process parameter definition. Parameter description is required" );
        }

        // processParameterDataType is required
        if ( processParameter.getProcessParameterDataTypeCode() == null )
        {
          throw new BeaconRuntimeException( "Invalid process parameter definition. Parameter processParameterDataType is required" );
        }

        // processParameterInputFormatType is required
        String processParameterInputFormatTypeCode = processParameter.getProcessParameterInputFormatTypeCode();
        if ( processParameterInputFormatTypeCode == null )
        {
          throw new BeaconRuntimeException( "Invalid process parameter definition. Parameter processParameterInputFormatType is required" );
        }

        if ( processParameterInputFormatTypeCode.equals( ProcessParameterInputFormatType.CHECK_BOXES ) || processParameterInputFormatTypeCode.equals( ProcessParameterInputFormatType.DROP_DOWN ) )
        {
          // if multi-choice, source params are required
          if ( processParameter.getProcessParameterSourceTypeCode() == null )
          {
            throw new BeaconRuntimeException( "Invalid process parameter definition. "
                + "Parameter processParameterSourceTypeCode is required when using a multi-choice processParameterInputFormatType " );
          }

          if ( processParameter.getSourceName() == null )
          {
            throw new BeaconRuntimeException( "Invalid process parameter definition. " + "Parameter sourceName is required when using a multi-choice processParameterInputFormatType" );
          }

        }
      }
    }
  }

  public File writeFile( List paxs, String beanName )
  {
    String fileName = "";
    String failureMsg = "";
    File extractFile = null;
    String extractLocation = getExtractLocation();
    try
    {
      // Create an unique filename for the extract file.
      fileName = generateUniqueFileName( beanName );

      FileExtractUtils.createDirIfNeeded( extractLocation );

      // Writes a file with the participants extracted.
      extractFile = new File( extractLocation, fileName );

      // Build failure message just in case.
      failureMsg = new String( "An exception occurred while attempting to write file for process " + beanName );

      boolean success = extractFile.createNewFile();
      if ( success )
      {
        BufferedWriter writer = new BufferedWriter( new FileWriter( extractFile ) );

        // header row.
        writer.write( "login id" + FILE_DELIMITER + "first name" + FILE_DELIMITER + "last name" + FILE_DELIMITER );
        writer.newLine();

        for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
        {
          Object temp = iter.next();

          // data row. format: loginid,firstName LastName
          String loginId = new String( "" );
          String firstName = new String( "" );
          String lastName = new String( "" );
          if ( temp instanceof Participant )
          {
            Participant pax = (Participant)temp;
            loginId = pax.getUserName();
            firstName = pax.getFirstName();
            lastName = pax.getLastName();
          }
          else if ( temp instanceof UserValueBean )
          {
            UserValueBean pax = (UserValueBean)temp;
            loginId = pax.getUserName();
            firstName = pax.getFirstName();
            lastName = pax.getLastName();
          }
          // Resend Welcome Email Count Process passes IDs
          else if ( temp instanceof Long )
          {
            Participant pax = participantService.getParticipantById( (Long)temp );
            loginId = pax.getUserName();
            firstName = pax.getFirstName();
            lastName = pax.getLastName();
          }
          String row = StringUtil.truncateStringToLength( loginId, 254 ) + FILE_DELIMITER + StringUtil.truncateStringToLength( firstName, 254 ) + FILE_DELIMITER
              + StringUtil.truncateStringToLength( lastName, 254 );
          writer.write( row );
          writer.newLine();
        }
        writer.close();

        String msg = new String( "Executing the process " + beanName + ". File:" + extractFile.getAbsolutePath() + " has been saved successfully." + " File size:"
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

  protected String generateUniqueFileName( /* String clientName, */ String beanName )
  {
    StringBuffer fileName = new StringBuffer();
    fileName.append( beanName );
    fileName.append( "_" );
    fileName.append( DateUtils.getCurrentDate().getTime() );
    fileName.append( ".csv" );
    return fileName.toString();
  }

  public File extractFile( List allUsers, String beanName )
  {
    File savedFile = writeFile( allUsers, beanName );

    if ( savedFile != null && doesFileExists( savedFile.getAbsolutePath() ) )
    {
      return savedFile;
    }
    else
    {
      String msg = null;
      if ( savedFile != null )
      {
        msg = new String( "Problem accessing the file attachment while executing the " + beanName + ". File:" + savedFile.getAbsolutePath() + " File size:" + printFileSize( savedFile.length() )
            + " The email has not been sent. Process ended.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
      }
      else
      {
        msg = new String( "Failed to create file while executing the " + beanName );
      }
      log.warn( msg );
      addComment( msg );
      return null;
    }
  }

  public WelcomeMessageService getWelcomeMessageService()
  {
    return welcomeMessageService;
  }

  public void setWelcomeMessageService( WelcomeMessageService welcomeMessageService )
  {
    this.welcomeMessageService = welcomeMessageService;
  }

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public HierarchyService getHierarchyService()
  {
    return hierarchyService;
  }

  public void setHierarchyService( HierarchyService hierarchyService )
  {
    this.hierarchyService = hierarchyService;
  }

  public ListBuilderService getListBuilderService()
  {
    return listBuilderService;
  }

  public void setListBuilderService( ListBuilderService listBuilderService )
  {
    this.listBuilderService = listBuilderService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public String getUserLocale()
  {
    return userLocale;
  }

  public void setUserLocale( String userLocale )
  {
    this.userLocale = userLocale;
  }

}
