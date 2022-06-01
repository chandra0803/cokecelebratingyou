
package com.biperf.core.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatUtil
{

  public static String getUserLocaleBasedNumberDelimiter()
  {
    String delimiter = "";
    for ( char c : getUserLocaleBasedNumberPattern().toCharArray() )
    {
      if ( (int)c != 35 )
      {
        delimiter = String.valueOf( c );
        break;
      }
    }
    return delimiter;
  }

  public static String getUserLocaleBasedNumberPattern()
  {
    return getLocaleBasedNumberFormat( 111111111111L ).replace( '1', '#' );
  }

  public static String getLocaleBasedNumberFormat( long amount )
  {
    return NumberFormat.getNumberInstance( UserManager.getLocale() ).format( amount );
  }

  public static String getUserLocaleBasedNumberFormat( long amount, Locale userlocale )
  {
    return NumberFormat.getNumberInstance( userlocale ).format( amount );
  }

  public static String getLocaleBasedBigDecimalFormat( BigDecimal amount, int precision, Locale userlocale )
  {
    return getLocaleBasedBigDecimalFormat( amount, precision, userlocale, precision );
  }

  public static String getLocaleBasedBigDecimalFormat( BigDecimal amount, int precision, Locale userlocale, int minPrecision )
  {
    NumberFormat numberFormat;
    String decimalOut = null;
    if ( amount != null )
    {
      numberFormat = NumberFormat.getInstance( userlocale );
      int value = precision;
      if ( value == 0 )
      {
        numberFormat.setMaximumFractionDigits( 0 );
      }
      else if ( value == 1 )
      {
        numberFormat.setMaximumFractionDigits( 1 );
      }
      else if ( value == 2 )
      {
        numberFormat.setMaximumFractionDigits( 2 );
      }
      else if ( value == 3 )
      {
        numberFormat.setMaximumFractionDigits( 3 );
      }
      else if ( value == 4 )
      {
        numberFormat.setMaximumFractionDigits( 4 );
      }
      if ( minPrecision != 0 )
      {
        numberFormat.setMinimumFractionDigits( minPrecision );
      }
      double dblAmount = amount.doubleValue();
      decimalOut = numberFormat.format( dblAmount );
    }
    return decimalOut;
  }

  public static String getLocaleBasedCurrencyFormat( Object number, int precision )
  {
    if ( number == null )
    {
      return null;
    }
    else
    {
      NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance( UserManager.getLocale() );
      currencyFormatter.setMaximumFractionDigits( precision );
      return currencyFormatter.format( number );
    }
  }

  public static String getCurrencyWithSymbol( Object number, String currencySymbol )
  {
    return number != null ? currencySymbol + getLocaleBasedNumberFormat( number, 2 ) : null;
  }

  public static String getLocaleBasedNumberFormat( Object number, int precision )
  {
    if ( number == null )
    {
      return null;
    }
    else
    {
      NumberFormat numberFormatter = NumberFormat.getNumberInstance( UserManager.getLocale() );
      numberFormatter.setMaximumFractionDigits( precision );
      return numberFormatter.format( number );
    }
  }

  public static String getLocaleBasedDobleNumberFormat( Double number )
  {
    if ( number == null )
    {
      return null;
    }
    else
    {
      String bigNumber = BigDecimal.valueOf( number ).toPlainString();
      int decimalPlace = bigNumber.toString().indexOf( '.' );
      int precision = decimalPlace > -1 ? bigNumber.substring( decimalPlace, bigNumber.length() ).length() : 0;
      NumberFormat numberFormatter = NumberFormat.getNumberInstance( UserManager.getLocale() );
      numberFormatter.setMaximumFractionDigits( precision );
      return numberFormatter.format( number );
    }
  }

  public static BigDecimal nullCheckBigDecimal( BigDecimal value )
  {
    return value == null ? new BigDecimal( 0 ) : value;
  }

  public static Long convertStringToLongDefaultZero( String stringVal )
  {
    Long longVal = null;
    try
    {
      longVal = new Long( stringVal );
    }
    catch( NumberFormatException e )
    {
      longVal = new Long( 0 );
    }
    return longVal;
  }

  // added getLocaleBasedRoundingBigDecimalFormat(..) to get rounded value for bug #65143
  public static String getLocaleBasedRoundingBigDecimalFormat( BigDecimal amount, int precision, int roundingMode, Locale userlocale, int minPrecision )
  {
    NumberFormat numberFormat;
    String decimalOut = null;
    if ( amount != null )
    {
      numberFormat = NumberFormat.getInstance( userlocale );
      int value = precision;
      if ( value == 0 )
      {
        numberFormat.setMaximumFractionDigits( 0 );
      }
      else if ( value == 1 )
      {
        numberFormat.setMaximumFractionDigits( 1 );
      }
      else if ( value == 2 )
      {
        numberFormat.setMaximumFractionDigits( 2 );
      }
      else if ( value == 3 )
      {
        numberFormat.setMaximumFractionDigits( 3 );
      }
      else if ( value == 4 )
      {
        numberFormat.setMaximumFractionDigits( 4 );
      }
      if ( minPrecision != 0 )
      {
        numberFormat.setMinimumFractionDigits( minPrecision );
      }

      if ( BigDecimal.ROUND_HALF_UP != roundingMode )
      {
        amount = amount.setScale( precision - 1, roundingMode );
      }
      else
      {
        amount = amount.setScale( precision, roundingMode );
      }

      double dblAmount = amount.doubleValue();
      decimalOut = numberFormat.format( dblAmount );
    }
    return decimalOut;
  }
}
