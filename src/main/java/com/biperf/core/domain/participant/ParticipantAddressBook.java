/*
 * Copyright 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import java.sql.Timestamp;

import com.biperf.core.domain.Address;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;

/**
 * @author crosenquest Apr 27, 2005
 */
public class ParticipantAddressBook extends BaseDomain
{
  private Participant participant;
  private String guid;
  private String firstname;
  private String lastname;
  private Address address;
  private String emailAddr;

  public ParticipantAddressBook()
  {
    super();
    setGuid( GuidUtils.generateGuid() );
    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
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
    buf.append( "PARTICIPANT [" );
    buf.append( "{USER " ).append( super.toString() ).append( "}, " );
    buf.append( "{guid=" ).append( this.getGuid() ).append( "}, " );
    buf.append( "{firstname=" ).append( this.getFirstname() ).append( "}, " );
    buf.append( "{lastname=" ).append( this.getLastname() ).append( "}, " );
    buf.append( "{address=" ).append( this.getAddress() ).append( "}, " );
    buf.append( "{emailAddr=" ).append( this.getEmailAddr() ).append( "} " );
    buf.append( "]" );
    return buf.toString();
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof ParticipantAddressBook ) )
    {
      return false;
    }

    final ParticipantAddressBook address = (ParticipantAddressBook)o;

    if ( getGuid() != null ? !getGuid().equals( address.getGuid() ) : address.getGuid() != null )
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
    return getGuid() != null ? getGuid().hashCode() : 0;
  }

  public String getEmailAddr()
  {
    return emailAddr;
  }

  public void setEmailAddr( String emailAddr )
  {
    this.emailAddr = emailAddr;
  }

  public String getFirstname()
  {
    return firstname;
  }

  public Address getAddress()
  {
    return address;
  }

  public void setAddress( Address address )
  {
    this.address = address;
  }

  public void setFirstname( String firstname )
  {
    this.firstname = firstname;
  }

  public String getGuid()
  {
    return guid;
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  public String getLastname()
  {
    return lastname;
  }

  public void setLastname( String lastname )
  {
    this.lastname = lastname;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }
}
