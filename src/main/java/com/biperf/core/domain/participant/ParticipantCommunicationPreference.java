/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/participant/ParticipantCommunicationPreference.java,v $
 */

package com.biperf.core.domain.participant;

import java.io.Serializable;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;

/**
 * ParticipantCommunicationPreference.
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
 * <td>Sep 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantCommunicationPreference implements AuditCreateInterface, Serializable
{

  private Long id;

  /** The participant which is associated with these contact methods. */
  private Participant participant;

  /** The contactMethod code which are available for the participant. */
  private ParticipantPreferenceCommunicationsType participantPreferenceCommunicationsType;

  /** the id of the textMessage if this comm preference is of type text message */
  private MessageSMSGroupType messageSMSGroupType;

  /** Audit information - cannot be null */
  private AuditCreateInfo auditCreateInfo;

  /**
   * Construct this with a participant and a contactMethods.
   * 
   * @param participant
   * @param participantPreferenceCommunicationsType
   */
  public ParticipantCommunicationPreference( Participant participant, ParticipantPreferenceCommunicationsType participantPreferenceCommunicationsType )
  {
    this.participant = participant;
    this.participantPreferenceCommunicationsType = participantPreferenceCommunicationsType;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  /**
   * Default constructor does nothing...
   */
  public ParticipantCommunicationPreference()
  {
    // nothing
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

  public MessageSMSGroupType getMessageSMSGroupType()
  {
    return messageSMSGroupType;
  }

  public void setMessageSMSGroupType( MessageSMSGroupType messageSMSGroupType )
  {
    this.messageSMSGroupType = messageSMSGroupType;
  }

  /**
   * Get the participantPreferenceCommunicationsType.
   * 
   * @return participantPreferenceCommunicationsType
   */
  public ParticipantPreferenceCommunicationsType getParticipantPreferenceCommunicationsType()
  {
    return this.participantPreferenceCommunicationsType;
  }

  /**
   * Set this participantPreferenceCommunicationsType.
   * 
   * @param participantPreferenceCommunicationsType
   */
  public void setParticipantPreferenceCommunicationsType( ParticipantPreferenceCommunicationsType participantPreferenceCommunicationsType )
  {
    this.participantPreferenceCommunicationsType = participantPreferenceCommunicationsType;
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
    buf.append( "ParticipantCommunicationPreference [" );
    buf.append( "{participant.id=" + this.getParticipant().getId() + "}, " );
    buf.append( "{participantPreferenceCommunicationsType=" + this.getParticipantPreferenceCommunicationsType() + "} " );
    buf.append( "{messageSMSGroupType=" + this.getMessageSMSGroupType() + "}, " );
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

    if ( ! ( object instanceof ParticipantCommunicationPreference ) )
    {
      return false;
    }

    final ParticipantCommunicationPreference participantCommunicationPreference = (ParticipantCommunicationPreference)object;

    if ( getId() != null ? !getId().equals( participantCommunicationPreference.getId() ) : participantCommunicationPreference.getId() != null )
    {
      return false;
    }

    if ( getParticipantPreferenceCommunicationsType() != null
        ? !getParticipantPreferenceCommunicationsType().equals( participantCommunicationPreference.getParticipantPreferenceCommunicationsType() )
        : participantCommunicationPreference.getParticipantPreferenceCommunicationsType() != null )
    {
      return false;
    }

    if ( getMessageSMSGroupType() != null
        ? !getMessageSMSGroupType().equals( participantCommunicationPreference.getMessageSMSGroupType() )
        : participantCommunicationPreference.getMessageSMSGroupType() != null )
    {
      return false;
    }

    if ( getParticipant() != null ? !getParticipant().equals( participantCommunicationPreference.getParticipant() ) : participantCommunicationPreference.getParticipant() != null )
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

    result = getParticipantPreferenceCommunicationsType() != null ? getParticipantPreferenceCommunicationsType().hashCode() : 0;
    result = 13 * result + ( getParticipant() != null ? getParticipant().hashCode() : 0 );
    result = 17 * result + ( getMessageSMSGroupType() != null ? getMessageSMSGroupType().hashCode() : 0 );

    return result;
  }

}
