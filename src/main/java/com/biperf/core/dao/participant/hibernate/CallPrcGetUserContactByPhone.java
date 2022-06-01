
package com.biperf.core.dao.participant.hibernate;

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

import com.biperf.core.ui.user.ContactType;
import com.biperf.core.ui.user.PaxContactType;

import oracle.jdbc.OracleTypes;

/*
 * This class returned the Contact info (both user_email and user_phone) from active participants,
 * based on the user_phone table.  The results are unique within the database,
 * so a pax with a "shared" email or phone - that item will be filtered out of the result list.
 */
public class CallPrcGetUserContactByPhone extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "prc_get_user_contact_by_phone";
  public static final String P_IN_PHONE_NUMBER = "p_in_phone_number";
  public static final String P_IN_UNIQUE_CONTACT = "p_in_unique_contact";
  public static final String P_OUT_RESULT_SET = "p_out_result_set";
  public static final String P_OUT_RETURN_CODE = "p_out_return_code";
  
  /** True if the procedure will return a "input_contact" flag */
  private boolean inputColumnExists = false;

  public CallPrcGetUserContactByPhone( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( P_IN_PHONE_NUMBER, Types.VARCHAR ) );
    declareParameter( new SqlParameter( P_IN_UNIQUE_CONTACT, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_RETURN_CODE, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_RESULT_SET, OracleTypes.CURSOR, new PrcParticipantConactInfoDataExtractor() ) );
    compile();
  }

  public Map<String, Object> executeProcedureUnique( String phoneNumber )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( P_IN_PHONE_NUMBER, phoneNumber );
    inParams.put( P_IN_UNIQUE_CONTACT, 1 );
    inputColumnExists = false;
    return execute( inParams );
  }
  
  public Map<String, Object> executeProcedureAll( String phoneNumber )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( P_IN_PHONE_NUMBER, phoneNumber );
    inParams.put( P_IN_UNIQUE_CONTACT, 0 );
    inputColumnExists = true;
    return execute( inParams );
  }

  private class PrcParticipantConactInfoDataExtractor implements ResultSetExtractor<Object>
  {
    @Override
    public List<PaxContactType> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PaxContactType> paxContactInfosByEmail = new ArrayList<PaxContactType>();

      while ( rs.next() )
      {
        PaxContactType contact = new PaxContactType();
        if ( rs.getString( "CONTACT_TYPE" ).equalsIgnoreCase( ContactType.EMAIL.toString() ) )
        {
          contact.setContactType( ContactType.EMAIL );
        }
        else
        {
          contact.setContactType( ContactType.PHONE );
        }
        contact.setContactId( rs.getLong( "CONTACT_ID" ) );
        contact.setValue( rs.getString( "CONTACT_VALUE" ) );
        contact.setUserId( rs.getLong( "USER_ID" ) );
        contact.setTotalRecords( rs.getLong( "TOTAL_RECORDS" ) );
        contact.setUnique( rs.getBoolean( "IS_CONTACT_UNIQUE" ) );
        if ( inputColumnExists )
        {
          contact.setInputContact( rs.getBoolean( "INPUT_CONTACT" ) );
        }
        paxContactInfosByEmail.add( contact );
      }

      return paxContactInfosByEmail;
    }
  }
}
