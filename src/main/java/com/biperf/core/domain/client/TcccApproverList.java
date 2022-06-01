
package com.biperf.core.domain.client;

import com.biperf.core.domain.BaseDomain;

/**
 * TcccApproverList.
 * This class is created as part of Client customization for WIP #42701
 * @author dudam
 * @since Oct 1, 2018
 * @version 1.0
 */
public class TcccApproverList extends BaseDomain
{
  private static final long serialVersionUID = 1L;
  private String participantLoginID;
  private String approverLoginID;

  public String getParticipantLoginID()
  {
    return participantLoginID;
  }

  public void setParticipantLoginID( String participantLoginID )
  {
    this.participantLoginID = participantLoginID;
  }

  public String getApproverLoginID()
  {
    return approverLoginID;
  }

  public void setApproverLoginID( String approverLoginID )
  {
    this.approverLoginID = approverLoginID;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( approverLoginID == null ) ? 0 : approverLoginID.hashCode() );
    result = prime * result + ( ( participantLoginID == null ) ? 0 : participantLoginID.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
      return true;
    if ( obj == null )
      return false;
    if ( getClass() != obj.getClass() )
      return false;
    TcccApproverList other = (TcccApproverList)obj;
    if ( approverLoginID == null )
    {
      if ( other.approverLoginID != null )
        return false;
    }
    else if ( !approverLoginID.equals( other.approverLoginID ) )
      return false;
    if ( participantLoginID == null )
    {
      if ( other.participantLoginID != null )
        return false;
    }
    else if ( !participantLoginID.equals( other.participantLoginID ) )
      return false;
    return true;
  }

}
