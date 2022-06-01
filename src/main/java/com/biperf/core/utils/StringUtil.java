/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/StringUtil.java,v $
 */

package com.biperf.core.utils;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * StringUtil.
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
 * <td>kumars</td>
 * <td>Mar 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class StringUtil
{

  protected static Log LOG = LogFactory.getLog( StringUtil.class );

  /**
   * replaces Forward and Back Slashes in an String with Dots (periods) to convert a path to package
   *
   * @param pInputString the String to be parsed
   * @return String
   */
  public static String replaceSlashesWithDots( String pInputString )
  {
    String lResult = new String( "" );
    for ( int i = 0; i < pInputString.length(); i++ )
    {
      if ( pInputString.substring( i, i + 1 ).equals( "/" ) || pInputString.substring( i, i + 1 ).equals( "\\" ) )
      {
        lResult = lResult + ".";
      }
      else
      {
        lResult = lResult + pInputString.substring( i, i + 1 );
      }
    }
    return lResult;
  }

  public static boolean isEmpty( String str )
  {
    return str == null || str.length() == 0;
  }

  public static boolean isNullOrEmpty( String str )
  {
    return str == null || str.trim().length() == 0;
  }

  public static boolean isValid( String str )
  {
    return str != null && str.trim().length() != 0;
  }

  /**
   * converts a semicolon delimited String to a Properties Collection
   *
   * @param pPropsString
   * @return java.util.Properties a Properties Collection
   */
  public static Properties buildPropertiesFromString( String pPropsString )
  {
    // parses a string for all property values assuming a format key=value;key2=value2...
    int lastSemiColon = 0;
    String lPropertySetting = null;
    Properties lProps = new Properties();
    int nextSemiColon = 0;
    while ( nextSemiColon >= 0 )
    {
      nextSemiColon = pPropsString.indexOf( ";", lastSemiColon + 1 );
      if ( nextSemiColon > 0 )
      {
        lPropertySetting = pPropsString.substring( lastSemiColon, nextSemiColon );
      }
      else
      {
        lPropertySetting = pPropsString.substring( lastSemiColon );
      }
      if ( LOG.isDebugEnabled() )
      {
        LOG.debug( "Current Property Setting[" + lPropertySetting + "], nextSemiColon =" + nextSemiColon );
      }
      int equalsSign = lPropertySetting.indexOf( "=" );
      String key = lPropertySetting.substring( 0, equalsSign ).trim();
      String value = lPropertySetting.substring( equalsSign + 1 ).trim();
      if ( LOG.isDebugEnabled() )
      {
        LOG.debug( "Setting Property [" + key + "]=[" + value + "]" );
      }
      lProps.setProperty( key, value );
      lastSemiColon = nextSemiColon + 1;
    }
    return lProps;
  }

  /**
   * @param inputString
   * @param maxlength
   * @return String tuncated to maxlength or less
   */
  public static String truncateStringToLength( String inputString, int maxlength )
  {
    if ( inputString != null && inputString.length() > maxlength )
    {
      inputString = inputString.substring( 0, maxlength );
    }

    return inputString;
  }

  /**
   * Convert any new line characters to the HTML equivalent so that line breaks display correctly.
   *
   * @param text the text whose newlines characters should be converted
   * @return String newline characters replaced with HTML equivalent
   */
  public static String convertLineBreaks( String text )
  {
    return text.replaceAll( "\n", "<br/>" );
  }

  /* Fix for Bug # 38845 starts */
  public static Object escapeHTML( Object o )
  {
    String s = (String)o;
    if ( s == null || s.equals( "null" ) )
    {
      return s;
    }
    return StringEscapeUtils.unescapeHtml4( s );
  }

  public static Object replaceApostrophe( Object o )
  {
    String s = (String)o;
    if ( s == null || s.equals( "null" ) )
    {
      return s;
    }
    return s.replace( "&apos;", "&#39;" );
  }

  /*
   * This function is to skip all the html tags and characters and returns the string without html
   */

  public static Object skipHTML( String text )
  {
    if ( text != null )
    {
      String noHtmlChars = text.replaceAll( "\\<.*?\\>", "" );
      noHtmlChars = noHtmlChars.replaceAll( "\r", "" );
      noHtmlChars = noHtmlChars.replaceAll( "\n", "" );
      noHtmlChars = noHtmlChars.replaceAll( "&amp;", "&" );
      noHtmlChars = noHtmlChars.replaceAll( "&nbsp;", " " );

      return noHtmlChars;
    }
    return null;
  }

  public static Object skipHTMLWithSpace( String text )
  {
    if ( text != null )
    {
      String noHtmlChars = text.replaceAll( "\\<.*?\\>", " " );
      noHtmlChars = noHtmlChars.replaceAll( "\r", "" );
      noHtmlChars = noHtmlChars.replaceAll( "\n", "" );
      noHtmlChars = noHtmlChars.replaceAll( "&amp;", "&" );
      noHtmlChars = noHtmlChars.replaceAll( "&nbsp;", " " );

      return noHtmlChars.trim();
    }
    return null;
  }

  public static String noSplChar( String text )
  {
    Pattern pt = Pattern.compile( "[^a-zA-Z0-9,]" );
    Matcher match = pt.matcher( text );
    while ( match.find() )
    {
      String s = match.group();
      text = text.replaceAll( "\\" + s, "" );
    }
    return text;
  }

  public static boolean hasContentInHtml( String html )
  {
    // is anything left after stripping html and whitespace?
    return html != null && ( (String)skipHTML( html ) ).trim().length() > 0;
  }

  public static String escapeXml( String text )
  {
    return StringEscapeUtils.escapeXml10( text );
  }

  public static String filterAlphaNumeric( String input )
  {
    Pattern pt = Pattern.compile( "[^a-zA-Z0-9]" );
    Matcher match = pt.matcher( input );
    while ( match.find() )
    {
      String s = match.group();
      input = input.replaceAll( "\\" + s, "" );
    }
    return input;
  }

  public static String join( List<String> array, String delimiter )
  {
    if ( array == null )
    {
      return null;
    }
    StringBuffer buf = new StringBuffer();
    for ( int i = 0; i < array.size(); i++ )
    {
      if ( i > 0 )
      {
        buf.append( delimiter );
      }
      if ( array.get( i ) != null )
      {
        buf.append( array.get( i ) );
      }
    }
    return buf.toString();
  }

  public static BigDecimal convertStringToDecimal( String value )
  {

    BigDecimal inputValue = null;
    try
    {
      if ( !isEmpty( value ) )
      {
        inputValue = new BigDecimal( value );
        inputValue.setScale( 2, BigDecimal.ROUND_FLOOR );
      }
    }
    catch( NumberFormatException e )
    {
      return null;
    }
    return inputValue;
  }

  public static String convertListToCommaSeparated( List<String> list )
  {
    StringBuilder builder = new StringBuilder();
    if ( list == null )
    {
      return builder.toString();
    }
    Iterator<String> listIterator = list.iterator();
    while ( listIterator.hasNext() )
    {
      String value = listIterator.next();
      builder.append( value );
      if ( listIterator.hasNext() )
      {
        builder.append( ", " );
      }
    }
    return builder.toString();
  }

  public static String escapeDoubleQuote( String var )
  {
    return StringUtils.replace( var, "\"", "\\\"" );
  }

  public static String maskEmailAddress( String emailAddress )
  {
    String maskedEmailAddress = null;
    Pattern pt = Pattern.compile( "(.*?)@(.*?)\\.(.*)" );
    Matcher match = pt.matcher( emailAddress );
    while ( match.find() )
    {
      String user = match.group( 1 );
      String domain = match.group( 2 );
      String emailDotDomain = match.group( 3 );

      // If user or domain length is greater than 5, mask the email address for example :
      // test123@biworldwide.com is masked to tes***3@biw*******e.com
      if ( user.length() >= 5 )
      {
        user = user.substring( 0, 3 ) + maskString( user.substring( 3, user.length() - 1 ) ) + user.substring( user.length() - 1 );
      }
      if ( domain.length() >= 5 )
      {
        domain = domain.substring( 0, 3 ) + maskString( domain.substring( 3, domain.length() - 1 ) ) + domain.substring( domain.length() - 1 );
      }

      // If user or domain length is in between 1 to 5, mask the email address for example :
      // test@biw.com is masked to t**t@b*w.com
      if ( user.length() > 1 && user.length() < 5 )
      {
        user = user.substring( 0, 1 ) + maskString( user.substring( 1, user.length() - 1 ) ) + user.substring( user.length() - 1 );
      }
      if ( domain.length() > 1 && domain.length() < 5 )
      {
        domain = domain.substring( 0, 1 ) + maskString( domain.substring( 1, domain.length() - 1 ) ) + domain.substring( domain.length() - 1 );
      }

      // If user or domain length is in between 1 to 5, mask the email address for example :
      // t@b.com is masked to *@*.com
      if ( user.length() == 1 )
      {
        user = maskString( user );
      }
      if ( domain.length() == 1 )
      {
        domain = maskString( domain );
      }

      maskedEmailAddress = user + "@" + domain + "." + emailDotDomain;
    }
    return maskedEmailAddress;
  }

  public static String maskString( String stringToMask )
  {
    return stringToMask.replaceAll( "(?s).", "*" );
  }

  public static String maskPhoneNumber( String phoneNumber )
  {
    String onlyNumbers = phoneNumber.replaceAll( "[^0-9]", "" );
    return onlyNumbers.substring( 0, 1 ) + maskString( onlyNumbers.substring( 1, onlyNumbers.length() - 2 ) ) + onlyNumbers.substring( onlyNumbers.length() - 3, onlyNumbers.length() );
  }

}
