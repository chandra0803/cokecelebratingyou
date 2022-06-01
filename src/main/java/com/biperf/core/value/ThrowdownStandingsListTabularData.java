
package com.biperf.core.value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.throwdown.Standing;
import com.biperf.core.domain.promotion.Round;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ThrowdownStandingsListTabularData
{
  private ThrowdownStandingsListMetaView meta = new ThrowdownStandingsListMetaView();
  private List<Standing> results = new ArrayList<Standing>();
  private Round round = null;

  public void setMeta( ThrowdownStandingsListMetaView meta )
  {
    this.meta = meta;
  }

  @JsonProperty( "meta" )
  public ThrowdownStandingsListMetaView getMeta()
  {
    return meta;
  }

  public void setResults( List<Standing> results )
  {
    this.results = results;
  }

  @JsonProperty( "results" )
  public List<Standing> getResults()
  {
    return results;
  }

  @JsonIgnore
  public void setRound( Round round )
  {
    this.round = round;
  }

  @JsonIgnore
  public Round getRound()
  {
    return round;
  }

  @JsonProperty( "roundStartDate" )
  public Date getRoundStartDate()
  {
    return round.getStartDate();
  }

  @JsonProperty( "roundEndDate" )
  public Date getRoundEndDate()
  {
    return round.getEndDate();
  }
}
