
package com.biperf.core.ui.nomination;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JSON view object for the information request on the more info page
 * 
 * @see NominationMoreInfoAction
 */
// This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class NominationMoreInfoViewBean
{
  @JsonProperty( "nominationName" )
  private String nominationName;
  @JsonProperty( "approversMessage" )
  private String approversMessage;
  @JsonProperty( "attachmentName" )
  private String attachmentName;
  
  @JsonProperty( "nominationsMoreInfo" )
  private List<NominationMoreInfoDetailViewBean> nominationsMoreInfo = new ArrayList<>();
  
 // @JsonProperty( "nominationLink" )
 // private String nominationLink;
  
  // Client customization for WIP #39189 starts
  // @JsonProperty( "nominationLink" )
  // private String nominationLink;
  private int minDocsAllowed;
  private int maxDocsAllowed;
  private int updatedDocCount;
  private List<NominationSubmitDataAttachmentViewBean> nominationLinks = new ArrayList<NominationSubmitDataAttachmentViewBean>();
  // Client customization for WIP #39189 ends

  public String getNominationName()
  {
    return nominationName;
  }

  public void setNominationName( String nominationName )
  {
    this.nominationName = nominationName;
  }

  public String getApproversMessage()
  {
    return approversMessage;
  }

  public void setApproversMessage( String approversMessage )
  {
    this.approversMessage = approversMessage;
  }

  public String getAttachmentName()
  {
    return attachmentName;
  }

  public void setAttachmentName( String attachmentName )
  {
    this.attachmentName = attachmentName;
  }
  // Client customization for WIP #39189 starts
  public int getMinDocsAllowed()
  {
    return minDocsAllowed;
  }

  public void setMinDocsAllowed( int minDocsAllowed )
  {
    this.minDocsAllowed = minDocsAllowed;
  }

  public int getMaxDocsAllowed()
  {
    return maxDocsAllowed;
  }

  public void setMaxDocsAllowed( int maxDocsAllowed )
  {
    this.maxDocsAllowed = maxDocsAllowed;
  }

  public int getUpdatedDocCount()
  {
    return updatedDocCount;
  }

  public void setUpdatedDocCount( int updatedDocCount )
  {
    this.updatedDocCount = updatedDocCount;
  }
  
  public List<NominationSubmitDataAttachmentViewBean> getNominationLinks()
  {
    return nominationLinks;
  }

  public void setNominationLinks( List<NominationSubmitDataAttachmentViewBean> nominationLinks )
  {
    this.nominationLinks = nominationLinks;
  }
  // Client customization for WIP #39189 ends
  public List<NominationMoreInfoDetailViewBean> getNominationsMoreInfo()
  {
    return nominationsMoreInfo;
  }

  public void setNominationsMoreInfo( List<NominationMoreInfoDetailViewBean> nominationsMoreInfo )
  {
    this.nominationsMoreInfo = nominationsMoreInfo;
  }

  /**
   * Contains the submission and claim information 
   */
  public static class NominationMoreInfoDetailViewBean
  {
    @JsonProperty( "nominatorInfo" )
    private List<NominatorInfoViewBean> nominatorInfo = new ArrayList<>();
    @JsonProperty( "originalSubmission" )
    private List<NominationSubmissionViewBean> originalSubmission = new ArrayList<>();

    public List<NominatorInfoViewBean> getNominatorInfo()
    {
      return nominatorInfo;
    }

    public void setNominatorInfo( List<NominatorInfoViewBean> nominatorInfo )
    {
      this.nominatorInfo = nominatorInfo;
    }

    public List<NominationSubmissionViewBean> getOriginalSubmission()
    {
      return originalSubmission;
    }

    public void setOriginalSubmission( List<NominationSubmissionViewBean> originalSubmission )
    {
      this.originalSubmission = originalSubmission;
    }

    /**
     * Information on the person who initially submitted the nomination
     */
    public static class NominatorInfoViewBean
    {
      @JsonProperty( "id" )
      private Long id;
      @JsonProperty( "name" )
      private String name;
      @JsonProperty( "firstName" )
      private String firstName;
      @JsonProperty( "lastName" )
      private String lastName;
      @JsonProperty( "title" )
      private String title;
      @JsonProperty( "countryCode" )
      private String countryCode;
      @JsonProperty( "countryName" )
      private String countryName;
      @JsonProperty( "orgName" )
      private String orgName;
      @JsonProperty( "departmentName" )
      private String departmentName;
      @JsonProperty( "jobName" )
      private String jobName;

      @JsonProperty( "teamMembers" )
      private List<NominationSubmissionTeamViewBean> teamMembers = new ArrayList<>();

      public List<NominationSubmissionTeamViewBean> getTeamMembers()
      {
        return teamMembers;
      }

      public void setTeamMembers( List<NominationSubmissionTeamViewBean> teamMembers )
      {
        this.teamMembers = teamMembers;
      }

      public String getName()
      {
        return name;
      }

      public void setName( String name )
      {
        this.name = name;
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

      public String getJobName()
      {
        return jobName;
      }

      public void setJobName( String jobName )
      {
        this.jobName = jobName;
      }

    }

    /**
     * Original submission
     */
    public static class NominationSubmissionViewBean
    {
      @JsonProperty( "badges" )
      private List<NominationSubmissionBadgeViewBean> badges = new ArrayList<>();
      @JsonProperty( "certificates" )
      private List<NominationSubmissionCertificateViewBean> certificates = new ArrayList<>();
      @JsonProperty( "videoContent" )
      private boolean videoContent;
      @JsonProperty( "posterImg" )
      private String posterImg;
      @JsonProperty( "videoWebLink" )
      private String videoWebLink;
      @JsonProperty( "awardedDate" )
      private String dateSubmitted;
      @JsonProperty( "ecardName" )
      private String ecardName;
      @JsonProperty( "ecardUrl" )
      private String ecardUrl;
      @JsonProperty( "certificateUrl" )
      private String certificateUrl;
      @JsonProperty( "reason" )
      private String reason;
      @JsonProperty( "allowTranslate" )
      private boolean allowTranslate;
      @JsonProperty( "customFields" )
      private List<NominationSubmissionCustomFieldViewBean> customFields = new ArrayList<>();
      @JsonProperty( "behaviors" )
      private boolean behaviors;

      public List<NominationSubmissionBadgeViewBean> getBadges()
      {
        return badges;
      }

      public void setBadges( List<NominationSubmissionBadgeViewBean> badges )
      {
        this.badges = badges;
      }

      public List<NominationSubmissionCertificateViewBean> getCertificates()
      {
        return certificates;
      }

      public void setCertificates( List<NominationSubmissionCertificateViewBean> certificates )
      {
        this.certificates = certificates;
      }

      public boolean isVideoContent()
      {
        return videoContent;
      }

      public void setVideoContent( boolean videoContent )
      {
        this.videoContent = videoContent;
      }

      public String getPosterImg()
      {
        return posterImg;
      }

      public void setPosterImg( String posterImg )
      {
        this.posterImg = posterImg;
      }

      public String getVideoWebLink()
      {
        return videoWebLink;
      }

      public void setVideoWebLink( String videoWebLink )
      {
        this.videoWebLink = videoWebLink;
      }

      public String getDateSubmitted()
      {
        return dateSubmitted;
      }

      public void setDateSubmitted( String dateSubmitted )
      {
        this.dateSubmitted = dateSubmitted;
      }

      public String getEcardName()
      {
        return ecardName;
      }

      public void setEcardName( String ecardName )
      {
        this.ecardName = ecardName;
      }

      public String getEcardUrl()
      {
        return ecardUrl;
      }

      public void setEcardUrl( String ecardUrl )
      {
        this.ecardUrl = ecardUrl;
      }

      public String getCertificateUrl()
      {
        return certificateUrl;
      }

      public void setCertificateUrl( String certificateUrl )
      {
        this.certificateUrl = certificateUrl;
      }

      public String getReason()
      {
        return reason;
      }

      public void setReason( String reason )
      {
        this.reason = reason;
      }

      public boolean isAllowTranslate()
      {
        return allowTranslate;
      }

      public void setAllowTranslate( boolean allowTranslate )
      {
        this.allowTranslate = allowTranslate;
      }

      public List<NominationSubmissionCustomFieldViewBean> getCustomFields()
      {
        return customFields;
      }

      public void setCustomField( List<NominationSubmissionCustomFieldViewBean> customFields )
      {
        this.customFields = customFields;
      }

      public boolean isBehaviors()
      {
        return behaviors;
      }

      public void setBehaviors( boolean behaviors )
      {
        this.behaviors = behaviors;
      }

      /**
       * Badges that were submitted
       */
      public static class NominationSubmissionBadgeViewBean
      {
        @JsonProperty( "id" )
        private String id;
        @JsonProperty( "behavior" )
        private String behavior;
        @JsonProperty( "badgeUrl" )
        private String badgeUrl;

        public String getId()
        {
          return id;
        }

        public void setId( String id )
        {
          this.id = id;
        }

        public String getBehavior()
        {
          return behavior;
        }

        public void setBehavior( String behavior )
        {
          this.behavior = behavior;
        }

        public String getBadgeUrl()
        {
          return badgeUrl;
        }

        public void setBadgeUrl( String badgeUrl )
        {
          this.badgeUrl = badgeUrl;
        }

      }

      /**
       * Certificate that was submitted
       */
      public static class NominationSubmissionCertificateViewBean
      {
        @JsonProperty( "id" )
        private String id;
        @JsonProperty( "name" )
        private String name;
        @JsonProperty( "img" )
        private String img;
        @JsonProperty( "imgLg" )
        private String imgLg;

        public String getId()
        {
          return id;
        }

        public void setId( String id )
        {
          this.id = id;
        }

        public String getName()
        {
          return name;
        }

        public void setName( String name )
        {
          this.name = name;
        }

        public String getImg()
        {
          return img;
        }

        public void setImg( String img )
        {
          this.img = img;
        }

        public String getImgLg()
        {
          return imgLg;
        }

        public void setImgLg( String imgLg )
        {
          this.imgLg = imgLg;
        }

      }

      /**
       * A claim form custom field
       */
      public static class NominationSubmissionCustomFieldViewBean
      {
        @JsonProperty( "label" )
        private String label;
        @JsonProperty( "value" )
        private String value;
        @JsonProperty( "sequenceNumber" )
        private Long sequenceNumber;
        @JsonProperty( "type" )
        private String type;
        @JsonProperty( "fieldId" )
        private Long fieldId;

        public String getLabel()
        {
          return label;
        }

        public void setLabel( String label )
        {
          this.label = label;
        }

        public String getValue()
        {
          return value;
        }

        public void setValue( String value )
        {
          this.value = value;
        }

        public Long getSequenceNumber()
        {
          return sequenceNumber;
        }

        public void setSequenceNumber( Long sequenceNumber )
        {
          this.sequenceNumber = sequenceNumber;
        }

        public String getType()
        {
          return type;
        }

        public void setType( String type )
        {
          this.type = type;
        }

        public Long getFieldId()
        {
          return fieldId;
        }

        public void setFieldId( Long fieldId )
        {
          this.fieldId = fieldId;
        }

      }
    }
  }
}
