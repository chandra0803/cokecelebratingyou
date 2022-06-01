
package com.biperf.core.dao.welcomemail.hibernate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.transform.ResultTransformer;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.welcomemail.WelcomeMessageDAO;
import com.biperf.core.domain.welcomemail.WelcomeMessage;
import com.biperf.core.domain.welcomemail.WelcomeMessageBean;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * 
 * WelcomeMessageDAOImpl.
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
 * <td>Ramesh Kunasekaran</td>
 * <td>Sep 18, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */
public class WelcomeMessageDAOImpl extends BaseDAO implements WelcomeMessageDAO
{

  private DataSource dataSource;

  /**
   * Overridden from
   * 
   * @seecom.biperf.core.dao.welcomemail.WelcomeMessageDAO#saveWelcomeMessage(com.biperf.core.domain.welcomemail.WelcomeMessage)
   * @param welcomeMessage
   * @return WelcomeMessage
   */
  public WelcomeMessage saveWelcomeMessage( WelcomeMessage welcomeMessage )
  {
    return (WelcomeMessage)HibernateUtil.saveOrUpdateOrShallowMerge( welcomeMessage );
  }

  /**
   * 
   * Overridden from @see com.biperf.core.dao.welcomemail.WelcomeMessageDAO#getAllMessageByNotificationDate()
   * @return list
   */
  public List getAllMessageByNotificationDate()
  {
    List list = getSession().getNamedQuery( "com.biperf.core.domain.welcomemail.AllMessageByNotificationDate" ).list();

    return list;
  }

  /**
   * 
   * Overridden from @see com.biperf.core.dao.welcomemail.WelcomeMessageDAO#getWelcomeMessageById(java.lang.Long)
   * @param id
   * @return welcome message
   */
  public WelcomeMessage getWelcomeMessageById( Long id )
  {
    return (WelcomeMessage)getSession().get( WelcomeMessage.class, id );
  }

  /**
   * 
   * Overridden from @see com.biperf.core.dao.welcomemail.WelcomeMessageDAO#deleteWelcomeMessagesForm(com.biperf.core.domain.welcomemail.WelcomeMessage)
   * @param welcomeMessage
   */
  public void deleteWelcomeMessagesForm( WelcomeMessage welcomeMessage )
  {
    getSession().delete( welcomeMessage );
  }

  /**
   * 
   * Overridden from @see com.biperf.core.dao.welcomemail.WelcomeMessageDAO#getAllMessage()
   * @return list
   */
  public List<WelcomeMessageBean> getAllWelcomeMessages()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.welcomemail.AllMessage" );
    query.setResultTransformer( new WelcomeMessageTransformer() );
    return (List<WelcomeMessageBean>)query.list();
  }

  @Override
  public void truncateStrongMailUserTable()
  {
    Query query = getSession().createSQLQuery( "TRUNCATE TABLE STRONGMAIL_USER" );
    query.executeUpdate();
  }

  @Override
  public Map executeWelcomeEmailProcedure()
  {
    CallPrcWelcomeEmail proc = new CallPrcWelcomeEmail( dataSource );
    return proc.executeProcedure();
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  private class WelcomeMessageTransformer implements ResultTransformer
  {

    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      WelcomeMessageBean valueBean = new WelcomeMessageBean();
      valueBean.setWelcomeMessageId( ( (BigDecimal)tuple[0] ).longValue() );
      valueBean.setNotificationDate( (String)tuple[1] );
      valueBean.setAudienceId( ( (BigDecimal)tuple[2] ).longValue() );
      valueBean.setAudienceList( (String)tuple[3] );
      valueBean.setMessageId( ( (BigDecimal)tuple[4] ).longValue() );
      valueBean.setMessage( (String)tuple[5] );
      if ( tuple[6] != null && tuple[7] != null )
      {
        valueBean.setSecondaryMessageId( ( (BigDecimal)tuple[6] ).longValue() );
        valueBean.setSecondaryMessage( (String)tuple[7] );
      }

      return valueBean;
    }

    @Override
    public List transformList( List collection )
    {
      return collection;
    }

  }
}
