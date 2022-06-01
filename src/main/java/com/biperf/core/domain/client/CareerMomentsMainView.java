/*
 * (c) 2020 BI, Inc.  All rights reserved.
 * $Source$
 */
package com.biperf.core.domain.client;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO Javadoc for CareerMomentsMainView.
 * 
 * @author yelamanc
 * @since Feb 28, 2020
 * @version 1.0
 */
public class CareerMomentsMainView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private List<CareerMomentsSets> celebrationSets = new ArrayList<CareerMomentsSets>();

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  @JsonProperty( "celebrationSets" )
  public List<CareerMomentsSets> getCelebrationSets()
  {
    return celebrationSets;
  }

  public void setCelebrationSets( List<CareerMomentsSets> celebrationSets )
  {
    this.celebrationSets = celebrationSets;
  }

}
