/**
 * 
 */

package com.biperf.core.domain.purl;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author poddutur
 *
 */
public class PurlCelebrateMainView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private List<PurlCelebrationSet> celebrationSets = new ArrayList<PurlCelebrationSet>();

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  @JsonProperty( "celebrationSets" )
  public List<PurlCelebrationSet> getCelebrationSets()
  {
    return celebrationSets;
  }

  public void setCelebrationSets( List<PurlCelebrationSet> celebrationSets )
  {
    this.celebrationSets = celebrationSets;
  }
}
