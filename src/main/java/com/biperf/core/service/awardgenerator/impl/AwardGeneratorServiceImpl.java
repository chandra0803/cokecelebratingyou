
package com.biperf.core.service.awardgenerator.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.awardgenerator.AwardGenBatchDAO;
import com.biperf.core.dao.awardgenerator.AwardGeneratorDAO;
import com.biperf.core.domain.awardgenerator.AwardGenAward;
import com.biperf.core.domain.awardgenerator.AwardGenBatch;
import com.biperf.core.domain.awardgenerator.AwardGenParticipant;
import com.biperf.core.domain.awardgenerator.AwardGenerator;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingAttachmentInfo;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardgenerator.AwardGeneratorService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.FileExtractUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.AwardGenFileExtractValueBean;
import com.biperf.core.value.AwardGenPlateauFormBean;
import com.biperf.core.value.AwardGenPlateauValueBean;
import com.biperf.core.value.AwardGeneratorManagerPaxBean;
import com.biperf.core.value.AwardGeneratorManagerReminderBean;
import com.biperf.core.value.FormattedValueBean;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * AwardGeneratorServiceImpl interfaces with AwardGenerator DAO for maintenance of awardgenerators
 * information
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
 * <td>chowdhur</td>
 * <td>Jul 08, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardGeneratorServiceImpl implements AwardGeneratorService
{
  public static final String HIRE_DATE_EXAMINER = "Hire Date";
  public static final String BIRTH_DATE_EXAMINER = "Birth Date";

  public static final String OUTPUT_DATA_1 = "p_out_data_1";

  public static final String OUTPUT_DATA_2 = "p_out_data_2";

  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";

  private static final Log logger = LogFactory.getLog( AwardGeneratorServiceImpl.class );

  private AwardGeneratorDAO awardGeneratorDAO;
  private AwardGenBatchDAO awardGenBatchDAO;
  private UserCharacteristicService userCharacteristicService;
  private UserService userService;
  private AudienceService audienceService;
  private ParticipantService paxService;

  private MailingService mailingService;
  private MessageService messageService;
  private SystemVariableService systemVariableService;

  /**
   * Manages deleting an awardgenerator from the database.
   * 
   * @param awardfenerator
   */
  public void deleteAwardGenerator( AwardGenerator awardGenerator ) throws ServiceErrorException
  {
    awardGeneratorDAO.deleteAwardGenerator( awardGenerator );
  }

  /**
   * Manages deleting an awardgenerator from the database.
   * 
   * @param awardfenerator
   */
  public void deleteAwardGenerator( Long awardGeneratorId ) throws ServiceErrorException
  {
    AwardGenerator awardGeneratorToDelete = awardGeneratorDAO.getAwardGeneratorById( awardGeneratorId );

    String name = awardGeneratorToDelete.getName();
    Timestamp timestamp = new Timestamp( new Date().getTime() );
    awardGeneratorToDelete.setName( name + "-" + timestamp );

    if ( awardGeneratorToDelete != null )
    {
      awardGeneratorDAO.deleteAwardGenerator( awardGeneratorToDelete );
    }
  }

  /**
   * Overridden from @see com.biperf.core.service.awardfenerator.AwardGeneratorService#deleteAwardGenerators(java.util.List)
   * @param awardfeneratorIdList
   * @throws ServiceErrorException
   */
  public void deleteAwardGenerators( List awardGeneratorIdList ) throws ServiceErrorException
  {
    Iterator idIter = awardGeneratorIdList.iterator();

    while ( idIter.hasNext() )
    {
      this.deleteAwardGenerator( (Long)idIter.next() );
    }
  }

  public AwardGenerator getAwardGeneratorByName( String name )
  {
    return awardGeneratorDAO.getAwardGeneratorByName( name );
  }

  /**
   * Save the awardgenerator to the database.
   * 
   * @param awardfenerator
   * @return AwardGenerator
   */
  public AwardGenerator saveAwardGenerator( AwardGenerator awardGeneratorToSave ) throws ServiceErrorException
  {
    return awardGeneratorDAO.saveAwardGenerator( awardGeneratorToSave );
  }

  /**
   * Gets the awardgenerator by the id.
   * 
   * @param id
   * @return AwardGenerator
   */
  public AwardGenerator getAwardGeneratorById( Long id ) throws ServiceErrorException
  {
    return awardGeneratorDAO.getAwardGeneratorById( id );
  }

  /**
   * Gets a list of all awardgenerators in the database. 
   * 
   * @return List
   */
  public List getAllActiveAwardGenerators() throws ServiceErrorException
  {
    return awardGeneratorDAO.getAllActiveAwardGenerators();
  }

  public List<String> getAllYearsByAwardGenId( Long awardGenId )
  {
    return awardGeneratorDAO.getAllYearsByAwardGenId( awardGenId );
  }

  public List<String> getAllDaysByAwardGenId( Long awardGenId )
  {
    return awardGeneratorDAO.getAllDaysByAwardGenId( awardGenId );
  }

  public List<AwardGenAward> getAllAwardsByAwardGenIdAndYears( Long awardGenId, String years )
  {
    return awardGeneratorDAO.getAllAwardsByAwardGenIdAndYears( awardGenId, years );
  }

  public List<AwardGenAward> getAllAwardsByAwardGenIdAndDays( Long awardGenId, String days )
  {
    return awardGeneratorDAO.getAllAwardsByAwardGenIdAndDays( awardGenId, days );
  }

  /**
   * AwardGenBatch
   * @param awardGenBatch
   * @return
   */
  public AwardGenBatch saveAwardGenBatch( AwardGenBatch awardGenBatch ) throws ServiceErrorException
  {
    return awardGenBatchDAO.save( awardGenBatch );
  }

  public List<AwardGenBatch> getAllBatchesByAwardGenId( Long awardGenId )
  {
    return awardGenBatchDAO.getAllBatchesByAwardGenId( awardGenId );
  }

  public List<FormattedValueBean> getAwardGenBatches( Long awardGenId )
  {
    List batches = new ArrayList();
    if ( awardGenId != null && awardGenId != 0 )
    {
      List<AwardGenBatch> awardGenBatchList = getAllBatchesByAwardGenId( awardGenId );
      for ( Iterator iter = awardGenBatchList.iterator(); iter.hasNext(); )
      {
        AwardGenBatch batch = (AwardGenBatch)iter.next();

        FormattedValueBean fb = new FormattedValueBean();
        fb.setValue( buildDateDesc( batch ) );
        fb.setId( batch.getId() );
        batches.add( fb );
      }
    }
    return batches;
  }

  private String buildDateDesc( AwardGenBatch batch )
  {
    return DateUtils.toDisplayString( batch.getStartDate() ) + " - " + DateUtils.toDisplayString( batch.getEndDate() );
  }

  public List<FormattedValueBean> getExaminerList()
  {
    List examinerList = buildInitialExaminerList();

    List userChars = userCharacteristicService.getAllCharacteristics();
    for ( Iterator iter = userChars.iterator(); iter.hasNext(); )
    {
      UserCharacteristicType uct = (UserCharacteristicType)iter.next();
      if ( CharacteristicDataType.DATE.equals( uct.getCharacteristicDataType().getCode() ) )
      {
        FormattedValueBean fb = new FormattedValueBean();
        fb.setValue( uct.getCharacteristicName() );
        fb.setId( uct.getId() );
        examinerList.add( fb );
      }
    }
    return examinerList;
  }

  private List buildInitialExaminerList()// hire date, birth date
  {
    List examinerList = new ArrayList();
    // birth date
    FormattedValueBean fb = new FormattedValueBean();
    fb.setValue( BIRTH_DATE_EXAMINER );
    fb.setId( new Long( 1 ) );
    examinerList.add( fb );

    // hire date
    fb = new FormattedValueBean();
    fb.setValue( HIRE_DATE_EXAMINER );
    fb.setId( new Long( 2 ) );
    examinerList.add( fb );

    return examinerList;
  }

  @Override
  public Map<String, Object> generateAndSaveBatch( Map<String, Object> awardGenParams )
  {
    return awardGenBatchDAO.generateAndSaveBatch( awardGenParams );
  }

  public static Date getExpiryDate( int days )
  {
    Date currentDate = new Date();

    Date expiryDay = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime( currentDate );
    cal.add( Calendar.DATE, days );
    expiryDay = cal.getTime();
    return expiryDay;
  }

  public boolean isBatchExist( Long awardGenId, String batchStartDate, String batchEndDate )
  {
    return awardGenBatchDAO.isBatchExist( awardGenId, batchStartDate, batchEndDate );
  }

  public AwardGenBatch getAwardGenBatchById( Long awardGenBatchId )
  {
    return awardGenBatchDAO.getAwardGenBatchById( awardGenBatchId );
  }

  /***********************************************************************************************************
   * Generate Extract
   * *********************************************************************************************************
   */

  public void generateAndSendEmailExtract( Long batchId ) throws ServiceErrorException
  {
    Map<String, Object> output = awardGenBatchDAO.generateValuesForEmail( batchId );

    List<AwardGenFileExtractValueBean> valueBeanList = new ArrayList<AwardGenFileExtractValueBean>();
    List<AwardGenFileExtractValueBean> merchnadiseValueBeanList = (List<AwardGenFileExtractValueBean>)output.get( "p_out_ref_cursor_merchandise" );
    List<AwardGenFileExtractValueBean> pointsValueBeanList = (List<AwardGenFileExtractValueBean>)output.get( "p_out_ref_cursor_points" );
    valueBeanList.addAll( merchnadiseValueBeanList );
    valueBeanList.addAll( pointsValueBeanList );
    try
    {
      if ( valueBeanList != null && !valueBeanList.isEmpty() )
      {
        emailExtract( valueBeanList,
                      (String)output.get( "p_out_awardtype" ),
                      (String)output.get( "p_out_promotionname" ),
                      (String)output.get( "p_out_awardgen_setupname" ),
                      (String)output.get( "p_out_batchdate" ) );
      }
    }
    catch( Exception e )
    {
      logger.error( "Error generating and sending email extract for: Award Gen File Extract for Award Gen Batch Id: " + batchId );
    }
  }

  private void emailExtract( List valueBeanList, String awardType, String promotionName, String awardGenSetupName, String batchDate )
  {
    // If required parameters are missing, write invocation log and stop.
    if ( valueBeanList == null )
    {
      // No file attachment, just send the email.
      sendMessage( null, null, promotionName, awardGenSetupName, batchDate );
      String msg = new String( "No records found to run the Award File Generator Extract" );
      logger.warn( msg );
    }
    else
    {
      List rowsReturned = generateEmailExtract( awardType, valueBeanList );

      if ( rowsReturned.size() > 1 )
      {
        File savedFile = writeFile( rowsReturned, promotionName, awardGenSetupName );

        String absolutePath = "";
        long fileLength = 0;
        if ( savedFile != null )
        {
          absolutePath = savedFile.getAbsolutePath();
        }

        if ( doesFileExists( absolutePath ) )
        {
          sendMessage( absolutePath, savedFile.getName(), promotionName, awardGenSetupName, batchDate );
        }
        else
        {
          if ( savedFile != null )
          {
            fileLength = savedFile.length();
          }
          String msg = new String( "Problem accessing the file attachment while executing the " + " Award File Generator Extract" + ". File:" + absolutePath + " File size:"
              + printFileSize( fileLength ) + " The email has not been sent to the participant. Process ended.  " );
          logger.warn( msg );
        }
      }
      else
      {
        // No file attachment, just send the email.
        sendMessage( null, null, promotionName, awardGenSetupName, batchDate );

        String msg = new String( "Problems with emailing Award File Generator Extract - No data found." );
        logger.warn( msg );

      }
    }

  }

  /**
   * Sends an e-mail message to the admin the file attachment of the
   * data extract
   */
  @SuppressWarnings( "unchecked" )
  public void sendMessage( String fullFileName, String attachmentFileName, String promotionName, String awardGenSetupName, String batchDate )
  {
    User runByUser = getUserService().getUserById( UserManager.getUserId() );
    Map objectMap = new HashMap();
    objectMap.put( "firstName", runByUser.getFirstName() );
    objectMap.put( "lastName", runByUser.getLastName() );
    objectMap.put( "promotionName", promotionName );
    objectMap.put( "awardFileName", awardGenSetupName );
    objectMap.put( "batchDate", batchDate );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    Mailing mailing = composeMail( MessageService.AWARD_FILE_EXTRACT_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    mailing.addMailingRecipient( addRecipient( runByUser ) );

    if ( fullFileName == null )
    {
      objectMap.put( "noDataFound", "true" );
    }
    else
    {
      mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, fullFileName, attachmentFileName ) );
    }

    try
    {
      mailing = mailingService.submitMailingWithoutScheduling( mailing, objectMap );

      mailingService.processMailing( mailing.getId() );

      String msg = new String( "Award File Generator Extract " + " email message sent to " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." + " (mailing ID = " + mailing.getId()
          + ")" );

      logger.info( msg );
    }
    catch( Exception e )
    {
      String msg = new String( "An exception occurred while sending a " + " Award File Generator Extract" + " (mailing ID = " + mailing.getId() + ")" );
      logger.error( msg, e );
    }
  }

  private static final char QUOTE = '"';
  private static final String QUOTE_COMMA_QUOTE = QUOTE + "," + QUOTE;

  public static final String UnixFileSeparator = "/";
  public static final String WindowsFileSeparator = "\\";

  /**
   * Constructs and returns a list of users. The first row returned is the header information for
   * the email extract while subsequent rows are user extract information
   * 
   */
  @SuppressWarnings( "unchecked" )
  public List generateEmailExtract( String awardType, List valueBeanList )
  {
    ArrayList results = new ArrayList();
    boolean firstRow = true;
    for ( Iterator valueBeanObjIter = valueBeanList.iterator(); valueBeanObjIter.hasNext(); )
    {
      AwardGenFileExtractValueBean valueBean = (AwardGenFileExtractValueBean)valueBeanObjIter.next();

      String detailRow = "";
      if ( firstRow )
      {
        results.add( getHeaderInformationForExtract( awardType, valueBean ) );
        firstRow = false;
      }

      detailRow = QUOTE + nullCheck( valueBean.getUserName() ) + QUOTE_COMMA_QUOTE;
      if ( PromotionAwardsType.POINTS.equals( awardType ) )
      {
        detailRow += nullCheck( valueBean.getAwardAmount() ) + QUOTE_COMMA_QUOTE;
      }
      else if ( PromotionAwardsType.MERCHANDISE.equals( awardType ) )
      {
        detailRow += nullCheck( valueBean.getOrdinalPosition() ) + QUOTE_COMMA_QUOTE;
      }
      detailRow += nullCheck( DateUtils.toDisplayString( valueBean.getAwardDate() ) ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( "" ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( "" ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( "" ) + QUOTE_COMMA_QUOTE;
      detailRow += nullCheck( "" );
      if ( valueBean.getAnniversaryNumberOfDays() > 0 )
      {
        detailRow += QUOTE_COMMA_QUOTE + nullCheck( valueBean.getAnniversaryNumberOfDays() );
      }
      if ( valueBean.getAnniversaryNumberOfYears() > 0 )
      {
        detailRow += QUOTE_COMMA_QUOTE + nullCheck( valueBean.getAnniversaryNumberOfYears() ) + QUOTE;
      }
      else
      {
        detailRow += QUOTE;
      }

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
   * Constructs the Header of the user changes email extract
   * 
   * @param valueBean
   * @return String
   */

  public String getHeaderInformationForExtract( String awardType, AwardGenFileExtractValueBean valueBean )
  {
    // Create header string for the CSV or Excel file to be extracted.
    String header = "";

    header = ContentReaderManager.getText( "awardgenerator.batch", "USER_NAME_LABEL" ) + ",";
    if ( PromotionAwardsType.POINTS.equals( awardType ) )
    {
      header += ContentReaderManager.getText( "awardgenerator.batch", "AWARD_AMOUNT_LABEL" ) + ",";
    }
    else if ( PromotionAwardsType.MERCHANDISE.equals( awardType ) )
    {
      header += ContentReaderManager.getText( "awardgenerator.batch", "LEVEL_LABEL" ) + ",";
    }
    header += ContentReaderManager.getText( "awardgenerator.batch", "AWARD_DATE_LABEL" ) + ",";
    header += ContentReaderManager.getText( "awardgenerator.batch", "COMMENTS" ) + ",";
    header += ContentReaderManager.getText( "awardgenerator.batch", "FORM_ELEMENT_1_LABEL" ) + ",";
    header += ContentReaderManager.getText( "awardgenerator.batch", "FORM_ELEMENT_2_LABEL" ) + ",";
    header += ContentReaderManager.getText( "awardgenerator.batch", "FORM_ELEMENT_3_LABEL" ) + ",";
    if ( valueBean.getAnniversaryNumberOfDays() > 0 )
    {
      header += ContentReaderManager.getText( "awardgenerator.batch", "ANNIVERSARY_NUM_DAYS" ) + ",";
    }
    if ( valueBean.getAnniversaryNumberOfYears() > 0 )
    {
      header += ContentReaderManager.getText( "awardgenerator.batch", "ANNIVERSARY_NUM_YEARS" );
    }
    return header;
  }

  /**
   * Constructs and saves the file to the current directory on the app server.
   * 
   * @return the file saved
   */
  public File writeFile( List results, String promotionName, String awardGenSetupName )
  {
    String fileName = "";
    String failureMsg = "";
    File extractFile = null;
    String extractLocation = getExtractLocation();
    try
    {
      // Create an unique filename for the extract file.
      fileName = generateUniqueFileName( promotionName, awardGenSetupName );

      FileExtractUtils.createDirIfNeeded( extractLocation );

      // Writes a file with the resultset from stored proc.
      extractFile = new File( extractLocation, fileName );

      // Build failure message just in case.
      failureMsg = new String( "An exception occurred while attempting to write file for " + " Award File Generator Extract " + " to File:" + extractFile.getAbsolutePath() );

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

        String msg = new String( "Executing the " + " Award File Generator Extract File:" + extractFile.getAbsolutePath() + " has been saved successfully." + " File size:"
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
   * Creates a .csv file name that is unique to: - the client name - the report requested - the
   * current datetime.
   * 
   * @return an unique file name
   */
  private String generateUniqueFileName( String promotionName, String awardGenSetupName )
  {
    StringBuffer fileName = new StringBuffer();
    fileName.append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    fileName.append( "_" );
    fileName.append( promotionName );
    fileName.append( "_" );
    fileName.append( "AwardFileExtract" );
    fileName.append( "_" );
    fileName.append( DateUtils.getCurrentDate().getTime() );
    fileName.append( ".csv" );
    return fileName.toString();
  }

  /**
   * This method returns user defined path where the csv file should be saved. i.e. /tmp/ on Unix.
   * It makes sure the file separator (On Windows this is \ On Unix this is /) works with the
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

  @Override
  public List<AwardGeneratorManagerReminderBean> getAwardGeneratorManagerRemindersList( Long participantId )
  {
    List<AwardGeneratorManagerReminderBean> reminderList = awardGeneratorDAO.getAwardGeneratorManagerRemindersList( participantId );
    return reminderList;
  }

  public List<AwardGeneratorManagerPaxBean> getPaxListByMgrAndBatchId( Long userId, Long batchId )
  {
    return awardGenBatchDAO.getPaxListByMgrAndBatchId( userId, batchId );
  }

  @Override
  public void dismissAlertForAwardGenManager( Long userId, Long batchId )
  {
    List<AwardGeneratorManagerPaxBean> awardGenPaxList = getPaxListByMgrAndBatchId( userId, batchId );

    for ( AwardGeneratorManagerPaxBean paxBean : awardGenPaxList )
    {
      Long awardGenPaxId = paxBean.getAwardGenPaxId();
      AwardGenParticipant awardGenParticipant = awardGenBatchDAO.getAwardGenParticipantById( awardGenPaxId );
      if ( awardGenParticipant != null )
      {
        awardGenBatchDAO.dismissAlertForAwardGenManager( awardGenParticipant );
      }
    }
  }

  public void deleteAwardGenAwards( List<AwardGenAward> awardGenAwards ) throws ServiceErrorException
  {
    Iterator awardGenAwardIter = awardGenAwards.iterator();
    while ( awardGenAwardIter.hasNext() )
    {
      AwardGenAward awardGenAward = (AwardGenAward)awardGenAwardIter.next();
      if ( awardGenAward.isDeleted() )
      {
        awardGeneratorDAO.deleteAwardGenAward( awardGenAward );
      }
    }
  }

  public List<Long> getAllManagersByBatchId( Long batchId )
  {
    return awardGenBatchDAO.getAllManagersByBatchId( batchId );
  }

  public void deleteAwardGenPlateauAwards( List<AwardGenPlateauFormBean> plateauValuesFormBeans, boolean awardActive ) throws ServiceErrorException
  {
    Iterator formBeansIterator = plateauValuesFormBeans.iterator();
    while ( formBeansIterator.hasNext() )
    {
      AwardGenPlateauFormBean pFormBean = (AwardGenPlateauFormBean)formBeansIterator.next();
      boolean deleted = pFormBean.isDeleted();
      if ( deleted )
      {
        Long id = new Long( 0 );
        if ( !awardActive )
        {
          id = pFormBean.getAwardInactiveAwardGenId();
          if ( id != null && !id.equals( 0L ) )
          {
            AwardGenAward awardGenAward = awardGeneratorDAO.getAwardGenAwardById( id );
            awardGeneratorDAO.deleteAwardGenAward( awardGenAward );
          }
        }
        else
        {
          Iterator pValueBeansIterator = pFormBean.getPlateauValueBeanList().iterator();
          while ( pValueBeansIterator.hasNext() )
          {
            AwardGenPlateauValueBean pValueBean = (AwardGenPlateauValueBean)pValueBeansIterator.next();
            id = pValueBean.getId();
            if ( id != null && !id.equals( 0L ) )
            {
              AwardGenAward awardGenAward = awardGeneratorDAO.getAwardGenAwardById( id );
              awardGeneratorDAO.deleteAwardGenAward( awardGenAward );
            }
          }
        }
      }
    }
  }

  /**
   * 
   * DAOs and Services
   */
  public AwardGeneratorDAO getAwardGeneratorDAO()
  {
    return awardGeneratorDAO;
  }

  public void setAwardGeneratorDAO( AwardGeneratorDAO awardGeneratorDAO )
  {
    this.awardGeneratorDAO = awardGeneratorDAO;
  }

  public AwardGenBatchDAO getAwardGenBatchDAO()
  {
    return awardGenBatchDAO;
  }

  public void setAwardGenBatchDAO( AwardGenBatchDAO awardGenBatchDAO )
  {
    this.awardGenBatchDAO = awardGenBatchDAO;
  }

  public UserCharacteristicService getUserCharacteristicService()
  {
    return userCharacteristicService;
  }

  public void setUserCharacteristicService( UserCharacteristicService userCharacteristicService )
  {
    this.userCharacteristicService = userCharacteristicService;
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

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public void setPaxService( ParticipantService participantService )
  {
    this.paxService = participantService;
  }

  public ParticipantService getPaxService()
  {
    return paxService;
  }

  public PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

}
