/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

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
 * 
 * @author rajadura
 * @since Dec 26, 2017
 * 
 */
public class CallPrcRecognitionAdvisorNewHireForEmail extends StoredProcedure
{
  // TODO stored proc name
  private static final String STORED_PROC_NAME = "PRC_RA_NH_REMINDER";
  public static final String P_OUT_DATA = "p_out_user_data";
  public static final String P_OUT_RETURN_CODE = "p_out_retun_code";

  public CallPrcRecognitionAdvisorNewHireForEmail( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_RETURN_CODE, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_DATA, OracleTypes.CURSOR, new RecognitionAdvisorNewHireExtractor() ) );
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

  private class RecognitionAdvisorNewHireExtractor implements ResultSetExtractor<Object>
  {
    @Override
    public List<RecognitionAdvisorValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionAdvisorValueBean> recognitionAdvisoryReminderList = new ArrayList<RecognitionAdvisorValueBean>();
      while ( rs.next() )
      {
        RecognitionAdvisorValueBean raNewHireEmailValueBean = new RecognitionAdvisorValueBean();
        raNewHireEmailValueBean.setId( rs.getLong( "USER_ID" ) );
        raNewHireEmailValueBean.setFirstName( rs.getString( "FIRST_NAME" ) );
        raNewHireEmailValueBean.setLastName( rs.getString( "LAST_NAME" ) );
        raNewHireEmailValueBean.setAvatarUrl( rs.getString( "AVATAR_SMALL" ) );
        raNewHireEmailValueBean.setPositionType( rs.getString( "POSITION_TYPE" ) );

        recognitionAdvisoryReminderList.add( raNewHireEmailValueBean );
      }
      return recognitionAdvisoryReminderList;
    }
  }

}
