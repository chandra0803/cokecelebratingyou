
package com.biperf.core.ui.nomination;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NominationsInProgressViewObject
{

  private Long id;
  private int index;
  private String dateStarted;
  private String nominee;
  private String nominationPromotionName;
  private String editUrl;
  private String removeParams;
  private String clientState;

  public NominationsInProgressViewObject()
  {
  }

  public NominationsInProgressViewObject( Long id, int index, String dateStarted, String nominee, String nominationPromotionName, String editUrl, String removeParams )
  {
    super();
    this.id = id;
    this.index = index;
    this.dateStarted = dateStarted;
    this.nominee = nominee;
    this.nominationPromotionName = nominationPromotionName;
    this.editUrl = editUrl;
    this.removeParams = removeParams;
  }

  @JsonProperty( "id" )
  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  @JsonProperty( "index" )
  public int getIndex()
  {
    return index;
  }

  public void setIndex( int index )
  {
    this.index = index;
  }

  @JsonProperty( "dateStarted" )
  public String getDateStarted()
  {
    return dateStarted;
  }

  public void setDateStarted( String dateStarted )
  {
    this.dateStarted = dateStarted;
  }

  @JsonProperty( "nominee" )
  public String getNominee()
  {
    return nominee;
  }

  public void setNominee( String nominee )
  {
    this.nominee = nominee;
  }

  @JsonProperty( "nominationPromotionName" )
  public String getNominationPromotionName()
  {
    return nominationPromotionName;
  }

  public void setNominationPromotionName( String nominationPromotionName )
  {
    this.nominationPromotionName = nominationPromotionName;
  }

  @JsonProperty( "editUrl" )
  public String getEditUrl()
  {
    return editUrl;
  }

  public void setEditUrl( String editUrl )
  {
    this.editUrl = editUrl;
  }

  @JsonProperty( "removeParams" )
  public String getRemoveParams()
  {
    return removeParams;
  }

  public void setRemoveParams( String removeParams )
  {
    this.removeParams = removeParams;
  }

  @JsonProperty( "clientState" )
  public String getClientState()
  {
    return clientState;
  }

  public void setClientState( String clientState )
  {
    this.clientState = clientState;
  }

}
