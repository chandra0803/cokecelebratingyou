
package com.biperf.core.value;

import java.math.BigDecimal;

public class CountryValueBean
{
  private String countryCode;
  private String countryName;
  private String cmAssetCode;
  private BigDecimal budgetMediaValue;
  private String awardbanqAbbrev;

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public BigDecimal getBudgetMediaValue()
  {
    return budgetMediaValue;
  }

  public void setBudgetMediaValue( BigDecimal budgetMediaValue )
  {
    this.budgetMediaValue = budgetMediaValue;
  }

  public String getAwardbanqAbbrev()
  {
    return awardbanqAbbrev;
  }

  public void setAwardbanqAbbrev( String awardbanqAbbrev )
  {
    this.awardbanqAbbrev = awardbanqAbbrev;
  }

}
