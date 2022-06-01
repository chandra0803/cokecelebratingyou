
package com.biperf.core.ui.publicrecognition;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PublicRecognitionLikeView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private Long numberOfLikers;

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  @JsonProperty( "numLikes" )
  public Long getNumberOfLikers()
  {
    return numberOfLikers;
  }

  public void setNumberOfLikers( Long numberOfLikers )
  {
    this.numberOfLikers = numberOfLikers;
  }

}
