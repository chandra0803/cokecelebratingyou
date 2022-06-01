/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/utils/crypto/MD5HashTest.java,v $
 */

package com.biperf.core.utils.crypto;

import junit.framework.TestCase;

/**
 * MD5HashTest.
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
 * <td>zahler</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MD5HashTest extends TestCase
{
  /**
   * Test for encrypt method
   * 
   * @throws Exception
   */
  public void testEncrypt() throws Exception
  {
    String toEncrypt = "password";
    String hashExpected = "{MD5}319F4D26E3C536B5DD871BB2C52E3178";

    String hashed = new MD5Hash().encrypt( toEncrypt );

    assertEquals( "Password not hashed correctly", hashExpected, hashed );

  }

  public void testEncryptMD5WithSalt()
  {
    String toEncrypt = "password";
    String salt = "salt";
    String hashExpected = "ce421738b1c5540836bdc8ff707f1572";
    String hashed = new MD5Hash().encryptWithSalt( toEncrypt, salt );
    assertTrue( hashed.equals( hashExpected ) );
  }

  public void testAlreadyEncrypted() throws Exception
  {
    String toEncrypt = "{MD5}ALREADY_ENCRYPTED";
    String expected = "{MD5}ALREADY_ENCRYPTED";

    String hashed = new MD5Hash().encrypt( toEncrypt );

    assertEquals( expected, hashed );
  }
}
