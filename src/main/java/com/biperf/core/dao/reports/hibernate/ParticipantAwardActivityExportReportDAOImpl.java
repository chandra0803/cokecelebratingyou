/**
 * 
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.ParticipantAwardActivityExportReportDAO;

/**
 * @author poddutur
 *
 */
public class ParticipantAwardActivityExportReportDAOImpl extends BaseReportsDAO implements ParticipantAwardActivityExportReportDAO
{
  private DataSource dataSource;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @Override
  public Map getParticipantAwardActivityExportReportExtract( Map<String, Object> reportParameters )
  {
    CallPrcParticipantAwardActivityExtract procedure = new CallPrcParticipantAwardActivityExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

}
