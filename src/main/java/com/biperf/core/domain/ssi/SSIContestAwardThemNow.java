
package com.biperf.core.domain.ssi;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.SSIContestIssuanceStatusType;

/**
 * 
 * SSIContestAwardThemNow.
 * 
 * @author kandhi
 * @since Feb 6, 2015
 * @version 1.0
 */
public class SSIContestAwardThemNow extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  private SSIContest contest;
  private Short issuanceNumber;
  private SSIContestIssuanceStatusType issuanceStatusType;
  private Date issuanceDate;
  private Long approvedByLevel1;
  private Date dateApprovedLevel1;
  private Long approvedByLevel2;
  private Date dateApprovedLevel2;
  private String denialReason;
  private int levelApproved;
  private String notificationMessageText;

  private Date payoutIssuedDate;

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public Short getIssuanceNumber()
  {
    return issuanceNumber;
  }

  public void setIssuanceNumber( Short issuanceNumber )
  {
    this.issuanceNumber = issuanceNumber;
  }

  public SSIContestIssuanceStatusType getIssuanceStatusType()
  {
    return issuanceStatusType;
  }

  public void setIssuanceStatusType( SSIContestIssuanceStatusType issuanceStatusType )
  {
    this.issuanceStatusType = issuanceStatusType;
  }

  public Date getIssuanceDate()
  {
    return issuanceDate;
  }

  public void setIssuanceDate( Date issuanceDate )
  {
    this.issuanceDate = issuanceDate;
  }

  public Long getApprovedByLevel1()
  {
    return approvedByLevel1;
  }

  public void setApprovedByLevel1( Long approvedByLevel1 )
  {
    this.approvedByLevel1 = approvedByLevel1;
  }

  public Date getDateApprovedLevel1()
  {
    return dateApprovedLevel1;
  }

  public void setDateApprovedLevel1( Date dateApprovedLevel1 )
  {
    this.dateApprovedLevel1 = dateApprovedLevel1;
  }

  public Long getApprovedByLevel2()
  {
    return approvedByLevel2;
  }

  public void setApprovedByLevel2( Long approvedByLevel2 )
  {
    this.approvedByLevel2 = approvedByLevel2;
  }

  public Date getDateApprovedLevel2()
  {
    return dateApprovedLevel2;
  }

  public void setDateApprovedLevel2( Date dateApprovedLevel2 )
  {
    this.dateApprovedLevel2 = dateApprovedLevel2;
  }

  public String getDenialReason()
  {
    return denialReason;
  }

  public void setDenialReason( String denialReason )
  {
    this.denialReason = denialReason;
  }

  public int getLevelApproved()
  {
    return levelApproved;
  }

  public void setLevelApproved( int levelApproved )
  {
    this.levelApproved = levelApproved;
  }

  public String getNotificationMessageText()
  {
    return notificationMessageText;
  }

  public void setNotificationMessageText( String notificationMessageText )
  {
    this.notificationMessageText = notificationMessageText;
  }

  public Date getPayoutIssuedDate()
  {
    return payoutIssuedDate;
  }

  public void setPayoutIssuedDate( Date payoutIssuedDate )
  {
    this.payoutIssuedDate = payoutIssuedDate;
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
    SSIContestAwardThemNow other = (SSIContestAwardThemNow)object;
    if ( contest == null || other.contest == null )
    {
      return false;
    }
    else if ( !contest.equals( other.contest ) )
    {
      return false;
    }
    if ( issuanceNumber == null || other.issuanceNumber == null )
    {
      return false;
    }
    else if ( !issuanceNumber.equals( other.issuanceNumber ) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( contest == null ? 0 : contest.hashCode() );
    result = prime * result + ( issuanceNumber == null ? 0 : issuanceNumber.hashCode() );
    return result;
  }

}
