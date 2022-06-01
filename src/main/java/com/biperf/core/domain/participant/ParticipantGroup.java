
package com.biperf.core.domain.participant;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

public class ParticipantGroup extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  private String groupName;
  private Participant groupCreatedBy;
  private Date dateCreated;
  private Set<ParticipantGroupDetails> groupDetails = new LinkedHashSet<ParticipantGroupDetails>();

  public String getGroupName()
  {
    return groupName;
  }

  public void setGroupName( String groupName )
  {
    this.groupName = groupName;
  }

  public Date getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( Date dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public Participant getGroupCreatedBy()
  {
    return groupCreatedBy;
  }

  public void setGroupCreatedBy( Participant groupCreatedBy )
  {
    this.groupCreatedBy = groupCreatedBy;
  }

  public Set<ParticipantGroupDetails> getGroupDetails()
  {
    return groupDetails;
  }

  public void setGroupDetails( Set<ParticipantGroupDetails> groupDetails )
  {
    this.groupDetails = groupDetails;
  }

  public void addDetails( ParticipantGroupDetails details )
  {
    details.setGroup( this );
    groupDetails.add( details );
  }

  public void removeDetails( ParticipantGroupDetails details )
  {
    groupDetails.remove( details );
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( getId() == null ? 0 : getId().intValue() );
    result = prime * result + ( getGroupName() == null ? 0 : getGroupName().hashCode() );
    result = prime * result + ( getGroupCreatedBy() == null ? 0 : getGroupCreatedBy().hashCode() );
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
    if ( ! ( obj instanceof ParticipantGroup ) )
    {
      return false;
    }

    ParticipantGroup other = (ParticipantGroup)obj;

    if ( this.getId() != null && other.getId() != null && this.getId().intValue() != other.getId().intValue() )
    {
      return false;
    }

    if ( this.getGroupName() != null && other.getGroupName() != null && this.getGroupName().hashCode() != other.getGroupName().hashCode() )
    {
      return false;
    }

    if ( this.getGroupCreatedBy() != null && other.getGroupCreatedBy() != null && this.getGroupCreatedBy().hashCode() != other.getGroupCreatedBy().hashCode() )
    {
      return false;
    }

    return true;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append( " { id : " ).append( this.getId() == null ? null : this.getId() ).append( " , groupCreatedBy : " ).append( this.groupCreatedBy == null ? null : this.getGroupCreatedBy().getId() );

    return builder.toString();
  }

}
