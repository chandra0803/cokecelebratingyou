
package com.biperf.core.dao.participant.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.UserPasswordHistoryDAO;
import com.biperf.core.domain.user.UserPasswordHistory;

public class UserPasswordHistoryDAOImpl extends BaseDAO implements UserPasswordHistoryDAO
{

  @Override
  public List<UserPasswordHistory> getUserPasswords( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.password.UserPasswordHistory.passwords" );
    query.setLong( "userId", userId );

    return query.list();
  }

  @Override
  public int savePassword( UserPasswordHistory userPasswordHistory )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.password.UserPasswordHistory.insertpassword" );
    query.setParameter( "userId", userPasswordHistory.getUser().getId() );
    query.setParameter( "password", userPasswordHistory.getPassword() );
    query.setParameter( "createdBy", userPasswordHistory.getUser().getId() );
    query.setParameter( "sequenceNum", userPasswordHistory.getSequenceNumber() );
    int count = query.executeUpdate();
    return count;
  }

  public void deletePassword( long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.password.UserPasswordHistory.deletepassword" );
    query.setParameter( "userId", userId );
    query.executeUpdate();
  }

  public int getMaxSequenceNumber()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.password.UserPasswordHistory.getSequenceNumber" );
    return runMaxSequenceQuery( query );
  }

  private int runMaxSequenceQuery( Query query )
  {
    Integer max = (Integer)query.uniqueResult();
    if ( max != null )
    {
      return max.intValue();
    }
    return 0;

  }
}
