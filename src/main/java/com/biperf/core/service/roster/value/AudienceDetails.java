/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.roster.value;

public class AudienceDetails
{
  private String companyId;
  private String audienceUUID;
  private String audienceName;
  private String type;
  private boolean isPublic;

  public String getCompanyId()
  {
    return companyId;
  }

  public void setCompanyId( String companyId )
  {
    this.companyId = companyId;
  }

  public String getAudienceUUID()
  {
    return audienceUUID;
  }

  public void setAudienceUUID( String audienceUUID )
  {
    this.audienceUUID = audienceUUID;
  }

  public String getAudienceName()
  {
    return audienceName;
  }

  public void setAudienceName( String audienceName )
  {
    this.audienceName = audienceName;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public boolean isPublic()
  {
    return isPublic;
  }

  public void setPublic( boolean isPublic )
  {
    this.isPublic = isPublic;
  }

}
