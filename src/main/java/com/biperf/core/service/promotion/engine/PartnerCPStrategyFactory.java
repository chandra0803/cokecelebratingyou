/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/PartnerCPStrategyFactory.java,v $
 */

package com.biperf.core.service.promotion.engine;

public interface PartnerCPStrategyFactory
{

  /**
   * Construct a PartnerCPStrategyFactory instance. The type of strategy is determined by the override structure.
   * 
   * @param payoutStructure
   * @return PartnerGoalStrategyFactory
   */
  public PartnerCPStrategy getPartnerGoalStrategy( String payoutStructure );

}
