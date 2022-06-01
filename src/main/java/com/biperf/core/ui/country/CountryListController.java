/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/country/CountryListController.java,v $
 */

package com.biperf.core.ui.country;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.country.CountrySupplier;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.util.StringUtils;

/**
 * Implements the controller for the CountryList page.
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
 * <td>sedey</td>
 * <td>June 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CountryListController extends BaseController
{
  /**
   * Tiles controller for the CountryList page
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext) Overridden from
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    List<Country> allCountryList = new ArrayList<Country>();

    String showAll = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "showAll" );

    if ( !StringUtils.isEmpty( showAll ) && showAll.equalsIgnoreCase( Boolean.TRUE.toString() ) )
    {
      allCountryList = getCountryService().getAll();
      request.setAttribute( "showAll", Boolean.TRUE.toString() );
    }
    else
    {
      allCountryList = getCountryService().getAllActive();
      request.setAttribute( "showAll", Boolean.FALSE.toString() );
    }

    List<Country> countryList = new ArrayList<Country>();

    for ( Iterator<Country> iter = allCountryList.iterator(); iter.hasNext(); )
    {
      Country country = iter.next();

      if ( country.getCountrySuppliers() != null && country.getCountrySuppliers().size() > 0 )
      {
        StringBuffer supplierName = new StringBuffer();
        for ( Iterator<CountrySupplier> supplierIter = country.getCountrySuppliers().iterator(); supplierIter.hasNext(); )
        {
          CountrySupplier supplier = supplierIter.next();
          supplierName.append( supplier.getSupplier().getSupplierName() );
          supplierName.append( " " );
        }
        country.setSuppliersName( supplierName.toString() );

      }
      countryList.add( country );
    }

    request.setAttribute( "countryList", countryList );
    request.setAttribute( "pageSize", new Integer( countryList.size() ) );
  }

  /**
   * Get the SupplierService from the beanLocator.
   * 
   * @return SupplierService
   */
  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }
}
