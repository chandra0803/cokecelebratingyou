/**
 * 
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.HierarchyExportReportDAO;

/**
 * @author poddutur
 *
 */
public class HierarchyExportReportDAOImpl extends BaseReportsDAO implements HierarchyExportReportDAO
{
  private DataSource dataSource;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @SuppressWarnings( { "rawtypes" } )
  @Override
  public Map getHierarchyExportReportExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcHierarchyExtract procedure = new CallPrcHierarchyExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }
}
