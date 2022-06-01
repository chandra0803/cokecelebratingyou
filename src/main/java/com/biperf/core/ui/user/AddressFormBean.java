/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/user/AddressFormBean.java,v $
 */

package com.biperf.core.ui.user;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.Address;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.AddressUtil;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ServiceLocator;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * AddressFormBean.
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
 * <td>robinsra</td>
 * <td>May 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AddressFormBean implements Serializable
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final Log log = LogFactory.getLog( AddressFormBean.class );

  private String countryCode;
  private String countryName;
  private String addr1;
  private String addr2;
  private String addr3;
  private String city;
  private String stateTypeCode;
  private String postalCode;
  private boolean requiredPostalCode;

  private static final String EMPTY_STRING = "";

  // ---------------------------------------------------------------------------
  // Validation Methods
  // ---------------------------------------------------------------------------

  /**
   * Validates the Address
   * 
   * @param actionErrors
   * @return ActionErrors
   */
  public ActionErrors validateAddress( ActionErrors actionErrors )
  {

    return validateAddress( actionErrors, EMPTY_STRING );
  } // end validate

  /**
   * Validates the Address
   * 
   * @param actionErrors
   * @return ActionErrors
   */
  public ActionErrors validateAddress( ActionErrors actionErrors, String postfix )
  {
    final String METHOD_NAME = "validateAddress";
    log.info( ">>> " + METHOD_NAME );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( countryCode == null || countryCode.equals( "" ) )
    {
      actionErrors.add( "countryCode" + postfix,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.address.COUNRTY_LABEL" ) ) );
    }

    if ( addr1 == null || addr1.equals( "" ) )
    {
      actionErrors.add( "addr1" + postfix, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.address.ADDR1_LABEL" ) ) );
    }

    if ( city == null || city.equals( "" ) )
    {
      actionErrors.add( "city" + postfix, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.address.CITY_LABEL" ) ) );
    }

    List stateList = StateType.getList( countryCode );
    if ( stateList != null && stateList.size() != 0 )
    {
      if ( stateTypeCode == null || stateTypeCode.equals( "" ) )
      {
        actionErrors.add( "stateTypeCode" + postfix,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.address.STATE_LABEL" ) ) );
      }
    }

    if ( actionErrors == null || actionErrors.size() == 0 )
    {
      Country country = this.getCountryService().getCountryByCode( this.countryCode );
      if ( country.getRequirePostalCode() )
      {
        if ( postalCode == null || postalCode.equals( "" ) )
        {
          actionErrors.add( "postalCode" + postfix,
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.address.POSTAL_CODE_LABEL" ) ) );
        } // For fix 18974 to avoid application error
        else if ( stateTypeCode != null && !"".equals( stateTypeCode ) )
        {
          // strip all allowed characters (-) from postalCode to do other checks
          String strippedPostalCode = postalCode.replaceAll( "[-]", "" );
          String omStateCode = StateType.lookup( stateTypeCode ).getAbbr();
          String omCountryCode = this.getCountryService().getCountryByCode( this.countryCode ).getAwardbanqAbbrev();

          if ( countryCode.equals( Country.UNITED_STATES ) )
          {
            if ( strippedPostalCode.length() < 5 )
            {
              actionErrors.add( "postalCode" + postfix, new ActionMessage( "participant.address.errors.US_POSTAL_CODE_INVALID_LENGTH" ) );
            }

            long longPostalCode = 0;
            try
            {
              longPostalCode = Long.parseLong( strippedPostalCode );
            }
            catch( NumberFormatException e )
            {
              longPostalCode = 0;
            }
            // To fix 18974 add new key for error message must be numeric
            if ( longPostalCode == 0 )
            {
              actionErrors.add( "postalCode" + postfix, new ActionMessage( "participant.address.errors.US_POSTAL_CODE_DATATYPE" ) );
            }
            else
            {
              try
              {
                boolean isValidZipCode = getAwardBanQService().isValidZipCode( omCountryCode, omStateCode, strippedPostalCode );
                if ( !isValidZipCode )
                {
                  actionErrors.add( "postalCode" + postfix, new ActionMessage( "participant.address.errors.US_POSTAL_CODE_INVALID" ) );
                }
              }
              catch( ServiceErrorException e1 )
              {
                List serviceErrors = e1.getServiceErrors();
                ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, actionErrors );
              }
            }
          } // if country is US
          else if ( countryCode.equals( Country.CANADA ) )
          {
            if ( strippedPostalCode.length() < 6 )
            {
              actionErrors.add( "postalCode" + postfix, new ActionMessage( "participant.address.errors.CA_POSTAL_CODE_INVALID_LENGTH" ) );
            }
            else
            {
              try
              {
                boolean isValidZipCode = getAwardBanQService().isValidZipCode( omCountryCode, omStateCode, strippedPostalCode );
                if ( !isValidZipCode )
                {
                  actionErrors.add( "postalCode" + postfix, new ActionMessage( "participant.address.errors.CA_POSTAL_CODE_INVALID" ) );
                }
              }
              catch( ServiceErrorException e1 )
              {
                List serviceErrors = e1.getServiceErrors();
                ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, actionErrors );
              }
            }

            // Remove all alphanumeric and see if anything is left.
            strippedPostalCode = strippedPostalCode.replaceAll( "[\\p{Alnum}]", "" );

            if ( strippedPostalCode.length() != 0 )
            {
              actionErrors.add( "postalCode" + postfix, new ActionMessage( "participant.address.errors.CA_POSTAL_CODE_INVALID" ) );
            }
          }
        } // if postal code not null
      }
    }

    log.info( "<<< " + METHOD_NAME );
    return actionErrors;
  } // end validate

  // ---------------------------------------------------------------------------
  // Load, Reset, and To Domain Object Methods
  // ---------------------------------------------------------------------------
  /**
   * Load the values from the input Address to the form
   * 
   * @param address
   */
  public void load( Address address )
  {
    if ( address == null )
    {
      resetForm();
    }
    else
    {
      this.countryCode = address.getCountry().getCountryCode();
      this.countryName = address.getCountry().getI18nCountryName();
      this.addr1 = address.getAddr1();
      this.addr2 = address.getAddr2();
      this.addr3 = address.getAddr3();
      this.city = address.getCity();
      StateType state = address.getStateType();
      if ( state != null )
      {
        this.stateTypeCode = address.getStateType().getCode();
      }
      this.postalCode = address.getPostalCode();

    }
  } // end load

  /**
   * resets the addtributes on the form
   */
  public void resetForm()
  {
    this.countryCode = "";
    this.countryName = "";
    this.addr1 = "";
    this.addr2 = "";
    this.addr3 = "";
    this.city = "";
    this.stateTypeCode = "";
    this.postalCode = "";
  } // resetForm

  /**
   * Builds a domain object from the form.
   * 
   * @return Address
   */
  public Address toDomainObject()
  {
    Address address = new Address();

    address.setAddr1( this.addr1 );
    address.setAddr2( this.addr2 );
    address.setAddr3( this.addr3 );

    Country country = getCountryService().getCountryByCode( this.countryCode );
    address.setCountry( country );

    address.setCity( this.city );
    address.setStateType( StateType.lookup( this.stateTypeCode ) );
    address.setPostalCode( this.postalCode );

    return address;
  } // end toDomainObject

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * @param request
   * @return List of states for the Country
   */
  public static List getStateListByCountryInRequest( HttpServletRequest request )
  {
    String countryCode = RequestUtils.getOptionalParamString( request, "addressFormBean.countryCode" );

    if ( countryCode == null || countryCode.equals( "" ) )
    {
      // try to get it out of the request attribute;
      countryCode = (String)request.getAttribute( "countryCode" );
    }

    if ( countryCode == null || countryCode.equals( "" ) )
    {
      countryCode = Country.UNITED_STATES;
    }

    return StateType.getList( countryCode );
  }

  /**
   * Get State List by Country Code
   * 
   * @param countryCode
   * @return List
   */
  public static List getStateListByCountryCode( String countryCode )
  {

    if ( countryCode == null || countryCode.equals( "" ) )
    {
      countryCode = Country.UNITED_STATES;
    }

    return StateType.getList( countryCode );
  }

  /**
   * Checks the country code to determine if it is International or not
   * 
   * @return boolean - true if Country is International - false if Country is not International
   */
  public boolean isInternational()
  {
    return AddressUtil.isInternational( this.countryCode );
  } // end is International

  /**
   * address in format:
   *      address 1
   *      address 2
   *      city, state, postal
   *      country
   * 
   * @return formattedAddress
   */

  public String getFormattedAddress()
  {
    StringBuffer addr = new StringBuffer();
    addr.append( addr1 );
    if ( addr2 != null && addr2.length() > 0 )
    {
      addr.append( "<br>" );
      addr.append( addr2 );
    }
    if ( addr3 != null && addr3.length() > 0 )
    {
      addr.append( "<br>" );
      addr.append( addr3 );
    }
    addr.append( "<br>" );
    addr.append( city );
    addr.append( "," );
    addr.append( getStateTypeDesc() );
    addr.append( " " );
    addr.append( postalCode );
    addr.append( "<br>\n" );

    addr.append( countryName );
    return addr.toString();
  }

  public String getAddr1()
  {
    return addr1;
  }

  public void setAddr1( String addr1 )
  {
    this.addr1 = addr1;
  }

  public String getAddr2()
  {
    return addr2;
  }

  public void setAddr2( String addr2 )
  {
    this.addr2 = addr2;
  }

  public String getAddr3()
  {
    return addr3;
  }

  public void setAddr3( String addr3 )
  {
    this.addr3 = addr3;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity( String city )
  {
    this.city = city;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getCountryName()
  {
    if ( this.countryCode != null && !this.countryCode.isEmpty() )
    {
      Country country = getCountryService().getCountryByCode( this.countryCode );
      if ( country != null )
      {
        this.countryName = country.getI18nCountryName();
      }
      return this.countryName;
    }
    return "";
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public String getPostalCode()
  {
    return postalCode;
  }

  public void setPostalCode( String postalCode )
  {
    this.postalCode = postalCode;
  }

  public String getStateTypeCode()
  {
    return stateTypeCode;
  }

  public void setStateTypeCode( String stateTypeCode )
  {
    this.stateTypeCode = stateTypeCode;
  }

  public boolean isRequiredPostalCode()
  {
    return requiredPostalCode;
  }

  public void setRequiredPostalCode( boolean requiredPostalCode )
  {
    this.requiredPostalCode = requiredPostalCode;
  }

  public String getStateTypeDesc()
  {
    if ( StringUtils.isNotBlank( getStateTypeCode() ) )
    {
      return StateType.lookup( getStateTypeCode() ).getName();
    }
    return "";
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the Country service.
   * 
   * @return a reference to the Country service.
   */
  private CountryService getCountryService()
  {
    return (CountryService)ServiceLocator.getService( CountryService.BEAN_NAME );
  }

  public List getStatesList()
  {
    if ( StringUtils.isEmpty( this.countryCode ) )
    {
      return null;
    }
    return getStateListByCountryCode( this.countryCode );
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );

    return factory.getAwardBanQService();
  }
}
