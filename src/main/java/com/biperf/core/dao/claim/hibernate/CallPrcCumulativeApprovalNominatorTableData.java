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

import com.biperf.core.value.NominationsApprovalCustomValueBean;
import com.biperf.core.value.nomination.CumulativeApprovalNominatorInfoValueBean;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcCumulativeApprovalNominatorTableData extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_LIST_PEND_NOMINATIONS.PRC_LIST_APPR_CUMMULA_DTL_PAGE";

  public CallPrcCumulativeApprovalNominatorTableData( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_promotion_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_nominee_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_claim_group_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_level_number", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_approver_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_time_period_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_submit_start_date", Types.DATE ) );
    declareParameter( new SqlParameter( "p_in_submit_end_date", Types.DATE ) );
    declareParameter( new SqlParameter( "p_in_status", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_nominator_detail", OracleTypes.CURSOR, new CallPrcCumulativeApprovalNominatorTableData.CumulativeNominatorInfoMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_custom_dtl", OracleTypes.CURSOR, new CallPrcCumulativeApprovalNominatorTableData.CumulativeNominatorCustomElementsInfoMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> parameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_promotion_id", parameters.get( "promotionId" ) );
    inParams.put( "p_in_nominee_id", parameters.get( "paxId" ) );
    inParams.put( "p_in_claim_group_id", parameters.get( "claimGroupId" ) );
    inParams.put( "p_in_level_number", parameters.get( "levelNumber" ) );
    inParams.put( "p_in_approver_id", parameters.get( "approverUserId" ) );
    inParams.put( "p_in_time_period_id", parameters.get( "timePeriodId" ) );
    inParams.put( "p_in_submit_start_date", parameters.get( "startDate" ) );
    inParams.put( "p_in_submit_end_date", parameters.get( "endDate" ) );
    inParams.put( "p_in_status", parameters.get( "status" ) );
    inParams.put( "p_in_locale", parameters.get( "locale" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class CumulativeNominatorInfoMapper implements ResultSetExtractor
  {
    @Override
    public List<CumulativeApprovalNominatorInfoValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<CumulativeApprovalNominatorInfoValueBean> valueBeanData = new ArrayList<CumulativeApprovalNominatorInfoValueBean>();
      while ( rs.next() )
      {
        CumulativeApprovalNominatorInfoValueBean valueBean = new CumulativeApprovalNominatorInfoValueBean();

        valueBean.setClaimId( rs.getLong( "claim_id" ) );
        valueBean.setSubmittedDate( rs.getDate( "submitted_date" ) );
        valueBean.setNominatorPaxId( rs.getLong( "nominator_pax_id" ) );
        valueBean.setNominatorName( rs.getString( "nominator_name" ) );
        valueBean.setReason( rs.getString( "submitter_comments" ) );
        valueBean.setMoreInfo( rs.getString( "more_info_comments" ) );
        valueBean.setWhyAttachmentName( rs.getString( "why_attachment_name" ) );
        valueBean.setWhyAttachmentUrl( rs.getString( "why_attachment_url" ) );
        valueBean.setCertificateId( rs.getLong( "certificate_id" ) );

        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

  private class CumulativeNominatorCustomElementsInfoMapper implements ResultSetExtractor
  {
    @Override
    public List<NominationsApprovalCustomValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsApprovalCustomValueBean> valueBeanData = new ArrayList<NominationsApprovalCustomValueBean>();
      while ( rs.next() )
      {
        NominationsApprovalCustomValueBean valueBean = new NominationsApprovalCustomValueBean();

        valueBean.setClaimId( rs.getLong( "claim_id" ) );
        // valueBean.setNomineePaxId( rs.getLong( "nominee_pax_id" ) );
        valueBean.setClaimFormStepElementId( rs.getLong( "claim_form_step_element_id" ) );
        valueBean.setClaimFormStepElementName( rs.getString( "claim_form_step_element_name" ) );
        valueBean.setDescription( rs.getString( "claim_form_step_element_desc" ) );
        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

}
