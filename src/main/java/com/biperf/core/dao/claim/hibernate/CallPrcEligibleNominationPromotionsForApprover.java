/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

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

import com.biperf.core.value.nomination.EligibleNominationPromotionValueObject;

import oracle.jdbc.OracleTypes;

/**
 * 
 * @author poddutur
 * @since May 25, 2016
 */
public class CallPrcEligibleNominationPromotionsForApprover extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PKG_LIST_PEND_NOMINATIONS.PRC_LIST_APPROVER_ELIG_PROMOS";

  public CallPrcEligibleNominationPromotionsForApprover( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_approver_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_returncode", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_promotion_detail", OracleTypes.CURSOR, new CallPrcEligibleNominationPromotionsForApprover.EligibleNominationPromotionsMapper() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> parameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_approver_id", parameters.get( "approverUserId" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class EligibleNominationPromotionsMapper implements ResultSetExtractor
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
