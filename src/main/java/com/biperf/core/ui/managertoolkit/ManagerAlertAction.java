
package com.biperf.core.ui.managertoolkit;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.NodeIncludeType;
import com.biperf.core.domain.managertoolkit.AlertMessage;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.managertoolkit.AlertMessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.ContentReaderManager;

public class ManagerAlertAction extends BaseDispatchAction
{
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";
  private static final String key = "USER_FRIENDLY_SYSTEM_ERROR_MESSAGE";
  private static final String asset = "system.generalerror";

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  /**
   * The participant cancels after clicking the Decline button. Forward to termsAndConditionsView.do
   * 
   * @param actionMapping
   * @param actionForm
   * @param httpServletRequest
   * @param httpServledtResponse
   * @return ActionForward
   */
  public ActionForward cancel( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServledtResponse )
  {

    return actionMapping.findForward( "cancelRequested" );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // get form and load duration default value
    ManagerAlertForm alertForm = (ManagerAlertForm)form;
    alertForm.load();

    // get orgUnitPaxCount, belowOrgUnitPaxCount as list and create bigdecimal with 0
    List result = getParticipantService().getNodePaxCount( UserManager.getUserId() );
    BigDecimal nodePaxCount = new BigDecimal( 0 );
    BigDecimal nodeAndBelowNodePaxCount = new BigDecimal( 0 );
    if ( result != null && result.size() == 2 )
    {
      nodePaxCount = (BigDecimal)result.get( 0 );
      nodeAndBelowNodePaxCount = (BigDecimal)result.get( 1 );
    }

    // set pax count to form bean
    alertForm.setOrgUnitRecips( nodePaxCount.longValue() );
    alertForm.setOrgUnitBelowRecips( nodeAndBelowNodePaxCount.longValue() );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   */
  public ActionForward sendAlrt( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    ActionErrors errors = new ActionErrors();

    // get form
    ManagerAlertForm alertForm = (ManagerAlertForm)form;
    boolean includeChildNode = false;

    // get participant with nodes initialized
    Participant manager = getParticipant( UserManager.getUserId() );

    // check for selected option in the send alert form page
    if ( alertForm.getOrgUnitSelect().equalsIgnoreCase( NodeIncludeType.lookup( NodeIncludeType.NODE_AND_CHILDREN ).getCode() ) )
    {
      includeChildNode = true;
    }
    Participant proxyUser = null;
    Long proxyUserId = null;
    if ( UserManager.getUser().isDelegate() )
    {
      proxyUserId = UserManager.getUser().getOriginalAuthenticatedUser().getUserId();
      // proxyUser=getParticipantService().getParticipantById( proxyUserId );
    }
    // create domain object, get participant list to send alert based on nodes and submit the alert
    AlertMessage alertMessage = alertForm.toDomainObject();
    alertMessage.setSubmitter( manager );
    alertMessage.setProxyUser( proxyUser );

    try
    {
      getAlertMessageService().createManagerSendAlert( alertMessage, manager.getId(), proxyUserId, includeChildNode );
      request.getSession().setAttribute( "sendAlertsuccess", true );
      return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    }
    catch( Exception e )
    {
      log.error( "Error: " + e );
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ContentReaderManager.getText( asset, key ) ) );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
  }

  public static void moveToRequest( HttpServletRequest request )
  {
    // get it from session....
    Boolean modelAvailable = (Boolean)request.getSession().getAttribute( "sendAlertsuccess" );
    if ( modelAvailable != null )
    {
      // add it to the request object...
      request.setAttribute( "sendAlertsuccess", true );

      // remove it from session....
      request.getSession().removeAttribute( "sendAlertsuccess" );
    }
    else
    {
      // add it to the request object...
      request.setAttribute( "sendAlertsuccess", false );
    }
  }

  /**
   * @param userId
   * @return participant with Nodes Initialized
   */
  private Participant getParticipant( Long userId )
  {
    AssociationRequestCollection reqs = new AssociationRequestCollection();
    reqs.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
    return getParticipantService().getParticipantByIdWithAssociations( userId, reqs );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private AlertMessageService getAlertMessageService()
  {
    return (AlertMessageService)getService( AlertMessageService.BEAN_NAME );
  }

}
