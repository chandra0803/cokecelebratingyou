/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/StackRankAssociationRequest.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.Iterator;

import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * StackRankAssociationRequest.
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
 * <td>sedey</td>
 * <td>March 15, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankAssociationRequest extends BaseAssociationRequest
{
  protected int hydrateLevel = 0;

  /**
   * Hyrate Level: ALL
   */
  public static final int ALL = 1;

  /**
   * Hydrate Level: PROMOTION
   */
  public static final int PROMOTION = 2;

  /**
   * Hydrate Level: NODES
   */
  public static final int NODES = 3;

  /**
   * Constructor with hydrateLevel as arg
   * 
   * @param hydrateLevel
   */
  public StackRankAssociationRequest( int hydrateLevel )
  {
    super();
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
    StackRank stackRank = (StackRank)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydratePromotion( stackRank );
        hydrateStackRankNodes( stackRank );
        break;

      case PROMOTION:
        hydratePromotion( stackRank );
        break;

      case NODES:
        hydrateStackRankNodes( stackRank );
        break;

      default:
        break;
    } // switch
  }

  /**
   * Loads the promotion definition attached on this stackRank object
   * 
   * @param stackRank
   */
  private void hydratePromotion( StackRank stackRank )
  {
    initialize( stackRank.getPromotion() );
  }

  /**
   * Loads the stackRankNodes attached on this stackRank object
   * 
   * @param stackRank
   */
  private void hydrateStackRankNodes( StackRank stackRank )
  {
    initialize( stackRank.getStackRankNodes() );

    for ( Iterator iter = stackRank.getStackRankNodes().iterator(); iter.hasNext(); )
    {
      StackRankNode stackRankNode = (StackRankNode)iter.next();

      initialize( stackRankNode.getNode() );
    }
  }
}
