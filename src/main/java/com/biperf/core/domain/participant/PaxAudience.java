/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/participant/PaxAudience.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.enums.AudienceType;

/**
 * PaxAudience.
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
 * <td>Jun 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PaxAudience extends Audience
{
  private List audienceParticipants = new ArrayList();
  private int size;

  /**
   * Returns a deep copy of this object.
   *
   * @param cloneWithChildren
   * @param newAudienceName   the audience name to be assigned to the copy.
   * @throws CloneNotSupportedException if one of this objects component objects
   *         does not support cloning.
   */
  public Object deepCopy( boolean cloneWithChildren, String newAudienceName, List newChildAudienceNameHolders ) throws CloneNotSupportedException
  {
    PaxAudience clonedPaxAudience = (PaxAudience)super.deepCopy( cloneWithChildren, newAudienceName, newChildAudienceNameHolders );

    clonedPaxAudience.setSize( this.size );
    clonedPaxAudience.setAudienceParticipants( new ArrayList() );
    if ( null != audienceParticipants && this.audienceParticipants.size() > 0 )
    {
      for ( Iterator iter = getAudienceParticipants().iterator(); iter.hasNext(); )
      {
        AudienceParticipant audienceParticipant = (AudienceParticipant)iter.next();
        AudienceParticipant clone = audienceParticipant.deepCopy();
        clonedPaxAudience.addPaxAudience( clone );
      }
    }
    return clonedPaxAudience;
  }

  public void addPaxAudience( AudienceParticipant audienceParticipant )
  {
    audienceParticipant.setPaxAudience( this );
    this.audienceParticipants.add( audienceParticipant );

  }

  public List getAudienceParticipants()
  {
    return audienceParticipants;
  }

  public void setAudienceParticipants( List audienceParticipants )
  {
    this.audienceParticipants = audienceParticipants;
    this.setSize( audienceParticipants.size() );
  }

  public void setSize( int size )
  {
    this.size = size;
  }

  /**
   * Overridden from parent. Gets the size of the audienceParticipant list.
   * 
   * @return int
   */
  public int getSize()
  {
    return this.size;
  }

  public AudienceType getAudienceType()
  {
    return AudienceType.lookup( AudienceType.SPECIFIC_PAX_TYPE );
  }

  public void addParticipant( Participant participant )
  {
    AudienceParticipant audienceParticipant = new AudienceParticipant();
    audienceParticipant.setParticipant( participant );
    audienceParticipant.setPaxAudience( this );

    // add AudienceParticipant to PaxAudience's collection
    this.getAudienceParticipants().add( audienceParticipant );

  }

  public void removeParticipant( Participant participant )
  {
    // remove AudienceParticipant from PaxAudience's collection
    List audienceAudParticipants = this.getAudienceParticipants();
    for ( Iterator iter = audienceAudParticipants.iterator(); iter.hasNext(); )
    {
      AudienceParticipant audienceParticipant = (AudienceParticipant)iter.next();
      if ( audienceParticipant.getParticipant().equals( participant ) )
      {
        iter.remove();
      }
    }

  }
}
