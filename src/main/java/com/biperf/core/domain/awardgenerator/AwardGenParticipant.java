/*
 * (c) 2013 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/awardgenerator/AwardGenParticipant.java,v $
 */

package com.biperf.core.domain.awardgenerator;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.user.User;

/**
 * AwardGenParticipant
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>chowdhur</td>
 * <td>Jul 22, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardGenParticipant extends BaseDomain
{
  private AwardGenBatch awardGenBatch;
  private User user;
  private PromoMerchProgramLevel level;
  private Long awardAmount;
  private Date anniversaryDate;
  private Date issueDate;
  private String isDismissed;
  private Integer anniversaryNumberOfDays = new Integer( 0 );
  private Integer anniversaryNumberOfYears = new Integer( 0 );

  public AwardGenBatch getAwardGenBatch()
  {
    return awardGenBatch;
  }

  public void setAwardGenBatch( AwardGenBatch awardGenBatch )
  {
    this.awardGenBatch = awardGenBatch;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public PromoMerchProgramLevel getLevel()
  {
    return level;
  }

  public void setLevel( PromoMerchProgramLevel level )
  {
    this.level = level;
  }

  public Long getAwardAmount()
  {
    return awardAmount;
  }

  public void setAwardAmount( Long awardAmount )
  {
    if ( awardAmount == null )
    {
      this.awardAmount = new Long( 0 );
    }
    else
    {
      this.awardAmount = awardAmount;
    }
  }

  public Date getAnniversaryDate()
  {
    return anniversaryDate;
  }

  public void setAnniversaryDate( Date anniversaryDate )
  {
    this.anniversaryDate = anniversaryDate;
  }

  public Date getIssueDate()
  {
    return issueDate;
  }

  public void setIssueDate( Date issueDate )
  {
    this.issueDate = issueDate;
  }

  public String getIsDismissed()
  {
    return isDismissed;
  }

  public void setIsDismissed( String isDismissed )
  {
    this.isDismissed = isDismissed;
  }

  public Integer getAnniversaryNumberOfDays()
  {
    return anniversaryNumberOfDays;
  }

  public void setAnniversaryNumberOfDays( Integer anniversaryNumberOfDays )
  {
    this.anniversaryNumberOfDays = anniversaryNumberOfDays;
  }

  public Integer getAnniversaryNumberOfYears()
  {
    return anniversaryNumberOfYears;
  }

  public void setAnniversaryNumberOfYears( Integer anniversaryNumberOfYears )
  {
    this.anniversaryNumberOfYears = anniversaryNumberOfYears;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "AwardGenParticipant [awardGenBatch=" + awardGenBatch + ", user=" + user + ", level=" + level + ", awardAmount=" + awardAmount + ", anniversaryDate=" + anniversaryDate + ", issueDate="
        + issueDate + " ]";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( awardAmount == null ? 0 : awardAmount.hashCode() );
    result = prime * result + ( awardGenBatch == null ? 0 : awardGenBatch.hashCode() );
    result = prime * result + ( anniversaryDate == null ? 0 : anniversaryDate.hashCode() );
    result = prime * result + ( issueDate == null ? 0 : issueDate.hashCode() );
    result = prime * result + ( level == null ? 0 : level.hashCode() );
    result = prime * result + ( user == null ? 0 : user.hashCode() );
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    AwardGenParticipant other = (AwardGenParticipant)obj;
    if ( awardAmount == null )
    {
      if ( other.awardAmount != null )
      {
        return false;
      }
    }
    else if ( !awardAmount.equals( other.awardAmount ) )
    {
      return false;
    }
    if ( awardGenBatch == null )
    {
      if ( other.awardGenBatch != null )
      {
        return false;
      }
    }
    else if ( !awardGenBatch.equals( other.awardGenBatch ) )
    {
      return false;
    }
    if ( issueDate == null )
    {
      if ( other.issueDate != null )
      {
        return false;
      }
    }
    else if ( !issueDate.equals( other.issueDate ) )
    {
      return false;
    }
    if ( anniversaryDate == null )
    {
      if ( other.anniversaryDate != null )
      {
        return false;
      }
    }
    else if ( !anniversaryDate.equals( other.anniversaryDate ) )
    {
      return false;
    }
    if ( level == null )
    {
      if ( other.level != null )
      {
        return false;
      }
    }
    else if ( !level.equals( other.level ) )
    {
      return false;
    }
    if ( user == null )
    {
      if ( other.user != null )
      {
        return false;
      }
    }
    else if ( !user.equals( other.user ) )
    {
      return false;
    }
    return true;
  }

}
