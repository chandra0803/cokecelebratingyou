/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/Attic/ThrowdownParticipantStatsAction.java,v $
 *
 */

package com.biperf.core.ui.participant;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.ThrowdownPlayerStatsBean;
import com.biperf.util.StringUtils;

public class ThrowdownParticipantStatsAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( ThrowdownParticipantStatsAction.class );

  /**
   * Displays Participant Statistics
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ParticipantThrowdownStatsForm participantThrowdownStatsForm = (ParticipantThrowdownStatsForm)actionForm;
    String forwardTo = ActionConstants.DETAILS_FORWARD;
    Long userId = null;

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
        userId = (Long)clientStateMap.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        userId = new Long( (String)clientStateMap.get( "userId" ) );
      }
      if ( userId == null )
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId as part of clientState" ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
      }
      participantThrowdownStatsForm.setUserId( userId );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    if ( !StringUtils.isEmpty( participantThrowdownStatsForm.getPromotionId() ) )
    {
      ThrowdownPlayerStatsBean stats = getTeamService().getPlayerStats( userId, Long.valueOf( participantThrowdownStatsForm.getPromotionId() ) );
      participantThrowdownStatsForm.setPlayerStats( stats );
    }

    return actionMapping.findForward( forwardTo );
  }

  private TeamService getTeamService()
  {
    return (TeamService)getService( TeamService.BEAN_NAME );
  }

}
