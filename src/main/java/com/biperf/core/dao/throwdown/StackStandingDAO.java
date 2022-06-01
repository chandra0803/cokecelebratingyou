
package com.biperf.core.dao.throwdown;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.service.AssociationRequestCollection;

public interface StackStandingDAO extends DAO
{

  public static final String BEAN_NAME = "stackStandingDAO";

  public StackStanding get( Long stackStandingId );

  public StackStanding get( Long stackStandingId, AssociationRequestCollection associationRequest );

  public StackStanding save( StackStanding stackStanding );

  public List<StackStanding> getAll();

  public List<StackStanding> getRankings();

  public List<StackStanding> getRankingsForPromotion( Long promotionId );

  public StackStanding getRankingForPromotionAndRound( Long promotionId, int roundNumber );

  public List<StackStanding> getApprovedRankings();

  public List<StackStanding> getApprovedRankingsForPromotion( Long promotionId );

  public StackStanding getApprovedRankingForPromotionAndRound( Long promotionId, int roundNumber );

  public List<StackStanding> getUnapprovedRankings();

  public List<StackStanding> getUnapprovedRankingsForPromotion( Long promotionId );

  public StackStanding getUnapprovedRankingForPromotionAndRound( Long promotionId, int roundNumber );

  public boolean isAnyPaxPaidOutForRanking( Long stackStandingId );

  public Integer getNodeRankForUser( Long promotionId, int roundNumber, Long userId, Long nodeId );

  public Integer getHierarchyRankForUser( Long promotionId, int roundNumber, Long userId );

  public Integer getTotalUsersInHierarchyRanking( Long promotionId, int roundNumber );

}
