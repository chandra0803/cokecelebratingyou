
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
 * Autocomplete results for forgot login ID. 
 * This is the variant for when an email was picked from the initial search
 */
public class CallPrcGetUserContactAutoCompleteEmail extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "prc_get_user_autocomp_by_email";
  public static final String P_IN_EMAIL_ADDRESS = "p_in_email_address";
  public static final String P_IN_SEARCH_STRING = "p_in_search_string";
  public static final String P_OUT_RESULT_SET = "p_out_result_set";
  public static final String P_OUT_RETURN_CODE = "p_out_return_code";

  public CallPrcGetUserContactAutoCompleteEmail( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( P_IN_EMAIL_ADDRESS, Types.VARCHAR ) );
    declareParameter( new SqlParameter( P_IN_SEARCH_STRING, Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( P_OUT_RETURN_CODE, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_RESULT_SET, OracleTypes.CURSOR, new PrcParticipantConactInfoDataExtractor() ) );
    compile();
  }

  public Map<String, Object> executeProcedure( String emailAddress, String searchString )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( P_IN_EMAIL_ADDRESS, emailAddress );
    inParams.put( P_IN_SEARCH_STRING, searchString );
    return execute( inParams );
  }

  private class PrcParticipantConactInfoDataExtractor implements ResultSetExtractor<List<PaxContactType>>
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
        contact.setUnique( rs.getBoolean( "IS_CONTACT_UNIQUE" ) );
        paxContactInfosByEmail.add( contact );
      }

      return paxContactInfosByEmail;
    }
  }
}
