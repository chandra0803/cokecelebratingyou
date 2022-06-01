/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/rewardoffering/RewardOfferingsService.java,v $
 */

package com.biperf.core.service.rewardoffering;

import java.util.List;

import com.biperf.core.service.SAO;
import com.biperf.services.rest.rewardoffering.domain.RewardOffering;

public interface RewardOfferingsService extends SAO
{
  /**
   * BEAN_NAME for referencing in tests and spring config files.
   */
  public final String BEAN_NAME = "rewardOfferingsService";

  public List<RewardOffering> getRewardOfferings( String programId );

  public void destroy();

}
