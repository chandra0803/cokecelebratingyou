/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion;

import java.util.Iterator;

import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.domain.promotion.StackRankParticipant;
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
 * <td>gaddam</td>
 * <td>Mar 9, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankNodeAssociationRequest extends BaseAssociationRequest
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  public static final int ALL_HYDRATION_LEVEL = 1;
  public static final int STACK_RANK_HYDRATION_LEVEL = 2;
  public static final int STACK_RANK_PARTICIPANTS_HYDRATION_LEVEL = 3;

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private int hydrationLevel;

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Constructs a <code>StackRankNodeAssociationRequest</code> object.
   * 
   * @param hydrationLevel the hydration level.
   */
  public StackRankNodeAssociationRequest( int hydrationLevel )
  {
    this.hydrationLevel = hydrationLevel;
  }

  /**
   * Hydrate the given {@link StackRankNode} object.
   * 
   * @param domainObject the {@link StackRankNode} object to hydrate.
   */
  public void execute( Object domainObject )
  {
    StackRankNode stackRankNode = (StackRankNode)domainObject;

    switch ( hydrationLevel )
    {
      case ALL_HYDRATION_LEVEL:
        hydrateStackRank( stackRankNode );
        hydrateStackRankParticipants( stackRankNode );
        break;
      case STACK_RANK_HYDRATION_LEVEL:
        hydrateStackRank( stackRankNode );
        break;
      case STACK_RANK_PARTICIPANTS_HYDRATION_LEVEL:
        hydrateStackRankParticipants( stackRankNode );
        break;
      default:
        break;
    }
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  private void hydrateStackRank( StackRankNode stackRankNode )
  {
    initialize( stackRankNode.getStackRank().getPromotion() );
  }

  private void hydrateStackRankParticipants( StackRankNode stackRankNode )
  {
    initialize( stackRankNode.getStackRankParticipants() );

    for ( Iterator iter = stackRankNode.getStackRankParticipants().iterator(); iter.hasNext(); )
    {
      StackRankParticipant stackRankparticipant = (StackRankParticipant)iter.next();

      initialize( stackRankparticipant.getParticipant() );
    }
  }
}
