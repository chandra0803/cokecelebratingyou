
package com.biperf.core.utils;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.biperf.core.utils.crypto.MD5Hash;
import com.biperf.core.utils.crypto.SHA256HashSeamless;

public class SecurityUtils
{
  private static final Logger logger = Logger.getLogger( SecurityUtils.class );

  public static String decryptAES( String encryptedString, String aesKey, String aesInitVector ) throws Exception
  {

    byte[] inpBytes = Base64.getDecoder().decode( encryptedString );

    AlgorithmParameterSpec initVectorSpec = new IvParameterSpec( getBytesFromKey( aesInitVector ) );
    Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5PADDING" );
    SecretKeySpec KeySpec = new SecretKeySpec( getBytesFromKey( aesKey ), "AES" );
    cipher.init( Cipher.DECRYPT_MODE, KeySpec, initVectorSpec );
    byte[] aes = cipher.doFinal( inpBytes );
    String decryptedResultaes = new String( aes );
    return decryptedResultaes;
  }

  public static String decryptAESWithCharKeys( String encryptedString, String aesKey, String aesInitVector ) throws Exception
  {
    byte[] aesKeyBytes = aesKey.getBytes( StandardCharsets.UTF_8 );// convert string to bytes
    byte[] aesInitVectorBytes = aesInitVector.getBytes( StandardCharsets.UTF_8 );

    return decryptAES( encryptedString, getBytesString( aesKeyBytes ), getBytesString( aesInitVectorBytes ) );

  }

  public static String encryptAES( String aesdata, String aesKey, String aesInitVector ) throws Exception
  {

    AlgorithmParameterSpec initVectorSpec = new IvParameterSpec( getBytesFromKey( aesInitVector ) );
    Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5PADDING" );
    SecretKeySpec KeySpec = new SecretKeySpec( getBytesFromKey( aesKey ), "AES" );
    cipher.init( Cipher.ENCRYPT_MODE, KeySpec, initVectorSpec );
    byte[] cipherText = cipher.doFinal( aesdata.getBytes() );
    String base64 = Base64.getEncoder().encodeToString( cipherText );
    return base64;
  }

  public static String encryptAESWithCharKeys( String aesdata, String aesKey, String aesInitVector ) throws Exception
  {
    byte[] aesKeyBytes = aesKey.getBytes( StandardCharsets.UTF_8 );// convert string to bytes
    byte[] aesInitVectorBytes = aesInitVector.getBytes( StandardCharsets.UTF_8 );

    return encryptAES( aesdata, getBytesString( aesKeyBytes ), getBytesString( aesInitVectorBytes ) );
  }

  private static byte[] getBytesFromKey( String aesKey )
  {

    String[] arrayKey = aesKey.split( "," );
    int length = arrayKey.length;
    byte[] inputKey = new byte[length];
    for ( int i = 0; i < length; i++ )
    {
      inputKey[i] = Byte.valueOf( arrayKey[i] );
    }

    return inputKey;
  }

  // return comma separated bytes
  private static String getBytesString( byte[] a )
  {
    int iMax = a.length - 1;

    StringBuilder b = new StringBuilder();
    for ( int i = 0;; i++ )
    {
      b.append( a[i] );
      if ( i == iMax )
      {
        return b.toString();
      }
      b.append( "," );
    }
  }

  public static boolean isTimeLagValid( String ssoTimeStamp, long timeLagAllowed, String timeZone, String dateFormat )
  {
    boolean validLag = false;

    TimeZone sentTZ = TimeZone.getTimeZone( timeZone );
    Calendar sentCal = Calendar.getInstance( sentTZ );

    SimpleDateFormat sdf = new SimpleDateFormat( dateFormat );
    sdf.setCalendar( sentCal );
    java.util.Date dt = null;

    Calendar cal = Calendar.getInstance( sentTZ );
    SimpleDateFormat current = new SimpleDateFormat( dateFormat );
    current.setCalendar( cal );

    String currentTimeStamp = current.format( cal.getTime() );
    java.util.Date currentDate = null;
    long dtTime = 0;
    long curTime = 0;

    // set the time stamp and get the date from dateformat
    try
    {
      dt = sdf.parse( ssoTimeStamp );
      currentDate = current.parse( currentTimeStamp );
      dtTime = dt.getTime();
      curTime = currentDate.getTime();
    }
    catch( Exception ex )
    {
      logger.error( "Unable to parse timestamp: " + ssoTimeStamp, ex );
      return validLag;
    }

    long diff = 0;
    if ( curTime >= dtTime )
    {
      diff = curTime - dtTime;
      if ( diff == 0 || diff <= timeLagAllowed )
      {
        validLag = true;
      }
    }
    else
    {
      diff = dtTime - curTime;
      if ( diff <= timeLagAllowed )
      {
        validLag = true;
      }
    }
    return validLag;
  }

  public static String buildDateString()
  {
    SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMddHHmmss" );
    formatter.setTimeZone( TimeZone.getTimeZone( "GMT" ) );
    String dateString = formatter.format( new Date() );

    return dateString;
  }

  public static boolean isHashStringValidSeamless( String ssoUniqueId, String ssoTimeStamp, String secretKey, String hashString )
  {
    boolean validHashString = false;
    /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    String computedHashString = "";
    // Using this for a safe check if clients has not converted to SHA yet.
    // TODO MD5 needs to be removed (May be a year later ? Ask Prabu) - 11/2/2016
    if ( hashString.startsWith( "{MD5}" ) )
    {
      computedHashString = new MD5Hash().encryptDefault( ssoUniqueId + ssoTimeStamp + secretKey );
    }
    else
    {
      computedHashString = new SHA256HashSeamless().encryptDefault( ssoUniqueId + ssoTimeStamp + secretKey );
    }
    /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */

    if ( computedHashString.equals( hashString ) )
    {
      validHashString = true;
    }

    return validHashString;
  }

  public static String generateSSOKey()
  {
    String ssoKey = "";
    try
    {
      KeyGenerator keygen = KeyGenerator.getInstance( "AES" );
      // Use 256 bit key strength
      keygen.init( 256 );

      SecretKey rawKey = keygen.generateKey();
      byte[] rawKeybyte = rawKey.getEncoded();

      for ( byte aRawKeybyte : rawKeybyte )
      {
        if ( ssoKey.equals( "" ) )
        {
          ssoKey = ssoKey + aRawKeybyte;
        }
        else
        {
          ssoKey += "," + aRawKeybyte;
        }
      }

    }
    catch( NoSuchAlgorithmException e )
    {
      Provider sunjce = new com.sun.crypto.provider.SunJCE();
      Security.addProvider( sunjce );
    }
    return ssoKey;
  }

}
