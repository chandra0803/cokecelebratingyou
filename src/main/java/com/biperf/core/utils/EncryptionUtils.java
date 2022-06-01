
package com.biperf.core.utils;

import java.security.Security;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.commons.CommonUtils;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.PBEConfig;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.salt.StringFixedSaltGenerator;

import com.biperf.core.service.security.EncryptionService;
import com.biperf.core.service.system.SystemVariableService;

public class EncryptionUtils
{
  private static final Log log = LogFactory.getLog( EncryptionUtils.class );
  private static final int KEY_OBTENTION_ITERATIONS = 20;
  private static final BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();

  private static final String algorithm = "PBEWITHSHA-1AND256BITAES-CBC-BC";

  public static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern( "MMddyyyyHHmmss" );

  public static String encryptValue( String value, String password )
  {
    String result = null;
    try
    {
      StringEncryptor encryptor = getEncryptor( password );
      result = encryptor.encrypt( value );
    }
    catch( Exception e )
    {
      log.error( "unable to encrypt ", e );
      result = null;
    }
    return result;
  }

  public static String decryptValue( String value, String password )
  {
    String result = null;
    try
    {
      StringEncryptor encryptor = getEncryptor( password );
      result = encryptor.decrypt( value );
    }
    catch( Exception e )
    {
      log.error( "unable to decrypt :" + value, e );
      result = null;
    }
    return result;
  }

  public static StringEncryptor getEncryptor( String password )
  {
    StandardPBEStringEncryptor result = new StandardPBEStringEncryptor();
    result.setConfig( getConfig( algorithm, password, getSalt( password ) ) );
    return result;
  }

  public static String getSharedServicesEncryptedSignature()
  {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "MMddyyyyHHmmss" );
    TimeZone cstTime = TimeZone.getTimeZone( "CST" ); // TODO : this line will be removed later

    Random randomno = new Random();
    simpleDateFormat.setTimeZone( cstTime );
    String date = simpleDateFormat.format( new Date() );
    StringBuffer signature = new StringBuffer();
    signature.append( randomno.nextInt( 10000 ) );
    signature.append( "|" );
    signature.append( getContextName() );
    signature.append( "|" );
    signature.append( date );
    signature.append( "|" );
    signature.append( randomno.nextInt( 10000 ) );

    String securityKey = getSecurityKey();
    String encryptedSignatureValue = EncryptionUtils.encryptValue( signature.toString(), securityKey );
    return encryptedSignatureValue;
  }

  public static String getSecurityKey()
  {
    String securityKey = null;

    String encryptedKey = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SHARED_SERVICES_KEY ).getStringVal();
    if ( encryptedKey != null )
    {
      securityKey = getEncryptionService().getDecryptedValue( encryptedKey );
    }

    return securityKey;
  }

  private static String getContextName()
  {
    return getSystemVariableService().getContextName();
  }

  protected static String getSalt( String password )// password never used
  {
    return getContextName();
  }

  protected static PBEConfig getConfig( String algorithm, String password, String salt )

  {
    if ( Security.getProvider( BouncyCastleProvider.PROVIDER_NAME ) == null )
    {
      Security.addProvider( bouncyCastleProvider );
    }
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setProviderName( BouncyCastleProvider.PROVIDER_NAME );
    config.setAlgorithm( algorithm );
    config.setPassword( password );
    config.setKeyObtentionIterations( KEY_OBTENTION_ITERATIONS );
    salt = String.format( "%-16s", salt );
    StringFixedSaltGenerator saltGen = new StringFixedSaltGenerator( salt );
    config.setSaltGenerator( saltGen );
    config.setStringOutputType( CommonUtils.STRING_OUTPUT_TYPE_HEXADECIMAL );
    return config;
  }

  /**
   * @return SystemVariableService
   */
  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  /**
   * @return SystemVariableService
   */
  private static EncryptionService getEncryptionService()
  {
    return (EncryptionService)BeanLocator.getBean( EncryptionService.BEAN_NAME );
  }

  /** 
   * Generates the encrypted signature based on the any salt key and client code provided
   * @param salt
   * @param clientCode
   * @return
   */
  public static String generateHoneyCombEncryptedSignature( String salt, String clientCode )
  {
    try
    {
      String signature = generateHoneyCombSignature( clientCode );
      StringEncryptor encryptor = getHoneyCombEncryptor( salt, clientCode );
      return encryptor.encrypt( signature );
    }
    catch( Exception ex )
    {
      log.error( "Error occured while generating signature, Please double check the client code.", ex );
      throw new EncryptionOperationNotPossibleException( "Error occured while generating signature, Please double check the client code." );
    }
  }

  /** 
   * @param clientCode
   * @return
   */
  public static String generateHoneyCombSignature( String clientCode )
  {
    LocalDateTime now = LocalDateTime.now();
    Random rand = new Random();
    int pickStart = rand.nextInt( 9000 ) + 1000;
    int pickEnd = rand.nextInt( 9000 ) + 1000;
    return pickStart + "|" + clientCode + "|" + TIMESTAMP_FORMATTER.format( now ) + "|" + pickEnd;
  }

  /**
   * Gets the Encryptor object based on the salt key and client code 
   * @param salt
   * @param clientCode
   * @return
   */
  public static StringEncryptor getHoneyCombEncryptor( String salt, String clientCode )
  {
    StandardPBEStringEncryptor result = new StandardPBEStringEncryptor();
    result.setConfig( getConfig( algorithm, salt, clientCode ) );
    return result;
  }

}
