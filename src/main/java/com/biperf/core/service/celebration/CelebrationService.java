
package com.biperf.core.service.celebration;

import java.util.List;

import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.CelebrationAvatarUploadValue;
import com.biperf.core.value.CelebrationManagerReminderBean;

public interface CelebrationService extends SAO
{
  /**
   * BEAN_NAME for referencing in tests and spring config files.
   */
  public final String BEAN_NAME = "celebrationService";

  public CelebrationManagerMessage saveCelebrationManagerMessage( CelebrationManagerMessage celebrationManagerMessage );

  public CelebrationManagerMessage getCelebrationManagerMessageById( Long id );

  public CelebrationManagerMessage getCelebrationManagerMessageById( Long id, AssociationRequestCollection associationRequestCollection );

  public List<CelebrationManagerReminderBean> getCelebrationManagerRemindersList( Long participantId );

  public List<CelebrationManagerMessage> getCelebrationManagerByPromotion( Long promotionId );

  public String getServiceAnniversaryEcardOrDefaultCelebrationEcard( String ecardFlashName, Long promotionId, String locale );

  public boolean validFileData( CelebrationAvatarUploadValue data );

  public CelebrationAvatarUploadValue uploadAvatarForCelebration( CelebrationAvatarUploadValue data ) throws ServiceErrorException;
}
