/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionPayoutAction.java,v $ */

package com.biperf.core.ui.promotion;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.dao.promotion.hibernate.StackRankQueryConstraint;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.exception.NonUniqueDataServiceErrorException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionBudgetMasterUpdateAssociation;
import com.biperf.core.service.promotion.PromotionPayoutUpdateAssociation;
import com.biperf.core.service.promotion.StackRankService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.product.ProductCategorySearchAction;
import com.biperf.core.ui.product.ProductSearchAction;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.BudgetSegmentValueBean;

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
 * <td>sedey</td>
 * <td>June 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionPayoutAction extends PromotionBaseDispatchAction
{
  /**
   * Log
   */
  private static final Log logger = LogFactory.getLog( PromotionPayoutAction.class );

  public static final String SESSION_PROMO_PAYOUT_FORM = "sessionPromoPayoutForm";
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

    PromotionPayoutForm promoPayoutForm = (PromotionPayoutForm)form;

    ProductClaimPromotion promotion;

    // WIZARD MODE
    if ( ViewAttributeNames.WIZARD_MODE.equals( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ) ) )
    {
      PromotionWizardManager promotionWizardManager = (PromotionWizardManager)request.getSession().getAttribute( PromotionWizardManager.SESSION_KEY );

      promotion = (ProductClaimPromotion)getWizardPromotion( request );

      if ( promotion != null )
      {
        // If the promotion has been saved and the sure is coming back, then we need to
        // initialize the lazy payout fields
        // TODO review to see if this should only be done for child promos.
        ProductClaimPromotion attachedPromotion = (ProductClaimPromotion)getPromotion( promotion.getId() );

        updateWizardPromotion( promotion, attachedPromotion );
        /*
         * promotion.setPromotionPayoutGroups( attachedPromotion.getPromotionPayoutGroups() ); if (
         * promotion.getParentPromotion() != null ) { promotion.setParentPromotion(
         * attachedPromotion.getParentPromotion() ); }
         */
        promoPayoutForm.loadPromotion( promotion );
      }

      request.getSession().setAttribute( PromotionWizardManager.SESSION_KEY, promotionWizardManager );
    }
    // NORMAL MODE
    else
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
        promotionId = (Long)clientStateMap.get( "id" );
        if ( promotionId == null )
        {
          ActionMessages errors = new ActionMessages();
          errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "id as part of clientState" ) );
          saveErrors( request, errors );
          return mapping.findForward( forwardTo );
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
      promotion = (ProductClaimPromotion)getPromotion( promotionId );
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
  public ActionForward payoutTypeChange( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionPayoutForm promoPayoutForm = (PromotionPayoutForm)form;

    // Save the state of the promotion payout form.
    ProductClaimPromotion promotion = (ProductClaimPromotion)getPromotion( promoPayoutForm.getPromotionId() );
    String payoutType = "";
    if ( promotion.getPayoutType() != null )
    {
      payoutType = promotion.getPayoutType().getCode();
    }

    List promoPayoutGroupValueList = new ArrayList();
    for ( Iterator payoutGroupIter = promoPayoutForm.getPromoPayoutGroupValueList().iterator(); payoutGroupIter.hasNext(); )
    {
      PromotionPayoutGroupFormBean payoutGroupFormBean = (PromotionPayoutGroupFormBean)payoutGroupIter.next();
      promoPayoutGroupValueList.add( payoutGroupFormBean.clone() );
    }

    if ( promotion.getPayoutType() != null && PromotionPayoutType.STACK_RANK.equals( promotion.getPayoutType().getCode() ) && promoPayoutForm.getPromoStackRankPayoutGroupValueList() != null )
    {
      List promoStackRankPayoutGroupValueList = new ArrayList();

      for ( Iterator payoutStackRankGroupIter = promoPayoutForm.getPromoStackRankPayoutGroupValueList().iterator(); payoutStackRankGroupIter.hasNext(); )
      {
        PromotionStackRankPayoutGroupFormBean payoutStackRankGroupFormBean = (PromotionStackRankPayoutGroupFormBean)payoutStackRankGroupIter.next();
        promoStackRankPayoutGroupValueList.add( payoutStackRankGroupFormBean.clone() );
      }
    }

    try
    {
      promoPayoutForm.resetPromoPayoutGroupValueList( (ProductClaimPromotion)getPromotion( promoPayoutForm.getPromotionId() ) );

      // Determine whether removing promotion payouts from the parent promotion causes a child
      // promotion to have an empty promotion payout group.
      Promotion detachedPromotion = promoPayoutForm.toDomainObject();
      PromotionPayoutUpdateAssociation updateAssocationRequest = new PromotionPayoutUpdateAssociation( detachedPromotion );
      getPromotionService().validatePromotion( detachedPromotion.getId(), updateAssocationRequest );
    }
    catch( ServiceErrorExceptionWithRollback e )
    {
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );

      // Restore the state of the promotion payout form.
      if ( !payoutType.equals( "" ) )
      {
        promoPayoutForm.setPayoutType( payoutType );
        promoPayoutForm.setPromoGroupPayoutValueList( promoPayoutGroupValueList );
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
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward prepareCategoryLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    PromotionPayoutForm promoPayoutForm = (PromotionPayoutForm)form;

    String returnUrl = RequestUtils.getRequiredParamString( request, "returnUrl" );

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_PROMO_PAYOUT_FORM, promoPayoutForm );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + "/product/productCategorySearch.do?" + "method=displaySearch&" + ProductCategorySearchAction.RETURN_ACTION_URL_PARAM + returnUrl );

    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnCategoryLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionPayoutForm promoPayoutForm = (PromotionPayoutForm)form;

    // Get the form back out of the Session to redisplay.
    PromotionPayoutForm sessionPromoPayoutForm = (PromotionPayoutForm)request.getSession().getAttribute( SESSION_PROMO_PAYOUT_FORM );

    if ( sessionPromoPayoutForm != null )
    {
      try
      {
        BeanUtils.copyProperties( promoPayoutForm, sessionPromoPayoutForm );

        // It seems as though copyProperties doesn't do a deep copy or the promoPayoutValueList is
        // getting reset.
        // So, we have to copy the payout list data manually
        promoPayoutForm.setPromoGroupPayoutValueList( sessionPromoPayoutForm.getPromoPayoutGroupValueList() );
      }
      catch( Exception e )
      {
        logger.info( "returnCategoryLookup: Copy Properties failed." );
      }
    }

    // get the searched category object from the session.
    ProductCategory productCategory = (ProductCategory)request.getSession().getAttribute( ProductCategorySearchAction.SESSION_RETURN_CATEGORY );

    // if a category was selected, add it to the forms list of payouts.
    if ( productCategory != null )
    {
      promoPayoutForm.addCategory( productCategory );
    }

    // clean up the session
    request.getSession().removeAttribute( SESSION_PROMO_PAYOUT_FORM );
    request.getSession().removeAttribute( ProductCategorySearchAction.SESSION_RETURN_CATEGORY );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward prepareProductLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    PromotionPayoutForm promoPayoutForm = (PromotionPayoutForm)form;

    String returnUrl = RequestUtils.getRequiredParamString( request, "returnUrl" );

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_PROMO_PAYOUT_FORM, promoPayoutForm );

    // TODO Fix so this isn't hardcoded
    response.sendRedirect( RequestUtils.getBaseURI( request ) + "/product/productSearch.do?" + "method=displaySearch&" + ProductSearchAction.RETURN_ACTION_URL_PARAM + returnUrl );

    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnProductLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionPayoutForm promoPayoutForm = (PromotionPayoutForm)form;

    // Get the form back out of the Session to redisplay.
    PromotionPayoutForm sessionPromoPayoutForm = (PromotionPayoutForm)request.getSession().getAttribute( SESSION_PROMO_PAYOUT_FORM );

    if ( sessionPromoPayoutForm != null )
    {
      try
      {
        BeanUtils.copyProperties( promoPayoutForm, sessionPromoPayoutForm );

        // It seems as though copyProperties doesn't do a deep copy or the promoPayoutValueList is
        // getting reset.
        // So, we have to copy the payout list data manually
        promoPayoutForm.setPromoGroupPayoutValueList( sessionPromoPayoutForm.getPromoPayoutGroupValueList() );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }

    // get the list of selected product from the session.
    List productList = (ArrayList)request.getSession().getAttribute( ProductSearchAction.SESSION_RETURN_PRODUCT_LIST );

    // add the list of product selected to the forms payoutList
    promoPayoutForm.addProducts( productList );

    // clean up the session
    request.getSession().removeAttribute( SESSION_PROMO_PAYOUT_FORM );
    request.getSession().removeAttribute( ProductSearchAction.SESSION_RETURN_PRODUCT_LIST );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Remove promotion payouts from the given promotion.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward removePayouts( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionPayoutForm promotionPayoutForm = (PromotionPayoutForm)form;

    // Save a copy of the parent promotion's list of promotion payout groups.
    List promoPayoutGroupValueList = new ArrayList();
    for ( Iterator payoutGroupIter = promotionPayoutForm.getPromoPayoutGroupValueList().iterator(); payoutGroupIter.hasNext(); )
    {
      PromotionPayoutGroupFormBean payoutGroupFormBean = (PromotionPayoutGroupFormBean)payoutGroupIter.next();
      promoPayoutGroupValueList.add( payoutGroupFormBean.clone() );
    }

    try
    {
      // Remove promotion payouts from the parent promotion.
      for ( Iterator payoutGroupIter = promotionPayoutForm.getPromoPayoutGroupValueList().iterator(); payoutGroupIter.hasNext(); )
      {
        PromotionPayoutGroupFormBean payoutGroupFormBean = (PromotionPayoutGroupFormBean)payoutGroupIter.next();

        for ( Iterator payoutIter = payoutGroupFormBean.getPromoPayoutValueList().iterator(); payoutIter.hasNext(); )
        {
          PromotionPayoutFormBean payoutFormBean = (PromotionPayoutFormBean)payoutIter.next();
          String removePayout = payoutFormBean.getRemovePayout();
          if ( removePayout != null && removePayout.equalsIgnoreCase( "Y" ) )
          {
            payoutIter.remove();
          }
        }

        if ( payoutGroupFormBean.getPromoPayoutValueListCount() == 0 )
        {
          payoutGroupIter.remove();
        }
      }

      // Determine whether removing promotion payouts from the parent promotion causes a child
      // promotion to have an empty promotion payout group.
      Promotion detachedPromotion = promotionPayoutForm.toDomainObject();
      PromotionPayoutUpdateAssociation updateAssocationRequest = new PromotionPayoutUpdateAssociation( detachedPromotion );
      getPromotionService().validatePromotion( detachedPromotion.getId(), updateAssocationRequest );
      promotionPayoutForm.buildJavascriptArray();
    }
    catch( ServiceErrorExceptionWithRollback e )
    {
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );

      // Restore the parent promotion's list of promotion payout groups.
      promotionPayoutForm.setPromoGroupPayoutValueList( promoPayoutGroupValueList );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  /**
   * Remove promotion Stack Rank payouts from the given promotion.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward removeStackRankPayouts( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    PromotionPayoutForm promotionPayoutForm = (PromotionPayoutForm)form;

    // Save a copy of the parent promotion's list of promotion payout groups.
    List promoStackRankPayoutGroupValueList = new ArrayList();
    for ( Iterator payoutGroupIter = promotionPayoutForm.getPromoStackRankPayoutGroupValueList().iterator(); payoutGroupIter.hasNext(); )
    {
      PromotionStackRankPayoutGroupFormBean payoutGroupFormBean = (PromotionStackRankPayoutGroupFormBean)payoutGroupIter.next();
      promoStackRankPayoutGroupValueList.add( payoutGroupFormBean.clone() );
    }

    try
    {
      // Remove promotion payouts from the parent promotion.
      for ( Iterator payoutGroupIter = promotionPayoutForm.getPromoStackRankPayoutGroupValueList().iterator(); payoutGroupIter.hasNext(); )
      {
        PromotionStackRankPayoutGroupFormBean payoutGroupFormBean = (PromotionStackRankPayoutGroupFormBean)payoutGroupIter.next();

        if ( payoutGroupFormBean.getGuid().equals( promotionPayoutForm.getStackRankGroupEditId() ) )
        {
          for ( Iterator payoutIter = payoutGroupFormBean.getPromoStackRankPayoutValueList().iterator(); payoutIter.hasNext(); )
          {
            PromotionStackRankPayoutFormBean payoutFormBean = (PromotionStackRankPayoutFormBean)payoutIter.next();
            String removePayout = payoutFormBean.getRemovePayout();
            if ( removePayout != null && removePayout.equalsIgnoreCase( "Y" ) )
            {
              payoutIter.remove();
            }
          }
          if ( payoutGroupFormBean.getPromoStackRankPayoutValueListCount() == 0 )
          {
            payoutGroupIter.remove();
            /*
             * if( promotionPayoutForm.getPromoStackRankPayoutGroupValueListCount() > 1 ) {
             * payoutGroupIter.remove(); }
             */
            /*
             * else { payoutGroupIter.remove(); addPayoutForThisNode(mapping,promotionPayoutForm,
             * request, response);
             * //promotionPayoutForm.getPromoStackRankPayoutGroupValueList().add(new
             * PromotionStackRankPayoutFormBean()); }
             */
          }
        }
      }

      if ( promotionPayoutForm.getPromoStackRankPayoutGroupValueListCount() == 0 )
      {
        addPayoutForThisNode( mapping, promotionPayoutForm, request, response );
        /*
         * for( Iterator payoutGroupIter =
         * promotionPayoutForm.getPromoStackRankPayoutGroupValueList().iterator();
         * payoutGroupIter.hasNext(); ) { PromotionStackRankPayoutGroupFormBean payoutGroupFormBean
         * = (PromotionStackRankPayoutGroupFormBean) payoutGroupIter.next();
         * if(payoutGroupFormBean.getPromoStackRankPayoutValueListCount() == 0) {
         * addPayoutForThisNode(mapping,promotionPayoutForm, request, response); } }
         */

        // promotionPayoutForm.getPromoStackRankPayoutGroupValueList().add(new
        // PromotionStackRankPayoutFormBean());
      }
      // Determine whether removing promotion payouts from the parent promotion causes a child
      // promotion to have an empty promotion payout group.
      Promotion detachedPromotion = promotionPayoutForm.toDomainObject();
      PromotionPayoutUpdateAssociation updateAssocationRequest = new PromotionPayoutUpdateAssociation( detachedPromotion );
      getPromotionService().validatePromotion( detachedPromotion.getId(), updateAssocationRequest );
    }
    catch( ServiceErrorExceptionWithRollback e )
    {
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );

      // Restore the parent promotion's list of promotion payout groups.
      promotionPayoutForm.setPromoStackRankPayoutGroupValueList( promoStackRankPayoutGroupValueList );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  /**
   * updateStackRankPayoutsAndSave this will be called when user changes ranks or payouts of a live
   * promotion
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward updateStackRankPayoutsAndSave( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    PromotionPayoutForm promoPayoutForm = (PromotionPayoutForm)form;

    Long promotionId = promoPayoutForm.getPromotionId();

    // now create stack rank query constraint
    StackRankQueryConstraint queryConstraint = new StackRankQueryConstraint();
    // set the promotionId
    queryConstraint.setPromotionIdsIncluded( new Long[] { promotionId } );
    // set the state - create
    queryConstraint.setStackRankStatesIncluded( new StackRankState[] { StackRankState.lookup( StackRankState.WAITING_FOR_STACK_RANK_LISTS_TO_BE_APPROVED ) } );
    List pendingStackRankList = getStackRankService().getStackRankList( queryConstraint );
    // as this list will have atmost one or 0 no need to loop
    if ( pendingStackRankList != null && pendingStackRankList.size() > 0 )
    {
      StackRank pendingStackRank = (StackRank)pendingStackRankList.get( 0 );
      if ( pendingStackRank.getId() != null )
      {
        getStackRankService().deleteStackRank( pendingStackRank.getId() );
      }
    }

    return save( mapping, promoPayoutForm, request, response );
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

    PromotionPayoutForm promoPayoutForm = (PromotionPayoutForm)form;

    ProductClaimPromotion promotion = null;
    BudgetMaster budgetMaster = null;

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

        // forward = getCancelForward( mapping, request );
      }

      return forward;
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL ) );

    promotion = (ProductClaimPromotion)getPromotionService().getPromotionByIdWithAssociations( promoPayoutForm.getPromotionId(), associationRequestCollection );

    BudgetMaster parentsBudgetMaster = null;
    if ( promoPayoutForm.isChildsBudgetSameAsParents() )
    {
      parentsBudgetMaster = promotion.getParentPromotion().getBudgetMaster();
    }
    // Bug fix# 28741
    BudgetMaster existingBudgetMaster = null;
    if ( PromotionPayoutForm.BUDGET_EXISTING.equals( promoPayoutForm.getBudgetOption() ) )
    {
      if ( promoPayoutForm.getBudgetMasterId() != null )
      {
        existingBudgetMaster = getBudgetMasterService().getBudgetMasterById( promoPayoutForm.getBudgetMasterId(), null );
      }
    }
    // Need to save new BudgetMaster before updating Promotion
    // (otherwise a transient object exception occurs)
    if ( promoPayoutForm.isCreateNewBudgetMaster() )
    {
      try
      {
        budgetMaster = promoPayoutForm.getNewBudgetMaster();

        // segment logic
        budgetMaster.getBudgetSegments().clear();
        for ( Iterator<BudgetSegmentValueBean> iter = promoPayoutForm.getBudgetSegmentVBList().iterator(); iter.hasNext(); )
        {
          BudgetSegmentValueBean budgetSegmentVB = (BudgetSegmentValueBean)iter.next();

          // build budget segment obj
          BudgetSegment budgetSegment = new BudgetSegment();
          budgetSegment = promoPayoutForm.populateBudgetSegment( budgetSegmentVB );

          if ( budgetMaster.getBudgetType().isCentralBudgetType() )
          {
            Budget budget = new Budget();

            budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) );

            BigDecimal originalValueLocal = new BigDecimal( budgetSegmentVB.getOriginalValue() );
            budget.setOriginalValue( originalValueLocal );
            budget.setCurrentValue( originalValueLocal );
            budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

            budgetSegment.getBudgets().clear();
            budgetSegment.addBudget( budget );
          }
          budgetMaster.addBudgetSegment( budgetSegment );
        }

        budgetMaster = getBudgetMasterService().saveBudgetMaster( budgetMaster );

      }
      catch( NonUniqueDataServiceErrorException e )
      {
        // If the name is not unique, the send back the error instead of
        // continuing on
        errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.BUDGET_NAME_EXISTS" ) );
      }
      catch( ServiceErrorException se )
      {
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( se.getServiceErrors(), errors );
      }
      catch( Exception e )
      {
        e.printStackTrace();
        errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.awards.errors.GENERIC_ERROR" ) );
        saveErrors( request, errors );
        forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
        return forward;
      }
    }

    Long promotionId = promoPayoutForm.getPromotionId();
    promotion = (ProductClaimPromotion)promoPayoutForm.toDomainObject();

    if ( promoPayoutForm.isCreateNewBudgetMaster() )
    {
      promotion.setBudgetMaster( budgetMaster );
    }
    else if ( promoPayoutForm.isChildsBudgetSameAsParents() )
    {
      promotion.setBudgetMaster( parentsBudgetMaster );
    }
    // Bug fix# 28741
    if ( PromotionPayoutForm.BUDGET_EXISTING.equals( promoPayoutForm.getBudgetOption() ) )
    {
      promotion.setBudgetMaster( existingBudgetMaster );
    }
    PromotionBudgetMasterUpdateAssociation promotionBudgetMasterUpdateAssociation = new PromotionBudgetMasterUpdateAssociation( promotion );
    PromotionPayoutUpdateAssociation promoPayoutUpdateAssociation = new PromotionPayoutUpdateAssociation( promotion );

    List updateAssociations = new ArrayList();
    updateAssociations.add( promotionBudgetMasterUpdateAssociation );
    updateAssociations.add( promoPayoutUpdateAssociation );

    try
    {
      promotion = (ProductClaimPromotion)getPromotionService().savePromotion( promotionId, updateAssociations );
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

  private Promotion getPromotion( Long promotionId )
  {

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMO_PAYOUT ) );

    return getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

  }

  private void updateWizardPromotion( ProductClaimPromotion wizardPromotion, ProductClaimPromotion attachedPromotion )
  {
    wizardPromotion.setPromotionPayoutGroups( attachedPromotion.getPromotionPayoutGroups() );
    if ( wizardPromotion.getParentPromotion() != null )
    {
      wizardPromotion.setParentPromotion( attachedPromotion.getParentPromotion() );
    }
  }

  /**
   * addGroup
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addGroup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    PromotionPayoutForm promoPayoutForm = (PromotionPayoutForm)form;
    promoPayoutForm.addPromoPayoutGroup( (ProductClaimPromotion)getPromotion( promoPayoutForm.getPromotionId() ) );
    promoPayoutForm.buildJavascriptArray();
    setRequestAttributes( request, promoPayoutForm );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void setRequestAttributes( HttpServletRequest request, PromotionPayoutForm form )
  {
    List list = form.getPromoPayoutGroupValueList();
    request.setAttribute( "promoGroupList", list );
  }

  public ActionForward addPayoutForThisNode( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    PromotionPayoutForm promoPayoutForm = (PromotionPayoutForm)form;
    promoPayoutForm.addPayoutLevelForThisNode();
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * addGroup
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addPayoutForNode( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    PromotionPayoutForm promoPayoutForm = (PromotionPayoutForm)form;
    promoPayoutForm.addPromoStackRankPayoutGroup( (ProductClaimPromotion)getPromotion( promoPayoutForm.getPromotionId() ) );
    setStackRankRequestAttributes( request, promoPayoutForm );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void setStackRankRequestAttributes( HttpServletRequest request, PromotionPayoutForm form )
  {
    List list = form.getPromoStackRankPayoutGroupValueList();
    request.setAttribute( "promoStackRankGroupList", list );
  }

  public ActionForward generatePayout( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    ActionMessages errors = new ActionMessages();
    PromotionPayoutForm promotionPayoutForm = (PromotionPayoutForm)actionForm;
    Promotion promotion = null;

    Long promotionId = promotionPayoutForm.getPromotionId();

    // WIZARD MODE
    if ( request.getSession().getAttribute( ViewAttributeNames.PAGE_MODE ).equals( ViewAttributeNames.WIZARD_MODE ) )
    {
      PromotionWizardManager promotionWizardManager = (PromotionWizardManager)request.getSession().getAttribute( PromotionWizardManager.SESSION_KEY );

      promotion = promotionWizardManager.getPromotion();
      if ( promotion != null )
      {
        promotionPayoutForm.loadPromotion( promotion );
      }
    }
    else
    {
      // NORMAL MODE
      if ( promotionId != null )
      {
        promotion = (ProductClaimPromotion)getPromotion( promotionId );
        promotionPayoutForm.loadPromotion( promotion );
      }
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
    }
    return mapping.findForward( forwardTo );
  }

  public ActionForward addAnotherSegment( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( "success_add_another" );
    ActionMessages errors = new ActionMessages();
    PromotionPayoutForm promotionPayoutForm = (PromotionPayoutForm)actionForm;

    promotionPayoutForm.addEmptyBudgetSegment();

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( "failure_add_another" );
    }

    return forward;
  }

  public ActionForward removeBudgetSegment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    PromotionPayoutForm promotionPayoutForm = (PromotionPayoutForm)form;
    promotionPayoutForm.getBudgetSegmentVBList().remove( promotionPayoutForm.getBudgetSegmentVBListSize() - 1 );
    return forward;
  }

  private Budget getCentralBudget( BudgetSegment budgetSegment )
  {
    Budget budget = null;

    if ( isNewBudgetSegment( budgetSegment ) )
    {
      budget = new Budget();
    }
    else
    {
      Set budgets = budgetSegment.getBudgets();
      if ( budgets != null )
      {
        Iterator iter = budgets.iterator();
        if ( iter.hasNext() )
        {
          budget = (Budget)iter.next();
        }
      }
    }
    return budget;
  }

  private boolean isNewBudgetSegment( BudgetSegment budgetSegment )
  {
    return budgetSegment.getId() == null || budgetSegment.getId().longValue() == 0;
  }

  /**
   * Gets a StackRankService
   * 
   * @return StackRankService
   */
  private StackRankService getStackRankService()
  {
    return (StackRankService)getService( StackRankService.BEAN_NAME );
  }

  /**
   * Retrieves a Budget Master Service
   * 
   * @return BudgetMasterService
   */
  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)BeanLocator.getBean( BudgetMasterService.BEAN_NAME );
  }

}
