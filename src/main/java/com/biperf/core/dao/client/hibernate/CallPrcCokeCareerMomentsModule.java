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

import com.biperf.core.dao.reports.hibernate.CallPrcBehaviorsReport;
import com.biperf.core.domain.client.JobChangesDatum;
import com.biperf.core.domain.client.NewHiresDatum;
import com.biperf.core.value.client.CokeCareerMomentsValue;

import oracle.jdbc.OracleTypes;

public class CallPrcCokeCareerMomentsModule extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PRC_FIND_CM_HIRE_JOB_HOME";
  public static final String PO_OUT_RESULT_HIRE_SET = "p_out_result_hire_set";
  public static final String PO_OUT_RESULT_JOB_SET = "p_out_result_job_set";


  public CallPrcCokeCareerMomentsModule( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
    declareParameter(new SqlOutParameter( "p_out_result_hire_set", OracleTypes.CURSOR, new CallPrcCokeCareerMomentsModule.CareerMomentsNewHireMapper() ));
    declareParameter(new SqlOutParameter( "p_out_result_job_set", OracleTypes.CURSOR, new CallPrcCokeCareerMomentsModule.CareerMomentsJobMapper() ));
    compile();
  }

  public Map executeProcedure()
  {    
    Map outParams = execute( );
    return outParams;
  }
  
  private class CareerMomentsNewHireMapper implements ResultSetExtractor
  {
    @Override
    public List<NewHiresDatum> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NewHiresDatum> data = new ArrayList<NewHiresDatum>();

      while ( rs.next() )
      {
        NewHiresDatum reportValue = new NewHiresDatum();
        reportValue.setId( rs.getInt( "user_id" ) );
        reportValue.setLastName( rs.getString( "last_name" ) );
        reportValue.setFirstName( rs.getString( "first_name" ) );
        reportValue.setDivision( rs.getString( "division_name" ) );
        reportValue.setPositionType( rs.getString( "position_type" ) );
        reportValue.setAvatarUrl( rs.getString( "avatar_url" ) );
        data.add( reportValue );
      }
      return data;
    }

  }
  
  private class CareerMomentsJobMapper implements ResultSetExtractor
  {
    @Override
    public List<JobChangesDatum> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<JobChangesDatum> data = new ArrayList<JobChangesDatum>();

      while ( rs.next() )
      {
        JobChangesDatum reportValue = new JobChangesDatum();
        reportValue.setId( rs.getInt( "user_id" ) );
        reportValue.setLastName( rs.getString( "last_name" ) );
        reportValue.setFirstName( rs.getString( "first_name" ) );
        reportValue.setDivision( rs.getString( "division_name" ) );
        reportValue.setPositionType( rs.getString( "position_type" ) );
        reportValue.setAvatarUrl( rs.getString( "avatar_url" ) );
        data.add( reportValue );
      }
      return data;
    }

  }

}
