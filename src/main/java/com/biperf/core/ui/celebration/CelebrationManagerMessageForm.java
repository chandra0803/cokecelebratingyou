/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/help/ContactUsForm.java,v $
 */

package com.biperf.core.ui.celebration;

import com.biperf.core.ui.BaseForm;

public class CelebrationManagerMessageForm extends BaseForm
{
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 3255570524384546899L;

  private Long managerMessageId;
  private String department = "";
  private String jobTitle = "";
  private String orgName = "";
  private String avatarUrl = "";
  private String firstName = "";
  private String lastName = "";
  private String primaryCountryCode = "";
  private String primaryCountryName = "";
  private boolean serviceAnniversaryEnabed = false;
  private int anniversaryNumberOfYearsOrDays;
  private boolean anniversaryInYears = true;
  private String managerName;
  private String comments;
  private boolean managerAbove;
  private String managerAvatarUrl;
  private String promotionName;
  private String managerFirstName = "";
  private String managerLastName = "";

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getJobTitle()
  {
    return jobTitle;
  }

  public void setJobTitle( String jobTitle )
  {
    this.jobTitle = jobTitle;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
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

  public int getAnniversaryNumberOfYearsOrDays()
  {
    return anniversaryNumberOfYearsOrDays;
  }

  public void setAnniversaryNumberOfYearsOrDays( int anniversaryNumberOfYearsOrDays )
  {
    this.anniversaryNumberOfYearsOrDays = anniversaryNumberOfYearsOrDays;
  }

  public String getManagerName()
  {
    return managerName;
  }

  public void setManagerName( String managerName )
  {
    this.managerName = managerName;
  }

  public Long getManagerMessageId()
  {
    return managerMessageId;
  }

  public void setManagerMessageId( Long managerMessageId )
  {
    this.managerMessageId = managerMessageId;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public boolean isAnniversaryInYears()
  {
    return anniversaryInYears;
  }

  public void setAnniversaryInYears( boolean anniversaryInYears )
  {
    this.anniversaryInYears = anniversaryInYears;
  }

  public boolean isManagerAbove()
  {
    return managerAbove;
  }

  public void setManagerAbove( boolean managerAbove )
  {
    this.managerAbove = managerAbove;
  }

  public String getManagerAvatarUrl()
  {
    return managerAvatarUrl;
  }

  public void setManagerAvatarUrl( String managerAvatarUrl )
  {
    this.managerAvatarUrl = managerAvatarUrl;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public boolean isServiceAnniversaryEnabed()
  {
    return serviceAnniversaryEnabed;
  }

  public void setServiceAnniversaryEnabed( boolean serviceAnniversaryEnabed )
  {
    this.serviceAnniversaryEnabed = serviceAnniversaryEnabed;
  }

  public String getPrimaryCountryCode()
  {
    return primaryCountryCode;
  }

  public void setPrimaryCountryCode( String primaryCountryCode )
  {
    this.primaryCountryCode = primaryCountryCode;
  }

  public String getPrimaryCountryName()
  {
    return primaryCountryName;
  }

  public void setPrimaryCountryName( String primaryCountryName )
  {
    this.primaryCountryName = primaryCountryName;
  }

  public String getManagerFirstName()
  {
    return managerFirstName;
  }

  public void setManagerFirstName( String managerFirstName )
  {
    this.managerFirstName = managerFirstName;
  }

  public String getManagerLastName()
  {
    return managerLastName;
  }

  public void setManagerLastName( String managerLastName )
  {
    this.managerLastName = managerLastName;
  }

}
