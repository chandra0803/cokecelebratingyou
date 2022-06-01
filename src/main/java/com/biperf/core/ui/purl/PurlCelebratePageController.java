/**
 * 
 */

package com.biperf.core.ui.purl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.homepage.HomePageController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.PageConstants;

/**
 * @author poddutur
 *
 */
public class PurlCelebratePageController extends HomePageController
{
  public static final String UPCOMING = "upcoming";

  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    super.onExecute( context, request, response, servletContext );

    String purlPastPresentSelect = request.getParameter( "purlPastPresentSelect" );
    if ( purlPastPresentSelect == null )
    {
      purlPastPresentSelect = UPCOMING;
    }
    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( "purlPastPresentSelect", purlPastPresentSelect );
    String ajaxUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.PURL_CELEBRATION_PAGE_URL, clientStateParameterMap );
    request.setAttribute( "ajaxUrl", ajaxUrl );
    request.setAttribute( "purlPastPresentSelect", purlPastPresentSelect );
    request.setAttribute( "isNewSAEnabled", NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() );

  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}
