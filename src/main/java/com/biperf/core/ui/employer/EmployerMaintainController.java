/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/employer/EmployerMaintainController.java,v $
 */

package com.biperf.core.ui.employer;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.country.CountryComparator;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.user.AddressFormBean;

/**
 * EmployerMaintainController.
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
 * <td>May 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class EmployerMaintainController extends BaseController
{
  private static CountryService countryService = (CountryService)getService( CountryService.BEAN_NAME );

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

    request.setAttribute( "countryList", getCountryList() );
    EmployerForm employerForm = (EmployerForm)request.getAttribute( "employerForm" );
    request.setAttribute( "countryCode", employerForm.getAddressFormBean().getCountryCode() );
    request.setAttribute( "stateList", AddressFormBean.getStateListByCountryInRequest( request ) );
    request.setAttribute( "requirePostalCode", new Boolean( requirePostalCodeByCountryCode( employerForm.getAddressFormBean().getCountryCode(), getCountryList() ) ) );
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
   * Returns a list of all countries supported by this application in alphabetical order by country
   * name.
   * 
   * @return a list of all countries supported by this application, as a <code>List</code> of
   *         {@link com.biperf.core.domain.country.Country} objects.
   */
  private List getCountryList()
  {
    List countryList = countryService.getAllActive();
    Collections.sort( countryList, new CountryComparator() );

    return countryList;
  }
}
