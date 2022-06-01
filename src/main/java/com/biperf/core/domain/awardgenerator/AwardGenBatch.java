/*
 * (c) 2013 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/awardgenerator/AwardGenBatch.java,v $
 */

package com.biperf.core.domain.awardgenerator;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

/**
 * AwardGenAward
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
public class AwardGenBatch extends BaseDomain
{
  private AwardGenerator awardGen;
  private Date startDate;
  private Date endDate;
  private boolean useIssueDate;
  private Date issueDate;
  private Set<AwardGenParticipant> awardGenParticipants = new LinkedHashSet<AwardGenParticipant>();
  private Set<AwardGenManager> awardGenManagers = new LinkedHashSet<AwardGenManager>();

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  /**
   * Default Constructor
   */
  public AwardGenBatch()
  {
    super();
  }

  /**
   * @param id
   */
  public AwardGenBatch( Long id )
  {
    super( id );
  }

  public AwardGenerator getAwardGen()
  {
    return awardGen;
  }

  public void setAwardGen( AwardGenerator awardGen )
  {
    this.awardGen = awardGen;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  public boolean isUseIssueDate()
  {
    return useIssueDate;
  }

  public void setUseIssueDate( boolean useIssueDate )
  {
    this.useIssueDate = useIssueDate;
  }

  public boolean getUseIssueDate()
  {
    return useIssueDate;
  }

  public Date getIssueDate()
  {
    return issueDate;
  }

  public void setIssueDate( Date issueDate )
  {
    this.issueDate = issueDate;
  }

  public Set<AwardGenParticipant> getAwardGenParticipants()
  {
    return awardGenParticipants;
  }

  public void setAwardGenParticipants( Set<AwardGenParticipant> awardGenParticipants )
  {
    this.awardGenParticipants = awardGenParticipants;
  }

  public Set<AwardGenManager> getAwardGenManagers()
  {
    return awardGenManagers;
  }

  public void setAwardGenManagers( Set<AwardGenManager> awardGenManagers )
  {
    this.awardGenManagers = awardGenManagers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "AwardGenBatch [awardGen=" + awardGen + ", startDate=" + startDate + ", endDate=" + endDate + ", useIssueDate=" + useIssueDate + ", issueDate=" + issueDate + "]";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( awardGen == null ? 0 : awardGen.hashCode() );
    result = prime * result + ( endDate == null ? 0 : endDate.hashCode() );
    result = prime * result + ( issueDate == null ? 0 : issueDate.hashCode() );
    result = prime * result + ( startDate == null ? 0 : startDate.hashCode() );
    result = prime * result + ( useIssueDate ? 1231 : 1237 );
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
    AwardGenBatch other = (AwardGenBatch)obj;
    if ( awardGen == null )
    {
      if ( other.awardGen != null )
      {
        return false;
      }
    }
    else if ( !awardGen.equals( other.awardGen ) )
    {
      return false;
    }
    if ( endDate == null )
    {
      if ( other.endDate != null )
      {
        return false;
      }
    }
    else if ( !endDate.equals( other.endDate ) )
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
    if ( startDate == null )
    {
      if ( other.startDate != null )
      {
        return false;
      }
    }
    else if ( !startDate.equals( other.startDate ) )
    {
      return false;
    }
    if ( useIssueDate != other.useIssueDate )
    {
      return false;
    }
    return true;
  }

}
