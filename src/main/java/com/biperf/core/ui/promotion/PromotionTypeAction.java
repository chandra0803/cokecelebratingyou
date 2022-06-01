/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionTypeAction.java,v $
 */

package com.biperf.core.ui.promotion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.constants.ActionConstants;

/**
 * PromotionTypeAction.
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
 * <td>crosenquest</td>
 * <td>Aug 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionTypeAction extends PromotionBaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( PromotionTypeAction.class );

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Entered Class-->" + this.getClass().getName() + " in display method" );
    }
    String forwardTo = ActionConstants.WIZARD_CONTINUE_FORWARD;

    PromotionWizardManager promotionWizardFormManager = getPromotionWizardManager( request );

    PromotionType promotionType = promotionWizardFormManager.getPromotionType();

    PromotionTypeForm promotionTypeForm = new PromotionTypeForm();
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "promotion Type from manager--->" + promotionType + "from form--->" + promotionTypeForm.getPromotionType() );
    }
    if ( promotionType == null )
    {
      promotionTypeForm._setScreen( 1 );
      promotionTypeForm.setMethod( "save" );

    }
    else
    {
      promotionTypeForm.load( promotionType );

    }

    // request.getSession().removeAttribute(PromotionWizardManager.SESSION_KEY);

    request.getSession().setAttribute( PromotionWizardManager.SESSION_KEY, promotionWizardFormManager );

    return mapping.findForward( forwardTo );
  }

  public ActionForward forward( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Entered Class-->" + this.getClass().getName() + " in forward method" );
    }
    if ( isCancelled( request ) )
    {
      ActionForward forward = mapping.findForward( ActionConstants.CANCEL_FORWARD );
      return forward;
    }

    PromotionWizardManager promotionWizardManager = getPromotionWizardManager( request );

    promotionWizardManager.setPromotionType( ( (PromotionTypeForm)form ).toDomain() );
    request.getSession().setAttribute( PromotionWizardManager.SESSION_KEY, promotionWizardManager );

    String promotionType = ( (PromotionTypeForm)form ).getPromotionType();
    ActionForward forward = mapping.findForward( promotionType );
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "promotion Type from form--->" + promotionType );
    }
    if ( forward == null )
    {
      throw new BeaconRuntimeException( "no struts forward mapping found: " + promotionType );
    }

    return forward;
  }

  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.WIZARD_CANCEL_FORWARD;

    PromotionWizardManager promotionWizardManager = (PromotionWizardManager)request.getSession().getAttribute( PromotionWizardManager.SESSION_KEY );

    if ( promotionWizardManager != null )
    {
      if ( promotionWizardManager.getPromotion() != null )
      {
        ActionMessages errors = new ActionMessages();

        Promotion promotion = promotionWizardManager.getPromotion();

        try
        {
          PromotionWizardUtil.deletePromotion( promotion );
        }
        catch( ServiceErrorException e )
        {
          logger.info( "save: Failed to delete promotion: id - " + promotion.getId() );
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.DELETE_FAILURE" ) );

          saveErrors( request, errors );
          forwardTo = ActionConstants.WIZARD_FAIL_FORWARD;
        }
      }
    }

    request.getSession().setAttribute( PromotionWizardManager.SESSION_KEY, null );

    return mapping.findForward( forwardTo );
  }

}
