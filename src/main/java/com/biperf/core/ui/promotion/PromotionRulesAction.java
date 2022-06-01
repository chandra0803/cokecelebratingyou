/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionRulesAction.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.LinkedHashSet;
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
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;

/**
 * PromotionRulesAction.
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
 * <td>robinsra</td>
 * <td>Aug 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionRulesAction extends BaseDispatchAction
{
  // private static final Log logger = LogFactory.getLog( PromotionRulesAction.class );

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    String forwardTo = "success";

    PromotionRulesForm promotionRulesForm = (PromotionRulesForm)form;
    loadData( promotionRulesForm, request, errors );
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }

    return mapping.findForward( forwardTo );

  } // end display

  public ActionForward printerFriendly( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    String forwardTo = "printerFriendly";

    PromotionRulesForm promotionRulesForm = (PromotionRulesForm)form;
    loadData( promotionRulesForm, request, errors );
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }

    return mapping.findForward( forwardTo );

  } // end printerFriendly

  public ActionForward modalFriendly( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    String forwardTo = "modalFriendly";

    PromotionRulesForm promotionRulesForm = (PromotionRulesForm)form;
    loadData( promotionRulesForm, request, errors );
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }

    return mapping.findForward( forwardTo );

  } // end

  private void loadData( PromotionRulesForm promotionRulesForm, HttpServletRequest request, ActionMessages errors )
  {
    Promotion promotion = null;
    Long promotionId = null;
    String pageName = null;
    // -------------------------
    // Get the PromotionId
    // ------------------------
    try
    {
      if ( RequestUtils.containsAttribute( request, "promotionId" ) )
      {
        promotionId = RequestUtils.getRequiredAttributeLong( request, "promotionId" );
      }
      else
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
          try
          {
            promotionId = (Long)clientStateMap.get( "promotionId" );
          }
          catch( ClassCastException cce )
          {
            String id = (String)clientStateMap.get( "promotionId" );
            if ( id != null && !id.equals( "" ) )
            {
              promotionId = new Long( id );
            }
          }

          pageName = (String)clientStateMap.get( "pageName" );

          if ( promotionId == null )
          {
            errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "id as part of clientState" ) );
            saveErrors( request, errors );

            return;
          }
        }
        catch( InvalidClientStateException e )
        {
          throw new IllegalArgumentException( "request parameter clientState was missing" );
        }
      }
    }
    catch( IllegalArgumentException e )
    {
      errors.add( "errorMessage", new ActionMessage( "required.param.missing" ) );
    }

    // -------------------
    // Find the promotion
    // -------------------
    promotion = getPromotionService().getPromotionById( promotionId );
    if ( promotion == null )
    {
      errors.add( "errorMessage", new ActionMessage( "required.param.missing" ) );
    }

    if ( errors.isEmpty() && !promotion.isWebRulesEndDateExpired() )
    {
      // populate form
      promotionRulesForm.setPromotionId( promotion.getId() );
      promotionRulesForm.setPromotionName( promotion.getName() );
      promotionRulesForm.setCmsCode( promotion.getCmAssetCode() );
      promotionRulesForm.setCmsKey( promotion.getWebRulesCmKey() );
      promotionRulesForm.setPageName( pageName );
    }
  }

  public ActionForward footerDisplay( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    ActionMessages errors = new ActionMessages();
    String forwardTo = null;

    PromotionRulesForm promotionRulesForm = (PromotionRulesForm)form;

    try
    {
      List promoList = getPromotionService().getPaxPromoRules( UserManager.getUser() );
      if ( promoList.size() == 1 )
      {
        forwardTo = "success";

        Promotion promotion = (Promotion)promoList.get( 0 );

        promotionRulesForm.setPromotionId( promotion.getId() );
        promotionRulesForm.setPromotionName( promotion.getName() );
        promotionRulesForm.setCmsCode( promotion.getCmAssetCode() );
        promotionRulesForm.setCmsKey( promotion.getWebRulesCmKey() );
      }
      else
      {
        forwardTo = "multiple";
        promotionRulesForm.setPromotionRulesList( promoList );
      }
      request.getSession().setAttribute( "sessionPromoList", new LinkedHashSet( promoList ) );
    }

    catch( ServiceErrorException e )
    {
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }

    return mapping.findForward( forwardTo );

  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

} // end class ViewPromotionRulesAction
