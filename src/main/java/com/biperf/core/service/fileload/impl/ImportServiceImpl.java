/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/fileload/impl/ImportServiceImpl.java,v $
 */

package com.biperf.core.service.fileload.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.fileload.ImportFileDAO;
import com.biperf.core.dao.fileload.ImportRecordDAO;
import com.biperf.core.dao.gamification.GamificationDAO;
import com.biperf.core.dao.hierarchy.HierarchyDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.leaderboard.LeaderBoardDAO;
import com.biperf.core.dao.message.MessageDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.FileImportApprovalType;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ImportRecord;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.MailingBatchHolder;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * ImportServiceImpl.
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
 * <td>kumars</td>
 * <td>Apr 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ImportServiceImpl implements ImportService
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private static final Log log = LogFactory.getLog( ImportServiceImpl.class );

  private HierarchyDAO hierarchyDAO;
  private ImportFileDAO importFileDAO;
  private ImportRecordDAO importRecordDAO;
  private MessageDAO messageDAO;
  private PromotionDAO promotionDAO;
  private LeaderBoardDAO leaderBoardDAO;
  private NodeDAO nodeDAO;
  private GamificationDAO gamificationDAO;
  private CountryDAO countryDAO;

  /**
   * Strategies for importing import files. The strategies vary by import file type. Maps import
   * file type codes (<code>String</code> objects) to import strategies ({@link ImportStrategy}
   * objects).
   */
  private Map importStrategies;

  // ---------------------------------------------------------------------------
  // Service Methods
  // ---------------------------------------------------------------------------

  /**
   * Deletes any UserNodes that reference a deleted Node
   */
  public void cleanupUserNodes()
  {
    List nodes = getNodeDAO().getAllDeletedNodes();
    for ( Iterator iter = nodes.iterator(); iter.hasNext(); )
    {
      Node node = (Node)iter.next();
      for ( Iterator iterator = node.getUserNodes().iterator(); iterator.hasNext(); )
      {
        UserNode userNode = (UserNode)iterator.next();
        userNode.getUser().getUserNodes().remove( userNode ); // remove it from the collection on
        // the User
        iterator.remove(); // remove it from the collection the Node
      }
    }
  }

  /**
   * Deletes a list of files. Overridden from
   * 
   * @param fileIdList - List of file ids to be deleted
   */
  public void deleteImportFiles( List fileIdList )
  {
    Iterator fileIdIter = fileIdList.iterator();

    while ( fileIdIter.hasNext() )
    {
      this.deleteImportFile( (Long)fileIdIter.next() );
    }

  }

  /**
   * Deletes the given import file.
   * 
   * @param id the ID of the import file to delete.
   */
  public void deleteImportFile( Long id )
  {

    importFileDAO.deleteImportFile( id );
  }

  /**
   * Returns the specified import file.
   * 
   * @param id the ID of the import file to select.
   * @return the specified import file.
   */
  public ImportFile getImportFile( Long id )
  {
    return importFileDAO.getImportFile( id );
  }

  /**
   * Returns the specified import file.
   * 
   * @param id the ID of the import file to select.
   * @param associationRequest hydrates component collections of the specified import file.
   * @return the specified import file.
   */
  public ImportFile getImportFile( Long id, AssociationRequest associationRequest )
  {
    return importFileDAO.getImportFile( id, associationRequest );
  }

  /**
   * Returns the import files that meet the given criteria.
   * 
   * @param fileNamePrefix a prefix of the import file name or null if import file name is not a
   *          selection criteria.
   * @param fileType the import file status or null if file type is not a selection criteria.
   * @param status the import file status or null if import file status is not a selection criteria.
   * @param startDate the earliest staging date.
   * @param endDate the latest staging date.
   * @return the import files that meet the given criteria, as a <code>List</code> of
   *         {@link ImportFile} objects.
   */
  public List getImportFiles( String fileNamePrefix, ImportFileTypeType fileType, ImportFileStatusType status, Date startDate, Date endDate )
  {
    return importFileDAO.getImportFiles( fileNamePrefix, fileType, status, startDate, endDate );
  }

  /**
   * Imports the specified import file.  Implements the default non-bulk email enabled
   * API
   * 
   * @param id the id of the import file to import.
   * @param pageNumber pageNumber the page number to process.
   * @param runByUser the User who launched this process
   * @return boolean
   * @throws Exception
   */
  public boolean importImportFile( Long id, int pageNumber, User runByUser ) throws Exception
  {
    return importImportFile( id, pageNumber, runByUser, null );
  }

  /**
   * Imports the specified import file. Overridden from
   * 
   * @param id the id of the import file to import.
   * @param pageNumber pageNumber the page number to process.
   * @param runByUser the User who launched this process
   * @param mailingBatchHolder the holder for batch email elements - both pax and partner emails
   * @return boolean
   * @throws Exception
   */
  public boolean importImportFile( Long id, int pageNumber, User runByUser, MailingBatchHolder mailingBatchHolder ) throws Exception
  {
    boolean fullResultSet = true;
    int pageSize = 0;
    // Get the import file.
    ImportFile importFile = importFileDAO.getImportFile( id );

    // Save the in process status
    importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORT_IN_PROCESS ) );
    importFile = getImportFileDAO().saveImportFile( importFile );

    if ( importFile.getHierarchy() != null )
    {
      String cmAssetCode = importFile.getHierarchy().getCmAssetCode();
      String cmItemKey = importFile.getHierarchy().getNameCmKey();
      String name = ContentReaderManager.getText( cmAssetCode, cmItemKey );
      importFile.getHierarchy().setName( name );
    }

    // Get the import strategy.
    String importFileTypeCode = importFile.getFileType().getCode();
    ImportStrategy importStrategy = (ImportStrategy)importStrategies.get( importFileTypeCode );

    // Import the specified page of the import file. NOTE: this will be all records if Hierarchy or
    // Quiz
    try
    {
      // - Get page size from sysvar vs. static constant
      // ------------------------------------------------------
      // bug 73458 changed deposit file loads to commit after every record
      // because we are calling a web service to do deposits. We cannot have
      // a rollback happen after the deposit web service is successful.
      if ( importFileTypeCode.equals( ImportFileTypeType.DEPOSIT ) )
      {
        pageSize = 1;
      }
      else
      {
        SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
        PropertySetItem property = systemVariableService.getPropertyByName( SystemVariableService.IMPORT_PAGE_SIZE );
        pageSize = property.getIntVal();
      }

      List records = getImportRecordDAO().getRecordsByPageNumber( importFile.getId(), pageNumber, pageSize, importFileTypeCode );
      fullResultSet = records.size() == pageSize;
      if ( importFileTypeCode.equals( ImportFileTypeType.HIERARCHY ) || importFileTypeCode.equals( ImportFileTypeType.PARTICIPANT ) || importFileTypeCode.equals( ImportFileTypeType.BADGE )
          || importFileTypeCode.equals( ImportFileTypeType.QUIZ ) || importFileTypeCode.equals( ImportFileTypeType.BUDGET ) || importFileTypeCode.equals( ImportFileTypeType.BUDGET_DISTRIBUTION )
          || importFileTypeCode.equals( ImportFileTypeType.NOMINATION_APPROVER ) )
      {
        fullResultSet = false;
      }
      log.warn( "Starting import of " + records.size() + " records." );
      importStrategy.importImportFile( importFile, records, mailingBatchHolder );
      log.warn( "Completed import of " + records.size() + " records." );
      if ( !fullResultSet && ! ( importFileTypeCode.equals( ImportFileTypeType.HIERARCHY ) || importFileTypeCode.equals( ImportFileTypeType.BADGE )
          || importFileTypeCode.equals( ImportFileTypeType.PARTICIPANT ) || importFileTypeCode.equals( ImportFileTypeType.BUDGET )
          || importFileTypeCode.equals( ImportFileTypeType.BUDGET_DISTRIBUTION ) || importFileTypeCode.equals( ImportFileTypeType.NOMINATION_APPROVER ) ) )
      {
        /* Remove logic added by bug 72152 */
        /*
         * if ( importFile.getRecognitionDeposit() != null &&
         * importFile.getRecognitionDeposit().booleanValue() &&
         * importFile.getImportRecordErrorCount() > 0 ) { importFile.setStatus(
         * ImportFileStatusType.lookup( ImportFileStatusType.IMPORT_FAILED ) ); } else {
         * importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) ); }
         */
        importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
        if ( runByUser != null )
        {
          importFile.setImportedBy( runByUser.getUserName() );
        }
        importFile.setDateImported( DateUtils.getCurrentDate() );
        getImportFileDAO().saveImportFile( importFile );
      }
    }
    catch( Exception e )
    {
      log.error( "Error during importing file", e );
      throw e;
    }

    return fullResultSet;
  }

  /**
   * Binds the specified hierarchy to the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the hierarchy.
   * @param hierarchyId the ID of the hierarchy to bind to the import file.
   */
  public void setHierarchy( Long importFileId, Long hierarchyId )
  {
    Hierarchy hierarchy = hierarchyDAO.getById( hierarchyId );
    ImportFile importFile = importFileDAO.getImportFile( importFileId );

    importFile.setHierarchy( hierarchy );
  }

  /**
   * Binds the specified file import approval type to the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the hierarchy.
   * @param fileImportApprovalType the ID of the fileImportApprovalType to bind to the import file.
   */
  public void setFileImportApprovalType( Long importFileId, FileImportApprovalType fileImportApprovalType )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );

    importFile.setFileImportApprovalType( fileImportApprovalType );
  }

  /**
   * Binds the specified hierarchy to the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the hierarchy.
   * @param messageId the ID of the email copy message to bind to the import file.
   */
  public void setMessage( Long importFileId, Long messageId )
  {
    Message message = messageDAO.getMessageById( messageId );
    ImportFile importFile = importFileDAO.getImportFile( importFileId );

    importFile.setMessage( message );
  }

  /**
   * Binds the specified promotion to the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the promotion.
   * @param promotionId the ID of the promotion to bind to the import file.
   */
  public void setPromotion( Long importFileId, Long promotionId )
  {
    Promotion promotion = promotionDAO.getPromotionById( promotionId );
    ImportFile importFile = importFileDAO.getImportFile( importFileId );
    importFile.setPromotion( promotion );
  }

  /**
   * Binds the specified value to the replaceValues of the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the promotion.
   * @param replaceValues a Boolean indicating if values should be updated or replaced.
   */
  public void setReplaceValues( Long importFileId, Boolean replaceValues )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );

    importFile.setReplaceValues( replaceValues );
  }

  /**
   * Binds the specified value to the country of the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the promotion.
   * @param countryId a Long indicating what country this import file is being run for
   */
  public void setCountry( Long importFileId, Long countryId )
  {
    Country country = countryDAO.getCountryById( countryId );
    ImportFile importFile = importFileDAO.getImportFile( importFileId );
    importFile.setCountry( country );
  }

  /**
   * Binds the specified value to the action of the specified import file.
   * 
   * @param importFileId the ID of the import file to bind.
   * @param action a Boolean indicating if values should be updated or deleted.
   */
  public void setActionType( Long importFileId, String action )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );
    importFile.setActionType( action );
  }

  public void setleaderBoardId( Long importFileId, Long leaderBoardId )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );
    importFile.setLeaderboardId( leaderBoardId );
    // making sure the persisted obj is saved QC bug #2678.
    importFileDAO.saveImportFile( importFile );

  }

  public void setBadgeId( Long importFileId, Long badgeId )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );
    importFile.setBadgepromotionId( badgeId );
    importFileDAO.saveImportFile( importFile );
  }

  /**
   * Binds the specified import file status to the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the import file.
   * @param statusCode the statusCode to bind to the import file.
   */
  public void setImportFileStatus( Long importFileId, String statusCode )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );

    importFile.setStatus( ImportFileStatusType.lookup( statusCode ) );
  }

  /**
   * Binds the specified import file status to the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the import file.
   * @param progressEndDate the progressEndDate of the import file.
   */
  public void setProgressEndDate( Long importFileId, Date progressEndDate )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );

    importFile.setProgressEndDate( progressEndDate );
  }

  public void setRoundNumber( Long importFileId, Integer roundNumber )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );

    importFile.setRoundNumber( roundNumber );
  }

  public void setBudgetSegmentId( Long importFileId, Long budgetSegmentId )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );
    importFile.setBudgetSegmentId( budgetSegmentId );
    importFileDAO.saveImportFile( importFile );
  }

  public void setDelayAwardDate( Long importFileId, Date delayAwardDate )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );

    importFile.setDelayAwardDate( delayAwardDate );
  }

  /**
   * Verifies the specified import file a page at a time. Overridden from
   * 
   * @param id the id of the import file to import.
   * @param pageNumber the page number to process.
   * @param runByUser the User who launched this process
   * @throws Exception
   * @return boolean false when the entire file is verified
   */
  public boolean verifyImportFile( Long id, int pageNumber, User runByUser ) throws Exception
  {
    boolean fullResultSet = true;
    int pageSize = 0;
    // Get the import file.
    ImportFile importFile = importFileDAO.getImportFile( id );

    // Save the in process status
    importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_IN_PROCESS ) );
    log.debug( "IMPORT FILE TYPE CODE: " + importFile.getFileType().getCode() );
    importFile = getImportFileDAO().saveImportFile( importFile );

    // Get the verification strategy.
    String importFileTypeCode = importFile.getFileType().getCode();
    log.debug( "IMPORT FILE TYPE CODE: " + importFileTypeCode );
    ImportStrategy importStrategy = (ImportStrategy)importStrategies.get( importFileTypeCode );

    // Verify the specified page of the import file. NOTE: this will be all records if Hierarchy or
    // Quiz
    try
    {
      // - Get page size from sysvar vs. static constant
      // ------------------------------------------------------
      SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
      PropertySetItem property = systemVariableService.getPropertyByName( SystemVariableService.IMPORT_PAGE_SIZE );
      pageSize = property.getIntVal();
      // Award level file each record need to commit.Bug fix # 32081
      if ( importFileTypeCode.equals( ImportFileTypeType.AWARD_LEVEL ) )
      {
        pageSize = 1;
      }
      // ---------------------------

      List records = getImportRecordDAO().getRecordsByPageNumber( importFile.getId(), pageNumber, pageSize, importFileTypeCode );
      fullResultSet = records.size() == pageSize;
      if ( importFileTypeCode.equals( ImportFileTypeType.QUIZ ) || importFileTypeCode.equals( ImportFileTypeType.PARTICIPANT ) || importFileTypeCode.equals( ImportFileTypeType.BADGE )
          || importFileTypeCode.equals( ImportFileTypeType.HIERARCHY ) || importFileTypeCode.equals( ImportFileTypeType.BUDGET ) || importFileTypeCode.equals( ImportFileTypeType.BUDGET_DISTRIBUTION )
          || importFileTypeCode.equals( ImportFileTypeType.NOMINATION_APPROVER ) )
      {
        fullResultSet = false;
      }
      log.warn( "Starting verification of " + records.size() + " records." );
      importStrategy.verifyImportFile( importFile, records, !fullResultSet );
      log.warn( "Completed verification of " + records.size() + " records." );
      if ( !fullResultSet && ! ( importFileTypeCode.equals( ImportFileTypeType.HIERARCHY ) || importFileTypeCode.equals( ImportFileTypeType.BADGE )
          || importFileTypeCode.equals( ImportFileTypeType.PARTICIPANT ) || importFileTypeCode.equals( ImportFileTypeType.BUDGET )
          || importFileTypeCode.equals( ImportFileTypeType.BUDGET_DISTRIBUTION ) || importFileTypeCode.equals( ImportFileTypeType.NOMINATION_APPROVER ) ) )
      {
        importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFIED ) );
        if ( runByUser != null )
        {
          importFile.setVerifiedBy( runByUser.getUserName() );
        }
        importFile.setDateVerified( DateUtils.getCurrentDate() );
      }
      getImportFileDAO().saveImportFile( importFile );
    }
    catch( Exception e )
    {
      log.error( "Error during verifying file", e );
      throw e;
    }

    return fullResultSet;
  }

  private String getUserFullName( User runByUser )
  {
    String middleInitial = "";
    if ( runByUser.getMiddleName() != null )
    {
      middleInitial = runByUser.getMiddleName().substring( 0, 1 );
    }
    return runByUser.getFirstName() + " " + middleInitial + " " + runByUser.getLastName();
  }

  /**
   * Returns records without errors by page
   * 
   * @param id the id of the import file whose records has to import
   * @param pageNum the page number to process
   * @param pageSize the number of records to be shown by page
   * @param fileType the type of import file
   * @param associationRequest hydrates domain object association
   * @return List
   */
  public List getRecordsbyPage( Long id, int pageNum, int pageSize, String fileType, AssociationRequest associationRequest )
  {
    List importrecords = getImportRecordDAO().getRecordsWithoutErrorByPageNumber( id, pageNum, pageSize, fileType, associationRequest );
    return importrecords;
  }

  /**
   * Returns records with errors by page
   * 
   * @param id the id of the import file whose records has to import
   * @param pageNum the page number to process
   * @param pageSize the number of records to be shown by page
   * @param fileType the type of import file
   * @param associationRequest hydrates domain object association
   * @return List
   */
  public List getRecordsWithErrorByPage( Long id, int pageNum, int pageSize, String fileType, AssociationRequest associationRequest )
  {
    List importrecords = getImportRecordDAO().getRecordsWithErrorByPageNumber( id, pageNum, pageSize, fileType, associationRequest );
    return importrecords;
  }

  /**
   * Overridden from @see com.biperf.core.service.fileload.ImportService#saveRecognitionOptions(com.biperf.core.domain.fileload.ImportFile)
   * @param importFile
   * @return ImportFile
   */
  public ImportFile saveRecognitionOptions( ImportFile importFile )
  {
    return getImportFileDAO().saveImportFile( importFile );
  }

  /**
   * Overridden from @see com.biperf.core.service.fileload.ImportService#saveRecognitionAwardsLevel(com.biperf.core.domain.fileload.ImportFile)
   * @param importFile
   * @return ImportFile
   */

  public ImportFile saveRecognitionAwardsLevel( ImportFile importFile )
  {
    return getImportFileDAO().saveImportFile( importFile );
  }

  public List getImportFileIdsByDateImported( Date dateImported )
  {
    return getImportFileDAO().getImportFileIdsByDateImported( dateImported );
  }

  public List getParticipantIdsToResendWelcomeEmailByImportFileId( Long importFileId )
  {
    return getImportFileDAO().getParticipantIdsToResendWelcomeEmailByImportFileId( importFileId );
  }

  public List getAllRecords( Long id, String importFileType, AssociationRequest associationRequest )
  {
    List<ImportRecord> records = importRecordDAO.getAllRecords( id, importFileType );
    for ( ImportRecord record : records )
    {
      if ( associationRequest != null )
      {
        associationRequest.execute( record );
      }
    }
    return records;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public HierarchyDAO getHierarchyDAO()
  {
    return hierarchyDAO;
  }

  public void setHierarchyDAO( HierarchyDAO hierarchyDAO )
  {
    this.hierarchyDAO = hierarchyDAO;
  }

  public LeaderBoardDAO getLeaderBoardDAO()
  {
    return leaderBoardDAO;
  }

  public void setLeaderBoardDAO( LeaderBoardDAO leaderBoardDAO )
  {
    this.leaderBoardDAO = leaderBoardDAO;
  }

  public GamificationDAO getGamificationDAO()
  {
    return gamificationDAO;
  }

  public void setGamificationDAO( GamificationDAO gamificationDAO )
  {
    this.gamificationDAO = gamificationDAO;
  }

  public CountryDAO getCountryDAO()
  {
    return countryDAO;
  }

  public void setCountryDAO( CountryDAO countryDAO )
  {
    this.countryDAO = countryDAO;
  }

  public ImportFileDAO getImportFileDAO()
  {
    return importFileDAO;
  }

  public void setImportFileDAO( ImportFileDAO importFileDAO )
  {
    this.importFileDAO = importFileDAO;
  }

  public ImportRecordDAO getImportRecordDAO()
  {
    return importRecordDAO;
  }

  public void setImportRecordDAO( ImportRecordDAO importRecordDAO )
  {
    this.importRecordDAO = importRecordDAO;
  }

  public Map getImportStrategies()
  {
    return importStrategies;
  }

  public void setImportStrategies( Map importStrategies )
  {
    this.importStrategies = importStrategies;
  }

  public MessageDAO getMessageDAO()
  {
    return messageDAO;
  }

  public void setMessageDAO( MessageDAO messageDAO )
  {
    this.messageDAO = messageDAO;
  }

  public NodeDAO getNodeDAO()
  {
    return nodeDAO;
  }

  public void setNodeDAO( NodeDAO nodeDAO )
  {
    this.nodeDAO = nodeDAO;
  }

  public PromotionDAO getPromotionDAO()
  {
    return promotionDAO;
  }

  public void setPromotionDAO( PromotionDAO promotionDAO )
  {
    this.promotionDAO = promotionDAO;
  }

  public void setasOfDate( Long importFileId, Date asOfDate )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );

    importFile.setAsOfDate( asOfDate );

  }

  public void setEarnedDate( Long importFileId, Date earnedDate )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );

    importFile.setEarnedDate( earnedDate );
  }

  @Override
  public boolean verifyRecordsComments( Long importFileId, String importFileType )
  {
    return importRecordDAO.verifyRecordsComments( importFileId, importFileType );
  }

  @Override
  public void setBudgetMasterId( Long importFileId, Long budgetMasterId )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );
    importFile.setBudgetMasterId( budgetMasterId );
    importFileDAO.saveImportFile( importFile );
  }

  @Override
  public void setContestId( Long importFileId, Long contestId )
  {
    ImportFile importFile = importFileDAO.getImportFile( importFileId );
    importFile.setContestId( contestId );
    importFileDAO.saveImportFile( importFile );

  }

}
