
package com.biperf.core.domain.managertoolkit;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;

public class ParticipantAlert extends BaseDomain
{
  private Participant user;
  private AlertMessage alertMessage;
  private Node node;
  private boolean read;

  public Participant getUser()
  {
    return user;
  }

  public void setUser( Participant user )
  {
    this.user = user;
  }

  public AlertMessage getAlertMessage()
  {
    return alertMessage;
  }

  public void setAlertMessage( AlertMessage alertMessage )
  {
    this.alertMessage = alertMessage;
  }

  public Node getNode()
  {
    return node;
  }

  public void setNode( Node node )
  {
    this.node = node;
  }

  public boolean isRead()
  {
    return read;
  }

  public void setRead( boolean read )
  {
    this.read = read;
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    else if ( object instanceof ParticipantAlert )
    {
      ParticipantAlert participantAlert = (ParticipantAlert)object;
      User participant = participantAlert.getUser();
      if ( this.getUser().getId() != null ? !this.getUser().getId().equals( participant.getId() ) : participant.getId() != null )
      {
        return false;
      }

      return true;
    }
    else
    {
      return false;
    }
  }

  public int hashCode()
  {
    int result = 0;

    result = this.getUser().getId() != null ? this.getUser().getId().hashCode() : 0;

    return result;
  }

}
