/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/utils/ValidatorChecks.java,v $
 */

package com.biperf.core.ui.utils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.Resources;

import com.biperf.core.utils.ValidationUtil;

/**
 * ValidatorChecks.
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
 * <td>May 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ValidatorChecks implements Serializable
{
  private static final String ALLOWED_CHARS = "[A-Za-z0-9]+";

  private static final String ALLOWED_CHARS_FOR_IMAGE_UPDLOAD = "[^_a-zA-Z0-9]";

  /**
   * Checks if the field is a valid phone number.
   * 
   * @param bean The bean validation is being performed on.
   * @param va The <code>ValidatorAction</code> that is currently being performed.
   * @param field The <code>Field</code> object associated with the current field being validated.
   * @param errors The <code>ActionMessages</code> object to add errors to if any validation
   *          errors occur.
   * @param request Current request object.
   * @return true if valid, false otherwise.
   */
  public static Object validatePhoneNumber( Object bean, ValidatorAction va, Field field, ActionMessages errors, HttpServletRequest request )
  {

    String value = null;
    if ( isString( bean ) )
    {
      value = (String)bean;
    }
    else
    {
      value = ValidatorUtils.getValueAsString( bean, field.getProperty() );
    }

    if ( !ValidationUtil.isValidPhoneNumber( value ) )
    {
      errors.add( field.getKey(), Resources.getActionMessage( request, va, field ) );
      return Boolean.FALSE;
    }

    return Boolean.TRUE;

  }

  /**
   * Return <code>true</code> if the specified object is a String or a <code>null</code> value.
   * 
   * @param o Object to be tested
   * @return The string value
   */
  protected static boolean isString( Object o )
  {
    return o == null ? true : String.class.isInstance( o );
  }

  /**
   * Return <code>true</code> if the specified String contains special characters as defined
   * by the SPECIAL_CHARACTERS_EXCLUDED constant.
   * 
   * @param stringToCheck
   * @return boolean
   */
  public static boolean containsSpecialCharactersOrSpaces( String stringToCheck )
  {
    if ( stringToCheck == null || stringToCheck.equals( "" ) || Pattern.matches( ALLOWED_CHARS, stringToCheck ) )
    {
      return false;
    }

    return true;
  }

  public static String removesSpecialCharactersAndSpaces( String stringToCheck )
  {
    Pattern pt = Pattern.compile( ALLOWED_CHARS_FOR_IMAGE_UPDLOAD );
    Matcher match = pt.matcher( stringToCheck );
    while ( match.find() )
    {
      String specialCharacter = match.group();
      stringToCheck = stringToCheck.replaceAll( "\\" + specialCharacter, "" );
    }
    return stringToCheck;
  }
}
