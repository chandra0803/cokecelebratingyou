
package com.biperf.core.dao.promotion;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.value.CelebrationManagerReminderBean;

public interface CelebrationManagerMessageDAO extends DAO
{
  public static final String BEAN_NAME = "celebrationManagerMessageDAO";

  public CelebrationManagerMessage saveCelebrationManagerMessage( CelebrationManagerMessage celebrationManagerMessage );

  public CelebrationManagerMessage getCelebrationManagerMessageById( Long id );

  public List<CelebrationManagerReminderBean> getCelebrationManagerRemindersList( Long participantId );

  public List<CelebrationManagerMessage> getCelebrationManagerByPromotion( Long promotionId );

  public String getServiceAnniversaryEcardOrDefaultCelebrationEcard( String ecardFlashName, Long promotionId, String locale );

}
