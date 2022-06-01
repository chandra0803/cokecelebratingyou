
package com.biperf.core.service.ssi;

import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.SSIFileUpload;

/**
 * SSI promotion specific service
 * SSIPromotionService.
 * 
 * @author kandhi
 * @since Oct 29, 2014
 * @version 1.0
 */
public interface SSIPromotionService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "ssiPromotionService";

  public SSIPromotion getLiveSSIPromotion();

  public SSIPromotion getLiveSSIPromotionWithAssociations( AssociationRequestCollection associationRequestCollection );

  public SSIPromotion getLiveOrCompletedSSIPromotion();

  public boolean isPaxInContestCreatorAudience( Long userId );

  public Map<String, Set<Participant>> getAllowedContestApprovers( Long promotionId );

  public SSIFileUpload uploadPdfForSSIGuide( SSIFileUpload data ) throws ServiceErrorException;

  public boolean moveFileToWebdav( String mediaUrl ) throws ServiceErrorException;

  public String buildSSIContestGuideUrl( String filePath );

  public String getSSIContestGuideUrl( String filePath );
}
