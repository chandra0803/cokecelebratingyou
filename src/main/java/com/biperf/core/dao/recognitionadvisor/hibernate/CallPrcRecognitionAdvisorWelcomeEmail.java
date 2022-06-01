
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
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.value.RAValueBean;

import oracle.jdbc.OracleTypes;

public class CallPrcRecognitionAdvisorWelcomeEmail extends StoredProcedure
{
  /*
   * (c) 2017 BI, Inc. All rights reserved. $Source$
   */

  private static final String STORED_PROC_NAME = "PRC_RA_WELCOME_EMAIL";
  public static final String P_OUT_DATA = "p_out_user_data";
  public static final String P_OUT_RETURN_CODE = "p_out_retun_code";

  public CallPrcRecognitionAdvisorWelcomeEmail( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    
    declareParameter( new SqlOutParameter( P_OUT_RETURN_CODE, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_DATA, OracleTypes.CURSOR, new RecognitionAdvisorWelcomeEmailExtractor() ) );
    compile();
  }

  @SuppressWarnings( { "rawtypes" } )
  public Map executeProcedure( Long userId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    
    Map outParams = execute( inParams );

    return outParams;
  }

  private class RecognitionAdvisorWelcomeEmailExtractor implements ResultSetExtractor<Object>
  {
    @Override
    public List<RAValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RAValueBean> recognitionAdvisoryReminderList = new ArrayList<RAValueBean>();
      while ( rs.next() )
      {
        RAValueBean raValueBean = new RAValueBean();
        raValueBean.setUserId( rs.getLong( "USER_ID" ) );
        raValueBean.setFirstName( rs.getString( "FIRST_NAME" ) );
        raValueBean.setLastName( rs.getString( "LAST_NAME" ) );
        raValueBean.setEmailAddress( rs.getString( "EMAIL_ADDR" ) );
        recognitionAdvisoryReminderList.add( raValueBean );
      }
      return recognitionAdvisoryReminderList;
    }
  }

}
