/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/fileload/ImportService.java,v $
 */

package com.biperf.core.service.fileload;

import java.util.Date;
import java.util.List;

import com.biperf.core.domain.enums.FileImportApprovalType;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.SAO;
import com.biperf.core.value.MailingBatchHolder;

/**
 * ImportService.
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
 * <td>crosenquest</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ImportService extends SAO
{
  public static final String BEAN_NAME = "importService";

  /**
   * Deletes any UserNodes that reference a deleted Node
   */
  public void cleanupUserNodes();

  /**
   * Deletes the given import file.
   * 
   * @param id the ID of the import file to delete.
   */
  public void deleteImportFile( Long id );

  /**
   * Returns the specified import file.
   * 
   * @param id the ID of the import file to select.
   * @return the specified import file.
   */
  public ImportFile getImportFile( Long id );

  /**
   * Returns the specified import file.
   * 
   * @param id the ID of the import file to select.
   * @param associationRequest hydrates component collections of the specified import file.
   * @return the specified import file.
   */
  public ImportFile getImportFile( Long id, AssociationRequest associationRequest );

  /**
   * Returns the import files that meet the given criteria.
   * 
   * @param fileNamePrefix a prefix of the import file name or null if import file name is not a
   *          selection criteria.
   * @param filetype the import file status or null if file type is not a selection criteria.
   * @param status the import file status or null if import file status is not a selection criteria.
   * @param startDate the earliest staging date.
   * @param endDate the latest staging date.
   * @return the import files that meet the given criteria, as a <code>List</code> of
   *         {@link ImportFile} objects.
   */
  public List getImportFiles( String fileNamePrefix, ImportFileTypeType filetype, ImportFileStatusType status, Date startDate, Date endDate );

  /**
   * Imports the specified import file a page at a time.
   * 
   * @param id the id of the import file to import.
   * @param pageNumber the page number to process.
   * @param runByUser the User who launched this process
   * @return boolean
   * @throws Exception
   */
  public boolean importImportFile( Long id, int pageNumber, User runByUser ) throws Exception;

  /**
   * Imports the specified import file a page at a time.
   * 
   * @param id the id of the import file to import.
   * @param pageNumber the page number to process.
   * @param runByUser the User who launched this process
   * @param mailingBatchHolder holder for global import processing where batch emails are enables.
   * @return boolean
   * @throws Exception
   */
  public boolean importImportFile( Long id, int pageNumber, User runByUser, MailingBatchHolder mailingBatchHolder ) throws Exception;

  /**
   * Binds the specified hierarchy to the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the hierarchy.
   * @param hierarchyId the ID of the hierarchy to bind to the import file.
   */
  public void setHierarchy( Long importFileId, Long hierarchyId );

  /**
   * Binds the specified file import approval type to the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the hierarchy.
   * @param fileImportApprovalType the ID of the fileImportApprovalType to bind to the import file.
   */
  public void setFileImportApprovalType( Long importFileId, FileImportApprovalType fileImportApprovalType );

  /**
   * Binds the specified hierarchy to the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the hierarchy.
   * @param messageId the ID of the email copy message to bind to the import file.
   */
  public void setMessage( Long importFileId, Long messageId );

  /**
   * Binds the specified promotion to the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the promotion.
   * @param promotionId the ID of the promotion to bind to the import file.
   */
  public void setPromotion( Long importFileId, Long promotionId );

  /**
   * Binds the specified value to the replaceValues of the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the promotion.
   * @param replaceValues a Boolean indicating if values should be updated or replaced.
   */
  public void setReplaceValues( Long importFileId, Boolean replaceValues );

  /**
   * Binds the specified value to the country of the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the promotion.
   * @param countryId a Long indicating what country this import file is being run for
   */
  public void setCountry( Long importFileId, Long countryId );

  /**
   * Binds the specified value to the action of the specified import file.
   * 
   * @param importFileId the ID of the import file to bind.
   * @param action a Boolean indicating if values should be updated or deleted.
   */
  public void setActionType( Long importFileId, String action );

  /**
   *  Binds the specified value to the asOfDate of the specified import file.
   * 
   * @param importFileId the ID of the import file.
   * @param asOfDate a Date indicating the as 0f date of the leaderboard.
   */
  public void setasOfDate( Long importFileId, Date asOfDate );

  /**
   *  Binds the specified value to the asOfDate of the specified import file.
   * 
   * @param importFileId the ID of the import file.
   * @param earnedDate a Date indicating the as badge earnedDate.
   */
  public void setEarnedDate( Long importFileId, Date earnedDate );

  /**
   *  Binds the specified value to the asOfDate of the specified import file.
   * 
   * @param importFieldId the ID of the import file to bind.
   * @param leaderBoardId an Id indicating the leaderboard.
   */
  public void setleaderBoardId( Long importFileId, Long leaderBoardId );

  /**
   *  Binds the specified value to the badgeId of the specified import file.
   * 
   * @param importFieldId the ID of the import file to bind.
   * @param badgeId an Id indicating the badge.
   */
  public void setBadgeId( Long importFileId, Long badgeId );

  /**
   * Binds the specified value to the progressEndDate of the specified import file.
   * 
   * @param importFileId the ID of the import file to bind to the promotion.
   * @param progressEndDate a Date indicating the progress end date of the progress load.
   */
  public void setProgressEndDate( Long importFileId, Date progressEndDate );

  /**
   * Verifies the specified import file a page at a time.
   * 
   * @param id the id of the import file to import.
   * @param pageNumber the page number to process.
   * @param runByUser the User who launched this process
   * @return boolean false when the entire file is verified
   * @throws Exception
   */

  /**
   *  Binds the specified value to the budgetSegmentId of the specified import file.
   * 
   * @param importFieldId the ID of the import file to bind.
   * @param budgetSegmentId an Id indicating the Budget Segment.
   */
  public void setBudgetSegmentId( Long importFileId, Long budgetSegmentId );

  public boolean verifyImportFile( Long id, int pageNumber, User runByUser ) throws Exception;

  /**
   * Deletes a list of files. Overridden from
   * 
   * @param fileIdList - List of file ids to be deleted
   */
  public void deleteImportFiles( List fileIdList );

  /** 
   * Returns records without errors by page
   * @param id the id of the import file whose records has to import
   * @param pageNum the page number to process
   * @param pageSize the number of records to be shown by page
   * @param fileType the type of import file 
   * @param associationRequest hydrates domain object association
   * @return List
   */
  public List getRecordsbyPage( Long id, int pageNum, int pageSize, String fileType, AssociationRequest associationRequest );

  /** 
   * Returns records with errors by page
   * @param id the id of the import file whose records has to import
   * @param pageNum the page number to process
   * @param pageSize the number of records to be shown by page
   * @param fileType the type of import file 
   * @param associationRequest hydrates domain object association
   * @return List
   */
  public List getRecordsWithErrorByPage( Long id, int pageNum, int pageSize, String fileType, AssociationRequest associationRequest );

  public void setImportFileStatus( Long importFileId, String statusCode );

  /**
   * @param importFile
   * @return ImportFile
   */
  public ImportFile saveRecognitionOptions( ImportFile importFile );

  public ImportFile saveRecognitionAwardsLevel( ImportFile importFile );

  public List getImportFileIdsByDateImported( Date dateImported );

  public List getParticipantIdsToResendWelcomeEmailByImportFileId( Long importFileId );

  public boolean verifyRecordsComments( Long importFileId, String importFileType );

  public void setRoundNumber( Long importFileId, Integer roundNumber );

  public List getAllRecords( Long id, String importFileType, AssociationRequest associationRequest );

  public void setDelayAwardDate( Long importFileId, Date delayAwardDate );

  public void setBudgetMasterId( Long importFileId, Long budgetMasterId );

  public void setContestId( Long importFileId, Long contestId );
}
