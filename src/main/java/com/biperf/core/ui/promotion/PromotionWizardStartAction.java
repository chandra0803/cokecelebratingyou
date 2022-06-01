/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionWizardStartAction.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * PromotionWizardStartAction.
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
 * <td>Sathish</td>
 * <td>Aug 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionWizardStartAction extends PromotionBaseDispatchAction
{
  /**
   * Method which is dispatched to when there is no value for specified request parameter included
   * in the request. Subclasses of <code>DispatchAction</code> should override this method if they
   * wish to provide default behavior different than throwing a ServletException. Overridden from
   * 
   * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
   *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  protected ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forwardTo = ActionConstants.WIZARD_SUCCESS_FORWARD;

    // Clear wizard and workflow specific attributes before starting the wizard
    request.getSession().removeAttribute( PromotionWizardManager.SESSION_KEY );
    request.getSession().removeAttribute( PromotionWizardManager.WORKFLOW_CONTAINER );

    PromotionWizardManager promotionWizardFormManager = getPromotionWizardManager( request );
    promotionWizardFormManager.setPromotion( null );

    request.getSession().setAttribute( ViewAttributeNames.PAGE_MODE, ViewAttributeNames.WIZARD_MODE );
    return mapping.findForward( forwardTo );
  }

  /**
   * Used to start the promotion wizard when creating a child promotion
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward createChild( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    // Lookup the parent promotion
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
      try
      {
        promotionId = new Long( (String)clientStateMap.get( "promotionId" ) );
      }
      catch( ClassCastException cce )
      {
        promotionId = (Long)clientStateMap.get( "promotionId" );
      }
      if ( promotionId == null )
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "promotionId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // Create the child promotion (with cloned data for Basics, Form Rules, Approvals, &
    // Notifications)
    Promotion childPromotion = getPromotionService().createChildPromotion( promotionId );

    // Get the wizard ready
    // Clear wizard and workflow specific attributes before starting the wizard
    request.getSession().removeAttribute( PromotionWizardManager.SESSION_KEY );
    request.getSession().removeAttribute( PromotionWizardManager.WORKFLOW_CONTAINER );

    PromotionWizardManager promotionWizardManager = getPromotionWizardManager( request );
    promotionWizardManager.setPromotionType( childPromotion.getPromotionType() );
    promotionWizardManager.setPromotion( childPromotion );
    request.getSession().setAttribute( PromotionWizardManager.SESSION_KEY, promotionWizardManager );

    request.getSession().setAttribute( ViewAttributeNames.PAGE_MODE, ViewAttributeNames.WIZARD_MODE );

    String promotionTypeCode = childPromotion.getPromotionType().getCode();
    ActionForward forward = mapping.findForward( promotionTypeCode );

    if ( forward == null )
    {
      throw new BeaconRuntimeException( "no struts forward mapping found: " + promotionTypeCode );
    }

    return forward;
  }

  /**
   * Does a Bean lookup for the PromotionService
   * 
   * @return PromotionService
   */
  protected static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
