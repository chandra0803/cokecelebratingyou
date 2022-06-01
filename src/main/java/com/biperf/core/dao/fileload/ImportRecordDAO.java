/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/fileload/ImportRecordDAO.java,v $
 */

package com.biperf.core.dao.fileload;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.fileload.ImportRecord;
import com.biperf.core.service.AssociationRequest;

public interface ImportRecordDAO extends DAO
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The name of the bean that implements this interface in the application
   * context.
   */
  static final String BEAN_NAME = "importRecordDAO";

  // ---------------------------------------------------------------------------
  // Methods
  // ---------------------------------------------------------------------------

  /**
   * Gets a set of import file records for a given import file ID.  Limits the
   * number of results to the pageSize passed.
   *
   * @param id the ID of the import file to get.
   * @param pageNumber the page number to retrieve data for.
   * @param pageSize the number of records to get.
   * @param importFileType identifies the type of records to retrieve
   * @return List of records for the specified import file.
   */
  List getRecordsByPageNumber( Long id, int pageNumber, int pageSize, String importFileType );

  /**
   * Gets a set of import file records without errors for a given import file ID.  Limits the
   * number of results to the pageSize passed.
   *
   * @param id the ID of the import file to get.
   * @param pageNumber the page number to retrieve data for.
   * @param pageSize the number of records to get.
   * @param importFileType identifies the type of records to retrieve
   * @param associationRequest hydrates domain object association
   * @return List of records for the specified import file.
   */
  List getRecordsWithoutErrorByPageNumber( Long id, int pageNumber, int pageSize, String importFileType, AssociationRequest associationRequest );

  /**
   * Gets a set of import file records with errors only for a given import file ID.  Limits the
   * number of results to the pageSize passed.
   *
   * @param id the ID of the import file to get.
   * @param pageNumber the page number to retrieve data for.
   * @param pageSize the number of records to get.
   * @param importFileType identifies the type of records to retrieve
   * @param associationRequest hydrates domain object association
   * @return List of records for the specified import file.
   */
  List getRecordsWithErrorByPageNumber( Long id, int pageNumber, int pageSize, String importFileType, AssociationRequest associationRequest );

  boolean verifyRecordsComments( Long importFileId, String importFileType );

  public List<ImportRecord> getAllRecords( Long id, String importFileType );

}
