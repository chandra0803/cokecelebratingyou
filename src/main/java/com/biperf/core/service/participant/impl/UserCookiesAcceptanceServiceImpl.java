/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/impl/UserServiceImpl.java,v $
 */

package com.biperf.core.service.participant.impl;

import com.biperf.core.dao.participant.UserCookiesAcceptanceDAO;
import com.biperf.core.domain.user.UserCookiesAcceptance;
import com.biperf.core.service.participant.UserCookiesAcceptanceService;

public class UserCookiesAcceptanceServiceImpl implements UserCookiesAcceptanceService
{

  private UserCookiesAcceptanceDAO userCookiesAcceptanceDAO;

  @Override
  public UserCookiesAcceptance getUserCookiesAcceptanceDetailsByPaxID( Long userId )
  {
    UserCookiesAcceptance userCookiesAcceptance = userCookiesAcceptanceDAO.getUserCookiesAcceptanceDetailsByPaxID( userId );
    return userCookiesAcceptance;
  }

  @Override
  public void saveUserCookiesAcceptanceDetails( UserCookiesAcceptance userCookiesAcceptance )
  {
    userCookiesAcceptanceDAO.saveUserCookiesAcceptanceDetails( userCookiesAcceptance );
  }

  public void setUserCookiesAcceptanceDAO( UserCookiesAcceptanceDAO userCookiesAcceptanceDAO )
  {
    this.userCookiesAcceptanceDAO = userCookiesAcceptanceDAO;
  }

}