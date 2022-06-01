
package com.biperf.core.domain.client;

import java.util.Objects;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;

public class ClientProfileLike extends BaseDomain
{
  private static final long serialVersionUID = 2774761138881503028L;
  private Long profileUserId;
  private Participant likedUser;
  private Long paxAboutMeId;
  private Long profileCommentId;
  private Long subLikeId;

  public Long getProfileUserId()
  {
    return profileUserId;
  }

  public void setProfileUserId( Long profileUserId )
  {
    this.profileUserId = profileUserId;
  }

  public Participant getLikedUser()
  {
    return likedUser;
  }

  public void setLikedUser( Participant likedUser )
  {
    this.likedUser = likedUser;
  }

  public Long getPaxAboutMeId()
  {
    return paxAboutMeId;
  }

  public void setPaxAboutMeId( Long paxAboutMeId )
  {
    this.paxAboutMeId = paxAboutMeId;
  }

  public Long getProfileCommentId()
  {
    return profileCommentId;
  }

  public void setProfileCommentId( Long profileCommentId )
  {
    this.profileCommentId = profileCommentId;
  }

  public Long getSubLikeId()
  {
    return subLikeId;
  }

  public void setSubLikeId( Long subLikeId )
  {
    this.subLikeId = subLikeId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return Objects.hash( likedUser, paxAboutMeId, profileCommentId, profileUserId, subLikeId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
      return true;
    if ( ! ( obj instanceof ClientProfileLike ) )
      return false;
    ClientProfileLike other = (ClientProfileLike)obj;
    return Objects.equals( likedUser, other.likedUser ) && Objects.equals( paxAboutMeId, other.paxAboutMeId ) && Objects.equals( profileCommentId, other.profileCommentId )
        && Objects.equals( profileUserId, other.profileUserId ) && Objects.equals( subLikeId, other.subLikeId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "ClientProfileLike [profileUserId=" + profileUserId + ", likedUser=" + likedUser + ", paxAboutMeId=" + paxAboutMeId + ", profileCommentId=" + profileCommentId + ", subLikeId=" + subLikeId
        + "]";
  }

}
