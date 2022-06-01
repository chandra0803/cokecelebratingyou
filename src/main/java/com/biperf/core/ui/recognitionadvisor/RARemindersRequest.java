
package com.biperf.core.ui.recognitionadvisor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings( "serial" )
@JsonInclude( value = Include.NON_EMPTY )
public class RARemindersRequest implements Serializable
{
  @JsonProperty( "rowNumStart" )
  private String rowNumStart;

  @JsonProperty( "rowNumEnd" )
  private String rowNumEnd;

  @JsonProperty( "activePage" )
  private String activePage;

  @JsonProperty( "sortColName" )
  private String sortColName;

  @JsonProperty( "sortedBy" )
  private String sortedBy;

  @JsonProperty( "excludeUpcoming" )
  private String excludeUpcoming;

  @JsonProperty( "filterValue" )
  private String filterValue;

  @JsonProperty( "pendingStatus" )
  private String pendingStatus;

  public String getRowNumStart()
  {
    return rowNumStart;
  }

  public void setRowNumStart( String rowNumStart )
  {
    this.rowNumStart = rowNumStart;
  }

  public String getRowNumEnd()
  {
    return rowNumEnd;
  }

  public void setRowNumEnd( String rowNumEnd )
  {
    this.rowNumEnd = rowNumEnd;
  }

  public String getSortColName()
  {
    return sortColName;
  }

  public void setSortColName( String sortColName )
  {
    this.sortColName = sortColName;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public String getExcludeUpcoming()
  {
    return excludeUpcoming;
  }

  public void setExcludeUpcoming( String excludeUpcoming )
  {
    this.excludeUpcoming = excludeUpcoming;
  }

  public String getFilterValue()
  {
    return filterValue;
  }

  public void setFilterValue( String filterValue )
  {
    this.filterValue = filterValue;
  }

  public String getActivePage()
  {
    return activePage;
  }

  public void setActivePage( String activePage )
  {
    this.activePage = activePage;
  }

  public String getPendingStatus()
  {
    return pendingStatus;
  }

  public void setPendingStatus( String pendingStatus )
  {
    this.pendingStatus = pendingStatus;
  }

}
