
package com.biperf.core.ui.search;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.BaseJsonView;
import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.value.participant.PaxCard;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaxSearchView extends BaseJsonView
{
  @JsonProperty
  public SearchViewHeaderInfo header = new SearchViewHeaderInfo( 0L );
  @JsonProperty( "participants" )
  public List<PaxCard> paxCards = new ArrayList<PaxCard>();

  @JsonProperty( "maxAllowedToRecognize" )
  public int maxAllowedToRecognize;
  @JsonProperty( "sourceType" )
  public String sourceType;

  public PaxSearchView()
  {
  }

  public PaxSearchView( List<PaxCard> paxcards, SearchViewHeaderInfo header )
  {
    this.paxCards = paxcards;
    this.header = header;
  }

  public PaxSearchView( List<PaxCard> paxcards, List<WebErrorMessage> message, SearchViewHeaderInfo header )
  {
    this.paxCards = paxcards;
    this.header = header;
    super.messages = message;
  }

  public SearchViewHeaderInfo getHeader()
  {
    return header;
  }

  public void setHeader( SearchViewHeaderInfo header )
  {
    this.header = header;
  }

  public List<PaxCard> getPaxCards()
  {
    return paxCards;
  }

  public void setPaxCards( List<PaxCard> paxCards )
  {
    this.paxCards = paxCards;
  }

  public int getMaxAllowedToRecognize()
  {
    return maxAllowedToRecognize;
  }

  public void setMaxAllowedToRecognize( int maxAllowedToRecognize )
  {
    this.maxAllowedToRecognize = maxAllowedToRecognize;
  }

  public String getSourceType()
  {
    return sourceType;
  }

  public void setSourceType( String sourceType )
  {
    this.sourceType = sourceType;
  }

}
