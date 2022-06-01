
package com.biperf.core.dao.throwdown;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.StackStandingParticipant;

public interface StackStandingParticipantDAO extends DAO
{

  public static final String BEAN_NAME = "stackStandingParticipantDAO";

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
