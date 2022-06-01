
package com.biperf.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

//This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class MetaDataBean
{
  private Integer sortedOn;
  private String sortedBy;
  private Long maxRows;

  public MetaDataBean()
  {

  }

  public MetaDataBean( Integer sortedOn, String sortedBy, Long maxRows )
  {
    this.sortedOn = sortedOn;
    this.sortedBy = sortedBy;
    this.maxRows = maxRows;
  }

  public Integer getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( Integer sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public Long getMaxRows()
  {
    return maxRows;
  }

  public void setMaxRows( Long maxRows )
  {
    this.maxRows = maxRows;
  }

}
