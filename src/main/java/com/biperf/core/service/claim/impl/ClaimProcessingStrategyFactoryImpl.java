/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/claim/impl/ClaimProcessingStrategyFactoryImpl.java,v $
 */

package com.biperf.core.service.claim.impl;

import java.util.Map;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.claim.ClaimProcessingStrategy;
import com.biperf.core.service.claim.ClaimProcessingStrategyFactory;

/**
 * ClaimProcessingStrategyFactoryImpl.
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
 * <td>Oct 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimProcessingStrategyFactoryImpl implements ClaimProcessingStrategyFactory
{
  /** Injected */
  private Map entries;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimProcessingStrategyFactory#getClaimProcessingStrategy(com.biperf.core.domain.enums.PromotionType)
   * @param promotionType
   * @return ClaimProcessingStrategy
   */
  public ClaimProcessingStrategy getClaimProcessingStrategy( PromotionType promotionType )
  {
    String type = promotionType.getCode();

    ClaimProcessingStrategy claimProcessingStrategy = (ClaimProcessingStrategy)entries.get( type );
    if ( claimProcessingStrategy == null )
    {
      throw new BeaconRuntimeException( "Unknown claimProcessingStrategy: " + type );
    }

    return claimProcessingStrategy;
  }

  /**
   * @param entries value for entries property
   */
  public void setEntries( Map entries )
  {
    this.entries = entries;
  }

}
