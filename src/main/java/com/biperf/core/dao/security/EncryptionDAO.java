/*
 * (c) 2015 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.security;

import com.biperf.core.dao.DAO;

/**
 * EncryptionDAO is a DAO to manage Encryption.
 *
 * @author kumars
 */
public interface EncryptionDAO extends DAO
{

  public String getDecryptedValue( String toDecrypt );

  public String getEncryptedValue( String toEncrypt );

}
