/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsNominationDetailsController.java,v $
 */

package com.biperf.core.ui.approvals;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.ApprovableItemApprover;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.PickListItemSortOrderComparator;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.domain.enums.ApproverLevelType;
import java.util.Collections;
import java.util.Comparator;
import com.biperf.core.domain.client.TccNomLevelPayout;
import com.biperf.core.service.client.CokeClientService;

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
public class ApprovalsNominationDetailsController extends BaseController
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
    ApprovalsNominationDetailsForm nominationApprovalDetailsForm = (ApprovalsNominationDetailsForm)request.getAttribute( "approvalsNominationDetailsForm" );

    Approvable approvable = (Approvable)request.getAttribute( "approvable" );
    // Store approvable to request if action hasn't already - happens on validation errors
    if ( approvable == null )
    {
      approvable = ApprovalsNominationDetailsAction.getHyrdatedApprovable( request, nominationApprovalDetailsForm );

      request.setAttribute( "approvable", approvable );
    }

    request.setAttribute( "statusType", ApprovalStatusType.getList( approvable.getPromotion().getPromotionType().getCode(), null ) );

    // Also set "claimDetails" for compatibility with claim elements jsp fragment
    if ( approvable instanceof Claim )
    {
      getClaimElementDomainObjects( (NominationClaim)approvable );
      request.setAttribute( "claimDetails", (NominationClaim)approvable );
    }
    NominationPromotion promotion = (NominationPromotion)approvable.getPromotion();
    if ( promotion.getEvaluationType().equals( NominationEvaluationType.lookup( NominationEvaluationType.CUMULATIVE ) ) )
    {
      ClaimGroup claimGroup = (ClaimGroup)approvable;
      for ( Object claimObject : claimGroup.getClaims() )
      {
        NominationClaim nominationClaim = (NominationClaim)claimObject;
        request.setAttribute( "claimDetails", nominationClaim );
      }
    }
    if ( approvable instanceof ClaimGroup )
    {
      ClaimGroup claimGroup = (ClaimGroup)approvable;
      for ( Object claimObject : claimGroup.getClaims() )
      {
        NominationClaim nominationClaim = (NominationClaim)claimObject;
        if ( nominationClaim.getOwnCardName() != null && nominationClaim.getOwnCardName().length() != 0 )
        {
          request.setAttribute( "ecardForNominationApproval", nominationClaim.getOwnCardName() );
        }
        else if ( nominationClaim.getCard() != null && nominationClaim.getCard().getLargeImageNameLocale().length() != 0 )
        {
          request.setAttribute( "ecardForNominationApproval", nominationClaim.getCard().getLargeImageNameLocale() );
        }
      }
    }
    if ( approvable instanceof NominationClaim )
    {
      NominationClaim nominationClaim = (NominationClaim)approvable;
      if ( nominationClaim.getOwnCardName() != null && nominationClaim.getOwnCardName().length() != 0 )
      {
        request.setAttribute( "ecardForNominationApproval", nominationClaim.getOwnCardName() );
      }
      else if ( nominationClaim.getCard() != null && nominationClaim.getCard().getLargeImageNameLocale().length() != 0 )
      {
        request.setAttribute( "ecardForNominationApproval", nominationClaim.getCard().getLargeImageNameLocale() );
      }
    }

    NominationPromotion nomPromo = (NominationPromotion)approvable.getPromotion();
    if ( nomPromo.isPublicationDateActive() )
    {
      nominationApprovalDetailsForm.setNotificationDate( DateUtils.toDisplayString( nomPromo.getPublicationDate() ) );
      nominationApprovalDetailsForm.setPublicationDateActive( true );
    }
    else
    {
      nominationApprovalDetailsForm.setPublicationDateActive( false );
    }
    if ( nominationApprovalDetailsForm.getNotificationDate() == null || nominationApprovalDetailsForm.getNotificationDate().length() == 0 )
    {
      nominationApprovalDetailsForm.setNotificationDate( null );
    }

    ApprovalsNominationListController.buildNotificationProcessTime( request );

    List<ApprovalStatusType> approvalStatusTypes = new ArrayList<ApprovalStatusType>();
    List<ApprovalStatusType> approvalStatusTypesWIthExpired = ApprovalStatusType.getList( PromotionType.NOMINATION, new PickListItemSortOrderComparator() );
    for ( ApprovalStatusType pickListItem : approvalStatusTypesWIthExpired )
    {
      if ( !pickListItem.getCode().equals( ApprovalStatusType.EXPIRED ) )
      {
        approvalStatusTypes.add( pickListItem );
      }

    }

    String viewApprovalStatusCode;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );

      viewApprovalStatusCode = (String)clientStateMap.get( "viewApprovalStatusCode" );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // when not viewing a pending approvable, set status to that previously assigned by the current
    // user, not the current status value
    if ( viewApprovalStatusCode != null && !ApprovalStatusType.PENDING.equals( viewApprovalStatusCode ) )
    {
      request.setAttribute( "readOnly", Boolean.TRUE );
      ApprovableItem approvableItem = (ApprovableItem)approvable.getApprovableItems().iterator().next();
      Set<ApprovableItemApprover> approvableItemApprovers = approvableItem.getApprovableItemApprovers();
      for ( ApprovableItemApprover approvableItemApprover : approvableItemApprovers )
      {
        User existingApprovalUser = approvableItemApprover.getApproverUser();
        if ( existingApprovalUser != null && existingApprovalUser.getId().equals( UserManager.getUserId() ) )
        {
          // Match found
          // Force approvableItem to first status set by this approver regardless of it's
          // current value.
          approvableItem.setApprovalStatusType( ApprovalStatusType.lookup( viewApprovalStatusCode ) );
          break;
        }
      }

    }
    request.setAttribute( "approvalStatusTypes", approvalStatusTypes );

    // Client customization for WIP #56492 starts
    List<ApproverLevelType> approverLevelTypes = ApproverLevelType.getList( );
    request.setAttribute( "approverLevelTypes", approverLevelTypes );
    // Client customization for WIP #56492 ends
    
    // Client customization for WIP 58122
    List<TccNomLevelPayout> levelPayouts=getCokeClientService().getLevelTotalPoints( promotion.getId());
    Collections.sort( levelPayouts, ASCE_COMPARATOR_LEVEL_PAYOUTS );
    request.setAttribute( "levelPayouts", levelPayouts );
    int i=1;
    for ( Iterator iter = levelPayouts.iterator(); iter.hasNext(); )
    {
    	TccNomLevelPayout t = (TccNomLevelPayout)iter.next();
    	t.setLevelId("level"+i);
    	i++;
    }
    if ( promotion.isBudgetUsed() )
    {
      if ( approvable instanceof NominationClaim )
      {
        NominationClaim nominationClaim = (NominationClaim)approvable;
        Participant submitter = nominationClaim.getSubmitter();
        double availableBudget = loadAvailableBudget( promotion, submitter );

        if ( ( promotion.getAwardAmountFixed() != null || promotion.getAwardAmountMin() != null && promotion.getAwardAmountMax() != null ) && approvable.getApprovalRound() > 1
            && nominationApprovalDetailsForm.getAwardQuantity() != null && !nominationApprovalDetailsForm.getAwardQuantity().isEmpty() )
        {
          availableBudget += new Double( nominationApprovalDetailsForm.getAwardQuantity() );
        }
        request.setAttribute( "availableBudget", BudgetUtils.getBudgetDisplayValue( availableBudget ) );
      }
      else if ( approvable instanceof ClaimGroup )
      {
        ClaimGroup claimGroup = (ClaimGroup)approvable;
        for ( Object claimObject : claimGroup.getClaims() )
        {
          NominationClaim nominationClaim = (NominationClaim)claimObject;
          Participant submitter = nominationClaim.getSubmitter();
          double availableBudget = loadAvailableBudget( promotion, submitter );
          if ( claimGroup.getApprovalRound() != null )
          {
            if ( ( promotion.getAwardAmountFixed() != null || promotion.getAwardAmountMin() != null && promotion.getAwardAmountMax() != null ) && claimGroup.getApprovalRound() > 1
                && nominationApprovalDetailsForm.getAwardQuantity() != null && !nominationApprovalDetailsForm.getAwardQuantity().isEmpty() )
            {
              availableBudget += new Double( nominationApprovalDetailsForm.getAwardQuantity() );
            }
          }
          request.setAttribute( "availableBudget", BudgetUtils.getBudgetDisplayValue( availableBudget ) );
        }
      }
    }

    request.setAttribute( "tomorrowDate", DateUtils.toDisplayString( org.apache.commons.lang3.time.DateUtils.addDays( new Date(), 1 ) ) );

    // determine number of nominees
    request.setAttribute( "participantNumber", 1 );
    if ( approvable instanceof NominationClaim && ( (NominationClaim)approvable ).isTeam() )
    {
      NominationClaim nominationClaim = (NominationClaim)approvable;
      request.setAttribute( "participantNumber", nominationClaim.getClaimRecipients().size() );
    }
    
    // Client customization for WIP 58122
    Long approvalRound=null;
    
     approvalRound =approvable.getApprovalRound();
    //request.setAttribute( "approvalRound", approvalRound );
     int capPerPax=0;
    if(promotion.getCapPerPax()!=null)
     capPerPax=promotion.getCapPerPax();
    request.setAttribute( "capPerPax", capPerPax );
    // Client customization for WIP 58122
    boolean final_Approver=false;
    if(promotion.getApprovalNodeLevels().toString().equals(approvalRound.toString()))
    {
  	  final_Approver=true; 
    }
    request.setAttribute( "final_Approver",final_Approver);
    nominationApprovalDetailsForm.setCapPerPax(capPerPax);
    nominationApprovalDetailsForm.setLevelPayouts(levelPayouts);

  }

  private double loadAvailableBudget( Promotion promotion, Participant participant )
  {
    // nomination budgets should always be central (don't need user or node id)
    Budget budget = getPromotionService().getAvailableBudget( promotion, participant, null );
    double availableBudget = 0;
    if ( budget != null )
    {
      availableBudget = budget.getCurrentValue().doubleValue();
    }
    return availableBudget;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
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
  // Client customization for WIP 58122
  private static CokeClientService getCokeClientService()
  {
    return (CokeClientService)getService( CokeClientService.BEAN_NAME );
  }
  private static Comparator<TccNomLevelPayout> ASCE_COMPARATOR_LEVEL_PAYOUTS = new Comparator<TccNomLevelPayout>()
  {
    public int compare( TccNomLevelPayout c1, TccNomLevelPayout c2 )
    {
      return c1.getLevelDescription().compareTo( c2.getLevelDescription() );
    }
  };

}
