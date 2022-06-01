/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/process/hibernate/ProcessDAOImpl.java,v $
 */

package com.biperf.core.dao.process.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.process.ProcessDAO;
import com.biperf.core.domain.process.Process;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.QuartzProcessBean;
import com.biperf.core.value.client.ClientGiftCodeSweepPromoValueBean;

/**
 * ProductDAO.
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
 * <td>Sathish</td>
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ProcessDAOImpl extends BaseDAO implements ProcessDAO
{

  public static final String BEAN_NAME = "processDAO";

  private static final String WORK_HAPPIER_PROCESS_NAMED_QUERY = "com.biperf.core.domain.report.reportParameterValueChoices.listOfMonths";
  private static final String ADMIN_TEST_RECOG_RECEIVED_BADGE_PROMO_NAMED_QUERY = "com.biperf.core.domain.gamification.processParameterValueChoices.allNonExpiredPromotions";

  private JdbcTemplate jdbcTemplate;

  /**
   * Get the process from the database by the id.
   * 
   * @param id
   * @return Process
   */
  public Process getProcessById( Long id )
  {
    return (Process)getSession().get( Process.class, id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionDAO#getPromotionByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return Promotion
   */
  public Process getProcessByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Session session = HibernateSessionManager.getSession();
    Process process = (Process)session.get( Process.class, id );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( process );
    }

    return process;
  }

  /**
   * Get the process from the database by the id.
   * 
   * @param status
   * @return List
   */
  public List getProcessListByStatus( String status )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.process.ProcessByStatus" );
    query.setParameter( "status", status );

    return query.list();
  }

  public List getProcessListByStatusAndType( String status, String processType )
  {

    Query query = null;
    if ( !processType.isEmpty() )
    {
      if ( processType.equals( "all" ) )
      {
        query = getSession().getNamedQuery( "com.biperf.core.domain.process.ProcessByStatus" );
      }
      else if ( processType.equals( "general" ) )
      {
        query = getSession().getNamedQuery( "com.biperf.core.domain.process.getGeneralProcessByStatus" );
      }
      else if ( processType.equals( "system" ) )
      {
        query = getSession().getNamedQuery( "com.biperf.core.domain.process.getSystemProcessByStatus" );
      }
      else if ( processType.equals( "adminTest" ) )
      {
        query = getSession().getNamedQuery( "com.biperf.core.domain.process.getAdminTestProcessByStatus" );
      }
    }
    if ( query != null )
    {
      query.setParameter( "status", status );

      return query.list();
    }
    return null;
  }

  /**
   * Get processes by status.
   * 
   * @param status
   * @param associationRequestCollection
   * @return List
   */
  public List getProcessListByStatus( String status, AssociationRequestCollection associationRequestCollection, String processType )
  {
    List processList = getProcessListByStatusAndType( status, processType );
    associationRequestCollection.process( processList );
    return processList;
  }

  /**
   * Saves the process to the database.
   * 
   * @param process
   * @return Process
   */
  public Process save( Process process )
  {

    return (Process)HibernateUtil.saveOrUpdateOrShallowMerge( process );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.process.ProcessDAO#getProcessByName(java.lang.String)
   * @param processName
   */
  public Process getProcessByName( String processName )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.process.ProcessByName" );
    query.setParameter( "processName", processName );

    return (Process)query.uniqueResult();
  }

  public Process getProcessByBeanName( String processBeanName )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.process.ProcessByBeanName" );
    query.setParameter( "processBeanName", processBeanName );

    return (Process)query.uniqueResult();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.process.ProcessDAO#getProcessParameterValueChoices(java.lang.String)
   * @param namedQueryName
   * @return List of {@link com.biperf.core.value.FormattedValueBean} objects.
   */
  public List getProcessParameterValueChoices( String namedQueryName )
  {
    if ( namedQueryName.equals( WORK_HAPPIER_PROCESS_NAMED_QUERY ) || namedQueryName.equals( ADMIN_TEST_RECOG_RECEIVED_BADGE_PROMO_NAMED_QUERY ) )
    {
      Query query = getSession().getNamedQuery( namedQueryName );
      query.setResultTransformer( new FormattedValueBeanTransformer() );
      return query.list();
    }
    return getSession().getNamedQuery( namedQueryName ).list();
  }

  private class FormattedValueBeanTransformer extends BaseResultTransformer
  {
    @Override
    public FormattedValueBean transformTuple( Object[] tuple, String[] aliases )
    {
      FormattedValueBean bean = new FormattedValueBean( (Long)tuple[0], extractString( tuple[1] ) );
      return bean;
    }
  }

  /**
   * Update the last execute time, ignoring optimistic lockding. Overridden from
   * 
   * @see com.biperf.core.dao.process.ProcessDAO#updateLastExecutedDate(java.util.Date,
   *      java.lang.Long)
   * @param lastExecutedTime
   * @param processId
   */
  public synchronized void updateLastExecutedDate( Date lastExecutedTime, Long processId )
  {
    // Using Direct sql since in hibernate 3.0, hibernate bulk updates can't be
    // run with native SQL and HQL bulk updates don't work with the classic parser (which we use).

    // Flush initially to bring the database up to date
    getSession().flush();

    PreparedStatement preparedStatement = null;
    try
    {
      preparedStatement = getSession().connection().prepareStatement( "update PROCESS set PROCESS_LAST_EXECUTED_DATE = ? " + " where PROCESS_ID = ?" );
      int index = 1;
      preparedStatement.setTimestamp( index++, new Timestamp( lastExecutedTime.getTime() ) );
      preparedStatement.setLong( index++, processId.longValue() );

      preparedStatement.executeUpdate();
    }
    catch( HibernateException e )
    {
      throw new BeaconRuntimeException( "Exception updating process last execute time", e );
    }
    catch( SQLException e )
    {
      throw new BeaconRuntimeException( "Exception updating process last execute time", e );
    }
    finally
    {
      if ( preparedStatement != null )
      {
        try
        {
          preparedStatement.close();
        }
        catch( SQLException e )
        {
          // ignore
        }
      }
    }

  }

  public void setDataSource( DataSource dataSource )
  {
    // TODO what if you want to use a JdbcTemplate by preference,
    // for a native extractor?
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  private static final String QRTZ_CURRENT_EXEC_PROCESS_QUERY = "select distinct p.process_id, p.process_name "
      + "from process p, (select substr(job_name, instr(job_name, '-')+1) process_id, substr(instance_name, 0, 8) server_name from qrtz_fired_triggers) q " + "where p.process_id = q.process_id";

  private static final String DEVQ_CURRENT_EXEC_PROCESS_QUERY = "select distinct p.process_id, p.process_name "
      + "from process p, (select substr(job_name, instr(job_name, '-')+1) process_id, substr(instance_name, 0, 8) server_name from devq_fired_triggers) q " + "where p.process_id = q.process_id";

  public List getAllCurrentlyExecutingProcesses()
  {
    String queryString = QRTZ_CURRENT_EXEC_PROCESS_QUERY;
    if ( !Environment.isCtech() )
    {
      queryString = DEVQ_CURRENT_EXEC_PROCESS_QUERY;
    }
    return jdbcTemplate.query( queryString, new QuartzProcessRowMapper() );
  }
//Client customizations for wip #23129 starts
 public List getClientGiftCodeSweepPromotions()
 {
   return getSession().getNamedQuery( "com.biperf.core.domain.process.clientGiftCodeSweepPromotions" ).list();
 }

 public List getClientGiftCodeSweepBean( Long promoId )
 {
   List beans = new ArrayList();
   Query query = getSession().getNamedQuery( "com.biperf.core.domain.process.clientGiftCodeSweepBean" );
   // if there is a parm in the query set it here before the query.list
   query.setParameter( "promoId", promoId );

   List results = query.list();
   for ( Iterator iter = results.iterator(); iter.hasNext(); )
   {
     Object[] row = (Object[])iter.next();
     ClientGiftCodeSweepPromoValueBean bean = new ClientGiftCodeSweepPromoValueBean();
     bean.setMonthYear( (String)row[0] );
     bean.setMonthYearDesc( (String)row[1] );
     beans.add( bean );
   }
   return beans;
 }
 // Client customizations for wip #23129 ends
  private class QuartzProcessRowMapper implements RowMapper<QuartzProcessBean>
  {
    public QuartzProcessBean mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      QuartzProcessBean bean = new QuartzProcessBean();

      bean.setProcessId( rs.getLong( "PROCESS_ID" ) );
      bean.setProcessName( rs.getString( "PROCESS_NAME" ) );

      return bean;
    }
  }

  @Override
  public void clearSecondLevelCache()
  {
    SessionFactory sessionFactory = getSession().getSessionFactory();
    sessionFactory.getCache().evictEntityRegions();
    sessionFactory.getCache().evictCollectionRegions();
    sessionFactory.getCache().evictDefaultQueryRegion();
    // NO LONGER USED THROWS EXCEPTION: sessionFactory.getCache().evictQueryRegions();
  }

}
