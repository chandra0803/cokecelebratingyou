/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/country/CountryController.java,v $
 */

package com.biperf.core.ui.country;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.country.CountrySupplier;
import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.enums.AddressMethodType;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.domain.enums.TimeZoneId;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.currency.CurrencyService;
import com.biperf.core.service.integration.SupplierService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.value.CountrySupplierValueBean;

/**
 * Implements the controller for the CountryEdit/Add page.
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
public class CountryController extends BaseController
{
  /**
   * Tiles controller for the CountryEdit/Add page
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
    // set the list of available supplier Statuses in the request
    request.setAttribute( "countryStatusList", CountryStatusType.getList() );
    request.setAttribute( "addressMethodList", AddressMethodType.getList() );
    request.setAttribute( "timeZoneList", TimeZoneId.getList() );
    request.setAttribute( "supplierList", getSupplierService().getAllActive() );

    CountryForm countryForm = (CountryForm)request.getAttribute( "countryForm" );
    int size = 0;

    // Bug fix 39925

    /*
     * //List displaySupplierList = new ArrayList(); List countrySuppliersList = new ArrayList(); if
     * ( countryForm != null && countryForm.getCountrySuppliersList() != null ) { size =
     * countryForm.getCountrySuppliersList().size(); countrySuppliersList =
     * countryForm.getCountrySuppliersList(); } request.setAttribute( "countrySuppliersList",
     * countrySuppliersList ); request.setAttribute( "countrySuppliersListCount", size );
     */

    List countrySuppliersList = new ArrayList();

    List<Supplier> supplierList = getSupplierService().getAllActive();
    Country savedCountry = null;
    List<CountrySupplierValueBean> supplierListForm = countryForm.getCountrySuppliersList();

    if ( countryForm.isAddCountry() )
    {
      if ( supplierList != null && supplierList.size() > 0 )
      {
        for ( Supplier supplier : supplierList )
        {
          CountrySupplierValueBean csvb = new CountrySupplierValueBean();
          csvb.setIsPrimary( Boolean.FALSE );
          csvb.setSupplierId( supplier.getId().toString() );
          csvb.setSupplierName( supplier.getSupplierName() );
          csvb.setSupplierType( supplier.getSupplierType() );

          if ( supplierListForm != null && supplierListForm.size() > 0 )
          {
            for ( Iterator iter = supplierListForm.iterator(); iter.hasNext(); )
            {
              CountrySupplierValueBean savedSupplier = (CountrySupplierValueBean)iter.next();
              if ( savedSupplier.getSelectedSupplierId() != null && !savedSupplier.getSelectedSupplierId().equals( "" ) && savedSupplier.getSelectedSupplierId().equals( supplier.getId().toString() ) )
              {
                if ( savedSupplier.getIsPrimary() != null )
                {
                  csvb.setIsPrimary( true );
                }

                csvb.setSelectedSupplierId( savedSupplier.getSelectedSupplierId() );
              }
            }
          }
          countrySuppliersList.add( csvb );
        }
      }

      countryForm.setCountrySuppliersList( countrySuppliersList );
      request.setAttribute( "countrySuppliersList", countrySuppliersList );
      request.setAttribute( "countrySuppliersListCount", countrySuppliersList.size() );

    }

    else
    {

      if ( !countryForm.isAddCountry() )
      // if ( countryForm.getCountryId() != null )
      {
        savedCountry = getCountryService().getCountryById( countryForm.getCountryId() );
      }

      boolean canRetainSuppliersSelections = checkCurrentSessionSupplierSelections( countryForm );

      if ( supplierList != null && supplierList.size() > 0 )
      {
        for ( Supplier supplier : supplierList )
        {
          CountrySupplierValueBean csvb = new CountrySupplierValueBean();
          csvb.setIsPrimary( Boolean.FALSE );
          csvb.setSupplierId( supplier.getId().toString() );
          csvb.setSupplierName( supplier.getSupplierName() );
          csvb.setSupplierType( supplier.getSupplierType() );
          if ( !countryForm.isAddCountry() )
          {
            if ( savedCountry.getCountrySuppliers() != null && savedCountry.getCountrySuppliers().size() > 0 )
            {
              for ( Iterator iter = savedCountry.getCountrySuppliers().iterator(); iter.hasNext(); )
              {
                CountrySupplier savedSupplier = (CountrySupplier)iter.next();
                if ( savedSupplier.getSupplier().getId().toString().equals( supplier.getId().toString() ) )
                {
                  if ( savedSupplier.getPrimary() )
                  {
                    csvb.setIsPrimary( true );
                  }
                  if ( canRetainSuppliersSelections )
                  {
                    csvb.setSelectedSupplierId( savedSupplier.getSupplier().getId().toString() );
                  }
                }
              }
            }
          }
          countrySuppliersList.add( csvb );
        }
      }

      // }

      countryForm.setCountrySuppliersList( countrySuppliersList );
      request.setAttribute( "countrySuppliersList", countrySuppliersList );
      request.setAttribute( "countrySuppliersListCount", countrySuppliersList.size() );

    }
    List<Currency> currencies = getCurrencyService().getAllCurrency();
    if ( currencies == null )
    {
      currencies = new ArrayList<Currency>();
    }

    currencies.sort( Comparator.comparing( Currency::getCurrencyCode ) );

    request.setAttribute( "currencies", currencies );
  }

  private boolean checkCurrentSessionSupplierSelections( CountryForm countryForm )
  {

    for ( Iterator iter = countryForm.getCountrySuppliersList().iterator(); iter.hasNext(); )
    {
      CountrySupplierValueBean countrySupplierValueBean = (CountrySupplierValueBean)iter.next();
      if ( countrySupplierValueBean.getSelectedSupplierId() != null && countrySupplierValueBean.getIsPrimary() )
      {
        return true;
      }
    }
    return false;

  }

  /**
   * Get the SupplierService from the beanLocator.
   * 
   * @return SupplierService
   */
  private SupplierService getSupplierService()
  {
    return (SupplierService)getService( SupplierService.BEAN_NAME );
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private CurrencyService getCurrencyService()
  {
    return (CurrencyService)getService( CurrencyService.BEAN_NAME );
  }

}
