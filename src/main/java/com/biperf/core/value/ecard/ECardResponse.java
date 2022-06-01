
package com.biperf.core.value.ecard;

import org.json.simple.JSONArray;

public class ECardResponse
{

  private String companyId;
  private JSONArray images = new JSONArray();
  private String cardName;
  private String visibility;
  private float cardId;
  private String cardTitle;
  private String status;
  private float migrateId;

  // Getter Methods

  public String getCompanyId()
  {
    return companyId;
  }

  public String getCardName()
  {
    return cardName;
  }

  public String getVisibility()
  {
    return visibility;
  }

  public float getCardId()
  {
    return cardId;
  }

  public String getCardTitle()
  {
    return cardTitle;
  }

  public String getStatus()
  {
    return status;
  }

  // Setter Methods

  public void setCompanyId( String companyId )
  {
    this.companyId = companyId;
  }

  public void setCardName( String cardName )
  {
    this.cardName = cardName;
  }

  public void setVisibility( String visibility )
  {
    this.visibility = visibility;
  }

  public void setCardId( float cardId )
  {
    this.cardId = cardId;
  }

  public void setCardTitle( String cardTitle )
  {
    this.cardTitle = cardTitle;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public JSONArray getImages()
  {
    return images;
  }

  public void setImages( JSONArray images )
  {
    this.images = images;
  }

  public float getMigrateId()
  {
    return migrateId;
  }

  public void setMigrateId( float migrateId )
  {
    this.migrateId = migrateId;
  }

}
