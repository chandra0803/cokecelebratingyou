
package com.biperf.core.ui.reports.individual;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.ReportIndividualActivityModuleType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.ui.reports.ReportParameterInfo;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.individualactivity.IndividualActivityReportValue;
import com.biperf.util.StringUtils;

public class IndividualActivityReportAction extends IndividualReportAction
{

  @Override
  protected String getReportCode()
  {
    return Report.INDIVIDUAL_ACTIVITY;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Long paxId = null;
    if ( request.getParameter( FILTER_PAX_ID ) != null )
    {
      paxId = Long.parseLong( request.getParameter( FILTER_PAX_ID ) );
      reportParametersForm.setPaxId( paxId );
    }
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    super.prepareDisplay( mapping, form, request, response );

    List<String> requiredFields = Arrays.asList( new String[] { FILTER_PAX_ID } );
    if ( isChangeFiltersPage( request ) && hasErrorMessages( request, reportParameters, requiredFields, false ) )
    {
      return mapping.findForward( "display_individual_activity_summary" );
    }
    if ( paxId != null )
    {
      boolean isValidUser = getNodeService().isUserInNodeorBelow( paxId, UserManager.getUserId() );
      boolean isBiAdminOrProjectManager = UserManager.isUserInRole( AuthorizationService.ROLE_CODE_BI_ADMIN ) || UserManager.isUserInRole( AuthorizationService.ROLE_CODE_PROJ_MGR );
      boolean viewReportsRole = UserManager.isUserInRole( AuthorizationService.ROLE_CODE_VIEW_REPORTS );
      boolean userWithoutAcl = CollectionUtils.isEmpty( getUserService().getAvailableAclsByUserId( paxId ) );
      // If user is bi admin, project manager or user with view reports role without acl, the user
      // can search for individual report irrespective of org unit
      if ( ! ( isBiAdminOrProjectManager || ( viewReportsRole && userWithoutAcl ) || isValidUser ) )
      {
        reportParametersForm.setPaxId( null );
        return mapping.findForward( "display_individual_activity_summary" );
      }
    }
    if ( StringUtils.isEmpty( request.getParameter( CLEAR_FORM ) ) || isDashboardPage( request ) )
    {
      populatePaxIdFromDashboard( request, reportParametersForm, reportParameters );
      Map<String, Object> output = getIndividualReportsService().getIndividualActivityTabularResults( reportParameters );
      List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
      reportData = validateForOntheSpotList( reportData );
      reportData = changeOrderForViewAllAwardsList( reportData );
      request.setAttribute( "reportData", reportData );

      Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        IndividualActivityReportValue totalsRowData = (IndividualActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "reportDetailType", "baseLevel" );
      populateFirstAndLastName( request, reportParametersForm );
    }
    else
    {
      request.setAttribute( "hideCharts", Boolean.TRUE );
    }
    return mapping.findForward( "display_individual_activity_summary" );
  }

  protected void populatePaxIdFromDashboard( HttpServletRequest request, ReportParametersForm reportParametersForm, Map<String, Object> reportParameters )
  {
    Long paxId;
    if ( isDashboardPage( request ) )
    {
      List<ReportParameterInfo> reportParameterInfoList = reportParametersForm.getReportParameterInfoList();
      for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
      {
        if ( reportParameterInfo.getName().equals( "paxId" ) )
        {
          paxId = Long.valueOf( reportParameterInfo.getValue() );
          reportParametersForm.setPaxId( paxId );
          reportParameters.put( "paxId", paxId );
        }
      }
    }
  }

  protected void populateChartPaxIdFromDashboard( HttpServletRequest request, ReportParametersForm reportParametersForm, Map<String, Object> reportParameters )
  {
    Long paxId;
    if ( isDashboardPage( request ) )
    {
      paxId = Long.valueOf( (String)reportParameters.get( "paxId" ) );
      reportParameters.put( "paxId", paxId );
    }
  }

  // Bug 1052
  private void populateFirstAndLastName( HttpServletRequest request, ReportParametersForm reportParametersForm )
  {
    String firstName = request.getParameter( "pax[0].firstName" );
    String lastName = request.getParameter( "pax[0].lastName" );

    List<ReportParameterInfo> reportParameterInfoList = reportParametersForm.getReportParameterInfoList();
    for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
    {
      if ( reportParameterInfo.getName().equals( "firstName" ) )
      {
        // Bug 1633 - Coming from dashboard
        if ( isDashboardPage( request ) )
        {
          firstName = reportParameterInfo.getValue();
        }
        else if ( !StringUtil.isEmpty( firstName ) )
        {
          reportParameterInfo.setValue( firstName );
        }
      }
      else if ( reportParameterInfo.getName().equals( "lastName" ) )
      {
        if ( isDashboardPage( request ) )
        {
          firstName = reportParameterInfo.getValue();
        }
        else if ( !StringUtil.isEmpty( lastName ) )
        {
          reportParameterInfo.setValue( lastName );
        }
      }
      else if ( reportParameterInfo.getName().equals( "paxId" ) )
      {
        reportParameterInfo.setValue( String.valueOf( reportParametersForm.getPaxId() ) );
      }
    }

    // Bug 1377 - Display pax name for the extract file name
    if ( !StringUtil.isEmpty( firstName ) )
    {
      reportParametersForm.setPaxFirstName( firstName );
    }
    if ( !StringUtil.isEmpty( lastName ) )
    {
      reportParametersForm.setPaxLastName( lastName );
    }

    if ( null != reportParametersForm.getPaxId() && reportParametersForm.getPaxId() > 0 )
    {
      Participant participant = getParticipantService().getParticipantById( reportParametersForm.getPaxId() );
      reportParametersForm.setAvatarUrl( participant.getAvatarOriginal() );
    }
  }

  public ActionForward displayDrilldownReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );
    ReportIndividualActivityModuleType drilldownModule = ReportIndividualActivityModuleType.lookup( ( (ReportParametersForm)form ).getDisplayType() );

    // if ( ReportIndividualActivityModuleType.AWARDS_RECEIVED.equals( drilldownModule.getCode() ) )
    // {
    // displayAwardsReceivedSummaryReport( (ReportParametersForm)form, request );
    // return mapping.findForward( "display_individual_activity_awards_received_summary" );
    // }

    if ( ReportIndividualActivityModuleType.GOAL_QUEST.equals( drilldownModule.getCode() ) )
    {
      displayGoalQuestSummaryReport( (ReportParametersForm)form, request );
      return mapping.findForward( "display_individual_activity_goal_quest_summary" );
    }
    if ( ReportIndividualActivityModuleType.CHALLENGEPOINT.equals( drilldownModule.getCode() ) )
    {
      displayChallengePointSummaryReport( (ReportParametersForm)form, request );
      return mapping.findForward( "display_individual_activity_challengepoint_summary" );
    }
    if ( ReportIndividualActivityModuleType.ONTHESPOT.equals( drilldownModule.getCode() ) )
    {
      displayOnTheSpotSummaryReport( (ReportParametersForm)form, request );
      return mapping.findForward( "display_individual_activity_onthespot_summary" );
    }
    if ( ReportIndividualActivityModuleType.NOMINATIONS_RECEIVED.equals( drilldownModule.getCode() ) )
    {
      displayNominationsReceivedSummaryReport( (ReportParametersForm)form, request );
      return mapping.findForward( "display_individual_activity_nominations_received_summary" );
    }
    if ( ReportIndividualActivityModuleType.NOMINATIONS_GIVEN.equals( drilldownModule.getCode() ) )
    {
      displayNominationsGivenSummaryReport( (ReportParametersForm)form, request );
      return mapping.findForward( "display_individual_activity_nominations_given_summary" );
    }
    if ( ReportIndividualActivityModuleType.PRODUCT_CLAIMS.equals( drilldownModule.getCode() ) )
    {
      displayProductClaimSummaryReport( (ReportParametersForm)form, request );
      return mapping.findForward( "display_individual_activity_product_claim_summary" );
    }
    if ( ReportIndividualActivityModuleType.RECOGNITIONS_GIVEN.equals( drilldownModule.getCode() ) )
    {
      displayRecognitionsGivenSummaryReport( (ReportParametersForm)form, request );
      return mapping.findForward( "display_individual_activity_recognitions_given_summary" );
    }
    if ( ReportIndividualActivityModuleType.RECOGNITIONS_RECEIVED.equals( drilldownModule.getCode() ) )
    {
      displayRecognitionsReceivedSummaryReport( (ReportParametersForm)form, request );
      return mapping.findForward( "display_individual_activity_recognitions_received_summary" );
    }
    if ( ReportIndividualActivityModuleType.QUIZZES.equals( drilldownModule.getCode() ) )
    {
      displayQuizSummaryReport( (ReportParametersForm)form, request );
      return mapping.findForward( "display_individual_activity_quiz_summary" );
    }
    if ( ReportIndividualActivityModuleType.THROWDOWN.equals( drilldownModule.getCode() ) )
    {
      displayThrowdownSummaryReport( (ReportParametersForm)form, request );
      return mapping.findForward( "display_individual_activity_throwdown_summary" );
    }
    if ( ReportIndividualActivityModuleType.BADGE.equals( drilldownModule.getCode() ) )
    {
      displayBadgeSummaryReport( (ReportParametersForm)form, request );
      return mapping.findForward( "display_individual_activity_badge_summary" );
    }
    if ( ReportIndividualActivityModuleType.SSI_CONTEST.equals( drilldownModule.getCode() ) )
    {
      displaySSIContestSummaryReport( (ReportParametersForm)form, request );
      return mapping.findForward( "display_individual_ssi_contest_summary" );
    }

    return null;
  }

  private void displayChallengePointSummaryReport( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> reportParameters = form.getReportParameters();

    Map<String, Object> output = getIndividualReportsService().getIndividualActivityChallengePointTabularResults( form.getReportParameters() );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      IndividualActivityReportValue totalsRowData = (IndividualActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }
  }

  public void displayGoalQuestSummaryReport( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> reportParameters = form.getReportParameters();

    Map<String, Object> output = getIndividualReportsService().getIndividualActivityGoalQuestTabularResults( form.getReportParameters() );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      IndividualActivityReportValue totalsRowData = (IndividualActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }
  }

  public void displayOnTheSpotSummaryReport( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> reportParameters = form.getReportParameters();

    Map<String, Object> output = getIndividualReportsService().getIndividualActivityOnTheSpotTabularResults( form.getReportParameters() );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      IndividualActivityReportValue totalsRowData = (IndividualActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }
  }

  public ActionForward displayAwardsReceivedSummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    Map<String, Object> output = getIndividualReportsService().getIndividualActivityAwardsReceivedTabularResults( reportParameters );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      IndividualActivityReportValue totalsRowData = (IndividualActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    return mapping.findForward( "display_individual_activity_awards_received_summary" );
  }

  public void displayNominationsReceivedSummaryReport( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> output = getIndividualReportsService().getIndividualActivityNominationsReceivedTabularResults( form.getReportParameters() );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );
  }

  public void displayNominationsGivenSummaryReport( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> output = getIndividualReportsService().getIndividualActivityNominationsGivenTabularResults( form.getReportParameters() );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );
  }

  public void displayProductClaimSummaryReport( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> output = getIndividualReportsService().getIndividualActivityProductClaimTabularResults( form.getReportParameters() );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );
  }

  public void displayRecognitionsGivenSummaryReport( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> output = getIndividualReportsService().getIndividualActivityRecognitionsGivenTabularResults( form.getReportParameters() );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );
  }

  public void displayRecognitionsReceivedSummaryReport( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> output = getIndividualReportsService().getIndividualActivityRecognitionsReceivedTabularResults( form.getReportParameters() );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );
  }

  public void displayQuizSummaryReport( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> reportParameters = form.getReportParameters();

    Map<String, Object> output = getIndividualReportsService().getIndividualActivityQuizTabularResults( form.getReportParameters() );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      IndividualActivityReportValue totalsRowData = (IndividualActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }
  }

  public void displayThrowdownSummaryReport( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> reportParameters = form.getReportParameters();

    Map<String, Object> output = getIndividualReportsService().getIndividualActivityThrowdownTabularResults( form.getReportParameters() );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      IndividualActivityReportValue totalsRowData = (IndividualActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }
  }

  public void displayBadgeSummaryReport( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> reportParameters = form.getReportParameters();

    Map<String, Object> output = getIndividualReportsService().getIndividualActivityBadgeTabularResults( form.getReportParameters() );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      IndividualActivityReportValue totalsRowData = (IndividualActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }
  }

  @SuppressWarnings( "unchecked" )
  public void displaySSIContestSummaryReport( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> output = getIndividualReportsService().getIndividualActivitySSIContestTabularResults( form.getReportParameters() );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    for ( ReportParameterInfo info : form.getReportParameterInfoList() )
    {
      if ( "paxId".equals( info.getName() ) )
      {
        for ( IndividualActivityReportValue individualActivityReportValue : reportData )
        {
          individualActivityReportValue.setUserId( Long.valueOf( info.getValue() ) );
        }
        break;
      }
    }
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );
  }

  // ==============================================
  // DETAIL REPORTS
  // ==============================================
  public ActionForward displayQuizDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );
    Map<String, Object> output = getIndividualReportsService().getIndividualActivityQuizDetailResults( ( (ReportParametersForm)form ).getReportParameters() );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );
    return mapping.findForward( "display_individual_activity_quiz_detail" );
  }

  public ActionForward displayThrowdownDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );
    Map<String, Object> reportParameters = ( (ReportParametersForm)form ).getReportParameters();
    Map<String, Object> output = getIndividualReportsService().getIndividualActivityThrowdownDetailResults( reportParameters );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      IndividualActivityReportValue totalsRowData = (IndividualActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }
    return mapping.findForward( "display_individual_activity_throwdown_detail" );
  }

  // ==============================================
  // REPORT CHARTS
  // ==============================================

  public ActionForward displayPointsReceivedChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    populateChartPaxIdFromDashboard( request, reportParametersForm, reportParameters );
    Map<String, Object> output = getIndividualReportsService().getIndividualActivityPointsReceivedChart( reportParameters );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_individual_activity_points_received" );
  }

  public ActionForward displayPointsGivenReceivedChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    populateChartPaxIdFromDashboard( request, reportParametersForm, reportParameters );
    Map<String, Object> output = getIndividualReportsService().getIndividualActivityPointsGivenReceivedChart( reportParameters );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_individual_activity_points_given_received" );
  }

  public ActionForward displayTotalActivityChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    populateChartPaxIdFromDashboard( request, reportParametersForm, reportParameters );
    Map<String, Object> output = getIndividualReportsService().getIndividualActivityTotalActivityChart( reportParameters );
    List<IndividualActivityReportValue> reportData = (List<IndividualActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_individual_activity_total_activity" );
  }

  public ActionForward displayMetricsChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    populateChartPaxIdFromDashboard( request, reportParametersForm, reportParameters );
    Map<String, Object> output = getIndividualReportsService().getIndividualActivityMetricsChart( reportParameters );
    IndividualActivityReportValue reportData = (IndividualActivityReportValue)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_individual_activity_metrics" );
  }

  @Override
  protected String[] getColumnHeaders()
  {
    throw new RuntimeException( "Method not supported" );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }
}
