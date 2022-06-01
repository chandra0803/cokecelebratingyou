
package com.biperf.core.ui.reports.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ReportParameterType;
import com.biperf.core.domain.report.ReportDashboard;
import com.biperf.core.domain.report.ReportDashboardItem;
import com.biperf.core.domain.report.ReportDashboardItemParam;
import com.biperf.core.domain.report.ReportParameter;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.reports.ReportController;
import com.biperf.core.ui.reports.ReportParameterInfo;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.ReportParameterValueBean;

public class DashboardReportController extends ReportController
{

  private static final String REGEX_COMMA = ",";
  private static final String FILTER_TO_DATE = "toDate";
  private static final String FILTER_FROM_DATE = "fromDate";

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    String systemUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    request.setAttribute( "systemUrl", systemUrl );
    if ( request.getAttribute( "reportData" ) != null )
    {
      ReportDashboard reportDashBoard = (ReportDashboard)request.getAttribute( "reportData" );
      if ( reportDashBoard.getReportDashboardItems() != null )
      {
        List<String> favList = new ArrayList<String>();
        for ( int i = 0; i < reportDashBoard.getReportDashboardItems().size(); i++ )
        {
          ReportDashboardItem reportDashboardItem = reportDashBoard.getReportDashboardItems().get( i );
          String searchCriteria = null;
          if ( reportDashboardItem != null )
          {
            List<ReportParameterInfo> reportParameterInfoList = populateFavoriteParameters( reportDashboardItem.getReportDashboardItemParams() );
            populateReportParameters( request, reportParameterInfoList );
            List<ReportParameterValueBean> criteriaList = populateSearchCriteria( request, reportParameterInfoList );
            searchCriteria = buildCriteriaString( criteriaList );
          }
          favList.add( searchCriteria );
        }
        request.setAttribute( "searchCriteria", favList );
      }
    }
  }

  private String buildCriteriaString( List<ReportParameterValueBean> criteriaList )
  {
    String criteria = "";
    for ( int i = 0; i < criteriaList.size(); i++ )
    {
      ReportParameterValueBean valueBean = criteriaList.get( i );
      if ( valueBean.isDisplayOnDashboard() )
      {
        if ( criteria.isEmpty() )
        {
          criteria = valueBean.getLabel() + ": " + valueBean.getValue();
        }
        else
        {
          criteria = criteria + ", " + valueBean.getLabel() + ": " + valueBean.getValue();
        }
      }
    }
    return criteria;
  }

  protected List<ReportParameterInfo> populateFavoriteParameters( Set<ReportDashboardItemParam> reportDashboardItemParams )
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
      if ( reportParameter.getCollectionName() != null )
      {
        reportParameterInfo.setCollectionName( reportParameter.getCollectionName() );
      }
      reportParameterInfo.setReportCode( reportParameter.getReport().getReportCode() );
      String value = reportDashboardItemParam.getValue();
      if ( value != null && ( ReportParameterType.MULTI_SELECT_PICKLIST.equals( reportParameter.getReportParameterType().getCode() )
          || ReportParameterType.MULTI_SELECT_QUERY.equals( reportParameter.getReportParameterType().getCode() ) ) )
      {
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
    return reportParameterInfoList;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}
