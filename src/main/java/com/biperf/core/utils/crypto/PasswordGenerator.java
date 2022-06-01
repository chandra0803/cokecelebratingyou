/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/crypto/PasswordGenerator.java,v $
 */

package com.biperf.core.utils.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.biperf.core.exception.BeaconRuntimeException;

/**
 * PasswordGenerator
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
 * <td>Thomas Eaton</td>
 * <td>Oct 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * </p>
 * 
 *
 */

public class PasswordGenerator
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The default length of a password in bytes.
   */
  private static final int DEFAULT_PASSWORD_LENGTH = 24;

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * <p>
   * A cryptographically secure random-number generator.
   * </p>
   * <p>
   * Unlike the standard random-number generator, numbers generated by an instance of the class
   * SecureRandom are cryptographically secure--that is, they are less subject to pattern guessing
   * and other attacks that can be made upon a traditional random-number generator.
   * </p>
   */
  private static SecureRandom secureRandom;
  static
  {
    try
    {
      secureRandom = SecureRandom.getInstance( "SHA1PRNG" );
    }
    catch( NoSuchAlgorithmException e )
    {
      throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
    }
  }

  // ---------------------------------------------------------------------------
  // Methods
  // ---------------------------------------------------------------------------

  /**
   * Generates a cryptographically-random password.
   * 
   * @return a cryptographically-random password, as a <code>String</code>.
   */
  public static String generatePassword()
  {
    return generatePassword( DEFAULT_PASSWORD_LENGTH );
  }

  /**
   * Generates a cryptographically-random password.
   * 
   * @param size the length of the password in bytes.
   * @return a cryptographically-random password, as a <code>String</code>.
   */
  public static String generatePassword( int size )
  {
    byte[] data = new byte[size];
    secureRandom.nextBytes( data );
    return new String( data );
  }

  /**
   * @param max - an upper limit int (exclusive) for which the random generator will return e.g.
   *          When max = 10, this method will randomly return an int value of 0 to 9
   * @return a cryptographically-random Integer in the length of numOfBits
   * @see com.biperf.core.strategy.impl.PasswordPolicyStrategyImpl
   */
  public static Integer generateInteger( int max )
  {
    int randomInt = secureRandom.nextInt( max );
    return new Integer( randomInt );
  }

  /**
   * @return a random Character
   * @see com.biperf.core.strategy.impl.PasswordPolicyStrategyImpl
   */
  public static Character generateCharacter()
  { // lowercase 'L' is omitted per business request
    final char[] allow = "aAbBcCdDeEfFgGhHiIjJkKLmMnNoOpPqQrRsStTuUvVwWxXyYzZ".toCharArray();
    char nextChar = allow[generateInteger( allow.length ).intValue()];
    return new Character( nextChar );
  }
}
