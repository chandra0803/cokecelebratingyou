
package com.biperf.core.value.client;

public class CokeCommentsLikes
{
  private Long userId;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private Integer likesCount;
  private Integer commentsCount;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
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

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public Integer getLikesCount()
  {
    return likesCount;
  }

  public void setLikesCount( Integer likesCount )
  {
    this.likesCount = likesCount;
  }

  public Integer getCommentsCount()
  {
    return commentsCount;
  }

  public void setCommentsCount( Integer commentsCount )
  {
    this.commentsCount = commentsCount;
  }

}
