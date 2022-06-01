/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/PromoMerchCountryAssociationRequest.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.Iterator;

import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * PromoMerchCountryAssociationRequest.
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
 * <td>babu</td>
 * <td>Aug 2, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class PromoMerchCountryAssociationRequest extends BaseAssociationRequest
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  public static final int ALL_HYDRATION_LEVEL = 1;

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private int hydrationLevel;

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Constructs a <code>PromoMerchCountryAssociationRequest</code> object.
   *
   * @param hydrationLevel the hydration level.
   */
  public PromoMerchCountryAssociationRequest( int hydrationLevel )
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
    PromoMerchCountry promoMerchCountry = (PromoMerchCountry)domainObject;

    switch ( hydrationLevel )
    {
      case ALL_HYDRATION_LEVEL:
        hydrateMerchCountry( promoMerchCountry );
        break;
      default:
        break;
    }
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  private void hydrateMerchCountry( PromoMerchCountry promoMerchCountry )
  {
    if ( promoMerchCountry == null || promoMerchCountry.getLevels() == null )
    {
      return;
    }
    for ( Iterator iter = promoMerchCountry.getLevels().iterator(); iter.hasNext(); )
    {
      PromoMerchProgramLevel promoMerchProgramLevel = (PromoMerchProgramLevel)iter.next();
      initialize( promoMerchProgramLevel );
    }
  }
}
