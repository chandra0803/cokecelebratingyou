
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

import com.biperf.core.value.nomination.EligibleNominationPromotionValueObject;

import oracle.jdbc.OracleTypes;

public class CallPrcNominationPastWinnersEligiblePromotion extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "pkg_list_past_win_nominations.prc_past_winner_elig_promolist";

  public CallPrcNominationPastWinnersEligiblePromotion( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_approver_user_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_promo_list", OracleTypes.CURSOR, new CallPrcNominationPastWinnersEligiblePromotion.NominationPastWinnersEligiblePromotionMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> parameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_approver_user_id", parameters.get( "approverUserId" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class NominationPastWinnersEligiblePromotionMapper implements ResultSetExtractor
  {
    @Override
    public List<EligibleNominationPromotionValueObject> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<EligibleNominationPromotionValueObject> valueBeanData = new ArrayList<EligibleNominationPromotionValueObject>();
      while ( rs.next() )
      {
        EligibleNominationPromotionValueObject valueBean = new EligibleNominationPromotionValueObject();

        valueBean.setPromoId( rs.getLong( "promotion_id" ) );
        valueBean.setName( rs.getString( "promotion_name" ) );

        valueBeanData.add( valueBean );
      }

      return valueBeanData;
    }
  }

}
