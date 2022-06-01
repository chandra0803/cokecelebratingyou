/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.participant;

import java.util.UUID;

public class AudienceDetail
{
  private UUID companyId;
  private UUID rosterAudienceId;
  private String audienceName;
  private String type;
  private boolean isPublic;

  public UUID getCompanyId()
  {
    return companyId;
  }

  public void setCompanyId( UUID companyId )
  {
    this.companyId = companyId;
  }

  public UUID getRosterAudienceId()
  {
    return rosterAudienceId;
  }

  public void setRosterAudienceId( UUID rosterAudienceId )
  {
    this.rosterAudienceId = rosterAudienceId;
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
