
package com.biperf.core.service.ssi;

import java.util.Iterator;
import java.util.Set;

import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.domain.ssi.SSIPromotionContestApprovalLevel1Audience;
import com.biperf.core.domain.ssi.SSIPromotionContestApprovalLevel2Audience;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.utils.BeanLocator;

/**
 * Update the SSI promotion contest level 1 approval audiences
 * Update the SSI promotion contest level 2 approval audiences
 * SSIPromotionContestApprovalAudienceUpdateAssociation.
 * 
 * @author kandhi
 * @since Oct 27, 2014
 * @version 1.0
 */
public class SSIPromotionContestApprovalAudienceUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructs a <code>SSIPromotionContestApprovalAudienceUpdateAssociation</code> object.
   * 
   * @param detachedPromotion a detached {@link Promotion} object.
   */
  public SSIPromotionContestApprovalAudienceUpdateAssociation( Promotion detachedPromotion )
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
    SSIPromotion detachedPromo = (SSIPromotion)getDetachedDomain();

    attachedPromotion.setRequireContestApproval( detachedPromo.getRequireContestApproval() );
    attachedPromotion.setDaysToApproveOnSubmission( detachedPromo.getDaysToApproveOnSubmission() );
    attachedPromotion.setContestApprovalLevels( detachedPromo.getContestApprovalLevels() );

    updateContestApprovalLevel1Audience( attachedPromotion );
    updateContestApprovalLevel2Audience( attachedPromotion );
  }

  /**
   * Update the contest approval level 1 audience.
   * 
   * @param attachedPromo
   */
  private void updateContestApprovalLevel1Audience( SSIPromotion attachedPromo )
  {
    AudienceDAO audienceDAO = getAudienceDAO();

    SSIPromotion detachedPromo = (SSIPromotion)getDetachedDomain();
    attachedPromo.setContestApprovalLevel1AudienceType( detachedPromo.getContestApprovalLevel1AudienceType() );

    Set<SSIPromotionContestApprovalLevel1Audience> detachedContestApprovalLevel1Audience = detachedPromo.getContestApprovalLevel1Audiences();
    Iterator<SSIPromotionContestApprovalLevel1Audience> detachedContestApprovalLevel1AudienceIter = detachedPromo.getContestApprovalLevel1Audiences().iterator();

    SSIPromotionContestApprovalLevel1Audience ssiContestApprovalLevel1Audience;
    Set<SSIPromotionContestApprovalLevel1Audience> attachedContestApprovalLevel1Audiences = attachedPromo.getContestApprovalLevel1Audiences();

    // If the attached promotion contains any audiences not in the detached set
    // then it should be removed from the promotion
    Iterator<SSIPromotionContestApprovalLevel1Audience> attachedContestApprovalLevel1AudienceIter = attachedContestApprovalLevel1Audiences.iterator();
    while ( attachedContestApprovalLevel1AudienceIter.hasNext() )
    {
      ssiContestApprovalLevel1Audience = (SSIPromotionContestApprovalLevel1Audience)attachedContestApprovalLevel1AudienceIter.next();
      if ( !detachedContestApprovalLevel1Audience.contains( ssiContestApprovalLevel1Audience ) )
      {
        attachedContestApprovalLevel1AudienceIter.remove();
      }
    }

    // This will attempt to add all detached audiences to the promotion.
    // Since it is a set, then only non-duplicates will be added.
    while ( detachedContestApprovalLevel1AudienceIter.hasNext() )
    {
      ssiContestApprovalLevel1Audience = (SSIPromotionContestApprovalLevel1Audience)detachedContestApprovalLevel1AudienceIter.next();
      ssiContestApprovalLevel1Audience.setAudience( audienceDAO.getAudienceById( ssiContestApprovalLevel1Audience.getAudience().getId() ) );
      attachedPromo.addContestApprovalLevel1Audiences( ssiContestApprovalLevel1Audience );
    }
  }

  /**
   * Update the contest approval level 2 audience.
   * 
   * @param attachedPromo
   */
  private void updateContestApprovalLevel2Audience( SSIPromotion attachedPromo )
  {
    AudienceDAO audienceDAO = getAudienceDAO();

    SSIPromotion detachedPromo = (SSIPromotion)getDetachedDomain();
    attachedPromo.setContestApprovalLevel1AudienceType( detachedPromo.getContestApprovalLevel2AudienceType() );

    Set<SSIPromotionContestApprovalLevel2Audience> detachedContestApprovalLevel2Audience = detachedPromo.getContestApprovalLevel2Audiences();
    Iterator<SSIPromotionContestApprovalLevel2Audience> detachedContestApprovalLevel2AudienceIter = detachedPromo.getContestApprovalLevel2Audiences().iterator();

    SSIPromotionContestApprovalLevel2Audience ssiContestApprovalLevel2Audience;
    Set<SSIPromotionContestApprovalLevel2Audience> attachedContestApprovalLevel2Audiences = attachedPromo.getContestApprovalLevel2Audiences();

    // If the attached promotion contains any audiences not in the detached set
    // then it should be removed from the promotion
    Iterator<SSIPromotionContestApprovalLevel2Audience> attachedContestApprovalLevel2AudienceIter = attachedContestApprovalLevel2Audiences.iterator();
    while ( attachedContestApprovalLevel2AudienceIter.hasNext() )
    {
      ssiContestApprovalLevel2Audience = (SSIPromotionContestApprovalLevel2Audience)attachedContestApprovalLevel2AudienceIter.next();
      if ( !detachedContestApprovalLevel2Audience.contains( ssiContestApprovalLevel2Audience ) )
      {
        attachedContestApprovalLevel2AudienceIter.remove();
      }
    }

    // This will attempt to add all detached audiences to the promotion.
    // Since it is a set, then only non-duplicates will be added.
    while ( detachedContestApprovalLevel2AudienceIter.hasNext() )
    {
      ssiContestApprovalLevel2Audience = (SSIPromotionContestApprovalLevel2Audience)detachedContestApprovalLevel2AudienceIter.next();
      ssiContestApprovalLevel2Audience.setAudience( audienceDAO.getAudienceById( ssiContestApprovalLevel2Audience.getAudience().getId() ) );
      attachedPromo.addContestApprovalLevel2Audiences( ssiContestApprovalLevel2Audience );
    }
  }

  /**
   * Returns a reference to the Audience DAO service.
   * 
   * @return a reference to the Audience DAO service.
   */
  private AudienceDAO getAudienceDAO()
  {
    return (AudienceDAO)BeanLocator.getBean( AudienceDAO.BEAN_NAME );
  }

}
