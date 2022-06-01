
package com.biperf.core.dao.participant;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.user.UserFacebook;

public interface UserFacebookDAO extends DAO
{

  public void delete( UserFacebook domain );
}
