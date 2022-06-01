
package com.biperf.core.dao.claim.hibernate;

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

import com.biperf.core.value.promotion.CustomApproverListValueBean;

import oracle.jdbc.OracleTypes;

public class CallPrcNominationCustomApproverList extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "prc_custom_approver_list_cas";

  public CallPrcNominationCustomApproverList( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_promotion_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_level_number", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_recipient_user_id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_behavior", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_award_amount", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nominator_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_is_team", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_approver_list", OracleTypes.CURSOR, new CallPrcNominationCustomApproverList.CustomApproverClassMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Long promotionId, Long levelNumber, String userIds, String behaviors, String awardAmount, Long nominatorId, Long isTeam )
  {
    HashMap<String, Object> inParams = new HashMap<String, Object>();

    inParams.put( "p_in_promotion_id", promotionId );
    inParams.put( "p_in_level_number", levelNumber );
    inParams.put( "p_in_recipient_user_id", userIds );
    inParams.put( "p_in_behavior", behaviors );
    inParams.put( "p_in_award_amount", awardAmount );
    inParams.put( "p_in_nominator_id", nominatorId );
    inParams.put( "p_in_is_team", isTeam );

    Map<String, Object> outParams = execute( inParams );

    return outParams;
  }

  @SuppressWarnings( "rawtypes" )
  private class CustomApproverClassMapper implements ResultSetExtractor
  {
    @Override
    public List<CustomApproverListValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<CustomApproverListValueBean> valueBeanData = new ArrayList<CustomApproverListValueBean>();
      while ( rs.next() )
      {
        CustomApproverListValueBean valueBean = new CustomApproverListValueBean();

        valueBean.setUserId( rs.getLong( "user_id" ) );
        valueBean.setNodeId( rs.getLong( "node_id" ) );

        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }
}
