
package com.biperf.core.dao.participant;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.user.UserTwitter;

public interface UserTwitterDAO extends DAO
{
  public UserTwitter getUserTwitterById( Long id );

  public void delete( UserTwitter domain );

  public void deleteUserTwitterById( Long id );

}
