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

import com.biperf.core.value.NominationsApprovalValueBean;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcNominationClaimsForApproval extends StoredProcedure
{
  /* Tables */
  private static final String STORED_PROC_NAME = "PKG_LIST_PEND_NOMINATIONS.PRC_LIST_PEND_NOMINATIONS_TILE";

  public CallPrcNominationClaimsForApproval( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_user_id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedOn", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_pend_claim_res", OracleTypes.CURSOR, new CallPrcNominationClaimsForApproval.PendingNominationClaimsMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> parameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_id", parameters.get( "userId" ) );
    inParams.put( "p_in_rowNumStart", parameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", parameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_sortedBy", parameters.get( "sortedBy" ) );
    inParams.put( "p_in_sortedOn", parameters.get( "sortedOn" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  /**
   * PendingNominationClaimsMapper is an Inner class which implements the RowMapper.
   */
  private class PendingNominationClaimsMapper implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalValueBean> valueBeanData = new ArrayList<NominationsApprovalValueBean>();
      int i = 0;
      while ( rs.next() )
      {
        NominationsApprovalValueBean valueBean = new NominationsApprovalValueBean();

        valueBean.setPromotionId( rs.getLong( "promotion_Id" ) );
        valueBean.setClaimId( rs.getLong( "claim_id" ) );
        valueBean.setPromotionName( rs.getString( "promotion_name" ) );
        valueBean.setDateSubmitted( rs.getDate( "date_submitted" ) );
        valueBean.setApprovalLevel( rs.getInt( "approval_round" ) );
        if ( i == 0 )
        {
          valueBean.setTotalRecords( rs.getInt( "total_records" ) );
          valueBean.setTotalPromoRecords( rs.getInt( "total_promo_records" ) );
        }
        valueBean.setLevelLabel( rs.getString( "level_label" ) );
        valueBean.setPayoutLevelType( rs.getString( "payout_level_type" ) );
        valueBean.setSubmissionStartDate( rs.getDate( "promotion_start_date" ) );
        valueBean.setSubmissionEndDate( rs.getDate( "promotion_end_date" ) );
        valueBean.setMultipleLevel( rs.getBoolean( "is_multiple_level" ) );
        valueBean.setFinalLevel( rs.getBoolean( "is_final_level" ) );
        valueBean.setStatus( rs.getString( "status" ) );
        valueBeanData.add( valueBean );
        i++;
      }

      return valueBeanData;
    }
  }
}
