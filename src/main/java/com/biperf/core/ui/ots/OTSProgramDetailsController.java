
package com.biperf.core.ui.ots;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.ui.BaseController;

public class OTSProgramDetailsController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    @SuppressWarnings( "unchecked" )
    List<Characteristic> characteristics = getUserCharacteristicService().getAllCharacteristics();
    
    request.setAttribute( "characteristics", characteristics );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }
}
