
package com.biperf.core.dao.purl.hibernate;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.purl.PurlContributorDAO;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.client.ExternalUnsubscribe;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.participant.PaxAvatarData;
import com.biperf.core.value.participant.PurlContributorAvatarData;
import com.biperf.core.value.participant.PurlNotMigratedPaxData;

/**
 * PurlContributorDAOImpl.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>shanmuga</td>
 * <td>Nov 22, 2010</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */

public class PurlContributorDAOImpl extends BaseDAO implements PurlContributorDAO
{

  /**
   * Overridden from @see com.biperf.core.dao.purl.PurlContributorDAOImpl#save(com.biperf.core.domain.purl.PurlContributorDAOImpl)
   * @param info
   * @return
   */

  private static final Log log = LogFactory.getLog( PurlContributorDAOImpl.class );

  private DataSource dataSource;

  private JdbcTemplate jdbcTemplate;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  public PurlContributor save( PurlContributor info )
  {
    PurlContributor savedPurlContributor = (PurlContributor)HibernateUtil.saveOrUpdateOrShallowMerge( info );

    // run lock to insure that sub-objects are reattached to this session, since some of
    // the subobjects may have come from another session.
    getSession().flush();
    getSession().refresh( info );

    return savedPurlContributor;
  }

  /**
   * Overridden from @see com.biperf.core.dao.purl.PurlContributorDAOImpl#delete(com.biperf.core.domain.purl.PurlContributorDAOImpl)
   * @param contributor
   */
  public void delete( PurlContributor contributor )
  {
    getSession().delete( contributor );
  }

  /**
   * Overridden from @see com.biperf.core.dao.purl.PurlContributorDAOImpl#delete(com.biperf.core.domain.purl.PurlContributorDAOImpl)
   * @param id
   */
  public PurlContributor getPurlContributorById( Long id )
  {
    return (PurlContributor)getSession().get( PurlContributor.class, id );
  }

  public List<PurlContributor> getContributors( PurlContributorQueryConstraint constraint )
  {
    return HibernateUtil.getObjectList( constraint );
  }

  public int getContributorCount( PurlContributorQueryConstraint constraint )
  {
    return HibernateUtil.getObjectListCount( constraint );
  }

  public PurlContributor getContributorStepElementById( Long id )
  {
    return (PurlContributor)getSession().get( PurlContributor.class, id );
  }

  @Override
  public List<PaxAvatarData> getNotMigratedPurlContributorAvatarData()
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.getNotMigratedPurlContributorAvatarData" );
    query.setResultTransformer( Transformers.aliasToBean( PaxAvatarData.class ) );
    return (List<PaxAvatarData>)query.list();
  }

  public List<PurlContributor> getAllPendingPurlContributionsProActive( Long promotionId, Long numberOfDays )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.PurlContributor.getAllPendingPurlContributionsProActive" );
    query.setParameter( "promoId", promotionId );
    query.setParameter( "numberOfDays", numberOfDays );
    return query.list();
  }

  @Override
  public void updateMigratedPurlContributorAvatar( Long purlContributorId, String avatarUrl )
  {
    String query = "update purl_contributor set avatar_url = ?, modified_by = 651, date_modified = sysdate where purl_contributor_id = ?";

    Object[] params = { avatarUrl, purlContributorId };

    try
    {
      jdbcTemplate.update( query, params );
    }
    catch( DataAccessException e )
    {
      log.error( "The participant id : " + purlContributorId + " : " + e );
    }

  }

  @Override
  public List<Long> getAllInternalPurlContributorUser()
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.getAllInternalPurlContributorUser" );
    return (List<Long>)query.list();
  }

  @Override
  public List<PurlContributorAvatarData> getNotSyncPurlContrbUserAvatarData( List<Long> userIds )
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.getNotSyncPurlContrbUserAvatarData" );
    query.setParameterList( "userIds", userIds );
    query.setResultTransformer( Transformers.aliasToBean( PurlContributorAvatarData.class ) );
    return (List<PurlContributorAvatarData>)query.list();
  }

  @Override
  public void updateMigratedPurlContrPaxAvatar( Long userId, String avatarUrl )
  {
    String query = "update purl_contributor set avatar_url = ?, modified_by = 651, date_modified = sysdate where user_id = ?";

    Object[] params = { avatarUrl, userId };

    try
    {
      jdbcTemplate.update( query, params );
    }
    catch( DataAccessException e )
    {
      log.error( "The user id : " + userId + " : " + e );
    }
  }

  @Override
  public List<Long> getAllPurlUsersAvatarMigrated( List<Long> userIds )
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.getAllPurlUsersAvatarMigrated" );
    query.setParameterList( "userIds", userIds );
    return (List<Long>)query.list();
  }

  @Override
  public List<PurlNotMigratedPaxData> getNotMigratedPurlUserAvatar( List<Long> userIds )
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.getNotMigratedPurlUserAvatar" );
    query.setParameterList( "userIds", userIds );
    query.setResultTransformer( Transformers.aliasToBean( PurlNotMigratedPaxData.class ) );
    return (List<PurlNotMigratedPaxData>)query.list();
  }

  @Override
  public List<Long> getAllPurlContrbUsersToCopyTheUrl()
  {
    getSession().flush();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.getAllPurlContrbUsersToCopyTheUrl" );
    return (List<Long>)query.list();
  }


  /*Customization for WIP 32479 starts here*/
  
  @Override
  public void unsubscribeExternalUser( String emailAddress )
  {
    ExternalUnsubscribe externalUnsubscribe = new ExternalUnsubscribe(); 
    externalUnsubscribe.setEmailAddress( emailAddress );
    
    getSession().saveOrUpdate( externalUnsubscribe );
  }

  @Override
  public boolean isExternalContributorExists( String emailAddr )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.purl.client.isExternalContributorExists" );
    query.setParameter( "emailAddress", emailAddr );

    return (Integer)query.uniqueResult() > 0 ;
  }  
  /*Customization for WIP 32479 ends here*/
  
}
