/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/utils/crypto/TripleDesCipherTest.java,v $
 */

package com.biperf.core.utils.crypto;

import junit.framework.TestCase;

/**
 * TripleDesCipherTest.
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
 * <td>dunne</td>
 * <td>Mar 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class TripleDesCipherTest extends TestCase
{

  public void testEncryptString()
  {
    BeaconCipher cipher = CryptoFactory.getTripleDesCipherInstance();
    String encr;
    try
    {
      encr = cipher.encrypt( "TripleDesCipherTest" );
      System.out.println( encr );
    }
    catch( Exception e )
    {
      e.getCause().printStackTrace();
      fail();
    }

  }

  public void testDecryptString()
  {
    try
    {
      for ( int i = 0; i < 1000; i++ )
      {
        BeaconCipher cipher = CryptoFactory.getTripleDesCipherInstance();
        String encr = cipher.encrypt( "TripleDesCipherTest" );
        // assertEquals(encr,"IGtuXWcF3lG6+we6R7ztXwjGVyRgCIKR");
        // System.out.println(encr);
        String decr = cipher.decrypt( encr );
        // System.out.println(encr);
        assertEquals( decr, "TripleDesCipherTest" );
      }
    }
    catch( CryptoException e )
    {
      e.getCause().printStackTrace();
      fail();
    }
  }

}
