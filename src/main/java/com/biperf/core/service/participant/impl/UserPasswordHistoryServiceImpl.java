
package com.biperf.core.service.participant.impl;

import java.util.List;

import com.biperf.core.dao.participant.UserPasswordHistoryDAO;
import com.biperf.core.domain.user.UserPasswordHistory;
import com.biperf.core.service.participant.UserPasswordHistoryService;

public class UserPasswordHistoryServiceImpl implements UserPasswordHistoryService
{
  private UserPasswordHistoryDAO userPasswordHistoryDAO;

  @Override
  public List<UserPasswordHistory> getUserPasswords( Long userId )
  {
    return getUserPasswordHistoryDAO().getUserPasswords( userId );
  }

  @Override
  public int savePassword( UserPasswordHistory userPasswordHistory )
  {
    return getUserPasswordHistoryDAO().savePassword( userPasswordHistory );
  }

  public UserPasswordHistoryDAO getUserPasswordHistoryDAO()
  {
    return userPasswordHistoryDAO;
  }

  public void setUserPasswordHistoryDAO( UserPasswordHistoryDAO userPasswordHistoryDAO )
  {
    this.userPasswordHistoryDAO = userPasswordHistoryDAO;
  }

}
