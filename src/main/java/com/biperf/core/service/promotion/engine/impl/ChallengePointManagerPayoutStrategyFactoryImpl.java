/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/impl/ChallengePointManagerPayoutStrategyFactoryImpl.java,v $
 */

package com.biperf.core.service.promotion.engine.impl;

import java.util.Map;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.promotion.engine.ChallengePointManagerPayoutStrategy;
import com.biperf.core.service.promotion.engine.ChallengePointManagerPayoutStrategyFactory;

/**
 * ChallengePointManagerPayoutStrategyFactoryImpl.
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
 * <td>Babu </td>
 * <td>Aug 13, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChallengePointManagerPayoutStrategyFactoryImpl implements ChallengePointManagerPayoutStrategyFactory
{
  /** Injected */
  private Map entries;

  /**
   * Overridden from @see com.biperf.core.service.promotion.engine.ChallengePointManagerPayoutStrategyFactory#getChallengePointAchievementStrategy(com.biperf.core.domain.enums.PayoutStructure)
   * @param ManagerPayoutStructure
   * @return
   */
  public ChallengePointManagerPayoutStrategy getChallengePointManagerPayoutStrategy( String achievementStructure )
  {

    ChallengePointManagerPayoutStrategy achievementStrategy = null;
    if ( achievementStructure != null )
    {
      achievementStrategy = (ChallengePointManagerPayoutStrategy)entries.get( achievementStructure );
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
  public ChallengePointManagerPayoutStrategy getChallengePointManagerPayoutStrategy()
  {
    // Default to fixed for merch/travel
    return getChallengePointManagerPayoutStrategy( "ovrper" );
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
