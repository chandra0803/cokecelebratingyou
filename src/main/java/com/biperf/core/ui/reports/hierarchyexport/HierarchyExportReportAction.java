/**
 * 
 */

package com.biperf.core.ui.reports.hierarchyexport;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.reports.HierarchyExportReportService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;

/**
 * @author poddutur
 *
 */
public class HierarchyExportReportAction extends BaseReportsAction
{

  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "RECORD_TYPE",
                                                             "ORG_UNIT_NAME",
                                                             "OLD_ORG_UNIT_NAME",
                                                             "MOVE_ORG_UNIT_NAME",
                                                             "DESCRIPTION",
                                                             "ORG_UNIT_TYPE",
                                                             "PARENT_ORG_UNIT_NAME",
                                                             "OLD_PARENT_ORG_UNIT_NAME",
                                                             "ORG_UNIT_CHAR1_NAME",
                                                             "ORG_UNIT_CHAR1_VALUE",
                                                             "ORG_UNIT_CHAR2_NAME",
                                                             "ORG_UNIT_CHAR2_VALUE",
                                                             "ORG_UNIT_CHAR3_NAME",
                                                             "ORG_UNIT_CHAR3_VALUE",
                                                             "ORG_UNIT_CHAR4_NAME",
                                                             "ORG_UNIT_CHAR4_VALUE",
                                                             "ORG_UNIT_CHAR5_NAME",
                                                             "ORG_UNIT_CHAR5_VALUE",
                                                             "ORG_UNIT_CHAR6_NAME",
                                                             "ORG_UNIT_CHAR6_VALUE",
                                                             "ORG_UNIT_CHAR7_NAME",
                                                             "ORG_UNIT_CHAR7_VALUE",
                                                             "ORG_UNIT_CHAR8_NAME",
                                                             "ORG_UNIT_CHAR8_VALUE",
                                                             "ORG_UNIT_CHAR9_NAME",
                                                             "ORG_UNIT_CHAR9_VALUE",
                                                             "ORG_UNIT_CHAR10_NAME",
                                                             "ORG_UNIT_CHAR10_VALUE" };

  protected HierarchyExportReportService getHierarchyExportReportService()
  {
    return (HierarchyExportReportService)getService( HierarchyExportReportService.BEAN_NAME );
  }

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  @Override
  protected String getReportCode()
  {
    return Report.HIERARCHY_EXPORT;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.hierarchy.extract";
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );
    return mapping.findForward( "display_summary_report" );
  }

  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map output = getHierarchyExportReportService().getHierarchyExportReportExtract( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, output );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.HIERARCHY_REPORT;
  }
}
