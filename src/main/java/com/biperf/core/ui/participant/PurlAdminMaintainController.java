
package com.biperf.core.ui.participant;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;

public class PurlAdminMaintainController extends BaseController
{
  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Long purlRecipientId = null;

    PurlAdminMaintainForm purlAdminMaintainForm = (PurlAdminMaintainForm)request.getAttribute( PurlAdminMaintainForm.FORM_NAME );

    if ( purlAdminMaintainForm.getPurlRecipientId() != null )
    {
      purlRecipientId = new Long( purlAdminMaintainForm.getPurlRecipientId() );
    }
    else
    {
      purlRecipientId = new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "purlRecipientId" ) );
    }

    if ( purlRecipientId != null )
    {
      PurlRecipient purlRecipient = getPurlService().getPurlRecipientById( purlRecipientId );

      List<PurlContributorComment> comments = getPurlService().getComments( purlRecipientId );

      request.setAttribute( "purlRecipient", purlRecipient );
      request.setAttribute( "comments", comments );
      request.setAttribute( "showUpdate", false );
      if ( comments != null && comments.size() > 0 )
      {
        request.setAttribute( "showUpdate", true );
      }
    }
  }

  private PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }
}
