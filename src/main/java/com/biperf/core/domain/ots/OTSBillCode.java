
package com.biperf.core.domain.ots;

import com.biperf.core.value.ots.v1.program.Batch;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class OTSBillCode 
{

  private Long sortOrder;
  private String billCode;
  private String customValue;
  private long id;

  public Long getSortOrder()
  {
    return sortOrder;
  }

  public void setSortOrder( Long sortOrder )
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

  public long getId()
  {
    return id;
  }

  public void setId( long id )
  {
    this.id = id;
  }

  public OTSBillCode()
  {
    super();
  }

  public OTSBillCode( OTSBillCode toCopy )
  {
    this.setBillCode( toCopy.getBillCode() );
    this.setCustomValue( toCopy.getCustomValue() );
    this.setSortOrder( toCopy.getSortOrder() );
  }

  public OTSBillCode( Batch batch, Long sortOrder, String billCode, String customValue )
  {
    
    this.setSortOrder( sortOrder );
    this.setBillCode( billCode );
    this.setCustomValue( customValue );
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof OTSBillCode ) )
    {
      return false;
    }

    return super.equals( object );
  }

  public int hashCode()
  {
    return super.hashCode();
  }

 
}
