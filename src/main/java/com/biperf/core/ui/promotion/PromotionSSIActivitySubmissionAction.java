
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
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.ssi.SSIPromotionClaimApprovalUpdateAssociation;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.RequestUtils;

/**
 * 
 * PromotionSSIActivitySubmissionAction.
 * 
 * @author kandhi
 * @since Oct 29, 2014
 * @version 1.0
 */
public class PromotionSSIActivitySubmissionAction extends PromotionBaseDispatchAction
{

  private static final String PROMOTION_ERRORS_UNIQUE_CONSTRAINT = "promotion.errors.UNIQUE_CONSTRAINT";

  private static final Log logger = LogFactory.getLog( PromotionSSIActivitySubmissionAction.class );

  private static String RETURN_ACTION_URL_PARAM = "returnActionUrl";
  public static String SESSION_CALIM_APPROVAL_AUDIENCE_FORM = "sessionClaimApprovalAudienceForm";

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
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    PromotionSSIActivitySubmissionForm promotionSSIActivitySubmissionForm = (PromotionSSIActivitySubmissionForm)form;

    Promotion promotion = null;
    promotionSSIActivitySubmissionForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
      promotion = getPromotionService().getPromotionById( promotionSSIActivitySubmissionForm.getPromotionId() );
    }
    else
    {
      Long promotionId = promotionSSIActivitySubmissionForm.getPromotionId();

      if ( promotionId != null )
      {
        promotion = getPromotionService().getPromotionById( promotionId );
      }
      else
      {
        throw new IllegalArgumentException( "promotionId is null" );
      }
    }
    if ( promotion != null )
    {
      promotionSSIActivitySubmissionForm.load( promotion );
    }

    return mapping.findForward( forwardTo );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.WIZARD_SAVE_AND_CONTINUE_ATTRIBUTE );
    PromotionSSIActivitySubmissionForm promotionSSIActivitySubmissionForm = (PromotionSSIActivitySubmissionForm)form;
    ActionMessages errors = new ActionMessages();
    Promotion promotion = null;

    if ( isCancelled( request ) )
    {
      if ( isWizardMode( request ) )
      {
        forward = super.cancelPromotion( request, mapping, errors );
      }
      else
      {
        forward = getCancelForward( mapping, request );
      }
      return forward;
    }

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      promotion = getWizardPromotion( request );
      promotion = getPromotionService().getPromotionById( promotionSSIActivitySubmissionForm.getPromotionId() );
    }
    else
    {
      promotion = getPromotionService().getPromotionById( promotionSSIActivitySubmissionForm.getPromotionId() );
    }
    try
    {
      SSIPromotion ssiPromotion = (SSIPromotion)promotion;
      Long promotionId = promotionSSIActivitySubmissionForm.getPromotionId();
      if ( promotionId != null && promotionId.longValue() > 0 )
      {
        promotionSSIActivitySubmissionForm.toDomainObject( ssiPromotion );
        SSIPromotionClaimApprovalUpdateAssociation ssiPromotionClaimApprovalAudienceUpdateAssociation = new SSIPromotionClaimApprovalUpdateAssociation( ssiPromotion );
        promotion = getPromotionService().savePromotion( promotion.getId(), ssiPromotionClaimApprovalAudienceUpdateAssociation );
      }
      else
      {
        // new promotion - just save it
        promotion = getPromotionService().savePromotion( promotion );
      }
    }
    catch( ConstraintViolationException e )
    {
      logger.error( e.getMessage(), e );
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( PROMOTION_ERRORS_UNIQUE_CONSTRAINT ) );
    }
    catch( UniqueConstraintViolationException e )
    {
      logger.error( e.getMessage(), e );
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( PROMOTION_ERRORS_UNIQUE_CONSTRAINT ) );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    else
    {
      if ( isWizardMode( request ) )
      {
        setPromotionInWizardManager( request, promotion );

        forward = getWizardNextPage( mapping, request, promotion );
      }
      else
      {
        forward = saveAndExit( mapping, request, promotion );
      }
    }
    return forward;
  }

}
