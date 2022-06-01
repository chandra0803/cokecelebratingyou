
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

import com.biperf.core.value.client.CokeCommentsLikes;

import oracle.jdbc.OracleTypes;

public class CallPrdCokeFindLikesComments extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PRC_FIND_COM_LIKES";

  public CallPrdCokeFindLikesComments( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // NOTE: Calls to declareParameter must be made in the same order as
    // they appear
    // in the database's stored procedure parameter list.
    // Date Format - mm/dd/yyyy
    declareParameter( new SqlParameter( "P_IN_DATE", Types.VARCHAR ) );

    declareParameter( new SqlOutParameter( "p_out_returncode", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new FetchCokeCommentsLikes() ) );

    compile();
  }

  public Map executeProcedure( String date )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "P_IN_DATE", date );

    Map<String, Object> outParams = execute( inParams );

    return outParams;
  }

  @SuppressWarnings( "rawtypes" )
  private class FetchCokeCommentsLikes implements ResultSetExtractor
  {
    @Override
    public List<CokeCommentsLikes> extractData( ResultSet commentsLikes ) throws SQLException, DataAccessException
    {
      List<CokeCommentsLikes> commentsLikesList = new ArrayList<CokeCommentsLikes>();

      while ( commentsLikes.next() )
      {
        CokeCommentsLikes view = new CokeCommentsLikes();
        view.setUserId( commentsLikes.getLong( "USER_ID" ) );
        view.setFirstName( commentsLikes.getString( "FIRST_NAME" ) );
        view.setLastName( commentsLikes.getString( "LAST_NAME" ) );
        view.setEmailAddress( commentsLikes.getString( "EMAIL_ADDR" ) );
        view.setLikesCount( commentsLikes.getInt( "NOFLIKES" ) );
        view.setCommentsCount( commentsLikes.getInt( "NOCOMMENTS" ) );
        commentsLikesList.add( view );
      }

      return commentsLikesList;
    }
  }

}
