
package com.biperf.core.ui.search;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.value.participant.PaxCard;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HeroPaxSearchView extends PaxSearchView
{

  public HeroPaxSearchView()
  {
    super();
  }

  public HeroPaxSearchView( List<PaxCard> paxcards, SearchViewHeaderInfo header )
  {
    super( paxcards, header );
  }

  public HeroPaxSearchView( List<PaxCard> paxcards, List<WebErrorMessage> message, SearchViewHeaderInfo header )
  {
    super( paxcards, message, header );
  }

  public HeroPaxSearchView( PaxSearchView paxSearchView )
  {
    super( paxSearchView.getPaxCards(), paxSearchView.getHeader() );
    super.maxAllowedToRecognize = paxSearchView.getMaxAllowedToRecognize();
    super.sourceType = paxSearchView.getSourceType();

  }

  @JsonProperty
  public List<SearchFilterTypeCountView> searchFilterTypeCounts = new ArrayList<SearchFilterTypeCountView>();

  public List<SearchFilterTypeCountView> getSearchFilterTypeCounts()
  {
    return searchFilterTypeCounts;
  }

  public void setSearchFilterTypeCounts( List<SearchFilterTypeCountView> searchFilterTypeCounts )
  {
    this.searchFilterTypeCounts = searchFilterTypeCounts;
  }
}
