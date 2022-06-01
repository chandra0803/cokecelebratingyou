
package com.biperf.core.value.nomination;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NominatorInfoViewBean
{
  private Long nominatorId;
  private String nominatorName;
  private String countryCode;
  private String countryName;
  private String nominatorOrg;
  private String title;
  private String departmentName;
  private String commentText;

  @JsonProperty( "nominatorID" )
  public Long getNominatorId()
  {
    return nominatorId;
  }

  public void setNominatorId( Long nominatorId )
  {
    this.nominatorId = nominatorId;
  }

  public String getNominatorName()
  {
    return nominatorName;
  }

  public void setNominatorName( String nominatorName )
  {
    this.nominatorName = nominatorName;
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

  public String getNominatorOrg()
  {
    return nominatorOrg;
  }

  public void setNominatorOrg( String nominatorOrg )
  {
    this.nominatorOrg = nominatorOrg;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  public String getCommentText()
  {
    return commentText;
  }

  public void setCommentText( String commentText )
  {
    this.commentText = commentText;
  }
}
