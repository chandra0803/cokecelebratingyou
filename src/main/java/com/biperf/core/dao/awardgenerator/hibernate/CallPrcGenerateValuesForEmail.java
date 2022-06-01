/**
 * 
 */

package com.biperf.core.dao.awardgenerator.hibernate;

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

import com.biperf.core.value.AwardGenFileExtractValueBean;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcGenerateValuesForEmail extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "prc_award_generator_extract";

  public CallPrcGenerateValuesForEmail( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "P_awardGenBatchId", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_awardtype", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_promotionname", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_awardgen_setupname", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_batchdate", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_ref_cursor_merchandise", OracleTypes.CURSOR, new CallPrcGenerateValuesForEmail.AwardGenFileExtractValueBeanListMapperForMerchnadise() ) );
    declareParameter( new SqlOutParameter( "p_out_ref_cursor_points", OracleTypes.CURSOR, new CallPrcGenerateValuesForEmail.AwardGenFileExtractValueBeanListMapperForPoints() ) );

    compile();
  }

  public Map<String, Object> executeProcedure( Long batchId )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "P_awardGenBatchId", batchId );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class AwardGenFileExtractValueBeanListMapperForMerchnadise implements ResultSetExtractor
  {
    @Override
    public List<AwardGenFileExtractValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<AwardGenFileExtractValueBean> reportData = new ArrayList<AwardGenFileExtractValueBean>();

      while ( rs.next() )
      {
        AwardGenFileExtractValueBean reportValue = new AwardGenFileExtractValueBean();

        reportValue.setUserName( rs.getString( "user_name" ) );
        reportValue.setOrdinalPosition( rs.getLong( "ordinal_position" ) );
        reportValue.setAwardDate( rs.getDate( "issue_date" ) );
        reportValue.setAnniversaryNumberOfDays( rs.getInt( "anniversaryDays" ) );
        reportValue.setAnniversaryNumberOfYears( rs.getInt( "anniversaryYears" ) );

        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class AwardGenFileExtractValueBeanListMapperForPoints implements ResultSetExtractor
  {
    @Override
    public List<AwardGenFileExtractValueBean> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<AwardGenFileExtractValueBean> reportData = new ArrayList<AwardGenFileExtractValueBean>();

      while ( rs.next() )
      {
        AwardGenFileExtractValueBean reportValue = new AwardGenFileExtractValueBean();

        reportValue.setUserName( rs.getString( "userName" ) );
        reportValue.setAwardAmount( rs.getLong( "awardAmount" ) );
        reportValue.setAwardDate( rs.getDate( "awardDate" ) );
        reportValue.setAnniversaryNumberOfDays( rs.getInt( "anniversaryDays" ) );
        reportValue.setAnniversaryNumberOfYears( rs.getInt( "anniversaryYears" ) );

        reportData.add( reportValue );
      }

      return reportData;
    }
  }

}
