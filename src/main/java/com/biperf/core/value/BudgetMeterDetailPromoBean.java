
package com.biperf.core.value;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BudgetMeterDetailPromoBean implements Comparable<BudgetMeterDetailPromoBean>
{
  private Long promoId;
  private String promoName;
  private Date promoStartDate;
  private Date promoEndDate;
  private String url;
  private String budgetOwner;
  private boolean cheersPromotion;

  public void setPromoId( Long promoId )
  {
    this.promoId = promoId;
  }

  @JsonProperty( "id" )
  public Long getPromoId()
  {
    return promoId;
  }

  public void setPromoName( String promoName )
  {
    this.promoName = promoName;
  }

  @JsonProperty( "name" )
  public String getPromoName()
  {
    return promoName;
  }

  public void setPromoStartDate( Date promoStartDate )
  {
    this.promoStartDate = promoStartDate;
  }

  @JsonIgnore
  public Date getPromoStartDate()
  {
    return promoStartDate;
  }

  public void setPromoEndDate( Date promoEndDate )
  {
    this.promoEndDate = promoEndDate;
  }

  @JsonIgnore
  public Date getPromoEndDate()
  {
    return promoEndDate;
  }

  @JsonIgnore
  @Override
  public int compareTo( BudgetMeterDetailPromoBean other )
  {
    return this.getPromoName().compareTo( other.getPromoName() );
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }
  
  public String getBudgetOwner() 
  {
    return budgetOwner;
  }

  public void setBudgetOwner(String budgetOwner)
  {
    this.budgetOwner = budgetOwner;
  }

  @JsonProperty( "cheersPromotion" )
  public boolean getCheersPromotion()
  {
    return cheersPromotion;
  }

  public void setCheersPromotion( boolean cheersPromotion )
  {
    this.cheersPromotion = cheersPromotion;
  }

}
