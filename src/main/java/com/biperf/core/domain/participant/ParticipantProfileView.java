/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * ParticipantProfileView.java.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>putta</th>
 * <th>12/03/2012</th>
 * <th>1.0</th>
 *
 */
@JsonInclude( value = Include.NON_EMPTY )
public class ParticipantProfileView implements Serializable
{

  private static final long serialVersionUID = 1L;

  private ParticipantInfoView participant = new ParticipantInfoView();

  private String[] messages = {};

  public ParticipantInfoView getParticipant()
  {
    return participant;
  }

  public void setParticipant( ParticipantInfoView participant )
  {
    this.participant = participant;
  }

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  @Override
  @JsonIgnore
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) ( participant.getId() ^ participant.getId() >>> 32 );
    return result;
  }

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
    ParticipantProfileView other = (ParticipantProfileView)obj;
    if ( participant.getId() != other.participant.getId() )
    {
      return false;
    }
    return true;
  }

}
