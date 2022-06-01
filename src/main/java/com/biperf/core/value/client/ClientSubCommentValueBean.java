
package com.biperf.core.value.client;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonPropertyOrder( { "messages", "comment" } )
public class ClientSubCommentValueBean
{

  @JsonProperty( "messages" )
  private List<Object> messages = new ArrayList<Object>();
  @JsonProperty( "comment" )
  private Comment comment;

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

  @JsonProperty( "comment" )
  public Comment getComment()
  {
    return comment;
  }

  @JsonProperty( "comment" )
  public void setComment( Comment comment )
  {
    this.comment = comment;
  }

}
