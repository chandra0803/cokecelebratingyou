/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserAddressMaintainController.java,v $
 */

package com.biperf.core.ui.user;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.country.CountryComparator;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;

/**
 * UserAddressMaintainController.
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
 * <td>May 9, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserAddressMaintainController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    UserAddressForm userAddressForm = (UserAddressForm)request.getAttribute( UserAddressForm.FORM_NAME );

    // Available Address Types
    Long userId = new Long( userAddressForm.getUserId() );
    request.setAttribute( "addressTypeList", getAddressTypeList( userId ) );

    // Countries
    request.setAttribute( "countryList", getCountryList() );

    // Get country code
    String countryCode = "";
    AddressFormBean addrFormBean = userAddressForm.getAddressFormBean();
    if ( addrFormBean != null )
    {
      countryCode = addrFormBean.getCountryCode();
    }

    // States
    request.setAttribute( "stateTypeList", AddressFormBean.getStateListByCountryCode( countryCode ) );

    // for displaying name
    if ( userAddressForm.getUserId() != null && !"".equals( userAddressForm.getUserId() ) )
    {
      request.setAttribute( "displayNameUserId", userAddressForm.getUserId() );
    }
    request.setAttribute( "requirePostalCode", new Boolean( requirePostalCodeByCountryCode( userAddressForm.getAddressFormBean().getCountryCode(), getCountryList() ) ) );
  }

  private boolean requirePostalCodeByCountryCode( String countryCode, List countryList )
  {
    Country country = null;
    for ( int i = 0; i < countryList.size(); i++ )
    {
      country = (Country)countryList.get( i );
      if ( country.getCountryCode().equals( countryCode ) )
      {
        return country.getRequirePostalCode();
      }
    }

    return false;
  }

  /**
   * Returns a list of address types available to the specified user in alphabetical order by
   * address type name.
   * 
   * @param userId identifies a user.
   * @return a list of address types, as a <code>List</code> of
   *         {@link com.biperf.core.domain.enums.AddressType} objects.
   */
  private List getAddressTypeList( Long userId )
  {
    return getUserService().getAvailableAddressTypes( userId );
  }

  /**
   * Returns a list of countries supported by this application in alphabetical order by country
   * name.
   * 
   * @return a list of countries, as a <code>List</code> of
   *         {@link com.biperf.core.domain.country.Country} objects.
   */
  private List getCountryList()
  {
    List countryList = getCountryService().getAllActive();
    Collections.sort( countryList, new CountryComparator() );

    return countryList;
  }

  /**
   * Returns a reference to the Country service.
   * 
   * @return a reference to the Country service.
   */
  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  /**
   * Returns a reference to the User service.
   * 
   * @return a reference to the User service.
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }
}
