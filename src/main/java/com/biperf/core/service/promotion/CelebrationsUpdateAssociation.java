
package com.biperf.core.service.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.UpdateAssociationRequest;

public class CelebrationsUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param promotion
   */
  public CelebrationsUpdateAssociation( Promotion promotion )
  {
    super( promotion );
  }

  public void execute( BaseDomain attachedDomain )
  {
    RecognitionPromotion attachedPromo = (RecognitionPromotion)attachedDomain;
    RecognitionPromotion detachedPromo = (RecognitionPromotion)getDetachedDomain();

    attachedPromo.setCelebrationDisplayPeriod( detachedPromo.getCelebrationDisplayPeriod() );
    attachedPromo.setAllowOwnerMessage( detachedPromo.isAllowOwnerMessage() );
    attachedPromo.setAllowDefaultMessage( detachedPromo.isAllowDefaultMessage() );
    attachedPromo.setDefaultMessage( detachedPromo.getDefaultMessage() );
    attachedPromo.setYearTileEnabled( detachedPromo.isYearTileEnabled() );
    attachedPromo.setTimelineTileEnabled( detachedPromo.isTimelineTileEnabled() );
    attachedPromo.setVideoTileEnabled( detachedPromo.isVideoTileEnabled() );
    attachedPromo.setVideoPath( detachedPromo.getVideoPath() );
    attachedPromo.setShareToMedia( detachedPromo.isShareToMedia() );
    attachedPromo.setFillerImg1AwardNumberEnabled( detachedPromo.isFillerImg1AwardNumberEnabled() );
    attachedPromo.setFillerImg2AwardNumberEnabled( detachedPromo.isFillerImg2AwardNumberEnabled() );
    attachedPromo.setFillerImg3AwardNumberEnabled( detachedPromo.isFillerImg3AwardNumberEnabled() );
    attachedPromo.setFillerImg4AwardNumberEnabled( detachedPromo.isFillerImg4AwardNumberEnabled() );
    attachedPromo.setFillerImg5AwardNumberEnabled( detachedPromo.isFillerImg5AwardNumberEnabled() );
    attachedPromo.setCelebrationFillerImage1( detachedPromo.getCelebrationFillerImage1() );
    attachedPromo.setCelebrationFillerImage2( detachedPromo.getCelebrationFillerImage2() );
    attachedPromo.setCelebrationFillerImage3( detachedPromo.getCelebrationFillerImage3() );
    attachedPromo.setCelebrationFillerImage4( detachedPromo.getCelebrationFillerImage4() );
    attachedPromo.setCelebrationFillerImage5( detachedPromo.getCelebrationFillerImage5() );
    attachedPromo.setServiceAnniversary( detachedPromo.isServiceAnniversary() );
    attachedPromo.setAnniversaryInYears( detachedPromo.getAnniversaryInYears() );
    attachedPromo.setCelebrationGenericEcard( detachedPromo.getCelebrationGenericEcard() );
    attachedPromo.setDefaultCelebrationName( detachedPromo.getDefaultCelebrationName() );
    attachedPromo.setDefaultCelebrationAvatar( detachedPromo.getDefaultCelebrationAvatar() );
  }

}
