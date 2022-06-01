/**
 * 
 */

package com.biperf.core.ui.claim;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;

/**
 * @author poddutur
 *
 */
public class ProductClaimPageController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    request.setAttribute( "allowPasswordFieldAutoComplete", getSystemVariableService().getPropertyByName( SystemVariableService.ALLOW_PASSWORD_FIELD_AUTO_COMPLETE ).getBooleanVal() );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
