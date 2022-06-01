
package com.biperf.core.utils.crypto;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.commons.CommonUtils;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.PBEConfig;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.salt.StringFixedSaltGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AESUtil
{

  private static final Logger log = LoggerFactory.getLogger( AESUtil.class );
  private static final int KEY_OBTENTION_ITERATIONS = 20;
  private static final BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
  private static final String ALGORITHM = "PBEWITHSHA-1AND256BITAES-CBC-BC";

  /**
   * getEncryptor
   * @param salt
   * @param password
   * @return StringEncryptor
   */
  public static StringEncryptor getEncryptor( String salt, String password )
  {
    StandardPBEStringEncryptor result = new StandardPBEStringEncryptor();
    result.setConfig( getConfig( ALGORITHM, salt, password ) );
    return result;
  }

  /**
   * encryptValue
   * @param value
   * @param salt
   * @param password
   * @return String
   */
  public static String encryptValue( String value, String salt, String password )
  {
    log.error( "before encryption:" + value );
    String result = null;
    try
    {
      StringEncryptor encryptor = getEncryptor( salt, password );
      result = encryptor.encrypt( value );
    }
    catch( Exception e )
    {
      log.error( "unable to decrypt :" + value, e );
      result = null;
    }
    return result;
  }

  /**
   * getConfig
   * @param algorithm
   * @param salt
   * @param pwd
   * @return PBEConfig
   */
  protected static PBEConfig getConfig( String algorithm, String salt, String pwd )
  {
    if ( Security.getProvider( BouncyCastleProvider.PROVIDER_NAME ) == null )
    {
      Security.addProvider( bouncyCastleProvider );
    }
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setProviderName( BouncyCastleProvider.PROVIDER_NAME );
    config.setAlgorithm( algorithm );
    config.setPassword( pwd );
    config.setKeyObtentionIterations( KEY_OBTENTION_ITERATIONS );
    salt = String.format( "%-16s", salt );
    StringFixedSaltGenerator saltGen = new StringFixedSaltGenerator( salt );
    config.setSaltGenerator( saltGen );
    config.setStringOutputType( CommonUtils.STRING_OUTPUT_TYPE_HEXADECIMAL );
    return config;
  }

}
