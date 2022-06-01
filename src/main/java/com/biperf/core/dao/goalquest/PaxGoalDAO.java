
package com.biperf.core.dao.goalquest;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.service.AssociationRequestCollection;

public interface PaxGoalDAO extends DAO
{
  /**
   * BEAN_NAME
   */
  public static final String BEAN_NAME = "paxGoalDAO";

  /**
   * Get the PaxGoal from the database by the id.
   * 
   * @param id
   * @return paxGoal
   */
  public PaxGoal getPaxGoalById( Long id );

  /**
   * Get the PaxGoal with associations from the database by the id.
   * 
   * @param id
   * @param associationRequestCollection
   * @return paxGoal
   */
  public PaxGoal getPaxGoalByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Get the paxGoal of a participant by promotionId and userId.
   * 
   * @param promotionId
   * @param userId
   * @return paxGoal
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
   * @param associationRequestCollection
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

  /**
   * Get a list of user ids that have selected a paxgoal in a promotion.
   * 
   * @param userId
   * @return paxGoal
   */
  public List<PaxGoal> getUserIdsWithPaxGoalByPromotionId( Long promotionId );

}
