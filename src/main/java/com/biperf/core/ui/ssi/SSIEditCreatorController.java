
package com.biperf.core.ui.ssi;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

public class SSIEditCreatorController extends BaseController
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
    Long ssiContestID = null;

    SSIContestSearchForm ssiContestSearchForm = (SSIContestSearchForm)request.getAttribute( "ssiContestSearchForm" );

    if ( ssiContestSearchForm != null && ssiContestSearchForm.getSsiContestID() != null )
    {
      ssiContestID = ssiContestSearchForm.getSsiContestID();
    }
    else
    {
      ssiContestID = getContestId( request );
    }
    SSIContest ssiContest = getSSIContestService().getContestById( ssiContestID );
    request.setAttribute( "contestName", ssiContest.getContestNameFromCM() );
    request.setAttribute( "creatorName", getParticipantService().getLNameFNameByPaxIdWithComma( ssiContest.getContestOwnerId() ) );

    request.setAttribute( "ssiContestId", ssiContestID );

  }

  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Returns the import file ID.
   * 
   * @param request the HTTP request from which the import file ID is retrieved.
   * @return the import file ID.
   */
  private Long getContestId( HttpServletRequest request )
  {
    Long ssiContestId = null;

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
      try
      {
        String ssiContestIdString = (String)clientStateMap.get( "ssiContestId" );
        ssiContestId = new Long( ssiContestIdString );
      }
      catch( ClassCastException cce )
      {
        ssiContestId = (Long)clientStateMap.get( "ssiContestId" );
      }
    }
    catch( InvalidClientStateException e )
    {

      throw new IllegalArgumentException( "request parameter clientState was missing" );

    }

    return ssiContestId;
  }

}
