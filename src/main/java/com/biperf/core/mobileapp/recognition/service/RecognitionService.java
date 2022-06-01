
package com.biperf.core.mobileapp.recognition.service;

import java.util.List;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.SAO;
import com.biperf.core.value.PromotionMenuBean;

public interface RecognitionService extends SAO
{
  public static final String BEAN_NAME = "mobileRecognitionService";

  public List<EligibleRecognitionPromotion> getMobileRecognitionSubmissionList( Long userId, List<PromotionMenuBean> eligiblePromotions, boolean isUserAParticipant, Long recipientId );

  public List<Participant> getTeamMemberRecipients( Long userId, Long promotionId );

  public List<Participant> findRecentRecipientsFor( Long userId, Long promotionId );
}
