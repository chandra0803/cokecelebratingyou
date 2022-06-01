
package com.biperf.core.dao.reports.hibernate;

import java.math.BigDecimal;
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

import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.value.BudgetReallocationValueBean;

import oracle.jdbc.OracleTypes;

public class CallPrcBudgetTransfer extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "PRC_BUDGET_TRANSFER";

  public CallPrcBudgetTransfer( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_budgetid", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_budget_mas_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_budget_seg_id", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_media_ratio", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new CallPrcBudgetTransfer.DataMapper() ) );

    compile();
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public Map executeProcedure( Long ownerBudgetId, Long budgetMastertId, Long budgetSegmentId, BigDecimal mediaRatio )
  {
    HashMap inParams = new HashMap();
    inParams.put( "p_in_budgetid", ownerBudgetId );
    inParams.put( "p_in_budget_mas_id", budgetMastertId );
    inParams.put( "p_in_budget_seg_id", budgetSegmentId );
    inParams.put( "p_in_media_ratio", mediaRatio );

    Map outParams = execute( inParams );

    return outParams;
  }

  private class DataMapper implements ResultSetExtractor<List<BudgetReallocationValueBean>>
  {
    @Override
    public List<BudgetReallocationValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BudgetReallocationValueBean> listBudgetReallocValueBean = new ArrayList<BudgetReallocationValueBean>();
      while ( rs.next() )
      {
        BudgetReallocationValueBean brvBean = new BudgetReallocationValueBean();
        brvBean.setChildBudgetId( rs.getLong( "budget_id" ) );
        if ( Long.valueOf( rs.getLong( "own_node_id" ) ) != null && rs.getLong( "own_node_id" ) > 0 )
        {
          brvBean.setNodeName( rs.getString( "NAME" ) );
          brvBean.setOwnerBudgetNodeId( Long.valueOf( rs.getLong( "own_node_id" ) ) != null ? Long.valueOf( rs.getLong( "own_node_id" ) ).toString() : "" );
          brvBean.setChildNodeOwnerNodeId( rs.getString( "node_id" ) );
        }
        else
        {
          brvBean.setUserId( rs.getString( "user_id" ) );
          brvBean.setNodeName( rs.getString( "first_name" ).concat( " " ).concat( rs.getString( "last_name" ) ) );
        }

        brvBean.setBudgetSpent( Integer.toString( BudgetUtils.getBudgetDisplayValue( rs.getBigDecimal( "budget_spent" ) ) ) );
        brvBean.setCurrentBudget( "" + (int)Math.floor( rs.getBigDecimal( "current_value" ).doubleValue() ) );

        listBudgetReallocValueBean.add( brvBean );
      }
      return listBudgetReallocValueBean;
    }

  }
}
