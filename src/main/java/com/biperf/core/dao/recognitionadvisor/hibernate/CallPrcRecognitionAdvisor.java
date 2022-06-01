
package com.biperf.core.dao.recognitionadvisor.hibernate;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorValueBean;

import oracle.jdbc.OracleTypes;

/**
 * Used for the RecognitonAdvisor Algorithm screen(s).
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Ramesh J</td>
 * <td>Dec 11, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */

public class CallPrcRecognitionAdvisor extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PRC_RA_REMINDER";
  private static final String P_IN_USER_ID = "p_in_user_id";
  private static final String P_IN_ROWNUM_START = "p_in_rownum_start";
  private static final String P_IN_ROWNUM_END = "p_in_rownum_end";
  private static final String P_IN_SORTCOLNAME = "p_in_sortColName";
  private static final String P_IN_SORTEDBY = "p_in_sortedBy";
  private static final String P_IN_EXCLUDE_UPCOMING = "p_in_exclude_upcoming";
  private static final String P_OUT_RETURN_CODE = "p_out_retun_code";
  private static final String P_OUT_DATA = "p_out_user_data";
  private static final String P_IN_EMP_FILTER = "p_in_emp_filter";
  private static final String P_IN_NO_PEND = "p_in_no_pend";
  public static final String RA_DATE_FORMAT = "DD/mm/yyyy";

  public CallPrcRecognitionAdvisor( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( P_IN_USER_ID, Types.NUMERIC ) );
    declareParameter( new SqlParameter( P_IN_EXCLUDE_UPCOMING, Types.NUMERIC ) );
    declareParameter( new SqlParameter( P_IN_NO_PEND, Types.NUMERIC ) );
    declareParameter( new SqlParameter( P_IN_EMP_FILTER, Types.VARCHAR ) );
    declareParameter( new SqlParameter( P_IN_ROWNUM_START, Types.NUMERIC ) );
    declareParameter( new SqlParameter( P_IN_ROWNUM_END, Types.NUMERIC ) );
    declareParameter( new SqlParameter( P_IN_SORTCOLNAME, Types.VARCHAR ) );
    declareParameter( new SqlParameter( P_IN_SORTEDBY, Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( P_OUT_RETURN_CODE, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_DATA, OracleTypes.CURSOR, new RecognitionAdvisorExtractor() ) );
    compile();
  }

  @SuppressWarnings( { "rawtypes" } )
  public Map executeProcedure( Long userId, Long rowStart, Long rowEnd, String sortColumnName, String sortBy, Long excludeUpcoming, String filterValue, Long pendingStatus )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( P_IN_USER_ID, userId );
    inParams.put( P_IN_EXCLUDE_UPCOMING, excludeUpcoming );
    inParams.put( P_IN_NO_PEND, pendingStatus );
    inParams.put( P_IN_EMP_FILTER, filterValue );
    inParams.put( P_IN_ROWNUM_START, rowStart );
    inParams.put( P_IN_ROWNUM_END, rowEnd );
    inParams.put( P_IN_SORTCOLNAME, sortColumnName );
    inParams.put( P_IN_SORTEDBY, sortBy );
    Map outParams = execute( inParams );

    return outParams;
  }

  private class RecognitionAdvisorExtractor implements ResultSetExtractor<List<RecognitionAdvisorValueBean>>
  {
    @Override
    public List<RecognitionAdvisorValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionAdvisorValueBean> recognitionAdvisoryReminderList = new ArrayList<RecognitionAdvisorValueBean>();
      while ( rs.next() )
      {
        if ( rs.getLong( "TOTAL_RECORDS" ) > 0L )
        {
          RecognitionAdvisorValueBean raValueBean = new RecognitionAdvisorValueBean();
          raValueBean.setFirstName( rs.getString( "FIRST_NAME" ) );
          raValueBean.setLastName( rs.getString( "LAST_NAME" ) );
          raValueBean.setId( rs.getLong( "USER_ID" ) );
          raValueBean.setAvatarUrl( rs.getString( "AVATAR_SMALL" ) );
          raValueBean.setParticipantGroupType( rs.getInt( "IS_NEW_HIRE" ) );
          if ( !Objects.isNull( rs.getString( "POSITION_TYPE" ) ) )
          {
            raValueBean.setPositionType( rs.getString( "POSITION_TYPE" ) );
          }
          if ( !Objects.isNull( rs.getDate( "BY_ME_DATE_SENT" ) ) )
          {

            raValueBean.setCurrentMgrApprovedDate( convertDateString( rs.getDate( "BY_ME_DATE_SENT" ) ) );
          }
          if ( !Objects.isNull( rs.getDate( "BY_OTH_DATE_SENT" ) ) )
          {
            raValueBean.setOtherMgrApprovedDate( convertDateString( rs.getDate( "BY_OTH_DATE_SENT" ) ) );
          }

          if ( rs.getInt( "BY_ME_QUANTITY" ) > 0 )
          {
            raValueBean.setCurrentMgrAwardPoints( rs.getInt( "BY_ME_QUANTITY" ) );
          }

          if ( rs.getInt( "BY_OTH_QUANTITY" ) > 0 )
          {
            raValueBean.setOtherMgrAwardPoints( rs.getInt( "BY_OTH_QUANTITY" ) );
          }

          raValueBean.setCurrentMgrLastRecogDays( rs.getObject( "BY_ME_SINCE_LAST_RECOG" ) != null ? rs.getInt( "BY_ME_SINCE_LAST_RECOG" ) : null );
          raValueBean.setOtherMgrLastRecogDays( rs.getObject( "BY_OTH_SINCE_LAST_RECOG" ) != null ? rs.getInt( "BY_OTH_SINCE_LAST_RECOG" ) : null );

          raValueBean.setClaimIdByCurrentMgr( rs.getString( "BY_ME_CLAIM_ID" ) );
          raValueBean.setClaimIdByOtherMgr( rs.getString( "BY_OTH_CLAIM_ID" ) );
          raValueBean.setRaTotalParticipants( rs.getLong( "TOTAL_RECORDS" ) );
          raValueBean.setParticipantRecDaysColour( rs.getString( "COLOUR_GRADE" ) );
          raValueBean.setParticipantRecDaysColourForOthers( rs.getString( "COLOUR_GRADE_OTHER" ) );
          raValueBean.setCountryId( rs.getInt( "COUNTRY_ID" ) );
          raValueBean.setRecApprovalStatusType( rs.getString( "approval_status_type" ) );

          String[] nodeIdValue = null;
          String nodeId = rs.getString( "NODE_ID" );
          if ( Objects.nonNull( nodeId ) )
          {
            nodeIdValue = nodeId.split( "," );
          }

          raValueBean.setNodeId( nodeIdValue );
          raValueBean.setCountryName( rs.getString( "COUNTRY_NAME" ) );
          raValueBean.setCountryCode( rs.getString( "COUNTRY_CODE" ) );

          raValueBean.setNewHireAvailable( rs.getString( "IS_NEWHIRE" ) );
          raValueBean.setOverDueAvailable( rs.getString( "IS_OVERDUE" ) );
          raValueBean.setUpComingAvailable( rs.getString( "IS_UPCOMING" ) );

          raValueBean.setNewHireTotalcount( rs.getInt( "NEWHIRE_COUNT" ) );
          raValueBean.setOverDueTotalcount( rs.getInt( "OVERDUE_COUNT" ) );

          recognitionAdvisoryReminderList.add( raValueBean );

        }

      }
      return recognitionAdvisoryReminderList;
    }
  }

  private static String convertDateString( Date RAdateFormate )
  {
    Locale locale = UserManager.getLocale();
    SimpleDateFormat sdf = new SimpleDateFormat( RA_DATE_FORMAT );
    DateFormat displayFormat = DateUtils.displayStringDateFormat( locale );
    return DateUtils.toConvertDateFormatString( sdf, displayFormat, sdf.format( RAdateFormate ) );

  }

}
