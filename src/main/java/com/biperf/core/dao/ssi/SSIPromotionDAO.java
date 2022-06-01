
package com.biperf.core.dao.ssi;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.ssi.SSIPromotion;

public interface SSIPromotionDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "ssiPromotionDAO";

  public SSIPromotion getLiveSSIPromotion();

  public SSIPromotion getLiveOrCompletedSSIPromotion();

}
