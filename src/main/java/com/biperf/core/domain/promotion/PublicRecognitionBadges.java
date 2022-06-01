
package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;

/**
 * 
 * This class is used to populate the recognition badges
 * 
 * @author dudyala
 * @since Oct 3, 2016
 * @version 1.0
 */
public class PublicRecognitionBadges extends BaseDomain
{
  private static final long serialVersionUID = -5993159699796991073L;
  private Long teamId;
  private String badgeName;
  private String badgeUrl;

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

  public String getBadgeName()
  {
    return badgeName;
  }

  public void setBadgeName( String badgeName )
  {
    this.badgeName = badgeName;
  }

  public String getBadgeUrl()
  {
    return badgeUrl;
  }

  public void setBadgeUrl( String badgeUrl )
  {
    this.badgeUrl = badgeUrl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( badgeName == null ? 0 : badgeName.hashCode() );
    result = prime * result + ( badgeUrl == null ? 0 : badgeUrl.hashCode() );
    result = prime * result + ( teamId == null ? 0 : teamId.hashCode() );
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    PublicRecognitionBadges other = (PublicRecognitionBadges)obj;
    if ( badgeName == null )
    {
      if ( other.badgeName != null )
      {
        return false;
      }
    }
    else if ( !badgeName.equals( other.badgeName ) )
    {
      return false;
    }
    if ( badgeUrl == null )
    {
      if ( other.badgeUrl != null )
      {
        return false;
      }
    }
    else if ( !badgeUrl.equals( other.badgeUrl ) )
    {
      return false;
    }
    if ( teamId == null )
    {
      if ( other.teamId != null )
      {
        return false;
      }
    }
    else if ( !teamId.equals( other.teamId ) )
    {
      return false;
    }
    return true;
  }
}
