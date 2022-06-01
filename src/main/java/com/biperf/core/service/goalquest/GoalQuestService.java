
package com.biperf.core.service.goalquest;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.goalquest.PendingGoalQuestAwardSummary;
import com.biperf.core.domain.goalquest.PromotionView;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.SAO;
import com.biperf.core.ui.goalquest.ManagerGoalquestViewBean;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.core.value.PromotionMenuBean;

public interface GoalQuestService extends SAO
{
  public static final String BEAN_NAME = "goalQuestService";

  /**
   * Get the map containing the summary lists for participant and manager override levels by promotion id.
   * 
   * @param promotionId
   * @return PendingGoalQuestAwardSummary 
   */
  public PendingGoalQuestAwardSummary getGoalQuestAwardSummaryByPromotionId( Long promotionId );

  /**
   * Generate and mail a detail extract from the goalCalculationList
   * 
   * @param goalCalculationList
   * @return boolean 
   */
  public boolean generateAndMailExtractReport( List goalCalculationList, GoalQuestPromotion promotion, String batchComments );

  /**
   * Generate and mail a detail extract from the goalCalculationList
   * 
   * @param goalCalculationList
   * @return boolean 
   */
  public boolean generateAndMailExtractReport( List goalCalculationList, GoalQuestPromotion promotion );

  /**
   * @param promotionId 
   * @param userId
   * @return Map containing list of GoalQuestReviewProgress objects, promotion if any
   */
  public Map<String, Object> getGoalQuestProgressByPromotionIdAndUserId( Long promotionId, Long userId );

  /**
   * Get the Encrypted External Survey Link
   * @author kumarse
   * @param 
   * 
   */
  public String getEncyptedExternalSurveyLink( String url ) throws Exception;

  /**
   * Get the promotion and user id and survey type information 
   * Survey Type ex. WINNER, POSTCHOICE Etc.
   * Use 3DES to decrypt
   * @author kumarse
   * @param encryptedSurveyQueryParam
   * @return Map with promotionId, userId and Survey Type
   * 
   */
  public Map<String, String> getSurveyPromoInformation( String encryptedSurveyLnk );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.GoalQuestPaxActivityDAO#isParticipantPayoutComplete(java.lang.Long,
   *      java.lang.Long)
   * @param userId
   * @param promotionId
   * @return boolean payout completed or not
   */
  public boolean isParticipantPayoutComplete( Long userId, Long promotionId );

  public List<ParticipantSearchView> getGoalQuestMiniProfilesForUserAndPromotion( Long promotionId, Long userId );

  public boolean isParticipantInUserCharType( Participant participant, GoalQuestPromotion promotion );

  public boolean isParticipantInNodeType( Participant participant, GoalQuestPromotion promotion );

  public ParticipantPartner saveParticipantPartnerAssoc( ParticipantPartner participantPartnerAssoc );

  public List<Long> getEligibleParticipantsForPromotion( GoalQuestPromotion promotion );

  public void getJournalsDeposited( List<DepositProcessBean> depositProcessPointsList, Long promotionId );

  public void createMerchOrders( List<DepositProcessBean> depositProcessMerchList, Long promotionId );
  
  /** programRole is the honeycomb backend code. section is section of the page, like url fragment */
  public String buildGoalquestSSOLink( String programRole, String programId, String section );
  
  /**
   * Get list of goalquest program details from Honeycomb services. 
   * If a honeycomb error occurs, an empty list will be returned 
   */
  public List<PromotionView> getHoneycombProgramDetails( Participant participant );
  
  /**
   * Get a list of a manager's goalquest programs from Honeycomb services. They are returned as an object compatible with the manager views on the site.
   */
  public List<ManagerGoalquestViewBean> getHoneycombManagerPrograms( Long honeycombUserId );
  
  public List<PromotionMenuBean> getManagerPromotionMenuBeans( List<PromotionMenuBean> eligiblePromotions, Participant participant, String promotionTypeCode );

}
