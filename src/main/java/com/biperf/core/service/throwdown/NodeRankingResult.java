
package com.biperf.core.service.throwdown;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.hierarchy.Node;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.objectpartners.cms.util.ContentReaderManager;

public class NodeRankingResult implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Node node = null;
  private List<TeamRankingResult> teamResults = new ArrayList<TeamRankingResult>();

  public NodeRankingResult()
  {
  }

  public NodeRankingResult( Node node )
  {
    this.node = node;
  }

  @JsonIgnore
  public Node getNode()
  {
    return node;
  }

  @JsonIgnore
  public void setNode( Node node )
  {
    this.node = node;
  }

  @JsonIgnore
  public List<TeamRankingResult> getTeamResults()
  {
    return teamResults;
  }

  @JsonIgnore
  public void setTeamResults( List<TeamRankingResult> teamResults )
  {
    this.teamResults = teamResults;
  }

  public void addTeamResult( TeamRankingResult result )
  {
    this.teamResults.add( result );
  }

  // add this method to display nodeName in throwdownAwardSummaryRunCalculations page to get data
  // from json.
  public String getNodesNodeTypeName()
  {
    return node != null ? this.node.getNodeType().getNodeTypeName() : ContentReaderManager.getText( "system.general", "ALL" );
  }

  // add this method to display nodeName in throwdownAwardSummaryRunCalculations page to get data
  // from json.
  public String getNodeName()
  {
    return node != null ? this.node.getName() : ContentReaderManager.getText( "system.general", "ALL" );
  }

  public int getPointsIssuedForNode()
  {
    int points = 0;
    for ( TeamRankingResult teamResult : getTeamResults() )
    {
      points = points + teamResult.getPayoutAmount();
    }
    return points;
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof NodeRankingResult ) )
    {
      return false;
    }

    final NodeRankingResult nodeRanking = (NodeRankingResult)o;

    if ( getNode() != null ? !getNode().equals( nodeRanking.getNode() ) : nodeRanking.getNode() != null )
    {
      return false;
    }

    if ( getNode() == null && nodeRanking.getNode() == null )
    {
      return true;
    }

    return getNode().getId().equals( nodeRanking.getNode().getId() );
  }

  public int hashCode()
  {
    int result;
    result = getNode() != null ? getNode().getId().hashCode() : ContentReaderManager.getText( "system.general", "ALL" ).hashCode();
    return result;
  }

  public boolean isHierarchyRanking()
  {
    return node == null;
  }

}
