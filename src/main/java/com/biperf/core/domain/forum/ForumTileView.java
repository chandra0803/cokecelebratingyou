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
public class ForumTileView
{
  private String[] messages = {};
  private ThreadView threadView;

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  @JsonProperty( "discussionJson" )
  public ThreadView getThreadView()
  {
    return threadView;
  }

  public void setThreadView( ThreadView threadView )
  {
    this.threadView = threadView;
  }

  public static class ThreadView
  {
    private ThreadAuthorView threadAuthorView;
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
    @JsonProperty( "totalNumberOfReplies" )
    private int totalNumberOfReplies;
    @JsonProperty( "repliesPerPage" )
    private int repliesPerPage;
    @JsonProperty( "page" )
    private int page;
    @JsonProperty( "isLiked" )
    private Boolean isLiked;
    @JsonProperty( "likedIds" )
    private String likedIds;
    @JsonProperty( "date" )
    private String dateCreated;
    @JsonProperty( "time" )
    private String timeCreated;
    @JsonProperty( "showLatest" )
    private Boolean showLatest;
    private List<LastRepliesView> lastRepliesView = new ArrayList<LastRepliesView>();

    public ThreadView()
    {

    }

    @JsonProperty( "author" )
    public ThreadAuthorView getThreadAuthorView()
    {
      return threadAuthorView;
    }

    public void setThreadAuthorView( ThreadAuthorView threadAuthorView )
    {
      this.threadAuthorView = threadAuthorView;
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

    public Boolean getShowLatest()
    {
      return showLatest;
    }

    public void setShowLatest( Boolean showLatest )
    {
      this.showLatest = showLatest;
    }

    @JsonProperty( "comments" )
    public List<LastRepliesView> getLastRepliesView()
    {
      return lastRepliesView;
    }

    public void setLastRepliesView( List<LastRepliesView> lastRepliesView )
    {
      this.lastRepliesView = lastRepliesView;
    }

  }

  public static class ThreadAuthorView
  {
    @JsonProperty( "id" )
    private Long id;
    @JsonProperty( "firstName" )
    private String firstName;
    @JsonProperty( "lastName" )
    private String lastName;
    @JsonProperty( "avatarUrl" )
    private String avatarUrl;

    public ThreadAuthorView()
    {

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

  public static class LastRepliesView
  {
    @JsonProperty( "id" )
    private Long id;
    @JsonProperty( "likedIds" )
    private String likedIds;
    private CommenterInfo commenterInfo;
    @JsonProperty( "numberOfLikes" )
    private Long numberOfLikes;
    @JsonProperty( "isLiked" )
    private Boolean isLiked;
    @JsonProperty( "comment" )
    private String comment;
    @JsonProperty( "date" )
    private String date;
    @JsonProperty( "time" )
    private String time;

    public LastRepliesView()
    {

    }

    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public String getLikedIds()
    {
      return likedIds;
    }

    public void setLikedIds( String likedIds )
    {
      this.likedIds = likedIds;
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

    public String getComment()
    {
      return comment;
    }

    public void setComment( String comment )
    {
      this.comment = comment;
    }

    public String getDate()
    {
      return date;
    }

    public void setDate( String date )
    {
      this.date = date;
    }

    public String getTime()
    {
      return time;
    }

    public void setTime( String time )
    {
      this.time = time;
    }
  }
}
