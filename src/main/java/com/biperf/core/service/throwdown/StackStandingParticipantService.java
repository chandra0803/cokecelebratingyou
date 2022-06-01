
package com.biperf.core.service.throwdown;

import java.util.List;

import com.biperf.core.domain.promotion.StackStandingParticipant;
import com.biperf.core.service.SAO;

public interface StackStandingParticipantService extends SAO
{
  public static String BEAN_NAME = "stackStandingParticipantService";

  public StackStandingParticipant getStackStandingParticipant( Long stackStandingParticipantId );

  public StackStandingParticipant saveStackStandingParticipant( StackStandingParticipant stackStandingParticipant );

  public StackStandingParticipant getStackStandingParticipant( Long stackStandingNodeId, Long userId );

  public List<StackStandingParticipant> getPageRankingParticipants( Long rankingNodeId, int fromIndex, int endIndex );

  public List<StackStandingParticipant> getAllRankingParticipants( Long rankingNodeId );

  public List<StackStandingParticipant> getTopRankingParticipants( Long rankingNodeId );

  public int getTotalRankingParticipants( Long rankingNodeId );

  public int getPaxPositionInRanking( Long rankingNodeId, Long userId );

  public StackStandingParticipant getHierarchyRankDetailsForPax( Long promotionId, int roundNumber, Long userId );

}
