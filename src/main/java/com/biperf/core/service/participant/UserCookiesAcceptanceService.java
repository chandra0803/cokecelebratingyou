/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/ProfileService.java,v $
 */

package com.biperf.core.service.participant;

import com.biperf.core.domain.user.UserCookiesAcceptance;
import com.biperf.core.service.SAO;

public interface UserCookiesAcceptanceService extends SAO
{
  /**
   * BEAN_NAME
   */
  public static final String BEAN_NAME = "userCookiesAcceptanceService";

  UserCookiesAcceptance getUserCookiesAcceptanceDetailsByPaxID( Long userId );

  void saveUserCookiesAcceptanceDetails( UserCookiesAcceptance userCookiesAcceptance );

}
