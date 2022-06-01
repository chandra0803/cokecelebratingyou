/**
 * 
 */

package com.biperf.core.dao.claim.hibernate;

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

import com.biperf.core.value.NominationsApprovalPageDetailsValueBean;
import com.biperf.core.value.NominationsApprovalPagePromotionLevelsValueBean;
import com.biperf.core.value.NominationsApprovalPageTimePeriodsValueBean;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcNominationApprovalsPageData extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_LIST_PEND_NOMINATIONS.PRC_LIST_PEND_NOMINATIONS_LAND";

  public CallPrcNominationApprovalsPageData( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_promotion_id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_level_number", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_approver_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_promo_levels", OracleTypes.CURSOR, new CallPrcNominationApprovalsPageData.NominationApprovalsPageDataMapper1() ) );
    declareParameter( new SqlOutParameter( "p_out_time_periods", OracleTypes.CURSOR, new CallPrcNominationApprovalsPageData.NominationApprovalsPageDataMapper2() ) );
    declareParameter( new SqlOutParameter( "p_out_dropdown_details", OracleTypes.CURSOR, new CallPrcNominationApprovalsPageData.NominationApprovalsPageDataMapper3() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> parameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_promotion_id", parameters.get( "promotionId" ) );
    inParams.put( "p_in_level_number", parameters.get( "levelNumber" ) );
    inParams.put( "p_in_approver_id", parameters.get( "approverUserId" ) );
    inParams.put( "p_in_locale", parameters.get( "userLocale" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class NominationApprovalsPageDataMapper1 implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalPagePromotionLevelsValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalPagePromotionLevelsValueBean> valueBeanData = new ArrayList<NominationsApprovalPagePromotionLevelsValueBean>();

      while ( rs.next() )
      {
        NominationsApprovalPagePromotionLevelsValueBean valueBean = new NominationsApprovalPagePromotionLevelsValueBean();

        valueBean.setLevelIndex( rs.getLong( "level_number" ) );
        valueBean.setLevelLabel( rs.getString( "level_name" ) );

        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

  private class NominationApprovalsPageDataMapper2 implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalPageTimePeriodsValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalPageTimePeriodsValueBean> valueBeanData = new ArrayList<NominationsApprovalPageTimePeriodsValueBean>();

      while ( rs.next() )
      {
        NominationsApprovalPageTimePeriodsValueBean valueBean = new NominationsApprovalPageTimePeriodsValueBean();

        valueBean.setTimePeriodId( rs.getLong( "nomination_time_period_id" ) );
        valueBean.setTimePeriodName( rs.getString( "time_period_name" ) );

        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

  private class NominationApprovalsPageDataMapper3 implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalPageDetailsValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalPageDetailsValueBean> valueBeanData = new ArrayList<NominationsApprovalPageDetailsValueBean>();

      while ( rs.next() )
      {
        NominationsApprovalPageDetailsValueBean valueBean = new NominationsApprovalPageDetailsValueBean();

        valueBean.setPromotionName( rs.getString( "promotion_name" ) );
        valueBean.setFinalLevelApprover( rs.getBoolean( "final_level_approver" ) );
        valueBean.setTotalPromotionCount( rs.getInt( "total_promotion_count" ) );
        valueBean.setTimePeriodEnabled( rs.getBoolean( "time_period_enabled" ) );
        valueBean.setPayoutAtEachLevel( rs.getBoolean( "payout_each_level" ) );
        valueBean.setCurrencyLabel( rs.getString( "currency_label" ) );
        valueBean.setPayoutType( rs.getString( "payout_type" ) );
        valueBean.setAwardType( rs.getString( "award_type" ) );
        valueBean.setRulesText( rs.getString( "rules_text" ) );

        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

}
