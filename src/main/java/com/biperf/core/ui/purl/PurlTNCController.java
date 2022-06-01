
package com.biperf.core.ui.purl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;

public class PurlTNCController extends BaseController
{
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    PurlTNCForm purlTNCForm = (PurlTNCForm)request.getAttribute( "purlTNCForm" );

    if ( null != purlTNCForm.getPurlContributor() && null != purlTNCForm.getPurlContributor().getId() )
    {
      PurlContributor contributor = getPurlService().getPurlContributorById( purlTNCForm.getPurlContributor().getId() );
      purlTNCForm.setPurlContributor( contributor );
      request.setAttribute( "purlContributor", contributor );
    }

    String clientName = getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    request.setAttribute( "clientName", clientName );
    request.setAttribute( "languageList", LanguageType.getList() );
  }

  private PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
