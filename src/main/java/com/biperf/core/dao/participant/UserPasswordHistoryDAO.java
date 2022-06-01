
package com.biperf.core.dao.participant;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.user.UserPasswordHistory;

public interface UserPasswordHistoryDAO extends DAO
{
  public List<UserPasswordHistory> getUserPasswords( Long userId );

  public int savePassword( UserPasswordHistory userPasswordHistory );

  public void deletePassword( long userId );

  public int getMaxSequenceNumber();
}
