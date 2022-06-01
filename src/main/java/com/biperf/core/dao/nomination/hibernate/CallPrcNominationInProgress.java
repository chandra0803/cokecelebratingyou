
package com.biperf.core.dao.nomination.hibernate;

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

import com.biperf.core.value.nomination.NominationsInProgressValueObject;

import oracle.jdbc.OracleTypes;

public class CallPrcNominationInProgress extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "prc_list_nom_inprogress";

  public CallPrcNominationInProgress( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_user_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedOn", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcNominationInProgress.NominationInProgressMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> parameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_id", parameters.get( "submitterId" ) );
    inParams.put( "p_in_rowNumStart", parameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", parameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_sortedBy", parameters.get( "sortedBy" ) );
    inParams.put( "p_in_sortedOn", parameters.get( "sortedOn" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class NominationInProgressMapper implements ResultSetExtractor<List<NominationsInProgressValueObject>>
  {
    @Override
    public List<NominationsInProgressValueObject> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationsInProgressValueObject> valueBeanData = new ArrayList<NominationsInProgressValueObject>();
      while ( rs.next() )
      {
        NominationsInProgressValueObject valueBean = new NominationsInProgressValueObject();

        valueBean.setClaimId( rs.getLong( "claim_id" ) );
        valueBean.setPromotionId( rs.getLong( "promotion_id" ) );
        valueBean.setDateCreated( rs.getTimestamp( "date_created" ) );
        valueBean.setPromotionName( rs.getString( "promotion_name" ) );
        valueBean.setName( rs.getString( "name" ) );

        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

}
