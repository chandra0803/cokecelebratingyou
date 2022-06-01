/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/activity/impl/SalesActivityAssociationRequest.java,v $
 */

package com.biperf.core.service.activity.impl;

import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.service.BaseAssociationRequest;

/**
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
 * <td>robinsra</td>
 * <td>Jul 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SalesActivityAssociationRequest extends BaseAssociationRequest
{
  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int CLAIM = 2;
  public static final int PRODUCT = 3;

  public SalesActivityAssociationRequest( int hydrateLevel )
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
    SalesActivity salesActivity = (SalesActivity)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
      {
        hydrateAll( salesActivity );
        break;
      }
      case CLAIM:
      {
        hydrateClaim( salesActivity );
        break;
      }
      case PRODUCT:
      {
        hydrateProduct( salesActivity );
        break;
      }
      default:
      {
        break;
      }
    } // switch
  }

  /**
   * hydrates the promotion.
   * 
   * @param salesActivity
   */
  private void hydrateClaim( SalesActivity salesActivity )
  {
    initialize( salesActivity.getClaim() );
  }

  /**
   * hydrates the promotion.
   * 
   * @param salesActivity
   */
  private void hydrateProduct( SalesActivity salesActivity )
  {
    initialize( salesActivity.getProduct() );
  }

  /**
   * hydrates all pieces of the salesActivity
   * 
   * @param salesActivity
   */
  private void hydrateAll( SalesActivity salesActivity )
  {
    hydrateClaim( salesActivity );
    hydrateProduct( salesActivity );
  }
}
