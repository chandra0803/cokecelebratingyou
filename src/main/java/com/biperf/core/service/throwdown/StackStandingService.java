
package com.biperf.core.service.throwdown;

import java.util.List;

import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.domain.promotion.StackStandingParticipant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

public interface StackStandingService extends SAO
{
  public static String BEAN_NAME = "stackStandingService";

  public StackStanding createRankingForRound( Long promotionId, int roundNumber ) throws ServiceErrorException;

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

  public StackStandingParticipant saveStackStandingParticipant( StackStandingParticipant stackStandingParticipant );

  public List<StackStandingParticipant> getAllRankingParticipants( Long rankingNodeId );

  public List<StackStandingParticipant> getPageRankingParticipants( Long rankingNodeId, int fromIndex, int endIndex );

  public List<StackStandingParticipant> getTopRankingParticipants( Long rankingNodeId );

  public int getTotalRankingParticipants( Long rankingNodeId );

  public int getPaxPositionInRanking( Long rankingNodeId, Long userId );

  public StackStandingParticipant getStackStandingParticipant( Long stackStandingNodeId, Long userId );

  public Integer getTotalUsersInHierarchyRanking( Long promotionId, int roundNumber );

  public StackStandingParticipant getHierarchyRankDetailsForPax( Long promotionId, int roundNumber, Long userId );

}
