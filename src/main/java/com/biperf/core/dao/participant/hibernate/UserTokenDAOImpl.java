
package com.biperf.core.dao.participant.hibernate;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.UserTokenDAO;
import com.biperf.core.domain.enums.UserTokenStatusType;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.utils.crypto.SHA256Hash;

public class UserTokenDAOImpl extends BaseDAO implements UserTokenDAO
{
  @Override
  public void saveUserToken( UserToken userToken )
  {
    getSession().save( userToken );
  }

  @Override
  public boolean validateToken( Long userId, String encryptToken )
  {
    Criteria criteria = getSession().createCriteria( UserToken.class ).setProjection( Projections.rowCount() )
        .add( Restrictions.conjunction().add( Restrictions.eq( "token", encryptToken ) ).add( Restrictions.eq( "user.id", userId ) )
            .add( Restrictions.eq( "status", UserTokenStatusType.lookup( UserTokenStatusType.ISSUED ) ) ).add( Restrictions.gt( "expirationDate", new Date() ) ) );
    return ( (Long)criteria.uniqueResult() ).longValue() == 1;
  }

  @Override
  public boolean validateClearTextToken( Long userId, String clearTextToken )
  {
    return validateToken( userId, new SHA256Hash().encryptDefault( clearTextToken ) );
  }

  @Override
  public UserToken getTokenById( String encryptToken )
  {
    Criteria getTokenCriteria = getSession().createCriteria( UserToken.class ).add( Restrictions.eq( "token", encryptToken ) );
    return (UserToken)getTokenCriteria.uniqueResult();
  }

  @Override
  public void purgeUserTokens()
  {
    getSession().getNamedQuery( "com.biperf.core.domain.user.PurgeUserTokens" ).executeUpdate();
  }
}
