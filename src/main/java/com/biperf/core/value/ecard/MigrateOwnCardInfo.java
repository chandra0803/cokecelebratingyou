
package com.biperf.core.value.ecard;

import java.io.Serializable;

public class MigrateOwnCardInfo implements Serializable
{

  private static final long serialVersionUID = 7702390385756086896L;
  private String imageName;
  private String cardData;
  private Long claimId;

  public String getImageName()
  {
    return imageName;
  }

  public void setImageName( String imageName )
  {
    this.imageName = imageName;
  }

  public String getCardData()
  {
    return cardData;
  }

  public void setCardData( String cardData )
  {
    this.cardData = cardData;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

}
