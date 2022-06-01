/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/impl/ChallengePointAchievementStrategyFactoryImpl.java,v $
 */

package com.biperf.core.service.promotion.engine.impl;

import java.util.Map;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.promotion.engine.ChallengePointAchievementStrategy;
import com.biperf.core.service.promotion.engine.ChallengePointAchievementStrategyFactory;

/**
 * ChallengePointAchievementStrategyFactoryImpl.
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
public class ChallengePointAchievementStrategyFactoryImpl implements ChallengePointAchievementStrategyFactory
{
  /** Injected */
  private Map entries;

  /**
   * Overridden from @see com.biperf.core.service.promotion.engine.ChallengePointAchievementStrategyFactory#getChallengePointAchievementStrategy(com.biperf.core.domain.enums.PayoutStructure)
   * @param achievementStructure
   * @return
   */
  public ChallengePointAchievementStrategy getChallengePointAchievementStrategy( String achievementStructure )
  {

    ChallengePointAchievementStrategy achievementStrategy = null;
    if ( achievementStructure != null )
    {
      achievementStrategy = (ChallengePointAchievementStrategy)entries.get( achievementStructure );
    }
    if ( achievementStrategy == null )
    {
      throw new BeaconRuntimeException( "Unknown PayoutStructureCode: " + achievementStructure );
    }

    return achievementStrategy;
  }

  /**
   * Overridden from @see com.biperf.core.service.promotion.engine.ChallengePointAchievementStrategyFactory#getChallengePointAchievementStrategy(com.biperf.core.domain.enums.PayoutStructure)
   * @return
   */
  public ChallengePointAchievementStrategy getChallengePointAchievementStrategy()
  {
    // Default to fixed for merch/travel
    return getChallengePointAchievementStrategy( "fixed" );
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
