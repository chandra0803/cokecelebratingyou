
package com.biperf.core.ui.reports.nomination;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.value.nomination.NominationReportValue;

public class NominationReceiverReportAction extends NominationReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.NOMINATION_RECEIVER_SUMMARY;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.displayNomitionsReceivedNomineesList( mapping, form, request, response, true );
    return mapping.findForward( "display_nomination_receiver_summary_report" );
  }

  public ActionForward displayNomineeReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.displayNomitionsNomineeList( mapping, form, request, response, true );
    return mapping.findForward( "display_nomination_nominee_report" );
  }

  public ActionForward displayNominationsTopNominee( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    // super.prepareDisplay( mapping, form, request, response );
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getNominationReportsService().getNominationTopNomineeChartResult( reportParameters );
    List<NominationReportValue> reportData = (List<NominationReportValue>)output.get( OUTPUT_DATA );
    PropertyComparator.sort( reportData, new MutableSortDefinition( "promoName", false, true ) );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_nomination_top_nominee" );
  }

  @Override
  protected Boolean hasReceived()
  {
    return Boolean.TRUE;
  }

}
