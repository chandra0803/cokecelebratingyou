
package com.biperf.core.value.client;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.value.contributor.Media;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude( JsonInclude.Include.NON_NULL )
@JsonPropertyOrder( { "commentId", "userInfo", "commentText", "commentLang", "isLiked", "numLikers", "levelOneComments", "media" } )
public class ActivityPod
{

  @JsonProperty( "commentId" )
  private Long commentId;
  @JsonProperty( "userInfo" )
  private List<UserInfo> userInfo = new ArrayList<UserInfo>();
  @JsonProperty( "commentText" )
  private String commentText;
  @JsonProperty( "commentLang" )
  private String commentLang;
  @JsonProperty( "isLiked" )
  private Boolean isLiked;
  @JsonProperty( "numLikers" )
  private Long numLikers;
  @JsonProperty( "levelOneComments" )
  private List<LevelOneComment> levelOneComments = new ArrayList<LevelOneComment>();
  @JsonProperty( "media" )
  private List<Medium> media = new ArrayList<Medium>();

  @JsonProperty( "commentId" )
  public Long getCommentId()
  {
    return commentId;
  }

  @JsonProperty( "commentId" )
  public void setCommentId( Long commentId )
  {
    this.commentId = commentId;
  }

  public ActivityPod withCommentId( Long commentId )
  {
    this.commentId = commentId;
    return this;
  }

  @JsonProperty( "userInfo" )
  public List<UserInfo> getUserInfo()
  {
    return userInfo;
  }

  @JsonProperty( "userInfo" )
  public void setUserInfo( List<UserInfo> userInfo )
  {
    this.userInfo = userInfo;
  }

  public ActivityPod withUserInfo( List<UserInfo> userInfo )
  {
    this.userInfo = userInfo;
    return this;
  }

  @JsonProperty( "commentText" )
  public String getCommentText()
  {
    return commentText;
  }

  @JsonProperty( "commentText" )
  public void setCommentText( String commentText )
  {
    this.commentText = commentText;
  }

  public ActivityPod withCommentText( String commentText )
  {
    this.commentText = commentText;
    return this;
  }

  @JsonProperty( "commentLang" )
  public String getCommentLang()
  {
    return commentLang;
  }

  @JsonProperty( "commentLang" )
  public void setCommentLang( String commentLang )
  {
    this.commentLang = commentLang;
  }

  public ActivityPod withCommentLang( String commentLang )
  {
    this.commentLang = commentLang;
    return this;
  }

  @JsonProperty( "isLiked" )
  public Boolean getIsLiked()
  {
    return isLiked;
  }

  @JsonProperty( "isLiked" )
  public void setIsLiked( Boolean isLiked )
  {
    this.isLiked = isLiked;
  }

  public ActivityPod withIsLiked( Boolean isLiked )
  {
    this.isLiked = isLiked;
    return this;
  }

  @JsonProperty( "numLikers" )
  public Long getNumLikers()
  {
    return numLikers;
  }

  @JsonProperty( "numLikers" )
  public void setNumLikers( Long numLikers )
  {
    this.numLikers = numLikers;
  }

  public ActivityPod withNumLikers( Long numLikers )
  {
    this.numLikers = numLikers;
    return this;
  }

  @JsonProperty( "levelOneComments" )
  public List<LevelOneComment> getLevelOneComments()
  {
    return levelOneComments;
  }

  @JsonProperty( "levelOneComments" )
  public void setLevelOneComments( List<LevelOneComment> levelOneComments )
  {
    this.levelOneComments = levelOneComments;
  }

  public ActivityPod withLevelOneComments( List<LevelOneComment> levelOneComments )
  {
    this.levelOneComments = levelOneComments;
    return this;
  }

  @JsonProperty( "media" )
  public List<Medium> getMedia()
  {
    return media;
  }

  @JsonProperty( "media" )
  public void setMedia( List<Medium> media )
  {
    this.media = media;
  }

  public ActivityPod withMedia( List<Medium> media )
  {
    this.media = media;
    return this;
  }

}
