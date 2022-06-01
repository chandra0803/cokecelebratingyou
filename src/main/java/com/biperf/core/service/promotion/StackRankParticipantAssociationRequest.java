/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion;

import com.biperf.core.domain.promotion.StackRankParticipant;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * StackRankParticipantAssociationRequest.
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
 * <td>zahler</td>
 * <td>Mar 17, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankParticipantAssociationRequest extends BaseAssociationRequest
{

  protected int hydrateLevel = 0;

  /**
   * Hyrate Level: ALL
   */
  public static final int ALL = 1;

  /**
   * Hydrate Level: PARTICIPANT
   */
  public static final int PARTICIPANT = 2;

  /**
   * Hydrate Level: NODE
   */
  public static final int NODE = 3;

  /**
   * Constructor with hydrateLevel as arg
   * 
   * @param hydrateLevel
   */
  public StackRankParticipantAssociationRequest( int hydrateLevel )
  {
    super();
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(java.lang.Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    StackRankParticipant stackRankParticipant = (StackRankParticipant)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateParticipant( stackRankParticipant );
        hydrateStackRankNode( stackRankParticipant );
        break;

      case PARTICIPANT:
        hydrateParticipant( stackRankParticipant );
        break;

      case NODE:
        hydrateStackRankNode( stackRankParticipant );
        break;

      default:
        break;
    } // switch

  }

  /**
   * Loads the Participant definition attached on this stackRankParticipant object
   * 
   * @param stackRankParticipant
   */
  private void hydrateParticipant( StackRankParticipant stackRankParticipant )
  {
    initialize( stackRankParticipant.getParticipant() );
  }

  /**
   * Loads the stackRankNode attached on this stackRankParticipant object
   * 
   * @param stackRankParticipant
   */
  private void hydrateStackRankNode( StackRankParticipant stackRankParticipant )
  {
    initialize( stackRankParticipant.getStackRankNode() );
  }

}
