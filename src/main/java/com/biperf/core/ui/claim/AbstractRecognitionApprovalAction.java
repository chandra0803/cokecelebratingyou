
package com.biperf.core.ui.claim;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ApprovalsNoEmailBean;
import com.biperf.core.value.PickListValueBean;

public class AbstractRecognitionApprovalAction extends BaseDispatchAction
{
  /**
   * Goes through list of claims and returns list of claim ids 
   * for recipients with no email.
   * @param claims
   * @return
   */
  @SuppressWarnings( "unchecked" )
  protected List<ApprovalsNoEmailBean> getApprovedFormattedValueBeansForRecipientsWithNoEmail( List<AbstractRecognitionClaim> claims )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS_WITH_EMAIL ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENT_ADDRESS ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENT_EMPLOYERS ) );

    List<ApprovalsNoEmailBean> noEmailClaims = new ArrayList<ApprovalsNoEmailBean>();
    for ( AbstractRecognitionClaim claim : claims )
    {
      AbstractRecognitionClaim hydratedClaim = (AbstractRecognitionClaim)getClaimService().getClaimByIdWithAssociations( claim.getId(), associationRequestCollection );
      for ( ClaimRecipient claimRecipient : hydratedClaim.getClaimRecipients() )
      {
        if ( claimRecipient.getRecipient().getUserEmailAddresses().isEmpty() )
        {
          if ( claimRecipient.getApprovalStatusType().isAbstractApproved() )
          {
            ApprovalsNoEmailBean noEmailBean = new ApprovalsNoEmailBean();
            noEmailBean.setClaimId( claim.getId() );
            noEmailBean.setPromotionName( claim.getPromotion().getName() );
            noEmailBean.setRecipientDisplayName( claimRecipient.getRecipientDisplayName() );
            noEmailBean.setRecipientCountryCode( claimRecipient.getRecipientDisplayCountryCode() );
            noEmailBean.setRecipientNodeName( claimRecipient.getNode().getName() );

            for ( ParticipantEmployer recipientEmployer : claimRecipient.getRecipient().getParticipantEmployers() )
            {
              if ( recipientEmployer.getTerminationDate() == null )
              {
                PickListValueBean pickListDeptValueBean = getUserService().getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items",
                                                                                                       recipientEmployer.getParticipant().getLanguageType() == null
                                                                                                           ? UserManager.getDefaultLocale().toString()
                                                                                                           : recipientEmployer.getParticipant().getLanguageType().getCode(),
                                                                                                       recipientEmployer.getDepartmentType() );
                PickListValueBean pickListPositionValueBean = getUserService().getPickListValueFromCMView( PositionType.PICKLIST_ASSET + ".items",
                                                                                                           recipientEmployer.getParticipant().getLanguageType() == null
                                                                                                               ? UserManager.getDefaultLocale().toString()
                                                                                                               : recipientEmployer.getParticipant().getLanguageType().getCode(),
                                                                                                           recipientEmployer.getPositionType() );
                if ( recipientEmployer.getPositionType() != null )
                {
                  noEmailBean.setRecipientJobPosition( pickListPositionValueBean.getName() );
                }
                if ( recipientEmployer.getDepartmentType() != null )
                {
                  noEmailBean.setRecipientDepartment( pickListDeptValueBean.getName() );
                }
                break;
              }
            }

            noEmailClaims.add( noEmailBean );
          }
        }
      }
    }
    return noEmailClaims;
  }

  protected static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  protected static CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  protected BigDecimal calculateBudgetEquivalence( BigDecimal value, Participant recipient )
  {
    BigDecimal usMediaValue = getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
    BigDecimal recipientMediaValue = getUserService().getBudgetMediaValueForUser( recipient.getId() );
    value = BudgetUtils.applyMediaConversion( value, recipientMediaValue, usMediaValue );
    return value;
  }

  protected static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  protected static PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }
}
