
package com.biperf.core.service.throwdown.impl;

import java.util.List;

import com.biperf.core.dao.throwdown.StackStandingParticipantDAO;
import com.biperf.core.domain.promotion.StackStandingParticipant;
import com.biperf.core.service.throwdown.StackStandingParticipantService;

public class StackStandingParticipantServiceImpl implements StackStandingParticipantService
{
  private StackStandingParticipantDAO stackStandingParticipantDAO;

  @Override
  public StackStandingParticipant getStackStandingParticipant( Long stackStandingParticipantId )
  {
    return stackStandingParticipantDAO.getStackStandingParticipant( stackStandingParticipantId );
  }

  @Override
  public StackStandingParticipant saveStackStandingParticipant( StackStandingParticipant stackStandingParticipant )
  {
    return stackStandingParticipantDAO.saveStackStandingParticipant( stackStandingParticipant );
  }

  @Override
  public StackStandingParticipant getStackStandingParticipant( Long stackStandingNodeId, Long userId )
  {
    return stackStandingParticipantDAO.getStackStandingParticipant( stackStandingNodeId, userId );
  }

  @Override
  public List<StackStandingParticipant> getPageRankingParticipants( Long rankingNodeId, int fromIndex, int endIndex )
  {
    return stackStandingParticipantDAO.getPageRankingParticipants( rankingNodeId, fromIndex, endIndex );
  }

  @Override
  public List<StackStandingParticipant> getAllRankingParticipants( Long rankingNodeId )
  {
    return stackStandingParticipantDAO.getAllRankingParticipants( rankingNodeId );
  }

  @Override
  public List<StackStandingParticipant> getTopRankingParticipants( Long rankingNodeId )
  {
    return stackStandingParticipantDAO.getTopRankingParticipants( rankingNodeId );
  }

  @Override
  public int getTotalRankingParticipants( Long rankingNodeId )
  {
    return stackStandingParticipantDAO.getTotalRankingParticipants( rankingNodeId );
  }

  @Override
  public int getPaxPositionInRanking( Long rankingNodeId, Long userId )
  {
    return stackStandingParticipantDAO.getPaxPositionInRanking( rankingNodeId, userId );
  }

  public StackStandingParticipant getHierarchyRankDetailsForPax( Long promotionId, int roundNumber, Long userId )
  {
    return stackStandingParticipantDAO.getHierarchyRankDetailsForPax( promotionId, roundNumber, userId );
  }

  public void setStackStandingParticipantDAO( StackStandingParticipantDAO stackStandingParticipantDAO )
  {
    this.stackStandingParticipantDAO = stackStandingParticipantDAO;
  }

}
