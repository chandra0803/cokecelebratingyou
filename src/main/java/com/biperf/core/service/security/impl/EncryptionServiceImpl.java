/*
 * (c) 2015 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.security.impl;

import com.biperf.core.dao.security.EncryptionDAO;
import com.biperf.core.service.security.EncryptionService;

/**
 * Service to manage Encryption with DAO and business logic.
 *
 * @author kumars
 */
public class EncryptionServiceImpl implements EncryptionService
{

  /** EncryptionDAO */
  private EncryptionDAO encryptionDAO;

  /**
   * Set the encryptionDAO through IoC.
   *
   * @param encryptionDAO
   */
  public void setEncryptionDAO( EncryptionDAO encryptionDAO )
  {
    this.encryptionDAO = encryptionDAO;
  }

  public String getDecryptedValue( String toDecrypt )
  {
    return encryptionDAO.getDecryptedValue( toDecrypt );
  }

  public String getEncryptedValue( String toEncrypt )
  {
    return encryptionDAO.getEncryptedValue( toEncrypt );
  }

}
