
package com.biperf.core.utils;

// This is only for US phone number format(xxx-xxx-xxxx) or normal number(xxxxxxxxxx) validator. 
public class PhoneNumberValidator
{
  /*
   * Rules according to Emilie. This checks the length in digits of the number ignoring any other
   * elements.
   */
  public static boolean isValidPhoneNumberLength( String phone )
  {
    if ( phone == null )
    {
      return false;
    }
    String digits = phone.replaceAll( "[^0-9]", "" );
    if ( digits.length() < 4 || digits.length() > 15 )
    {
      return false;
    }
    return true;
  }

  public static boolean checkPhoneNumberFormat( String textPhoneNbr )
  {
    String textPhoneNbrTrmd = textPhoneNbr.trim();

    // check for Length
    if ( ! ( textPhoneNbrTrmd.length() == 12 || textPhoneNbrTrmd.length() == 10 ) )
    {
      return false;
    }
    else
    {
      // if length is 10. ex:0123456789.
      if ( textPhoneNbrTrmd.length() == 10 )
      {
        return textPhoneNbrTrmd.matches( "\\d{10}" );
      }
      else // If Length is 12.
      {
        // check for 4th and 7th palces
        if ( ! ( textPhoneNbrTrmd.charAt( 3 ) == '-' ) || ! ( textPhoneNbrTrmd.charAt( 7 ) == '-' ) )
        {
          return false;
        }
        return getOnlyNumerics( textPhoneNbrTrmd );
      }
    }
  }

  private static boolean getOnlyNumerics( String str )
  {
    char c;
    for ( int i = 0; i < str.length(); i++ )
    {
      if ( i != 3 && i != 7 )
      {
        c = str.charAt( i );
        if ( !Character.isDigit( c ) )
        {
          return false;
        }
      }
    }
    return true;
  }

}
