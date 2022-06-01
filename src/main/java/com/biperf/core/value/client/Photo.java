
package com.biperf.core.value.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonPropertyOrder( { "src", "thumbSrc" } )
public class Photo
{

  @JsonProperty( "src" )
  private String src;
  @JsonProperty( "thumbSrc" )
  private String thumbSrc;

  @JsonProperty( "src" )
  public String getSrc()
  {
    return src;
  }

  @JsonProperty( "src" )
  public void setSrc( String src )
  {
    this.src = src;
  }

  @JsonProperty( "thumbSrc" )
  public String getThumbSrc()
  {
    return thumbSrc;
  }

  @JsonProperty( "thumbSrc" )
  public void setThumbSrc( String thumbSrc )
  {
    this.thumbSrc = thumbSrc;
  }

}
