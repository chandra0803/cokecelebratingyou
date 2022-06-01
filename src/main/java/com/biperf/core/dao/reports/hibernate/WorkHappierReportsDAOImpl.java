
package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.WorkHappierReportsDAO;

public class WorkHappierReportsDAOImpl extends BaseReportsDAO implements WorkHappierReportsDAO
{
  private DataSource dataSource;

  public DataSource getDataSource()
  {
    return dataSource;
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @Override
  public Map<String, Object> getConfidentialFeedbackReportExtract( Map<String, Object> reportParameters )
  {
    CallPrcConfidentialFeedbackExtract procedure = new CallPrcConfidentialFeedbackExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getHappinessPulseReportExtract( Map<String, Object> reportParameters )
  {
    CallPrcHappinessPulseExtract procedure = new CallPrcHappinessPulseExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getWorkHappierReportExtract( Map<String, Object> extractParameters )
  {
    CallPrcWorkHappierReportExtract procedure = new CallPrcWorkHappierReportExtract( dataSource );
    return procedure.executeProcedure( extractParameters );
  }

}
