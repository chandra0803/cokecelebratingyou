/*
 * (c) 2013 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/awardgenerator/hibernate/AwardGenBatchDAOImpl.java,v $
 */

package com.biperf.core.dao.awardgenerator.hibernate;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.awardgenerator.AwardGenBatchDAO;
import com.biperf.core.domain.awardgenerator.AwardGenBatch;
import com.biperf.core.domain.awardgenerator.AwardGenParticipant;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.value.AwardGenFileExtractValueBean;
import com.biperf.core.value.AwardGenPaxValueBean;
import com.biperf.core.value.AwardGeneratorManagerPaxBean;

/**
 * AwardGenBatchDAOImpl
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
 * <td>Jul 22, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardGenBatchDAOImpl extends BaseDAO implements AwardGenBatchDAO
{
  public static final String SPOTLIGHT_LEVEL_NAME_KEY = "LEVEL_NAME";

  private DataSource dataSource;

  public AwardGenBatch save( AwardGenBatch awardGenBatch )
  {
    getSession().saveOrUpdate( awardGenBatch );
    getSession().flush();
    getSession().refresh( awardGenBatch );
    return awardGenBatch;
  }

  /**
   * Gets the awardGenBatch by the id.
   * 
   * @param id
   * @return AwardGenBatch
   */
  public AwardGenBatch getAwardGenBatchById( Long id )
  {
    return (AwardGenBatch)getSession().get( AwardGenBatch.class, id );
  }

  public List<AwardGenBatch> getAllBatchesByAwardGenId( Long awardGenId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.awardgenerator.AwardGenBatch.allBatchesByAwardGenId" );
    query.setParameter( "awardGenId", awardGenId );
    return query.list();
  }

  public List getAllAwardGenBatchs()
  {
    return getSession().createCriteria( AwardGenBatch.class ).list();
  }

  private class AwardGenPaxResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      AwardGenPaxValueBean bean = new AwardGenPaxValueBean();
      bean.setUserId( extractLong( tuple[0] ) ); // user id
      bean.setYear( extractLong( tuple[1] ) ); // Number of Years
      bean.setDays( extractLong( tuple[2] ) ); // Number of Days
      bean.setAnniversaryDate( extractString( tuple[3] ) ); // anniversary date

      return bean;
    }
  }

  public boolean isBatchExist( Long awardGenId, String batchStartDate, String batchEndDate )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.awardgenerator.AwardGenBatch.countOfBatchMatchingStartAndEndDate" );
    query.setParameter( "awardGenId", awardGenId );
    query.setParameter( "batchStartDate", batchStartDate );
    query.setParameter( "batchEndDate", batchEndDate );
    int count = (Integer)query.uniqueResult();
    if ( count > 0 )
    {
      return true;
    }
    return false;
  }

  private class AwardGenFilePlateauExtractResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      AwardGenFileExtractValueBean bean = new AwardGenFileExtractValueBean();
      bean.setUserName( extractString( tuple[0] ) ); // user name
      int levelId = extractInt( tuple[1] );
      String displayLevelName = String.valueOf( levelId );
      bean.setLevelName( displayLevelName ); // level name
      bean.setAwardDate( extractDate( tuple[2] ) ); // award date
      bean.setAnniversaryNumberOfDays( extractInt( tuple[3] ) );
      bean.setAnniversaryNumberOfYears( extractInt( tuple[4] ) );
      return bean;
    }
  }

  public List<AwardGeneratorManagerPaxBean> getPaxListByMgrAndBatchId( Long userId, Long batchId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.awardgenerator.AwardGenBatch.managerAlertPaxListByPromotion" );
    query.setParameter( "userId", userId );
    query.setParameter( "batchId", batchId );
    query.setResultTransformer( new AwardGeneratorManagerPaxResultTransformer() );
    return query.list();
  }

  private class AwardGeneratorManagerPaxResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      AwardGeneratorManagerPaxBean bean = new AwardGeneratorManagerPaxBean();
      bean.setUserId( extractLong( tuple[0] ) );
      bean.setName( extractString( tuple[1] ) );
      bean.setAwardGenPaxId( extractLong( tuple[2] ) );
      bean.setAnniversaryDate( extractDate( tuple[3] ) );
      bean.setAwardDate( extractDate( tuple[4] ) );
      return bean;
    }
  }

  @Override
  public void dismissAlertForAwardGenManager( AwardGenParticipant awardGenParticipant )
  {
    awardGenParticipant.setIsDismissed( "Y" );
    getSession().update( awardGenParticipant );
  }

  @Override
  public AwardGenParticipant getAwardGenParticipantById( Long awardGenPaxId )
  {
    return (AwardGenParticipant)getSession().get( AwardGenParticipant.class, awardGenPaxId );
  }

  public List<Long> getAllManagersByBatchId( Long batchId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.awardgenerator.AwardGenBatch.getManagerUserIdByBatchId" );
    query.setParameter( "batchId", batchId );
    return query.list();
  }

  @Override
  public Map<String, Object> generateAndSaveBatch( Map<String, Object> awardGenParams )
  {
    CallPrcGenerateAndSaveBatch procedure = new CallPrcGenerateAndSaveBatch( dataSource );
    return procedure.executeProcedure( awardGenParams );
  }

  @Override
  public Map<String, Object> generateValuesForEmail( Long batchId )
  {
    CallPrcGenerateValuesForEmail procedure = new CallPrcGenerateValuesForEmail( dataSource );
    return procedure.executeProcedure( batchId );
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
