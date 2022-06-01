/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/user/UserCharacteristicAction.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.user;

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

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.CharacteristicUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * UserCharacteristicAction.
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
 * <td>sedey</td>
 * <td>Apr 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserCharacteristicAction extends BaseDispatchAction
{
  /**
   * Update the user characteristics with the data provided through the Form.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.EDIT_FORWARD;

    UserCharacteristicForm userCharacteristicForm = null;
    if ( form instanceof UserCharacteristicForm )
    {
      userCharacteristicForm = (UserCharacteristicForm)form;
    }
    else
    {
      userCharacteristicForm = new UserCharacteristicForm();
      request.setAttribute( "userCharacteristicForm", userCharacteristicForm );
    }

    Long userId = (Long)request.getAttribute( "userId" );
    if ( userId == null )
    {
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
        String userIdString = (String)clientStateMap.get( "userId" );
        userId = new Long( userIdString );
        if ( userId == null )
        {
          ActionMessages errors = new ActionMessages();
          errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId as part of clientState" ) );
          saveErrors( request, errors );
          return mapping.findForward( forwardTo );
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }

    userCharacteristicForm.setUserId( userId );

    return mapping.findForward( forwardTo );
  }

  /**
   * Update the user characteristics with the data provided through the Form.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();

    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    UserCharacteristicForm userCharacteristicForm = (UserCharacteristicForm)form;

    if ( isTokenValid( request, true ) )
    {
      try
      {
        List userCharacteristics = (List)CharacteristicUtils.toListOfUserCharacteristicDomainObjects( userCharacteristicForm.getUserCharacteristicValueList() );

        getUserService().updateUserCharacteristics( userCharacteristicForm.getUserId(), userCharacteristics );
        getUserService().updateBankCharacteristics( userCharacteristicForm.getUserId(), userCharacteristics );
        getAudienceService().rematchParticipantForAllCriteriaAudiences( userCharacteristicForm.getUserId() );
      }
      catch( ServiceErrorException e )
      {
        log.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      }
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
      return mapping.findForward( forwardTo );
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", userCharacteristicForm.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    queryString += "&method=display";
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString } );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return displayUpdate( actionMapping, actionForm, request, response );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward cancelled( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return displayUpdate( actionMapping, actionForm, request, response );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }
}
