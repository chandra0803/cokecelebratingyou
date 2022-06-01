
package com.biperf.core.ui.recognitionadvisor;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.RecognitionAdvisorUtil;
import com.biperf.core.utils.UserManager;

public class RecognitionAdvisorAction extends BaseDispatchAction
{
  @SuppressWarnings( "unused" )
  private static final Log logger = LogFactory.getLog( RecognitionAdvisorAction.class );

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    boolean isRaFlow = new Boolean( request.getParameter( "isRaFlow" ) );

    if ( UserManager.isUserLoggedIn() && !isRaFlow )
    {
      return details( mapping, actionForm, request, response );
    }
    else
    {
      return initEmailAccess( mapping, actionForm, request, response );
    }
  }

  public ActionForward details( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    Participant manager = getParticipantService().getParticipantById( UserManager.getUserId() );
    // RA not enabled
    if ( !RecognitionAdvisorUtil.isRAEnabled() && manager.isManager() )
    {
      return mapping.findForward( "ra_not_enabled" );
    }
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward initEmailAccess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    if ( RecognitionAdvisorUtil.isValidFlow( request, true ) )
    {
      if ( UserManager.isUserLoggedIn() )
      {
        String redirectUrl = RecognitionAdvisorUtil.buildRedirect( request );
        if ( Objects.nonNull( redirectUrl ) )
        {
          response.sendRedirect( redirectUrl );
          return null;
        }
      }
      else
      {
        RecognitionAdvisorUtil.setupLoginAttributes( request );
      }
    }
    if ( UserManager.isUserLoggedIn() )
    {
      return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    }
    else
    {
      return mapping.findForward( ActionConstants.LOGIN_FORWARD );
    }
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

}
