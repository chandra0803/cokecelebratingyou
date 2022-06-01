/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionGoalPayoutAction.java,v $ */

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

import com.biperf.core.domain.enums.PayoutStructure;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionPayoutUpdateAssociation;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
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
public class PromotionGoalPayoutAction extends PromotionBaseDispatchAction
{
  public static final String SESSION_PROMO_GOAL_PAYOUT_FORM = "sessionPromoGoalPayoutForm";
  public static final String SEARCH_TYPE_PARAM = "searchType";
  public static final String PAGE_TYPE_PARAM = "pageType";
  public static final String ALTERNATE_RETURN_URL = "alternateReturnUrl";

  private static final Log logger = LogFactory.getLog( PromotionGoalPayoutAction.class );

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

    PromotionGoalPayoutForm promoPayoutForm = (PromotionGoalPayoutForm)form;

    GoalQuestPromotion promotion;

    // WIZARD MODE
    if ( ViewAttributeNames.WIZARD_MODE.equals( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ) ) )
    {
      PromotionWizardManager promotionWizardManager = (PromotionWizardManager)request.getSession().getAttribute( PromotionWizardManager.SESSION_KEY );

      promotion = (GoalQuestPromotion)getWizardPromotion( request );

      if ( promotion != null )
      {
        // If the promotion has been saved and the sure is coming back, then we need to
        // initialize the lazy payout fields
        // TODO review to see if this should only be done for child promos.
        GoalQuestPromotion attachedPromotion = (GoalQuestPromotion)getPromotion( promotion.getId() );

        updateWizardPromotion( promotion, attachedPromotion );
        /*
         * promotion.setPromotionPayoutGroups( attachedPromotion.getPromotionPayoutGroups() ); if (
         * promotion.getParentPromotion() != null ) { promotion.setParentPromotion(
         * attachedPromotion.getParentPromotion() ); }
         */
        if ( promotion.getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) && ( (ChallengePointPromotion)promotion ).getChallengePointAwardType() != null )
        {
          promoPayoutForm.setPayoutStructure( PayoutStructure.FIXED );
        }
        promoPayoutForm.loadPromotion( promotion );
      }

      request.getSession().setAttribute( PromotionWizardManager.SESSION_KEY, promotionWizardManager );
    }
    // NORMAL MODE
    else
    {
      Long promotionId = promoPayoutForm.getPromotionId();
      promotion = (GoalQuestPromotion)getPromotion( promotionId );
      if ( promotion.getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) && ( (ChallengePointPromotion)promotion ).getChallengePointAwardType() != null )
      {
        promoPayoutForm.setPayoutStructure( PayoutStructure.FIXED );
      }
      promoPayoutForm.loadPromotion( promotion );
    }

    promoPayoutForm.setMethod( "save" );

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
  public ActionForward payoutStructureChange( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionGoalPayoutForm promoPayoutForm = (PromotionGoalPayoutForm)form;

    // Save the state of the promotion payout form.
    GoalQuestPromotion promotion = (GoalQuestPromotion)getPromotion( promoPayoutForm.getPromotionId() );
    String payoutStructure = "";
    if ( promotion.getPayoutStructure() != null )
    {
      payoutStructure = promotion.getPayoutStructure().getCode();
    }

    List<PromotionGoalPayoutLevelFormBean> goalLevelValueList = new ArrayList<PromotionGoalPayoutLevelFormBean>();
    if ( promoPayoutForm.getGoalLevelValueList() != null )
    {
      for ( Iterator<PromotionGoalPayoutLevelFormBean> goalLevelIter = promoPayoutForm.getGoalLevelValueList().iterator(); goalLevelIter.hasNext(); )
      {
        PromotionGoalPayoutLevelFormBean promotionGoalPayoutLevelFormBean = goalLevelIter.next();
        goalLevelValueList.add( promotionGoalPayoutLevelFormBean.clone() );
      }
    }

    try
    {
      promoPayoutForm.resetPromoPayoutGoalLevelList();
      Promotion detachedPromotion = promoPayoutForm.toDomainObject();
      PromotionPayoutUpdateAssociation updateAssocationRequest = new PromotionPayoutUpdateAssociation( detachedPromotion );
      getPromotionService().validatePromotion( detachedPromotion.getId(), updateAssocationRequest );
    }
    catch( ServiceErrorExceptionWithRollback e )
    {
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );

      // Restore the state of the promotion payout form.
      if ( !payoutStructure.equals( "" ) )
      {
        promoPayoutForm.setPayoutStructure( payoutStructure );
        promoPayoutForm.setGoalLevelValueList( goalLevelValueList );
      }
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  /**
   * Remove goals from the given promotion.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward removeGoals( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionGoalPayoutForm promotionGoalPayoutForm = (PromotionGoalPayoutForm)form;

    if ( promotionGoalPayoutForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
    {
      promotionGoalPayoutForm.setPayoutStructure( PayoutStructure.FIXED );
    }

    List<PromotionGoalPayoutLevelFormBean> goalLevelValueList = new ArrayList<PromotionGoalPayoutLevelFormBean>();
    if ( promotionGoalPayoutForm.getGoalLevelValueList() != null )
    {
      for ( Iterator<PromotionGoalPayoutLevelFormBean> goalLevelIter = promotionGoalPayoutForm.getGoalLevelValueList().iterator(); goalLevelIter.hasNext(); )
      {
        PromotionGoalPayoutLevelFormBean promotionGoalPayoutLevelFormBean = goalLevelIter.next();
        goalLevelValueList.add( promotionGoalPayoutLevelFormBean.clone() );
      }
    }

    try
    {
      // Remove goals from the promotion.
      int i = 1;
      for ( Iterator<PromotionGoalPayoutLevelFormBean> goalLevelIter = promotionGoalPayoutForm.getGoalLevelValueList().iterator(); goalLevelIter.hasNext(); )
      {
        PromotionGoalPayoutLevelFormBean promotionGoalPayoutLevelFormBean = goalLevelIter.next();
        String removeGoal = promotionGoalPayoutLevelFormBean.getRemoveGoal();
        if ( removeGoal != null && removeGoal.equalsIgnoreCase( "Y" ) )
        {
          goalLevelIter.remove();
        }
        else
        {
          promotionGoalPayoutLevelFormBean.setSequenceNumber( i++ );
        }
      }

      Promotion detachedPromotion = promotionGoalPayoutForm.toDomainObject();
      PromotionPayoutUpdateAssociation updateAssocationRequest = new PromotionPayoutUpdateAssociation( detachedPromotion );
      getPromotionService().validatePromotion( detachedPromotion.getId(), updateAssocationRequest );
    }
    catch( ServiceErrorExceptionWithRollback e )
    {
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );

      // Restore the parent promotion's list of promotion payout groups.
      promotionGoalPayoutForm.setGoalLevelValueList( goalLevelValueList );
    }

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

    PromotionGoalPayoutForm promotionGoalPayoutForm = (PromotionGoalPayoutForm)form;

    if ( promotionGoalPayoutForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
    {
      promotionGoalPayoutForm.setPayoutStructure( PayoutStructure.FIXED );
    }

    promotionGoalPayoutForm.addEmptyGoalLevel();

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

    PromotionGoalPayoutForm promotionGoalPayoutForm = (PromotionGoalPayoutForm)form;

    if ( promotionGoalPayoutForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
    {
      promotionGoalPayoutForm.setPayoutStructure( PayoutStructure.FIXED );
    }

    int oldSequence = 0;
    int newSequence = 0;
    try
    {
      oldSequence = Integer.parseInt( promotionGoalPayoutForm.getOldSequence() );
      newSequence = Integer.parseInt( promotionGoalPayoutForm.getnewElementSequenceNum() );
    }
    catch( NumberFormatException nfe )
    {
    }
    if ( oldSequence != 0 && newSequence != 0 && promotionGoalPayoutForm.getGoalLevelValueListSize() != 0 )
    {
      for ( Iterator<PromotionGoalPayoutLevelFormBean> iter = promotionGoalPayoutForm.getGoalLevelValueList().iterator(); iter.hasNext(); )
      {
        PromotionGoalPayoutLevelFormBean promotionGoalPayoutLevelFormBean = iter.next();
        if ( promotionGoalPayoutLevelFormBean.getSequenceNumber() == oldSequence )
        {
          promotionGoalPayoutLevelFormBean.setSequenceNumber( newSequence );
        }
        else if ( promotionGoalPayoutLevelFormBean.getSequenceNumber() == newSequence )
        {
          promotionGoalPayoutLevelFormBean.setSequenceNumber( oldSequence );
        }
      }
      promotionGoalPayoutForm.resortGoalLevels();
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

    PromotionGoalPayoutForm promoPayoutForm = (PromotionGoalPayoutForm)form;

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

    GoalQuestPromotion promotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( promoPayoutForm.getPromotionId(), associationRequestCollection );

    Long promotionId = promoPayoutForm.getPromotionId();
    promotion = (GoalQuestPromotion)promoPayoutForm.toDomainObject();

    try
    {
      if ( StringUtils.isNotEmpty( promoPayoutForm.getBaseUnit() ) )
      {
        promotion = (GoalQuestPromotion)getPromotionService().savePayoutStrutureBaseUnitInCM( promotion, promoPayoutForm.getBaseUnit() );
      }
      if ( promoPayoutForm.getGoalLevelValueList() != null )
      {
        List<AbstractGoalLevel> goalLevelsFromAdmin = new ArrayList<AbstractGoalLevel>();
        for ( Iterator<PromotionGoalPayoutLevelFormBean> goalLevelIter = promoPayoutForm.getGoalLevelValueList().iterator(); goalLevelIter.hasNext(); )
        {
          PromotionGoalPayoutLevelFormBean goalLevelFormBean = goalLevelIter.next();
          if ( StringUtils.isNotBlank( goalLevelFormBean.getName() ) && StringUtils.isNotBlank( goalLevelFormBean.getDescription() ) )
          {
            // goalLevel Obejct is containg Goal Name and Description.It does not contain any CM
            // Keys.
            // This is only for Saving Goal Name and Description in CM.In other places goalLevel
            // will behave normal.
            GoalLevel goalLevel = new GoalLevel();
            goalLevel.setId( goalLevelFormBean.getGoalLevelId() );
            goalLevel.setGoalLevelNameKey( goalLevelFormBean.getName() );
            goalLevel.setGoalLevelDescriptionKey( goalLevelFormBean.getDescription() );
            goalLevel.setGoalLevelcmAssetCode( goalLevelFormBean.getGoalLevelcmAssetCode() );
            goalLevelsFromAdmin.add( goalLevel );

          }
        }
        promotion = (GoalQuestPromotion)getPromotionService().saveGoalNameAndDescriptionInCM( promotion, goalLevelsFromAdmin );
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
      promotion = (GoalQuestPromotion)getPromotionService().savePromotion( promotionId, updateAssociations );
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
        if ( !isSaveAndExit( request ) && promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) && promotion.getPartnerAudienceType() != null )
        {
          forward = mapping.findForward( "showPartnerPayout" );
        }
        else
        {
          forward = getWizardNextPage( mapping, request, promotion );
        }
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

  protected Promotion getPromotion( Long promotionId )
  {

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    // TODO: Need to add hydrate for goallevels
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );

    return getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

  }

  protected void updateWizardPromotion( GoalQuestPromotion wizardPromotion, GoalQuestPromotion attachedPromotion )
  {
    wizardPromotion.setGoalLevels( attachedPromotion.getGoalLevels() );
    if ( wizardPromotion.getGoalLevels() == null || wizardPromotion.getGoalLevels().isEmpty() )
    {
      for ( int i = 0; i < 3; i++ )
      {
        GoalLevel goalLevel = new GoalLevel();
        goalLevel.setSequenceNumber( i + 1 );
        wizardPromotion.addGoalLevel( goalLevel );
      }
    }
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}
