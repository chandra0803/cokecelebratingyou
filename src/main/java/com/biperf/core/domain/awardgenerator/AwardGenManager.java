/*
 * (c) 2013 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/awardgenerator/AwardGenManager.java,v $
 */

package com.biperf.core.domain.awardgenerator;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;

/**
 * AwardGenManager
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
public class AwardGenManager extends BaseDomain
{
  private AwardGenBatch awardGenBatch;
  private User user;
  private AwardGenParticipant awardGenParticipant;
  private Date expiryDate;

  public AwardGenBatch getAwardGenBatch()
  {
    return awardGenBatch;
  }

  public void setAwardGenBatch( AwardGenBatch awardGenBatch )
  {
    this.awardGenBatch = awardGenBatch;
  }

  public Date getExpiryDate()
  {
    return expiryDate;
  }

  public void setExpiryDate( Date expiryDate )
  {
    this.expiryDate = expiryDate;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public AwardGenParticipant getAwardGenParticipant()
  {
    return awardGenParticipant;
  }

  public void setAwardGenParticipant( AwardGenParticipant awardGenParticipant )
  {
    this.awardGenParticipant = awardGenParticipant;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "AwardGenManager [awardGenBatch=" + awardGenBatch + ", user=" + user + ", awardGenParticipant=" + awardGenParticipant + ", expiryDate=" + expiryDate + "]";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( awardGenBatch == null ? 0 : awardGenBatch.hashCode() );
    result = prime * result + ( awardGenParticipant == null ? 0 : awardGenParticipant.hashCode() );
    result = prime * result + ( expiryDate == null ? 0 : expiryDate.hashCode() );
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
    AwardGenManager other = (AwardGenManager)obj;
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
    if ( awardGenParticipant == null )
    {
      if ( other.awardGenParticipant != null )
      {
        return false;
      }
    }
    else if ( !awardGenParticipant.equals( other.awardGenParticipant ) )
    {
      return false;
    }
    if ( expiryDate == null )
    {
      if ( other.expiryDate != null )
      {
        return false;
      }
    }
    else if ( !expiryDate.equals( other.expiryDate ) )
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
