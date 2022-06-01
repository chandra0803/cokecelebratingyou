/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/impl/PartnerCPStrategyFactoryImpl.java,v $
 */

package com.biperf.core.service.promotion.engine.impl;

import java.util.Map;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.promotion.engine.PartnerCPStrategy;
import com.biperf.core.service.promotion.engine.PartnerCPStrategyFactory;

public class PartnerCPStrategyFactoryImpl implements PartnerCPStrategyFactory
{

  /** Injected */
  private Map entries;

  /**
   * Overridden from @see com.biperf.core.service.promotion.engine.PartnerGoalStrategyFactoryImpl#getPartnerGoalStrategy(java.lang.String)
   * @param payoutStructure
   * @return
   */
  public PartnerCPStrategy getPartnerGoalStrategy( String payoutStructure )
  {
    PartnerCPStrategy cpStrategy = null;
    if ( payoutStructure != null )
    {
      cpStrategy = (PartnerCPStrategy)entries.get( payoutStructure );
    }
    if ( payoutStructure == null )
    {
      throw new BeaconRuntimeException( "Unknown Payout StructureCode: " + payoutStructure );
    }

    return cpStrategy;
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
