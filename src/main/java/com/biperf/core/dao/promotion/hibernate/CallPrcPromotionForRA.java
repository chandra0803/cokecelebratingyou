/*
 * (c) 2018 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.promotion.hibernate;

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

import com.biperf.core.value.promotion.RecognitionAdvisorPromotionValueBean;

import oracle.jdbc.OracleTypes;

/**
 * 
 * @author rajadura
 * @since Feb 7, 2018
 * 
 */
public class CallPrcPromotionForRA extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PRC_RA_PROGRAMS";
  public static final String P_OUT_DATA = "p_out_user_data";
  public static final String P_OUT_RETURN_CODE = "p_out_retun_code";

  public CallPrcPromotionForRA( DataSource dataSource )
  {
    super( dataSource, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_giver_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_receiver_id", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_RETURN_CODE, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_DATA, OracleTypes.CURSOR, new PromotionForRAExtractor() ) );
    compile();
  }

  public Map executeProcedure( Long giverId, Long receiverId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_giver_id", giverId );
    inParams.put( "p_in_receiver_id", receiverId );
    Map outParams = execute( inParams );

    return outParams;
  }

  private class PromotionForRAExtractor implements ResultSetExtractor<List<RecognitionAdvisorPromotionValueBean>>
  {

    @Override
    public List<RecognitionAdvisorPromotionValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<RecognitionAdvisorPromotionValueBean> recAdvisorPromoValueBeanLst = new ArrayList<RecognitionAdvisorPromotionValueBean>();
      while ( rs.next() )
      {
        RecognitionAdvisorPromotionValueBean recAdvisorPromoValueBean = new RecognitionAdvisorPromotionValueBean();

        recAdvisorPromoValueBean.setPromotionId( rs.getLong( "PROMOTION_ID" ) );
        recAdvisorPromoValueBean.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        recAdvisorPromoValueBean.setCurrentDate( rs.getDate( "CURRENT_DATE" ) );
        recAdvisorPromoValueBean.setBudgetExpDate( rs.getDate( "BUDGET_EXP_DATE" ) );
        recAdvisorPromoValueBean.setDaysToBudgetExpiry( rs.getInt( "DAYS_TO_BUDGET_EXP" ) );
        recAdvisorPromoValueBean.setAmountRemaining( rs.getInt( "AMOUNT_REMAINING" ) );
        recAdvisorPromoValueBean.setBudgetId( rs.getLong( "BUDGET_ID" ) );
        recAdvisorPromoValueBean.setUserId( rs.getLong( "USER_ID" ) );
        recAdvisorPromoValueBean.setNodeId( rs.getLong( "NODE_ID" ) );
        recAdvisorPromoValueBean.setBudgetMasterId( rs.getLong( "BUDGET_MASTER_ID" ) );
        recAdvisorPromoValueBean.setBudgetSegmentId( rs.getLong( "BUDGET_SEGMENT_ID" ) );
        recAdvisorPromoValueBean.setBudgetAmt( rs.getInt( "BUDGET_AMT" ) );

        recAdvisorPromoValueBeanLst.add( recAdvisorPromoValueBean );
      }
      return recAdvisorPromoValueBeanLst;
    }

  }
}
