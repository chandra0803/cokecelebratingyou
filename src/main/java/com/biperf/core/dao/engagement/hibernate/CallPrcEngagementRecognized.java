/**
 * 
 */

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
import com.biperf.core.value.EngagementRecognizedParticipantValueBean;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcEngagementRecognized extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_ENGAGEMENT.PRC_ENG_RECOG_BYUSER";

  public CallPrcEngagementRecognized( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_node_id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_mode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_start_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_end_date", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_giver_recvr", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_time_frame", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_end_month", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_end_year", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_viewer_first_name", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_viewer_avatar_url", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_node_names", OracleTypes.CURSOR, new EngagementRecognizedNodeDataExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new EngagementRecognizedDataExtractor() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> extractParams )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_id", extractParams.get( "userId" ) );
    inParams.put( "p_in_mode", extractParams.get( "mode" ) );
    inParams.put( "p_in_giver_recvr", extractParams.get( "giverReceiver" ) );
    inParams.put( "p_in_time_frame", extractParams.get( "timeframeType" ) );
    if ( extractParams.get( "nodeId" ) != null )
    {
      inParams.put( "p_in_node_id", extractParams.get( "nodeId" ).toString() );
    }
    else
    {
      inParams.put( "p_in_node_id", "" );
    }
    inParams.put( "p_in_locale", extractParams.get( "locale" ) );
    inParams.put( "p_in_end_month", extractParams.get( "endMonth" ) );
    inParams.put( "p_in_end_year", extractParams.get( "endYear" ) );
    // Always send the date in English locale for the procedure input
    inParams.put( "p_in_start_date", DateUtils.toDisplayString( (Date)extractParams.get( "startDate" ), Locale.ENGLISH ) );
    inParams.put( "p_in_end_date", DateUtils.toDisplayString( (Date)extractParams.get( "endDate" ), Locale.ENGLISH ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class EngagementRecognizedDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<EngagementRecognizedParticipantValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EngagementRecognizedParticipantValueBean> resultList = new ArrayList<EngagementRecognizedParticipantValueBean>();
      while ( rs.next() )
      {
        EngagementRecognizedParticipantValueBean engagementRecognizedParticipantView = new EngagementRecognizedParticipantValueBean();
        engagementRecognizedParticipantView.setId( rs.getLong( "user_id" ) );
        engagementRecognizedParticipantView.setName( rs.getString( "first_name" ) );
        engagementRecognizedParticipantView.setAvatarUrl( rs.getString( "avatar" ) );
        engagementRecognizedParticipantView.setCount( rs.getInt( "recognition_count" ) );
        engagementRecognizedParticipantView.setNodeName( rs.getString( "node_name" ) );
        engagementRecognizedParticipantView.setNodeId( rs.getLong( "node_id" ) );
        resultList.add( engagementRecognizedParticipantView );
      }
      return resultList;
    }
  }

  private class EngagementRecognizedNodeDataExtractor implements ResultSetExtractor
  {
    @Override
    public List<EngagementRecognizedParticipantValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EngagementRecognizedParticipantValueBean> nodesList = new ArrayList<EngagementRecognizedParticipantValueBean>();
      while ( rs.next() )
      {
        EngagementRecognizedParticipantValueBean engagementRecognizedNodesView = new EngagementRecognizedParticipantValueBean();
        engagementRecognizedNodesView.setNodeName( rs.getString( "node_name" ) );
        engagementRecognizedNodesView.setNodeId( rs.getLong( "node_id" ) );
        nodesList.add( engagementRecognizedNodesView );
      }
      return nodesList;
    }
  }

}
