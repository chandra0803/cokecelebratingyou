
package com.biperf.core.dao.recognitionadvisor.hibernate;

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

import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorValueBean;

import oracle.jdbc.OracleTypes;

/**
 * Used for the RecognitonAdvisor Email TeamMember screen(s).
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
 * <td>Dec 26, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CallPrcRecognitionAdvisorTeamMemberEmail extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "PRC_RA_OD_REMINDER";
  public static final String P_OUT_DATA = "p_out_user_data";
  public static final String P_OUT_RETURN_CODE = "p_out_retun_code";

  public CallPrcRecognitionAdvisorTeamMemberEmail( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_RETURN_CODE, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_DATA, OracleTypes.CURSOR, new RecognitionAdvisorTeamMemberEmailExtractor() ) );
    compile();
  }

  @SuppressWarnings( { "rawtypes" } )
  public Map executeProcedure( Long userId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_id", userId );
    Map outParams = execute( inParams );

    return outParams;
  }

  private class RecognitionAdvisorTeamMemberEmailExtractor implements ResultSetExtractor<Object>
  {
    @Override
    public List<RecognitionAdvisorValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionAdvisorValueBean> recognitionAdvisoryReminderList = new ArrayList<RecognitionAdvisorValueBean>();
      while ( rs.next() )
      {
        RecognitionAdvisorValueBean raTeamMemberEmailValueBean = new RecognitionAdvisorValueBean();
        raTeamMemberEmailValueBean.setId( rs.getLong( "USER_ID" ) );
        raTeamMemberEmailValueBean.setFirstName( rs.getString( "FIRST_NAME" ) );
        raTeamMemberEmailValueBean.setLastName( rs.getString( "LAST_NAME" ) );
        raTeamMemberEmailValueBean.setNoOfDaysPastDueDate( rs.getLong( "SINCE_LAST_RECOG" ) );
        raTeamMemberEmailValueBean.setAvatarUrl( rs.getString( "AVATAR_SMALL" ) );
        raTeamMemberEmailValueBean.setPositionType( rs.getString( "POSITION_TYPE" ) );

        recognitionAdvisoryReminderList.add( raTeamMemberEmailValueBean );
      }
      return recognitionAdvisoryReminderList;
    }
  }

}
