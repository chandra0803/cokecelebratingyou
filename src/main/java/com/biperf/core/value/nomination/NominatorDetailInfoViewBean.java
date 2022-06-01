
package com.biperf.core.value.nomination;

import java.util.List;

public class NominatorDetailInfoViewBean
{
  private Long id;
  private Long claimId;
  private String firstName;
  private String lastName;
  private String avatarUrl;
  private String title;
  private String countryCode;
  private String countryName;
  private String orgName;
  private String departmentName;
  private String ecardImg;
  private String videoImg;
  private String videoUrl;
  private String commentText;
  private List<NominationDetailInfoBadgesViewBean> badges;
  private String dateSubmitted;
  private String eCertUrl;
  private List<NominationDetailInfoFieldsViewBean> fields;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
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

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  public String getEcardImg()
  {
    return ecardImg;
  }

  public void setEcardImg( String ecardImg )
  {
    this.ecardImg = ecardImg;
  }

  public String getVideoImg()
  {
    return videoImg;
  }

  public void setVideoImg( String videoImg )
  {
    this.videoImg = videoImg;
  }

  public String getVideoUrl()
  {
    return videoUrl;
  }

  public void setVideoUrl( String videoUrl )
  {
    this.videoUrl = videoUrl;
  }

  public String getCommentText()
  {
    return commentText;
  }

  public void setCommentText( String commentText )
  {
    this.commentText = commentText;
  }

  public List<NominationDetailInfoBadgesViewBean> getBadges()
  {
    return badges;
  }

  public void setBadges( List<NominationDetailInfoBadgesViewBean> badges )
  {
    this.badges = badges;
  }

  public String getDateSubmitted()
  {
    return dateSubmitted;
  }

  public void setDateSubmitted( String dateSubmitted )
  {
    this.dateSubmitted = dateSubmitted;
  }

  public String geteCertUrl()
  {
    return eCertUrl;
  }

  public void seteCertUrl( String eCertUrl )
  {
    this.eCertUrl = eCertUrl;
  }

  public List<NominationDetailInfoFieldsViewBean> getFields()
  {
    return fields;
  }

  public void setFields( List<NominationDetailInfoFieldsViewBean> fields )
  {
    this.fields = fields;
  }
}
