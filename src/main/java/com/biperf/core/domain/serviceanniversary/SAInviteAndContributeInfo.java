/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.serviceanniversary;

import java.util.Date;
import java.util.UUID;

import com.biperf.core.domain.BaseDomain;

/**
 * 
 * 
 * @author palaniss
 * @since Nov 1, 2018
 * 
 */
public class SAInviteAndContributeInfo extends BaseDomain
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private UUID celebrationId;
  private Long contributorPersonId;
  private String contributorFirstName;
  private String contributorLastName;
  private String contributorEmailAddr;
  private Date inviteSendDate;
  private Long inviteePersonId;
  private String contributionState;
  private boolean InternalOrExternal;
  private boolean Invited;
  private Date contributedDate;
  private Long purlContributorId;

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( object == null )
    {
      return false;
    }

    SAInviteAndContributeInfo saInviteAndContributeInfo = (SAInviteAndContributeInfo)object;
    if ( celebrationId == null || saInviteAndContributeInfo.getCelebrationId() == null )
    {
      return false;
    }

    if ( celebrationId.equals( saInviteAndContributeInfo.getCelebrationId() ) )
    {
      return true;
    }

    return false;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( celebrationId == null ? 0 : celebrationId.hashCode() );
    return result;
  }

  public Long getContributorPersonId()
  {
    return contributorPersonId;
  }

  public void setContributorPersonId( Long contributorPersonId )
  {
    this.contributorPersonId = contributorPersonId;
  }

  public String getContributorFirstName()
  {
    return contributorFirstName;
  }

  public void setContributorFirstName( String contributorFirstName )
  {
    this.contributorFirstName = contributorFirstName;
  }

  public String getContributorLastName()
  {
    return contributorLastName;
  }

  public void setContributorLastName( String contributorLastName )
  {
    this.contributorLastName = contributorLastName;
  }

  public String getContributorEmailAddr()
  {
    return contributorEmailAddr;
  }

  public void setContributorEmailAddr( String contributorEmailAddr )
  {
    this.contributorEmailAddr = contributorEmailAddr;
  }

  public Date getInviteSendDate()
  {
    return inviteSendDate;
  }

  public void setInviteSendDate( Date inviteSendDate )
  {
    this.inviteSendDate = inviteSendDate;
  }

  public Long getInviteePersonId()
  {
    return inviteePersonId;
  }

  public void setInviteePersonId( Long inviteePersonId )
  {
    this.inviteePersonId = inviteePersonId;
  }

  public String getContributionState()
  {
    return contributionState;
  }

  public void setContributionState( String contributionState )
  {
    this.contributionState = contributionState;
  }

  public Date getContributedDate()
  {
    return contributedDate;
  }

  public void setContributedDate( Date contributedDate )
  {
    this.contributedDate = contributedDate;
  }

  public boolean isInternalOrExternal()
  {
    return InternalOrExternal;
  }

  public void setInternalOrExternal( boolean internalOrExternal )
  {
    InternalOrExternal = internalOrExternal;
  }

  public boolean isInvited()
  {
    return Invited;
  }

  public void setInvited( boolean invited )
  {
    Invited = invited;
  }

  public UUID getCelebrationId()
  {
    return celebrationId;
  }

  public void setCelebrationId( UUID celebrationId )
  {
    this.celebrationId = celebrationId;
  }

  public Long getPurlContributorId()
  {
    return purlContributorId;
  }

  public void setPurlContributorId( Long purlContributorId )
  {
    this.purlContributorId = purlContributorId;
  }

}
