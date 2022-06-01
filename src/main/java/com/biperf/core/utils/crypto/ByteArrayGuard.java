/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/crypto/ByteArrayGuard.java,v $
 */

package com.biperf.core.utils.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.biperf.core.exception.BeaconRuntimeException;

/*
 * ByteArrayGuard <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Oct 10, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

/**
 * This utility class encrypts and decrypts byte arrays. It is based on the class ByteArrayGuard
 * from the sample code for the pattern "Storing Session State on the Client" documented in Sun's
 * BluePrints Solutions Catalog. See
 * https://bpcatalog.dev.java.net/nonav/webtier/clientside-state/frames.html for more details.
 */
public class ByteArrayGuard
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The name of the transformation used by the encryption and decryption ciphers.
   */
  private static final String CIPHER_TRANSFORMATION = "DESede/CBC/PKCS5Padding";

  /**
   * The length of an initialization vector in bytes.
   */
  private static final int INITIALIZATION_VECTOR_LENGTH = 8;

  /**
   * The length of a message authentication code in bytes.
   */
  private static final int MAC_LENGTH = 20;

  /**
   * The algorithm used to generate cryptographically secure random numbers.
   */
  private static final String RANDOM_ALGORITHM = "SHA1PRNG";

  /**
   * The algorithm used to genearate secret keys.
   */
  private static final String SECRET_KEY_ALGORITHM = "DESede";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The password used to encrypt and decrypt byte arrays.
   */
  private String password;

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Constructs a <code>ByteArrayGuard</code> object.
   * 
   * @param password the password used to encrypt and decrypt byte arrays. Must be not null and not
   *          empty.
   */
  public ByteArrayGuard( String password )
  {
    if ( password == null || password.length() == 0 )
    {
      throw new IllegalArgumentException( "Parameter \"password\" is null or empty." );
    }

    this.password = password;
  }

  /**
   * Decrypts the given data.
   * 
   * @param secureData a secure version of the data.
   * @return the decrypted version of the data.
   * @throws com.biperf.core.utils.crypto.InvalidMacException if the secure data's message
   *           authentication code does not match the calculated message authentication code.
   */
  public byte[] decrypt( byte[] secureData ) throws InvalidMacException
  {
    byte[] decryptedData = null;

    // Get the components of the secure data.
    SecureDataWrapper secureDataWrapper = new SecureDataWrapper( secureData );

    byte[] givenMac = secureDataWrapper.getMessageAuthenticationCode();
    byte[] initializationVector = secureDataWrapper.getInitializationVector();
    byte[] encryptedData = secureDataWrapper.getEncryptedData();

    // Build the key.
    byte[] key = KeyBuilder.build( password );

    // Build the message authentication code.
    byte[] calculatedMac = MacBuilder.build( key, initializationVector, encryptedData );

    // Determine whether anyone has tampered with the secure data.
    if ( Arrays.equals( givenMac, calculatedMac ) )
    {
      DecryptionCipher cipher = new DecryptionCipher( key, initializationVector );
      decryptedData = cipher.decrypt( encryptedData );
    }
    else
    {
      throw new InvalidMacException();
    }

    return decryptedData;
  }

  /**
   * Encrypts the given data.
   * 
   * @param decryptedData the decrypted version of the data.
   * @return the encrypted version of the data.
   */
  public byte[] encrypt( byte[] decryptedData )
  {
    byte[] secureData = null;

    // Build the key.
    byte[] key = KeyBuilder.build( password );

    // Encrypt the data.
    EncryptionCipher cipher = new EncryptionCipher( key );
    byte[] encryptedData = cipher.encrypt( decryptedData );

    // Get the cipher's initialization vector.
    byte[] initializationVector = cipher.getInitializationVector();

    // Build the message authentication code (MAC).
    byte[] messageAuthenticationCode = MacBuilder.build( key, initializationVector, encryptedData );

    // Build the secure data.
    secureData = SecureDataBuilder.build( messageAuthenticationCode, initializationVector, encryptedData );

    return secureData;
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  private static class DecryptionCipher
  {
    /**
     * The cipher used to decrypt data.
     */
    private Cipher cipher;

    /**
     * Constructs a <code>DecryptionCipher</code> object.
     * 
     * @param key the key used to encrypt the data.
     * @param initializationVector used to initialize the cipher.
     */
    public DecryptionCipher( byte[] key, byte[] initializationVector )
    {
      try
      {
        // Create a secret key.
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( SECRET_KEY_ALGORITHM );
        Key secretKey = keyFactory.generateSecret( new DESedeKeySpec( key ) );

        // Create a cryptographically secure random number generator.
        SecureRandom secureRandom = SecureRandom.getInstance( RANDOM_ALGORITHM );

        // Construct a cipher.
        Cipher tmpCipher = Cipher.getInstance( CIPHER_TRANSFORMATION );
        tmpCipher.init( Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec( initializationVector ), secureRandom );
        cipher = tmpCipher;
      }
      catch( NoSuchAlgorithmException e )
      {
        // Possible causes:
        // 1. The method SecretKeyFactory.getInstance(String) determined that a
        // secret key factory for the specified algorithm is not available in
        // default provider package or in any of the other provider packages
        // that were searched.
        // 2. The method Cipher.getInstance(String) determined that a cipher
        // for the specified algorithm is not available in the default
        // provider package or in any of the other provider packages that
        // were searched.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
      catch( InvalidKeySpecException e )
      {
        // The key specification passed to SecretKeyFactory.generateSecret(KeySpec)
        // does not support secret keys.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
      catch( InvalidKeyException e )
      {
        // Possible causes:
        // 1. The length of the key passed to DESedeKeySpec(byte[]) is less
        // than 24 bytes.
        // 2. The key passed to Cipher.init() is inappropriate for initializing
        // this cipher, or the key's keysize exceeds the maxium allowable
        // keysize.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
      catch( NoSuchPaddingException e )
      {
        // The transformation identifier passed to Cipher.getInstance(String)
        // identifies a transformation that uses padding scheme is not available.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
      catch( InvalidAlgorithmParameterException e )
      {
        // The initialization vector parameter specification passed to
        // Cipher.init() specifies algorithm parameters that are inappropriate
        // for the cipher.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
    }

    /**
     * Decrypts the given data.
     * 
     * @param encryptedData the encrypted version of the data.
     * @return the decrypted version of the data.
     */
    public byte[] decrypt( byte[] encryptedData )
    {
      byte[] decryptedData = null;

      try
      {
        decryptedData = cipher.doFinal( encryptedData );
      }
      catch( IllegalBlockSizeException e )
      {
        // The method Cipher.doFinal(byte[]) determined that the length of the
        // data is not a multiple of the cipher's block size and the cipher's
        // builder did not request padding.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
      catch( BadPaddingException e )
      {
        // This exception should never occur because it occurs only when the
        // cipher is in decrypt mode. (The cipher used above is in encrypt
        // mode.)
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }

      return decryptedData;
    }
  }

  private static class EncryptionCipher
  {
    /**
     * The cipher used to encrypt data.
     */
    private Cipher cipher;

    /**
     * Constructs an <code>EncryptionCipher</code> object.
     * 
     * @param key the key used to encrypt and decrypt data.
     */
    public EncryptionCipher( byte[] key )
    {
      try
      {
        // Create a secret key.
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( SECRET_KEY_ALGORITHM );
        Key secretKey = keyFactory.generateSecret( new DESedeKeySpec( key ) );

        // Create a cryptographically secure random number generator.
        SecureRandom secureRandom = SecureRandom.getInstance( RANDOM_ALGORITHM );

        // Create an initialization vector.
        byte[] initializationVector = new byte[INITIALIZATION_VECTOR_LENGTH];
        secureRandom.nextBytes( initializationVector );

        // Construct a cipher.
        Cipher tmpCipher = Cipher.getInstance( CIPHER_TRANSFORMATION );
        tmpCipher.init( Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec( initializationVector ), secureRandom );
        cipher = tmpCipher;
      }
      catch( NoSuchAlgorithmException e )
      {
        // Possible causes:
        // 1. The method SecretKeyFactory.getInstance(String) determined that a
        // secret key factory for the specified algorithm is not available in
        // default provider package or in any of the other provider packages
        // that were searched.
        // 2. The method Cipher.getInstance(String) determined that a cipher
        // for the specified algorithm is not available in the default
        // provider package or in any of the other provider packages that
        // were searched.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
      catch( InvalidKeyException e )
      {
        // Possible causes:
        // 1. The length of the key passed to DESedeKeySpec(byte[]) is less
        // than 24 bytes.
        // 2. The key passed to Cipher.init() is inappropriate for initializing
        // this cipher, or the key's keysize exceeds the maxium allowable
        // keysize.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
      catch( InvalidKeySpecException e )
      {
        // The key specification passed to SecretKeyFactory.generateSecret(KeySpec)
        // does not support secret keys.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
      catch( NoSuchPaddingException e )
      {
        // The transformation identifier passed to Cipher.getInstance(String)
        // identifies a transformation that uses padding scheme is not available.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
      catch( InvalidAlgorithmParameterException e )
      {
        // The initialization vector parameter specification passed to
        // Cipher.init() specifies algorithm parameters that are inappropriate
        // for the cipher.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
    }

    /**
     * Encrypts the given data.
     * 
     * @param decryptedData the decrypted version of the data.
     * @return the encrypted version of the data.
     */
    public byte[] encrypt( byte[] decryptedData )
    {
      byte[] encryptedData = null;

      try
      {
        encryptedData = cipher.doFinal( decryptedData );
      }
      catch( IllegalBlockSizeException e )
      {
        // The method Cipher.doFinal(byte[]) determined that the length of the
        // data is not a multiple of the cipher's block size and the cipher's
        // builder did not request padding.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
      catch( BadPaddingException e )
      {
        // This exception should never occur because it occurs only when the
        // cipher is in decrypt mode. (The cipher used above is in encrypt
        // mode.)
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }

      return encryptedData;
    }

    /**
     * Returns the cipher's initialization vector.
     * 
     * @return the cipher's initialization vector.
     */
    public byte[] getInitializationVector()
    {
      return cipher.getIV();
    }
  }

  private static class KeyBuilder
  {
    /**
     * The algorithm used to generate message digests.
     */
    private static final String MESSAGE_DIGEST_ALGORITHM = "SHA-1";

    /**
     * The length of a key in bytes.
     */
    private static final int KEY_LENGTH = 24;

    /**
     * Builds a key that can be used to generate ciphers and message authentication codes (MACs).
     * 
     * @param password the basis of the generated key.
     * @return a key that can be used to generate ciphers and message authentication codes.
     */
    public static byte[] build( String password )
    {
      byte[] key = new byte[KEY_LENGTH];

      try
      {
        // Generate a message digest of the password.
        MessageDigest messageDigest = MessageDigest.getInstance( MESSAGE_DIGEST_ALGORITHM );
        byte[] seed = messageDigest.digest( password.getBytes() );

        // Use the message digest to seed a secure random number generator.
        SecureRandom random = SecureRandom.getInstance( RANDOM_ALGORITHM );
        random.setSeed( seed );

        // Use the secure random number generator to generate the key.
        random.nextBytes( key );
      }
      catch( NoSuchAlgorithmException e )
      {
        // Possible causes:
        // 1. The method MessageDigest.getInstance(String) could not locate an
        // implementation of the Secure Hash Algorithm (SHA-1) algorithm.
        // 2. The method SecureRandom.getInstance(String) count not locate an
        // implementation of the specified algorithm.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }

      return key;
    }
  }

  private static class MacBuilder
  {
    /**
     * The algorithm used to generate the message authentication code.
     */
    private static final String MAC_ALGORITHM = "HmacSHA1";

    /**
     * Builds a message authentication code (MAC) for the given initialization vector and encrypted
     * data.
     * 
     * @param key the key used to generate the cipher's secret key.
     * @param initializationVector the cipher's initialization vector.
     * @param encryptedData the encrypted data.
     * @return a message authentication code.
     */
    public static byte[] build( byte[] key, byte[] initializationVector, byte[] encryptedData )
    {
      byte[] macBytes = null;

      try
      {
        Mac mac = Mac.getInstance( MAC_ALGORITHM );

        Key secretKeySpec = new SecretKeySpec( key, 0, MAC_LENGTH, MAC_ALGORITHM );
        mac.init( secretKeySpec );

        mac.update( initializationVector );
        mac.update( encryptedData );

        macBytes = mac.doFinal();
      }
      catch( NoSuchAlgorithmException e )
      {
        // The algorithm passed to Mac.getInstance(String) is not available in
        // the default provider package or any of the other packages that were
        // searched.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }
      catch( InvalidKeyException e )
      {
        // The key passed to Mac.init(Key) is inappropriate for initializing that
        // message authentication code algorithm.
        throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
      }

      return macBytes;
    }
  }

  private static class SecureDataBuilder
  {
    /**
     * Builds a secure version of the encrypted data.
     * 
     * @param messageAuthenticationCode used to detect tampering with the encrypted data.
     * @param initializationVector used to initialize the cipher that decodes the encrypted data.
     * @param encryptedData the encrypted data.
     * @return a secure version of the encrypted data.
     */
    public static byte[] build( byte[] messageAuthenticationCode, byte[] initializationVector, byte[] encryptedData )
    {
      return concatBytes( concatBytes( messageAuthenticationCode, initializationVector ), encryptedData );
    }

    /**
     * Returns a byte array that is the concatenation of array 1 and array 2.
     * 
     * @param array1 the first array.
     * @param array2 the second array.
     * @return the concatenation of array 1 and array 2.
     */
    private static byte[] concatBytes( byte[] array1, byte[] array2 )
    {
      byte[] cBytes = new byte[array1.length + array2.length];

      System.arraycopy( array1, 0, cBytes, 0, array1.length );
      System.arraycopy( array2, 0, cBytes, array1.length, array2.length );

      return cBytes;
    }
  }

  private static class SecureDataWrapper
  {
    private byte[] messageAuthenticationCode;
    private byte[] initializationVector;
    private byte[] encryptedData;

    /**
     * Constructs a <code>SecureDataWrapper</code> object.
     * 
     * @param secureData a byte array that contains a fixed-length message authentication code, a
     *          fixed-lenth initialization vector, and variable-length encrypted data, in that
     *          order.
     */
    public SecureDataWrapper( byte[] secureData )
    {
      // Extract the message authentication code.
      messageAuthenticationCode = new byte[MAC_LENGTH];
      System.arraycopy( secureData, 0, messageAuthenticationCode, 0, messageAuthenticationCode.length );

      // Extract the initialization vector.
      initializationVector = new byte[INITIALIZATION_VECTOR_LENGTH];
      System.arraycopy( secureData, messageAuthenticationCode.length, initializationVector, 0, initializationVector.length );

      // Extract the encrypted data.
      encryptedData = new byte[secureData.length - messageAuthenticationCode.length - initializationVector.length];
      System.arraycopy( secureData, messageAuthenticationCode.length + initializationVector.length, encryptedData, 0, encryptedData.length );
    }

    /**
     * Returns the encrypted data portion of the secure data.
     * 
     * @return the encrypted data.
     */
    public byte[] getEncryptedData()
    {
      return encryptedData;
    }

    /**
     * Returns the initialization vector portion of the secure data.
     * 
     * @return the initialization vector.
     */
    public byte[] getInitializationVector()
    {
      return initializationVector;
    }

    /**
     * Returns the message authentication code portion of the secure data.
     * 
     * @return the message authentication code.
     */
    public byte[] getMessageAuthenticationCode()
    {
      return messageAuthenticationCode;
    }
  }
}
