
package com.biperf.core.dao.engagement.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.value.EngagementSummaryValueBean;

import oracle.jdbc.OracleTypes;

/**
 * 
 * CallPrcEngagementSummary.
 * 
 * @author kandhi
 * @since Jun 4, 2014
 * @version 1.0
 */
public class CallPrcEngagementSummary extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_ENGAGEMENT.PRC_ENGAGEMENT_USER_DETAILS";

  public CallPrcEngagementSummary( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_mode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_time_frame", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_end_month", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_end_year", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new EngagementSummaryDataExtractor() ) );
    compile();
  }

  public Map<String, Object> executeProcedure( String mode, Long userId, Locale userLocale, String timeframeType, int endMonth, int endYear )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_mode", mode );
    inParams.put( "p_in_user_id", userId );
    inParams.put( "p_in_locale", userLocale );
    inParams.put( "p_in_time_frame", timeframeType );
    inParams.put( "p_in_end_month", endMonth );
    inParams.put( "p_in_end_year", endYear );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class EngagementSummaryDataExtractor implements ResultSetExtractor
  {
    @Override
    public EngagementSummaryValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      EngagementSummaryValueBean engagementSummaryValueBean = new EngagementSummaryValueBean();
      while ( rs.next() )
      {
        engagementSummaryValueBean.setAsofDate( rs.getTimestamp( "as_of_date" ) );
        engagementSummaryValueBean.setCompanyGoal( rs.getInt( "company_goal" ) );
        boolean isScoreActive = rs.getBoolean( "is_score_active" );
        engagementSummaryValueBean.setScoreActive( isScoreActive );
        if ( !isScoreActive )
        {
          engagementSummaryValueBean.setSelectedBenchmarks( rs.getLong( "selected_benchmarks" ) );
        }
        engagementSummaryValueBean.setDisplayTarget( rs.getBoolean( "display_target" ) );
        engagementSummaryValueBean.setScore( rs.getInt( "score" ) );
        engagementSummaryValueBean.setReceivedCnt( rs.getInt( "received_count" ) );
        engagementSummaryValueBean.setSentCnt( rs.getInt( "sent_count" ) );
        engagementSummaryValueBean.setConnectedToCnt( rs.getInt( "connected_to_count" ) );
        engagementSummaryValueBean.setConnectedFromCnt( rs.getInt( "connected_from_count" ) );
        engagementSummaryValueBean.setLoginActivityCnt( rs.getInt( "login_activity_count" ) );
        engagementSummaryValueBean.setReceivedTarget( rs.getInt( "received_target" ) );
        engagementSummaryValueBean.setSentTarget( rs.getInt( "sent_target" ) );
        engagementSummaryValueBean.setConnectedToTarget( rs.getInt( "connected_target" ) );
        engagementSummaryValueBean.setConnectedFromTarget( rs.getInt( "connected_from_target" ) );
        engagementSummaryValueBean.setLoginActivityTarget( rs.getInt( "login_activity_target" ) );
        // Fix for 59693
        engagementSummaryValueBean.setDate( rs.getString( "as_of_date_str" ) );
        engagementSummaryValueBean.setTime( rs.getString( "as_of_time_str" ) );
        engagementSummaryValueBean.setTimeZoneId( rs.getString( "timezone_id" ) );

      }
      return engagementSummaryValueBean;
    }
  }
}
