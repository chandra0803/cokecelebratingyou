
package com.biperf.core.utils;

import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class LocaleUtils
{
  private LocaleUtils()
  {
  }

  public static Locale getLocale( String localeCode )
  {
    Locale locale = Locale.US;
    if ( !StringUtils.isEmpty( localeCode ) )
    {
      if ( localeCode.length() == 2 )
      {
        locale = new Locale( localeCode );
      }
      else if ( localeCode.length() == 5 )
      {
        locale = new Locale( localeCode.substring( 0, 2 ), localeCode.substring( 3, localeCode.length() ) );
      }
      else if ( localeCode.length() == 8 )
      {
        locale = new Locale( localeCode.substring( 0, 2 ), localeCode.substring( 3, 5 ), localeCode.substring( 6, localeCode.length() ) );
      }
    }
    return locale;
  }

  public static Locale findClosestMatchingLocale( Locale requestedLocale, Set<Locale> locales )
  {
    // shouldn't happen, but if the client gives us a null value, they get the default
    if ( null == requestedLocale )
    {
      return findClosestMatchingLocale( Locale.US, locales );
    }

    // check for exact match
    if ( locales.contains( requestedLocale ) )
    {
      return requestedLocale;
    }

    // check the language only
    if ( !StringUtils.isBlank( requestedLocale.getCountry() ) )
    {
      Locale languageOnly = reduce( requestedLocale );
      if ( locales.contains( languageOnly ) )
      {
        return languageOnly;
      }
      else
      {
        // ok, if the collection contains a Locale somewhere with the same language, grab the first
        // one. ie,
        // I want fr, but you only have fr_CA, well, that will do!
        for ( Locale locale : locales )
        {
          if ( locale.getLanguage().equals( languageOnly.getLanguage() ) )
          {
            return locale;
          }
        }
      }
      // here's out break-out of recursion...if there is NO english, then return null
      if ( languageOnly.equals( Locale.ENGLISH ) )
      {
        return null;
      }
    }
    else // ok, the Locale request is ONLY for a language, grab the first one. ie,
    // I want fr, but you only have fr_CA, well, that will do!
    {
      // ok, if the collection contains a Locale somewhere with the same language, use it..grab the
      // first one
      for ( Locale locale : locales )
      {
        if ( locale.getLanguage().equals( requestedLocale.getLanguage() ) )
        {
          return locale;
        }
      }
    }
    // ok, do it all again, but default to English US
    Locale defaultLocale = findClosestMatchingLocale( Locale.US, locales );

    // something went way wrong, return somethings
    if ( null == defaultLocale )
    {
      return locales.iterator().next();
    }

    return defaultLocale;
  }

  public static Locale reduce( Locale locale )
  {
    if ( !StringUtils.isBlank( locale.getVariant() ) )
    {
      return new Locale( locale.getLanguage(), locale.getCountry() );
    }
    if ( !StringUtils.isBlank( locale.getCountry() ) )
    {
      return new Locale( locale.getLanguage() );
    }
    if ( !locale.getLanguage().equals( Locale.ENGLISH.getLanguage() ) )
    {
      return Locale.US;
    }
    return null;
  }
}
