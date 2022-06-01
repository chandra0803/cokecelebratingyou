
package com.biperf.core.ui.promotion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionPayoutUpdateAssociation;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.StringUtil;

public class PromotionThrowdownPayoutAction extends PromotionBaseDispatchAction
{
  public static final String SEARCH_TYPE_PARAM = "searchType";
  public static final String PAGE_TYPE_PARAM = "pageType";

  private static final Log logger = LogFactory.getLog( PromotionThrowdownPayoutAction.class );

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

    PromotionThrowdownPayoutForm promoPayoutForm = (PromotionThrowdownPayoutForm)form;

    ThrowdownPromotion promotion;

    // WIZARD MODE
    if ( ViewAttributeNames.WIZARD_MODE.equals( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ) ) )
    {
      PromotionWizardManager promotionWizardManager = (PromotionWizardManager)request.getSession().getAttribute( PromotionWizardManager.SESSION_KEY );

      promotion = (ThrowdownPromotion)getWizardPromotion( request );
      if ( promotion != null )
      {
        ThrowdownPromotion attachedPromotion = (ThrowdownPromotion)getPromotion( promotion.getId() );
        updateWizardPromotion( promotion, attachedPromotion );
        promoPayoutForm.loadPromotion( promotion );
      }

      request.getSession().setAttribute( PromotionWizardManager.SESSION_KEY, promotionWizardManager );
    }
    // NORMAL MODE
    else
    {
      Long promotionId = promoPayoutForm.getPromotionId();
      promotion = (ThrowdownPromotion)getPromotion( promotionId );
      promoPayoutForm.loadPromotion( promotion );
    }

    promoPayoutForm.setMethod( "save" );

    // get the actionForward to display the create pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Save
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionThrowdownPayoutForm promoPayoutForm = (PromotionThrowdownPayoutForm)form;

    if ( isCancelled( request ) )
    {
      if ( isWizardMode( request ) )
      {
        forward = super.cancelPromotion( request, mapping, errors );
      }
      else
      {
        if ( promoPayoutForm.getPromotionId() != null )
        {
          Map clientStateParameterMap = new HashMap();
          clientStateParameterMap.put( "promotionId", promoPayoutForm.getPromotionId() );
          String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
          String method = "method=display";
          forward = ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, method } );
        }
        else
        {
          forward = mapping.findForward( ActionConstants.WIZARD_CANCEL_FORWARD );
        }
      }

      return forward;
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NONSWEEP_PROMO_BILLCODES ) );

    ThrowdownPromotion promotion = (ThrowdownPromotion)getPromotionService().getPromotionByIdWithAssociations( promoPayoutForm.getPromotionId(), associationRequestCollection );

    Long promotionId = promoPayoutForm.getPromotionId();
    promotion = (ThrowdownPromotion)promoPayoutForm.toDomainObject();

    try
    {
      if ( StringUtils.isNotEmpty( promoPayoutForm.getBaseUnit() ) )
      {
        promotion = (ThrowdownPromotion)getPromotionService().savePayoutStrutureBaseUnitInCM( promotion, promoPayoutForm.getBaseUnit() );
      }
    }
    catch( ServiceErrorException e )
    {
      logger.error( e.getMessage(), e );
    }

    PromotionPayoutUpdateAssociation promoPayoutUpdateAssociation = new PromotionPayoutUpdateAssociation( promotion );

    List updateAssociations = new ArrayList();
    updateAssociations.add( promoPayoutUpdateAssociation );

    try
    {
      promotion = (ThrowdownPromotion)getPromotionService().savePromotion( promotionId, updateAssociations );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.UNIQUE_CONSTRAINT" ) );
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

  public ActionForward addPayoutForThisNode( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionThrowdownPayoutForm promoPayoutForm = (PromotionThrowdownPayoutForm)form;
    promoPayoutForm.addPayoutLevelForThisNode();
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward addPayoutForThisDivision( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionThrowdownPayoutForm promoPayoutForm = (PromotionThrowdownPayoutForm)form;
    promoPayoutForm.addPayoutLevelForThisDivision();
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward addPayoutForNode( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    PromotionThrowdownPayoutForm promoPayoutForm = (PromotionThrowdownPayoutForm)form;
    promoPayoutForm.addPromoStackStandingPayoutGroup();
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward addPayoutForDivision( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    PromotionThrowdownPayoutForm promoPayoutForm = (PromotionThrowdownPayoutForm)form;
    promoPayoutForm.addDivision();
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward removeStackStandingPayouts( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionThrowdownPayoutForm promotionPayoutForm = (PromotionThrowdownPayoutForm)form;

    for ( Iterator<PromotionStackStandingPayoutGroupFormBean> payoutGroupRemoveIter = promotionPayoutForm.getPromoStackStandingPayoutGroupValueList().iterator(); payoutGroupRemoveIter.hasNext(); )
    {
      int count = 0;
      PromotionStackStandingPayoutGroupFormBean payoutGroupFormBeanRemove = payoutGroupRemoveIter.next();

      for ( Iterator<PromotionStackStandingPayoutFormBean> payoutRemoveIter = payoutGroupFormBeanRemove.getPromoStackStandingPayoutValueList().iterator(); payoutRemoveIter.hasNext(); )
      {
        PromotionStackStandingPayoutFormBean payoutFormBean = payoutRemoveIter.next();
        String removePayout = payoutFormBean.getRemovePayout();
        if ( removePayout != null && removePayout.equalsIgnoreCase( "Y" ) )
        {
          count += 1;

          // Make sure that 'ALL' has atleast one payout.
          if ( StringUtil.isEmpty( payoutGroupFormBeanRemove.getNodeTypeId() ) && count == payoutGroupFormBeanRemove.getPromoStackStandingPayoutValueListCount() )
          {
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.ALL_RANKING_REQUIRED" ) );
            saveErrors( request, errors );
            forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
            return forward;
          }
        }
      }
    }
    // Save a copy of the parent promotion's list of promotion payout groups.
    List<PromotionStackStandingPayoutGroupFormBean> promoStackRankPayoutGroupValueList = new ArrayList<PromotionStackStandingPayoutGroupFormBean>();
    for ( Iterator<PromotionStackStandingPayoutGroupFormBean> payoutGroupIter = promotionPayoutForm.getPromoStackStandingPayoutGroupValueList().iterator(); payoutGroupIter.hasNext(); )
    {
      PromotionStackStandingPayoutGroupFormBean payoutGroupFormBean = payoutGroupIter.next();
      promoStackRankPayoutGroupValueList.add( (PromotionStackStandingPayoutGroupFormBean)payoutGroupFormBean.clone() );
    }

    // Remove promotion payouts from the promotion.
    for ( Iterator<PromotionStackStandingPayoutGroupFormBean> payoutGroupIter = promotionPayoutForm.getPromoStackStandingPayoutGroupValueList().iterator(); payoutGroupIter.hasNext(); )
    {
      PromotionStackStandingPayoutGroupFormBean payoutGroupFormBean = payoutGroupIter.next();

      for ( Iterator<PromotionStackStandingPayoutFormBean> payoutIter = payoutGroupFormBean.getPromoStackStandingPayoutValueList().iterator(); payoutIter.hasNext(); )
      {
        PromotionStackStandingPayoutFormBean payoutFormBean = payoutIter.next();
        String removePayout = payoutFormBean.getRemovePayout();
        if ( removePayout != null && removePayout.equalsIgnoreCase( "Y" ) )
        {
          payoutIter.remove();
        }
      }

      if ( payoutGroupFormBean.getPromoStackStandingPayoutValueListCount() == 0 )
      {
        payoutGroupIter.remove();
      }
    }
    if ( promotionPayoutForm.getPromoStackStandingPayoutGroupValueListCount() == 0 )
    {
      promotionPayoutForm.addPromoStackStandingPayoutGroup();
    }
    return forward;
  }

  public ActionForward removeDivisionPayouts( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    PromotionThrowdownPayoutForm promotionPayoutForm = (PromotionThrowdownPayoutForm)form;

    for ( Iterator<DivisionFormBean> divPayoutIter = promotionPayoutForm.getDivisionValueList().iterator(); divPayoutIter.hasNext(); )
    {
      DivisionFormBean divisionFormBean = divPayoutIter.next();
      for ( Iterator<DivisionPayoutFormBean> payoutIter = divisionFormBean.getDivisionPayoutValueList().iterator(); payoutIter.hasNext(); )
      {
        DivisionPayoutFormBean payoutFormBean = payoutIter.next();
        String removePayout = payoutFormBean.getRemovePayout();
        if ( removePayout != null && removePayout.equalsIgnoreCase( "Y" ) )
        {
          payoutIter.remove();
        }
      }

      if ( divisionFormBean.getDivisionPayoutValueListCount() == 0 )
      {
        divPayoutIter.remove();
      }
    }
    if ( promotionPayoutForm.getDivisionValueListCount() == 0 )
    {
      promotionPayoutForm.addDivision();
    }
    return forward;
  }

  public ActionForward recalculateRounds( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionThrowdownPayoutForm promoPayoutForm = (PromotionThrowdownPayoutForm)form;
    promoPayoutForm.recalculateRounds();
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  protected Promotion getPromotion( Long promotionId )
  {
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_PAYOUTS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISION_ROUNDS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISION_MATCH_OUTCOMES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NONSWEEP_PROMO_BILLCODES ) );
    return getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
  }

  private void updateWizardPromotion( ThrowdownPromotion wizardPromotion, ThrowdownPromotion attachedPromotion )
  {
    wizardPromotion.setStackStandingPayoutGroups( attachedPromotion.getStackStandingPayoutGroups() );
    wizardPromotion.setDivisions( attachedPromotion.getDivisions() );
  }

}
