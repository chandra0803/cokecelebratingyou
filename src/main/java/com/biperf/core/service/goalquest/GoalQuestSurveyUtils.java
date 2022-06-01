/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/goalquest/GoalQuestSurveyUtils.java,v $
 */

package com.biperf.core.service.goalquest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.biperf.core.utils.crypto.BeaconCipher;
import com.biperf.core.utils.crypto.CryptoException;
import com.biperf.core.utils.crypto.CryptoFactory;
import com.biperf.core.utils.crypto.TripleDesCipher;

/**
 * GoalQuestSurveyUtils.
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
 * <td>skumar</td>
 * <td>Apr, 7 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoalQuestSurveyUtils
{
  /**
   * Logger for this class
   */
  private static final Logger log = Logger.getLogger( GoalQuestSurveyUtils.class );

  public static String TRIPLE_DES_PREFIX = "{" + TripleDesCipher.getTransformation() + "}";
  public static final String PROMOTION_ID = "promotionId";
  public static final String SURVEY_TYPE = "surveyType";
  public static final String USER_ID = "userId";
  public static final String PROGRAMID = "GoalQuest";
  public static final String EMAIL_URL_QUERY_PARAM_ENCRYPTED = "queryParam";

  public static final String DURING_GOAL_SETTING = "GQ_DGS";
  public static final String ACHIEVERS = "GQ_ACP";
  public static final String NON_ACHIEVERS = "GQ_NACP";

  public static final String GOAL_SELECTION_TYPE = "Goal Selection";
  public static final String ACHIEVERS_TYPE = "Achievers";
  public static final String NON_ACHIEVERS_TYPE = "Non Achievers";
  public static final String PREVIEW_MESSAGE_GQ_PREFIX = "GQ_";
  public static final String SURVEY_CONTXT_NAME = "goalquestSurvey.do?";

  /**
   * protected since static-method-only Util class.
   */
  protected GoalQuestSurveyUtils()
  {
    super();
  }

  /**
   * getUrlParam
   * 
   * @param propertyName
   * @return String urlName
   */
  public static String getUrlParam( String propName )
  {

    String Url = ResourceBundle.getBundle( "urlAddress" ).toString();
    return Url;

  }

  public static String encodeUrlParam( String UrlAddress )
  {

    String encodeUrl = "";
    return encodeUrl;
  }

  /**
   * Load a properties file from the classpath
   * @param propsName
   * @return Properties
   * @throws Exception
   */
  public static Properties load( String propsName ) throws Exception
  {
    Properties props = new Properties();
    URL url = ClassLoader.getSystemResource( propsName );
    props.load( url.openStream() );
    return props;
  }

  /**
   * Load a Properties File
   * @param propsFile
   * @return Properties
   * @throws IOException
   */
  public static Properties load( File propsFile ) throws IOException
  {
    Properties props = new Properties();
    FileInputStream fis = new FileInputStream( propsFile );
    props.load( fis );
    fis.close();
    return props;
  }

  /**
   * @param siteURLPrefix eg. https://wwwqa.mycustomerresearch.com/gq/loadParam.jsp?
   * @param pagePath eg. ""
   * @param surveyParameterMap map of parameters that will be serialized into clientState
   * @return encoded String link
   */
  public static String generateEncodedLink( String siteURLPrefix, String pagePath, Map surveyParameterMap )
  {
    String link = "";
    int index = 0;
    int endIndex = pagePath.length();
    if ( null != surveyParameterMap )
    {
      // iterate over passed params and add to map (which will overwrite existing parameters by the
      // same name
      StringBuilder sbPagePath = new StringBuilder();

      for ( Iterator iter = surveyParameterMap.keySet().iterator(); iter.hasNext(); )
      {
        String key = (String)iter.next();
        if ( null == sbPagePath || sbPagePath.toString().equals( "" ) )
        {
          sbPagePath.append( key ).append( "=" ).append( surveyParameterMap.get( key ) );
        }
        else
        {
          sbPagePath.append( "&" ).append( key ).append( "=" ).append( surveyParameterMap.get( key ) );
        }
      }
      link = siteURLPrefix + sbPagePath.substring( endIndex );
    }
    return link;
  }

  /**
   * 
   * @param surveyParameterMap map of parameters that will be serialized into clientState
   * @return encoded String link
   */
  public static String generateEncodedQuery( Map surveyParameterMap )
  {
    String link = "";
    int index = 0;

    StringBuilder sbPagePath = new StringBuilder();
    if ( null != surveyParameterMap )
    {
      // iterate over passed params and add to map (which will overwrite existing parameters by the
      // same name
      for ( Iterator iter = surveyParameterMap.keySet().iterator(); iter.hasNext(); )
      {
        String key = (String)iter.next();
        if ( null == sbPagePath || sbPagePath.toString().equals( "" ) )
        {
          sbPagePath.append( key ).append( "=" ).append( surveyParameterMap.get( key ) );
        }
        else
        {
          sbPagePath.append( "&" ).append( key ).append( "=" ).append( surveyParameterMap.get( key ) );
        }
      }

    }
    return sbPagePath.toString();
  }

  /**
   * Returns map with name value pair from URL  Query
   * Ex:  www.bi.com/new.html?promotionId=1&surveyId=2
   * Query returns promotionId=1&surveyId=2
   * nameValueQuery method returns map with name promotionId and value 1 and name with surveyId and value 2
   * @param packagePrefixList
   */

  public static Map urlTokenizer( String urlQueryStr )
  {
    StringTokenizer packagePrefixIter = new StringTokenizer( urlQueryStr, "&" );
    Map<String, String> queryValueMap = new HashMap<String, String>();
    while ( packagePrefixIter.hasMoreTokens() )
    {
      String packagePrefix = packagePrefixIter.nextToken().trim();
      StringTokenizer nameValueIter = new StringTokenizer( packagePrefix, "=" );
      while ( nameValueIter.hasMoreTokens() )
      {
        String nameStr = nameValueIter.nextToken().trim();
        String valueStr = "";
        if ( nameValueIter.hasMoreTokens() )
        {
          valueStr = nameValueIter.nextToken().trim();
        }
        queryValueMap.put( nameStr, valueStr );
      }
    }

    return queryValueMap;
  }

  /**
   * decryptWithDecode (UTF format) using 3DES
   * This will be used if encryptedText passed with system.  If the encrypted string pass thru' browser, text will be in UTF format and no decoding necessary.
   * @param encryptedText
   * @param keyString
   * @return
   */
  public static String decryptWithDecode( String encryptedText, String keyString )
  {
    if ( encryptedText != null )
    {
      // we have a tripleDes encrypted String
      BeaconCipher cipher = CryptoFactory.getTripleDesCipherInstance();
      try
      {
        // return cipher.decrypt( encryptedText.substring( TRIPLE_DES_PREFIX.length() ) );
        String decryptStr = URLDecoder.decode( encryptedText, "UTF-8" );
        return cipher.decrypt( decryptStr );
      }
      catch( CryptoException e )
      {
        log.error( "An exception occurred while decrypting External Link URL for Goal Quest Survey. ", e );
        return null;
      }
      catch( Exception e )
      {
        log.error( "An exception occurred while decrypting External Link URL for Goal Quest Survey. ", e );
        return null;
      }
    }
    // This is really a clear text string. (DecryptedText)
    return encryptedText;

  }

  /**
   * decrypt using 3DES
   * If the encrypted string pass thru' browser, text will be in UTF format and no decoding necessary.
   * @param encryptedText
   * @param keyString
   * @return
   */
  public static String decrypt( String encryptedText, String keyString )
  {
    if ( encryptedText != null )
    {
      // we have a tripleDes encrypted String
      BeaconCipher cipher = CryptoFactory.getTripleDesCipherInstance();
      try
      {
        // return cipher.decrypt( encryptedText.substring( TRIPLE_DES_PREFIX.length() ) );
        return cipher.decrypt( encryptedText );
      }
      catch( CryptoException e )
      {
        log.error( "An exception occurred while decrypting External Link URL for Goal Quest Survey. ", e );
        return null;
      }
      catch( Exception e )
      {
        log.error( "An exception occurred while decrypting External Link URL for Goal Quest Survey. ", e );
        return null;
      }
    }
    // This is really a clear text string. (DecryptedText)
    return encryptedText;

  }

  /**
   * Encrypt using 3DES
   * @param encryptedText
   * @param keyString
   * @return
   */
  public static String encrypt( String url )
  {
    BeaconCipher cipher = CryptoFactory.getTripleDesCipherInstance();
    try
    {
      // return TRIPLE_DES_PREFIX + cipher.encrypt( url );
      String encodedUTFStr = URLEncoder.encode( cipher.encrypt( url ), "UTF-8" );
      return encodedUTFStr;
    }
    catch( CryptoException e )
    {
      log.error( "An exception occurred while encrypting External Link URL for Goal Quest Survey. ", e );
      return null;
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while encrypting External Link URL for Goal Quest Survey. ", e );
      return null;
    }
  }

  public static String getPreviewMessageGqPrefix()
  {
    return PREVIEW_MESSAGE_GQ_PREFIX;
  }

}
