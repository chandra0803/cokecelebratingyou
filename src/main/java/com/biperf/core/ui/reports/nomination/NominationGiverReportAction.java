
package com.biperf.core.ui.reports.nomination;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.value.nomination.NominationReportValue;

public class NominationGiverReportAction extends NominationReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.NOMINATION_GIVER_SUMMARY;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.displayNomitionGiverNominatorsList( mapping, form, request, response, true );
    return mapping.findForward( "display_summary_report" );
  }

  @Override
  public ActionForward displayNomitionGiverNominationsList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    super.displayNomitionGiverNominationsList( mapping, form, request, response );
    return mapping.findForward( "display_nominationslist_report" );
  }

  public ActionForward displayTopNominatorsChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    if ( !isHaveNotFilterSelected( reportParameters, request ) )
    {
      Map<String, Object> output = getNominationReportsService().getTopNominatorsChartResults( reportParameters );
      List<NominationReportValue> reportData = (List<NominationReportValue>)output.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );
    }
    return mapping.findForward( "display_top_nominators_report" );
  }

  @Override
  protected Boolean hasGiven()
  {
    return Boolean.TRUE;
  }

}
