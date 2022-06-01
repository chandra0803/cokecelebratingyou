
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

import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorUnusedBudgetBean;

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
public class CallPrcRecognitionAdvisorUnusedBudgetEmail extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "PRC_RA_UNUSED_BUD_DETAILS";
  public static final String P_OUT_DATA = "p_out_user_data";
  public static final String P_OUT_RETURN_CODE = "p_out_retun_code";

  public CallPrcRecognitionAdvisorUnusedBudgetEmail( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_RETURN_CODE, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_DATA, OracleTypes.CURSOR, new RecognitionAdvisorUnusedBudgetEmailExtractor() ) );
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

  private class RecognitionAdvisorUnusedBudgetEmailExtractor implements ResultSetExtractor<Object>
  {
    @Override
    public List<RecognitionAdvisorUnusedBudgetBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionAdvisorUnusedBudgetBean> raUnusedBudgetReminderList = new ArrayList<RecognitionAdvisorUnusedBudgetBean>();
      while ( rs.next() )
      {
        RecognitionAdvisorUnusedBudgetBean raUnusedBudgetEmailValueBean = new RecognitionAdvisorUnusedBudgetBean();
        raUnusedBudgetEmailValueBean.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        raUnusedBudgetEmailValueBean.setLastRecDate( rs.getDate( "LAST_REC_DATE" ) );
        Object daysSinceLastRecObj = rs.getObject( "DAYS_SINCE_LAST_REC" );
        Long daysSinceLastRec = ( null != daysSinceLastRecObj ) ? rs.getLong( "DAYS_SINCE_LAST_REC" ) : null;
        raUnusedBudgetEmailValueBean.setDaysSinceLastRec( daysSinceLastRec );
        raUnusedBudgetEmailValueBean.setPoints( rs.getLong( "POINTS" ) );
        raUnusedBudgetEmailValueBean.setBudgetType( BudgetType.lookup( rs.getString( "BUDGET_TYPE" ) ) );
        Object daysToPromoExpObj = rs.getObject( "DAYS_TO_PROMO_EXP" );
        Long daysToPromoExp = ( null != daysToPromoExpObj ) ? rs.getLong( "DAYS_TO_PROMO_EXP" ) : null;
        raUnusedBudgetEmailValueBean.setDaysToPromoExp( daysToPromoExp );
        raUnusedBudgetEmailValueBean.setDaystopromoexpcolor( rs.getString( "DAYS_TO_PROMO_EXP_COLOR" ) );
        raUnusedBudgetEmailValueBean.setDayssincelastcolor( rs.getString( "DAYS_SINCE_LAST_COLOR" ) );

        raUnusedBudgetReminderList.add( raUnusedBudgetEmailValueBean );
      }
      return raUnusedBudgetReminderList;
    }
  }

}
