/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/crypto/MD5Hash.java,v $
 */

package com.biperf.core.utils.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.utils.ParamConstants;
import com.biperf.core.utils.ResourceManager;

/**
 * MD5Hash.
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
public class MD5Hash
{
  private static final Log LOG = LogFactory.getLog( MD5Hash.class );

  public static String ALGORITHM_PREFIX = "{MD5}";

  private MessageDigest md;

  /**
   * Constructor to initialize MessageDigest
   */

  public MD5Hash()
  {
    super();

    try
    {
      md = MessageDigest.getInstance( "MD5" );
    }
    catch( NoSuchAlgorithmException e )
    {
      LOG.error( "No MD5 Algorithm Found", e );
      throw new BeaconRuntimeException( e );
    }

  }

  public String encrypt( String toEncrypt )
  {
    return encrypt( toEncrypt, true );
  }

  public String encryptDefault( String toEncrypt )
  {
    return encrypt( toEncrypt, isDefaultUpperCase() );
  }

  /**
   * Use MD5 hash to encrypt and encode to Base 16 (Hex).
   * 
   * @param toEncrypt
   * @param uppercase if true, will toUppercase the input
   * @return String encrypted
   */
  public String encrypt( String toEncrypt, boolean uppercase )
  {
    if ( toEncrypt == null )
    {
      throw new BeaconRuntimeException( "attempted to encrypt a null String" );
    }

    if ( toEncrypt.startsWith( "{MD5}" ) )
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

    encrypted = ALGORITHM_PREFIX + Hex.encode( hash ).toUpperCase();

    return encrypted;
  }

  public String encryptWithSalt( String toEncrypt, String salt )
  {
    return new MessageDigestPasswordEncoder( "md5" ).encodePassword( toEncrypt, salt );
  }

  public static boolean isDefaultUpperCase()
  {
    Boolean isUpperCase = (Boolean)ResourceManager.getResource( ParamConstants.DEFAULT_UPPERCASE );
    return isUpperCase == null ? true : isUpperCase.booleanValue();
  }

  public static void setDefaultUpperCase( boolean defaultUpperCase )
  {
    ResourceManager.setResource( ParamConstants.DEFAULT_UPPERCASE, new Boolean( defaultUpperCase ) );
  }

  public static void removeDefaultUpperCase()
  {
    ResourceManager.removeResource( ParamConstants.DEFAULT_UPPERCASE );
  }

}
