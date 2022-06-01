/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/crypto/SHA256Hash.java,v $
 */

package com.biperf.core.utils.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.utils.ResourceManager;

/**
 * SHA256Hash.
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
 * <td>Mar 21, 2016</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SHA256Hash
{
  private static final Log LOG = LogFactory.getLog( SHA256Hash.class );

  public static String ALGORITHM_PREFIX = "{V2}";
  public static final String DEFAULT_UPPERCASE = "defaultUpperCase";

  private MessageDigest md;

  /**
   * Constructor to initialize MessageDigest
   */

  public SHA256Hash()
  {
    super();

    try
    {
      md = MessageDigest.getInstance( "SHA-256" );
    }
    catch( NoSuchAlgorithmException e )
    {
      LOG.error( "No SHA-256 Algorithm Found", e );
      throw new BeaconRuntimeException( e );
    }

  }

  /**
   * @param toEncrypt String to hash
   * @return Hexadecimal representation of sha256 hash of input
   */
  public String encrypt( String toEncrypt )
  {
    return encrypt( toEncrypt, true );
  }

  public String encryptDefault( String toEncrypt )
  {
    return encrypt( toEncrypt, isDefaultUpperCase() );
  }

  /**
   * @param toEncrypt String to hash
   * @param uppercase If true, convert input to uppercase before hashing
   * @return Hexadecimal representation of sha256 hash of uppercase input
   */
  public String encrypt( String toEncrypt, boolean uppercase )
  {
    return encrypt( toEncrypt, uppercase, true );
  }

  /**
   * Use SHA-256 hash to encrypt and encode to Base 16 (Hex).
   * 
   * @param toEncrypt
   * @param uppercase if true, will toUppercase the input
   * @param addPrefix if true, will prepend algorithm prefix
   * @return String encrypted
   */
  public String encrypt( String toEncrypt, boolean uppercase, boolean addPrefix )
  {
    if ( toEncrypt == null )
    {
      throw new BeaconRuntimeException( "attempted to encrypt a null String" );
    }

    if ( toEncrypt.startsWith( ALGORITHM_PREFIX ) )
    {
      // EARLY EXIT
      return toEncrypt;
    }

    String encrypted = null;

    if ( uppercase )
    {
      toEncrypt = toEncrypt.toUpperCase();
    }

    byte[] hash;

    synchronized ( md )
    {
      md.reset();
      md.update( toEncrypt.getBytes() );
      hash = md.digest();
    }

    encrypted = Hex.encode( hash ).toUpperCase();

    if ( addPrefix )
    {
      encrypted = ALGORITHM_PREFIX + encrypted;
    }

    return encrypted;
  }

  public String encryptWithSalt( String toEncrypt, String salt )
  {
    return new ShaPasswordEncoder( 256 ).encodePassword( toEncrypt, salt );
  }

  public static boolean isDefaultUpperCase()
  {
    Boolean isUpperCase = (Boolean)ResourceManager.getResource( DEFAULT_UPPERCASE );
    return isUpperCase == null ? true : isUpperCase.booleanValue();
  }

  public static void setDefaultUpperCase( boolean defaultUpperCase )
  {
    ResourceManager.setResource( DEFAULT_UPPERCASE, new Boolean( defaultUpperCase ) );
  }

  public static void removeDefaultUpperCase()
  {
    ResourceManager.removeResource( DEFAULT_UPPERCASE );
  }

}
