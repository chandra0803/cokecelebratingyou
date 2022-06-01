
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
import com.biperf.core.value.EngagementChartValueBean;

import oracle.jdbc.OracleTypes;

/**
 * 
 * CallPrcEngagementLoginChart.
 * 
 * @author kandhi
 * @since Jul 22, 2014
 * @version 1.0
 */
public class CallPrcEngagementLoginChart extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_ENGAGEMENT.PRC_ENG_LOGIN_CHART";

  public CallPrcEngagementLoginChart( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_node_id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_start_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_end_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_time_frame", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", OracleTypes.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new EngagementAveragesDataExtractor() ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> extractParams )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_node_id", extractParams.get( "nodeId" ) );
    // Always send the date in English locale for the procedure input
    inParams.put( "p_in_start_date", DateUtils.toDisplayString( (Date)extractParams.get( "startDate" ), Locale.ENGLISH ) );
    inParams.put( "p_in_end_date", DateUtils.toDisplayString( (Date)extractParams.get( "endDate" ), Locale.ENGLISH ) );
    inParams.put( "p_in_languageCode", extractParams.get( "locale" ) );
    inParams.put( "p_in_time_frame", extractParams.get( "timeframeType" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class EngagementAveragesDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<EngagementChartValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EngagementChartValueBean> engagementChartValueBeanList = new ArrayList<EngagementChartValueBean>();
      while ( rs.next() )
      {
        EngagementChartValueBean engagementChartValueBean = new EngagementChartValueBean();
        engagementChartValueBean.setLabel( DateUtils.toDisplayString( rs.getDate( "date_created" ) ) );
        engagementChartValueBean.setValue( rs.getInt( "login_activity_count" ) );
        engagementChartValueBeanList.add( engagementChartValueBean );
      }
      return engagementChartValueBeanList;
    }
  }
}
