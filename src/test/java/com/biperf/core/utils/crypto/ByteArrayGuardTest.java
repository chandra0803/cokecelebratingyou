/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/utils/crypto/ByteArrayGuardTest.java,v $
 */

package com.biperf.core.utils.crypto;

import junit.framework.TestCase;

/*
 * ByteArrayGuardTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Oct
 * 11, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ByteArrayGuardTest extends TestCase
{
  private static final String data = "The quick brown fox jumped over the lazy brown dog.";

  public void testEncryptDecrypt() throws InvalidMacException
  {
    ByteArrayGuard byteArrayGuard = new ByteArrayGuard( PasswordGenerator.generatePassword() );

    byte[] encryptedData = byteArrayGuard.encrypt( data.getBytes() );
    assertNotNull( encryptedData );
    assertTrue( encryptedData.length > 0 );
    assertTrue( ! ( new String( encryptedData ) ).equals( data ) );

    byte[] decryptedData = byteArrayGuard.decrypt( encryptedData );
    assertTrue( ( new String( decryptedData ) ).equals( data ) );
  }
}
