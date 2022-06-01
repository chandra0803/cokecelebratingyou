
package com.biperf.core.domain.forum;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

//This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class ForumCommentsTileView
{
  @JsonProperty( "totalNumberOfReplies" )
  private int totalNumberOfReplies;
  @JsonProperty( "repliesPerPage" )
  private int repliesPerPage;
  @JsonProperty( "page" )
  private int page;
  private List<CommentsView> commentsView = new ArrayList<CommentsView>();

  @JsonProperty( "comments" )
  public List<CommentsView> getCommentsView()
  {
    return commentsView;
  }

  public void setCommentsView( List<CommentsView> commentsView )
  {
    this.commentsView = commentsView;
  }

  public int getTotalNumberOfReplies()
  {
    return totalNumberOfReplies;
  }

  public void setTotalNumberOfReplies( int totalNumberOfReplies )
  {
    this.totalNumberOfReplies = totalNumberOfReplies;
  }

  public int getRepliesPerPage()
  {
    return repliesPerPage;
  }

  public void setRepliesPerPage( int repliesPerPage )
  {
    this.repliesPerPage = repliesPerPage;
  }

  public int getPage()
  {
    return page;
  }

  public void setPage( int page )
  {
    this.page = page;
  }

  public static class CommentsView
  {
    private Long id;
    private CommenterInfo commenterInfo;
    @JsonProperty( "numberOfLikes" )
    private Long numberOfLikes;
    @JsonProperty( "isLiked" )
    private Boolean isLiked;
    @JsonProperty( "likedIds" )
    private String likedIds;
    @JsonProperty( "comment" )
    private String comment;
    @JsonProperty( "date" )
    private String datePosted;
    @JsonProperty( "time" )
    private String timePosted;

    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    @JsonProperty( "commenter" )
    public CommenterInfo getCommenterInfo()
    {
      return commenterInfo;
    }

    public void setCommenterInfo( CommenterInfo commenterInfo )
    {
      this.commenterInfo = commenterInfo;
    }

    public Long getNumberOfLikes()
    {
      return numberOfLikes;
    }

    public void setNumberOfLikes( Long numberOfLikes )
    {
      this.numberOfLikes = numberOfLikes;
    }

    public Boolean getIsLiked()
    {
      return isLiked;
    }

    public void setIsLiked( Boolean isLiked )
    {
      this.isLiked = isLiked;
    }

    public String getLikedIds()
    {
      return likedIds;
    }

    public void setLikedIds( String likedIds )
    {
      this.likedIds = likedIds;
    }

    public String getComment()
    {
      return comment;
    }

    public void setComment( String comment )
    {
      this.comment = comment;
    }

    public String getDatePosted()
    {
      return datePosted;
    }

    public void setDatePosted( String datePosted )
    {
      this.datePosted = datePosted;
    }

    public String getTimePosted()
    {
      return timePosted;
    }

    public void setTimePosted( String timePosted )
    {
      this.timePosted = timePosted;
    }

  }

}
