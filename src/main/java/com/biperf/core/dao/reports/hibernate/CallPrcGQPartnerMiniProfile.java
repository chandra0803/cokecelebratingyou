
package com.biperf.core.dao.reports.hibernate;

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

import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.value.NameableBean;

import oracle.jdbc.OracleTypes;

public class CallPrcGQPartnerMiniProfile extends StoredProcedure
{
  private static final String STORED_PROC_NAME = "prc_getGQPaxMiniProfile";

  public CallPrcGQPartnerMiniProfile( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_promotion_id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_user_id", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_locale", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GQPartnerMiniProfileExtractor() ) );

    compile();
  }

  @SuppressWarnings( { "rawtypes" } )
  public Map executeProcedure( String promotionId, String userId, String locale )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();

    inParams.put( "p_in_promotion_id", promotionId );
    inParams.put( "p_in_user_id", userId );
    inParams.put( "p_in_locale", locale );

    Map outParams = execute( inParams );
    return outParams;
  }

  @SuppressWarnings( "rawtypes" )
  private class GQPartnerMiniProfileExtractor implements ResultSetExtractor
  {
    @Override
    public List<ParticipantSearchView> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ParticipantSearchView> participantSearchViewList = new ArrayList<ParticipantSearchView>();
      while ( rs.next() )
      {
        ParticipantSearchView valueBean = new ParticipantSearchView();
        valueBean.setId( rs.getLong( "user_id" ) );
        valueBean.setFirstName( rs.getString( "first_name" ) );
        valueBean.setLastName( rs.getString( "last_name" ) );
        valueBean.setAvatarUrl( rs.getString( "avatar_small" ) );
        valueBean.setAllowPublicRecognition( rs.getBoolean( "allow_public_recognition" ) );
        valueBean.setAllowPublicInformation( rs.getBoolean( "allow_public_information" ) );
        valueBean.setFollowed( rs.getBoolean( "allow_public_information" ) );

        String nodeName = rs.getString( "primary_node" );
        NameableBean node = new NameableBean( 1L, nodeName );
        List<NameableBean> nodes = new ArrayList<NameableBean>();
        nodes.add( node );
        valueBean.setNodes( nodes );
        valueBean.setOrgName( nodeName );

        valueBean.setCountryCode( rs.getString( "country_code" ) );
        valueBean.setCountryName( rs.getString( "country_name" ) );
        valueBean.setJobName( rs.getString( "position_type" ) );
        valueBean.setDepartmentName( rs.getString( "department_type" ) );

        participantSearchViewList.add( valueBean );
      }
      return participantSearchViewList;
    }
  }
}
