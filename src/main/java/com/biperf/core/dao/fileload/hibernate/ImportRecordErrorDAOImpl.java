/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/fileload/hibernate/ImportRecordErrorDAOImpl.java,v $
 */

package com.biperf.core.dao.fileload.hibernate;

import org.hibernate.Session;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.fileload.ImportRecordErrorDAO;
import com.biperf.core.domain.fileload.ImportRecordError;
import com.biperf.core.utils.HibernateSessionManager;

public class ImportRecordErrorDAOImpl extends BaseDAO implements ImportRecordErrorDAO
{
  /**
   * Saves the given import record error.
   *
   * @param importRecordError  the import record error to save.
   * @return the saved import record error.
   */
  public ImportRecordError saveImportRecordError( ImportRecordError importRecordError )
  {
    Session session = HibernateSessionManager.getSession();
    session.saveOrUpdate( importRecordError );

    return importRecordError;
  }
}
