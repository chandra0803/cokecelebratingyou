/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/claim/ClaimProcessingStrategyFactory.java,v $
 */

package com.biperf.core.service.claim;

import com.biperf.core.domain.enums.PromotionType;

/**
 * ClaimProcessingStrategyFactory.
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
public interface ClaimProcessingStrategyFactory
{
  /**
   * @param promotionType
   * @return ClaimProcessingStrategy Implementation based on type.
   */
  public ClaimProcessingStrategy getClaimProcessingStrategy( PromotionType promotionType );
}
