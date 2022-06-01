/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/ArrayUtil.java,v $
 */

package com.biperf.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * ArrayUtil.
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
public class ArrayUtil
{

  /**
   * Builds a list of string tokens.
   * 
   * @param pStringTokenizer
   * @return List
   */
  public static List convertStringTokenizerToList( StringTokenizer pStringTokenizer )
  {

    List lListOfTokens = new ArrayList( pStringTokenizer.countTokens() );

    // Keep a positing count to avoid using bigger objects which maintain order.
    int pos = 0;
    while ( pStringTokenizer.hasMoreTokens() )
    {
      lListOfTokens.add( pos, pStringTokenizer.nextToken() );
      pos++;
    }
    return lListOfTokens;
  }

  /**
   * Converts the list passed in into a Long[].
   * 
   * @param listOfLongs
   * @return Long[]
   */
  public static Long[] convertListToLongArray( List listOfLongs )
  {
    Long[] longArray = new Long[listOfLongs.size()];

    Iterator listOfLongsIter = listOfLongs.iterator();
    int count = 0;
    while ( listOfLongsIter.hasNext() )
    {
      longArray[count] = (Long)listOfLongsIter.next();
      count++;
    }

    return longArray;
  }

  /**
   * Converts an Object[] to a set.
   * 
   * @param pArray
   * @return Set
   */
  public static Set convertArrayToSet( Object[] pArray )
  {
    Set lNewSet = new HashSet();
    for ( int i = 0; i < pArray.length; i++ )
    {
      lNewSet.add( pArray[i] );
    }
    return lNewSet;
  }

  /**
   * Convert a delimited string to a list of strings. The input string is usually of the form
   * <code>{a,b,c}</code>. The delimiter in this case is "," and the stripCharacters are "{}".
   * Note: stripCharacaters can be null and is provided as a convience since the
   * convertStringArrayToDelimited method puts the curly braces in the string.
   * 
   * @param str
   * @param delim
   * @return List
   */
  public static List convertDelimitedStringToList( String str, String delim )
  {
    if ( str == null || str.length() == 0 )
    {
      return Collections.EMPTY_LIST;
    }

    List list = new ArrayList();
    String workingStr = str;

    StringTokenizer st = new StringTokenizer( workingStr, delim );
    while ( st.hasMoreTokens() )
    {
      String token = st.nextToken();
      list.add( token );
    }
    return list;
  }

  /**
   * Convert a list of string objects to an array of strings.
   * 
   * @param strList
   * @return List
   */
  public static String[] convertListToStringArray( List strList )
  {
    if ( strList == null || strList.size() == 0 )
    {
      return new String[0];
    }

    String[] result = new String[strList.size()];

    for ( int ix = 0; ix < strList.size(); ix++ )
    {
      result[ix] = (String)strList.get( ix );
    }

    return result;
  }

  /**
   * Convert
   * 
   * @param strs
   * @param delim
   * @return String
   */
  public static String convertStringArrayToDelimited( String[] strs, String delim )
  {
    String str = ArrayUtils.toString( strs, null );
    // strip off the curly braces that ArrayUtils puts on the string...
    str = StringUtils.replaceChars( str, "{}", "" );
    if ( !delim.equals( "," ) )
    {
      // replace the comma that ArrayUtils puts in...
      str = StringUtils.replace( str, ",", delim );
    }
    return str;
  }

  /**
   * Converts an array of Strings to a list of Longs
   * 
   * @param stringArray
   * @return List
   */
  public static List convertStringArrayToListOfLongObjects( String[] stringArray )
  {

    List listOfLongObjects = new ArrayList();

    for ( int i = 0; i < stringArray.length; i++ )
    {
      String id = stringArray[i];
      Long longId = new Long( id );

      listOfLongObjects.add( longId );
    }

    return listOfLongObjects;

  }

  /**
   * Converts a String[] to a list of Strings.
   * 
   * @param stringArray
   * @return List
   */
  public static List stringArrayToList( String[] stringArray )
  {

    List listOfStrings = new ArrayList();

    for ( int i = 0; i < stringArray.length; i++ )
    {
      String stringValue = stringArray[i];
      listOfStrings.add( stringValue );
    }

    return listOfStrings;

  }

}
