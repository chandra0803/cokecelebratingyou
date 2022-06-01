/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionManagerOverrideAction.java,v $ */

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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionManagerOverrideUpdateAssociation;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;

/**
 * Action class for Promotion Payout operations <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>meadows</td>
 * <td>December 15, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionManagerOverrideAction extends PromotionBaseDispatchAction
{
  public static final String SESSION_PROMO_MANAGER_OVERRIDE_FORM = "sessionPromoManagerOverrideForm";
  public static final String SEARCH_TYPE_PARAM = "searchType";
  public static final String PAGE_TYPE_PARAM = "pageType";

  public static final String ALTERNATE_RETURN_URL = "alternateReturnUrl";

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

    PromotionManagerOverrideForm promoOverrideForm = (PromotionManagerOverrideForm)form;

    Promotion promotion = null;

    // WIZARD MODE
    if ( ViewAttributeNames.WIZARD_MODE.equals( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ) ) )
    {
      PromotionWizardManager promotionWizardManager = (PromotionWizardManager)request.getSession().getAttribute( PromotionWizardManager.SESSION_KEY );
      promotion = getWizardPromotion( request );

      if ( promotion != null )
      {
        Promotion attachedPromotion = getPromotion( promotion.getId(), promotion.getPromotionType().getCode() );
        updateWizardPromotion( promotion, attachedPromotion );
        promoOverrideForm.loadPromotion( promotion );
      }

      request.getSession().setAttribute( PromotionWizardManager.SESSION_KEY, promotionWizardManager );
    }
    // NORMAL MODE
    else
    {
      Long promotionId = promoOverrideForm.getPromotionId();
      promotion = getPromotion( promotionId, promoOverrideForm.getPromotionTypeCode() );
      promoOverrideForm.loadPromotion( promotion );
    }

    promoOverrideForm.setMethod( "save" );

    // get the actionForward to display the create pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward overrideStructureChange( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionManagerOverrideForm promoOverrideForm = (PromotionManagerOverrideForm)form;

    // Save the state of the promotion override form.
    Promotion promotion = getPromotion( promoOverrideForm.getPromotionId(), promoOverrideForm.getPromotionTypeCode() );
    String overrideStructure = "";
    if ( promotion.isChallengePointPromotion() )
    {
      if ( ( (ChallengePointPromotion)promotion ).getOverrideStructure() != null )
      {
        overrideStructure = ( (ChallengePointPromotion)promotion ).getOverrideStructure().getCode();
      }
    }
    if ( promotion.isGoalQuestPromotion() )
    {
      if ( ( (GoalQuestPromotion)promotion ).getOverrideStructure() != null )
      {
        overrideStructure = ( (GoalQuestPromotion)promotion ).getOverrideStructure().getCode();
      }
    }

    List overrideLevelValueList = new ArrayList();
    if ( promoOverrideForm.getManagerOverrideValueAsList() != null )
    {
      for ( Iterator overrideLevelIter = promoOverrideForm.getManagerOverrideValueAsList().iterator(); overrideLevelIter.hasNext(); )
      {
        PromotionManagerOverrideLevelFormBean promotionManagerOverrideLevelFormBean = (PromotionManagerOverrideLevelFormBean)overrideLevelIter.next();
        overrideLevelValueList.add( promotionManagerOverrideLevelFormBean.clone() );
      }
    }

    // try
    // {
    promoOverrideForm.resetManagerOverrideValueList( getPromotion( promoOverrideForm.getPromotionId(), promoOverrideForm.getPromotionTypeCode() ) );
    // Promotion detachedPromotion = promoOverrideForm.toDomainObject();
    // PromotionPayoutUpdateAssociation updateAssocationRequest = new
    // PromotionPayoutUpdateAssociation( detachedPromotion );
    // getPromotionService().validatePromotion( detachedPromotion.getId(), updateAssocationRequest
    // );
    // }
    // catch( ServiceErrorExceptionWithRollback e )
    // {
    // ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
    //
    // // Restore the state of the promotion payout form.
    // if ( !overrideStructure.equals( "" ) )
    // {
    // promoOverrideForm.setOverrideStructure( overrideStructure );
    // promoOverrideForm.setManagerOverrideValueList( overrideLevelValueList );
    // }
    // }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  /**
   * Remove override levels from the given promotion.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward removeOverrideLevels( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionManagerOverrideForm promotionManagerOverrideForm = (PromotionManagerOverrideForm)form;

    // List overrideLevelValueList = new ArrayList();
    // if (promotionManagerOverrideForm.getManagerOverrideValueList() != null)
    // {
    // for ( Iterator overrideLevelIter =
    // promotionManagerOverrideForm.getManagerOverrideValueList().iterator();
    // overrideLevelIter.hasNext(); )
    // {
    // PromotionManagerOverrideLevelFormBean promotionManagerOverrideLevelFormBean =
    // (PromotionManagerOverrideLevelFormBean)overrideLevelIter.next();
    // overrideLevelValueList.add( promotionManagerOverrideLevelFormBean.clone() );
    // }
    // }

    // try
    // {
    // Remove override levels from the promotion.
    int i = 1;
    for ( Iterator overrideLevelIter = promotionManagerOverrideForm.getManagerOverrideValueAsList().iterator(); overrideLevelIter.hasNext(); )
    {
      PromotionManagerOverrideLevelFormBean promotionManagerOverrideLevelFormBean = (PromotionManagerOverrideLevelFormBean)overrideLevelIter.next();
      String removeOverrideLevel = promotionManagerOverrideLevelFormBean.getRemoveOverrideLevel();
      if ( removeOverrideLevel != null && removeOverrideLevel.equalsIgnoreCase( "Y" ) )
      {
        overrideLevelIter.remove();
      }
      else
      {
        promotionManagerOverrideLevelFormBean.setSequenceNumber( i++ );
      }
    }

    // Promotion detachedPromotion = promotionManagerOverrideForm.toDomainObject();
    // PromotionPayoutUpdateAssociation updateAssocationRequest = new
    // PromotionPayoutUpdateAssociation( detachedPromotion );
    // getPromotionService().validatePromotion( detachedPromotion.getId(), updateAssocationRequest
    // );
    // }
    // catch( ServiceErrorExceptionWithRollback e )
    // {
    // ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
    //
    // // Restore the parent promotion's list of promotion payout groups.
    // promotionManagerOverrideForm.setGoalLevelValueList( goalLevelValueList );
    // }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  /**
   * Add a new empty goal to the given promotion.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward addAnother( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionManagerOverrideForm promotionManagerOverrideForm = (PromotionManagerOverrideForm)form;

    promotionManagerOverrideForm.addEmptyOverrideLevel();

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  /**
   * Add a new empty goal to the given promotion.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward reorder( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionManagerOverrideForm promotionManagerOverrideForm = (PromotionManagerOverrideForm)form;
    int oldSequence = 0;
    int newSequence = 0;
    try
    {
      oldSequence = Integer.parseInt( promotionManagerOverrideForm.getOldSequence() );
      newSequence = Integer.parseInt( promotionManagerOverrideForm.getnewElementSequenceNum() );
    }
    catch( NumberFormatException nfe )
    {
    }
    if ( oldSequence != 0 && newSequence != 0 && promotionManagerOverrideForm.getManagerOverrideValueListSize() != 0 )
    {
      for ( Iterator iter = promotionManagerOverrideForm.getManagerOverrideValueAsList().iterator(); iter.hasNext(); )
      {
        PromotionManagerOverrideLevelFormBean promotionManagerOverrideLevelFormBean = (PromotionManagerOverrideLevelFormBean)iter.next();
        if ( promotionManagerOverrideLevelFormBean.getSequenceNumber() == oldSequence )
        {
          promotionManagerOverrideLevelFormBean.setSequenceNumber( newSequence );
        }
        else if ( promotionManagerOverrideLevelFormBean.getSequenceNumber() == newSequence )
        {
          promotionManagerOverrideLevelFormBean.setSequenceNumber( oldSequence );
        }
      }
      promotionManagerOverrideForm.resortOverrideLevels();
    }
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
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

    PromotionManagerOverrideForm promoPayoutForm = (PromotionManagerOverrideForm)form;

    if ( isCancelled( request ) )
    {
      if ( isWizardMode( request ) )
      {
        forward = super.cancelPromotion( request, mapping, errors );
      }
      else
      {
        String alternateReturnUrl = RequestUtils.getOptionalParamString( request, ALTERNATE_RETURN_URL );
        if ( !StringUtils.isBlank( alternateReturnUrl ) )
        {
          response.sendRedirect( alternateReturnUrl );
          return null;
        }
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

    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( promoPayoutForm.getPromotionId(), associationRequestCollection );

    Long promotionId = promoPayoutForm.getPromotionId();
    promotion = (Promotion)promoPayoutForm.toDomainObject();

    PromotionManagerOverrideUpdateAssociation promoOverrideUpdateAssociation = new PromotionManagerOverrideUpdateAssociation( promotion );

    List updateAssociations = new ArrayList();
    updateAssociations.add( promoOverrideUpdateAssociation );

    try
    {
      promotion = getPromotionService().savePromotion( promotionId, updateAssociations );
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
        String alternateReturnUrl = RequestUtils.getOptionalParamString( request, ALTERNATE_RETURN_URL );
        if ( !StringUtils.isBlank( alternateReturnUrl ) )
        {
          response.sendRedirect( alternateReturnUrl );
          return null;
        }
        forward = saveAndExit( mapping, request, promotion );
      }
    }

    return forward;
  }

  /**
   * Back Action Overeride
   * 
   * @param request
   * @param mapping
   * @param actionForm
   * @param response
   * @return ActionForward
   */
  public ActionForward back( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionManagerOverrideForm promoManagerOverrideForm = (PromotionManagerOverrideForm)request.getAttribute( "promotionManagerOverrideForm" );
    Promotion promotion = getPromotionService().getPromotionById( promoManagerOverrideForm.getPromotionId() );
    if ( promoManagerOverrideForm.getAwardType() != null && promoManagerOverrideForm.getAwardType().equalsIgnoreCase( PromotionAwardsType.POINTS ) && promotion.getPartnerAudienceType() != null )
    {
      return mapping.findForward( "backToPartner" );
    }
    return mapping.findForward( ActionConstants.WIZARD_BACK_FORWARD );
  }

  private Promotion getPromotion( Long promotionId, String promotionTypeCode )
  {

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    if ( promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) )
    {
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CP_LEVELS ) );
    }
    else if ( promotionTypeCode.equals( PromotionType.GOALQUEST ) )
    {
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
    }

    return getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

  }

  private void updateWizardPromotion( Promotion wizardPromotion, Promotion attachedPromotion )
  {
    if ( wizardPromotion.isGoalQuestPromotion() )
    {
      GoalQuestPromotion gqAttachedPromotion = (GoalQuestPromotion)attachedPromotion;
      ( (GoalQuestPromotion)wizardPromotion ).setGoalLevels( gqAttachedPromotion.getGoalLevels() );
      ( (GoalQuestPromotion)wizardPromotion ).setManagerOverrideGoalLevels( gqAttachedPromotion.getManagerOverrideGoalLevels() );
    }
    else if ( wizardPromotion.isChallengePointPromotion() )
    {
      ChallengePointPromotion cpAttachedPromotion = (ChallengePointPromotion)attachedPromotion;
      // For Challenge Point, Manager Override is not separate, it is clubed with regular levels.
      ( (ChallengePointPromotion)wizardPromotion ).setGoalLevels( cpAttachedPromotion.getGoalLevels() );
    }
    // if (wizardPromotion.getGoalLevels() == null || wizardPromotion.getGoalLevels().isEmpty())
    // {
    // for (int i=0; i<5; i++)
    // {
    // GoalLevel goalLevel = new GoalLevel();
    // goalLevel.setSequenceNumber( i+1 );
    // wizardPromotion.addGoalLevel( goalLevel );
    // }
    // }
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}
