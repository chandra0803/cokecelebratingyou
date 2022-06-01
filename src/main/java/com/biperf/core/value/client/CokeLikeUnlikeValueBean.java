
package com.biperf.core.value.client;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CokeLikeUnlikeValueBean
{
  @JsonProperty( "messages" )
  private List<Object> messages = new ArrayList<Object>();
  @JsonProperty( "numLikes" )
  private Long numLikes;

  @JsonProperty( "messages" )
  public List<Object> getMessages()
  {
    return messages;
  }

  @JsonProperty( "messages" )
  public void setMessages( List<Object> messages )
  {
    this.messages = messages;
  }

  @JsonProperty( "numLikes" )
  public Long getNumLikes()
  {
    return numLikes;
  }

  @JsonProperty( "numLikes" )
  public void setNumLikes( Long numLikes )
  {
    this.numLikes = numLikes;
  }

}
