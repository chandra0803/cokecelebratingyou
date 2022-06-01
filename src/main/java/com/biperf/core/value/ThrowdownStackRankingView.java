
package com.biperf.core.value;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class ThrowdownStackRankingView extends BaseJsonView
{
  private static final long serialVersionUID = 1L;

  private String[] messages = {};
  private String rankingsUrl = null;
  private String rulesUrl = null;
  private List<ThrowdownStackRankingSet> nodeTypeSets = new ArrayList<ThrowdownStackRankingSet>();

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public static ThrowdownStackRankingView getViewForNoRankings()
  {
    ThrowdownStackRankingView rankingView = new ThrowdownStackRankingView();
    rankingView.setVisible( false );
    return rankingView;
  }

  @JsonProperty( "nodeTypeSets" )
  public List<ThrowdownStackRankingSet> getNodeTypeSets()
  {
    return nodeTypeSets;
  }

  @JsonIgnore
  public void setNodeTypeSets( List<ThrowdownStackRankingSet> nodeTypeSets )
  {
    this.nodeTypeSets = nodeTypeSets;
  }

  @JsonProperty( "rankingsUrl" )
  public String getRankingsUrl()
  {
    return rankingsUrl;
  }

  @JsonIgnore
  public void setRankingsUrl( String rankingsUrl )
  {
    this.rankingsUrl = rankingsUrl;
  }

  @JsonIgnore
  public Long getNodeId()
  {
    for ( ThrowdownStackRankingSet nodeTypeSet : nodeTypeSets )
    {
      if ( nodeTypeSet.getRankings() instanceof ThrowdownStackRanking )
      {
        return ( (ThrowdownStackRanking)nodeTypeSet.getRankings() ).getNodeId();
      }
    }
    return null;
  }

  @JsonIgnore
  public Long getNodeTypeId()
  {
    for ( ThrowdownStackRankingSet nodeTypeSet : nodeTypeSets )
    {
      if ( nodeTypeSet.getRankings() instanceof ThrowdownStackRanking )
      {
        return ( (ThrowdownStackRanking)nodeTypeSet.getRankings() ).getNodeTypeId();
      }
    }
    return null;
  }

  @JsonIgnore
  public ThrowdownStackRanking getRankings()
  {
    for ( ThrowdownStackRankingSet nodeTypeSet : nodeTypeSets )
    {
      if ( nodeTypeSet.getRankings() instanceof ThrowdownStackRanking )
      {
        return (ThrowdownStackRanking)nodeTypeSet.getRankings();
      }
    }
    return null;
  }

  public String getRulesUrl()
  {
    return rulesUrl;
  }

  public void setRulesUrl( String rulesUrl )
  {
    this.rulesUrl = rulesUrl;
    for ( ThrowdownStackRankingSet nodeTypeSet : nodeTypeSets )
    {
      if ( nodeTypeSet.getRankings() instanceof ThrowdownStackRanking )
      {
        ( (ThrowdownStackRanking)nodeTypeSet.getRankings() ).setRules( rulesUrl );
      }
    }
  }

}
