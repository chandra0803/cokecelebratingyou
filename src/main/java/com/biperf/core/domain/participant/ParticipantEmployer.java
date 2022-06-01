/*
 * Copyright 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.domain.AuditUpdateInfo;
import com.biperf.core.domain.AuditUpdateInterface;
import com.biperf.core.domain.employer.Employer;
import com.biperf.core.utils.UserManager;

/**
 * @author crosenquest Apr 28, 2005
 */
public class ParticipantEmployer implements AuditCreateInterface, AuditUpdateInterface, Serializable
{

  /** employer */
  private Employer employer;

  /** user */
  private Participant participant;

  /** Audit information - cannot be null */
  private AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
  private AuditUpdateInfo auditUpdateInfo = new AuditUpdateInfo();

  private String positionType;
  private String departmentType;
  private Date hireDate;
  private Date terminationDate;

  /**
   * Public constructor
   */
  public ParticipantEmployer()
  {
    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  /**
   * Constructor creates a new ParticipantEmployer populating the participant and the employer with
   * the params.
   * 
   * @param participant
   * @param employer
   */
  public ParticipantEmployer( Participant participant, Employer employer )
  {

    this.participant = participant;
    this.employer = employer;

    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  /**
   * Get the Employer.
   * 
   * @return employer
   */
  public Employer getEmployer()
  {
    return this.employer;
  }

  /**
   * Set the employer.
   * 
   * @param employer
   */
  public void setEmployer( Employer employer )
  {
    this.employer = employer;
  }

  /**
   * Get the Participant.
   * 
   * @return Participant
   */
  public Participant getParticipant()
  {
    return this.participant;
  }

  /**
   * Set the Participant.
   * 
   * @param participant
   */
  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  /**
   * Get the Audit Information Overridden from
   * 
   * @see com.biperf.core.domain.AuditCreateInterface#getAuditCreateInfo()
   * @return AuditInfo
   */
  public AuditCreateInfo getAuditCreateInfo()
  {
    return this.auditCreateInfo;
  }

  /**
   * Set the AuditInfo.
   * 
   * @param info
   */
  public void setAuditCreateInfo( AuditCreateInfo info )
  {
    this.auditCreateInfo = info;
  }

  public AuditUpdateInfo getAuditUpdateInfo()
  {
    return auditUpdateInfo;
  }

  public void setAuditUpdateInfo( AuditUpdateInfo auditUpdateInfo )
  {
    this.auditUpdateInfo = auditUpdateInfo;
  }

  public String getPositionType()
  {
    return positionType;
  }

  public void setPositionType( String positionType )
  {
    this.positionType = positionType;
  }

  public String getDepartmentType()
  {
    return departmentType;
  }

  public void setDepartmentType( String departmentType )
  {
    this.departmentType = departmentType;
  }

  public Date getHireDate()
  {
    return hireDate;
  }

  public void setHireDate( Date hireDate )
  {
    this.hireDate = hireDate;
  }

  public Date getTerminationDate()
  {
    return terminationDate;
  }

  public void setTerminationDate( Date terminationDate )
  {
    this.terminationDate = terminationDate;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "ParticipantEmployer [" );
    buf.append( "{participant_id =" + this.getParticipant().getId() + "}, " );
    buf.append( "{employer_id =" + this.getEmployer().getId() + "}" );
    buf.append( "]" );
    return buf.toString();
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof ParticipantEmployer ) )
    {
      return false;
    }

    final ParticipantEmployer participantEmployer = (ParticipantEmployer)object;

    if ( getEmployer() != null ? !getEmployer().equals( participantEmployer.getEmployer() ) : participantEmployer.getEmployer() != null )
    {
      return false;
    }
    if ( getParticipant() != null ? !getParticipant().equals( participantEmployer.getParticipant() ) : participantEmployer.getParticipant() != null )
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
    result = getEmployer() != null ? getEmployer().hashCode() : 0;
    result = 29 * result + ( getParticipant() != null ? getParticipant().hashCode() : 0 );

    return result;
  }
}
