/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/utils/crypto/SHA256HashTest.java,v $
 */

package com.biperf.core.utils.crypto;

import junit.framework.TestCase;

/**
 * SHA256HashTest.
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
 * <td>hamza</td>
 * <td>Mar 24, 2016</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SHA256HashTest extends TestCase
{
  /**
   * Test for encrypt method
   * 
   * @throws Exception
   */
  public void testEncrypt() throws Exception
  {
    String toEncrypt = "password";
    String hashExpected = "{V2}0BE64AE89DDD24E225434DE95D501711339BAEEE18F009BA9B4369AF27D30D60";

    String hashed = new SHA256Hash().encrypt( toEncrypt );

    assertEquals( "Password not hashed correctly", hashExpected, hashed );

  }

  /*
   * public void testEncryptMD5WithSalt() { String toEncrypt = "password" ; String salt = "salt" ;
   * String hashExpected = "ce421738b1c5540836bdc8ff707f1572" ; String hashed = new
   * SHA256Hash().encryptWithSalt( toEncrypt, salt ) ; assertTrue( hashed.equals( hashExpected ) ) ;
   * } public void testAlreadyEncrypted() throws Exception { String toEncrypt =
   * "{MD5}ALREADY_ENCRYPTED"; String expected = "{MD5}ALREADY_ENCRYPTED"; String hashed = new
   * SHA256Hash().encrypt( toEncrypt ); assertEquals( expected, hashed ); }
   */
}