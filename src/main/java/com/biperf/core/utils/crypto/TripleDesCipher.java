/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/crypto/TripleDesCipher.java,v $
 */

package com.biperf.core.utils.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * TripleDesCipher.
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
 * <td>jdunne</td>
 * <td>Mar 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class TripleDesCipher implements BeaconCipher
{
  private static String provider = "SunJCE";
  // TODO this should be PBEWithMD5AndTripleDES - but we need the unlimited strength patch
  // installed.
  private static String algorithm = "PBEWithMD5AndDES";
  private static String mode = "CBC";
  private static String padding = "PKCS5Padding";
  private static String transformation = algorithm + "/" + mode + "/" + padding;

  private String seed;

  static
  {
    // log.info( "Adding Sun Provider" );
    Security.addProvider( new com.sun.crypto.provider.SunJCE() );
    // log.info("Crypto Providers..." + Security.getAlgorithms("Cipher"));
  }

  public String getSeed()
  {
    return seed;
  }

  public void setSeed( String seed )
  {
    this.seed = seed;
  }

  public static String getTransformation()
  {
    return transformation;
  }

  /**
   * Encrypt the clearText
   * 
   * @param clearText
   * @return encryptedText
   * @throws CryptoException
   */
  public synchronized String encrypt( String clearText ) throws CryptoException
  {
    Cipher cipher = getCipher( Cipher.ENCRYPT_MODE );
    byte[] encr = null;
    try
    {
      encr = cipher.doFinal( clearText.getBytes() );
    }
    catch( IllegalBlockSizeException e )
    {
      e.printStackTrace();
      throw new CryptoException( "IllegalBlockSizeException", e );
    }
    catch( BadPaddingException e )
    {
      e.printStackTrace();
      throw new CryptoException( "BadPaddingException", e );
    }
    return Base64.encodeBytes( encr );
  }

  /**
   * Decrypt the encodedText
   * 
   * @param encodedText
   * @return clearText
   * @throws CryptoException
   */
  public synchronized String decrypt( String encodedText ) throws CryptoException
  {
    Cipher decipher = getCipher( Cipher.DECRYPT_MODE );
    byte[] deciph = null;
    try
    {
      deciph = decipher.doFinal( Base64.decode( encodedText ) );
    }
    catch( IllegalBlockSizeException e )
    {
      e.printStackTrace();
      throw new CryptoException( "IllegalBlockSizeException", e );
    }
    catch( BadPaddingException e )
    {
      e.printStackTrace();
      throw new CryptoException( "BadPaddingException", e );
    }
    return new String( deciph );
  }

  // private SecretKeySpec secretKey;
  private SecretKey secretKey;
  private PBEParameterSpec pbeParamSpec;

  private SecretKey getSecretKey() throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    if ( secretKey == null )
    {

      PBEKeySpec pbeKeySpec;

      // Salt
      byte[] salt = { (byte)0xc3, (byte)0x73, (byte)0x2c, (byte)0x8c, (byte)0x7e, (byte)0xa8, (byte)0xe2, (byte)0x9f };

      // Iteration count
      int count = 20;

      // Create PBE parameter set
      pbeParamSpec = new PBEParameterSpec( salt, count );
      pbeKeySpec = new PBEKeySpec( seed.toCharArray() );

      SecretKeyFactory keyFac = SecretKeyFactory.getInstance( algorithm );
      secretKey = keyFac.generateSecret( pbeKeySpec );

    }
    // create a secret key from the keygenerator.
    return secretKey;
  }

  private Cipher encryptCipher = null;
  private Cipher decryptCipher = null;

  private Cipher getCipher( int cipherMode ) throws CryptoException
  {
    try
    {
      // create a cipher object: ("algorithm/mode/padding", provider)

      if ( cipherMode == Cipher.DECRYPT_MODE )
      {
        if ( decryptCipher == null )
        {
          decryptCipher = Cipher.getInstance( transformation, provider );
          decryptCipher.init( cipherMode, getSecretKey(), pbeParamSpec );
        }
        return decryptCipher;
      }
      if ( encryptCipher == null )
      {
        encryptCipher = Cipher.getInstance( transformation, provider );
        encryptCipher.init( cipherMode, getSecretKey(), pbeParamSpec );
      }
      return encryptCipher;
    }
    catch( NoSuchAlgorithmException e )
    {
      e.printStackTrace();
      throw new CryptoException( "NoSuchAlgorithmException", e );
    }
    catch( NoSuchProviderException e )
    {
      e.printStackTrace();
      throw new CryptoException( "NoSuchProviderException", e );
    }
    catch( NoSuchPaddingException e )
    {
      e.printStackTrace();
      throw new CryptoException( "NoSuchPaddingException", e );
    }
    catch( InvalidKeySpecException e )
    {
      e.printStackTrace();
      throw new CryptoException( "InvalidKeySpecException", e );
    }
    catch( InvalidKeyException e )
    {
      e.printStackTrace();
      throw new CryptoException( "InvalidKeyException", e );
    }
    catch( InvalidAlgorithmParameterException e )
    {
      e.printStackTrace();
      throw new CryptoException( "InvalidAlgorithmParameterException", e );
    }
  }
}
