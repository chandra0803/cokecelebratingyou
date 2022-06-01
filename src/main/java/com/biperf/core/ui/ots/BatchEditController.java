
package com.biperf.core.ui.ots;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.ui.BaseController;

public class BatchEditController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    OTSBillCodesForm otsBillCodesForm = (OTSBillCodesForm)request.getAttribute( "otsBillCodesForm" );
    List<Characteristic> characteristics = getUserCharacteristicService().getAllCharacteristics();
    request.setAttribute( "userCharList", characteristics );
    List<String> inBuiltBillCodes = new ArrayList<>();
    inBuiltBillCodes.add( "department" );
    inBuiltBillCodes.add( "orgUnitName" );
    inBuiltBillCodes.add( "countryCode" );
    inBuiltBillCodes.add( "userName" );
    for ( Characteristic c : characteristics )
    {
      inBuiltBillCodes.add( c.getId().toString() );
    }
    request.setAttribute( "inBuiltBillCodes", inBuiltBillCodes );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

}
