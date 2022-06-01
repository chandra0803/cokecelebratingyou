
package com.biperf.core.service.ssi;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.utils.BeanLocator;

/**
 * Update the SSI promotion claim approval audiences
 * SSIPromotionClaimApprovalAudienceUpdateAssociation.
 * 
 * @author kandhi
 * @since Oct 27, 2014
 * @version 1.0
 */
public class SSIPromotionClaimApprovalUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructs a <code>PromotionAudienceUpdateAssociation</code> object.
   * 
   * @param detachedPromotion a detached {@link Promotion} object.
   */
  public SSIPromotionClaimApprovalUpdateAssociation( Promotion detachedPromotion )
  {
    super( detachedPromotion );
  }

  /**
   * Use the detached {@link Promotion} object to update the attached {@link Promotion} object.
   * 
   * @param attachedDomain the attached version of the {@link Promotion} object.
   */
  public void execute( BaseDomain attachedDomain )
  {
    SSIPromotion attachedPromotion = (SSIPromotion)attachedDomain;

    updateClaimApproval( attachedPromotion );
  }

  private void updateClaimApproval( SSIPromotion attachedPromotion )
  {
    SSIPromotion detachedPromotion = (SSIPromotion)getDetachedDomain();
    attachedPromotion.setAllowActivityUpload( detachedPromotion.getAllowActivityUpload() );
    attachedPromotion.setAllowClaimSubmission( detachedPromotion.getAllowClaimSubmission() );
    attachedPromotion.setDaysToApproveClaim( detachedPromotion.getDaysToApproveClaim() );
    if ( detachedPromotion.getClaimForm() != null )
    {
      if ( attachedPromotion.getClaimForm() == null || attachedPromotion.getClaimForm().getId().longValue() != detachedPromotion.getClaimForm().getId().longValue() )
      {
        attachedPromotion.setClaimForm( getClaimFormDefinitionService().getClaimFormById( detachedPromotion.getClaimForm().getId() ) );
      }
    }
    else
    {
      attachedPromotion.setClaimForm( null );
    }
  }

  private ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)BeanLocator.getBean( ClaimFormDefinitionService.BEAN_NAME );
  }

}
