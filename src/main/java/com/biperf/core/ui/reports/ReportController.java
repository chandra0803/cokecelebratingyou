/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/ReportController.java,v $
 *
 */

package com.biperf.core.ui.reports;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.DynaPickListFactory;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.ReportDisplayType;
import com.biperf.core.domain.enums.ReportParameterType;
import com.biperf.core.domain.report.Report;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.reports.ReportsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.ReportParameterValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * ReportsController <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Aug 15, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author drahn
 *
 */
public class ReportController extends BaseController
{
  private static final String SHOW_ALL = "show_all";
  private static final String SHOW_ALL_CM_KEY = "SHOW_ALL";

  /**
   * Fetches generic data for Report Display pages
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   * @throws ClassNotFoundException 
   * @throws NoSuchMethodException 
   * @throws SecurityException 
   * @throws InvocationTargetException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ReportParametersForm form = (ReportParametersForm)request.getSession().getAttribute( ReportParametersForm.FORM_NAME );

    String systemUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    request.setAttribute( "systemUrl", systemUrl );
    request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
    // Bug 50911 Enable this when the chart issue is fixed. Prabu emailed fusion charts and waiting
    // for a response to fix the number format issue in charts.
    // request.setAttribute( "LOCALE_FOR_CHART", UserManager.getLocale() );
    request.setAttribute( "LOCALE_FOR_REPORT", UserManager.getLocale() );
    // request.setAttribute( "isLeaf", isLeaf );
    request.setAttribute( "displayTypeList", ReportDisplayType.getList() );

    populateReportParameters( request, form.getReportParameterInfoList() );
    List<ReportParameterValueBean> searchCriteriaList = populateSearchCriteria( request, form.getReportParameterInfoList() );
    request.setAttribute( "searchCriteria", searchCriteriaList );

    if ( searchCriteriaList != null && !searchCriteriaList.isEmpty() )
    {
      ReportParameterValueBean searchValueBean = searchCriteriaList.get( 0 );
      if ( searchValueBean.getLabel().equalsIgnoreCase( CmsResourceBundle.getCmsBundle().getString( "report.parameters", "DATES" ) )
          || searchValueBean.getLabel().equalsIgnoreCase( CmsResourceBundle.getCmsBundle().getString( "report.parameters", "SELECT_MONTH" ) ) )
      {
        request.setAttribute( "showDates", true );
      }
    }

    request.setAttribute( "onTheSpotAvailable", getSystemVariableService().getPropertyByName( SystemVariableService.AWARDBANQ_CONVERTCERT_IS_USED ).getBooleanVal() );
  }

  protected void populateReportParameters( HttpServletRequest request, List<ReportParameterInfo> reportParameterInfoList )
  {
    if ( reportParameterInfoList != null )
    {
      for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
      {
        if ( ReportParameterType.SELECT_QUERY.equals( reportParameterInfo.getType() ) || ReportParameterType.MULTI_SELECT_QUERY.equals( reportParameterInfo.getType() ) )
        {
          getQueryCollection( request, reportParameterInfo );
        }
        else if ( ReportParameterType.SELECT_PICKLIST.equals( reportParameterInfo.getType() ) )
        {
          if ( ( reportParameterInfo.getReportCode().equals( "awardsByOrg" ) || reportParameterInfo.getReportCode().equals( "awardsByPax" ) ) && reportParameterInfo.getName().equals( "awardType" ) )
          {
            getAwardTypePickListCollection( request, reportParameterInfo );
          }
          else
          {
            getPickListCollection( request, reportParameterInfo );
          }

        }
        else if ( ReportParameterType.MULTI_SELECT_PICKLIST.equals( reportParameterInfo.getType() ) )
        {
          getPickListCollection( request, reportParameterInfo );
        }
      }
    }
  }

  @SuppressWarnings( { "unchecked" } )
  protected List<DynaPickListType> getAwardTypePickListCollection( HttpServletRequest request, ReportParameterInfo reportParameterInfo )
  {
    List<DynaPickListType> dynaPickList = DynaPickListFactory.getPickList( reportParameterInfo.getListDefinition() );
    List<DynaPickListType> selectList = new ArrayList<DynaPickListType>();
    for ( DynaPickListType dynaPick : dynaPickList )
    {
      if ( !dynaPick.getCode().equals( "none" ) && !dynaPick.getCode().equals( "travelaward" ) )
      {
        selectList.add( dynaPick );
      }
    }
    request.setAttribute( reportParameterInfo.getCollectionName(), selectList );
    return selectList;
  }

  @SuppressWarnings( { "unchecked" } )
  protected List<DynaPickListType> getPickListCollection( HttpServletRequest request, ReportParameterInfo reportParameterInfo )
  {
    List<DynaPickListType> selectList = DynaPickListFactory.getPickList( reportParameterInfo.getListDefinition() );
    request.setAttribute( reportParameterInfo.getCollectionName(), selectList );
    return selectList;
  }

  @SuppressWarnings( { "unchecked" } )
  protected List<FormattedValueBean> getQueryCollection( HttpServletRequest request, ReportParameterInfo reportParameterInfo )
  {
    List<FormattedValueBean> selectList = null;
    // For organization and promotion the named query needs a parameter - userId
    if ( "organizationList".equals( reportParameterInfo.getCollectionName() )
        || reportParameterInfo.getReportCode().equalsIgnoreCase( Report.SURVEY_ANALYSIS ) && "promotionTypeList".equals( reportParameterInfo.getCollectionName() ) )
    {
      Long userId = UserManager.getUser().getUserId();
      selectList = getReportsService().getValuesFromNamedQuery( reportParameterInfo.getListDefinition(), userId );
    }
    else if ( "countryList".equals( reportParameterInfo.getCollectionName() ) || "roundNumberTypeList".equals( reportParameterInfo.getCollectionName() ) )
    {
      String locale = UserManager.getLocale().toString();
      selectList = getReportsService().getValuesFromNamedQuery( reportParameterInfo.getListDefinition(), locale );
    }
    else
    {
      selectList = getReportsService().getValuesFromNamedQuery( reportParameterInfo.getListDefinition(), null );
    }
    if ( !"monthTypeList".equals( reportParameterInfo.getCollectionName() ) )
    {
      Collections.sort( selectList, new QueryListComparator() );
    }
    request.setAttribute( reportParameterInfo.getCollectionName(), selectList );
    return selectList;
  }

  public class QueryListComparator implements Comparator<FormattedValueBean>
  {
    public int compare( FormattedValueBean o1, FormattedValueBean o2 )
    {
      return ( (Comparable<String>)o1.getValue().toLowerCase() ).compareTo( o2.getValue().toLowerCase() );
    }
  }

  protected List<ReportParameterValueBean> populateSearchCriteria( HttpServletRequest request, List<ReportParameterInfo> reportParameterInfoList )
  {
    List<ReportParameterValueBean> criteria = new ArrayList<ReportParameterValueBean>();
    if ( reportParameterInfoList != null )
    {
      Locale locale = UserManager.getLocale();
      String fromDate = null;
      String endDate = null;
      for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
      {
        if ( reportParameterInfo != null && reportParameterInfo.getCmKey() != null )
        {
          String value = extractValue( request, reportParameterInfo );

          if ( ReportParameterType.DATE_PICKER.equals( reportParameterInfo.getType() ) )
          {
            if ( reportParameterInfo.getName().equals( "fromDate" ) )
            {
              fromDate = value;
            }
            else if ( reportParameterInfo.getName().equals( "toDate" ) )
            {
              endDate = value;
            }
          }
          else
          {
            String label = getCMAssetService().getString( "report.parameters", reportParameterInfo.getCmKey(), locale, false );
            // Bug 2471 - Display orgname as show all when first link in the breadcrumb is clicked
            if ( StringUtil.isEmpty( value ) || "parentNodeId".equals( reportParameterInfo.getName() ) && request.getAttribute( "displayNodeAsShowAll" ) != null )
            {
              reportParameterInfo.setValue( null );
              value = getCMAssetService().getString( "report.parameters", SHOW_ALL_CM_KEY, locale, false );
            }
            // Do not display paxId or any parameter that is marked as hidden in DB
            if ( !reportParameterInfo.getType().equals( ReportParameterType.HIDDEN ) )
            {
              criteria.add( new ReportParameterValueBean( label, value, reportParameterInfo.isShowOnDashboard() ) );
            }
          }
        }
      }
      if ( fromDate != null )
      { // Add the Date filter in the top of the list
        criteria.add( 0, new ReportParameterValueBean( CmsResourceBundle.getCmsBundle().getString( "report.parameters", "DATES" ), fromDate + " - " + endDate, true ) );
      }
    }
    return criteria;
  }

  protected String extractValue( HttpServletRequest request, ReportParameterInfo reportParameterInfo )
  {
    String value = null;
    if ( reportParameterInfo.getValues() != null && reportParameterInfo.getValues().length > 0 )
    { // For multi select tags
      value = getArrayParamValue( request, reportParameterInfo );
    }
    else
    { // for other fields
      value = getParameterValueDescription( request, reportParameterInfo, reportParameterInfo.getValue() );
    }
    return value;
  }

  protected String getArrayParamValue( HttpServletRequest request, ReportParameterInfo reportParameterInfo )
  {
    String value = null;
    for ( String arrValue : reportParameterInfo.getValues() )
    {
      arrValue = getParameterValueDescription( request, reportParameterInfo, arrValue );
      if ( arrValue != null )
      {
        if ( value != null )
        {
          value = value + ", " + arrValue;
        }
        else
        {
          value = arrValue;
        }
      }
    }
    return value;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  private String getParameterValueDescription( HttpServletRequest request, ReportParameterInfo reportParameterInfo, String inputValue )
  {
    String value = null;
    if ( inputValue != null && !SHOW_ALL.equals( inputValue ) )
    {
      if ( ReportParameterType.SELECT_PICKLIST.equals( reportParameterInfo.getType() ) || ReportParameterType.MULTI_SELECT_PICKLIST.equals( reportParameterInfo.getType() ) )
      {
        List<DynaPickListType> coll = (List)request.getAttribute( reportParameterInfo.getCollectionName() );
        if ( coll == null )
        {
          // when the picklist is null re-load the collection
          coll = getPickListCollection( request, reportParameterInfo );
          request.setAttribute( reportParameterInfo.getCollectionName(), coll );
        }

        for ( DynaPickListType pickListType : coll )
        {
          if ( inputValue.equals( pickListType.getCode() ) )
          {
            value = pickListType.getName();
          }
        }
      }
      else if ( ReportParameterType.SELECT_QUERY.equals( reportParameterInfo.getType() ) || ReportParameterType.MULTI_SELECT_QUERY.equals( reportParameterInfo.getType() ) )
      {
        List<FormattedValueBean> coll = (List)request.getAttribute( reportParameterInfo.getCollectionName() );
        if ( coll == null )
        {
          // when the picklist is null re-load the collection
          coll = getQueryCollection( request, reportParameterInfo );
          request.setAttribute( reportParameterInfo.getCollectionName(), coll );
        }

        if ( !StringUtil.isEmpty( inputValue ) )
        {
          try
          {
            if ( reportParameterInfo.getCollectionName() != null && reportParameterInfo.getCollectionName().equals( "awardLevelTypeList" ) )
            {
              value = inputValue;
            }
            else if ( reportParameterInfo.getCollectionName() != null && reportParameterInfo.getCollectionName().equals( "monthTypeList" ) )
            {
              if ( inputValue.equals( "happy" ) )
              {
                value = coll.get( 0 ).getValue();
                reportParameterInfo.setValue( coll.get( 0 ).getId().toString() );
              }
              else
              {
                Long inputLong = new Long( inputValue );
                for ( FormattedValueBean valueBean : coll )
                {
                  if ( inputLong.equals( valueBean.getId() ) )
                  {
                    value = valueBean.getValue();
                  }
                }
              }
            }
            else
            {
              Long inputLong = new Long( inputValue );
              for ( FormattedValueBean valueBean : coll )
              {
                if ( inputLong.equals( valueBean.getId() ) )
                {
                  value = valueBean.getValue();
                }
              }
            }
          }
          catch( NumberFormatException nfe )
          {
            value = SHOW_ALL;
          }
        }
      }
      else
      {
        value = reportParameterInfo.getValue();
      }
    }
    return value;
  }

  protected ReportsService getReportsService()
  {
    return (ReportsService)getService( ReportsService.BEAN_NAME );
  }

  protected NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  protected CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }
}
