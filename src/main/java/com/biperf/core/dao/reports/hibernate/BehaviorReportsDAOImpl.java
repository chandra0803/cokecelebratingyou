
package com.biperf.core.dao.reports.hibernate;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.BehaviorReportsDAO;

public class BehaviorReportsDAOImpl extends BaseReportsDAO implements BehaviorReportsDAO
{
  private static final Log log = LogFactory.getLog( BehaviorReportsDAOImpl.class );

  private DataSource dataSource;

  @Override
  public Map<String, Object> getBehaviorsTabularResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_BEHAVIORS_REPORT.PRC_GETTABULARRESULTS";
    String sortColName = "node_name";
    reportParameters.put( "sortColName", sortColName );
    setSortOrderBy( reportParameters );
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcBehaviorsReport procedure = new CallPrcBehaviorsReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getBehaviorsPiechartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_BEHAVIORS_REPORT.PRC_GETPIECHARTRESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcBehaviorsReport procedure = new CallPrcBehaviorsReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getBehaviorsBarchartResults( Map<String, Object> reportParameters )
  {
    String STORED_PROC_NAME = "PKG_QUERY_BEHAVIORS_REPORT.PRC_GETBARCHARTRESULTS";
    setLocaleDatePattern( reportParameters );
    setFilterReportParameters( reportParameters );
    log.debug( " bind variables: " + reportParameters );
    CallPrcBehaviorsReport procedure = new CallPrcBehaviorsReport( dataSource, STORED_PROC_NAME );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public List<String> getBehaviorsByPromo( String promotionId, String locale )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.report.getBehaviorsByPromo" );

    query.setString( "promoId", promotionId );
    query.setString( "locale", locale );

    return query.list();
  }

  @Override
  public Map getBehaviorReportExtract( Map<String, Object> reportParameters )
  {
    CallPrcBehaviorExtract procedure = new CallPrcBehaviorExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

}
