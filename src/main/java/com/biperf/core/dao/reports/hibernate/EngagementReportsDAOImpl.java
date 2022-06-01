
package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.EngagementReportsDAO;

/**
 * 
 * EngagementReportsDAOImpl.
 * 
 * @author kandhi
 * @since Aug 22, 2014
 * @version 1.0
 */
public class EngagementReportsDAOImpl extends BaseReportsDAO implements EngagementReportsDAO
{

  private static final Log log = LogFactory.getLog( EngagementReportsDAOImpl.class );

  private DataSource dataSource;

  @Override
  public Map<String, Object> getParticipationScoreReportExtract( Map<String, Object> reportParameters )
  {
    log.debug( " bind variables: " + reportParameters );
    CallPrcEngagementReportsExtract procedure = new CallPrcEngagementReportsExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

}
