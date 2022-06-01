/**
 * 
 */

package com.biperf.core.domain.forum;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author poddutur
 *
 */
// This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class ForumLikeCountView
{
  private String[] messages = {};
  @JsonProperty( "numberOfLikes" )
  private Long numberOfLikes;

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public Long getNumberOfLikes()
  {
    return numberOfLikes;
  }

  public void setNumberOfLikes( Long numberOfLikes )
  {
    this.numberOfLikes = numberOfLikes;
  }

}
