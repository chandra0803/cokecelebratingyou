
package com.biperf.core.dao.participant.hibernate;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.UserCountryChangesDAO;
import com.biperf.core.domain.user.UserCountryChanges;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * UserCountryChangesDAOImpl.
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
 * <td>Bala</td>
 * <td>Dec 09, 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */

public class UserCountryChangesDAOImpl extends BaseDAO implements UserCountryChangesDAO
{

  private DataSource dataSource;

  private JdbcTemplate jdbcTemplate;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  public List<UserCountryChanges> getUsersToMoveBalance()
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.GetUsersToMoveBalance" );
    List<UserCountryChanges> userCountryChanges = query.list();
    return userCountryChanges;
  }

  public UserCountryChanges saveUserCountryChanges( UserCountryChanges userCountryChanges ) throws ServiceErrorException
  {
    return (UserCountryChanges)HibernateUtil.saveOrUpdateOrShallowMerge( userCountryChanges );
  }

  public UserCountryChanges getUserByOldCampaign( String oldCampaignNbr, Long userId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.UserCountryChanges.getUserByOldCampaign" );
    query.setParameter( "oldCampaignNbr", oldCampaignNbr );
    query.setParameter( "userId", userId );
    List userCountryChanges = query.list();
    if ( userCountryChanges.size() > 0 )
    {
      return (UserCountryChanges)userCountryChanges.get( 0 );
    }
    return null;
  }

  public String getDecryptedValue( String toDecrypt )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.user.UserCountryChanges.getDecryptedValue" );
    query.setParameter( "toDecrypt", toDecrypt );
    String awardbanqDec = (String)query.uniqueResult();
    return awardbanqDec;
  }

  public String getEncryptedValue( String toEncrypt )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.UserCountryChanges.getEncryptedValue" );
    query.setParameter( "toEncrypt", toEncrypt );
    String awardbanqDec = (String)query.uniqueResult();
    return awardbanqDec;
  }

  /**
   * @param id
   * @return UserCountryChanges
   */
  public UserCountryChanges getById( Long id )
  {
    UserCountryChanges userCountryChanges = (UserCountryChanges)getSession().get( UserCountryChanges.class, id );
    return userCountryChanges;
  }

  /**
   * @return List of userCountryChanges IDs
   */
  public List<Long> getUCCsWithAccountBalancesToTranfer()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.uccWithAccountBalancesToTranfer" );
    return query.list();
  }
}
