/**
 * 
 */

package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.HierarchyExportReportDAO;
import com.biperf.core.service.reports.HierarchyExportReportService;

/**
 * @author poddutur
 *
 */
public class HierarchyExportReportServiceImpl implements HierarchyExportReportService
{
  private HierarchyExportReportDAO hierarchyExportReportDAO;

  @Override
  public Map getHierarchyExportReportExtract( Map<String, Object> reportParameters )
  {
    return hierarchyExportReportDAO.getHierarchyExportReportExtractResults( reportParameters );
  }

  public void setHierarchyExportReportDAO( HierarchyExportReportDAO hierarchyExportReportDAO )
  {
    this.hierarchyExportReportDAO = hierarchyExportReportDAO;
  }

}
