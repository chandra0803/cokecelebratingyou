
package com.biperf.core.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class HtmlUtils
{
  private static final Pattern REMOVE_TAGS = Pattern.compile( "(<.+?>)" );

  // Tags that will be left in a string after removing formatting with the whitelist method
  private static final Set<String> WHITELIST_SET = new HashSet<>();
  static
  {
    WHITELIST_SET.add( "<b>" );
    WHITELIST_SET.add( "</b>" );
    WHITELIST_SET.add( "<i>" );
    WHITELIST_SET.add( "</i>" );
    WHITELIST_SET.add( "<u>" );
    WHITELIST_SET.add( "</u>" );
    WHITELIST_SET.add( "<ol>" );
    WHITELIST_SET.add( "</ol>" );
    WHITELIST_SET.add( "<ul>" );
    WHITELIST_SET.add( "</ul>" );
    WHITELIST_SET.add( "<li>" );
    WHITELIST_SET.add( "</li>" );
    WHITELIST_SET.add( "<strong>" );
    WHITELIST_SET.add( "</strong>" );
    WHITELIST_SET.add( "<em>" );
    WHITELIST_SET.add( "</em>" );
    WHITELIST_SET.add( "<p>" );
    WHITELIST_SET.add( "</p>" );
  }

  public static String removeFormatting( String value )
  {
    if ( StringUtils.isEmpty( value ) )
    {
      return value;
    }
    return REMOVE_TAGS.matcher( value ).replaceAll( " " );
  }

  /**
   * Removes HTML formatting, leaving behind tags that are in a whitelist. (Generally, styling tags like bold, underline, etc)
   */
  public static String whitelistFormatting( String value )
  {
    if ( StringUtils.isEmpty( value ) )
    {
      return value;
    }

    StringBuffer buffer = new StringBuffer();
    Matcher matcher = REMOVE_TAGS.matcher( value );
    while ( matcher.find() )
    {
      if ( !WHITELIST_SET.contains( matcher.group() ) )
      {
        matcher.appendReplacement( buffer, "" );
      }
    }
    return matcher.appendTail( buffer ).toString();
  }
}
