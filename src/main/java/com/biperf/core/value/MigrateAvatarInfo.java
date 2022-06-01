/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.io.Serializable;

/**
 * 
 * @author sivanand
 * @since Jan 30, 2019
 * @version 1.0
 */
public class MigrateAvatarInfo implements Serializable
{
  private Long participantId;
  private String avatarOriginal;
  private String avatarSmall;
  private String contentType;
  private String avatarOrgUrl;
  private String avatarsmallUrl;
  private String imageName;

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

  public String getContentType()
  {
    return contentType;
  }

  public void setContentType( String contentType )
  {
    this.contentType = contentType;
  }

  public String getAvatarOrgUrl()
  {
    return avatarOrgUrl;
  }

  public void setAvatarOrgUrl( String avatarOrgUrl )
  {
    this.avatarOrgUrl = avatarOrgUrl;
  }

  public String getAvatarsmallUrl()
  {
    return avatarsmallUrl;
  }

  public void setAvatarsmallUrl( String avatarsmallUrl )
  {
    this.avatarsmallUrl = avatarsmallUrl;
  }

  public String getImageName()
  {
    return imageName;
  }

  public void setImageName( String imageName )
  {
    this.imageName = imageName;
  }

  public String getAvatarOriginal()
  {
    return avatarOriginal;
  }

  public void setAvatarOriginal( String avatarOriginal )
  {
    this.avatarOriginal = avatarOriginal;
  }

  public String getAvatarSmall()
  {
    return avatarSmall;
  }

  public void setAvatarSmall( String avatarSmall )
  {
    this.avatarSmall = avatarSmall;
  }

}
