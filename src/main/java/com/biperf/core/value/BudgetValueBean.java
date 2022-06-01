
package com.biperf.core.value;

import java.math.BigDecimal;

public class BudgetValueBean
{

  private Long id;
  private String name;
  private BigDecimal remaining;
  private BigDecimal originalValue;
  private boolean hardCap;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public BigDecimal getRemaining()
  {
    return remaining;
  }

  public void setRemaining( BigDecimal remaining )
  {
    this.remaining = remaining;
  }

  public BigDecimal getOriginalValue()
  {
    return originalValue;
  }

  public void setOriginalValue( BigDecimal originalValue )
  {
    this.originalValue = originalValue;
  }

  public boolean isHardCap()
  {
    return hardCap;
  }

  public void setHardCap( boolean hardCap )
  {
    this.hardCap = hardCap;
  }

}
