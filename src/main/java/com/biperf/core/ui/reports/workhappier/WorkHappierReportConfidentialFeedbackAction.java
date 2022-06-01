
package com.biperf.core.ui.reports.workhappier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.reports.WorkHappierReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParametersForm;

public class WorkHappierReportConfidentialFeedbackAction extends BaseReportsAction
{

  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "ORG_NAME", "FEEDBACK", "DATE" };

  @Override
  protected String getReportCode()
  {
    return Report.WORK_HAPPIER_CONFIDENTIAL_FEEDBACK;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.confidentialfeedback.extract";
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> extractParameters = reportParametersForm.getReportParameters();

    List<String> requiredFields = Arrays.asList( new String[] { FILTER_SELECT_MONTH } );
    request.setAttribute( "maxRows", 1L );
    if ( isChangeFiltersPage( request ) && hasErrorMessages( request, extractParameters, requiredFields, true ) )
    {
      return mapping.findForward( "display_summary_report" );
    }
    return mapping.findForward( "display_summary_report" );
  }

  @Override
  protected String getExtractReportData( Map<String, Object> extractParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map output = getWorkHappierReportsService().getConfidentialFeedbackReportExtract( extractParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, output );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.WORK_HAPPIER_CONFIDENTIAL_FEEDBACK_REPORT;
  }

  @Override
  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  protected WorkHappierReportsService getWorkHappierReportsService()
  {
    return (WorkHappierReportsService)getService( WorkHappierReportsService.BEAN_NAME );
  }
}
