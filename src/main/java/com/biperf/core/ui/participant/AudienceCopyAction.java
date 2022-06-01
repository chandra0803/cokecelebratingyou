
package com.biperf.core.ui.participant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.participant.Audience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * AudienceCopyAction.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Balamurugan Shanmugam</td>
 * <td>Aug 6, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class AudienceCopyAction extends BaseDispatchAction
{
  /**
   * RETURN_ACTION_URL_PARAM
   */
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  /**
   * Overridden from
   * 
   * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
   *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = "display";
    AudienceCopyForm audienceCopyForm = (AudienceCopyForm)form;

    Audience audience = null;

    String audienceId = audienceCopyForm.getAudienceId();
    if ( audienceId != null && audienceId.length() > 0 )
    {
      Long audId = new Long( audienceId );

      audience = getAudienceService().getAudienceById( audId, null );

    }

    request.setAttribute( "audience", audience );

    return mapping.findForward( forwardTo );
  }

  /**
   * Copies ClaimForm - Almost like a 'save as'
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward copy( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    AudienceCopyForm audienceCopyForm = (AudienceCopyForm)form;
    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_COPY ); // EARLY EXIT
    }

    String forward = ActionConstants.SUCCESS_COPY;

    String newAudienceName = null;

    // Get the copy-from audience id
    Long audienceId = new Long( audienceCopyForm.getAudienceId() );

    newAudienceName = audienceCopyForm.getNewAudienceName();

    try
    {
      if ( newAudienceName != null )
      {
        getAudienceService().copyAudience( audienceId, newAudienceName, null );
      }
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_COPY;
      return mapping.findForward( forward );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_COPY;
      return mapping.findForward( forward );
    }

    return mapping.findForward( forward );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Long audienceId = null;
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
      String audienceIdString = (String)clientStateMap.get( "audienceId" );
      audienceId = new Long( audienceIdString );
    }
    catch( InvalidClientStateException e )
    {
      // do nothing since this is an option parameter
    }

    ActionForward forward = null;

    if ( audienceId != null && audienceId.longValue() > 0 )
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "audienceId", audienceId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      String method = "method=display";
      forward = ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, method } );
    }
    else
    {
      forward = mapping.findForward( "cancelNoId" );
    }

    return forward;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

}
