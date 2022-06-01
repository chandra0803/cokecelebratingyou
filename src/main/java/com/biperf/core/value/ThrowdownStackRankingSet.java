/*
 * (c) 2013 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/ThrowdownStackRankingSet.java,v $
 */

package com.biperf.core.value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author kothanda
 * @since Oct 8, 2013
 * @version 1.0
 */
public class ThrowdownStackRankingSet
{
  private String nameId;
  private String name;
  private String emptyMessage;
  private BaseJsonView rankings = new BaseJsonView();

  public String getNameId()
  {
    return nameId;
  }

  public void setNameId( String nameId )
  {
    this.nameId = nameId;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getEmptyMessage()
  {
    return emptyMessage;
  }

  public void setEmptyMessage( String emptyMessage )
  {
    this.emptyMessage = emptyMessage;
  }

  @JsonProperty( "rankings" )
  public BaseJsonView getRankings()
  {
    return rankings;
  }

  @JsonIgnore
  public void setRankings( BaseJsonView rankings )
  {
    this.rankings = rankings;
  }

}
