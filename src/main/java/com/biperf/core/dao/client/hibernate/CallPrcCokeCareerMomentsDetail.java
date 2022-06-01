
package com.biperf.core.dao.client.hibernate;

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

import com.biperf.core.domain.client.CareerMomentsView;
import com.biperf.core.domain.enums.CareerMomentsType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.utils.UserManager;

import oracle.jdbc.OracleTypes;

public class CallPrcCokeCareerMomentsDetail extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PRC_FIND_CM_HIRE_JOB_DET";

  public CallPrcCokeCareerMomentsDetail( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // NOTE: Calls to declareParameter must be made in the same order as
    // they appear
    // in the database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_curent", Types.INTEGER ) );
    declareParameter( new SqlParameter( "p_in_rec_type", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_tab_type", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_list_value", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_user_name", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );

    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new FetchRecognitionPurlsListExtractor() ) );

    compile();
  }

  public Map executeProcedure( Long userId, int current, String recType, String tabType, String listVal, String locale, String username )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_id", UserManager.getUserId() );
    inParams.put( "p_in_curent", current );
    inParams.put( "p_in_rec_type", recType.toLowerCase() );
    inParams.put( "p_in_tab_type", tabType.toLowerCase() );
    inParams.put( "p_in_list_value", listVal );
    inParams.put( "p_in_user_name", username );
    inParams.put( "p_in_locale", locale );

    Map<String, Object> outParams = execute( inParams );

    return outParams;
  }

  /**
   * PublicRecognitionResultSetExtractor is an Inner class which implements
   * the RowMapper.
   */
  @SuppressWarnings( "rawtypes" )
  private class FetchRecognitionPurlsListExtractor implements ResultSetExtractor
  {
    @Override
    public List<CareerMomentsView> extractData( ResultSet purlCelebrations ) throws SQLException, DataAccessException
    {
      ArrayList<CareerMomentsView> purlCelebrationsList = new ArrayList<CareerMomentsView>();

      while ( purlCelebrations.next() )
      {
        CareerMomentsView view = new CareerMomentsView();
        view.setId( purlCelebrations.getLong( "USER_ID" ) );
        view.setFirstName( purlCelebrations.getString( "FIRST_NAME" ) );
        view.setLastName( purlCelebrations.getString( "LAST_NAME" ) );
        view.setAvatarUrl( purlCelebrations.getString( "AVATAR_URL" ) );
        view.setTotalRecords( purlCelebrations.getInt( "total_records" ) );
        view.setPositionType( purlCelebrations.getString( "position_type" ) );
        view.setDepartmentType( purlCelebrations.getString( "DEPARTMENT_TYPE" ) );
        view.setDivisionName( purlCelebrations.getString( "DIVISION_NAME" ) );
        view.setRecType( CareerMomentsType.lookup( purlCelebrations.getString( "REC_TYPE" )).getName() );
        view.setCelebrationDate( purlCelebrations.getString( "CELEBRATION_DATE" ) );
        view.setAvatarUrl( purlCelebrations.getString( "avatar_url" ) );
        purlCelebrationsList.add( view );
      }

      return purlCelebrationsList;
    }
  }

}
