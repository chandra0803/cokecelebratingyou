
package com.biperf.core.dao.participant.hibernate;

import java.util.Objects;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.UserCookiesAcceptanceDAO;
import com.biperf.core.domain.user.UserCookiesAcceptance;
import com.biperf.core.utils.HibernateSessionManager;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

public class UserCookiesAcceptanceDAOImpl extends BaseDAO implements UserCookiesAcceptanceDAO
{

  @Override
  public UserCookiesAcceptance getUserCookiesAcceptanceDetailsByPaxID( Long userId )
  {
    Long policyVersion = (long)0;
    Content content = CmsUtil.getContentFromReaderObject( ContentReaderManager.getContentReader().getContent( "admin.privacy.cookies" ) );
    if ( !Objects.isNull( content ) )
    {
      policyVersion = new Long( content.getVersion() );
    }
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( UserCookiesAcceptance.class );
    criteria.add( Restrictions.eq( "userId", userId ) );
    criteria.add( Restrictions.eq( "policyVersion", policyVersion ) );

    return (UserCookiesAcceptance)criteria.uniqueResult();
  }

  @Override
  public void saveUserCookiesAcceptanceDetails( UserCookiesAcceptance userCookiesAcceptance )
  {
    getSession().save( userCookiesAcceptance );
  }

}
