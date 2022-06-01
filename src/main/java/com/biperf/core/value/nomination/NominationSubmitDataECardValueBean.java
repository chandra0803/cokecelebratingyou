
package com.biperf.core.value.nomination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents ECard option within nomination submission wizard
 */
public class NominationSubmitDataECardValueBean
{
  private long id;
  private String name;
  private String smallImage;
  private String largeImage;
  private boolean canEdit;
  private String cardType;

  private boolean translatable;
  private String locale;

  public NominationSubmitDataECardValueBean()
  {
  }

  public NominationSubmitDataECardValueBean( long id, String name, String smallImage, String largeImage, boolean canEdit, String cardType, boolean translatable, String locale )
  {
    this.id = id;
    this.name = name;
    this.smallImage = smallImage;
    this.largeImage = largeImage;
    this.canEdit = canEdit;
    this.cardType = cardType;
    this.translatable = translatable;
    this.locale = locale;
  }

  @JsonProperty( "id" )
  public long getId()
  {
    return id;
  }

  public void setId( long id )
  {
    this.id = id;
  }

  @JsonProperty( "name" )
  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  @JsonProperty( "smallImage" )
  public String getSmallImage()
  {
    return smallImage;
  }

  public void setSmallImage( String smallImage )
  {
    this.smallImage = smallImage;
  }

  @JsonProperty( "largeImage" )
  public String getLargeImage()
  {
    return largeImage;
  }

  public void setLargeImage( String largeImage )
  {
    this.largeImage = largeImage;
  }

  @JsonProperty( "canEdit" )
  public boolean isCanEdit()
  {
    return canEdit;
  }

  public void setCanEdit( boolean canEdit )
  {
    this.canEdit = canEdit;
  }

  @JsonProperty( "cardType" )
  public String getCardType()
  {
    return cardType;
  }

  public void setCardType( String cardType )
  {
    this.cardType = cardType;
  }

  @JsonIgnore
  public boolean isTranslatable()
  {
    return translatable;
  }

  public void setTranslatable( boolean translatable )
  {
    this.translatable = translatable;
  }

  @JsonIgnore
  public String getLocale()
  {
    return locale;
  }

  public void setLocale( String locale )
  {
    this.locale = locale;
  }

}
