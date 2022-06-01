
package com.biperf.core.domain.purl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.enums.PurlContributorMediaStatus;
import com.biperf.core.domain.enums.PurlContributorVideoType;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ImageUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author drahn
 * @since Jan 12, 2013
 * @version 1.0
 */
@JsonInclude( value = Include.NON_NULL )
public class PurlActivityPodsView
{
  private Long activityId;
  private List<PurlActivityUserInfo> userInfo = new ArrayList<PurlActivityUserInfo>();
  private String commentText;
  private String commentLang;
  private String videoWebLink;
  private List<PurlActivityMediaView> media = new ArrayList<PurlActivityMediaView>();

  public PurlActivityPodsView( PurlContributorComment commentItem, String siteUrl )
  {
    activityId = commentItem.getId();
    userInfo.add( new PurlActivityUserInfo( commentItem.getPurlContributor(), siteUrl ) );
    commentText = commentItem.getComments();

    if ( commentItem.getCommentsLanguageType() != null )
    {
      commentLang = commentItem.getCommentsLanguageType().getLanguageCode();
    }

    // Web Video
    if ( commentItem.getVideoType() != null && PurlContributorVideoType.WEB.equals( commentItem.getVideoType().getCode() ) && commentItem.getVideoStatus() != null
        && PurlContributorMediaStatus.ACTIVE.equals( commentItem.getVideoStatus().getCode() ) )
    {
      videoWebLink = commentItem.getVideoUrl();
    }
    // Direct Video
    else if ( commentItem.getVideoType() != null && PurlContributorVideoType.DIRECT.equals( commentItem.getVideoType().getCode() ) && commentItem.getVideoStatus() != null
        && PurlContributorMediaStatus.ACTIVE.equals( commentItem.getVideoStatus().getCode() ) )
    {
      // Use the extension to determine which content type. Send only the one. In future, may
      // transcode to multiple formats.
      // Added to avoid NullPointerException in case of video url extension value is null
      String videoUrlExtension = commentItem.getVideoUrlExtension() != null ? commentItem.getVideoUrlExtension() : "";
      String eCardVideoLink = null;
      if ( commentItem.getVideoUrl().contains( ActionConstants.REQUEST_ID ) )
      {
        MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( commentItem.getRequestId( commentItem.getVideoUrl() ) );

        if ( Objects.nonNull( mtcVideo ) )
        {
          eCardVideoLink = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();

        }
        else
        {
          eCardVideoLink = commentItem.getActualCardUrl( commentItem.getVideoUrl() );

        }

      }
      else
      {
        eCardVideoLink = commentItem.getVideoUrl();
      }

      switch ( videoUrlExtension )
      {
        case "mp4":
          media.add( new PurlActivityMediaView( "mp4", eCardVideoLink ) );
          break;
        case "mov":
          media.add( new PurlActivityMediaView( "quicktime", eCardVideoLink + ".mov" ) );
          break;
        case "webm":
          media.add( new PurlActivityMediaView( "webm", eCardVideoLink ) );
          break;
        case "ogg":
          media.add( new PurlActivityMediaView( "ogg", eCardVideoLink + ".ogg" ) );
          break;
        case "3gp":
          media.add( new PurlActivityMediaView( "3gpp", eCardVideoLink + ".3gp" ) );
          break;
        default:
          break;
      }
    }
    // Photo
    else if ( StringUtils.isNotBlank( commentItem.getImageUrl() ) && commentItem.getImageStatus() != null && PurlContributorMediaStatus.ACTIVE.equals( commentItem.getImageStatus().getCode() ) )
    {
      media.add( new PurlActivityMediaView( commentItem.getImageUrl() ) );
    }
  }

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

  public List<PurlActivityUserInfo> getUserInfo()
  {
    return userInfo;
  }

  public void setUserInfo( List<PurlActivityUserInfo> userInfo )
  {
    this.userInfo = userInfo;
  }

  public String getCommentText()
  {
    return commentText;
  }

  public void setCommentText( String commentText )
  {
    this.commentText = commentText;
  }

  public String getCommentLang()
  {
    return commentLang;
  }

  public String getVideoWebLink()
  {
    return videoWebLink;
  }

  public void setVideoWebLink( String videoWebLink )
  {
    this.videoWebLink = videoWebLink;
  }

  public List<PurlActivityMediaView> getMedia()
  {
    return media;
  }

  public void setMedia( List<PurlActivityMediaView> media )
  {
    this.media = media;
  }

  public static class PurlActivityUserInfo
  {
    private String userName;
    private Long contributorID;
    private String signedIn;
    private String profileLink;
    private String profilePhoto;

    private String firstName;
    private String lastName;

    public PurlActivityUserInfo( PurlContributor purlContributor, String siteUrl )
    {
      boolean internalContributor = false;
      String avatarUrl = "";
      userName = purlContributor.getFirstName() + " " + purlContributor.getLastName();
      firstName = purlContributor.getFirstName();
      lastName = purlContributor.getLastName();
      contributorID = purlContributor.getId();
      signedIn = "false";

      profileLink = "";
      internalContributor = purlContributor.getUser() != null ? true : false;

      if ( internalContributor )
      {
        Participant internalContributorPax = (Participant)purlContributor.getUser();
        avatarUrl = internalContributorPax.getAvatarSmall() != null ? internalContributorPax.getAvatarSmall() : null;
      }
      else
      {
        avatarUrl = purlContributor.getDisplayAvatarUrl();
      }

      profilePhoto = avatarUrl;

    }

    public String getDisplayAvatarUrl( String avatarImage )
    {
      String displayAvatarUrl;
      if ( avatarImage != null && avatarImage.length() > 0 )
      {
        if ( avatarImage.indexOf( "cm3dam" ) > 0 )
        {
          displayAvatarUrl = avatarImage;
        }
        else
        {
          displayAvatarUrl = ImageUtils.getFullImageUrlPath( avatarImage );
        }
      }
      else
      {
        displayAvatarUrl = ImageUtils.getFullImageUrlPath( avatarImage );
      }

      return displayAvatarUrl;
    }

    public String getUserName()
    {
      return userName;
    }

    public void setUserName( String userName )
    {
      this.userName = userName;
    }

    public Long getContributorID()
    {
      return contributorID;
    }

    public void setContributorID( Long contributorID )
    {
      this.contributorID = contributorID;
    }

    public String getSignedIn()
    {
      return signedIn;
    }

    public void setSignedIn( String signedIn )
    {
      this.signedIn = signedIn;
    }

    public String getProfileLink()
    {
      return profileLink;
    }

    public void setProfileLink( String profileLink )
    {
      this.profileLink = profileLink;
    }

    public String getProfilePhoto()
    {
      return profilePhoto;
    }

    public void setProfilePhoto( String profilePhoto )
    {
      this.profilePhoto = profilePhoto;
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
  }

  @Override
  public String toString()
  {
    return "PurlActivityPodsView [activityId=" + activityId + ", userInfo=" + userInfo + ", commentText=" + commentText + ", commentLang=" + commentLang + ", videoWebLink=" + videoWebLink + ", media="
        + media + "]";
  }

  private static MTCVideoService getMtcVideoService()
  {
    return (MTCVideoService)BeanLocator.getBean( MTCVideoService.BEAN_NAME );
  }
}
