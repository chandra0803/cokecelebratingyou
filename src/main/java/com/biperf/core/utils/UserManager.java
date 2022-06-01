/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/UserManager.java,v $
 */

package com.biperf.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.SAO;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;

/**
 * The UserManager is used to set the username on the thread. The username is used in the Audit
 * Interceptor to set the modified_by and created_by fields in the database.
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
 * <td>jdunne</td>
 * <td>Mar 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserManager
{

  private static Locale[] availableLocales = null;

  /**
   * @param user
   */
  public static void setUser( AuthenticatedUser user )
  {
    ResourceManager.setResource( ParamConstants.AUTHENTICATED_USER, user );
  }

  /**
   * @return userId
   */
  public static Long getUserId()
  {
    if ( isUserLoggedIn() )
    {
      return getUser().getUserId();
    }
    return null;
  }

  /**
   * @return userId
   */
  public static UUID getRosterUserId()
  {
    if ( isUserLoggedIn() )
    {
      return getUser().getRosterUserId();
    }
    return null;
  }

  /**
   * Get the current usernaame from the user object in ThreadLocal.
   * 
   * @return username
   */
  public static String getUserName()
  {
    if ( isUserLoggedIn() )
    {
      return getUser().getUsername();
    }
    return "";
  }

  /**
   * Get the current user's first MI and last name from the user object in ThreadLocal. Return
   * firstName + middle initial + lastName
   * 
   * @return String
   */
  public static String getUserFullName()
  {
    if ( isUserLoggedIn() )
    {
      String middleInitial = "";
      if ( getUser().getMiddleName() != null && getUser().getMiddleName().length() > 0 )
      {
        middleInitial = getUser().getMiddleName().substring( 0, 1 );
      }
      return getUser().getFirstName() + " " + middleInitial + " " + getUser().getLastName();
    }
    return "";
  }

  private static String getUserPrimaryCountryCode()
  {
    String primaryCountryCode = null;
    if ( isUserLoggedIn() )
    {
      primaryCountryCode = getUser().getPrimaryCountryCode();
      if ( null == primaryCountryCode )
      {
        Country country = getUserService().getPrimaryUserAddressCountry( getUser().getUserId() );
        if ( null != country )
        {
          primaryCountryCode = country.getCountryCode();
          getUser().setPrimaryCountryCode( primaryCountryCode );
        }
      }
    }
    return primaryCountryCode;
  }

  public static String getUserPrimaryCountryCurrencyCode()
  {
    if ( isUserLoggedIn() )
    {
      if ( null != getUser().getPrimaryCountryCode() )
      {
        Country country = getUserService().getPrimaryUserAddressCountry( getUser().getUserId() );
        if ( null != country )
        {
          return country.getCurrencyCode();
        }
      }
    }
    return "USD";
  }
  
  //Client customization start
  public static String getUsersDivision()
  {
    String division = null;
    if ( isUserLoggedIn() )
    {
      Long userCharId = getSystemVariableService().getPropertyByName( SystemVariableService.COKE_USER_DIVISION_KEY_CHAR ).getLongVal();
      division = getUserCharacteristicService().getCharacteristicValueByUserAndCharacterisiticId( getUser().getUserId(), userCharId );
    }
    return division;
  }
  //Client customization end

  /**
   * Get the current user's first MI and last name from the user object in ThreadLocal. Return
   * lastName, + firstName + middle initial
   * 
   * @return String
   */
  public static String getNameLFMWithComma()
  {
    StringBuffer fullName = new StringBuffer();
    if ( isUserLoggedIn() )
    {
      if ( getUser().getLastName() != null )
      {
        fullName.append( getUser().getLastName() ).append( ", " );
      }

      if ( getUser().getFirstName() != null )
      {
        fullName.append( getUser().getFirstName() );
      }

      if ( getUser().getMiddleName() != null )
      {
        String middleInitial = getUser().getMiddleName().substring( 0, 1 );
        fullName.append( " " ).append( middleInitial ).append( " " );
      }

      return fullName.toString();
    }
    return "";
  }

  /**
   * Clear the user from ThreadLocal.
   */
  public static void removeUser()
  {
    ResourceManager.removeResource( ParamConstants.AUTHENTICATED_USER );
  }

  /**
    * @return system variable
   */
  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  /**
   * will return true if the user is logged in, false otherwise
   * 
   * @return boolean isUserLoggedIn
   */
  public static boolean isUserLoggedIn()
  {
    if ( getUser() == null )
    {
      return false;
    }
    return true;
  }

  /**
   * @return AuthenticatedUser
   */
  public static AuthenticatedUser getUser()
  {
    return (AuthenticatedUser)ResourceManager.getResource( ParamConstants.AUTHENTICATED_USER );
  }

  public static String getTimeZoneID()
  {
    String timeZoneID = null;
    if ( null != getUser() )
    {
      timeZoneID = getUser().getTimeZoneId();
    }
    if ( StringUtils.isEmpty( timeZoneID ) )
    {
      timeZoneID = getClaimService().getDBTimeZone();
    }
    return timeZoneID;
  }

  public static Date getCurrentDateWithTimeZoneID()
  {
    return DateUtils.applyTimeZone( new Date(), getTimeZoneID() );
  }

  public static Locale getLocale()
  {
    // Set the default locale
    Locale locale = Locale.getDefault();

    // If user is logged in and has a locale preference, set it to user locale
    if ( isUserLoggedIn() && null != getUser().getLocale() && !getUser().isUser() )
    {
      String countryCode = getUserPrimaryCountryCode();
      if ( null != countryCode )
      {
        locale = getLocaleBasedOnCountry( countryCode, getUser().getLocale() );
      }
      else
      {
        locale = getDefaultLocale();
      }
    }
    else if ( isUserLoggedIn() )
    {
      String countryCode = getUserPrimaryCountryCode();
      if ( null != countryCode )
      {
        locale = getLocaleBasedOnCountry( countryCode, getUser().getLocale() );
      }
      else
      {
        locale = getDefaultLocale();
      }
    }
    else
    {
      locale = getDefaultLocale();
    }

    return locale;
  }

  public static Locale getLoggedInLocale()
  {
    Locale locale = Locale.getDefault();
    // Set the default locale
    // if session locale, return the locale,
    // else, call getLocale();
    return locale;

  }

  /*
   * This test is used to determine if the user logged in is able to access "Participant"
   * information. When running as a delegate or launching as, the user should not be able to hit/use
   * certain resources
   */

  public static boolean isUserDelegateOrLaunchedAs()
  {
    AuthenticatedUser user = UserManager.getUser();

    if ( user == null )
    {
      return false;
    }
    else if ( user.isDelegate() || user.isLaunched() )
    {
      return true;
    }

    return false;
  }

  private static Locale getLocaleBasedOnCountry( String countryCode, Locale userLocale )
  {
    List<LanguageType> languageList = LanguageType.getList();
    List<Locale> locales = new ArrayList<Locale>();

    for ( LanguageType languagetype : languageList )
    {
      Locale locale = LocaleUtils.getLocale( languagetype.getCode() );
      locales.add( locale );
    }

    if ( availableLocales == null )
    {
      sortLocales();
    }
    for ( Locale availableLocale : availableLocales )
    {
      if ( userLocale != null && availableLocale.getCountry().equalsIgnoreCase( userLocale.getCountry() ) && availableLocale.getLanguage().equalsIgnoreCase( userLocale.getLanguage() ) )
      {
        return availableLocale;
      }
    }
    for ( Locale availableLocale : availableLocales )
    {
      if ( userLocale != null && availableLocale.getCountry().equalsIgnoreCase( countryCode ) && availableLocale.getLanguage().equalsIgnoreCase( userLocale.getLanguage() ) )
      {
        return availableLocale;
      }
    }
    for ( Locale availableLocale : availableLocales )
    {
      if ( availableLocale.getCountry().equalsIgnoreCase( countryCode ) && locales.contains( availableLocale ) )
      {
        return availableLocale;
      }
    }
    return getDefaultLocale();
  }

  private static void sortLocales()
  {
    availableLocales = Locale.getAvailableLocales();
    Comparator<Locale> localeComparator = new Comparator<Locale>()
    {
      public int compare( Locale locale1, Locale locale2 )
      {
        return locale1.toString().compareTo( locale2.toString() );
      }
    };
    Arrays.sort( availableLocales, localeComparator );
  }

  public static Locale getDefaultLocale()
  {
    String defaultLanguage = getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    return LocaleUtils.getLocale( defaultLanguage );
  }

  /**
   * 
   * @return date pattern based on locale
   */
  public static String getUserDatePattern()
  {
    return DateFormatterUtil.getDatePattern( UserManager.getLocale() );
  }

  /**
   * 
   * @return language based on locale
   */
  public static String getUserLanguage()
  {
    return UserManager.getLocale().getLanguage();
  }

  /**
   * 
   * @return locale as string
   */
  public static String getUserLocale()
  {
    return UserManager.getLocale().toString();
  }

  /**
   * Bean location through BeanLocator look-up.
   * 
   * @param beanName
   * @return SAO
   */
  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  private static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  public static HttpSession getSession()
  {
    return (HttpSession)ResourceManager.getResource( ParamConstants.HTTP_SESSION );
  }

  public static void setSession( HttpSession session )
  {
    ResourceManager.setResource( ParamConstants.HTTP_SESSION, session );
  }

  public static void removeSession()
  {
    ResourceManager.removeResource( ParamConstants.HTTP_SESSION );
  }

  public static Long getPrimaryNodeId()
  {
    return getUser().getPrimaryNodeId();
  }

  public static Long getOriginalUserId()
  {
    if ( UserManager.isUserDelegateOrLaunchedAs() )
    {
      return UserManager.getUser().getOriginalAuthenticatedUser().getUserId();
    }
    else
    {
      return UserManager.getUserId();
    }
  }

  public static boolean isUserInRole( String roleName )
  {
    if ( roleName == null )
    {
      return false;
    }
    if ( !roleName.startsWith( "ROLE_" ) )
    {
      roleName = "ROLE_" + roleName;
    }
    GrantedAuthority authority = new SimpleGrantedAuthority( roleName );
    return isUserInRole( authority );
  }

  public static boolean isUserInRole( GrantedAuthority authority )
  {
    AuthenticatedUser user = getUser();
    if ( null == user )
    {
      return false;
    }
    return user.getAuthorities().contains( authority );
  }

  private static ClaimService getClaimService()
  {
    return (ClaimService)BeanLocator.getBean( ClaimService.BEAN_NAME );
  }
  
  private static UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)BeanLocator.getBean( UserCharacteristicService.BEAN_NAME );
  }
}
