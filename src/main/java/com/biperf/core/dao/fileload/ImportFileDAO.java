/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/fileload/ImportFileDAO.java,v $
 */

package com.biperf.core.dao.fileload;

import java.util.Date;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.service.AssociationRequest;

/**
 * CountryDAO.
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
 * <td>sedey</td>
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface ImportFileDAO extends DAO
{
  static final String BEAN_NAME = "importFileDAO";

  /**
   * Deletes the given import file.
   * 
   * @param importFile the import file to delete.
   */
  void deleteImportFile( ImportFile importFile );

  /**
   * Gets an import file given its ID.
   * 
   * @param id the ID of the import file to get.
   * @return the specified import file.
   */
  ImportFile getImportFile( Long id );

  /**
   * Gets an import file given its ID.
   * 
   * @param id the ID of the import file to get.
   * @param associationRequest hydrates component collections of the specified import file.
   * @return the specified import file.
   */
  ImportFile getImportFile( Long id, AssociationRequest associationRequest );

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
  List getImportFiles( String fileNamePrefix, ImportFileTypeType fileType, ImportFileStatusType status, Date startDate, Date endDate );

  /**
   * Saves the given import file.
   * 
   * @param importFile the import file to save.
   * @return the saved version of the import file.
   */
  ImportFile saveImportFile( ImportFile importFile );

  List getImportFileIdsByDateImported( Date dateImported );

  List getParticipantIdsToResendWelcomeEmailByImportFileId( Long importFileId );

  Date getLastFileLoadDateForPromotionAndRound( Long promotionId, int roundNumber );

  Date getLastFileLoadDateForPromotion( Long promotionId );

  public List<ImportFile> getImportFileByPromotionId( Long id );

  public void deleteImportFile( Long id );

}
