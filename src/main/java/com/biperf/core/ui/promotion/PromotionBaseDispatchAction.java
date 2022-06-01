/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionBaseDispatchAction.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.HashMap;
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

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * PromotionBaseDispatchAction is the base class for all the Promotion action classes.
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
public class PromotionBaseDispatchAction extends BaseDispatchAction
{
  /**
   * Not using "log" since DispatchAction (and some of our subclasses - though they probably
   * shouldn't) uses it and we want our our own so we can turn on or off just logging of this class.
   */
  private static final Log wizardLogger = LogFactory.getLog( PromotionBaseDispatchAction.class );

  /**
   * constant METHOD_QUERY_STRING
   */
  protected static final String METHOD_QUERY_STRING = "method=display";

  /**
   * @param request
   * @param mapping
   * @param errors
   * @return ActionForward
   */
  protected ActionForward cancelPromotion( HttpServletRequest request, ActionMapping mapping, ActionMessages errors )
  {
    ActionForward forward = mapping.findForward( ActionConstants.WIZARD_CANCEL_FORWARD );

    Promotion promotion = getWizardPromotion( request );

    try
    {
      if ( promotion != null )
      {
        PromotionWizardUtil.deletePromotion( promotion );
      }

      request.getSession().removeAttribute( PromotionWizardManager.SESSION_KEY );
    }
    catch( ServiceErrorException see )
    {
      wizardLogger.info( "cancelPromotion: Failed to delete promotion: id - " + promotion.getId(), see );
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.DELETE_FAILURE" ) );

      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.WIZARD_FAIL_FORWARD );
    }

    return forward;
  }

  /**
   * Back Action
   * 
   * @param request
   * @param mapping
   * @param actionForm
   * @param response
   * @return ActionForward
   */
  public ActionForward back( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.WIZARD_BACK_FORWARD );
  }

  /**
   * Continue Action
   * 
   * @param request
   * @param mapping
   * @param actionForm
   * @param response
   * @return ActionForward
   */
  public ActionForward continueNoSave( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.WIZARD_SAVE_AND_CONTINUE_FORWARD );
  }

  /**
   * Get the PromotionWizardManager from the session or create a new one if it isn't present.
   * 
   * @param request
   * @return PromotionWizardManager
   */
  public static PromotionWizardManager getPromotionWizardManager( HttpServletRequest request )
  {
    PromotionWizardManager promotionWizardManager = (PromotionWizardManager)request.getSession().getAttribute( PromotionWizardManager.SESSION_KEY );

    if ( promotionWizardManager == null )
    {
      promotionWizardManager = new PromotionWizardManager();
      setPromotionWizardManager( request, promotionWizardManager );
    }

    return promotionWizardManager;
  }

  /**
   * Set the promotionWizardManager into the session.
   * 
   * @param request
   * @param promotionWizardManager
   */
  public static void setPromotionWizardManager( HttpServletRequest request, PromotionWizardManager promotionWizardManager )
  {
    request.getSession().setAttribute( PromotionWizardManager.SESSION_KEY, promotionWizardManager );
  }

  /**
   * Set the promotion onto the promotionWizardManager.
   * 
   * @param request
   * @param promotion
   */
  public static void setWizardPromotion( HttpServletRequest request, Promotion promotion )
  {
    PromotionWizardManager promotionWizardManager = getPromotionWizardManager( request );
    promotionWizardManager.setPromotion( promotion );
    setPromotionWizardManager( request, promotionWizardManager );
  }

  /**
   * Get the promotion from the promotionWizardManager
   * 
   * @param request
   * @return Promotion
   */
  public static Promotion getWizardPromotion( HttpServletRequest request )
  {
    Promotion promotion = null;

    PromotionWizardManager manager = getPromotionWizardManager( request );
    if ( manager != null )
    {
      promotion = manager.getPromotion();
    }

    return promotion;
  }

  /**
   * Prepares a save and exit forward when the user saves the promotion and exits from the wizard.
   * 
   * @param mapping
   * @param request
   * @param promotion
   * @return ActionForward
   */
  protected static ActionForward saveAndExit( ActionMapping mapping, HttpServletRequest request, Promotion promotion )
  {
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", promotion.getId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String[] requestParams = new String[] { METHOD_QUERY_STRING, queryString };

    request.getSession().removeAttribute( PromotionWizardManager.SESSION_KEY );

    return ActionUtils.forwardWithParameters( mapping, ActionConstants.WIZARD_SAVE_AND_EXIT_FORWARD, requestParams );
  }

  /**
   * Get the next page to forward to for the Promotion Wizard
   * 
   * @param mapping
   * @param request
   * @param promotion
   * @return ActionForward
   */
  protected static ActionForward getWizardNextPage( ActionMapping mapping, HttpServletRequest request, Promotion promotion )
  {
    ActionForward forward;

    if ( isSaveAndExit( request ) )
    {
      forward = saveAndExit( mapping, request, promotion );
    }
    else
    {
      if ( promotion.isDIYQuizPromotion() )
      {
        forward = mapping.findForward( ActionConstants.WIZARD_SAVE_AND_CONTINUE_DIY_QUIZ_FORWARD );
      }
      else
      {
        forward = mapping.findForward( ActionConstants.WIZARD_SAVE_AND_CONTINUE_FORWARD );
      }
    }

    return forward;
  }

  /**
   * @param mapping
   * @param request
   * @return ActionForward
   */
  protected ActionForward getCancelForward( ActionMapping mapping, HttpServletRequest request )
  {
    String promotionId = null;
    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( clientState != null && !clientState.equals( "" ) )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        promotionId = (String)clientStateMap.get( "promotionId" );
      }
    }
    catch( InvalidClientStateException e )
    {
      // do nothing since this is an option parameter
    }

    ActionForward forward;

    if ( promotionId != null )
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "promotionId", promotionId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      String method = "method=display";
      forward = ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, method } );
    }
    else
    {
      forward = mapping.findForward( ActionConstants.WIZARD_CANCEL_FORWARD );
    }

    return forward;
  }

  /**
   * checks if the specified request in the wizard mode
   * 
   * @param request
   * @return boolean true or false
   */
  protected boolean isWizardMode( HttpServletRequest request )
  {
    return request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ) != null && request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE );
  }

  /**
   * Set the promotion object in the wizard manager in the user's session
   * 
   * @param request
   * @param promotion
   */
  protected void setPromotionInWizardManager( HttpServletRequest request, Promotion promotion )
  {
    PromotionWizardManager wizardManager = getPromotionWizardManager( request );
    wizardManager.setPromotion( promotion );
  }

  /**
   * checks if the request is for Save&Exit
   * 
   * @param request
   * @return boolean
   */
  protected static boolean isSaveAndExit( HttpServletRequest request )
  {
    return RequestUtils.containsAttribute( request, ActionConstants.WIZARD_SAVE_AND_EXIT_ATTRIBUTE ) || RequestUtils.containsParam( request, ActionConstants.WIZARD_SAVE_AND_EXIT_ATTRIBUTE );
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
