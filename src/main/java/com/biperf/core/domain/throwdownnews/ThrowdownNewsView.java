
package com.biperf.core.domain.throwdownnews;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

//This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class ThrowdownNewsView
{
  // This is for future use to keep additional messages
  private String[] messages = {};
  private List<ThrowdownNewsDetailsView> communications;

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public List<ThrowdownNewsDetailsView> getCommunications()
  {
    return communications;
  }

  public void setCommunications( List<ThrowdownNewsDetailsView> communications )
  {
    this.communications = communications;
  }

}
