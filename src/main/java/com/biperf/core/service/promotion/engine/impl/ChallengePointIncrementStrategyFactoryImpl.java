/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/impl/ChallengePointIncrementStrategyFactoryImpl.java,v $
 */

package com.biperf.core.service.promotion.engine.impl;

import java.util.Map;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.promotion.engine.ChallengePointIncrementStrategy;
import com.biperf.core.service.promotion.engine.ChallengePointIncrementStrategyFactory;

/**
 * ChallengePointIncrementStrategyFactoryImpl.
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
 * <td>Jan 8, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChallengePointIncrementStrategyFactoryImpl implements ChallengePointIncrementStrategyFactory
{
  /** Injected */
  private Map entries;

  /**
   * Overridden from @see com.biperf.core.service.promotion.engine.ChallengePointIncrementStrategyFactory#getChallengePointIncrementStrategy(com.biperf.core.domain.enums.PayoutStructure)
   * @param incrementStructure
   * @return
   */
  public ChallengePointIncrementStrategy getChallengePointIncrementStrategy( String incrementStructure )
  {
    ChallengePointIncrementStrategy achievementStrategy = null;
    if ( incrementStructure != null )
    {
      achievementStrategy = (ChallengePointIncrementStrategy)entries.get( incrementStructure );
    }
    if ( achievementStrategy == null )
    {
      throw new BeaconRuntimeException( "Unknown IncrementStructureCode: " + incrementStructure );
    }
    return achievementStrategy;
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
