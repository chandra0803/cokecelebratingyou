/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/utils/crypto/PasswordGeneratorTest.java,v $
 */

package com.biperf.core.utils.crypto;

import junit.framework.TestCase;

/*
 * PasswordGeneratorTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Oct
 * 11, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class PasswordGeneratorTest extends TestCase
{
  private static final int PASSWORD_LENGTH = 24;

  public void testGeneratePassword1()
  {
    String password = PasswordGenerator.generatePassword();
    assertNotNull( password );
    assertTrue( password.length() > 0 );
  }

  public void testGeneratePassword2()
  {
    String password = PasswordGenerator.generatePassword( PASSWORD_LENGTH );
    assertNotNull( password );
    assertEquals( password.length(), PASSWORD_LENGTH );
  }
}
