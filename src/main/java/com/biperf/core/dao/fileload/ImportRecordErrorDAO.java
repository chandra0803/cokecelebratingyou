/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/fileload/ImportRecordErrorDAO.java,v $
 */

package com.biperf.core.dao.fileload;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.fileload.ImportRecordError;

public interface ImportRecordErrorDAO extends DAO
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The name of the bean that implements this interface in the application
   * context.
   */
  static final String BEAN_NAME = "importRecordErrorDAO";


  // ---------------------------------------------------------------------------
  // Methods
  // ---------------------------------------------------------------------------

  /**
   * Saves the given import record error.
   *
   * @param importRecordError  the import record error to save.
   * @return the saved version of the import record error.
   */
  ImportRecordError saveImportRecordError( ImportRecordError importRecordError );
}
