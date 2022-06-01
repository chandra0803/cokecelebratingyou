
package com.biperf.core.dao.ssi.hibernate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestDoThisGetThatActivityPayoutsTotalValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantPayoutsValueBean;
import com.biperf.core.value.ssi.SSIContestPaxPayoutBadgeValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutsValueBean;

import oracle.jdbc.OracleTypes;

// TODO; make me singleton
public class CallPrcSSIContestApprovalsList extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_SSI_CONTEST_DATA.prc_ssi_contest_approvals_list";
  private SSIContestPayoutsValueBean contestPayoutsValueBean = new SSIContestPayoutsValueBean();

  protected static final Log log = LogFactory.getLog( CallPrcSSIContestApprovalsList.class );

  public CallPrcSSIContestApprovalsList( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_ssi_contest_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_contest_activity_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.INTEGER ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_contest_type", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_pax_count", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.INTEGER ) );
    declareParameter( new SqlOutParameter( "p_out_obj_ref_cursor", OracleTypes.CURSOR, new SSIContestObjectiveParticipantPayoutsResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_obj_total_ref_cursor", OracleTypes.CURSOR, new SSIContestObjectivePayoutTotalsResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_dtgt_ref_cursor", OracleTypes.CURSOR, new SSIContestDTGTParticipantPayoutsResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_total_dtgt_ref_cursor", OracleTypes.CURSOR, new SSIContestDTGTPayoutTotalsResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_siu_ref_cursor", OracleTypes.CURSOR, new SSIContestStepItUpParticipantPayoutsResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_siu_total_ref_cursor", OracleTypes.CURSOR, new SSIContestStepItUpPayoutTotalsResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_sr_ref_cursor", OracleTypes.CURSOR, new SSIContestStackRankParticipantPayoutsResultSetExtractor() ) );
    declareParameter( new SqlOutParameter( "p_out_sr_total_ref_cursor", OracleTypes.CURSOR, new SSIContestStackRankPayoutTotalsResultSetExtractor() ) );
    compile();
  }

  public SSIContestPayoutsValueBean executeProcedure( Long contestId, Long contestActivityId, String sortColumnName, String sortBy, Integer rowNumStart, Integer rowNumEnd )
      throws ServiceErrorException
  {
    contestPayoutsValueBean = new SSIContestPayoutsValueBean();
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_ssi_contest_id", contestId );
    inParams.put( "p_in_contest_activity_id", contestActivityId );
    inParams.put( "p_in_sortColName", sortColumnName );
    inParams.put( "p_in_sortedBy", sortBy );
    inParams.put( "p_in_rowNumStart", rowNumStart );
    inParams.put( "p_in_rowNumEnd", rowNumEnd );

    Map<String, Object> outParams = execute( inParams );
    Integer returnCode = (Integer)outParams.get( "p_out_return_code" );
    if ( returnCode != null && returnCode != 0 )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + returnCode );
      throw new ServiceErrorException( "Stored procedure returned error. Procedure returned: " + returnCode ); // TODO
    }
    contestPayoutsValueBean.setTotalParticipantCount( ( (BigDecimal)outParams.get( "p_out_pax_count" ) ).intValue() );
    return contestPayoutsValueBean;
  }

  protected void setParticipantDetails( ResultSet rs, SSIContestParticipantPayoutsValueBean participantPayout ) throws SQLException
  {
    participantPayout.setId( rs.getString( "USER_ID" ) );
    participantPayout.setLastName( rs.getString( "LAST_NAME" ) );
    participantPayout.setFirstName( rs.getString( "FIRST_NAME" ) );
    participantPayout.setOptOutAwards( rs.getBoolean( "IS_OPT_OUT_OF_AWARDS" ) );
  }

  protected void setParticipantBadgeDetails( ResultSet rs, SSIContestParticipantPayoutsValueBean participantPayout ) throws SQLException
  {
    participantPayout.setBadge( new SSIContestPaxPayoutBadgeValueBean( rs.getString( "BADGE_ID" ), rs.getString( "BADGE_NAME" ), rs.getString( "BADGE_IMAGE" ) ) );
  }

  @SuppressWarnings( "rawtypes" )
  private class SSIContestObjectiveParticipantPayoutsResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPayoutsValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestParticipantPayoutsValueBean> participantsPayoutList = contestPayoutsValueBean.getPartiticpantPayoutsList();
      while ( rs.next() )
      {
        SSIContestParticipantPayoutsValueBean participantPayout = new SSIContestParticipantPayoutsValueBean();
        setParticipantDetails( rs, participantPayout );
        int decimalPrecision = SSIContestUtil.getPrecision( rs.getString( "ACTIVITY_MEASURE_TYPE" ) );
        participantPayout.setGoal( SSIContestUtil.getFormattedValue( rs.getDouble( "OBJECTIVE_AMOUNT" ), decimalPrecision ) );
        participantPayout.setProgress( SSIContestUtil.getFormattedValue( rs.getDouble( "ACTIVITY_AMT" ), decimalPrecision ) );
        String totalPayout = SSIContestUtil.getFormattedValue( rs.getLong( "TOTAL_PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        // payout points
        participantPayout.setBonusPayout( SSIContestUtil.getFormattedValue( rs.getLong( "BONUS_PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        participantPayout.setObjectivePayout( SSIContestUtil.getFormattedValue( rs.getLong( "PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        participantPayout.setPayout( totalPayout );
        // payout other
        participantPayout.setPayoutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );
        participantPayout.setPayoutValue( totalPayout );
        participantPayout.setIncludeBonus( rs.getString( "INCLUDE_BONUS" ).equals( "1" ) );
        participantsPayoutList.add( participantPayout );
      }
      return contestPayoutsValueBean;
    }
  }

  @SuppressWarnings( "rawtypes" )
  private class SSIContestObjectivePayoutTotalsResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPayoutsValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      while ( rs.next() )
      {
        int decimalPrecision = SSIContestUtil.getPrecision( rs.getString( "ACTIVITY_MEASURE_TYPE" ) );
        contestPayoutsValueBean.setTotalGoal( SSIContestUtil.getFormattedValue( rs.getDouble( "OBJECTIVE_AMOUNT" ), decimalPrecision ) );
        contestPayoutsValueBean.setTotalProgress( SSIContestUtil.getFormattedValue( rs.getDouble( "ACTIVITY_AMT" ), decimalPrecision ) );
        String totalObjectivePayout = SSIContestUtil.getFormattedValue( rs.getLong( "TOTAL_OBJECTIVE_PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        String totalPayout = SSIContestUtil.getFormattedValue( rs.getLong( "TOTAL_PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        // payout points
        contestPayoutsValueBean.setTotalObjectivePayout( totalObjectivePayout );
        contestPayoutsValueBean.setTotalPayout( totalPayout );
        contestPayoutsValueBean.setTotalBonusPayout( SSIContestUtil.getFormattedValue( rs.getLong( "TOTAL_BONUS_PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        // payout other
        contestPayoutsValueBean.setTotalPayoutValue( totalPayout );
        contestPayoutsValueBean.setIncludeBonus( rs.getString( "INCLUDE_BONUS" ).equals( "1" ) );
      }
      return contestPayoutsValueBean;
    }
  }

  @SuppressWarnings( "rawtypes" )
  private class SSIContestDTGTParticipantPayoutsResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPayoutsValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestParticipantPayoutsValueBean> participantsPayoutList = contestPayoutsValueBean.getPartiticpantPayoutsList();
      while ( rs.next() )
      {
        SSIContestParticipantPayoutsValueBean participantPayout = new SSIContestParticipantPayoutsValueBean();
        setParticipantDetails( rs, participantPayout );

        int decimalPrecision = SSIContestUtil.getPrecision( rs.getString( "ACTIVITY_MEASURE_TYPE" ) );
        participantPayout.setProgress( SSIContestUtil.getFormattedValue( rs.getDouble( "ACTIVITY_AMT" ), decimalPrecision ) );
        participantPayout.setQualifiedActivity( SSIContestUtil.getFormattedValue( rs.getDouble( "QUALIFIED_ACTIVITY" ), decimalPrecision ) );
        participantPayout.setPayoutIncrements( SSIContestUtil.getFormattedValue( rs.getLong( "NUMBER_OF_INCREMENTS" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        String totalPayout = SSIContestUtil.getFormattedValue( rs.getLong( "PAYOUT_VALUE" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        // payout points
        participantPayout.setTotalPayout( totalPayout );
        // payout other
        participantPayout.setPayoutValue( totalPayout );
        participantPayout.setPayoutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );
        participantsPayoutList.add( participantPayout );
      }
      return contestPayoutsValueBean;
    }
  }

  @SuppressWarnings( "rawtypes" )
  private class SSIContestDTGTPayoutTotalsResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPayoutsValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestDoThisGetThatActivityPayoutsTotalValueBean> dtgtActivityPayoutTotalsList = contestPayoutsValueBean.getDtgtActivityPayoutTotals();
      while ( rs.next() )
      {
        SSIContestDoThisGetThatActivityPayoutsTotalValueBean dtgtActivityPayoutTotals = new SSIContestDoThisGetThatActivityPayoutsTotalValueBean();

        dtgtActivityPayoutTotals.setActivityId( rs.getString( "SSI_CONTEST_ACTIVITY_ID" ) );
        int decimalPrecision = SSIContestUtil.getPrecision( rs.getString( "ACTIVITY_MEASURE_TYPE" ) );
        dtgtActivityPayoutTotals.setActivityDescription( rs.getString( "DESCRIPTION" ) );
        dtgtActivityPayoutTotals.setForEvery( SSIContestUtil.getFormattedValue( rs.getDouble( "FOR_EVERY" ), decimalPrecision ) );
        dtgtActivityPayoutTotals.setWillEarn( SSIContestUtil.getFormattedValue( rs.getLong( "WILL_EARN" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );

        dtgtActivityPayoutTotals.setMinQualifier( SSIContestUtil.getFormattedValue( rs.getDouble( "MIN_QUALIFIER" ), decimalPrecision ) );
        dtgtActivityPayoutTotals.setTotalActivity( SSIContestUtil.getFormattedValue( rs.getDouble( "ACTIVITY_AMT" ), decimalPrecision ) );
        dtgtActivityPayoutTotals.setQualifiedActivity( SSIContestUtil.getFormattedValue( rs.getDouble( "QUALIFIED_ACTIVITY" ), decimalPrecision ) );
        dtgtActivityPayoutTotals.setTotalIncrementPayout( SSIContestUtil.getFormattedValue( rs.getLong( "NUMBER_OF_INCREMENTS" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );

        // DoThisGetThatActivity points
        dtgtActivityPayoutTotals.setTotalPayout( SSIContestUtil.getFormattedValue( rs.getLong( "PAYOUT_VALUE" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );

        // DoThisGetThatActivity other
        dtgtActivityPayoutTotals.setTotalPayoutValue( SSIContestUtil.getFormattedValue( rs.getLong( "PAYOUT_VALUE" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        dtgtActivityPayoutTotals.setPayoutOtherCurrency( rs.getString( "PAYOUT_OTHER_CUR_CODE" ) );
        dtgtActivityPayoutTotals.setPayoutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );

        dtgtActivityPayoutTotalsList.add( dtgtActivityPayoutTotals );
      }
      return contestPayoutsValueBean;
    }
  }

  @SuppressWarnings( "rawtypes" )
  private class SSIContestStepItUpParticipantPayoutsResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPayoutsValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestParticipantPayoutsValueBean> participantsPayoutList = contestPayoutsValueBean.getPartiticpantPayoutsList();
      while ( rs.next() )
      {
        SSIContestParticipantPayoutsValueBean participantPayout = new SSIContestParticipantPayoutsValueBean();
        setParticipantDetails( rs, participantPayout );
        int decimalPrecision = SSIContestUtil.getPrecision( rs.getString( "ACTIVITY_MEASURE_TYPE" ) );
        participantPayout.setActivityAmount( SSIContestUtil.getFormattedValue( rs.getDouble( "ACTIVITY_AMT" ), decimalPrecision ) );
        participantPayout.setLevelAchieved( "" + rs.getLong( "LEVEL_COMPLETED" ) );
        String levelPayout = SSIContestUtil.getFormattedValue( rs.getLong( "PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        String totalPayout = SSIContestUtil.getFormattedValue( rs.getLong( "TOTAL_PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        // payout points
        participantPayout.setBonusPayout( SSIContestUtil.getFormattedValue( rs.getLong( "BONUS_PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        participantPayout.setLevelPayout( levelPayout );
        participantPayout.setTotalPayout( totalPayout );
        // payout other
        participantPayout.setPayoutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );
        participantPayout.setQty( levelPayout );
        participantPayout.setPayoutValue( totalPayout );
        participantPayout.setIncludeBonus( rs.getString( "INCLUDE_BONUS" ).equals( "1" ) );
        participantsPayoutList.add( participantPayout );
      }
      return contestPayoutsValueBean;
    }
  }

  @SuppressWarnings( "rawtypes" )
  private class SSIContestStepItUpPayoutTotalsResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPayoutsValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      while ( rs.next() )
      {
        int decimalPrecision = SSIContestUtil.getPrecision( rs.getString( "ACTIVITY_MEASURE_TYPE" ) );
        contestPayoutsValueBean.setTotalActivity( SSIContestUtil.getFormattedValue( rs.getDouble( "ACTIVITY_AMT" ), decimalPrecision ) );
        String totalLevelPayout = SSIContestUtil.getFormattedValue( rs.getLong( "LEVEL_PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        String totalPayout = SSIContestUtil.getFormattedValue( rs.getLong( "TOTAL_PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        // payout points
        contestPayoutsValueBean.setTotalLevelPayout( totalLevelPayout );
        contestPayoutsValueBean.setTotalPayout( totalPayout );
        contestPayoutsValueBean.setTotalBonusPayout( SSIContestUtil.getFormattedValue( rs.getLong( "BONUS_PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        // payout other
        contestPayoutsValueBean.setTotalPayoutValue( totalPayout );
        contestPayoutsValueBean.setIncludeBonus( rs.getString( "INCLUDE_BONUS" ).equals( "1" ) );
      }
      return contestPayoutsValueBean;
    }
  }

  @SuppressWarnings( "rawtypes" )
  private class SSIContestStackRankParticipantPayoutsResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPayoutsValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<SSIContestParticipantPayoutsValueBean> participantsPayoutList = contestPayoutsValueBean.getPartiticpantPayoutsList();
      while ( rs.next() )
      {
        SSIContestParticipantPayoutsValueBean participantPayout = new SSIContestParticipantPayoutsValueBean();
        setParticipantDetails( rs, participantPayout );
        setParticipantBadgeDetails( rs, participantPayout );
        int decimalPrecision = SSIContestUtil.getPrecision( rs.getString( "ACTIVITY_MEASURE_TYPE" ) );
        participantPayout.setProgress( SSIContestUtil.getFormattedValue( rs.getDouble( "ACTIVITY_AMT" ), decimalPrecision ) );
        participantPayout.setRank( SSIContestUtil.getFormattedValue( rs.getLong( "STACK_RANK" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        String totalPayout = SSIContestUtil.getFormattedValue( rs.getLong( "TOTAL_PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        // payout points
        participantPayout.setPayout( totalPayout );
        // payout other
        participantPayout.setPayoutValue( totalPayout );
        participantPayout.setPayoutDescription( rs.getString( "PAYOUT_DESCRIPTION" ) );
        participantsPayoutList.add( participantPayout );
      }
      return contestPayoutsValueBean;
    }
  }

  @SuppressWarnings( "rawtypes" )
  private class SSIContestStackRankPayoutTotalsResultSetExtractor implements ResultSetExtractor
  {
    public SSIContestPayoutsValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      while ( rs.next() )
      {
        int precision = SSIContestUtil.getPrecision( rs.getString( "ACTIVITY_MEASURE_TYPE" ) );
        contestPayoutsValueBean.setTotalActivity( SSIContestUtil.getFormattedValue( rs.getDouble( "ACTIVITY_AMT" ), precision ) );
        String totalPayout = SSIContestUtil.getFormattedValue( rs.getLong( "TOTAL_PAYOUT" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        // payout points
        contestPayoutsValueBean.setTotalPayout( totalPayout );
        // payout other
        contestPayoutsValueBean.setTotalPayoutValue( totalPayout );
        // stack rank tied
        contestPayoutsValueBean.setHasTie( rs.getString( "HAS_TIE" ).equals( "1" ) );
        contestPayoutsValueBean.setPayoutCap( SSIContestUtil.getFormattedValue( rs.getLong( "PAYOUT_CAP" ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
      }
      return contestPayoutsValueBean;
    }
  }

}
