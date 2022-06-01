
package com.biperf.core.ui.participant;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

public class ParticipantPurlRecipientListController extends BaseController
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
    Long userId = null;
    Long promotionId = null;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      if ( clientStateMap.get( "userId" ) != null && clientStateMap.get( "userId" ).toString().length() > 0 )
      {
        try
        {
          userId = (Long)clientStateMap.get( "userId" );
        }
        catch( ClassCastException cce )
        {
          userId = new Long( (String)clientStateMap.get( "userId" ) );
        }
      }
      if ( clientStateMap.get( "promotionId" ) != null && clientStateMap.get( "promotionId" ).toString().length() > 0 )
      {
        try
        {
          promotionId = (Long)clientStateMap.get( "promotionId" );
        }
        catch( ClassCastException cce )
        {
          promotionId = new Long( (String)clientStateMap.get( "promotionId" ) );
        }
      }
      if ( userId == null || promotionId == null )
      {
        clientStateMap = (Map)request.getAttribute( "clientStateParameterMap" );
        try
        {
          userId = (Long)clientStateMap.get( "userId" );
        }
        catch( ClassCastException cce )
        {
          userId = new Long( (String)clientStateMap.get( "userId" ) );
        }
        try
        {
          promotionId = (Long)clientStateMap.get( "promotionId" );
        }
        catch( ClassCastException cce )
        {
          promotionId = new Long( (String)clientStateMap.get( "promotionId" ) );
        }
      }
      if ( promotionId != null )
      {
        request.setAttribute( "promotionId", promotionId );
      }
      if ( userId != null )
      {
        request.setAttribute( "userId", userId );
      }
      else
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId as part of clientState" ) );
      }

      if ( promotionId != null && userId != null )
      {

        List<PurlRecipient> purlRecipientList = getPurlService().getAllNonExpiredPurlRecipients( userId, promotionId );

        request.setAttribute( "purlRecipientList", purlRecipientList );
      }

    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  private PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }
}
