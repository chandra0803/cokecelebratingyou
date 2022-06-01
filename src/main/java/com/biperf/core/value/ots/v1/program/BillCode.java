
package com.biperf.core.value.ots.v1.program;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class BillCode
{
  private long id;
  private long sortOrder;
  private String billCode;
  private String customValue;

  public long getId()
  {
    return id;
  }

  public void setId( long id )
  {
    this.id = id;
  }

  public long getSortOrder()
  {
    return sortOrder;
  }

  public void setSortOrder( long sortOrder )
  {
    this.sortOrder = sortOrder;
  }

  public String getBillCode()
  {
    return billCode;
  }

  public void setBillCode( String billCode )
  {
    this.billCode = billCode;
  }

  public String getCustomValue()
  {
    return customValue;
  }

  public void setCustomValue( String customValue )
  {
    this.customValue = customValue;
  }
}
