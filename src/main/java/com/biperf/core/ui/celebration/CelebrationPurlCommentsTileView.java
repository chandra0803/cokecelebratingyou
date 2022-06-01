/**
 * 
 */

package com.biperf.core.ui.celebration;

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
public class CelebrationPurlCommentsTileView
{
  private List<CommentsView> purlComments = new ArrayList<CommentsView>();

  @JsonProperty( "purlComments" )
  public List<CommentsView> getPurlComments()
  {
    return purlComments;
  }

  public void setPurlComments( List<CommentsView> purlComments )
  {
    this.purlComments = purlComments;
  }

  public static class CommentsView
  {
    @JsonProperty( "id" )
    private String id;
    @JsonProperty( "firstName" )
    private String firstName;
    @JsonProperty( "lastName" )
    private String lastName;
    @JsonProperty( "hasAvatar" )
    private Boolean hasAvatar;
    @JsonProperty( "avatarUrl" )
    private String avatarUrl;
    @JsonProperty( "comment" )
    private String comment;
    @JsonProperty( "hasImg" )
    private Boolean hasImg;
    @JsonProperty( "imgUrl" )
    private String imgUrl;
    @JsonProperty( "hasVid" )
    private Boolean hasVid;
    @JsonProperty( "purlUrl" )
    private String purlUrl;

    public String getId()
    {
      return id;
    }

    public void setId( String id )
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

    public Boolean getHasAvatar()
    {
      return hasAvatar;
    }

    public void setHasAvatar( Boolean hasAvatar )
    {
      this.hasAvatar = hasAvatar;
    }

    public String getAvatarUrl()
    {
      return avatarUrl;
    }

    public void setAvatarUrl( String avatarUrl )
    {
      this.avatarUrl = avatarUrl;
    }

    public String getComment()
    {
      return comment;
    }

    public void setComment( String comment )
    {
      this.comment = comment;
    }

    public Boolean getHasImg()
    {
      return hasImg;
    }

    public void setHasImg( Boolean hasImg )
    {
      this.hasImg = hasImg;
    }

    public String getImgUrl()
    {
      return imgUrl;
    }

    public void setImgUrl( String imgUrl )
    {
      this.imgUrl = imgUrl;
    }

    public Boolean getHasVid()
    {
      return hasVid;
    }

    public void setHasVid( Boolean hasVid )
    {
      this.hasVid = hasVid;
    }

    public String getPurlUrl()
    {
      return purlUrl;
    }

    public void setPurlUrl( String purlUrl )
    {
      this.purlUrl = purlUrl;
    }

  }

}
