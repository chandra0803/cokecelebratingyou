/**
 * 
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.ParticipantExportReportDAO;

/**
 * @author poddutur
 *
 */
public class ParticipantExportReportDAOImpl extends BaseReportsDAO implements ParticipantExportReportDAO
{

  private DataSource dataSource;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @SuppressWarnings( { "rawtypes" } )
  @Override
  public Map getParticipantExportReportExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcParticipantExtract procedure = new CallPrcParticipantExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

}
