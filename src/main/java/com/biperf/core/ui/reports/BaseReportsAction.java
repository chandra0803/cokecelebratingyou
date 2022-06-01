/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/BaseReportsAction.java,v $
 *
 */

package com.biperf.core.ui.reports;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ReportParameterType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.report.Report;
import com.biperf.core.domain.report.ReportChart;
import com.biperf.core.domain.report.ReportDashboardItem;
import com.biperf.core.domain.report.ReportDashboardItemParam;
import com.biperf.core.domain.report.ReportParameter;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.GenericReportExtractProcess;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.reports.BudgetReportsService;
import com.biperf.core.service.reports.DashboardReportsService;
import com.biperf.core.service.reports.ReportsService;
import com.biperf.core.service.reports.SurveyReportsService;
import com.biperf.core.service.reports.WorkHappierReportsService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.reports.awards.AwardsReportAction;
import com.biperf.core.ui.reports.budget.BudgetReportAction;
import com.biperf.core.ui.reports.nomination.NominationAgingReportAction;
import com.biperf.core.ui.reports.nomination.NominationReportAction;
import com.biperf.core.ui.reports.survey.SurveyAnalysisReportAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.utils.threads.CallableFactory;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.survey.SurveyAnalysisHeaderListReportValue;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * ReportsAction
 * <p/>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Oct 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * <tr>
 * <td>Senthil</td>
 * <td>Nov 15, 2005</td>
 * <td>1.1</td>
 * <td>Updated with report option</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 * 
 */
public abstract class BaseReportsAction extends BaseDispatchAction
{

  protected static final String NODE_AND_BELOW = "nodeAndBelow";
  protected static final String PARAM_EXPORT_LEVEL = "exportLevel";
  protected static final String PARENT_NODE_ID = "parentNodeId";
  private static final String DASHBOARD_ITEM_ID = "dashboardItemId";
  protected static final String CLEAR_FORM = "clearForm";
  private static final String IS_FILTERS_PAGE = "isFiltersPage";
  protected static final String FILTER_PROMOTION_ID = "promotionId";
  protected static final String FILTER_PAX_ID = "pax[0].userId";
  private static final String FILTER_TO_DATE = "toDate";
  private static final String FILTER_FROM_DATE = "fromDate";
  protected static final String FILTER_SELECT_MONTH = "selectMonth";
  protected static final String FILTER_VIEW_COUNTRY_ID = "viewCountryId";
  private static final String FILTER_COUNTRY_RATIO = "countryRatio";
  protected static final String REGEX_COMMA = ",";
  protected static final String NEW_LINE = "\n";
  protected static final String EXPORT_VIEW_TYPE = "exportViewType";
  protected static final String EXTRACT_FULL_VIEW = "fullReport";
  protected static final String SURVEY_ANALYSIS_EXTRACT = "getSurveyAnalysisOptionsReportExtract";
  protected static final String SURVEY_OPEN_ENDED_EXTRACT = "getSurveyAnalysisOpenEndedReportExtract";
  protected static final String POINTS_BUDGET_ISSUANCE_EXTRACT = "getPointsBudgetIssuanceExtractResults";
  protected static final String CASH_BUDGET_ISSUANCE_EXTRACT = "getCashBudgetIssuanceExtractResults";
  protected static final String AWARD_ACTIVITY_EXTRACT = "getAwardsActivityReportExtract";
  protected static final String NOMINATION_SUMMARY_EXTRACT = "getNominationAgingReportSummaryExtract";
  protected static final String NOMINATION_SUMMARY_REPORT = "getNominationSummaryExtractResults";

  public static final String TOTALS_ROW_DATA = "totalsRowData";
  public static final String MAX_ROWS = "maxRows";
  public static final String REPORT_DATA = "reportData";

  private static final String[] EXPORT_FILTER_MSG_REPORT_LIST_ARRAY = { Report.RECOGNITION_RECEIVED_BY_PAX,
                                                                        Report.RECOGNITION_RECEIVED_BY_ORG,
                                                                        Report.RECOGNITION_GIVEN_BY_PAX,
                                                                        Report.RECOGNITION_GIVEN_BY_ORG,
                                                                        Report.NOMINATION_GIVEN_BY_ORG,
                                                                        Report.NOMINATION_GIVER_SUMMARY,
                                                                        Report.NOMINATION_RECEIVED_BY_ORG,
                                                                        Report.NOMINATION_RECEIVER_SUMMARY,
                                                                        Report.CLAIM_BY_ORG,
                                                                        Report.CLAIM_BY_PAX,
                                                                        Report.AWARDS_BY_ORG,
                                                                        Report.AWARDS_BY_PAX,
                                                                        Report.LOGIN_ACTIVITY,
                                                                        Report.LOGIN_ACTIVITY_BY_PAX,
                                                                        Report.QUIZ_ACTIVITY };

  private static final List<String> EXPORT_FILTER_MSG_REPORT_LIST = Arrays.asList( EXPORT_FILTER_MSG_REPORT_LIST_ARRAY );

  /**
   * Return code returned from the stored procedure
   */
  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";

  /**
   * Result set returned from the stored procedure
   */
  public static final String OUTPUT_RESULT_SET = "p_out_result_set";

  /**
   * Result set returned from the stored procedure
   */
  public static final String OUTPUT_DATA = "p_out_data";

  public static final String OUTPUT_TOTALS_DATA = "p_out_totals_data";

  public static final String OUTPUT_SIZE_DATA = "p_out_size_data";

  public static final String OUTPUT_RESULTS_CURSOR = "p_out_resultsCursor";

  public static final String OUTPUT_TOTALS_CURSOR = "p_out_totalsCursor";

  public static final String SUMMARY_PAX_OUTPUT_RESULT_SET = "p_out_rs_getAwdSmryPaxRes";

  /**
   * Stored proc returns this code when the stored procedure executed without errors
   */
  public static final String GOOD = "00";

  /**
   * Not using "log" since DispatchAction (and some of our subclasses - though they probably
   * shouldn't) uses it and we want our our own so we can turn on or off just logging of this class.
   */
  private static final Log localLog = LogFactory.getLog( BaseReportsAction.class );

  protected abstract String getReportCode();

  public ActionForward prepareDisplay( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;

    if ( Boolean.TRUE.equals( reportParametersForm.getClearForm() ) )
    {
      reportParametersForm.clearForm();
    }

    populateForm( reportParametersForm, request );
    Report report = getReportsService().getReportByCode( getReportCode() );
    report = populateCharts( report );
    populateSystemUrl( report );
    reportParametersForm.setReportParameterInfoList( populateParameterInfo( report, request, reportParametersForm.getReportParameterInfoList(), reportParametersForm ) );
    request.setAttribute( "report", report );
    request.setAttribute( "refreshDate", getRefreshDate( report.getCategoryType().getCode() ) );
    return null;
  }

  public ActionForward loadReportParameters( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    this.prepareDisplay( mapping, form, request, response );
    if ( EXPORT_FILTER_MSG_REPORT_LIST.contains( getReportCode() ) )
    {
      request.setAttribute( "displayExportOnlyFilterLabel", Boolean.TRUE );
    }
    if ( getReportCode().equals( Report.QUIZ_ANALYSIS ) || getReportCode().equals( Report.SURVEY_ANALYSIS ) )
    {
      request.setAttribute( "showSelectPromotion", Boolean.TRUE );
    }

    request.setAttribute( "showExportSelection", isExportSelectionEnabled( getReportCode() ) );
    return mapping.findForward( "display_report_parameters" );
  }

  private boolean isExportSelectionEnabled( String reportCode )
  {
    boolean showExportSelection = true;
    if ( reportCode.equals( Report.NOMINATION_GIVER_SUMMARY ) || reportCode.equals( Report.NOMINATION_RECEIVER_SUMMARY ) || reportCode.equals( Report.NOMINATION_GIVEN_BY_ORG ) )
    {
      PropertySetItem exportSelection = getSystemVariableService().getPropertyByName( "nomination.report.comment.available" );
      if ( exportSelection != null )
      {
        if ( exportSelection.getBooleanVal() )
        {
          showExportSelection = true;
        }
        else
        {
          showExportSelection = false;
        }
      }
    }
    return showExportSelection;
  }

  protected boolean isLastPage( int maxRows, Map<String, Object> reportParameters )
  {
    boolean isLastPage = false;
    int beginIndex = (Integer)reportParameters.get( "rowNumStart" );
    int endIndex = (Integer)reportParameters.get( "rowNumEnd" );
    if ( beginIndex < maxRows && maxRows < endIndex )
    {
      isLastPage = true;
    }
    return isLastPage;
  }

  protected void populateForm( ReportParametersForm form, HttpServletRequest request )
  {
    // Reset parent node id only if the form has no value or
    // if the form has a value but the request does not have the parent node id. This should be done
    // only for the summary methods and not the chart display methods.

    String parentNodeId = getRequestParentNodeId( request );
    String rootLevel = request.getParameter( "rootLevel" );

    if ( StringUtils.isEmpty( rootLevel ) )
    {
      rootLevel = "";
    }

    boolean orgShowAll = false;
    List<ReportParameterInfo> reportParameterInfoList = form.getReportParameterInfoList();
    if ( reportParameterInfoList != null && reportParameterInfoList.size() > 0 )
    {
      for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
      {
        if ( reportParameterInfo.getName().equals( "parentNodeId" ) )
        {
          if ( reportParameterInfo.getValues() != null && reportParameterInfo.getValues().length > 0 )
          {
            String[] reportParams = reportParameterInfo.getValues();
            for ( int i = 0; i < reportParameterInfo.getValues().length; i++ )
            {
              orgShowAll = "show_all".equalsIgnoreCase( reportParams[i] );
            }
          }
          else
          {
            orgShowAll = "show_all".equalsIgnoreCase( reportParameterInfo.getValue() );
          }

        }
      }
    }

    if ( form.getParentNodeId() == null || ( orgShowAll || rootLevel.equalsIgnoreCase( "true" ) ) && parentNodeId == null && isSummaryMethod( form ) )
    {
      form.setParentNodeId( getTopLevelNodeId() );
    }
    form.setChangeFilterPage( false );
    form.setDashboardPage( false );

    if ( isChangeFiltersPage( request ) )
    {
      form.setChangeFilterPage( true );
      form.setPageNumber( 0 );
    }
    else if ( isDashboardPage( request ) )
    {
      form.setDashboardPage( true );
    }
  }

  @SuppressWarnings( "rawtypes" )
  protected String getRequestParentNodeId( HttpServletRequest request )
  {
    String parentNodeId = request.getParameter( PARENT_NODE_ID );
    if ( StringUtils.isBlank( parentNodeId ) )
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( !StringUtils.isBlank( clientState ) )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        Map clientStateMap = null;
        try
        {
          clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        }
        catch( InvalidClientStateException e )
        {

        }
        if ( clientStateMap != null )
        {
          if ( clientStateMap.get( "parentNodeId" ) != null )
          {
            try
            {
              parentNodeId = String.valueOf( (Long)clientStateMap.get( "parentNodeId" ) );
            }
            catch( ClassCastException cce )
            {
              parentNodeId = (String)clientStateMap.get( "parentNodeId" );
            }
          }
        }
      }
    }
    return parentNodeId;
  }

  protected boolean isSummaryMethod( ReportParametersForm form )
  {
    return form.getMethod().equals( "displaySummaryReport" );
  }

  protected boolean isDashboardPage( HttpServletRequest request )
  {
    String dashboardItemId = getDashboardItemIdFromRequest( request );
    return StringUtils.isNotEmpty( dashboardItemId );
  }

  @SuppressWarnings( "rawtypes" )
  protected String getDashboardItemIdFromRequest( HttpServletRequest request )
  {
    String dashboardItemId = request.getParameter( DASHBOARD_ITEM_ID );
    if ( StringUtils.isBlank( dashboardItemId ) )
    {
      String clientState = RequestUtils.getOptionalParamString( request, "amp;clientState" );
      if ( !StringUtils.isBlank( clientState ) )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "amp;cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        Map clientStateMap = null;
        try
        {
          clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        }
        catch( InvalidClientStateException e )
        {

        }
        if ( clientStateMap != null )
        {
          if ( clientStateMap.get( DASHBOARD_ITEM_ID ) != null )
          {
            dashboardItemId = String.valueOf( (Long)clientStateMap.get( DASHBOARD_ITEM_ID ) );
          }
        }
      }
    }
    return dashboardItemId;
  }

  protected boolean isChangeFiltersPage( HttpServletRequest request )
  {
    return request.getParameter( IS_FILTERS_PAGE ) != null;
  }

  protected List<ReportParameterInfo> populateParameterInfo( Report report, HttpServletRequest request, List<ReportParameterInfo> inputReportParameterInfoList, ReportParametersForm form )
  {
    List<ReportParameterInfo> reportParameterInfoList = null;
    if ( isDashboardPage( request ) )
    { // load favorite parameters
      Long dashboardItemId = Long.valueOf( getDashboardItemIdFromRequest( request ) );
      ReportDashboardItem reportDashboardItem = getDashboardReportsService().getUserDashboardItemById( dashboardItemId );
      Set<ReportDashboardItemParam> reportDashboardItemParams = reportDashboardItem.getReportDashboardItemParams();
      reportParameterInfoList = populateFavoriteParameters( reportDashboardItemParams, reportDashboardItem.getNodeAndBelow() );
      form.setNodeAndBelow( reportDashboardItem.getNodeAndBelow() );
      request.setAttribute( "selectedChartId", reportDashboardItem.getReportChart().getId() );
    }
    else if ( inputReportParameterInfoList != null )
    { // load parameters selected from the page
      reportParameterInfoList = inputReportParameterInfoList;
      // Bug 606
      // If the user is coming from a drill down then update the change filter parent node id.
      String reqParentNodeId = getRequestParentNodeId( request );
      if ( StringUtils.isNotBlank( reqParentNodeId ) )
      {
        for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
        {
          if ( PARENT_NODE_ID.equals( reportParameterInfo.getName() ) )
          {
            reportParameterInfo.setValue( reqParentNodeId );
          }
        }
      } // End bug 606
      else if ( isChangeFiltersPage( request ) )
      { // Post contains the checkbox value only when it is checked. The following code will reset
        // the check box value when coming from the change filters screen.
        Boolean autoUpdate = false;
        if ( request.getParameter( "autoUpdateIndex" ) != null && request.getParameter( "reportParameterInfoList[" + request.getParameter( "autoUpdateIndex" ) + "].autoUpdate" ) != null )
        {
          autoUpdate = true;
        }
        for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
        {
          if ( FILTER_TO_DATE.equals( reportParameterInfo.getName() ) )
          {
            reportParameterInfo.setAutoUpdate( autoUpdate );
          }
          // If coming from change filters screen the node and below should always be true.
          reportParameterInfo.setNodeAndBelow( Boolean.TRUE );
        }
        // Update the form as well with node and below as true.
        form.setNodeAndBelow( Boolean.TRUE );
      }
      else if ( StringUtils.isBlank( reqParentNodeId ) && isSummaryMethod( form ) )
      { // Case when the parent node id is empty and if it is a summary method then set the report
        // parameter info object with the top level node. Note - this should not be set for the
        // chart methods or detail methods

        // Summary should always have node and below set to true.
        form.setNodeAndBelow( Boolean.TRUE );

        for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
        {
          if ( PARENT_NODE_ID.equals( reportParameterInfo.getName() ) )
          {
            reportParameterInfo.setValue( String.valueOf( getTopLevelNodeId() ) );
            // Bug 2471 - Display orgname as show all when first link in the breadcrumb is clicked
            request.setAttribute( "displayNodeAsShowAll", Boolean.TRUE );
          }
        }
      }
    }
    else
    { // load default parameters
      if ( report.getReportParameters() != null )
      {
        reportParameterInfoList = populateDefaultParameters( report );
      }
      // If the request is coming from the engagement screens on the core then set the filters
      // accordingly.
      if ( "true".equals( form.getIsEngagement() ) )
      {
        for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
        {
          if ( FILTER_FROM_DATE.equals( reportParameterInfo.getName() ) )
          {
            reportParameterInfo.setValue( form.getPeriodStartDate() );
          }
          else if ( FILTER_TO_DATE.equals( reportParameterInfo.getName() ) )
          {
            reportParameterInfo.setValue( form.getPeriodEndDate() );
          }
          else if ( FILTER_PROMOTION_ID.equals( reportParameterInfo.getName() ) )
          {
            // The following will retrieve all the recognition promotions selected for the live
            // engagement promotion.
            List<FormattedValueBean> engagementEligiblePromotionList = getPromotionService().getEngagementRecognitionPromotionsList();
            String promotionIds = "";
            for ( FormattedValueBean engagementEligiblePromotion : engagementEligiblePromotionList )
            {
              if ( !StringUtils.isEmpty( promotionIds ) )
              {
                promotionIds = promotionIds + ",";
              }
              promotionIds = promotionIds + String.valueOf( engagementEligiblePromotion.getId() );
            }
            String[] values = promotionIds.split( REGEX_COMMA );
            reportParameterInfo.setValues( values );
          }
        }
      }
    }
    if ( reportParameterInfoList != null )
    {
      populateCountryRatio( reportParameterInfoList );
      displayNominationCommentAvailableFilter( reportParameterInfoList );
    }

    return reportParameterInfoList;
  }

  /**
   * When the system property is set to false this filter should not be displayed. 
   * Default is false.
   * @param reportParameterInfoList
   */
  private void displayNominationCommentAvailableFilter( List<ReportParameterInfo> reportParameterInfoList )
  {
    if ( reportParameterInfoList != null )
    {
      if ( !getSystemVariableService().getPropertyByName( SystemVariableService.NOMINATION_REPORT_COMMENT_AVAILABLE ).getBooleanVal() )
      {
        for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
        {
          if ( ReportParameterInfo.NOMINATION_COMMENT_AVAILABLE.equals( reportParameterInfo.getName() ) )
          {
            reportParameterInfoList.remove( reportParameterInfo );
            break;
          }
        }
      }
    }
  }

  protected List<ReportParameterInfo> populateDashboardParameters( HttpServletRequest request )
  {
    List<ReportParameterInfo> reportParameterInfoList;
    Long dashboardItemId = Long.valueOf( getDashboardItemIdFromRequest( request ) );
    ReportDashboardItem reportDashboardItem = getDashboardReportsService().getUserDashboardItemById( dashboardItemId );
    Set<ReportDashboardItemParam> reportDashboardItemParams = reportDashboardItem.getReportDashboardItemParams();
    reportParameterInfoList = populateFavoriteParameters( reportDashboardItemParams, reportDashboardItem.getNodeAndBelow() );
    request.setAttribute( "selectedChartId", reportDashboardItem.getReportChart().getId() );
    return reportParameterInfoList;
  }

  protected Map<String, Object> populateDashboardReportParametersMap( HttpServletRequest request )
  {
    List<ReportParameterInfo> reportParameterInfoList = populateDashboardParameters( request );
    Map<String, Object> reportParameters = new HashMap<String, Object>();
    if ( reportParameterInfoList != null )
    {
      for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
      {
        String value = reportParameterInfo.getParameterValue();
        reportParameters.put( reportParameterInfo.getName(), value );
        reportParameters.put( NODE_AND_BELOW, reportParameterInfo.getNodeAndBelow() );
      }
    }
    if ( reportParameters.get( PARENT_NODE_ID ) == null )
    {
      reportParameters.put( PARENT_NODE_ID, getTopLevelNodeId() );
    }
    else
    {
      reportParameters.put( PARENT_NODE_ID, (String)reportParameters.get( PARENT_NODE_ID ) );
    }
    return reportParameters;
  }

  private void populateCountryRatio( List<ReportParameterInfo> reportParameterInfoList )
  {
    BigDecimal conversionRatio = null;
    for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
    {
      if ( FILTER_VIEW_COUNTRY_ID.equals( reportParameterInfo.getName() ) )
      {
        if ( StringUtils.isNotBlank( reportParameterInfo.getValue() ) )
        {
          Country selectedCountry = getCountryService().getCountryById( Long.valueOf( reportParameterInfo.getValue() ) );
          final BigDecimal US_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
          final BigDecimal USER_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( selectedCountry.getCountryCode() );
          conversionRatio = BudgetUtils.calculateConversionRatio( US_MEDIA_VALUE, USER_MEDIA_VALUE );
        }
        break;
      }
    }

    if ( conversionRatio != null )
    {
      for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
      {
        if ( FILTER_COUNTRY_RATIO.equals( reportParameterInfo.getName() ) )
        {
          reportParameterInfo.setValue( conversionRatio.toPlainString() );
        }
      }
    }
  }

  // Bug 1711 - used by charts only. When coming from dashboard use dashboard pararameters only
  // instead of using the form
  protected Map<String, Object> prepareChartDisplay( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    this.prepareDisplay( mapping, form, request, response );
    Map<String, Object> reportParameters = ( (ReportParametersForm)form ).getReportParameters();
    if ( isDashboardPage( request ) )
    {
      reportParameters = populateDashboardReportParametersMap( request );
      reportParameters.put( "languageCode", UserManager.getLocale().toString() );
    }
    return reportParameters;
  }

  protected List<ReportParameterInfo> populateDefaultParameters( Report report )
  {
    List<ReportParameterInfo> reportParameterInfoList;
    reportParameterInfoList = new ArrayList<ReportParameterInfo>();
    for ( ReportParameter reportParameter : report.getReportParameters() )
    {
      if ( reportParameter != null )
      {
        ReportParameterInfo reportParameterInfo = new ReportParameterInfo();
        reportParameterInfo.setCmKey( reportParameter.getParameterCmKey() );
        reportParameterInfo.setListDefinition( reportParameter.getListDefinition() );
        reportParameterInfo.setId( reportParameter.getId() );
        reportParameterInfo.setName( reportParameter.getParameterName() );
        reportParameterInfo.setType( reportParameter.getReportParameterType().getCode() );
        reportParameterInfo.setAdminSelectOnly( reportParameter.isAdminSelectOnly() );
        reportParameterInfo.setHideShowAllOption( reportParameter.isHideShowAllOption() );
        reportParameterInfo.setAutoUpdate( Boolean.FALSE );
        reportParameterInfo.setParameterGroup( reportParameter.getParameterGroup() );
        reportParameterInfo.setShowOnDashboard( reportParameter.isDisplayOnDashboard() );
        if ( reportParameter.getCollectionName() != null )
        {
          reportParameterInfo.setCollectionName( reportParameter.getCollectionName() );
        }
        reportParameterInfo.setReportCode( reportParameter.getReport().getReportCode() );
        String value = getDefaultValue( reportParameter );
        if ( value != null && ( ReportParameterType.MULTI_SELECT_PICKLIST.equals( reportParameter.getReportParameterType().getCode() )
            || ReportParameterType.MULTI_SELECT_QUERY.equals( reportParameter.getReportParameterType().getCode() ) ) )
        {
          String[] values = value.split( REGEX_COMMA );
          reportParameterInfo.setValues( values );
        }
        else
        {
          if ( FILTER_FROM_DATE.equals( reportParameter.getParameterName() ) )
          {
            value = getDefaultFromDate();
          }
          else if ( FILTER_TO_DATE.equals( reportParameter.getParameterName() ) )
          {
            value = DateUtils.toDisplayString( DateUtils.getCurrentDate() );
            reportParameterInfo.setAutoUpdate( Boolean.TRUE );
          }
          else if ( FILTER_VIEW_COUNTRY_ID.equals( reportParameter.getParameterName() ) )
          {
            if ( getAuthorizationService().isUserInRole( "ROLE_BI_ADMIN" ) )
            {
              value = getCountryService().getCountryByCode( getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_COUNTRY ).getStringVal() ).getId().toString();
            }
            else
            {
              value = getUserService().getPrimaryUserAddress( UserManager.getUserId() ).getAddress().getCountry().getId().toString();
            }
          }
          reportParameterInfo.setValue( value );
        }
        reportParameterInfoList.add( reportParameterInfo );
      }
    }
    Collections.sort( reportParameterInfoList, new ReportParameterComparator() );
    return reportParameterInfoList;
  }

  private static String getFirstDateOfTheYear()
  {
    return DateUtils.toDisplayString( DateUtils.getFirstDayOfThisYear() );
  }

  private String getDefaultFromDate()
  {
    // check the system variable to see if date start should default to.
    // value of 0 means default to beginning of current year
    // any other value means subtract that many days from today
    int rollbackDays = getSystemVariableService().getPropertyByName( SystemVariableService.REPORT_START_DATE_ROLLBACK_DAYS ).getIntVal();

    if ( rollbackDays == 0 )
    {
      return getFirstDateOfTheYear();
    }

    Calendar calendar = GregorianCalendar.getInstance();

    calendar.setTime( new Date() );

    calendar.add( Calendar.DAY_OF_MONTH, -1 * rollbackDays );

    return DateUtils.toDisplayString( calendar.getTime() );
  }

  protected List<ReportParameterInfo> populateFavoriteParameters( Set<ReportDashboardItemParam> reportDashboardItemParams, Boolean nodeAndBelow )
  {
    List<ReportParameterInfo> reportParameterInfoList;
    reportParameterInfoList = new ArrayList<ReportParameterInfo>();
    for ( ReportDashboardItemParam reportDashboardItemParam : reportDashboardItemParams )
    {
      ReportParameter reportParameter = reportDashboardItemParam.getReportParameter();
      ReportParameterInfo reportParameterInfo = new ReportParameterInfo();
      reportParameterInfo.setCmKey( reportParameter.getParameterCmKey() );
      reportParameterInfo.setListDefinition( reportParameter.getListDefinition() );
      reportParameterInfo.setId( reportParameter.getId() );
      reportParameterInfo.setName( reportParameter.getParameterName() );
      reportParameterInfo.setAdminSelectOnly( reportParameter.isAdminSelectOnly() );
      reportParameterInfo.setHideShowAllOption( reportParameter.isHideShowAllOption() );
      reportParameterInfo.setType( reportParameter.getReportParameterType().getCode() );
      reportParameterInfo.setParameterGroup( reportParameter.getParameterGroup() );
      reportParameterInfo.setAutoUpdate( reportDashboardItemParam.getAutoUpdate() );
      reportParameterInfo.setShowOnDashboard( reportParameter.isDisplayOnDashboard() );
      reportParameterInfo.setNodeAndBelow( nodeAndBelow );
      if ( reportParameter.getCollectionName() != null )
      {
        reportParameterInfo.setCollectionName( reportParameter.getCollectionName() );
      }
      reportParameterInfo.setReportCode( reportParameter.getReport().getReportCode() );
      String value = reportDashboardItemParam.getValue();
      if ( value != null && ( ReportParameterType.MULTI_SELECT_PICKLIST.equals( reportParameter.getReportParameterType().getCode() )
          || ReportParameterType.MULTI_SELECT_QUERY.equals( reportParameter.getReportParameterType().getCode() ) ) )
      { // If multi-select then split the comma separated string and set the value as an array
        String[] values = value.split( REGEX_COMMA );
        reportParameterInfo.setValues( values );
      }
      else if ( FILTER_TO_DATE.equals( reportParameter.getParameterName() ) || FILTER_FROM_DATE.equals( reportParameter.getParameterName() ) )
      {
        reportParameterInfo.setValue( DateUtils.toDisplayString( DateUtils.toDate( value, Locale.US ) ) );
        if ( Boolean.TRUE.equals( reportDashboardItemParam.getAutoUpdate() ) )
        { // If autoUpdate is checked then set the end date to the current date
          reportParameterInfo.setValue( DateUtils.toDisplayString( DateUtils.getCurrentDate() ) );
        }
      }
      else
      {
        reportParameterInfo.setValue( value );
      }
      reportParameterInfoList.add( reportParameterInfo );
    }
    Collections.sort( reportParameterInfoList, new ReportParameterComparator() );
    return reportParameterInfoList;
  }

  public class ReportParameterComparator implements Comparator<ReportParameterInfo>
  {
    public int compare( ReportParameterInfo o1, ReportParameterInfo o2 )
    {
      return ( (Comparable<String>)o2.getParameterGroup() ).compareTo( o1.getParameterGroup() );
    }
  }

  private Report populateCharts( Report report )
  {
    Boolean plateauPlatformOnly = getSystemVariableService().getPropertyByName( SystemVariableService.PLATEAU_PLATFORM_ONLY ).getBooleanVal();

    List<ReportChart> charts = report.getCharts();
    List<ReportChart> plateauPlatformCharts = new ArrayList<ReportChart>();
    if ( plateauPlatformOnly )
    {
      if ( charts != null )
      {
        for ( Iterator<ReportChart> chartIter = charts.iterator(); chartIter.hasNext(); )
        {
          ReportChart chart = (ReportChart)chartIter.next();
          if ( chart.isIncludedInPlateau() )
          {
            plateauPlatformCharts.add( chart );
          }
        }
        report.setCharts( plateauPlatformCharts );
      }
    }
    return report;
  }

  private void populateSystemUrl( Report report )
  {
    String systemUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    report.setUrl( systemUrl + report.getUrl() );
    if ( report.getCharts() != null )
    {
      for ( int i = 0; i < report.getCharts().size(); i++ )
      {
        ReportChart chart = report.getCharts().get( i );
        if ( chart != null )
        {
          chart.setChartDataUrl( systemUrl + chart.getChartDataUrl() );
        }
      }
    }
  }

  @SuppressWarnings( "unchecked" )
  private String getTopLevelNodeId()
  {
    String parentNodeIds = "";
    // if the logged in user is a BI_ADMIN, they wont have a user node
    // record, so we should set the
    // node to the top most level so they can see all user data.

    if ( getAuthorizationService().isUserInRole( "ROLE_BI_ADMIN" ) )
    {
      Node rootNode = getNodeService().getRootNode( getHierarchyService().getPrimaryHierarchy().getId(), null );
      return rootNode.getId().toString();
    }

    List<Node> usersTopLevelNodes = getReportsService().getUsersTopLevelNodes();
    if ( usersTopLevelNodes != null && usersTopLevelNodes.size() > 0 )
    {
      for ( Node node : usersTopLevelNodes )
      {
        if ( !parentNodeIds.equals( "" ) )
        {
          parentNodeIds = parentNodeIds + "," + node.getId();
        }
        else
        {
          parentNodeIds = node.getId().toString();
        }
      }

      // return usersTopLevelNodes.get( 0 ).getId();
    }
    return parentNodeIds;
  }

  private String getDefaultValue( ReportParameter reportParameter )
  {
    String value = null;
    if ( reportParameter.getDefaultValue() != null )
    {
      if ( ReportParameterType.DATE_PICKER.equals( reportParameter.getReportParameterType().getCode() ) )
      {
        try
        {
          value = DateUtils.toDisplayString( DateUtils.toDateChecked( reportParameter.getDefaultValue() ) );
        }
        catch( ParseException e )
        {
          value = DateUtils.toDisplayString( DateUtils.getCurrentDate() );
        }
      }
      else
      {
        value = reportParameter.getDefaultValue();
      }
    }
    return value;
  }

  // ==========================
  // CHANGE FILTER VALIDATIONS
  // ==========================

  protected boolean hasErrorMessages( HttpServletRequest request, Map<String, Object> reportParameters, List<String> requiredFields, boolean isDateFilterOptional )
  {
    boolean hasMessages = false;
    if ( isChangeFiltersPage( request ) )
    {
      ActionMessages errors = new ActionMessages();
      if ( !isDateFilterOptional )
      {
        if ( isFilterEmpty( FILTER_FROM_DATE, reportParameters ) )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "report.errors.FROM_DATE_REQUIRED" ) );
        }

        if ( isFilterEmpty( FILTER_TO_DATE, reportParameters ) )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "report.errors.END_DATE_REQUIRED" ) );
        }
      }

      if ( !isFilterEmpty( FILTER_FROM_DATE, reportParameters ) && !isFilterEmpty( FILTER_TO_DATE, reportParameters ) )
      {
        Date fromDate = DateUtils.toDate( (String)reportParameters.get( FILTER_FROM_DATE ) );
        Date toDate = DateUtils.toDate( (String)reportParameters.get( FILTER_TO_DATE ) );
        if ( fromDate.after( toDate ) )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "report.errors.TO_DATE_AFTER_FROM_DATE" ) );
        }
      }

      if ( requiredFields != null )
      {
        for ( String field : requiredFields )
        {
          if ( field.equals( FILTER_PROMOTION_ID ) && isFilterEmpty( field, reportParameters ) )
          {
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "report.errors.PROMOTION_REQUIRED" ) );
          }
          if ( field.equals( FILTER_PAX_ID ) && StringUtils.isBlank( request.getParameter( FILTER_PAX_ID ) ) )
          {
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "report.errors.PARTICIPANT_REQUIRED" ) );
          }
          if ( field.equals( FILTER_VIEW_COUNTRY_ID ) && isFilterEmpty( field, reportParameters ) )
          {
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "report.errors.VIEW_COUNTRY_ID_REQUIRED" ) );
          }
          if ( field.equals( FILTER_SELECT_MONTH ) && isFilterEmpty( field, reportParameters ) )
          {
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "report.errors.MONTH_REQUIRED" ) );
          }
        }
      }
      if ( errors.size() > 0 )
      {
        saveErrors( request, errors );
        hasMessages = true;
      }
    }
    return hasMessages;
  }

  private boolean isFilterEmpty( String fieldName, Map<String, Object> reportParameters )
  {
    if ( reportParameters.get( fieldName ) == null || ( (String)reportParameters.get( fieldName ) ).length() == 0 )
    {
      return true;
    }
    return false;
  }

  // ==================================
  // EXTRACT REPORT METHODS
  // ==================================

  protected abstract String getReportExtractCmAssetCode();

  protected abstract String getExtractReportData( Map<String, Object> extractParameters );

  protected abstract ReportBeanMethod getReportBeanMethod();

  protected ReportBeanMethod getSecondaryReportBeanMethod()
  {
    return null;
  }

  protected ReportBeanMethod getSecondaryLevelReportBeanMethod()
  {
    return null;
  }

  protected abstract String[] getColumnHeaders();

  protected String[] getColumnHeaders( Map<String, Object> extractParameters )
  {
    return getColumnHeaders();
  }

  protected String[] getAwardColumnHeaders( Map<String, Object> extractParameters )
  {
    return AwardsReportAction.getColumnHeaders( (String)extractParameters.get( "awardType" ) );
  }

  protected ReportBeanMethod getReportBeanMethod( Map<String, Object> extractParameters )
  {
    return getReportBeanMethod();
  }

  protected ReportBeanMethod getSecondaryLevelReportBeanMethod( Map<String, Object> extractParameters )
  {
    return getSecondaryLevelReportBeanMethod();
  }

  protected ReportBeanMethod getSecondaryReportBeanMethod( Map<String, Object> extractParameters )
  {
    return getSecondaryReportBeanMethod();
  }

  protected String getSecondaryExtractReportData( Map<String, Object> extractParameters )
  {
    return "";
  }

  protected String getBudgetSecondLevelExtractReportData( Map<String, Object> extractParameters )
  {
    return "";
  }

  protected String getBudgetThirdLevelExtractReportData( Map<String, Object> extractParameters )
  {
    return "";
  }

  public ActionForward extractReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    populateForm( reportParametersForm, request );
    String filename = getExtractFileName( reportParametersForm );

    Map<String, Object> extractParameters = reportParametersForm.getReportParameters();

    // Bug 944
    if ( request.getParameter( PARAM_EXPORT_LEVEL ) != null )
    {
      extractParameters.put( PARAM_EXPORT_LEVEL, request.getParameter( PARAM_EXPORT_LEVEL ) );
    }
    if ( request.getParameter( EXPORT_VIEW_TYPE ) != null )
    {
      extractParameters.put( EXPORT_VIEW_TYPE, request.getParameter( EXPORT_VIEW_TYPE ) );
    }

    Boolean node = (Boolean)extractParameters.get( "nodeAndBelow" );

    if ( request.getParameter( PARAM_EXPORT_LEVEL ) == null && request.getParameter( EXPORT_VIEW_TYPE ) == null )
    {
      if ( node )
      {
        extractParameters.put( "nodeAndBelow", true );
      }
      if ( request.getParameter( EXTRACT_FULL_VIEW ) != null )
      {
        extractParameters.put( "parentNodeId", reportParametersForm.getRootParentNodeId() );
      }
    }
    // large audience or standard?
    if ( !isLargeAudienceReport() )
    {
      processStandardExtract( response, filename, extractParameters );
    }
    else
    {
    	/*WIP 20160 customization start */
        processLargeAudienceExtract( filename, extractParameters, false, request );
        /*WIP 20160 customization end */
      writeAsJsonToResponse( buildModal( false ), response );
    }
    return null;
  }

  protected void processStandardExtract( HttpServletResponse response, String filename, Map<String, Object> extractParameters ) throws Exception
  {
    writeHeader( response, filename );
    String content = getExtractReportData( extractParameters );
    writeContent( content, response );
  }

  /*WIP 20160 customization start */
  protected void processLargeAudienceExtract( String filename, Map<String, Object> extractParameters, boolean isSecondaryExtract, HttpServletRequest request)
  {
    launchReportExtract( extractParameters, filename, isSecondaryExtract , request );
  }

  protected void processLargeAudienceExtract( String filename, Map<String, Object> extractParameters, HttpServletRequest request )
  {
    launchReportExtract( extractParameters, filename , request );
  }
  /*WIP 20160 customization end */
  
  protected WebErrorMessageList buildModal( boolean isSecondaryReport )
  {

    WebErrorMessageList messages = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage();
    message = WebErrorMessage.addServerCmd( message );
    message.setCommand( WebResponseConstants.RESPONSE_COMMAND_MODAL );
    message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
    if ( getReportCode() != null && ( getReportCode().charAt( 0 ) + getReportCode().substring( 1 ) ).equals( Report.BUDGET_BALANCE ) && isSecondaryReport )
    {
      message.setText( CmsResourceBundle.getCmsBundle().getString( "report.extract.confirm.YOUR" ) + " " + CmsResourceBundle.getCmsBundle().getString( getSecondaryReportAssetName() ) + " "
          + CmsResourceBundle.getCmsBundle().getString( "report.extract.confirm.PROCESSING_TEXT1" ) );
    }
    else
    {
      message.setText( CmsResourceBundle.getCmsBundle().getString( "report.extract.confirm.YOUR" ) + " " + CmsResourceBundle.getCmsBundle().getString( getReportAssetName() ) + " "
          + CmsResourceBundle.getCmsBundle().getString( "report.extract.confirm.PROCESSING_TEXT1" ) );
    }
    messages.getMessages().add( message );
    return messages;
  }

  protected String getReportAssetName()
  {
    Report report = getReportsService().getReportByCode( getReportCode() );
    return report.getCmAssetCode() + "." + report.getName();
  }

  protected String getSecondaryReportAssetName()
  {
    Report report = getReportsService().getReportByCode( getReportCode() );
    return report.getCmAssetCode() + ".SECONDARY_REPORT_NAME";
  }

  protected String buildReportContent( final String fileName, final Map<String, Object> extractParameters, final HttpServletRequest request, final HttpServletResponse response )
  {
    final String key = "reportContent_" + fileName; // unique key for this request

    final long systemPingTime = getSystemVariableService().getPropertyByName( "reportExportPingTime" ).getLongVal();
    final boolean useCookie = systemPingTime % 2 != 0;

    // ok, if there is already a report being generated, don't continue!
    if ( null != request.getSession().getAttribute( key ) )
    {
      return null;
    }

    List<Callable<String>> callables = new ArrayList<Callable<String>>();

    // add the keep-alive thread
    callables.add( new Callable<String>()
    {
      public String call() throws Exception
      {
        long pingTime = systemPingTime;
        if ( useCookie )
        {
          pingTime = pingTime - 1;
        }

        localLog.warn( ".........Initial Ping for extract, System Ping Time set to " + systemPingTime );
        localLog.warn( ".........Use Cookie Flag set to " + useCookie );
        localLog.warn( ".........Actual Ping Time to use is " + pingTime );
        // ok, this part is scary, because if the other thread generating the report
        // doesn't return or something, this will keep going for EVAR!!!!
        long time = 0;

        while ( request.getAttribute( key ) == null )
        {
          Thread.sleep( 250 );// wake up and check every quarter seconds
          // ok, lets only dump a space out every so often to keep things alive
          time = time + 250;
          if ( time % pingTime == 0 )// spit something out every x minutes...
          {
            localLog.warn( ".........Pinging to keep long running report extract alive" );
            if ( useCookie )
            {
              response.addCookie( new Cookie( "test1", "test2" ) );
            }
            else
            {
              response.getWriter().print( " " );
            }
            response.flushBuffer();
            time = 0;
          }
        }
        return null;
      }
    } );
    // add the actual worker thread
    callables.add( CallableFactory.createCallable( new Callable<String>()
    {
      public String call() throws Exception
      {
        try
        {
          String value = getExtractReportData( extractParameters );
          request.setAttribute( key, "DONE" );
          return value;
        }
        catch( Exception e )
        {
          throw e;
        }
        finally
        {
          // notify the other thread to stop
          request.setAttribute( key, "DONE" );
        }
      }
    } ) );
    try
    {
      // set a termination timeout
      ExecutorService executorService = getExecutorService();
      // limit the length of time the keep-alive can operate
      List<Future<String>> threadResults = executorService.invokeAll( callables, 11, TimeUnit.MINUTES );// limit
      for ( Future<String> future : threadResults )
      {
        if ( null != future.get() )
        {
          return future.get();
        }
      }
    }
    catch( Throwable t )
    {
      t.printStackTrace();
    }
    finally
    {
      // remove sesion variable
      request.getSession().removeAttribute( key );
    }

    return null;
  }

  protected boolean isLargeAudienceReport()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.ENABLE_LARGE_AUDIENCE_REPORT_GENERATION ).getBooleanVal();
  }

  protected boolean isPlateauOnlyPlatform()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.PLATEAU_PLATFORM_ONLY ).getBooleanVal();
  }

  protected ExecutorService getExecutorService()
  {
    return (ExecutorService)BeanLocator.getBean( "executorService" );
  }

  public ActionForward extractSecondaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    populateForm( reportParametersForm, request );

    Map<String, Object> extractParameters = reportParametersForm.getReportParameters();
    extractParameters.put( "budgetSegmentId", reportParametersForm.getBudgetSegmentId() );

    // large audience or standard?
    if ( !isLargeAudienceReport() )
    {
      writeHeader( response, getSecondExtractFileName( reportParametersForm ) );

      String content = getSecondaryExtractReportData( extractParameters );

      writeContent( content, response );
    }
    else
    {
    	/*WIP 20160 customization start */
        processLargeAudienceExtract( getSecondExtractFileName( reportParametersForm ), extractParameters, true, request );
        /*WIP 20160 customization end */
      writeAsJsonToResponse( buildModal( true ), response );
    }

    return null;
  }

  public ActionForward extractBudgetSecondLevelReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    populateForm( reportParametersForm, request );

    Map<String, Object> extractParameters = reportParametersForm.getReportParameters();
    extractParameters.put( "budgetSegmentId", reportParametersForm.getBudgetSegmentId() );
    // large audience or standard?
    if ( !isLargeAudienceReport() )
    {
      writeHeader( response, getBudgetSecondLevelExtractFileName( reportParametersForm ) );

      String content = getBudgetSecondLevelExtractReportData( extractParameters );

      writeContent( content, response );
    }
    else
    {
    	/*WIP 20160 customization start */
        processLargeAudienceExtract( getBudgetSecondLevelExtractFileName( reportParametersForm ), extractParameters, request );
        /*WIP 20160 customization end */
      writeAsJsonToResponse( buildModal( true ), response );
    }

    return null;
  }

  public ActionForward extractBudgetThirdLevelReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    populateForm( reportParametersForm, request );

    Map<String, Object> extractParameters = reportParametersForm.getReportParameters();

    // large audience or standard?
    if ( !isLargeAudienceReport() )
    {
      writeHeader( response, getBudgetThirdLevelExtractFileName( reportParametersForm ) );

      String content = getBudgetThirdLevelExtractReportData( extractParameters );

      writeContent( content, response );
    }
    else
    {
   	 /*WIP 20160 customization start */
        processLargeAudienceExtract( getBudgetThirdLevelExtractFileName( reportParametersForm ), extractParameters, request );
        /*WIP 20160 customization end */
        writeAsJsonToResponse( buildModal( true ), response );
    }

    return null;
  }

  public void writeHeader( HttpServletResponse response, String fileName )
  {
    response.setContentType( "text/csv; charset=UTF-8" );
    response.setCharacterEncoding( "UTF-8" );
    response.setHeader( "Pragma", "public" );
    response.setHeader( "Cache-Control", "max-age=0" );
    response.setHeader( "Content-disposition", "attachment; filename=" + fileName );
    try
    {
      response.flushBuffer();
    }
    catch( Exception ex )
    {
      localLog.error( ex );
    }
  }

  public void writeContent( String content, HttpServletResponse response ) throws Exception
  {
    response.getOutputStream().write( new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF } );
    response.getOutputStream().write( content.getBytes( Charset.forName( "UTF-8" ) ) );
  }

  protected String getExtractFileName( ReportParametersForm reportParametersForm )
  {
    return Character.toUpperCase( getReportCode().charAt( 0 ) ) + getReportCode().substring( 1 ) + DateUtils.toDisplayString( DateUtils.getCurrentDate() ) + ".csv";
  }

  protected String getSecondExtractFileName( ReportParametersForm reportParametersForm )
  {
    return "";
  }

  protected String getBudgetSecondLevelExtractFileName( ReportParametersForm reportParametersForm )
  {
    return "";
  }

  protected String getBudgetThirdLevelExtractFileName( ReportParametersForm reportParametersForm )
  {
    return "";
  }

  protected StringBuffer buildCSVColumnNames( StringBuffer contentBuf, String[] columnCMKeys )
  {
    Locale locale = UserManager.getLocale();
    CMAssetService service = getCMAssetService();
    if ( columnCMKeys != null )
    {
      for ( int i = 0; i < columnCMKeys.length; i++ )
      {
        if ( i != 0 )
        {
          contentBuf.append( REGEX_COMMA );
        }
        String columnDesc = service.getString( getReportExtractCmAssetCode(), columnCMKeys[i], locale, false );
        contentBuf.append( columnDesc );
      }
      contentBuf.append( NEW_LINE );
    }
    return contentBuf;
  }

  protected StringBuffer buildCSVColumnNamesForHappinessPulse( StringBuffer contentBuf, String[] columnCMKeys, List<String> timeFrame )
  {
    Locale locale = UserManager.getLocale();
    CMAssetService service = getCMAssetService();
    int monthIndex = 2;
    String month = null;
    if ( columnCMKeys != null )
    {
      for ( int i = 0; i < columnCMKeys.length; i++ )
      {
        if ( i != 0 )
        {
          contentBuf.append( REGEX_COMMA );
        }
        String columnDesc = service.getString( getReportExtractCmAssetCode(), columnCMKeys[i], locale, false );
        if ( i != 0 && i != columnCMKeys.length - 1 )
        {
          if ( i % 3 == 1 )
          {
            month = timeFrame.get( monthIndex );
            monthIndex--;
          }
          columnDesc = month + " - " + columnDesc;
        }
        contentBuf.append( columnDesc );
      }
      contentBuf.append( NEW_LINE );
    }
    return contentBuf;
  }

  protected StringBuffer buildCSVColumnNamesForSurvey( StringBuffer contentBuf, String[] columnCMKeys, List surveyResponseList )
  {
    Locale locale = UserManager.getLocale();
    CMAssetService service = getCMAssetService();
    for ( int i = 0; i < columnCMKeys.length; i++ )
    {
      if ( i != 0 )
      {
        contentBuf.append( REGEX_COMMA );
      }
      String columnDesc = "";

      if ( i < 5 || i > 5 && columnCMKeys[i] != null && ( columnCMKeys[i].equals( "MEAN" ) || columnCMKeys[i].equals( "STD_DEVIATION" ) ) )
      {
        columnDesc = service.getString( getReportExtractCmAssetCode(), columnCMKeys[i], locale, false );
        contentBuf.append( columnDesc );
      }
      else
      {
        Iterator iter = surveyResponseList.iterator();
        if ( iter.hasNext() )
        {
          SurveyAnalysisHeaderListReportValue reportValue = (SurveyAnalysisHeaderListReportValue)iter.next();

          columnDesc = reportValue.getHeaderValue();
          contentBuf.append( columnDesc );
          contentBuf.append( REGEX_COMMA );
          contentBuf.append( columnDesc + " %" );
          surveyResponseList.remove( reportValue );
        }
      }
    }
    contentBuf.append( NEW_LINE );
    return contentBuf;
  }

  @SuppressWarnings( "rawtypes" )
  protected void buildCSVExtractContent( StringBuffer contentBuf, Map output )
  {
    List results = (List)output.get( OUTPUT_RESULT_SET );

    if ( GOOD.equals( output.get( OUTPUT_RETURN_CODE ) ) && null != results && results.size() > 1 )
    {
      for ( int i = 1; i < results.size(); i++ )
      {
        contentBuf.append( results.get( i ) ).append( NEW_LINE );
      }
    }
  }

  protected String buildCSVColumnHeaders( String[] columnCMKeys )
  {
    Locale locale = UserManager.getLocale();
    CMAssetService service = getCMAssetService();
    StringBuffer csHeaders = new StringBuffer();

    if ( columnCMKeys != null )
    {
      for ( int i = 0; i < columnCMKeys.length; i++ )
      {
        if ( i != 0 )
        {
          csHeaders.append( REGEX_COMMA );
        }
        String columnDesc = service.getString( getReportExtractCmAssetCode(), columnCMKeys[i], locale, false );
        csHeaders.append( columnDesc );
      }
    }
    return csHeaders.toString();
  }

  protected void launchReportExtract( Map<String, Object> reportParameters, String userFileName, boolean isSecondaryExtract ,HttpServletRequest request )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "genericReportExtractProcess", GenericReportExtractProcess.BEAN_NAME );
    LinkedHashMap<String, Object> parameterMap = new LinkedHashMap<String, Object>();
    String secondaryExtarctBeanMethod = null;
    /*WIP 20160 customization start */
    Long userId = getUserId(request);
    /*WIP 20160 customization end */
    reportParameters.put( "countryRatio", countryMediaValue() );
    if ( isSecondaryExtract )
    {
      secondaryExtarctBeanMethod = getSecondaryReportBeanMethod( reportParameters ).getMethodName();
      if ( secondaryExtarctBeanMethod != null && ( secondaryExtarctBeanMethod.equals( POINTS_BUDGET_ISSUANCE_EXTRACT ) || secondaryExtarctBeanMethod.equals( CASH_BUDGET_ISSUANCE_EXTRACT ) ) )
      {
        addColumnBudgetIssuanceHeaders( reportParameters );
      }
      else if ( secondaryExtarctBeanMethod != null && secondaryExtarctBeanMethod.equals( SURVEY_OPEN_ENDED_EXTRACT ) )
      {
        addColumnSecondarySurveyHeaders( reportParameters );
      }
      else if ( secondaryExtarctBeanMethod != null && secondaryExtarctBeanMethod.equals( NOMINATION_SUMMARY_EXTRACT ) )
      {
        addColumnSecondarySummayHeaders( reportParameters );
      }
      else if ( secondaryExtarctBeanMethod != null && secondaryExtarctBeanMethod.equals( NOMINATION_SUMMARY_REPORT ) )
      {
        addColumnNominationSecondarySummaryHeaders( reportParameters );
      }
    }
    else if ( getReportBeanMethod( reportParameters ).getMethodName().equals( SURVEY_ANALYSIS_EXTRACT ) )
    {
      addSurveyColumnHeaders( reportParameters );
    }
    else if ( getReportBeanMethod( reportParameters ).getMethodName().equals( AWARD_ACTIVITY_EXTRACT ) )
    {
      addAwardColumnHeaders( reportParameters );
      buildOnTheSpotReportParameter( reportParameters );
    }
    else
    {
      addColumnHeaders( reportParameters );
    }

    if ( isSecondaryExtract )
    {
      parameterMap.put( "beanName", new String[] { getSecondaryReportBeanMethod( reportParameters ).getBeanName() } );
      parameterMap.put( "methodName", new String[] { getSecondaryReportBeanMethod( reportParameters ).getMethodName() } );
    }
    else
    {
      parameterMap.put( "beanName", new String[] { getReportBeanMethod( reportParameters ).getBeanName() } );
      parameterMap.put( "methodName", new String[] { getReportBeanMethod( reportParameters ).getMethodName() } );
    }
    /*WIP 20160 customization start */
    parameterMap.put( "userId", new String[] { userId.toString() } );
    /*WIP 20160 customization end */
    parameterMap.put( "userFilename", new String[] { userFileName } );
    parameterMap.put( "reportParameters", reportParameters );
    /*WIP 20160 customization start */
    getProcessService().launchProcess( process, parameterMap, userId );
    /*WIP 20160 customization end */
  }

  protected void launchReportExtract( Map<String, Object> reportParameters, String userFileName,HttpServletRequest request )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "genericReportExtractProcess", GenericReportExtractProcess.BEAN_NAME );
    LinkedHashMap<String, Object> parameterMap = new LinkedHashMap<String, Object>();

    /*WIP 20160 customization start */
    Long userId = getUserId(request);
    /*WIP 20160 customization end */

    reportParameters.put( "countryRatio", countryMediaValue() );
    addColumnBudgetSecondLevelHeaders( reportParameters );
    if ( reportParameters.get( "extractType" ).equals( "secondary" ) )
    {
      parameterMap.put( "beanName", new String[] { getSecondaryLevelReportBeanMethod( reportParameters ).getBeanName() } );
      parameterMap.put( "methodName", new String[] { getSecondaryLevelReportBeanMethod( reportParameters ).getMethodName() } );
    }
    else
    {
      parameterMap.put( "beanName", new String[] { getReportBeanMethod( reportParameters ).getBeanName() } );
      parameterMap.put( "methodName", new String[] { getReportBeanMethod( reportParameters ).getMethodName() } );
    }
    /*WIP 20160 customization start */
    parameterMap.put( "userId", new String[] { userId.toString() } );
    /*WIP 20160 customization end */
    //parameterMap.put( "userId", new String[] { UserManager.getUserId().toString() } );
    parameterMap.put( "userFilename", new String[] { userFileName } );
    parameterMap.put( "reportParameters", reportParameters );
    /*WIP 20160 customization start */
    getProcessService().launchProcess( process, parameterMap,  userId);
    /*WIP 20160 customization end */
    //getProcessService().launchProcess( process, parameterMap, UserManager.getUserId() );
  }

  protected void addColumnHeaders( Map<String, Object> reportParameters )
  {
    reportParameters.put( "csHeaders", buildCSVColumnHeaders( getColumnHeaders( reportParameters ) ) );
  }

  protected void addAwardColumnHeaders( Map<String, Object> reportParameters )
  {
    reportParameters.put( "csHeaders", buildCSVColumnHeaders( getAwardColumnHeaders( reportParameters ) ) );
  }

  protected void addSurveyColumnHeaders( Map<String, Object> reportParameters )
  {
    if ( (String)reportParameters.get( "promotionId" ) != null )
    {
      List<SurveyAnalysisHeaderListReportValue> surveyResponseList = new ArrayList<SurveyAnalysisHeaderListReportValue>();

      Map<String, Object> output = getSurveyReportsService().getSurveyResponseList( reportParameters );
      surveyResponseList = (List<SurveyAnalysisHeaderListReportValue>)output.get( OUTPUT_DATA );
      String[] constantString = new String[surveyResponseList.size() + 7];
      constantString[0] = "ORG_UNIT";
      constantString[1] = "QUESTIONS_ASKED";
      constantString[2] = "ELIGIBLE_PAX";
      constantString[3] = "TOTAL_RESPONSE";
      constantString[4] = "TOTAL_RESPONSE_PERC";
      constantString[constantString.length - 2] = "MEAN";
      constantString[constantString.length - 1] = "STD_DEVIATION";

      reportParameters.put( "csHeadersForSurvey", buildCSVColumnNamesForSurvey( new StringBuffer(), constantString, surveyResponseList ) );
    }
  }

  protected void addColumnBudgetIssuanceHeaders( Map<String, Object> reportParameters )
  {
    reportParameters.put( "csHeaders", buildCSVColumnHeaders( getColumnBudgetIssuanceHeaders() ) );
  }

  protected void addColumnBudgetSecondLevelHeaders( Map<String, Object> reportParameters )
  {
    reportParameters.put( "csHeaders", buildCSVColumnHeaders( getColumnBudgetSecondLevelHeaders() ) );
  }

  protected void addColumnSecondarySurveyHeaders( Map<String, Object> reportParameters )
  {
    reportParameters.put( "csHeaders", buildCSVColumnHeaders( getColumnSecondarySurveyHeaders() ) );
  }

  protected String[] getColumnBudgetIssuanceHeaders()
  {
    return BudgetReportAction.EXTRACT_ISSUANCE_COLUMN_NAMES;
  }

  protected String[] getColumnBudgetSecondLevelHeaders()
  {
    return BudgetReportAction.EXTRACT_BUDGET_SECOND_LEVEL_COLUMN_NAMES;
  }

  protected String[] getColumnSecondarySurveyHeaders()
  {
    return SurveyAnalysisReportAction.EXTRACT_OPEN_ENDED_COLUMN_NAMES;
  }

  protected void buildOnTheSpotReportParameter( Map<String, Object> reportParameters )
  {
    String promotionId = (String)reportParameters.get( "promotionId" );
    String awardType = (String)reportParameters.get( "awardType" );

    String onTheSpotIncluded = "Y";

    if ( promotionId != null && !promotionId.equals( "0" ) )
    {
      // If other promotions are selected and on the spot is not selected then make onTheSpot
      // paramter as "N"
      onTheSpotIncluded = "N";
      String[] promotionIds = promotionId.split( "," );
      for ( String promotionItem : promotionIds )
      {
        // Is onTheSpot promotion selected
        // For on the spot the promotion id is equal to -1
        if ( promotionItem.equals( "-1" ) )
        {
          onTheSpotIncluded = "Y";
        }
      }
    }
    else if ( awardType.equalsIgnoreCase( "other" ) )
    {
      onTheSpotIncluded = "N";
    }
    reportParameters.put( "promotionId", promotionId );
    reportParameters.put( "onTheSpotIncluded", onTheSpotIncluded );
  }

  protected String getRefreshDate( String categoryCode )
  {
    return DateUtils.toDisplayTimeWithMeridiemString( getReportsService().getReportDate( categoryCode ) );
  }

  protected String formatReportValue( String input )
  {
    if ( input != null )
    {
      input = input.replaceAll( REGEX_COMMA, " " );
    }
    else
    {
      input = "";
    }
    return input + REGEX_COMMA;
  }

  /*WIP 20160 customization start */
  public Long getUserId(HttpServletRequest request)
  {
	Boolean launchUser = (Boolean)request.getSession().getAttribute("launchUser");
	Long userId = null; 
	    if(launchUser != null && launchUser.booleanValue())
	    {
	       userId = (Long)request.getSession().getAttribute("adminUserId");

	    }
	    else
	    {
	    	userId = UserManager.getUserId();
	    }
    return userId;
  }
  /*WIP 20160 customization end */

  protected String formatReportValue( int input )
  {
    return String.valueOf( input ) + REGEX_COMMA;
  }

  protected String formatReportValue( Long input )
  {
    return String.valueOf( input ) + REGEX_COMMA;
  }

  protected String formatReportValue( double input )
  {
    return String.valueOf( input ) + REGEX_COMMA;
  }

  protected String formatReportValue( BigDecimal input )
  {
    return String.valueOf( input ) + REGEX_COMMA;
  }

  protected String formatReportValue( Date input )
  {
    return DateUtils.toDisplayString( input ) + REGEX_COMMA;
  }

  protected ReportsService getReportsService()
  {
    return (ReportsService)getService( ReportsService.BEAN_NAME );
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  protected CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

  protected DashboardReportsService getDashboardReportsService()
  {
    return (DashboardReportsService)getService( DashboardReportsService.BEAN_NAME );
  }

  protected AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }

  protected NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  protected HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  protected UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  protected CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  protected ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

  protected SurveyReportsService getSurveyReportsService()
  {
    return (SurveyReportsService)getService( SurveyReportsService.BEAN_NAME );
  }

  protected WorkHappierReportsService getWorkHappierReportsService()
  {
    return (WorkHappierReportsService)getService( WorkHappierReportsService.BEAN_NAME );
  }

  protected PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  protected Float countryMediaValue()
  {
    Long userId = UserManager.getUserId();
    BigDecimal budgetMediaValue = getUserService().getBudgetMediaValueByUserId( userId );
    return budgetMediaValue.floatValue();
  }
  


  //-------------------------------
  // Client customization for WIP #27192 start
  //-------------------------------  
  protected Long getFirstBudgetUpHierarchy(Long nodeId)
  {	 
	  while(!isBudgetExist(nodeId))
	  {
		Node node = getNodeService().getNodeById(nodeId);		  
		nodeId= node.getParentNode().getId();
	  }
	  return nodeId;
  }
  
  protected boolean isBudgetExist(Long nodeId)
  {
	    //Look for active budget count when trying to find one in the higher hierarchy levels
	  int budgetCount = getBudgetReportsService().getActiveBudgetForNodebyId( nodeId );
	  if(budgetCount >0)
	  {
		  return true;
	  }
	  else
	  {
		  return false;		  
	  }

  }
  protected BudgetReportsService getBudgetReportsService()
  {
    return (BudgetReportsService)getService( BudgetReportsService.BEAN_NAME );
  }
  
  protected void addColumnSecondarySummayHeaders( Map<String, Object> reportParameters )
  {
    reportParameters.put( "csHeaders", buildCSVColumnHeaders( getColumnSecondarySummaryHeaders() ) );
  }

  protected String[] getColumnSecondarySummaryHeaders()
  {
    return NominationAgingReportAction.EXTRACT_SUMMARY_COLUMN_NAMES;
  }

  protected void addColumnNominationSecondarySummaryHeaders( Map<String, Object> reportParameters )
  {
    String contentHeader = buildCSVColumnHeaders( getColumnNominationSecondarySummaryHeaders() );
    String regex = "{0}";
    String currencyCode = "(" + UserManager.getUserPrimaryCountryCurrencyCode() + ")";
    reportParameters.put( "csHeaders", contentHeader.toString().replace( regex, currencyCode ) );
  }

  protected String[] getColumnNominationSecondarySummaryHeaders()
  {
    return NominationReportAction.EXTRACT_SUMMARY_COLUMN_NAMES;
  }

}
