
package com.biperf.core.ui.ws.rest.sea.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.biperf.core.utils.StringUtil;


public class HashTagParser
{
  private static String POINTS_REGEX = "#([0-9]+\\w*)";
  private static String BEHAVIORS_REGEX = "#([a-zA-Z]+\\w*)";

  public static int getPointsHashTag( String messageContent )
  {
    if ( null == messageContent )
      return 0;

    Pattern pattern = Pattern.compile( POINTS_REGEX );
    Matcher matcher = pattern.matcher( messageContent );
    while ( matcher.find() )
    {
      String hashtagValue = matcher.group().substring( 1 );
      try
      {
        Integer.parseInt( hashtagValue );
        return new Integer( hashtagValue ).intValue();
      }
      catch( Exception e )
      {
      }
    }

    return 0;
  }

  public static String getBehaviorHashTag( String messageContent )
  {
    messageContent = messageContent.split( "\\^", 2 )[0];
    if ( null == messageContent )
      return null;

    Pattern pattern = Pattern.compile( BEHAVIORS_REGEX );
    Matcher matcher = pattern.matcher( messageContent );
    while ( matcher.find() )
    {
      String hashtagValue = matcher.group().substring( 1 );
      if ( StringUtils.isAlpha( hashtagValue ) && !"end".equals( hashtagValue ) )
        return hashtagValue;
    }
    return null;
  }

  public static String getBehaviour( String message )
  {
    String behavioursAddedBySystem = "";
    Pattern pattern = Pattern.compile( "(\n\r\\s{3}\n\r\\(\\s#[^\\)]*\\))" );
    Matcher matcher = pattern.matcher( message );

    while ( matcher.find() )
    {
      behavioursAddedBySystem = matcher.group( 1 );
    }
    message = message.replaceAll( behavioursAddedBySystem, "" );
    message = message.replaceAll( "\\^", "" );
    return getBehaviorHashTag( message );
  }

  public static boolean isEndTagAvailable( String messageContent )
  {
    Pattern pattern = Pattern.compile( "#END", Pattern.CASE_INSENSITIVE );
    Matcher matcher = pattern.matcher( messageContent );
    return matcher.find();
  }

  public static String getMessage( String originalMessage, String responseMessage )
  {
    Pattern pattern = Pattern.compile( "#END", Pattern.CASE_INSENSITIVE );
    Matcher matcher = pattern.matcher( responseMessage );
    int end = -1;
    while ( matcher.find() )
    {
      end = matcher.start();
    }
    // search for the start now that we have the end point
    String seaMessageAndStoppedMessage = responseMessage.substring( 0, end );

    // now we need to find the "start"
    String sub = originalMessage;
    while ( true )
    {
      sub = originalMessage.substring( 0, ( sub.length() - 1 ) );
      if ( seaMessageAndStoppedMessage.indexOf( sub ) > -1 )
      {
        // System.out.println( sub ) ;
        break;
      }
      if ( sub.isEmpty() || sub.length() == 0 )
      {
        break;
      }
    }
    return sub;
  }

  public static String getMessage( String originalMessage )
  {
    if ( StringUtil.isNullOrEmpty( originalMessage ) )
    {
      return originalMessage;
    }
    else
    {
      String end = "#END";
      int index = originalMessage.toLowerCase().indexOf( end.toLowerCase() );
      if ( index != -1 )
      {
        return originalMessage.substring( 0, index );
      }
      else
      {
        return originalMessage.substring( 0 );
      }
    }
  }

  // Removes all Hashtags
  public static String removeHashtags( String messageContent )
  {
    Pattern pattern = Pattern.compile( "#([a-zA-Z0-9]+\\w*)" );
    Matcher matcher = pattern.matcher( messageContent );
    while ( matcher.find() )
    {
      // ex: replaces #greatidea with ""
      messageContent = messageContent.replace( matcher.group().substring( 0 ), "" );
    }
    return messageContent;
  }

  // Removes all Hashtags except #end, #END, #End
  public static String removeHashtagsExceptEndTags( String messageContent )
  {
    System.out.println( "Message to parse and remove hash tags except #end tags is \n" + messageContent );
    Pattern pattern = Pattern.compile( "#\\b(?:(?!(end|END|End))\\w)+\\b" );
    Matcher matcher = pattern.matcher( messageContent );
    while ( matcher.find() )
    {
      // ex: replaces #greatidea with "" except #end #END #End
      messageContent = messageContent.replace( matcher.group().substring( 0 ), "" );
    }
    System.out.println( "Message after parsed and removed hash tags #end tags is \n" + messageContent );
    return messageContent;
  }

}
