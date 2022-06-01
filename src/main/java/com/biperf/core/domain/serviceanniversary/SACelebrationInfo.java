/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.serviceanniversary;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;

import com.biperf.core.domain.BaseDomain;

/**
 * 
 * 
 * @author palaniss
 * @since Nov 1, 2018
 * 
 */
public class SACelebrationInfo extends BaseDomain
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Long programId;
  private UUID companyId;
  private UUID celebrationId;
  private Long recipientId;
  private Date awardDate;
  private Long teamId;
  private String awardStatus;
  private String giftCodeStatus;
  private String pointsStatus;
  private boolean giftCode;
  private boolean points;
  private boolean celebrationSite;
  private Long claimId;
  private String awardLevel;
  private Double awardPoints;
  private boolean taxable;
  private boolean daymaker;
  private String country;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getProgramId()
  {
    return programId;
  }

  public void setProgramId( Long programId )
  {
    this.programId = programId;
  }

  public Date getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( Date awardDate )
  {
    this.awardDate = awardDate;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

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

    SACelebrationInfo pgrm = (SACelebrationInfo)object;
    if ( programId == null || pgrm.getProgramId() == null )
    {
      return false;
    }

    if ( programId.equals( pgrm.getProgramId() ) )
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
    result = prime * result + ( programId == null ? 0 : programId.hashCode() );
    return result;
  }

  public UUID getCompanyId()
  {
    return companyId;
  }

  public void setCompanyId( UUID companyId )
  {
    this.companyId = companyId;
  }

  public Long getRecipientId()
  {
    return recipientId;
  }

  public void setRecipientId( Long recipientId )
  {
    this.recipientId = recipientId;
  }

  public UUID getCelebrationId()
  {
    return celebrationId;
  }

  public void setCelebrationId( UUID celebrationId )
  {
    this.celebrationId = celebrationId;
  }

  public String getAwardStatus()
  {
    return awardStatus;
  }

  public void setAwardStatus( String awardStatus )
  {
    this.awardStatus = awardStatus;
  }

  public String getGiftCodeStatus()
  {
    return giftCodeStatus;
  }

  public void setGiftCodeStatus( String giftCodeStatus )
  {
    this.giftCodeStatus = giftCodeStatus;
  }

  public String getPointsStatus()
  {
    return pointsStatus;
  }

  public void setPointsStatus( String pointsStatus )
  {
    this.pointsStatus = pointsStatus;
  }

  public boolean isGiftCode()
  {
    return giftCode;
  }

  public void setGiftCode( boolean giftCode )
  {
    this.giftCode = giftCode;
  }

  public boolean isPoints()
  {
    return points;
  }

  public void setPoints( boolean points )
  {
    this.points = points;
  }

  public boolean isCelebrationSite()
  {
    return celebrationSite;
  }

  public void setCelebrationSite( boolean celebrationSite )
  {
    this.celebrationSite = celebrationSite;
  }

  public Date getCloseDate()
  {
    Date closeDate = new Date();
    closeDate.setTime( awardDate.getTime() - 1 * DateUtils.MILLIS_PER_DAY );
    return com.biperf.core.utils.DateUtils.toEndDate( closeDate );
  }

  public String getAwardLevel()
  {
    return awardLevel;
  }

  public void setAwardLevel( String awardLevel )
  {
    this.awardLevel = awardLevel;
  }

  public Double getAwardPoints()
  {
    return awardPoints;
  }

  public void setAwardPoints( Double awardPoints )
  {
    this.awardPoints = awardPoints;
  }

  public boolean isTaxable()
  {
    return taxable;
  }

  public void setTaxable( boolean taxable )
  {
    this.taxable = taxable;
  }

  public boolean isDaymaker()
  {
    return daymaker;
  }

  public void setDaymaker( boolean daymaker )
  {
    this.daymaker = daymaker;
  }

}
