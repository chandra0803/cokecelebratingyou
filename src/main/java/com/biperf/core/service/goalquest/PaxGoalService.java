
package com.biperf.core.service.goalquest;

import java.util.List;

import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

public interface PaxGoalService extends SAO
{
  public static final String BEAN_NAME = "paxGoalService";

  /**
   * Get the PaxGoal from the database by the id.
   * 
   * @param id
   * @return PaxGoal
   */
  public PaxGoal getPaxGoalById( Long id );

  /**
   * Get the PaxGoal with associations from the database by the id.
   * 
   * @param id
   * @param associationRequestCollection
   * @return PaxGoal
   */
  public PaxGoal getPaxGoalByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Gets a PaxGoal object by promotionId and userId.
   * 
   * @param promotionId 
   * @param userId a participant's userID.
   * @return the specified PaxGoal object.
   */
  public PaxGoal getPaxGoalByPromotionIdAndUserId( Long promotionId, Long userId );

  /**
   * Get the paxGoal of a participant by promotion id.
   * 
   * @param promotionId
   * @param associationRequestCollection
   * @return paxGoal
   */
  public List<PaxGoal> getPaxGoalByPromotionId( Long promotionId, AssociationRequestCollection associationRequestCollection );

  /**
   * Get the paxGoal of a participant by promotion id.
   * 
   * @param promotionId
   * @return paxGoal
   */
  public List<PaxGoal> getPaxGoalByPromotionId( Long promotionId );

  /**
   * Saves the paxGoal.
   * 
   * @param paxGoal
   * @return paxGoal
   */
  public PaxGoal savePaxGoal( PaxGoal paxGoal );

  public PaxGoal savePaxGoalWithPartners( Long promotionId, PaxGoal paxGoal, List<ParticipantPartner> partners );

  /**
   * Gets a PaxGoal object by promotionId and userId.
   * 
   * @param promotionId 
   * @param userId a participant's userID.
   * @param associationRequestCollection
   * @return the specified PaxGoal object.
   */
  public PaxGoal getPaxGoalByPromotionIdAndUserId( Long promotionId, Long userId, AssociationRequestCollection associationRequestCollection );

  public void sendGoalSelectedEmailNotification( PaxGoal paxGoal, List paxPartners );

  public void sendPartnerGoalSelectedEmailNotification( PaxGoal paxGoal, Participant partner );

  public void sendCPPartnerGoalSelectedEmailNotification( PaxGoal paxGoal, Participant partner );

}
