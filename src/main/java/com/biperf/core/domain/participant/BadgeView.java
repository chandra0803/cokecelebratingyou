
package com.biperf.core.domain.participant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class BadgeView
{
  private Long id;
  @JsonProperty( "img" )
  private String badgeImageUrl;
  private String name;
  @JsonIgnore
  private String badgeLibCmKey;

  public BadgeView()
  {
  }

  public BadgeView( Long id, String name, String badgeLibCmKey )
  {
    this.id = id;
    this.name = name;
    this.badgeLibCmKey = badgeLibCmKey;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getBadgeImageUrl()
  {
    return badgeImageUrl;
  }

  public void setBadgeImageUrl( String badgeImageUrl )
  {
    this.badgeImageUrl = badgeImageUrl;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getBadgeLibCmKey()
  {
    return badgeLibCmKey;
  }

  public void setBadgeLibCmKey( String badgeLibCmKey )
  {
    this.badgeLibCmKey = badgeLibCmKey;
  }

}
