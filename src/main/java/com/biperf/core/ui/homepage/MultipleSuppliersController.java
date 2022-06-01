
package com.biperf.core.ui.homepage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.Address;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.country.CountrySupplier;
import com.biperf.core.domain.enums.SupplierStatusType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.shopping.ShoppingService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.MultipleSupplierValueBean;

public class MultipleSuppliersController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    String navselected = (String)tileContext.getAttribute( "navselected" );
    request.getSession().setAttribute( "selectedTabId", navselected );

    List multipleSupplierList = new ArrayList();

    Long userId = UserManager.getUserId();
    AssociationRequestCollection arqc = new AssociationRequestCollection();
    arqc.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
    Participant pax = getParticipantService().getParticipantByIdWithAssociations( userId, arqc );

    // Find the primary address
    Address address = pax.getPrimaryAddress().getAddress();

    if ( address != null )
    {
      Country country = address.getCountry();
      if ( country.getCountrySuppliers() != null && country.getCountrySuppliers().size() > 0 )
      {
        for ( Iterator iter = country.getCountrySuppliers().iterator(); iter.hasNext(); )
        {
          CountrySupplier countrySupplier = (CountrySupplier)iter.next();
          Supplier supplier = countrySupplier.getSupplier();
          if ( supplier.getStatus().equals( SupplierStatusType.lookup( SupplierStatusType.ACTIVE ) ) )
          {

            if ( supplier.getSupplierType().equals( ShoppingService.INTERNAL ) )
            {
              if ( supplier.getSupplierName().equalsIgnoreCase( ShoppingService.PAYROLL_EXTRACT ) )
              {
                // nop - payroll extract returns null
                continue;
              }

              MultipleSupplierValueBean valueBean = new MultipleSupplierValueBean();
              valueBean.setImageName( supplier.getImageName() );
              valueBean.setTitle( supplier.getMerchSupplierNameText() );
              valueBean.setSupplierDesc( StringUtils.EMPTY );
              valueBean.setButtonDesc( supplier.getMerchButtonText() );
              valueBean.setSupplierType( "internal" );
              valueBean.setShoppingUrl( ClientStateUtils.generateEncodedLink( request.getContextPath(), "/shopping.do?method=displayInternal", null ) );
              multipleSupplierList.add( valueBean );

            }
            else if ( supplier.getSupplierType().equals( ShoppingService.EXTERNAL ) )
            {
              MultipleSupplierValueBean valueBean = new MultipleSupplierValueBean();
              valueBean.setImageName( supplier.getImageName() );
              valueBean.setTitle( supplier.getMerchSupplierNameText() );
              valueBean.setSupplierDesc( StringUtils.EMPTY );
              valueBean.setButtonDesc( supplier.getMerchButtonText() );
              valueBean.setSupplierType( "external" );
              Map paramMap = new HashMap();
              paramMap.put( "externalSupplierId", supplier.getId() );
              valueBean.setShoppingUrl( ClientStateUtils.generateEncodedLink( request.getContextPath(), "/externalSupplier.do?method=displayExternal", paramMap ) );
              multipleSupplierList.add( valueBean );
            }
          }
        }
      }

      // display your travel award option
      if ( country.getDisplayTravelAward() )
      {
        MultipleSupplierValueBean valueBean = new MultipleSupplierValueBean();
        /*
         * valueBean.setImageName("DiyTravel.Image"); valueBean.setTitle("DIY Travel");
         * valueBean.setSupplierDesc("Travel Description"); valueBean.setButtonDesc("View Travel");
         */
        valueBean.setSupplierType( "travel" );
        valueBean.setShoppingUrl( ClientStateUtils.generateEncodedLink( request.getContextPath(), "/shopping.do?method=displayExperience&page=travel", null ) );
        multipleSupplierList.add( valueBean );
      }

    }
    request.setAttribute( "multipleSupplierList", multipleSupplierList );

  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

}
