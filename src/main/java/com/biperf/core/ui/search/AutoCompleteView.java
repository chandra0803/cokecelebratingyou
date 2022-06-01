
package com.biperf.core.ui.search;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AutoCompleteView
{
  private String[] messages = {};
  private List<AutoCompleteNameIdView> nameIdView = new ArrayList<AutoCompleteNameIdView>();

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  @JsonProperty( "completions" )
  public List<AutoCompleteNameIdView> getNameIdView()
  {
    return nameIdView;
  }

  public void setNameIdView( List<AutoCompleteNameIdView> nameIdView )
  {
    this.nameIdView = nameIdView;
  }
}
