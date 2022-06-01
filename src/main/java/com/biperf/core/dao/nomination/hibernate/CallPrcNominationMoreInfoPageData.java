
package com.biperf.core.dao.nomination.hibernate;

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

import com.biperf.core.value.nomination.NominationMoreInfoPageBehaviorValueBean;
import com.biperf.core.value.nomination.NominationMoreInfoPageCustomFieldValueBean;
import com.biperf.core.value.nomination.NominationMoreInfoPageTeamValueBean;
import com.biperf.core.value.nomination.NominationMoreInfoValueBean;

import oracle.jdbc.OracleTypes;

/**
 * Procedure to obtain the information for display on the nomination more info page
 * (page that allows a nominator to go back and give an approver more information)
 * 
 * @author corneliu
 */
public class CallPrcNominationMoreInfoPageData extends StoredProcedure
{
  // TODO
  private static final String STORED_PROC_NAME = "PRC_NOM_MORE_INFO_PAGE";

  public CallPrcNominationMoreInfoPageData( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );

    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    // TODO
    declareParameter( new SqlParameter( "p_in_claim_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcNominationMoreInfoPageData.NominationMoreInfoDataMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_custom_data", OracleTypes.CURSOR, new CallPrcNominationMoreInfoPageData.NominationMoreInfoCustomFieldMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_behavior_data", OracleTypes.CURSOR, new CallPrcNominationMoreInfoPageData.NominationMoreInfoBehaviorMapper() ) );
    declareParameter( new SqlOutParameter( "p_out_team_data", OracleTypes.CURSOR, new CallPrcNominationMoreInfoPageData.NominationMoreInfoTeamMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Long claimId, Long submitterId, String locale )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_claim_id", claimId );
    inParams.put( "p_in_user_id", submitterId );
    inParams.put( "p_in_locale", locale );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class NominationMoreInfoDataMapper implements ResultSetExtractor<NominationMoreInfoValueBean>
  {
    @Override
    public NominationMoreInfoValueBean extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      NominationMoreInfoValueBean moreInfoPageBean = new NominationMoreInfoValueBean();

      while ( rs.next() )
      {
        moreInfoPageBean.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        moreInfoPageBean.setApproverComments( rs.getString( "APPROVER_COMMENTS" ) );
        moreInfoPageBean.setDateSubmitted( rs.getDate( "SUBMISSION_DATE" ) );
        moreInfoPageBean.setParticipantId( rs.getLong( "PARTICIPANT_ID" ) );
        moreInfoPageBean.setNominatorFirstName( rs.getString( "FIRST_NAME" ) );
        moreInfoPageBean.setNominatorLastName( rs.getString( "LAST_NAME" ) );
        moreInfoPageBean.setNominatorPosition( rs.getString( "POSITION_TYPE" ) );
        moreInfoPageBean.setNominatorCountryName( rs.getString( "COUNTRY_ID" ) );
        moreInfoPageBean.setNominatorCountryCode( rs.getString( "COUNTRY_CODE" ) );
        moreInfoPageBean.setCmAssetCode( rs.getString( "CM_ASSET_CODE" ) );
        moreInfoPageBean.setNominatorOrgId( rs.getLong( "NODE_ID" ) );
        moreInfoPageBean.setNominatorOrgName( rs.getString( "NODE_NAME" ) );
        moreInfoPageBean.setCertificateId( rs.getLong( "CERTIFICATE_ID" ) );
        moreInfoPageBean.setVideoImage( rs.getString( "CARD_VIDEO_IMAGE_URL" ) );
        moreInfoPageBean.setVideoUrl( rs.getString( "CARD_VIDEO_URL" ) );
        moreInfoPageBean.setCardName( rs.getString( "CARD_NAME" ) );
        moreInfoPageBean.setCardImage( rs.getString( "LARGE_IMAGE_NAME" ) );
        moreInfoPageBean.setSubmissionWhyText( rs.getString( "WHY_ATTACHMENT_NAME" ) );
        moreInfoPageBean.setNominationLink( rs.getString( "WHY_ATTACHMENT_URL" ) );
        moreInfoPageBean.setSubmitterLangId( rs.getString( "SUBMITTER_COMMENTS_LANG_ID" ) );
        moreInfoPageBean.setTeamId( rs.getLong( "TEAM_ID" ) );
        moreInfoPageBean.setTeamName( rs.getString( "TEAM_NAME" ) );
        moreInfoPageBean.setReason( rs.getString( "REASON" ) );
        moreInfoPageBean.setDepartmentName( rs.getString( "DEPARTMENT_TYPE" ) );
        moreInfoPageBean.setJobName( rs.getString( "POSITION_TYPE" ) );
        moreInfoPageBean.setOwnCardName( rs.getString( "OWN_CARD_NAME" ) );
      }

      return moreInfoPageBean;
    }
  }

  private class NominationMoreInfoBehaviorMapper implements ResultSetExtractor<List<NominationMoreInfoPageBehaviorValueBean>>
  {
    @Override
    public List<NominationMoreInfoPageBehaviorValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationMoreInfoPageBehaviorValueBean> behaviors = new ArrayList<>();

      while ( rs.next() )
      {
        NominationMoreInfoPageBehaviorValueBean behavior = new NominationMoreInfoPageBehaviorValueBean();
        behavior.setBehaviorId( rs.getLong( "BEHAVIOR_ID" ) );
        behavior.setBehaviorName( rs.getString( "BEHAVIOR_NAME" ) );
        behavior.setBadgeName( rs.getString( "BADGE_NAME" ) );

        behaviors.add( behavior );
      }

      return behaviors;
    }
  }

  private class NominationMoreInfoTeamMapper implements ResultSetExtractor<List<NominationMoreInfoPageTeamValueBean>>
  {
    @Override
    public List<NominationMoreInfoPageTeamValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationMoreInfoPageTeamValueBean> teams = new ArrayList<>();

      while ( rs.next() )
      {
        NominationMoreInfoPageTeamValueBean team = new NominationMoreInfoPageTeamValueBean();
        team.setUserId( rs.getLong( "USER_ID" ) );
        team.setFirstName( rs.getString( "FIRST_NAME" ) );
        team.setLastName( rs.getString( "LAST_NAME" ) );

        teams.add( team );
      }

      return teams;
    }
  }

  private class NominationMoreInfoCustomFieldMapper implements ResultSetExtractor<List<NominationMoreInfoPageCustomFieldValueBean>>
  {
    @Override
    public List<NominationMoreInfoPageCustomFieldValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationMoreInfoPageCustomFieldValueBean> customFields = new ArrayList<>();

      while ( rs.next() )
      {
        NominationMoreInfoPageCustomFieldValueBean customField = new NominationMoreInfoPageCustomFieldValueBean();
        customField.setClaimStepElmtId( rs.getLong( "CLAIM_FORM_STEP_ELEMENT_ID" ) );
        customField.setName( rs.getString( "NAME" ) );
        customField.setDescription( rs.getString( "DESCRIPTION" ) );
        customField.setSequenceNum( rs.getLong( "SEQUENCE_NUM" ) );
        customField.setWhyField( rs.getBoolean( "WHY_FIELD" ) );

        customFields.add( customField );
      }

      return customFields;
    }
  }

}
