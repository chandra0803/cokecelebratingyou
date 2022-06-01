
package com.biperf.core.value.nomination;

import java.sql.Timestamp;

public class NominationsInProgressValueObject
{

  private Long claimId;
  private Long promotionId;
  private Timestamp dateCreated;
  private String promotionName;
  /** Recipient name or team name */
  private String name;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Timestamp getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( Timestamp dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

}
