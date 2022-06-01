/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsRecognitionDetailsController.java,v $
 */

package com.biperf.core.ui.approvals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.calculator.CalculatorPayout;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.PickListItemSortOrderComparator;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToUserNodesAssociationRequest;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.recognition.PromotionNodeCheckBean;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * RecognitionApprovalDetailsController.
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
 * <td></td>
 * <td></td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalsRecognitionDetailsController extends BaseController
{

  /**
   * Overridden from
   * 
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  @SuppressWarnings( "unchecked" )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    AssociationRequestCollection promotionAssociationRequestCollection = new AssociationRequestCollection();
    promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );
    promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CALCULATOR_CRITERION_AND_RATINGS ) );
    promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CALCULATOR_PAYOUTS ) );

    // Reset promotion with hydrated version.
    // ----------------------------------------------------------------
    // Bug 51672 - Fetch the claim when the request attribute is null
    // ----------------------------------------------------------------
    ApprovalsRecognitionDetailsForm approvalsRecognitionDetailsForm = (ApprovalsRecognitionDetailsForm)request.getAttribute( "approvalsRecognitionDetailsForm" );

    RecognitionClaim claim = null;
    if ( request.getAttribute( "claimDetails" ) == null )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );
      claim = (RecognitionClaim)getClaimService().getClaimByIdWithAssociations( new Long( approvalsRecognitionDetailsForm.getClaimId() ), associationRequestCollection );
      request.setAttribute( "claimDetails", claim );
    }
    else
    {
      claim = (RecognitionClaim)request.getAttribute( "claimDetails" );
    }
    // ----------------------------------
    // End - bug 51672
    // ----------------------------------

    getClaimElementDomainObjects( claim );

    Promotion promotion = getPromotionService().getPromotionByIdWithCalcAsso( claim.getPromotion().getId(), promotionAssociationRequestCollection );
    claim.setPromotion( promotion );

    // Calculator values
    PromotionNodeCheckBean bean = new PromotionNodeCheckBean();
    if ( promotion.getCalculator() != null )
    {
      if ( !StringUtil.isEmpty( promotion.getCalculator().getWeightCMAssetName() ) )
      {
        promotion.getCalculator().setWeightLabel( CmsResourceBundle.getCmsBundle().getString( promotion.getCalculator().getWeightCMAssetName(), Calculator.CM_CALC_WEIGHT_NAME_KEY ) );
      }

      if ( !StringUtil.isEmpty( promotion.getCalculator().getScoreCMAssetName() ) )
      {
        promotion.getCalculator().setScoreLabel( CmsResourceBundle.getCmsBundle().getString( promotion.getCalculator().getScoreCMAssetName(), Calculator.CM_CALC_SCORE_NAME_KEY ) );
      }
    }
    bean.setCalculatorData( promotion.getCalculator() );
    request.setAttribute( "bean", bean );

    List<ApprovalStatusType> approvalStatusTypes = ApprovalStatusType.getList( PromotionType.RECOGNITION, new PickListItemSortOrderComparator() );
    request.setAttribute( "approvalStatusTypes", approvalStatusTypes );
    request.setAttribute( "approvalStatusWhenCurrentlyDeniedList", ApprovalStatusType.getListWhenCurrentlyDenied() );

    AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();
    nodeAssociationRequestCollection.add( new NodeToUserNodesAssociationRequest() );
    Node submitterNode = getNodeService().getNodeWithAssociationsById( claim.getSubmittersNode().getId(), nodeAssociationRequestCollection );
    AssociationRequestCollection paxAssociationRequestCollection = new AssociationRequestCollection();
    paxAssociationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    paxAssociationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
    Set<Participant> submitterNodeOwners = new HashSet<Participant>();
    for ( Object giverManager : submitterNode.getNodeManagersForUser( claim.getSubmitter() ) )
    {
      submitterNodeOwners.add( getParticipantService().getParticipantByIdWithAssociations( ( (User)giverManager ).getId(), paxAssociationRequestCollection ) );
    }
    request.setAttribute( "submitterNodeOwners", submitterNodeOwners );
    if ( claim.getOwnCardName() != null && claim.getOwnCardName().length() != 0 )
    {
      request.setAttribute( "ecardForApproval", claim.getOwnCardName() );
    }
    else if ( claim.getCard() != null && claim.getCard().getLargeImageNameLocale().length() != 0 )
    {
      request.setAttribute( "ecardForApproval", claim.getCard().getLargeImageNameLocale() );
    }

    if ( promotion.getCalculator() != null && promotion.getCalculator().getCalculatorAwardType().isRangeAward() )
    {
      String lowValue = "";
      String highValue = "";
      request.setAttribute( "rangeCalculator", "true" );

      if ( StringUtils.isNotBlank( (String)request.getAttribute( "lowValue" ) ) && StringUtils.isNotBlank( (String)request.getAttribute( "highValue" ) ) )
      {
        lowValue = (String)request.getAttribute( "lowValue" );
        highValue = (String)request.getAttribute( "highValue" );
      }
      else
      {
        AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CALCULATOR_RESPONSES ) );
        AbstractRecognitionClaim abstractClaim = (AbstractRecognitionClaim)getClaimService().getClaimByIdWithAssociations( claim.getId(), claimAssociationRequestCollection );

        Set<ClaimRecipient> claimRecipients = abstractClaim.getClaimRecipients();
        if ( claimRecipients != null )
        {
          for ( ClaimRecipient claimRecipient : claimRecipients )
          {
            if ( claimRecipient.getCalculatorScore() != null )
            {
              CalculatorPayout payout = getCalculatorService().getCalculatorPayoutByScore( promotion.getCalculator().getId(), claimRecipient.getCalculatorScore().intValue() );
              lowValue = String.valueOf( payout.getLowAward() );
              highValue = String.valueOf( payout.getHighAward() );
            }
          }
        }
      }
      request.setAttribute( "lowValue", lowValue );
      request.setAttribute( "highValue", highValue );
    }

    if ( claim.getPromotion().isBudgetUsed() )
    {
      Budget budget = getPromotionService().getAvailableBudget( claim.getPromotion().getId(), claim.getSubmitter().getId(), claim.getNode().getId() );
      if ( budget != null )
      {
        BudgetMaster budgetMaster = budget.getBudgetSegment().getBudgetMaster();
        if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
        {
          request.setAttribute( "isCentralBudget", "true" );
          request.setAttribute( "conversionRatio", 1 );

          double availableBudget = budget.getCurrentValue().doubleValue();
          request.setAttribute( "availableBudget", BudgetUtils.getBudgetDisplayValue( availableBudget ) );
        }
        else
        {
          BigDecimal US_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
          BigDecimal submitterMediaValue = getUserService().getBudgetMediaValueForUser( claim.getSubmitter().getId() );
          BigDecimal receiverMediaValue = getUserService().getBudgetMediaValueForUser( claim.getClaimRecipients().iterator().next().getRecipient().getId() );
          request.setAttribute( "conversionRatio", BudgetUtils.calculateConversionRatio( receiverMediaValue, submitterMediaValue ).doubleValue() );

          double availableBudget = BudgetUtils.applyMediaConversion( budget.getCurrentValue(), US_MEDIA_VALUE, submitterMediaValue ).doubleValue();
          request.setAttribute( "availableBudget", BudgetUtils.getBudgetDisplayValue( availableBudget ) );

        }
      }
      else
      {
        BudgetMaster budgetMaster = getPromotionService().getPromotionById( claim.getPromotion().getId() ).getBudgetMaster();
        if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
        {
          request.setAttribute( "isCentralBudget", "true" );
          request.setAttribute( "conversionRatio", 1 );

          double availableBudget = 0;
          request.setAttribute( "availableBudget", BudgetUtils.getBudgetDisplayValue( availableBudget ) );
        }
        else
        {
          BigDecimal US_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
          BigDecimal submitterMediaValue = getUserService().getBudgetMediaValueForUser( claim.getSubmitter().getId() );
          BigDecimal receiverMediaValue = getUserService().getBudgetMediaValueForUser( claim.getClaimRecipients().iterator().next().getRecipient().getId() );
          request.setAttribute( "conversionRatio", BudgetUtils.calculateConversionRatio( receiverMediaValue, submitterMediaValue ).doubleValue() );

          double availableBudget = BudgetUtils.applyMediaConversion( new BigDecimal( 0 ), US_MEDIA_VALUE, submitterMediaValue ).doubleValue();
          request.setAttribute( "availableBudget", BudgetUtils.getBudgetDisplayValue( availableBudget ) );
        }
      }
    }

  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  /**
   * Returns a reference to the Calculator service.
   * 
   * @return a reference to the Calculator service.
   */
  private CalculatorService getCalculatorService()
  {
    return (CalculatorService)getService( CalculatorService.BEAN_NAME );
  }

  /**
   * Returns a reference to the Claim service.
   * 
   * @return a reference to the Claim service.
   */
  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  /**
   * Returns a reference to the Node service.
   * 
   * @return a reference to the Node service.
   */
  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  @SuppressWarnings( "unchecked" )
  private Claim getClaimElementDomainObjects( Claim claim )
  {
    for ( Iterator<ClaimElement> iter = claim.getClaimElements().iterator(); iter.hasNext(); )
    {
      ClaimElement claimElement = iter.next();
      if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isMultiSelectField() || claimElement.getClaimFormStepElement().getClaimFormElementType().isSelectField() )
      {
        List<DynaPickListType> pickListItems = new ArrayList<DynaPickListType>();
        // convert the comma delimited list of selected pickListItems to a list of strings
        Iterator<String> pickListCodes = ArrayUtil.convertDelimitedStringToList( claimElement.getValue(), "," ).iterator();
        while ( pickListCodes.hasNext() )
        {
          String code = (String)pickListCodes.next();
          pickListItems.add( DynaPickListType.lookup( claimElement.getClaimFormStepElement().getSelectionPickListName(), code ) );
        }
        claimElement.setPickListItems( pickListItems );
      }
    }
    return claim;
  }

}
