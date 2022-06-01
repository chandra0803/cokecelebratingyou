/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/participant/ParticipantContactMethod.java,v $
 */

package com.biperf.core.domain.participant;

import java.io.Serializable;
import java.sql.Timestamp;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.domain.enums.ContactMethod;
import com.biperf.core.utils.UserManager;

/**
 * ParticipantContactMethod: Domain object to hold the association between the participant and their
 * contact methods.
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
 * <td>crosenquest</td>
 * <td>May 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantContactMethod implements AuditCreateInterface, Serializable
{

  /** The participant which is associated with these contact methods. */
  private Participant participant;

  /** The contactMethod code which are available for the participant. */
  private ContactMethod contactMethodCode;

  /** Audit information - cannot be null */
  private AuditCreateInfo auditCreateInfo;

  /** Defines this as a primary contact method for the participant. */
  private Boolean primary;

  /**
   * Construct this with a participant and a contactMethods.
   * 
   * @param participant
   * @param contactMethodCode
   * @param primary
   */
  public ParticipantContactMethod( Participant participant, ContactMethod contactMethodCode, Boolean primary )
  {
    this();
    this.participant = participant;
    this.contactMethodCode = contactMethodCode;
    this.primary = primary;
  }

  /**
   * Default constructor does nothing...
   */
  public ParticipantContactMethod()
  {
    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  /**
   * Set the participant.
   * 
   * @param participant
   */
  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  /**
   * Get the participant.
   * 
   * @return participant
   */
  public Participant getParticipant()
  {
    return this.participant;
  }

  /**
   * Hibernate specific method needed for getting the boolean property from this.
   * 
   * @return Boolean
   */
  public Boolean getPrimary()
  {
    return this.primary;
  }

  /**
   * Get the boolean value to determine if this ParticipantConcactMethod is the primary contact
   * method for the participant.
   * 
   * @return Boolean
   */
  public Boolean isPrimary()
  {
    return this.primary;
  }

  /**
   * Set the boolean value to determine if this is the primary ContactMethod for the participant.
   * 
   * @param primary
   */
  public void setPrimary( Boolean primary )
  {
    this.primary = primary;
  }

  /**
   * Get the contactMethodCode.
   * 
   * @return contactMethodCode
   */
  public ContactMethod getContactMethodCode()
  {
    return this.contactMethodCode;
  }

  /**
   * Set this contactMethodCode.
   * 
   * @param contactMethodCode
   */
  public void setContactMethodCode( ContactMethod contactMethodCode )
  {
    this.contactMethodCode = contactMethodCode;
  }

  /**
   * Get the Audit Information Overridden from
   * 
   * @see com.biperf.core.domain.AuditCreateInterface#getAuditCreateInfo()
   * @return AuditInfo
   */
  public AuditCreateInfo getAuditCreateInfo()
  {
    if ( this.auditCreateInfo == null )
    {
      this.auditCreateInfo = new AuditCreateInfo();
    }
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

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "PARTICIPANTCONTACTMETHOD [" );
    buf.append( "{participant.id=" + this.getParticipant().getId() + "}, " );
    buf.append( "{contactMethodCode=" + this.getContactMethodCode() + "}, " );
    buf.append( "{primary=" + this.getPrimary() + "}" );
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
    if ( ! ( object instanceof ParticipantContactMethod ) )
    {
      return false;
    }

    final ParticipantContactMethod participantContactMethod = (ParticipantContactMethod)object;

    if ( getContactMethodCode() != null ? !getContactMethodCode().equals( participantContactMethod.getContactMethodCode() ) : participantContactMethod.getContactMethodCode() != null )
    {
      return false;
    }
    if ( getParticipant() != null ? !getParticipant().equals( participantContactMethod.getParticipant() ) : participantContactMethod.getParticipant() != null )
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
    result = getContactMethodCode() != null ? getContactMethodCode().hashCode() : 0;
    result = 29 * result + ( getParticipant() != null ? getParticipant().hashCode() : 0 );

    return result;
  }

}
