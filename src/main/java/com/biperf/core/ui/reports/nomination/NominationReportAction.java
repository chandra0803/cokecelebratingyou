/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/nomination/NominationReportAction.java,v $
 *
 */

package com.biperf.core.ui.reports.nomination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.GivenType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.reports.NominationReportsService;
import com.biperf.core.service.reports.ReportsConstants;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.nomination.NominationReportValue;

public abstract class NominationReportAction extends BaseReportsAction
{
  protected static final String[] EXTRACT_ALL_COLUMN_NAMES = { "CLAIM_ID",
                                                               "DATE_SUBMITTED",
                                                               "PROMOTION_NAME",
                                                               "CURRENT_APPROVER_LEVEL",
                                                               "STATUS",
                                                               "PREV_LVL_APPROVED_DATE",
                                                               "PREV_LVL_APPROVER_LOGIN_ID",
                                                               "PREV_LVL_APPROVER_NAME",
                                                               "NOMINATOR_LOGIN_ID",
                                                               "NOMINATOR_FIRST_NAME",
                                                               "NOMINATOR_MIDDLE_NAME",
                                                               "NOMINATOR_LAST_NAME",
                                                               "NOMINATOR_COUNTRY",
                                                               "BEHAVIOR",
                                                               "POINTS_RECEIVED",
                                                               "CASH_RECEIVED",
                                                               "OTHER_AWARD_DESCRIPTION",
                                                               "OTHER_QUANTITY_RECEIVED",
                                                               "OTHER_AWARD_VALUE",
                                                               "NOMINATOR_PRIMARY_ORG_UNIT",
                                                               "NOMINATOR_PRIMARY_ORG_UNIT_ROLE",
                                                               "NOMINATOR_DEPARTMENT",
                                                               "NOMINATOR_JOB_POSITION",
                                                               "NOMINEE_USER_ID",
                                                               "NOMINEE_FIRST_NAME",
                                                               "NOMINEE_LAST_NAME",
                                                               "TEAM_NAME",
                                                               "NOMINEE_COUNTRY",
                                                               "NOMINEE_PRIMARY_ORG_UNIT",
                                                               "NOMINEE_PRIMARY_ORG_UNIT_ROLE",
                                                               "NOMINEE_DEPARTMENT",
                                                               "NOMINEE_JOB_POSITION",
                                                               "PAX_CHAR1",
                                                               "PAX_CHAR2",
                                                               "PAX_CHAR3",
                                                               "PAX_CHAR4",
                                                               "PAX_CHAR5",
                                                               "PAX_CHAR6",
                                                               "PAX_CHAR7",
                                                               "PAX_CHAR8",
                                                               "PAX_CHAR9",
                                                               "PAX_CHAR10",
                                                               "PAX_CHAR11",
                                                               "PAX_CHAR12",
                                                               "PAX_CHAR13",
                                                               "PAX_CHAR14",
                                                               "PAX_CHAR15",
 															  "PAX_CHAR16",
 															  "PAX_CHAR17",
 															  "PAX_CHAR18",
 															  "PAX_CHAR19",
 															  "PAX_CHAR20",
 															  "PAX_CHAR21",
 															  "PAX_CHAR22",
 															  "PAX_CHAR23",
 															  "PAX_CHAR24",
 															  "PAX_CHAR25",
 															  "PAX_CHAR26",
 															  "PAX_CHAR27",
 															  "PAX_CHAR28",
 															  "PAX_CHAR29",
 															  "PAX_CHAR30",
 															  "PAX_CHAR31",
 															  "PAX_CHAR32",
 															  "PAX_CHAR33",
 															  "PAX_CHAR34",
 															  "PAX_CHAR35",
                                                               "COMMENTS",
                                                               "CUSTOM_FORM_FIELDS",
                                                               "MORE_INFO_COMMENTS" };

  public static final String[] EXTRACT_SUMMARY_COLUMN_NAMES = { "ORG_NAME",
                                                                "ELIGIBLE_NOMINATORS",
                                                                "ACTUAL_NOMINATORS",
                                                                "PERCENT_ELIGIBLE_NOMINATORS",
                                                                "ELIGIBLE_NOMINEES",
                                                                "ACTUAL_NOMINEES",
                                                                "PERCENT_ELIGIBLE_NOMINEES",
                                                                "NOMINATIONS_SUBMITTED",
                                                                "NOMINATIONS_FOR_NOMINEES",
                                                                "NOMINATIONS_FOR_NOMINATORS",
                                                                "POINTS_RECEIVED",
                                                                "CASH_RECEIVED",
                                                                "OTHER_QUANTITY_RECEIVED",
                                                                "OTHER_AWARD_VALUE" };
  
  protected static final String[] EXTRACT_ALL_COLUMNS_WITH_COMMENTS = { "DATE_SUBMITTED",
          "STATUS",
          "DATE_APPROVED",
          "FINAL_APPROVER",
          "PROMOTION_NAME",
          "BEHAVIOR",
          "NOMINATOR_FIRST_NAME",
          "NOMINATOR_MIDDLE_NAME",
          "NOMINATOR_LAST_NAME",
          "NOMINATOR_USER_NAME",
          "NOMINATOR_COUNTRY",
          "NOMINATOR_PRIMARY_ORG_UNIT",
          "NOMINATOR_JOB_POSITION",
          "NOMINATOR_DEPARTMENT",
          "NOMINEE_FIRST_NAME",
          "NOMINEE_MIDDLE_NAME",
          "NOMINEE_LAST_NAME",
          "NOMINEE_LOGIN_ID",
          "NOMINEE_COUNTRY",
          "NOMINEE_PRIMARY_ORG_UNIT",
          "NOMINEE_JOB_POSITION",
          "NOMINEE_DEPARTMENT",
          "POINTS",
          "PAX_CHAR1",
          "PAX_CHAR2",
          "PAX_CHAR3",
          "PAX_CHAR4",
          "PAX_CHAR5",
          "PAX_CHAR6",
          "PAX_CHAR7",
          "PAX_CHAR8",
          "PAX_CHAR9",
          "PAX_CHAR10",
          "PAX_CHAR11",
          "PAX_CHAR12",
          "PAX_CHAR13",
          "PAX_CHAR14",
          "PAX_CHAR15",
		  "PAX_CHAR16",
		  "PAX_CHAR17",
		  "PAX_CHAR18",
		  "PAX_CHAR19",
		  "PAX_CHAR20",
		  "PAX_CHAR21",
		  "PAX_CHAR22",
		  "PAX_CHAR23",
		  "PAX_CHAR24",
		  "PAX_CHAR25",
		  "PAX_CHAR26",
		  "PAX_CHAR27",
		  "PAX_CHAR28",
		  "PAX_CHAR29",
		  "PAX_CHAR30",
		  "PAX_CHAR31",
		  "PAX_CHAR32",
		  "PAX_CHAR33",
		  "PAX_CHAR34",
		  "PAX_CHAR35",
          "COMMENTS" };


  protected NominationReportsService getNominationReportsService()
  {
    return (NominationReportsService)getService( NominationReportsService.BEAN_NAME );
  }

  @Override
  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.nomination.extract";
  }

  /**
   * Subclasses may override this. Defaults to false.
   * @return True if the extract is for nominators (i.e. list of nominators)
   */
  protected Boolean includeNotGiven()
  {
    return Boolean.FALSE;
  }

  protected Boolean hasReceived()
  {
    return Boolean.FALSE;
  }

  protected Boolean hasGiven()
  {
    return Boolean.FALSE;
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    boolean commentsRequired = false;
    if ( null != reportParameters.get( "givenType" ) )
    {
      reportParameters.put( "includeNotGiven", GivenType.HAVE_NOT.equals( reportParameters.get( "givenType" ) ) );
    }
    else
    {
      reportParameters.put( "includeNotGiven", includeNotGiven() );
    }
    reportParameters.put( "hasReceived", hasReceived() );
    reportParameters.put( "hasGiven", hasGiven() );
    Map reportExtractData = getNominationReportsService().getNominationExtractResults( reportParameters );
    buildCSVColumnNames( contentBuf, getColumnHeaders( reportParameters ) );

    if ( reportParameters.get( "nomCommentAvailable" ) != null )
    {
      if ( reportParameters.get( "nomCommentAvailable" ).equals( "yes" ) )
      {
        commentsRequired = true;
      }
    }

    if ( !commentsRequired )
    {
      String subString = "Nominator Comments";
      String subString2 = "Custom Form Field(s)";

      while ( true )
      {
        int pos = contentBuf.indexOf( subString );
        if ( pos < 0 )
        {
          break; // ready
        }
        contentBuf.delete( pos, pos + subString.length() + 1 );
      }
      while ( true )
      {
        int pos1 = contentBuf.indexOf( subString2 );
        if ( pos1 < 0 )
        {
          break;
        }
        contentBuf.delete( pos1, pos1 + subString2.length() );
      }
    }
    /*
     * For adding currency label to extract file we have to add unique regex value in cm key value
     * So, that we can replace that unique regex value with country currency code.
     */
    String regex = "{0}";
    String currencyCode = "(" + UserManager.getUserPrimaryCountryCurrencyCode() + ")";
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString().replace( regex, currencyCode );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  protected String getSecondaryExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map reportExtractData = getNominationReportsService().getNominationSummaryExtractResults( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_SUMMARY_COLUMN_NAMES );

    String regex = "{0}";
    String currencyCode = "(" + UserManager.getUserPrimaryCountryCurrencyCode() + ")";
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString().replace( regex, currencyCode );
  }

  @Override
  protected String getSecondExtractFileName( ReportParametersForm reportParametersForm )
  {
    return "NominationSummary" + DateUtils.toDisplayString( DateUtils.getCurrentDate() ) + ".csv";
  }
  /*WIP 20160 customization start */
  @Override
  protected void launchReportExtract( Map<String, Object> reportParameters, String userFileName, boolean isSecondaryExtract ,HttpServletRequest request )
  {
    reportParameters.put( "includeNotGiven", includeNotGiven() );
    reportParameters.put( "hasGiven", hasGiven() );
    reportParameters.put( "hasReceived", hasReceived() );
    super.launchReportExtract( reportParameters, userFileName, false , request );
  }
  /*WIP 20160 customization end */
  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.NOMINATION_REPORT;
  }

  @Override
  protected ReportBeanMethod getSecondaryReportBeanMethod()
  {
    return ReportBeanMethod.NOMINATION_SUMMARY_REPORT;
  }

  protected void displayNomitionsReceivedNomineesList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean nodeAndBelow )
      throws ServiceErrorException
  {
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;

    super.prepareDisplay( mapping, form, request, response );
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    Integer maxRows = 0;

    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      Map<String, Object> output = getNominationReportsService().getNominationReceiverTabularResults( reportParameters, nodeAndBelow );
      List<NominationReportValue> reportData = (List<NominationReportValue>)output.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      if ( reportData != null && reportData.size() > 0 )
      {
        maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords().intValue();
      }

      if ( isLastPage( maxRows, reportParameters ) )
      {
        NominationReportValue totalsRowData = (NominationReportValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      String currencyCode = "(" + UserManager.getUserPrimaryCountryCurrencyCode() + ")";
      request.setAttribute( "currencyCode", currencyCode );
      request.setAttribute( "maxRows", maxRows );
    }
  }

  protected void displayNomitionsNomineeList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean nodeAndBelow ) throws ServiceErrorException
  {
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;

    super.prepareDisplay( mapping, form, request, response );
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    Integer maxRows = 0;
    Map<String, Object> clientStateParameters = new HashMap<String, Object>();
    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      try
      {
        clientStateParameters = ClientStateSerializer.deserialize( clientState, password );
      }
      catch( InvalidClientStateException e )
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      Long userId;
      try
      {
        userId = (Long)clientStateParameters.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        userId = Long.valueOf( clientStateParameters.get( "userId" ).toString() );
      }
      reportParameters.put( "userId", userId );
      if ( userId != null )
      {
        request.setAttribute( "userId", userId );
      }
    }
    if ( reportParametersForm.getDrilldownPromoId() != null )
    {
      reportParameters.put( "promotionId", reportParametersForm.getDrilldownPromoId() );
    }
    else
    {
      reportParameters.put( "promotionId", clientStateParameters.get( "drilldownPromoId" ) );
    }
    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      Map<String, Object> output = getNominationReportsService().getNominationNomineeTabularResults( reportParameters, nodeAndBelow );
      List<NominationReportValue> reportData = (List<NominationReportValue>)output.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      if ( reportData != null && reportData.size() > 0 )
      {
        maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords().intValue();
      }

      if ( isLastPage( maxRows, reportParameters ) )
      {
        NominationReportValue totalsRowData = (NominationReportValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      String currencyCode = "(" + UserManager.getUserPrimaryCountryCurrencyCode() + ")";
      request.setAttribute( "currencyCode", currencyCode );
      request.setAttribute( "maxRows", maxRows );
    }
  }

  protected void displayNomitionGiverNominatorsList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, boolean nodeAndBelow )
      throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    if ( !isHaveNotFilterSelected( reportParameters, request ) )
    {
      Integer maxRows = 0;
      if ( !hasErrorMessages( request, reportParameters, null, false ) )
      {
        Map<String, Object> output = getNominationReportsService().getNominationGiverNominatorsListTabularResults( reportParameters, nodeAndBelow );
        List<NominationReportValue> reportData = (List<NominationReportValue>)output.get( OUTPUT_DATA );

        if ( reportData != null && reportData.size() > 0 )
        {
          maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords().intValue();
        }

        if ( isLastPage( maxRows, reportParameters ) )
        {
          NominationReportValue totalsRowData = (NominationReportValue)output.get( OUTPUT_TOTALS_DATA );
          request.setAttribute( "totalsRowData", totalsRowData );
        }

        String currencyCode = "(" + UserManager.getUserPrimaryCountryCurrencyCode() + ")";
        request.setAttribute( "currencyCode", currencyCode );
        request.setAttribute( "maxRows", maxRows );
        request.setAttribute( "reportData", reportData );
      }
    }
  }

  @SuppressWarnings( "unchecked" )
  protected ActionForward displayNomitionGiverNominationsList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    if ( !isHaveNotFilterSelected( reportParameters, request ) )
    {
      Integer maxRows = 0;

      Map<String, Object> clientStateParameters = new HashMap<String, Object>();
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( StringUtils.isNotBlank( clientState ) )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        clientStateParameters = ClientStateSerializer.deserialize( clientState, password );

        Long userId;
        try
        {
          userId = (Long)clientStateParameters.get( "giverUserId" );
        }
        catch( ClassCastException cce )
        {
          userId = Long.valueOf( clientStateParameters.get( "giverUserId" ).toString() );
        }
        reportParameters.put( "userId", userId );
        if ( userId != null )
        {
          request.setAttribute( "userId", userId );
        }
      }

      if ( reportParametersForm.getDrilldownPromoId() != null )
      {
        reportParameters.put( "promotionId", reportParametersForm.getDrilldownPromoId() );
      }
      else
      {
        reportParameters.put( "promotionId", clientStateParameters.get( "drilldownPromoId" ) );
      }

      Map<String, Object> output = getNominationReportsService().getNominationGiverNominationsListTabularResults( reportParameters );

      List<NominationReportValue> reportData = (List<NominationReportValue>)output.get( OUTPUT_DATA );

      for ( NominationReportValue data : reportData )
      {
        if ( data.getClaimApprovalStatus().equals( "pend" ) )
        {
          data.setClaimApprovalStatus( ReportsConstants.PENDING );
        }
      }

      if ( reportData != null && reportData.size() > 0 )
      {
        maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords().intValue();
      }

      if ( isLastPage( maxRows, reportParameters ) )
      {
        NominationReportValue totalsRowData = (NominationReportValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "drilldownPromoId", reportParametersForm.getDrilldownPromoId() );

      reportParametersForm.setDrilldownPromoId( null );

      String currencyCode = "(" + UserManager.getUserPrimaryCountryCurrencyCode() + ")";
      request.setAttribute( "currencyCode", currencyCode );
      request.setAttribute( "maxRows", maxRows );
      request.setAttribute( "reportData", reportData );
    }
    return null;
  }

  // ===============
  // GIVEN CHARTS
  // ===============
  public ActionForward displayTotalNominationsGivenChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getNominationReportsService().getTotalNominationsChartResults( reportParameters );
    List<NominationReportValue> reportData = (List<NominationReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_total_nominations_report" );
  }

  public ActionForward displayNominationsGivenPointsIssuedChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getNominationReportsService().getPointsIssuedByPromotionChartResults( reportParameters );
    List<NominationReportValue> reportData = (List<NominationReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_points_issued_report" );
  }

  public ActionForward displayTotalNominationsGivenByPromotionChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getNominationReportsService().getTotalNominationsByPromotionChartResults( reportParameters );
    List<NominationReportValue> reportData = (List<NominationReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_nominations_by_promotion_report" );
  }

  public boolean isHaveNotFilterSelected( Map<String, Object> reportParameters, HttpServletRequest request )
  {
    boolean isHaveNotFilterSelected = false;
    if ( GivenType.HAVE_NOT.equals( reportParameters.get( "givenType" ) ) )
    {
      isHaveNotFilterSelected = true;
      request.setAttribute( "displayHaveNotFilterExportMessage", isHaveNotFilterSelected );
      request.setAttribute( "hideCharts", Boolean.TRUE );
      request.setAttribute( "maxRows", 1 );
    }
    return isHaveNotFilterSelected;
  }

}
