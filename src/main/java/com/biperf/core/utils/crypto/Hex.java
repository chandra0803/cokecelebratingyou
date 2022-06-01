/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/crypto/Hex.java,v $
 */

package com.biperf.core.utils.crypto;

import java.io.ByteArrayOutputStream;

/**
 * Encode and decode between Hex and Binary.
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
 * <td>zahler</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Hex extends Object
{

  byte[] m_value = null;

  public Hex( String string )
  {
    m_value = decode( string );
  }

  public byte[] getBytes()
  {
    return m_value;
  }

  public String toString()
  {
    return encode( m_value );
  }

  public int hashCode()
  {
    return super.hashCode();
  }

  public boolean equals( java.lang.Object object )
  {
    String s1 = object.toString();
    String s2 = this.toString();
    return s1.equals( s2 );
  }

  public static final String ERROR_ODD_NUMBER_OF_DIGITS = "Odd number of digits in hex string";
  public static final String ERROR_BAD_CHARACTER_IN_HEX_STRING = "Bad character or insufficient number of characters in hex string";

  // Code from Ajp11, from Apache's JServ

  // Table for HEX to DEC byte translation
  public static final int[] DEC = { -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    00,
                                    01,
                                    02,
                                    03,
                                    04,
                                    05,
                                    06,
                                    07,
                                    8,
                                    9,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    10,
                                    11,
                                    12,
                                    13,
                                    14,
                                    15,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    10,
                                    11,
                                    12,
                                    13,
                                    14,
                                    15,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1,
                                    -1, };

  /**
   * Convert a String of hexadecimal digits into the corresponding byte array by encoding each two
   * hexadecimal digits as a byte.
   * 
   * @param digits Hexadecimal digits representation
   * @return byte[]
   * @exception IllegalArgumentException if an invalid hexadecimal digit is found, or the input
   *              string contains an odd number of hexadecimal digits
   */
  public static byte[] decode( String digits )
  {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    for ( int i = 0; i < digits.length(); i += 2 )
    {
      char c1 = digits.charAt( i );
      if ( i + 1 >= digits.length() )
      {
        throw new IllegalArgumentException( ERROR_ODD_NUMBER_OF_DIGITS );
      }
      char c2 = digits.charAt( i + 1 );
      byte b = 0;
      if ( c1 >= '0' && c1 <= '9' )
      {
        b += ( c1 - '0' ) * 16;
      }
      else if ( c1 >= 'a' && c1 <= 'f' )
      {
        b += ( c1 - 'a' + 10 ) * 16;
      }
      else if ( c1 >= 'A' && c1 <= 'F' )
      {
        b += ( c1 - 'A' + 10 ) * 16;
      }
      else
      {
        throw new IllegalArgumentException( ERROR_BAD_CHARACTER_IN_HEX_STRING );
      }
      if ( c2 >= '0' && c2 <= '9' )
      {
        b += c2 - '0';
      }
      else if ( c2 >= 'a' && c2 <= 'f' )
      {
        b += c2 - 'a' + 10;
      }
      else if ( c2 >= 'A' && c2 <= 'F' )
      {
        b += c2 - 'A' + 10;
      }
      else
      {
        throw new IllegalArgumentException( ERROR_BAD_CHARACTER_IN_HEX_STRING );
      }
      baos.write( b );
    }
    return baos.toByteArray();

  }

  /**
   * Convert a byte array into a printable format containing a String of hexadecimal digit
   * characters (two per byte).
   * 
   * @param bytes Byte array representation
   * @return String
   */
  public static String encode( byte[] bytes )
  {

    StringBuffer sb = new StringBuffer( bytes.length * 2 );
    for ( int i = 0; i < bytes.length; i++ )
    {
      sb.append( convertDigit( bytes[i] >> 4 ) );
      sb.append( convertDigit( bytes[i] & 0x0f ) );
    }
    return sb.toString();

  }

  /**
   * Convert 4 hex digits to an int, and return the number of converted bytes.
   * 
   * @param hex Byte array containing exactly four hexadecimal digits
   * @return int
   * @exception IllegalArgumentException if an invalid hexadecimal digit is included
   */
  public static int convert2Int( byte[] hex )
  {
    // Code from Ajp11, from Apache's JServ

    // assert b.length==4
    // assert valid data
    int len;
    if ( hex.length < 4 )
    {
      return 0;
    }
    if ( DEC[hex[0]] < 0 )
    {
      throw new IllegalArgumentException( ERROR_BAD_CHARACTER_IN_HEX_STRING );
    }
    len = DEC[hex[0]];
    len = len << 4;
    if ( DEC[hex[1]] < 0 )
    {
      throw new IllegalArgumentException( ERROR_BAD_CHARACTER_IN_HEX_STRING );
    }
    len += DEC[hex[1]];
    len = len << 4;
    if ( DEC[hex[2]] < 0 )
    {
      throw new IllegalArgumentException( ERROR_BAD_CHARACTER_IN_HEX_STRING );
    }
    len += DEC[hex[2]];
    len = len << 4;
    if ( DEC[hex[3]] < 0 )
    {
      throw new IllegalArgumentException( ERROR_BAD_CHARACTER_IN_HEX_STRING );
    }
    len += DEC[hex[3]];
    return len;
  }

  /**
   * [Private] Convert the specified value (0 to 15) to the corresponding hexadecimal digit.
   * 
   * @param value Value to be converted
   */
  private static char convertDigit( int value )
  {

    value &= 0x0f;
    if ( value >= 10 )
    {
      return (char) ( value - 10 + 'a' );
    }
    return (char) ( value + '0' );

  }
}
