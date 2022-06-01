
package com.biperf.core.service.participant;

import java.util.List;

import com.biperf.core.domain.user.UserPasswordHistory;
import com.biperf.core.service.SAO;

public interface UserPasswordHistoryService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "userPasswordHistoryService";

  public List<UserPasswordHistory> getUserPasswords( Long userId );

  public int savePassword( UserPasswordHistory userPasswordHistory );
}
