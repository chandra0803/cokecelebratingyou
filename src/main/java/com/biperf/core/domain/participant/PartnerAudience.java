/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/participant/PartnerAudience.java,v $
 * (c) 2008 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.enums.AudienceType;

/**
 * PartnerAudience.
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
 * <td>reddy</td>
 * <td>Feb 21, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */

public class PartnerAudience extends Audience
{
  private List audiencePartners = new ArrayList();
  private int size;

  public List getAudiencePartners()
  {
    return audiencePartners;
  }

  public void setAudiencePartners( List audiencePartners )
  {
    this.audiencePartners = audiencePartners;
    this.setSize( audiencePartners.size() );
  }

  public void setSize( int size )
  {
    this.size = size;
  }

  /**
   * Overridden from parent. Gets the size of the audiencePartners list.
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

  public void addPartner( Participant participant )
  {
    AudienceParticipant audienceParticipant = new AudienceParticipant();
    audienceParticipant.setParticipant( participant );
    audienceParticipant.setPartnerAudience( this );

    // add AudienceParticipant to PartnerAudience's collection
    this.getAudiencePartners().add( audienceParticipant );

  }

  public void removeParticipant( Participant participant )
  {
    // remove AudienceParticipant from PaxAudience's collection
    List audienceAudPartners = this.getAudiencePartners();
    for ( Iterator iter = audienceAudPartners.iterator(); iter.hasNext(); )
    {
      AudienceParticipant audienceParticipant = (AudienceParticipant)iter.next();
      if ( audienceParticipant.getParticipant().equals( participant ) )
      {
        iter.remove();
      }
    }

  }
}
