
package com.biperf.core.ui.help;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;

public class PrivacyPolicyAction extends BaseDispatchAction
{

  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return view( mapping, form, request, response );
  }

  public ActionForward view( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( isFullPageView( request ) )
    {
      return mapping.findForward( ActionConstants.FULL_VIEW );
    }
    else
    {
      return mapping.findForward( ActionConstants.SHEET_VIEW );
    }
  }

  private boolean isFullPageView( HttpServletRequest request )
  {
    boolean isFullPage = false;
    String fullPageView = request.getParameter( "isFullPage" );
    if ( !StringUtils.isEmpty( fullPageView ) )
    {
      isFullPage = Boolean.valueOf( fullPageView );
    }
    return isFullPage;
  }

  /* GDPR Compliance */
  public ActionForward paxView( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( ActionConstants.PAX_FULL_VIEW );
  }

}
