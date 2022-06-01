
package com.biperf.core.dao.engagement.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.EngagementTeamsValueBean;

import oracle.jdbc.OracleTypes;

/**
 * 
 * CallPrcEngagementTeamsSort.
 * 
 * @author kandhi
 * @since Jul 29, 2014
 * @version 1.0
 */
public class CallPrcEngagementTeamsSort extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_ENGAGEMENT.PRC_ENG_CHILDNODES_SORT";

  public CallPrcEngagementTeamsSort( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_mode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_time_frame", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_node_id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_end_month", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_end_year", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rownum_start", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rownum_end", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sort_mem_col", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sort_mem_order", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sort_team_col", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sort_team_order", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_start_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_end_date", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set1", OracleTypes.CURSOR, new EngagementTeamsDataExtractor() ) );// Teams
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> extractParams )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_id", extractParams.get( "userId" ) );
    inParams.put( "p_in_mode", extractParams.get( "mode" ) );
    inParams.put( "p_in_time_frame", extractParams.get( "timeframeType" ) );
    inParams.put( "p_in_node_id", extractParams.get( "nodeId" ) );
    inParams.put( "p_in_locale", extractParams.get( "locale" ) );
    inParams.put( "p_in_end_month", extractParams.get( "endMonth" ) );
    inParams.put( "p_in_end_year", extractParams.get( "endYear" ) );
    inParams.put( "p_in_rownum_start", extractParams.get( "rowNumStart" ) );
    inParams.put( "p_in_rownum_end", extractParams.get( "rowNumEnd" ) );
    // Sort
    inParams.put( "p_in_sort_mem_col", "last_name" );
    inParams.put( "p_in_sort_mem_order", "asc" );

    String sortedBy = extractParams.get( "sortedBy" ) == null ? "asc" : (String)extractParams.get( "sortedBy" );
    inParams.put( "p_in_sort_team_col", getSortColumn( extractParams ) );
    inParams.put( "p_in_sort_team_order", sortedBy );

    // Always send the date in English locale for the procedure input
    inParams.put( "p_in_start_date", DateUtils.toDisplayString( (Date)extractParams.get( "startDate" ), Locale.ENGLISH ) );
    inParams.put( "p_in_end_date", DateUtils.toDisplayString( (Date)extractParams.get( "endDate" ), Locale.ENGLISH ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private String getSortColumn( Map<String, Object> extractParams )
  {
    String sortedOn = "member";
    if ( extractParams.get( "sortedOn" ) != null && !StringUtil.isEmpty( (String)extractParams.get( "sortedOn" ) ) )
    {
      sortedOn = (String)extractParams.get( "sortedOn" );
    }
    return sortedOn;
  }

  private class EngagementTeamsDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<EngagementTeamsValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EngagementTeamsValueBean> engagementTeamsValueBeanList = new ArrayList<EngagementTeamsValueBean>();
      while ( rs.next() )
      {
        EngagementTeamsValueBean engagementTeamsValueBean = new EngagementTeamsValueBean();
        engagementTeamsValueBean.setCompanyGoal( rs.getInt( "score_target" ) );
        engagementTeamsValueBean.setNodeId( rs.getLong( "node_id" ) );
        engagementTeamsValueBean.setNodeName( rs.getString( "member" ) );
        engagementTeamsValueBean.setScore( rs.getInt( "score_actual" ) );
        engagementTeamsValueBean.setReceivedTarget( rs.getInt( "recRecv_target" ) );
        engagementTeamsValueBean.setSentTarget( rs.getInt( "recSent_target" ) );
        engagementTeamsValueBean.setConnectedToTarget( rs.getInt( "paxRecTo_target" ) );
        engagementTeamsValueBean.setConnectedFromTarget( rs.getInt( "paxRecBy_target" ) );
        engagementTeamsValueBean.setLoginActivityTarget( rs.getInt( "visits_target" ) );
        engagementTeamsValueBean.setReceivedCnt( rs.getInt( "recRecv_actual" ) );
        engagementTeamsValueBean.setSentCnt( rs.getInt( "recSent_actual" ) );
        engagementTeamsValueBean.setConnectedToCnt( rs.getInt( "paxRecTo_actual" ) );
        engagementTeamsValueBean.setConnectedFromCnt( rs.getInt( "paxRecBy_actual" ) );
        engagementTeamsValueBean.setLoginActivityCnt( rs.getInt( "visits_actual" ) );
        engagementTeamsValueBean.setManagerName( rs.getString( "manager_name" ) );

        engagementTeamsValueBeanList.add( engagementTeamsValueBean );
      }
      return engagementTeamsValueBeanList;
    }
  }
}
