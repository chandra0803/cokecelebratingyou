
package com.biperf.core.service.throwdown;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.StackStandingParticipant;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.domain.user.UserNode;

public class TeamRankingResult implements Serializable, Cloneable
{
  private static final long serialVersionUID = 1L;

  private Team team = null;
  private Participant participant = null;
  private List<MatchRankingResult> matchResults = new ArrayList<MatchRankingResult>();
  private int rank;
  private boolean tied;
  private int payoutAmount;
  private BigDecimal rankingFactor = null;
  private StackStandingParticipant rankingPax = null;

  public TeamRankingResult()
  {
  }

  public TeamRankingResult( Team team )
  {
    this.team = team;
  }

  public Team getTeam()
  {
    return team;
  }

  public void setTeam( Team team )
  {
    this.team = team;
  }

  public int getRank()
  {
    return rank;
  }

  public void setRank( int rank )
  {
    this.rank = rank;
  }

  public boolean isTied()
  {
    return tied;
  }

  public void setTied( boolean tied )
  {
    this.tied = tied;
  }

  public List<MatchRankingResult> getMatchResults()
  {
    return matchResults;
  }

  public void setMatchResults( List<MatchRankingResult> matchResults )
  {
    this.matchResults = matchResults;
  }

  public BigDecimal getTeamProgress()
  {
    if ( rankingFactor == null )
    {
      BigDecimal progress = BigDecimal.ZERO;
      for ( MatchRankingResult matchResult : matchResults )
      {
        progress = progress.add( matchResult.getTotalProgress() );
      }
      return progress;
    }
    else
    {
      return rankingFactor;
    }
  }

  public void addMatchResult( MatchRankingResult result )
  {
    this.matchResults.add( result );
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof TeamRankingResult ) )
    {
      return false;
    }

    final TeamRankingResult teamRanking = (TeamRankingResult)o;

    if ( getParticipant() != null ? !getParticipant().equals( teamRanking.getParticipant() ) : teamRanking.getParticipant() != null )
    {
      return false;
    }

    if ( getParticipant() == null && teamRanking.getParticipant() == null )
    {
      return true;
    }
    if ( null != getParticipant() && null != teamRanking.getParticipant() )
    {
      return getParticipant().getId().equals( teamRanking.getParticipant().getId() );
    }
    else
    {
      return false;
    }
  }

  public int hashCode()
  {
    int result;
    result = getParticipant().getId().hashCode();
    return result;
  }

  public int getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( int payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public BigDecimal getRankingFactor()
  {
    return rankingFactor;
  }

  public void setRankingFactor( BigDecimal rankingFactor )
  {
    this.rankingFactor = rankingFactor;
  }

  public Object deepCopy() throws CloneNotSupportedException
  {
    TeamRankingResult clonedTeamResult = (TeamRankingResult)super.clone();
    clonedTeamResult.setTeam( getTeam() );
    clonedTeamResult.setParticipant( getParticipant() );
    clonedTeamResult.setMatchResults( getMatchResults() );
    clonedTeamResult.setRankingFactor( getRankingFactor() );
    return clonedTeamResult;
  }

  public Participant getParticipant()
  {
    return team != null ? team.getParticipant() : participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public Set<Node> getUserNodes()
  {
    Set<Node> nodes = new HashSet<Node>();
    if ( participant != null && !participant.getUserNodes().isEmpty() )
    {
      for ( UserNode userNode : participant.getUserNodes() )
      {
        nodes.add( userNode.getNode() );
      }
    }
    return nodes;
  }

  public StackStandingParticipant getRankingPax()
  {
    return rankingPax;
  }

  public void setRankingPax( StackStandingParticipant rankingPax )
  {
    this.rankingPax = rankingPax;
  }

}
