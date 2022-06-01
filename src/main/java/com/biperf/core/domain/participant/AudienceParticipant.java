/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/participant/AudienceParticipant.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import java.io.Serializable;
import java.sql.Timestamp;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.utils.UserManager;

/**
 * AudienceParticipant.
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
 * <td>sharma</td>
 * <td>Jun 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AudienceParticipant implements AuditCreateInterface, Serializable
{
  private PaxAudience paxAudience;
  private Participant participant;
  private AuditCreateInfo auditCreateInfo;
  private PartnerAudience partnerAudience;
  private CriteriaAudience criteriaAudience;

  public AudienceParticipant()
  {
    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  public AudienceParticipant( PaxAudience paxAudience, Participant participant )
  {
    this.paxAudience = paxAudience;
    this.participant = participant;
    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  public AudienceParticipant( PartnerAudience partnerAudience, Participant participant )
  {
    this.partnerAudience = partnerAudience;
    this.participant = participant;
    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  public AuditCreateInfo getAuditCreateInfo()
  {
    if ( this.auditCreateInfo == null )
    {
      this.auditCreateInfo = new AuditCreateInfo();
    }
    return this.auditCreateInfo;
  }

  public void setAuditCreateInfo( AuditCreateInfo auditCreateInfo )
  {
    this.auditCreateInfo = auditCreateInfo;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public PaxAudience getPaxAudience()
  {
    return paxAudience;
  }

  public void setPaxAudience( PaxAudience paxAudience )
  {
    this.paxAudience = paxAudience;
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof AudienceParticipant ) )
    {
      return false;
    }

    final AudienceParticipant otherAudienceParticipant = (AudienceParticipant)o;

    if ( getPaxAudience() != null ? !getPaxAudience().equals( otherAudienceParticipant.getPaxAudience() ) : otherAudienceParticipant.getPaxAudience() != null )
    {
      return false;
    }
    if ( getParticipant() != null ? !getParticipant().equals( otherAudienceParticipant.getParticipant() ) : otherAudienceParticipant.getParticipant() != null )
    {
      return false;
    }
    if ( getPartnerAudience() != null ? !getPartnerAudience().equals( otherAudienceParticipant.getPartnerAudience() ) : otherAudienceParticipant.getPartnerAudience() != null )
    {
      return false;
    }
    if ( getCriteriaAudience() != null ? !getCriteriaAudience().equals( otherAudienceParticipant.getCriteriaAudience() ) : otherAudienceParticipant.getCriteriaAudience() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = getPaxAudience() != null ? getPaxAudience().hashCode() : 0;
    result += getPartnerAudience() != null ? getPartnerAudience().hashCode() : 0;
    result += getCriteriaAudience() != null ? getCriteriaAudience().hashCode() : 0;
    result = 31 * result + ( getParticipant() != null ? getParticipant().hashCode() : 0 );

    return result;
  }

  public AudienceParticipant deepCopy()
  {
    AudienceParticipant clone = new AudienceParticipant();
    clone.setParticipant( getParticipant() );
    clone.setPaxAudience( getPaxAudience() );
    clone.setPartnerAudience( getPartnerAudience() );
    clone.setCriteriaAudience( getCriteriaAudience() );

    return clone;
  }

  public PartnerAudience getPartnerAudience()
  {
    return partnerAudience;
  }

  public void setPartnerAudience( PartnerAudience partnerAudience )
  {
    this.partnerAudience = partnerAudience;
  }

  public CriteriaAudience getCriteriaAudience()
  {
    return criteriaAudience;
  }

  public void setCriteriaAudience( CriteriaAudience criteriaAudience )
  {
    this.criteriaAudience = criteriaAudience;
  }

}
