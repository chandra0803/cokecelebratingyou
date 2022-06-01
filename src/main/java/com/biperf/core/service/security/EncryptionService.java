/*
 * (c) 2015 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.security;

import com.biperf.core.service.SAO;

/**
 * Service interface for Encryption.
 *
 * @author kumars
 */
public interface EncryptionService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "encryptionService";

  public String getDecryptedValue( String toDecrypt );

  public String getEncryptedValue( String toEncrypt );

}
