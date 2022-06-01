
package com.biperf.core.dao.participant.hibernate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.UserFacebookDAO;
import com.biperf.core.domain.user.UserFacebook;

public class UserFacebookDAOImpl extends BaseDAO implements UserFacebookDAO
{

  public void delete( UserFacebook domain )
  {
    getSession().delete( domain );
  }

}
