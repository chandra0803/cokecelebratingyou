
package com.biperf.core.dao.participant.hibernate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.UserTwitterDAO;
import com.biperf.core.domain.user.UserTwitter;

public class UserTwitterDAOImpl extends BaseDAO implements UserTwitterDAO
{
  public UserTwitter getUserTwitterById( Long id )
  {
    return (UserTwitter)getSession().get( UserTwitter.class, id );
  }

  public void delete( UserTwitter domain )
  {
    getSession().delete( domain );
  }

  public void deleteUserTwitterById( Long id )
  {
    UserTwitter userTwitter = getUserTwitterById( id );
    delete( userTwitter );
  }

}
