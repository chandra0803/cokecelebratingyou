
package com.biperf.core.ui.search;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchViewHeaderInfo
{
  @JsonProperty( "totalRecordsFound" )
  private Long totalRecordsFound;

  public SearchViewHeaderInfo()
  {
  }

  public SearchViewHeaderInfo( Long totalRecords )
  {
    this.totalRecordsFound = totalRecords;
  }

  public Long getTotalRecordsFound()
  {
    return totalRecordsFound;
  }

  public void setTotalRecordsFound( Long totalRecords )
  {
    this.totalRecordsFound = totalRecords;
  }
}
