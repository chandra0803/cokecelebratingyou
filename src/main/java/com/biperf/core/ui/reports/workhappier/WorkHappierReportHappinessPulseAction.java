
package com.biperf.core.ui.reports.workhappier;

import java.time.Month;
import java.util.ArrayList;
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

public class WorkHappierReportHappinessPulseAction extends BaseReportsAction
{
  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "ORG_UNITS",
                                                             "NUMBER_OF_RESPONDENTS",
                                                             "ELIGIBLE_PAX",
                                                             "AVG_SCORE",
                                                             "NUMBER_OF_RESPONDENTS",
                                                             "ELIGIBLE_PAX",
                                                             "AVG_SCORE",
                                                             "NUMBER_OF_RESPONDENTS",
                                                             "ELIGIBLE_PAX",
                                                             "AVG_SCORE",
                                                             "THREE_MONTH_AVG" };

  @Override
  protected String getReportCode()
  {
    return Report.WORK_HAPPIER_HAPPINESS_PULSE;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.happinesspulse.extract";
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
    Map output = getWorkHappierReportsService().getHappinessPulseReportExtract( extractParameters );
    buildCSVColumnNamesForHappinessPulse( contentBuf, EXTRACT_ALL_COLUMN_NAMES, getMonths( (String)extractParameters.get( "selectMonth" ) ) );
    buildCSVExtractContent( contentBuf, output );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.WORK_HAPPIER_HAPPINESS_PULSE;
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

  private List<String> getMonths( String timeFrame )
  {
    int month = 0;
    List<String> months = new ArrayList<String>();
    if ( timeFrame != null )
    {
      month = Integer.parseInt( timeFrame.substring( 0, 2 ) );
    }

    // iterating to get the 3 months
    for ( int index = 0; index < 3; index++ )
    {
      if ( month == 0 )
      {
        month = 12;
      }
      months.add( Month.of( month ).name() );
      month--;
    }
    return months;
  }
}
