/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.utils;

import java.util.regex.Pattern;

import org.apache.commons.validator.GenericValidator;

/**
 * .
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
 * <td>wadzinsk</td>
 * <td>Mar 10, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ValidationUtil
{

  /**
   * Reg expression for validating a USA or international phone number with an optional extension.
   */
  private static Pattern phoneNumberPattern = Pattern.compile( "^(\\(?\\+?[0-9]*\\)?)?[0-9_\\-\\. \\(\\)]*(?:(?:[ ]+|[xX]|(i:ext[\\.]?)){1,2}([\\d]{1,5}))?$" );

  /**
   * Check to see if an phoneNumber is valid according to the phoneNumberPattern regular expression.
   * This method allows for both domestic and international numbers with optional extensions.
   * 
   * @param phoneNumber
   * @return boolean
   */
  public static boolean isValidPhoneNumber( String phoneNumber )
  {
    if ( GenericValidator.isBlankOrNull( phoneNumber ) )
    {
      return true;
    }
    return phoneNumberPattern.matcher( phoneNumber ).matches();
  }
}
