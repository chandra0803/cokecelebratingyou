
package com.biperf.core.domain.participant;

import com.biperf.core.domain.BaseDomain;

public class ParticipantGroupDetails extends BaseDomain
{

  private ParticipantGroup group;
  private Participant participant;

  private static final long serialVersionUID = 1L;

  public ParticipantGroup getGroup()
  {
    return group;
  }

  public void setGroup( ParticipantGroup group )
  {
    this.group = group;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public static void main( String[] args )
  {
    System.out.println( 999998 >> 29 );
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( getId() == null ? 0 : getId().intValue() );
    result = prime * result + ( group == null ? 0 : group.hashCode() );
    result = prime * result + ( participant == null ? 0 : participant.hashCode() );
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
    if ( ! ( obj instanceof ParticipantGroupDetails ) )
    {
      return false;
    }
    ParticipantGroupDetails other = (ParticipantGroupDetails)obj;

    if ( this.getId() != null && other.getId() != null && this.getId().intValue() != other.getId().intValue() )
    {
      return false;
    }

    if ( this.getGroup() != null && other.getGroup() != null && this.getGroup().hashCode() != other.getGroup().hashCode() )
    {
      return false;
    }

    if ( this.getParticipant() != null && other.getParticipant() != null && this.getParticipant().getId().longValue() != other.getParticipant().getId().longValue() )
    {
      return false;
    }

    return true;
  }

  @Override
  public String toString()
  {

    StringBuilder builder = new StringBuilder();
    builder.append( "Detail id : " ).append( this.getId() == null ? null : this.getId() ).append( "group id: " ).append( this.getGroup() == null ? null : this.getGroup().toString() )
        .append( "participant Id : " ).append( this.getParticipant() == null ? null : this.getParticipant().getId() );

    return builder.toString();
  }

}
