/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/ShippingAddressFormBean.java,v $
 */

package com.biperf.core.ui.user;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.Address;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.om.participant.ShipToAddress;
import com.biperf.om.participant.ShipToAddressValidationException;
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
public class ShippingAddressFormBean extends AddressFormBean
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final Log log = LogFactory.getLog( ShippingAddressFormBean.class );

  private boolean businessAddress;
  private String businessName;
  private String daytimePhone;
  private String eveningPhone;
  private String geoCode = "00";
  private List geoCodeList;
  private String omStateCode = "";
  private String omCountryCode;

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

    super.validateAddress( actionErrors, postfix );

    if ( this.daytimePhone == null || daytimePhone.trim().length() <= 0 )
    {
      actionErrors.add( "daytimePhone" + postfix,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.address.DAYTIME_PHONE_LABEL" ) ) );
    }

    log.info( "<<< " + METHOD_NAME );
    return actionErrors;
  } // end validate

  /**
   * Validates and formats the evening and daytime phone number.
   * 
   * @param actionErrors
   * @return ActionErrors
   */
  public ActionErrors formatAndValidateUsPhoneNumbers( ActionErrors actionErrors )
  {
    return formatAndValidateUsPhoneNumbers( actionErrors, EMPTY_STRING );
  }

  /**
   * Validates and formats the evening and daytime phone number.
   * 
   * @param actionErrors
   * @param postfix
   * @return ActionErrors
   */
  public ActionErrors formatAndValidateUsPhoneNumbers( ActionErrors actionErrors, String postfix )
  {
    final String METHOD_NAME = "formatAndValidateUsPhoneNumbers";
    log.info( ">>> " + METHOD_NAME );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }
    if ( StringUtils.isNotBlank( daytimePhone ) )
    {
      String numericOnlyPhone = removeNonNumeric( daytimePhone );
      if ( numericOnlyPhone == null || numericOnlyPhone.length() != 10 )
      {
        actionErrors.add( "daytimePhone" + postfix,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_PHONE_FORMAT, CmsResourceBundle.getCmsBundle().getString( "participant.address.DAYTIME_PHONE_LABEL" ) ) );
      }
      else
      {
        daytimePhone = formatPhoneNumber( numericOnlyPhone );
      }
    }
    if ( StringUtils.isNotBlank( eveningPhone ) )
    {
      String numericOnlyPhone = removeNonNumeric( eveningPhone );
      if ( numericOnlyPhone == null || numericOnlyPhone.length() != 10 )
      {
        actionErrors.add( "eveningPhone" + postfix,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_PHONE_FORMAT, CmsResourceBundle.getCmsBundle().getString( "participant.address.EVENING_PHONE_LABEL" ) ) );
      }
      else
      {
        eveningPhone = formatPhoneNumber( numericOnlyPhone );
      }
    }

    log.info( "<<< " + METHOD_NAME );
    return actionErrors;
  }

  /**
   * Formats the phone number as 999-999-9999.
   * 
   * @param phoneDigits This should be a nine character string containing only digits.
   * @return The phone number in the format of 999-999-9999
   */
  private String formatPhoneNumber( String phoneDigits )
  {
    if ( phoneDigits != null && phoneDigits.length() == 10 )
    {
      StringBuffer formattedPhone = new StringBuffer();
      formattedPhone.append( phoneDigits.substring( 0, 3 ) );
      formattedPhone.append( '-' );
      formattedPhone.append( phoneDigits.substring( 3, 6 ) );
      formattedPhone.append( '-' );
      formattedPhone.append( phoneDigits.substring( 6, 10 ) );
      return formattedPhone.toString();
    }
    return null;
  }

  /* Removes all non-numeric characters from the given String */
  private String removeNonNumeric( String aString )
  {
    if ( aString != null )
    {
      StringBuffer newString = new StringBuffer();
      for ( int i = 0; i < aString.length(); i++ )
      {
        char currentChar = aString.charAt( i );
        if ( Character.isDigit( currentChar ) )
        {
          newString.append( currentChar );
        }
      }
      return newString.toString();
    }
    return null;
  }

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
      super.load( address );
      if ( address.getCountry() != null )
      {
        this.setOMCountryCode( address.getCountry().getAwardbanqAbbrev() );
      }
      if ( address.getStateType() != null )
      {
        this.setOMStateCode( address.getStateType().getAbbr() );
      }
    }

  }

  // ---------------------------------------------------------------------------
  // Load, Reset, and To Domain Object Methods
  // ---------------------------------------------------------------------------

  /**
   * resets the addtributes on the form
   */
  public void resetForm()
  {
    super.resetForm();
    this.businessAddress = false;
    this.businessName = "";
    this.daytimePhone = "";
    this.eveningPhone = "";
    this.geoCode = "";
    this.geoCodeList = null;
    this.omCountryCode = "";
    this.omStateCode = "";
  } // resetForm

  public String getFormattedAddress()
  {
    StringBuffer addr = new StringBuffer();
    addr.append( super.getFormattedAddress() );
    addr.append( "<br> Daytime Phone:" );
    addr.append( this.daytimePhone );
    if ( eveningPhone != null && eveningPhone.trim().length() > 0 )
    {
      addr.append( "<br> Evening Phone:" );
      addr.append( eveningPhone );
    }
    return addr.toString();
  }

  /**
   * Builds an omService ShipToAddress object from the form.
   * 
   * @param firstName
   * @param lastName
   * @return shipToAddress
   */
  public ShipToAddress toOMShipToAddress( String firstName, String lastName ) throws ShipToAddressValidationException
  {
    String postalCode = this.getPostalCode();
    String city = this.getCity();
    ShipToAddress omShipto = null;

    try
    {
      omShipto = new ShipToAddress( firstName,
                                    lastName,
                                    getAddr1(),
                                    getAddr2(),
                                    getAddr3(),
                                    null,
                                    null,
                                    null,
                                    null,
                                    StringUtils.isEmpty( city ) ? null : city,
                                    null,
                                    StringUtils.isEmpty( omStateCode ) ? null : omStateCode,
                                    StringUtils.isEmpty( postalCode ) ? null : postalCode,
                                    this.omCountryCode,
                                    this.daytimePhone,
                                    null,
                                    this.eveningPhone,
                                    geoCode );
    }
    catch( ShipToAddressValidationException e )
    {
      throw e;
    }
    return omShipto;
  } // end toDomainObject

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getDaytimePhone()
  {
    return daytimePhone;
  }

  public void setDaytimePhone( String daytimePhone )
  {
    this.daytimePhone = daytimePhone;
  }

  public String getEveningPhone()
  {
    return eveningPhone;
  }

  public void setEveningPhone( String eveningPhone )
  {
    this.eveningPhone = eveningPhone;
  }

  public String getGeoCode()
  {
    return geoCode;
  }

  public void setGeoCode( String geoCode )
  {
    this.geoCode = geoCode;
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  public List getStatesList()
  {
    return getStateListByCountryCode( this.getCountryCode() );
  }

  public String getBusinessName()
  {
    return businessName;
  }

  public void setBusinessName( String businessName )
  {
    this.businessName = businessName;
  }

  public boolean isBusinessAddress()
  {
    return businessAddress;
  }

  public void setBusinessAddress( boolean businessAddress )
  {
    this.businessAddress = businessAddress;
  }

  public List getGeoCodeList()
  {
    return geoCodeList;
  }

  public void setGeoCodeList( List geoCodeList )
  {
    this.geoCodeList = geoCodeList;
  }

  public String getOMCountryCode()
  {
    return omCountryCode;
  }

  public void setOMCountryCode( String omCountryCode )
  {
    this.omCountryCode = omCountryCode;
  }

  public String getOMStateCode()
  {
    return omStateCode;
  }

  public void setOMStateCode( String omStateCode )
  {
    this.omStateCode = omStateCode;
  }

}
