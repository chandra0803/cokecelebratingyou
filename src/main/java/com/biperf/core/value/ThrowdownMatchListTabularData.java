
package com.biperf.core.value;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ThrowdownMatchListTabularData
{
  private ThrowdownMatchListMetaView meta;

  public void setMeta( ThrowdownMatchListMetaView meta )
  {
    this.meta = meta;
  }

  @JsonProperty( "meta" )
  public ThrowdownMatchListMetaView getMeta()
  {
    return meta;
  }
}
