/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/utils/crypto/CryptoSuite.java,v $
 */

package com.biperf.core.utils.crypto;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Crypto test suite for running all the encryption utility tests out of container.
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
 * <td>Sathish</td>
 * <td>May 27,, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CryptoSuite extends TestSuite
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.utils.crypto.CryptoSuite" );

    suite.addTestSuite( ByteArrayGuardTest.class );
    suite.addTestSuite( MD5HashTest.class );
    suite.addTestSuite( PasswordGeneratorTest.class );
    suite.addTestSuite( TripleDesCipherTest.class );

    return suite;
  }

}
