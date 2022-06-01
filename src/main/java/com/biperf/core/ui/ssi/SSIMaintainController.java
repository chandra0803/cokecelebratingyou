
package com.biperf.core.ui.ssi;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.UserManager;

public class SSIMaintainController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( SSIMaintainController.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param componentContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext componentContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    final String METHOD_NAME = "execute";

    LOG.info( ">>> " + METHOD_NAME );
    if ( !UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_BI_ADMIN" ) ) )
    {
      // Logout
      response.sendRedirect( response.encodeRedirectURL( RequestUtils.getBaseURI( request ) + "/logout.do" ) );

    }
    request.setAttribute( "ssiContestStatusList", SSIContestStatus.getList() );

  }

}
