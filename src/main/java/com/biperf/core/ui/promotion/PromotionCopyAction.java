/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionCopyAction.java,v $
 */

package com.biperf.core.ui.promotion;

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

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
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
 * PromotionCopyAction.
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
 * <td>asondgeroth</td>
 * <td>Jul 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionCopyAction extends BaseDispatchAction
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

    PromotionCopyForm promoCopyForm = (PromotionCopyForm)form;
    Promotion promotion = null;
    String promotionId = promoCopyForm.getPromotionId();
    if ( promotionId != null && promotionId.length() > 0 )
    {
      Long promoId = new Long( promotionId );
      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CHILD_PROMOTIONS ) );
      promotion = getPromotionService().getPromotionByIdWithAssociations( promoId, promoAssociationRequestCollection );
      promoCopyForm.load( promotion );
    }
    request.setAttribute( "promotion", promotion );
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
    PromotionCopyForm promotionCopyForm = (PromotionCopyForm)form;
    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_COPY ); // EARLY EXIT
    }

    String forward = ActionConstants.SUCCESS_COPY;

    String newPromotionName = null;

    // Get the copy-from promotion id
    Long promotionId = new Long( promotionCopyForm.getPromotionId() );

    // Get the new promotion name based on which type of copy it is
    if ( promotionCopyForm.getEntireFamilySelected() != null && promotionCopyForm.getEntireFamilySelected().equals( "true" ) )
    {
      newPromotionName = promotionCopyForm.getNewParentFamilyPromotionName();

    }
    else if ( promotionCopyForm.getEntireFamilySelected() != null && promotionCopyForm.getEntireFamilySelected().equals( "false" ) )
    {
      newPromotionName = promotionCopyForm.getNewParentPromotionName();

    }
    else if ( promotionCopyForm.getEntireFamilySelected() == null || promotionCopyForm.getEntireFamilySelected().equals( "" ) )
    {
      newPromotionName = promotionCopyForm.getNewPromotionName();

    } // END get the new promotion name

    try
    {
      // *** perform copy of itself only *************************
      if ( promotionCopyForm.getEntireFamilySelected() == null || promotionCopyForm.getEntireFamilySelected().equals( "" )
          || promotionCopyForm.getEntireFamilySelected() != null && promotionCopyForm.getEntireFamilySelected().equals( "false" ) )
      {
        getPromotionService().copyPromotion( promotionId, newPromotionName, null );
      }
      // *** perform copy with children *************
      else if ( promotionCopyForm.getEntireFamilySelected() != null && promotionCopyForm.getEntireFamilySelected().equals( "true" ) )
      {
        getPromotionService().copyPromotion( promotionId, newPromotionName, promotionCopyForm.getPromotionCopies() );
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
      String promotionIdString = (String)clientStateMap.get( "promotionId" );
      promotionId = new Long( promotionIdString );
    }
    catch( InvalidClientStateException e )
    {
      // do nothing since this is an option parameter
    }

    ActionForward forward = null;

    if ( promotionId != null && promotionId.longValue() > 0 )
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "promotionId", promotionId );
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

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
