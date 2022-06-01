
package com.biperf.core.service.plateauawards;

import java.util.List;

import com.biperf.core.service.SAO;

public interface PlateauAwardsService extends SAO
{
  public static final String BEAN_NAME = "plateauAwardsService";

  public List<PlateauAwardReminderBean> findPlateauAwardRemindersFor( Long participantId );

  public PreviewMessage getPreviewMessage( Long participantId );

  public int getNumberOfPaxWithUnclaimedAwardsFor( Long managerId );

  public void sendPlateauAwardReminders( List<Long> merchOrderIds, Long managerId, String comments );
}
