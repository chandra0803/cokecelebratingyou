
package com.biperf.core.domain.client;

import java.util.Objects;

import com.biperf.core.domain.BaseDomain;

public class ClientProfileComment extends BaseDomain
{
  private static final long serialVersionUID = 5756195539471666107L;
  private Long profileUserId;
  private Long commenterUserId;
  private String comments;
  private String commentLanguageId;
  private int sequenceNum;
  private String imageUrl;
  private String imageUrlThumb;
  private String videoUrl;
  private Long profileSubCommentId;

  public Long getProfileUserId()
  {
    return profileUserId;
  }

  public void setProfileUserId( Long profileUserId )
  {
    this.profileUserId = profileUserId;
  }

  public Long getCommenterUserId()
  {
    return commenterUserId;
  }

  public void setCommenterUserId( Long commenterUserId )
  {
    this.commenterUserId = commenterUserId;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public String getCommentLanguageId()
  {
    return commentLanguageId;
  }

  public void setCommentLanguageId( String commentLanguageId )
  {
    this.commentLanguageId = commentLanguageId;
  }

  public int getSequenceNum()
  {
    return sequenceNum;
  }

  public void setSequenceNum( int sequenceNum )
  {
    this.sequenceNum = sequenceNum;
  }

  public String getImageUrl()
  {
    return imageUrl;
  }

  public void setImageUrl( String imageUrl )
  {
    this.imageUrl = imageUrl;
  }

  public String getImageUrlThumb()
  {
    return imageUrlThumb;
  }

  public void setImageUrlThumb( String imageUrlThumb )
  {
    this.imageUrlThumb = imageUrlThumb;
  }

  public String getVideoUrl()
  {
    return videoUrl;
  }

  public void setVideoUrl( String videoUrl )
  {
    this.videoUrl = videoUrl;
  }

  public Long getProfileSubCommentId()
  {
    return profileSubCommentId;
  }

  public void setProfileSubCommentId( Long profileSubCommentId )
  {
    this.profileSubCommentId = profileSubCommentId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return Objects.hash( commentLanguageId, commenterUserId, comments, imageUrl, imageUrlThumb, profileSubCommentId, profileUserId, sequenceNum, videoUrl );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
      return true;
    if ( ! ( obj instanceof ClientProfileComment ) )
      return false;
    ClientProfileComment other = (ClientProfileComment)obj;
    return Objects.equals( commentLanguageId, other.commentLanguageId ) && Objects.equals( commenterUserId, other.commenterUserId ) && Objects.equals( comments, other.comments )
        && Objects.equals( imageUrl, other.imageUrl ) && Objects.equals( imageUrlThumb, other.imageUrlThumb ) && Objects.equals( profileSubCommentId, other.profileSubCommentId )
        && Objects.equals( profileUserId, other.profileUserId ) && sequenceNum == other.sequenceNum && Objects.equals( videoUrl, other.videoUrl );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "ClientProfileComment [profileUserId=" + profileUserId + ", commenterUserId=" + commenterUserId + ", comments=" + comments + ", commentLanguageId=" + commentLanguageId + ", sequenceNum="
        + sequenceNum + ", imageUrl=" + imageUrl + ", imageUrlThumb=" + imageUrlThumb + ", videoUrl=" + videoUrl + ", profileSubCommentId=" + profileSubCommentId + "]";
  }

}
