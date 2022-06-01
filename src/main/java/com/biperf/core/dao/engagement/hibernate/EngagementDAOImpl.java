
package com.biperf.core.dao.engagement.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.engagement.EngagementDAO;
import com.biperf.core.domain.engagement.EngagementEligManager;
import com.biperf.core.domain.engagement.EngagementEligUser;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.EngagementAveragesValueBean;
import com.biperf.core.value.EngagementBehaviorValueBean;
import com.biperf.core.value.EngagementChartValueBean;
import com.biperf.core.value.EngagementDashboardValueBean;
import com.biperf.core.value.EngagementPromotionData;
import com.biperf.core.value.EngagementSiteVisitsLoginValueBean;
import com.biperf.core.value.EngagementSummaryValueBean;
import com.biperf.core.value.EngagementTeamMembersValueBean;
import com.biperf.core.value.EngagementTeamSumValueBean;
import com.biperf.core.value.EngagementTeamsValueBean;
import com.biperf.core.value.NodeBean;

/**
 * 
 * EngagementDAOImpl.
 * 
 * @author kandhi
 * @since May 30, 2014
 * @version 1.0
 */
public class EngagementDAOImpl extends BaseDAO implements EngagementDAO
{
  private static final BigDecimal BAD_OUTPUT = new BigDecimal( "99" );
  private static final BigDecimal NO_DATA_OUTPUT = new BigDecimal( "1" );
  private static final Object OUTPUT_RETURN_CODE = "p_out_return_code";

  @Override
  public EngagementDashboardValueBean getDashboardTeamData( Map<String, Object> extractParams ) throws Exception
  {
    Map<String, Object> results = new HashMap<String, Object>();
    CallPrcEngagementTeamDashboard dashboardproc = new CallPrcEngagementTeamDashboard( dataSource );
    results = dashboardproc.executeProcedure( extractParams );
    return extractResultSets( results );
  }

  @Override
  public EngagementDashboardValueBean getDashboardUserData( Map<String, Object> extractParams ) throws Exception
  {
    Map<String, Object> results = new HashMap<String, Object>();
    CallPrcEngagementUserDashboard dashboardproc = new CallPrcEngagementUserDashboard( dataSource );
    results = dashboardproc.executeProcedure( extractParams );
    return extractResultSets( results );
  }

  protected EngagementDashboardValueBean extractResultSets( Map<String, Object> results ) throws Exception
  {
    EngagementDashboardValueBean engagementDashboardValueBean = new EngagementDashboardValueBean();
    if ( results != null )
    {
      if ( results.get( OUTPUT_RETURN_CODE ) != null && NO_DATA_OUTPUT.equals( results.get( OUTPUT_RETURN_CODE ) ) )
      {
        return null;
      }
      else if ( results.get( OUTPUT_RETURN_CODE ) != null && BAD_OUTPUT.equals( results.get( OUTPUT_RETURN_CODE ) ) )
      {
        throw new Exception( "Stored procedure returned error. Procedure returned: " + results.get( OUTPUT_RETURN_CODE ) );
      }
      EngagementSummaryValueBean engagementSummaryValueBean = (EngagementSummaryValueBean)results.get( "p_out_result_set1" );
      List<EngagementTeamMembersValueBean> engagementTeamMembersValueBeanList = (ArrayList<EngagementTeamMembersValueBean>)results.get( "p_out_result_set2" );
      List<EngagementTeamsValueBean> engagementTeamsValueBeanList = (ArrayList<EngagementTeamsValueBean>)results.get( "p_out_result_set3" );
      List<EngagementTeamSumValueBean> engagementTeamSumValueBeanList = (ArrayList<EngagementTeamSumValueBean>)results.get( "p_out_result_set4" );
      List<EngagementBehaviorValueBean> engagementBehaviorValueBeanList = (ArrayList<EngagementBehaviorValueBean>)results.get( "p_out_result_set5" );
      List<NodeBean> engagementNodeBeanList = (ArrayList<NodeBean>)results.get( "p_out_result_set6" );
      EngagementAveragesValueBean engagementAveragesValueBean = (EngagementAveragesValueBean)results.get( "p_out_result_set7" );
      List<EngagementSiteVisitsLoginValueBean> engagementSiteVisitsLoginValueBeanList = (ArrayList<EngagementSiteVisitsLoginValueBean>)results.get( "p_out_result_set8" );
      List<NodeBean> engagementManagerNodeBeanList = (ArrayList<NodeBean>)results.get( "p_out_result_set9" );
      String userName = results.get( "p_out_user_name" ) != null ? (String)results.get( "p_out_user_name" ) : null;

      engagementDashboardValueBean.setEngagementSummaryValueBean( engagementSummaryValueBean );
      engagementDashboardValueBean.setEngagementTeamMembersValueBeanList( engagementTeamMembersValueBeanList );
      engagementDashboardValueBean.setEngagementTeamsValueBeanList( engagementTeamsValueBeanList );
      engagementDashboardValueBean.setEngagementTeamSumValueBeanList( engagementTeamSumValueBeanList );
      engagementDashboardValueBean.setEngagementBehaviorValueBeanList( engagementBehaviorValueBeanList );
      engagementDashboardValueBean.setNodeBeanList( engagementNodeBeanList );
      engagementDashboardValueBean.setManagerNodeBeanList( engagementManagerNodeBeanList );
      engagementDashboardValueBean.setEngagementAveragesValueBean( engagementAveragesValueBean );
      engagementDashboardValueBean.setUserName( userName );
      engagementDashboardValueBean.setEngagementSiteVisitsLoginValueBeanList( engagementSiteVisitsLoginValueBeanList );
    }
    return engagementDashboardValueBean;
  }

  @Override
  public EngagementSummaryValueBean getSummaryData( String mode, Long userId, Locale userLocale, String timeframeType, int endMonth, int endYear )
  {
    CallPrcEngagementSummary summaryProc = new CallPrcEngagementSummary( dataSource );
    Map<String, Object> results = summaryProc.executeProcedure( mode, userId, userLocale, timeframeType, endMonth, endYear );
    return (EngagementSummaryValueBean)results.get( "p_out_result_set" );
  }

  @Override
  public List<EngagementTeamMembersValueBean> sortTeamMembers( Map<String, Object> extractParams )
  {
    CallPrcEngagementTeamMembersSort proc = new CallPrcEngagementTeamMembersSort( dataSource );
    Map<String, Object> results = proc.executeProcedure( extractParams );
    List<EngagementTeamMembersValueBean> engagementTeamMembersValueBeanList = (List<EngagementTeamMembersValueBean>)results.get( "p_out_result_set1" );
    return engagementTeamMembersValueBeanList;
  }

  @Override
  public List<EngagementTeamsValueBean> sortTeams( Map<String, Object> extractParams )
  {
    CallPrcEngagementTeamsSort proc = new CallPrcEngagementTeamsSort( dataSource );
    Map<String, Object> results = proc.executeProcedure( extractParams );
    List<EngagementTeamsValueBean> engagementTeamsValueBeanList = (List<EngagementTeamsValueBean>)results.get( "p_out_result_set1" );
    return engagementTeamsValueBeanList;
  }

  @Override
  public List<EngagementChartValueBean> getRecognitionsByPromoChartData( Map<String, Object> extractParams ) throws Exception
  {
    List<EngagementChartValueBean> valueBeanList = null;
    Map<String, Object> results = new HashMap<String, Object>();
    CallPrcEngagementRecognitionsByPromoChart proc = new CallPrcEngagementRecognitionsByPromoChart( dataSource );
    results = proc.executeProcedure( extractParams );
    valueBeanList = validateOutput( valueBeanList, results );
    return valueBeanList;
  }

  @Override
  public List<EngagementChartValueBean> getUniqueRecognitionsChartData( Map<String, Object> extractParams ) throws Exception
  {
    List<EngagementChartValueBean> valueBeanList = null;
    Map<String, Object> results = new HashMap<String, Object>();
    CallPrcEngagementUniqueRecognitionsOrgChart proc = new CallPrcEngagementUniqueRecognitionsOrgChart( dataSource );
    results = proc.executeProcedure( extractParams );
    valueBeanList = validateOutput( valueBeanList, results );
    return valueBeanList;
  }

  @Override
  public List<EngagementChartValueBean> getLoginVisitsChartData( Map<String, Object> extractParams ) throws Exception
  {
    List<EngagementChartValueBean> valueBeanList = null;
    Map<String, Object> results = new HashMap<String, Object>();
    CallPrcEngagementLoginChart proc = new CallPrcEngagementLoginChart( dataSource );
    results = proc.executeProcedure( extractParams );
    valueBeanList = validateOutput( valueBeanList, results );
    return valueBeanList;
  }

  protected List<EngagementChartValueBean> validateOutput( List<EngagementChartValueBean> valueBeanList, Map<String, Object> results ) throws Exception
  {
    if ( BAD_OUTPUT.equals( results.get( OUTPUT_RETURN_CODE ) ) )
    {
      throw new Exception( "Stored procedure returned error. Procedure returned: " + results.get( OUTPUT_RETURN_CODE ) );
    }
    else
    {
      valueBeanList = (List<EngagementChartValueBean>)results.get( "p_out_data" );
    }
    return valueBeanList;
  }

  public Map refreshEngagementScores( Long userId )
  {
    CallPrcEngagementRefreshScores proc = new CallPrcEngagementRefreshScores( dataSource );

    return proc.executeProcedure( userId );
  }

  @Override
  public Map<String, Object> getEngagementRecognized( Map<String, Object> queryParams )
  {
    CallPrcEngagementRecognized proc = new CallPrcEngagementRecognized( dataSource );
    return proc.executeProcedure( queryParams );
  }

  private DataSource dataSource;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @Override
  public boolean isParticipantInManagerAudience( Long userId )
  {
    Criteria criteria = getSession().createCriteria( EngagementEligManager.class );
    criteria.add( Restrictions.eq( "id", userId ) );
    criteria.add( Restrictions.gt( "nodeUsersCnt", 1L ) );
    List<EngagementEligManager> eligibleManagers = criteria.list();
    if ( eligibleManagers != null && eligibleManagers.size() > 0 )
    {
      return true;
    }
    return false;
  }

  @Override
  public Map<String, Object> getNotificationsData( Map<String, Object> queryParams )
  {
    CallPrcEngagementNotifications proc = new CallPrcEngagementNotifications( dataSource );
    return proc.executeProcedure( queryParams );
  }

  @Override
  public List<EngagementEligManager> getAllEligibleManagers()
  {
    Criteria criteria = getSession().createCriteria( EngagementEligManager.class );
    List<EngagementEligManager> eligibleManagers = criteria.list();
    return eligibleManagers;
  }

  @Override
  public List<EngagementEligUser> getAllEligibleParticipants()
  {
    Criteria criteria = getSession().createCriteria( EngagementEligUser.class );
    List<EngagementEligUser> eligibleUsers = criteria.list();
    return eligibleUsers;
  }

  @Override
  public List<String> getAllEligiblePromotions()
  {
    Query query = null;

    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getAllEligiblePromotions.dmsa" );
    }
    else
    {
      query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getAllEligiblePromotions" );
    }
    query.setParameter( "locale", UserManager.getUserLocale() );
    return query.list();
  }

  @Override
  public Map deleteEngagementPromotion( Long promotionId )
  {
    CallPrcEngagementPromotionRemove procedure = new CallPrcEngagementPromotionRemove( dataSource );
    return procedure.executeProcedure( promotionId );
  }

  @Override
  public String getEngagementManagerModuleAppAudienceTypeByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.participant.findEngagementManagerModuleAppAudienceTypeByUserId" );
    query.setParameter( "pax_user_id", userId );

    return (String)query.uniqueResult();

  }
  
  @Override
  public EngagementPromotionData getLiveEngagementPromotionData()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getLiveEngagementPromotionData" );
    query.setResultTransformer( new LiveEngagementPromotionDataTransformer() );
    List promotionList = query.list();
    if ( promotionList != null && promotionList.size() > 0 )
    {
      return (EngagementPromotionData)promotionList.get( 0 );
    }
    return null;
  }
  
  private class LiveEngagementPromotionDataTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      EngagementPromotionData bean = new EngagementPromotionData();
      bean.setPromotionId( (Long)tuple[0] );
      bean.setTileDisplayStartDate( extractDate( tuple[1] ) );
      return bean;
    }
  }
    
  @Override
  public Set<Long> getAllEligiblePromotionIds( Long promotionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getAllEligiblePromotionIds" );
    query.setParameter( "promotionId", promotionId );
    return new LinkedHashSet<Long>( query.list() );
  }


}
