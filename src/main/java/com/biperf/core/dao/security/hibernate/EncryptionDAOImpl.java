/*
 * (c) 2015 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.security.hibernate;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.security.EncryptionDAO;

/**
 * EncryptionDAOImpl.
 *
 * @author kumars
 */
public class EncryptionDAOImpl extends BaseDAO implements EncryptionDAO
{

  public String getDecryptedValue( String toDecrypt )
  {
    Query query = getSession().createSQLQuery( "select FNC_JAVA_DECRYPT(:toDecrypt) as decryptedValue from dual" );
    query.setParameter( "toDecrypt", toDecrypt );
    String decryptedValue = (String)query.uniqueResult();
    return decryptedValue;
  }

  public String getEncryptedValue( String toEncrypt )
  {
    Query query = getSession().createSQLQuery( "select FNC_JAVA_ENCRYPT(:toEncrypt) as encryptedValue from dual" );
    query.setParameter( "toEncrypt", toEncrypt );
    String encryptedValue = (String)query.uniqueResult();
    return encryptedValue;
  }

}
