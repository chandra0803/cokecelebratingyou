
package com.biperf.core.dao.reports.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.value.loginreport.LoginByOrganizationReportValue;
import com.biperf.core.value.loginreport.LoginByPaxReportValue;
import com.biperf.core.value.loginreport.LoginByTimeReportValue;
import com.biperf.core.value.loginreport.LoginReportValue;
import com.biperf.core.value.loginreport.ReportLogonActivityListOfParticipantsValue;
import com.biperf.core.value.loginreport.ReportLogonActivityOrgPercentageValue;
import com.biperf.core.value.loginreport.ReportLogonActivityTopLevelValue;
import com.biperf.core.value.loginreport.ReportParticipantLogonActivityValue;

import oracle.jdbc.OracleTypes;

public class CallPrcLoginActivity extends StoredProcedure
{
  // Chart
  private static final String MONTHLY_LOGIN_BAR_RESULTS = "pkg_query_login_activity.prc_getMonthlyLoginBarResults";
  private static final String TOP_PAX_LOGINS_BY_NAME = "pkg_query_login_activity.prc_getTopPaxLoginsByName";
  private static final String LOGIN_PERCENTAGE_BAR_RESULTS = "pkg_query_login_activity.prc_getPercentageBarResults";
  private static final String ORGANIZATION_BAR_RESULTS = "pkg_query_login_activity.prc_getOrganizationBarResults";
  private static final String LOGIN_STATUS_CHART_RESULTS = "pkg_query_login_activity.prc_getLoginStatusChartResults";
  // Table
  private static final String PARTICIPANT_LOGON_ACTIVITY_RESULTS = "pkg_query_login_activity.prc_getPaxResults";
  private static final String LOGIN_LIST_OF_PARTICIPANTS_RESULTS = "pkg_query_login_activity.prc_getListOfPaxResults";
  private static final String ORG_LOGIN_ACTIVITY_TOP_LEVEL_RESULTS = "pkg_query_login_activity.prc_getOrgTopLevelResults";

  public CallPrcLoginActivity( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_uniqueValues", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nodeAndBelow", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_role", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_countryIds", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_userId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortedOn", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
    switch ( STORED_PROC_NAME )
    {
      case MONTHLY_LOGIN_BAR_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new LoginBarMapper() ) );
        break;
      case TOP_PAX_LOGINS_BY_NAME:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new LoginByPaxBarMapper() ) );
        break;
      case LOGIN_PERCENTAGE_BAR_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new LoginPercentageBarMapper() ) );
        break;
      case ORGANIZATION_BAR_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new LoginByOrganizationBarMapper() ) );
        break;
      case LOGIN_STATUS_CHART_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new LoginByTimeBarMapper() ) );
        break;
      case PARTICIPANT_LOGON_ACTIVITY_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new PaxLogonActivityBarMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        break;
      case LOGIN_LIST_OF_PARTICIPANTS_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ListOfPaxResultsMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.INTEGER ) );
        break;
      case ORG_LOGIN_ACTIVITY_TOP_LEVEL_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new TopLevelResultsMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new TopLevelTotalsMapper() ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters ) throws DataAccessException
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_uniqueValues", reportParameters.get( "uniqueValues" ) );
    Object nodeAndBelow = reportParameters.get( "nodeAndBelow" );
    if ( nodeAndBelow != null && ( (Boolean)nodeAndBelow ).booleanValue() )
    {
      inParams.put( "p_in_nodeAndBelow", 1 );
    }
    else
    {
      inParams.put( "p_in_nodeAndBelow", 0 );
    }
    inParams.put( "p_in_role", reportParameters.get( "role" ) );
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_countryIds", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_userId", reportParameters.get( "userId" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_sortedOn", reportParameters.get( "sortedOn" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class LoginBarMapper implements ResultSetExtractor<List<LoginReportValue>>
  {
    @Override
    public List<LoginReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<LoginReportValue> resultSet = new ArrayList<LoginReportValue>();
      while ( rs.next() )
      {
        LoginReportValue result = new LoginReportValue();
        result.setMonth( rs.getString( "OUT_MONTH" ) );
        result.setTotalCnt( rs.getInt( "TOTAL_COUNT" ) );
        resultSet.add( result );
      }
      return resultSet;
    }
  }

  private class LoginByPaxBarMapper implements ResultSetExtractor<List<LoginByPaxReportValue>>
  {
    @Override
    public List<LoginByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<LoginByPaxReportValue> resultSet = new ArrayList<LoginByPaxReportValue>();
      while ( rs.next() )
      {
        LoginByPaxReportValue result = new LoginByPaxReportValue();
        result.setName( rs.getString( "name" ) );
        result.setTotalCnt( rs.getInt( "total_visits" ) );
        resultSet.add( result );
      }
      return resultSet;
    }
  }

  private class LoginPercentageBarMapper implements ResultSetExtractor<List<ReportLogonActivityOrgPercentageValue>>
  {

    @Override
    public List<ReportLogonActivityOrgPercentageValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ReportLogonActivityOrgPercentageValue> resultSet = new ArrayList<ReportLogonActivityOrgPercentageValue>();
      while ( rs.next() )
      {
        ReportLogonActivityOrgPercentageValue result = new ReportLogonActivityOrgPercentageValue();
        result.setLoggedInPct( rs.getBigDecimal( "PERCENT_LOGIN" ) );
        result.setNotLoggedInPct( rs.getBigDecimal( "PERCENT_NOT_LOGIN" ) );
        result.setNodeName( rs.getString( "NODE_NAME" ) );
        resultSet.add( result );
      }
      return resultSet;
    }
  }

  private class LoginByOrganizationBarMapper implements ResultSetExtractor<List<LoginByOrganizationReportValue>>
  {
    @Override
    public List<LoginByOrganizationReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<LoginByOrganizationReportValue> resultSet = new ArrayList<LoginByOrganizationReportValue>();
      while ( rs.next() )
      {
        LoginByOrganizationReportValue result = new LoginByOrganizationReportValue();
        result.setNodeName( rs.getString( "NAME" ) );
        result.setTotalCnt( rs.getInt( "TOTAL_COUNT" ) );
        resultSet.add( result );
      }
      return resultSet;
    }
  }

  private class LoginByTimeBarMapper implements ResultSetExtractor<List<LoginByTimeReportValue>>
  {
    @Override
    public List<LoginByTimeReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<LoginByTimeReportValue> resultSet = new ArrayList<LoginByTimeReportValue>();
      while ( rs.next() )
      {
        LoginByTimeReportValue result = new LoginByTimeReportValue();
        result.setTotalCnt1( rs.getInt( "LOGGED_IN" ) );
        result.setTotalCnt2( rs.getInt( "NOT_LOGGED_IN" ) );
        resultSet.add( result );
      }
      return resultSet;
    }
  }

  /////////////// PARTICIPANT LOGON ACTIVITY RESULTS//////////////////////////////

  private class PaxLogonActivityBarMapper implements ResultSetExtractor<List<ReportParticipantLogonActivityValue>>
  {

    @Override
    public List<ReportParticipantLogonActivityValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ReportParticipantLogonActivityValue> resultSet = new ArrayList<ReportParticipantLogonActivityValue>();
      while ( rs.next() )
      {
        ReportParticipantLogonActivityValue result = new ReportParticipantLogonActivityValue();
        result.setParticipant( rs.getString( "pax_name" ) );
        result.setDepartment( rs.getString( "pax_department" ) );
        result.setPosition( rs.getString( "pax_position" ) );
        result.setCountry( rs.getString( "pax_country" ) );
        result.setOrganization( rs.getString( "organization_name" ) );
        result.setLoginDate( rs.getDate( "login_date" ) );
        resultSet.add( result );
      }
      return resultSet;
    }
  }

  //////////////////////////////// LIST OF PARTICIPANT///////////////////////////

  private class ListOfPaxResultsMapper implements ResultSetExtractor<List<ReportLogonActivityListOfParticipantsValue>>
  {
    @Override
    public List<ReportLogonActivityListOfParticipantsValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ReportLogonActivityListOfParticipantsValue> resultSet = new ArrayList<ReportLogonActivityListOfParticipantsValue>();
      while ( rs.next() )
      {
        ReportLogonActivityListOfParticipantsValue result = new ReportLogonActivityListOfParticipantsValue();
        result.setNodeName( rs.getString( "org" ) );
        result.setUserId( rs.getLong( "userId" ) );
        result.setFullName( rs.getString( "pax_name" ) );
        result.setDepartment( rs.getString( "department" ) );
        result.setPosition( rs.getString( "pax_position" ) );
        result.setLastLoggedIn( rs.getDate( "last_login" ) );
        result.setTotalCnt( rs.getInt( "total_visits" ) );
        resultSet.add( result );
      }
      return resultSet;
    }
  }

  ///////////////////////////// TOP LEVEL///////////////////////////////////////

  private class TopLevelResultsMapper implements ResultSetExtractor<List<ReportLogonActivityTopLevelValue>>
  {
    @Override
    public List<ReportLogonActivityTopLevelValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ReportLogonActivityTopLevelValue> resultSet = new ArrayList<ReportLogonActivityTopLevelValue>();
      while ( rs.next() )
      {
        ReportLogonActivityTopLevelValue result = new ReportLogonActivityTopLevelValue();
        result.setElgParticipantsCnt( rs.getInt( "eligible_pax" ) );
        result.setLoggedInCnt( rs.getInt( "logged_in_cnt" ) );
        result.setLoggedInPct( rs.getBigDecimal( "percent_login" ) );
        result.setNodeId( rs.getLong( "NODE_ID" ) );
        result.setNodeName( rs.getString( "NAME" ) );
        result.setNotLoggedInCnt( rs.getInt( "not_logged_in_cnt" ) );
        result.setNotLoggedInPct( rs.getBigDecimal( "percent_not_login" ) );
        result.setTotalVisitsCnt( rs.getInt( "TOTAL_VISITS" ) );
        resultSet.add( result );
      }
      return resultSet;
    }
  }

  private class TopLevelTotalsMapper implements ResultSetExtractor<ReportLogonActivityTopLevelValue>
  {
    @Override
    public ReportLogonActivityTopLevelValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ReportLogonActivityTopLevelValue result = new ReportLogonActivityTopLevelValue();
      if ( rs.next() )
      {
        result.setElgParticipantsCnt( rs.getInt( "ELIGIBLE_PARTICIPANTS" ) );
        result.setLoggedInCnt( rs.getInt( "LOGGED_IN_CNT" ) );
        result.setLoggedInPct( rs.getBigDecimal( "PERCENT_LOGIN" ) );
        result.setNotLoggedInCnt( rs.getInt( "NOT_LOGGED_IN_CNT" ) );
        result.setNotLoggedInPct( rs.getBigDecimal( "PERCENT_NOT_LOGIN" ) );
        result.setTotalVisitsCnt( rs.getInt( "TOTAL_VISITS" ) );
      }
      return result;
    }
  }

}
