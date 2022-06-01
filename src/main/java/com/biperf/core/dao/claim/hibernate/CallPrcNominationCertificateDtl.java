
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

import com.biperf.core.value.nomination.NominationCertificateCustomValueBean;
import com.biperf.core.value.nomination.NominationCertificateTeamValueBean;
import com.biperf.core.value.nomination.NominationCertificateValueBean;

import oracle.jdbc.OracleTypes;

public class CallPrcNominationCertificateDtl extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PRC_NOMINATION_CERTIFICATE_DTL";

  public CallPrcNominationCertificateDtl( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );

    declareParameter( new SqlParameter( "p_in_claim_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_nom_certificate_dtl", OracleTypes.CURSOR, new CallPrcNominationCertificateDtl.NominationCertificateDtlMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_team_dtl", OracleTypes.CURSOR, new CallPrcNominationCertificateDtl.NominationCertificateTeamMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_custom_data", OracleTypes.CURSOR, new CallPrcNominationCertificateDtl.NominationCertificateCustomDataMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Long claimId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_claim_id", claimId );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class NominationCertificateDtlMapper implements ResultSetExtractor<NominationCertificateValueBean>
  {
    @Override
    public NominationCertificateValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      NominationCertificateValueBean certificateValueBean = new NominationCertificateValueBean();

      while ( rs.next() )
      {
        certificateValueBean.setCertType( rs.getString( "CLAIM_TYPE" ) );
        certificateValueBean.setNomineeFirstName( rs.getString( "NOMINEE_FIRST_NAME" ) );
        certificateValueBean.setNomineeLastName( rs.getString( "NOMINEE_LAST_NAME" ) );
        certificateValueBean.setNominatorFirstName( rs.getString( "NOMINATOR_FIRST_NAME" ) );
        certificateValueBean.setNominatorLastName( rs.getString( "NOMINATOR_LAST_NAME" ) );
        certificateValueBean.setSubmissionDate( rs.getDate( "SUBMISSION_DATE" ) );
        certificateValueBean.setLevelIndex( rs.getInt( "LEVEL_INDEX" ) );
        certificateValueBean.setLevelName( rs.getString( "LEVEL_NAME" ) );
        certificateValueBean.setTeamName( rs.getString( "TEAM_NAME" ) );
        certificateValueBean.setTimePeriodName( rs.getString( "TIME_PERIOD_NAME" ) );
        certificateValueBean.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        certificateValueBean.setSubmitterComments( rs.getString( "SUBMITTER_COMMENTS" ) );
        certificateValueBean.setSubmitterLangId( rs.getString( "SUBMITTER_COMMENTS_LANG_ID" ) );
        certificateValueBean.setCertificateId( rs.getLong( "CERTIFICATE_ID" ) == 0 ? null : rs.getLong( "CERTIFICATE_ID" ) );
      }
      return certificateValueBean;
    }
  }

  private class NominationCertificateTeamMapper implements ResultSetExtractor<List<NominationCertificateTeamValueBean>>
  {
    @Override
    public List<NominationCertificateTeamValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationCertificateTeamValueBean> nomineeValueBeanList = new ArrayList<>();
      while ( rs.next() )
      {
        NominationCertificateTeamValueBean nomineeValueBean = new NominationCertificateTeamValueBean();
        nomineeValueBean.setClaimId( rs.getLong( "CLAIM_ID" ) );
        nomineeValueBean.setNomineeFirstName( rs.getString( "NOMINEE_FIRST_NAME" ) );
        nomineeValueBean.setNomineeLastName( rs.getString( "NOMINEE_LAST_NAME" ) );
        nomineeValueBeanList.add( nomineeValueBean );
      }
      return nomineeValueBeanList;
    }
  }

  private class NominationCertificateCustomDataMapper implements ResultSetExtractor<List<NominationCertificateCustomValueBean>>
  {
    @Override
    public List<NominationCertificateCustomValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationCertificateCustomValueBean> customFields = new ArrayList<>();
      while ( rs.next() )
      {
        NominationCertificateCustomValueBean customField = new NominationCertificateCustomValueBean();
        customField.setClaimStepElmtId( rs.getLong( "CLAIM_FORM_STEP_ELEMENT_ID" ) );
        customField.setName( rs.getString( "NAME" ) );
        customField.setClaimFormValue( rs.getString( "CLAIM_FORM_VALUE" ) );
        customField.setSeqNumber( rs.getLong( "SEQUENCE_NUM" ) );
        customField.setWhyField( rs.getBoolean( "WHY_FIELD" ) );
        customFields.add( customField );
      }
      return customFields;
    }
  }

}
