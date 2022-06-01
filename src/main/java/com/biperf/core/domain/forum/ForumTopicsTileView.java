/**
 * 
 */

package com.biperf.core.domain.forum;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author poddutur
 *
 */
// This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class ForumTopicsTileView
{
  private List<DiscussionsTileView> discussionsTileView = new ArrayList<DiscussionsTileView>();

  public ForumTopicsTileView()
  {
    super();
  }

  @JsonProperty( "discussions" )
  public List<DiscussionsTileView> getDiscussionsTileView()
  {
    return discussionsTileView;
  }

  public void setDiscussionsTileView( List<DiscussionsTileView> discussionsTileView )
  {
    this.discussionsTileView = discussionsTileView;
  }

  public static class DiscussionsTileView
  {
    private AuthorTileView authorTileView;
    @JsonProperty( "topicId" )
    private Long topicId;
    @JsonProperty( "topicName" )
    private String topicName;
    @JsonProperty( "name" )
    private String discussionName;
    @JsonProperty( "id" )
    private Long discussionId;
    @JsonProperty( "text" )
    private String discussionBody;
    @JsonProperty( "numberOfLikes" )
    private Long numberOfLikes;
    @JsonProperty( "numberOfReplies" )
    private int numberOfReplies;
    @JsonProperty( "isLiked" )
    private Boolean isLiked;
    @JsonProperty( "date" )
    private String dateCreated;
    @JsonProperty( "time" )
    private String timeCreated;
    private List<CommentsView> commentsViewList = new ArrayList<CommentsView>();
    @JsonProperty( "multipleComments" )
    private boolean multipleComments;

    public DiscussionsTileView()
    {
      super();
    }

    @JsonProperty( "author" )
    public AuthorTileView getAuthorTileView()
    {
      return authorTileView;
    }

    public void setAuthorTileView( AuthorTileView authorTileView )
    {
      this.authorTileView = authorTileView;
    }

    @JsonProperty( "comments" )
    public List<CommentsView> getCommentsViewList()
    {
      return commentsViewList;
    }

    public void setCommentsViewList( List<CommentsView> commentsViewList )
    {
      this.commentsViewList = commentsViewList;
    }

    public Long getTopicId()
    {
      return topicId;
    }

    public void setTopicId( Long topicId )
    {
      this.topicId = topicId;
    }

    public String getTopicName()
    {
      return topicName;
    }

    public void setTopicName( String topicName )
    {
      this.topicName = topicName;
    }

    public String getDiscussionName()
    {
      return discussionName;
    }

    public void setDiscussionName( String discussionName )
    {
      this.discussionName = discussionName;
    }

    public Long getDiscussionId()
    {
      return discussionId;
    }

    public void setDiscussionId( Long discussionId )
    {
      this.discussionId = discussionId;
    }

    public String getDiscussionBody()
    {
      return discussionBody;
    }

    public void setDiscussionBody( String discussionBody )
    {
      this.discussionBody = discussionBody;
    }

    public Long getNumberOfLikes()
    {
      return numberOfLikes;
    }

    public void setNumberOfLikes( Long numberOfLikes )
    {
      this.numberOfLikes = numberOfLikes;
    }

    public int getNumberOfReplies()
    {
      return numberOfReplies;
    }

    public void setNumberOfReplies( int numberOfReplies )
    {
      this.numberOfReplies = numberOfReplies;
    }

    public Boolean getIsLiked()
    {
      return isLiked;
    }

    public void setIsLiked( Boolean isLiked )
    {
      this.isLiked = isLiked;
    }

    public String getDateCreated()
    {
      return dateCreated;
    }

    public void setDateCreated( String dateCreated )
    {
      this.dateCreated = dateCreated;
    }

    public String getTimeCreated()
    {
      return timeCreated;
    }

    public void setTimeCreated( String timeCreated )
    {
      this.timeCreated = timeCreated;
    }

    public boolean isMultipleComments()
    {
      return multipleComments;
    }

    public void setMultipleComments( boolean multipleComments )
    {
      this.multipleComments = multipleComments;
    }

    public static class AuthorTileView
    {
      @JsonProperty( "id" )
      private Long id;
      @JsonProperty( "firstName" )
      private String firstName;
      @JsonProperty( "lastName" )
      private String lastName;
      @JsonProperty( "avatarUrl" )
      private String avatarUrl;

      public AuthorTileView()
      {
        super();
      }

      public Long getId()
      {
        return id;
      }

      public void setId( Long id )
      {
        this.id = id;
      }

      public String getFirstName()
      {
        return firstName;
      }

      public void setFirstName( String firstName )
      {
        this.firstName = firstName;
      }

      public String getLastName()
      {
        return lastName;
      }

      public void setLastName( String lastName )
      {
        this.lastName = lastName;
      }

      public String getAvatarUrl()
      {
        return avatarUrl;
      }

      public void setAvatarUrl( String avatarUrl )
      {
        this.avatarUrl = avatarUrl;
      }

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

}
