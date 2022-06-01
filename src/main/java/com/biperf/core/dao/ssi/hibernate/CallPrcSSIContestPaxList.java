
package com.biperf.core.dao.ssi.hibernate;

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

import com.biperf.core.value.ssi.SSIContestPayoutDtgtTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutStepItUpTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestSummaryTDPaxResultBean;

import oracle.jdbc.OracleTypes;

/**
 * CallPrcSSIContestPaxList is used to get the participant summary table data for creator and manager in contest detail page.
 * Input params for userId will be null for creator user. 
 * It is mainly used to get the table data for creator/manager
 * 
 * @author dudam
 * @since Jan 30, 2015
 * @version 1.0
 */
public class CallPrcSSIContestPaxList extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST_DATA.PRC_SSI_CONTEST_PAX_LIST";

  public CallPrcSSIContestPaxList( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.INTEGER ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.INTEGER ) );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_contest_type", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_size_data", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_obj_ref_cursor", OracleTypes.CURSOR, new SSIContestParticipantsSummaryResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_obj_total_ref_cursor", OracleTypes.CURSOR, new SSIContestParticipantsSummaryTotalResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_dtgt_ref_cursor", OracleTypes.CURSOR, new SSIContestParticipantsSummaryDtgtResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_total_dtgt_ref_cursor", OracleTypes.CURSOR, new SSIContestParticipantsSummaryDtgtTotalResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_siu_ref_cursor", OracleTypes.CURSOR, new SSIContestParticipantsSummarySiuResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_siu_total_ref_cursor", OracleTypes.CURSOR, new SSIContestParticipantsSummarySiuTotalResultSetExtractor() ) );
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> inParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", inParameters.get( "contestId" ) );
    inParams.put( "p_in_sortColName", inParameters.get( "sortColumnName" ) );
    inParams.put( "p_in_sortedBy", inParameters.get( "sortBy" ) );
    inParams.put( "p_in_rowNumStart", inParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", inParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_user_id", inParameters.get( "userId" ) );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  /**
   * SSIContestParticipantsSummaryObjectivesResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestParticipantsSummaryResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIContestSummaryTDPaxResultBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestSummaryTDPaxResultBean> participantsSummary = new ArrayList<SSIContestSummaryTDPaxResultBean>();
      while ( rs.next() )
      {
        SSIContestSummaryTDPaxResultBean participantSummary = new SSIContestSummaryTDPaxResultBean();
        // rs.getInt( "RN" );
        participantSummary.setUserId( rs.getLong( "USER_ID" ) );
        participantSummary.setLastName( rs.getString( "LAST_NAME" ) );
        participantSummary.setFirstName( rs.getString( "FIRST_NAME" ) );
        participantSummary.setOrgUnit( rs.getString( "ORG_NAME" ) );
        participantSummary.setObjective( rs.getDouble( "OBJECTIVE_AMOUNT" ) );
        participantSummary.setCurrentActivity( rs.getDouble( "ACTIVITY_AMT" ) );
        participantSummary.setPercentToObjective( rs.getLong( "PERC_ACHIEVED" ) );
        participantSummary.setObjectivePayout( rs.getLong( "OBJECTIVE_PAYOUT" ) );// SAME
        participantSummary.setPayoutValue( rs.getLong( "OBJECTIVE_PAYOUT" ) );// SAME
        participantSummary.setBonusAmount( rs.getLong( "OBJECTIVE_BONUS_PAYOUT" ) );
        participantSummary.setPayoutAmount( rs.getLong( "TOTAL_PAYOUT" ) );
        participantSummary.setPayoutDescription( rs.getString( "OBJECTIVE_PAYOUT_DESCRIPTION" ) );
        participantSummary.setContestId( rs.getLong( "CONTEST_ID" ) );
        participantsSummary.add( participantSummary );
      }
      return participantsSummary;
    }
  }

  /**
   * SSIContestParticipantsSummaryTotalResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestParticipantsSummaryTotalResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPayoutObjectivesTotalsValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      SSIContestPayoutObjectivesTotalsValueBean participantsSummaryTotal = new SSIContestPayoutObjectivesTotalsValueBean();
      while ( rs.next() )
      {
        participantsSummaryTotal.setObjectiveAmountTotal( rs.getDouble( "OBJECTIVE_AMOUNT" ) );
        participantsSummaryTotal.setActivityAmountTotal( rs.getDouble( "ACTIVITY_AMT" ) );
        participantsSummaryTotal.setPercentageAcheived( rs.getLong( "PERC_ACHIEVED" ) );
        participantsSummaryTotal.setMaxPayout( rs.getLong( "TOTAL_POTENTIAL_PAYOUT" ) );
        participantsSummaryTotal.setObjectivePayoutTotal( rs.getLong( "TOTAL_OBJECTIVE_PAYOUT" ) );
        participantsSummaryTotal.setBonusPayoutTotal( rs.getLong( "TOTAL_BONUS_PAYOUT" ) );
        participantsSummaryTotal.setMaxPotential( rs.getDouble( "TOTAL_POTENTIAL_PAYOUT" ) );
      }
      return participantsSummaryTotal;
    }
  }

  /**
   * SSIContestParticipantsSummaryDtgtResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestParticipantsSummaryDtgtResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIContestSummaryTDPaxResultBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestSummaryTDPaxResultBean> participants = new ArrayList<SSIContestSummaryTDPaxResultBean>();
      while ( rs.next() )
      {
        SSIContestSummaryTDPaxResultBean participant = new SSIContestSummaryTDPaxResultBean();
        // rs.getLong( "RN" );
        participant.setUserId( rs.getLong( "USER_ID" ) );
        participant.setLastName( rs.getString( "LAST_NAME" ) );
        participant.setFirstName( rs.getString( "FIRST_NAME" ) );
        participant.setOrgUnit( rs.getString( "NAME" ) );

        participant.setActivity1Id( rs.getLong( "ACTIVITY1" ) );
        participant.setActivityDescription1( rs.getString( "ACTIVITY_DESCRIPTION1" ) );
        participant.setPayoutValue1( rs.getLong( "PAYOUT_VALUE1" ) );
        participant.setActivityAmount1( rs.getDouble( "ACTIVITY_AMT1" ) );
        participant.setPayoutQuantity1( rs.getLong( "PAYOUT_QUANTITY1" ) );

        participant.setActivity2Id( rs.getLong( "ACTIVITY2" ) );
        participant.setActivityDescription2( rs.getString( "ACTIVITY_DESCRIPTION2" ) );
        participant.setPayoutValue2( rs.getLong( "PAYOUT_VALUE2" ) );
        participant.setActivityAmount2( rs.getDouble( "ACTIVITY_AMT2" ) );
        participant.setPayoutQuantity2( rs.getLong( "PAYOUT_QUANTITY2" ) );

        participant.setActivity3Id( rs.getLong( "ACTIVITY3" ) );
        participant.setActivityDescription3( rs.getString( "ACTIVITY_DESCRIPTION3" ) );
        participant.setPayoutValue3( rs.getLong( "PAYOUT_VALUE3" ) );
        participant.setActivityAmount3( rs.getDouble( "ACTIVITY_AMT3" ) );
        participant.setPayoutQuantity3( rs.getLong( "PAYOUT_QUANTITY3" ) );

        participant.setActivity4Id( rs.getLong( "ACTIVITY4" ) );
        participant.setActivityDescription4( rs.getString( "ACTIVITY_DESCRIPTION4" ) );
        participant.setPayoutValue4( rs.getLong( "PAYOUT_VALUE4" ) );
        participant.setActivityAmount4( rs.getDouble( "ACTIVITY_AMT4" ) );
        participant.setPayoutQuantity4( rs.getLong( "PAYOUT_QUANTITY4" ) );

        participant.setActivity5Id( rs.getLong( "ACTIVITY5" ) );
        participant.setActivityDescription5( rs.getString( "ACTIVITY_DESCRIPTION5" ) );
        participant.setPayoutValue5( rs.getLong( "PAYOUT_VALUE5" ) );
        participant.setActivityAmount5( rs.getDouble( "ACTIVITY_AMT5" ) );
        participant.setPayoutQuantity5( rs.getLong( "PAYOUT_QUANTITY5" ) );

        participant.setActivity6Id( rs.getLong( "ACTIVITY6" ) );
        participant.setActivityDescription6( rs.getString( "ACTIVITY_DESCRIPTION6" ) );
        participant.setPayoutValue6( rs.getLong( "PAYOUT_VALUE6" ) );
        participant.setActivityAmount6( rs.getDouble( "ACTIVITY_AMT6" ) );
        participant.setPayoutQuantity6( rs.getLong( "PAYOUT_QUANTITY6" ) );

        participant.setActivity7Id( rs.getLong( "ACTIVITY7" ) );
        participant.setActivityDescription7( rs.getString( "ACTIVITY_DESCRIPTION7" ) );
        participant.setPayoutValue7( rs.getLong( "PAYOUT_VALUE7" ) );
        participant.setActivityAmount7( rs.getDouble( "ACTIVITY_AMT7" ) );
        participant.setPayoutQuantity7( rs.getLong( "PAYOUT_QUANTITY7" ) );

        participant.setActivity8Id( rs.getLong( "ACTIVITY8" ) );
        participant.setActivityDescription8( rs.getString( "ACTIVITY_DESCRIPTION8" ) );
        participant.setPayoutValue8( rs.getLong( "PAYOUT_VALUE8" ) );
        participant.setActivityAmount8( rs.getDouble( "ACTIVITY_AMT8" ) );
        participant.setPayoutQuantity8( rs.getLong( "PAYOUT_QUANTITY8" ) );

        participant.setActivity9Id( rs.getLong( "ACTIVITY9" ) );
        participant.setActivityDescription9( rs.getString( "ACTIVITY_DESCRIPTION9" ) );
        participant.setPayoutValue9( rs.getLong( "PAYOUT_VALUE9" ) );
        participant.setActivityAmount9( rs.getDouble( "ACTIVITY_AMT9" ) );
        participant.setPayoutQuantity9( rs.getLong( "PAYOUT_QUANTITY9" ) );

        participant.setActivity10Id( rs.getLong( "ACTIVITY10" ) );
        participant.setActivityDescription10( rs.getString( "ACTIVITY_DESCRIPTION10" ) );
        participant.setPayoutValue10( rs.getLong( "PAYOUT_VALUE10" ) );
        participant.setActivityAmount10( rs.getDouble( "ACTIVITY_AMT10" ) );
        participant.setPayoutQuantity10( rs.getLong( "PAYOUT_QUANTITY10" ) );

        participant.setTotalPayoutValue( rs.getLong( "TOTAL_PAYOUT_VALUE" ) );

        participant.setContestId( rs.getLong( "CONTEST_ID" ) );

        participants.add( participant );
      }
      return participants;
    }
  }

  /**
   * SSIContestParticipantsSummaryDtgtTotalResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestParticipantsSummaryDtgtTotalResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPayoutDtgtTotalsValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      SSIContestPayoutDtgtTotalsValueBean participantsSummaryTotal = new SSIContestPayoutDtgtTotalsValueBean();
      while ( rs.next() )
      {

        participantsSummaryTotal.setPayoutValueTotal1( rs.getLong( "PAYOUT_VALUE_TOTAL1" ) );
        participantsSummaryTotal.setActivityAmountTotal1( rs.getDouble( "ACTIVITY_AMT_TOTAL1" ) );
        participantsSummaryTotal.setPayoutQuantityTotal1( rs.getLong( "PAYOUT_QUANTITY_TOTAL1" ) );

        participantsSummaryTotal.setPayoutValueTotal2( rs.getLong( "PAYOUT_VALUE_TOTAL2" ) );
        participantsSummaryTotal.setActivityAmountTotal2( rs.getDouble( "ACTIVITY_AMT_TOTAL2" ) );
        participantsSummaryTotal.setPayoutQuantityTotal2( rs.getLong( "PAYOUT_QUANTITY_TOTAL2" ) );

        participantsSummaryTotal.setPayoutValueTotal3( rs.getLong( "PAYOUT_VALUE_TOTAL3" ) );
        participantsSummaryTotal.setActivityAmountTotal3( rs.getDouble( "ACTIVITY_AMT_TOTAL3" ) );
        participantsSummaryTotal.setPayoutQuantityTotal3( rs.getLong( "PAYOUT_QUANTITY_TOTAL3" ) );

        participantsSummaryTotal.setPayoutValueTotal4( rs.getLong( "PAYOUT_VALUE_TOTAL4" ) );
        participantsSummaryTotal.setActivityAmountTotal4( rs.getDouble( "ACTIVITY_AMT_TOTAL4" ) );
        participantsSummaryTotal.setPayoutQuantityTotal4( rs.getLong( "PAYOUT_QUANTITY_TOTAL4" ) );

        participantsSummaryTotal.setPayoutValueTotal5( rs.getLong( "PAYOUT_VALUE_TOTAL5" ) );
        participantsSummaryTotal.setActivityAmountTotal5( rs.getDouble( "ACTIVITY_AMT_TOTAL5" ) );
        participantsSummaryTotal.setPayoutQuantityTotal5( rs.getLong( "PAYOUT_QUANTITY_TOTAL5" ) );

        participantsSummaryTotal.setPayoutValueTotal6( rs.getLong( "PAYOUT_VALUE_TOTAL6" ) );
        participantsSummaryTotal.setActivityAmountTotal6( rs.getDouble( "ACTIVITY_AMT_TOTAL6" ) );
        participantsSummaryTotal.setPayoutQuantityTotal6( rs.getLong( "PAYOUT_QUANTITY_TOTAL6" ) );

        participantsSummaryTotal.setPayoutValueTotal7( rs.getLong( "PAYOUT_VALUE_TOTAL7" ) );
        participantsSummaryTotal.setActivityAmountTotal7( rs.getDouble( "ACTIVITY_AMT_TOTAL7" ) );
        participantsSummaryTotal.setPayoutQuantityTotal7( rs.getLong( "PAYOUT_QUANTITY_TOTAL7" ) );

        participantsSummaryTotal.setPayoutValueTotal8( rs.getLong( "PAYOUT_VALUE_TOTAL8" ) );
        participantsSummaryTotal.setActivityAmountTotal8( rs.getDouble( "ACTIVITY_AMT_TOTAL8" ) );
        participantsSummaryTotal.setPayoutQuantityTotal8( rs.getLong( "PAYOUT_QUANTITY_TOTAL8" ) );

        participantsSummaryTotal.setPayoutValueTotal9( rs.getLong( "PAYOUT_VALUE_TOTAL9" ) );
        participantsSummaryTotal.setActivityAmountTotal9( rs.getDouble( "ACTIVITY_AMT_TOTAL9" ) );
        participantsSummaryTotal.setPayoutQuantityTotal9( rs.getLong( "PAYOUT_QUANTITY_TOTAL9" ) );

        participantsSummaryTotal.setPayoutValueTotal10( rs.getLong( "PAYOUT_VALUE_TOTAL10" ) );
        participantsSummaryTotal.setActivityAmountTotal10( rs.getDouble( "ACTIVITY_AMT_TOTAL10" ) );
        participantsSummaryTotal.setPayoutQuantityTotal10( rs.getLong( "PAYOUT_QUANTITY_TOTAL10" ) );

        participantsSummaryTotal.setPayoutValueTotal( rs.getLong( "TOTAL_PAYOUT_VALUE" ) );

      }
      return participantsSummaryTotal;
    }
  }

  /**
   * SSIContestParticipantsSummarySiuResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestParticipantsSummarySiuResultSetExtractor implements ResultSetExtractor
  {
    public List<SSIContestSummaryTDPaxResultBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestSummaryTDPaxResultBean> participants = new ArrayList<SSIContestSummaryTDPaxResultBean>();
      while ( rs.next() )
      {
        SSIContestSummaryTDPaxResultBean participant = new SSIContestSummaryTDPaxResultBean();
        // rs.getLong( "RN" )
        // rs.getLong( "SSI_CONTEST_ID" )
        participant.setUserId( rs.getLong( "USER_ID" ) );
        participant.setLastName( rs.getString( "LAST_NAME" ) );
        participant.setFirstName( rs.getString( "FIRST_NAME" ) );
        participant.setOrgUnit( rs.getString( "ORG_NAME" ) );
        participant.setBaseline( rs.getDouble( "SIU_BASELINE_AMOUNT" ) );
        participant.setCurrentActivity( rs.getDouble( "ACTIVITY_AMT" ) );
        participant.setLevelCompleted( rs.getLong( "LEVEL_COMPLETED" ) );
        participant.setLevelPayout( rs.getLong( "LEVEL_PAYOUT" ) );
        participant.setBonusAmount( rs.getLong( "BONUS_PAYOUT" ) );
        participant.setPayoutAmount( rs.getLong( "TOTAL_PAYOUT" ) );
        participant.setPayoutValue( rs.getLong( "TOTAL_PAYOUT" ) );
        participant.setPayoutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );
        participant.setContestId( rs.getLong( "CONTEST_ID" ) );
        participants.add( participant );
      }
      return participants;
    }
  }

  /**
   * SSIContestParticipantsSummarySiuTotalResultSetExtractor is an Inner class which implements the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class SSIContestParticipantsSummarySiuTotalResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPayoutStepItUpTotalsValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      SSIContestPayoutStepItUpTotalsValueBean participantsSummaryTotal = new SSIContestPayoutStepItUpTotalsValueBean();
      while ( rs.next() )
      {
        participantsSummaryTotal.setActivityAmount( rs.getDouble( "ACTIVITY_AMT" ) );
        participantsSummaryTotal.setLevelPayout( rs.getLong( "LEVEL_PAYOUT" ) );
        participantsSummaryTotal.setBonusPayout( rs.getLong( "BONUS_PAYOUT" ) );
        participantsSummaryTotal.setTotalPayout( rs.getLong( "TOTAL_PAYOUT" ) );
        participantsSummaryTotal.setBaselineTotal( rs.getDouble( "SIU_BASELINE_AMOUNT" ) );
      }
      return participantsSummaryTotal;
    }
  }

}
