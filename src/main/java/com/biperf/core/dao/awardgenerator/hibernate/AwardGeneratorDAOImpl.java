/*
 * (c) 2013 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/awardgenerator/hibernate/AwardGeneratorDAOImpl.java,v $
 */

package com.biperf.core.dao.awardgenerator.hibernate;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.awardgenerator.AwardGeneratorDAO;
import com.biperf.core.domain.awardgenerator.AwardGenAward;
import com.biperf.core.domain.awardgenerator.AwardGenerator;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.value.AwardGeneratorManagerReminderBean;

/**
 * AwardGeneratorDAOImpl
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
 * <td>chowdhur</td>
 * <td>Jul 09, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardGeneratorDAOImpl extends BaseDAO implements AwardGeneratorDAO
{
  private DataSource dataSource;

  /**
   * Manages deleting an awardGenerator from the database.
   * 
   * @param awardGenerator
   */
  public void deleteAwardGenerator( AwardGenerator awardGenerator )
  {
    awardGenerator.setActive( false );
    getSession().saveOrUpdate( awardGenerator );
    getSession().flush();
  }

  /**
   * Save the awardGenerator to the database.
   * 
   * @param awardGenerator
   * @return AwardGenerator
   */
  public AwardGenerator saveAwardGenerator( AwardGenerator awardGenerator )
  {
    getSession().saveOrUpdate( awardGenerator );
    getSession().flush();
    getSession().refresh( awardGenerator );
    return awardGenerator;
  }

  /**
   * Gets the awardGenerator by the id.
   * 
   * @param id
   * @return AwardGenerator
   */
  public AwardGenerator getAwardGeneratorById( Long id )
  {
    return (AwardGenerator)getSession().get( AwardGenerator.class, id );
  }

  /**
   * Gets a list of all awardGenerators in the database. If
   * startDate is null, Current Date is taken as startDate.
   * if EndDate is null, then there is no endDate.
   * 
   * @return List
   */
  public List getAllActiveAwardGenerators()
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.domain.awardgenerator.ActiveAwardGeneratorList" ).list();
  }

  public AwardGenerator getAwardGeneratorByName( String name )
  {
    return (AwardGenerator)getSession().getNamedQuery( "com.biperf.core.domain.awardgenerator.GetAwardGeneratorByName" ).setString( "name", name ).uniqueResult();
  }

  public List<String> getAllYearsByAwardGenId( Long awardGenId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.awardgenerator.AwardGenAward.getAllYearsByAwardGenId" );
    query.setParameter( "awardGenId", awardGenId );
    return query.list();
  }

  public List<String> getAllDaysByAwardGenId( Long awardGenId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.awardgenerator.AwardGenAward.getAllDaysByAwardGenId" );
    query.setParameter( "awardGenId", awardGenId );
    return query.list();
  }

  public List<AwardGenAward> getAllAwardsByAwardGenIdAndYears( Long awardGenId, String years )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.awardgenerator.AwardGenAward.getAllAwardsByAwardGenIdAndYears" );
    query.setParameter( "awardGenId", awardGenId );
    query.setString( "years", years );
    List<AwardGenAward> awards = query.list();
    return awards;
  }

  public List<AwardGenAward> getAllAwardsByAwardGenIdAndDays( Long awardGenId, String days )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.awardgenerator.AwardGenAward.getAllAwardsByAwardGenIdAndDays" );
    query.setParameter( "awardGenId", awardGenId );
    query.setString( "days", days );
    List<AwardGenAward> awards = query.list();
    return awards;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<AwardGeneratorManagerReminderBean> getAwardGeneratorManagerRemindersList( Long participantId )
  {
    String sql = buildAwardGeneratorManagerRemindersListQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "userId", participantId );
    query.setResultTransformer( new AwardGeneratorManagerReminderBeanMapper() );
    return query.list();
  }

  private String buildAwardGeneratorManagerRemindersListQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT distinct ag.notify_manager, " );
    sql.append( "   ag.number_of_days_for_alert, " );
    sql.append( "   ag.promotion_id, " );
    sql.append( "   am.awardgen_batch_id, " );
    sql.append( "   am.date_created, " );
    sql.append( "   am.expiry_date " );
    sql.append( " FROM awardgen_manager am, " );
    sql.append( "   awardgenerator ag, " );
    sql.append( "   awardgen_batch ab, " );
    sql.append( "   awardgen_participant ap " );
    sql.append( " WHERE am.awardgen_batch_id = ab.awardgen_batch_id " );
    sql.append( " AND am.awardgen_participant_id = ap.awardgen_participant_id " );
    sql.append( " AND ab.awardgen_id         = ag.awardgen_id " );
    sql.append( " AND am.user_Id             = :userId " );
    sql.append( " AND am.expiry_date >= sysdate " );
    sql.append( " AND ap.is_dismissed IS NULL " );

    return sql.toString();
  }

  @SuppressWarnings( "serial" )
  private class AwardGeneratorManagerReminderBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      AwardGeneratorManagerReminderBean reportValue = new AwardGeneratorManagerReminderBean();

      reportValue.setNotifyManager( extractBoolean( tuple[0] ) );
      reportValue.setNumberOfDaysForAlert( extractInt( tuple[1] ) );
      reportValue.setPromotionId( extractLong( tuple[2] ) );
      reportValue.setBatchId( extractLong( tuple[3] ) );
      reportValue.setCreatedDate( extractDate( tuple[4] ) );
      reportValue.setExpirationDate( extractDate( tuple[5] ) );

      return reportValue;
    }
  }

  public AwardGenAward getAwardGenAwardById( Long id )
  {
    return (AwardGenAward)getSession().get( AwardGenAward.class, id );
  }

  public void deleteAwardGenAward( AwardGenAward awardGenAward )
  {
    AwardGenAward awardGenAwardDb = getAwardGenAwardById( awardGenAward.getId() );
    if ( awardGenAwardDb != null )
    {
      awardGenAwardDb.setDeleted( true );
      getSession().saveOrUpdate( awardGenAwardDb );
      getSession().flush();
    }
  }

  /**
   * Setter: DataSource is provided by Dependency Injection.
   * 
   * @param dataSource
   */
  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

}
