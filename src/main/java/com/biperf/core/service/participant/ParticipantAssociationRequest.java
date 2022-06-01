/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/ParticipantAssociationRequest.java,v $
 */

package com.biperf.core.service.participant;

import java.util.Iterator;

import com.biperf.core.domain.employer.Employer;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * ParticipantAssociationRequest.
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
 * <td>meadows</td>
 * <td>Oct 5, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantAssociationRequest extends BaseAssociationRequest
{
  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int ADDRESS_BOOK = 2;
  public static final int PARTICIPANT = 3;
  public static final int TWITTER = 4;
  public static final int FACEBOOK = 5;
  public static final int USER_NODE = 6;
  public static final int EMPLOYER = 7;
  public static final int ADDRESSES = 8;
  public static final int PHONES = 9;
  public static final int EMAILS = 10;

  public ParticipantAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    Participant participant = (Participant)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
      {
        AssociationRequest userAssociationRequest = new UserAssociationRequest( UserAssociationRequest.ALL );
        userAssociationRequest.execute( domainObject );
        hydrateAddressBooks( participant );
        hydrateTwitter( participant );
        hydrateFacebook( participant );
        break;
      }
      case ADDRESS_BOOK:
      {
        hydrateAddressBooks( participant );
        break;
      }
      case PARTICIPANT:
      {
        initialize( participant );
        break;
      }
      case TWITTER:
      {
        hydrateTwitter( participant );
        break;
      }
      case FACEBOOK:
      {
        hydrateFacebook( participant );
        break;
      }
      case USER_NODE:
      {
        hydrateUserNode( participant );
        break;
      }
      case EMPLOYER:
      {
        hydrateEmployer( participant );

        for ( Iterator iter = participant.getParticipantEmployers().iterator(); iter.hasNext(); )
        {
          ParticipantEmployer temp = (ParticipantEmployer)iter.next();
          Employer employer = temp.getEmployer();

          initialize( employer );
        }

        break;
      }
      case ADDRESSES:
      {
        hydrateAddress( participant );
        break;
      }
      case PHONES:
      {
        hydratePhone( participant );
        break;
      }
      case EMAILS:
      {
        hydrateEmail( participant );
        break;
      }
      default:
      {
        break;
      }
    } // switch
  }

  /**
   * @param user
   */
  private void hydrateAddressBooks( Participant participant )
  {
    initialize( participant.getParticipantAddressBooks() );
  }

  private void hydrateTwitter( Participant participant )
  {
    initialize( participant.getUserTwitter() );
  }

  private void hydrateFacebook( Participant participant )
  {
    initialize( participant.getUserFacebook() );
  }

  private void hydrateUserNode( Participant participant )
  {
    initialize( participant.getUserNodes() );
  }

  private void hydrateEmployer( Participant participant )
  {
    initialize( participant.getParticipantEmployers() );
  }

  private void hydrateAddress( Participant participant )
  {
    initialize( participant.getUserAddresses() );
  }

  private void hydratePhone( Participant participant )
  {
    initialize( participant.getUserPhones() );
  }

  private void hydrateEmail( Participant participant )
  {
    initialize( participant.getUserEmailAddresses() );
  }
}
