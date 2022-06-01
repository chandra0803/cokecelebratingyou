/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/impl/PartnerGoalStrategyFactoryImpl.java,v $
 */

package com.biperf.core.service.promotion.engine.impl;

import java.util.Map;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.promotion.engine.PartnerGoalStrategy;
import com.biperf.core.service.promotion.engine.PartnerGoalStrategyFactory;

/**
 * PartnerGoalStrategyFactoryImpl.
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
 * <td>gadapa</td>
 * <td>Mar 31, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PartnerGoalStrategyFactoryImpl implements PartnerGoalStrategyFactory
{

  /** Injected */
  private Map entries;

  /**
   * Overridden from @see com.biperf.core.service.promotion.engine.PartnerGoalStrategyFactoryImpl#getPartnerGoalStrategy(java.lang.String)
   * @param payoutStructure
   * @return
   */
  public PartnerGoalStrategy getPartnerGoalStrategy( String payoutStructure )
  {
    PartnerGoalStrategy goalStrategy = null;
    if ( payoutStructure != null )
    {
      goalStrategy = (PartnerGoalStrategy)entries.get( payoutStructure );
    }
    if ( payoutStructure == null )
    {
      throw new BeaconRuntimeException( "Unknown Payout StructureCode: " + payoutStructure );
    }

    return goalStrategy;
  }

  public Map getEntries()
  {
    return entries;
  }

  public void setEntries( Map entries )
  {
    this.entries = entries;
  }

}
