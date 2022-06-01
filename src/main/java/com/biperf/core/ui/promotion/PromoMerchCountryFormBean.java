
package com.biperf.core.ui.promotion;

import java.io.Serializable;

public class PromoMerchCountryFormBean implements Serializable
{
  private Long promoMerchCountryId;
  private Long countryId;
  private String countryName;
  private String programId;

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public Long getCountryId()
  {
    return countryId;
  }

  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

  public Long getPromoMerchCountryId()
  {
    return promoMerchCountryId;
  }

  public void setPromoMerchCountryId( Long promoMerchCountryId )
  {
    this.promoMerchCountryId = promoMerchCountryId;
  }
}
