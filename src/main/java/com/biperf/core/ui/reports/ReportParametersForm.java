/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/ReportParametersForm.java,v $
 *
 *
 */

package com.biperf.core.ui.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.UserManager;

/**
 * ReportParametersForm
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Aug 14, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author drahn
 */
public class ReportParametersForm extends BaseForm
{
  private static final long serialVersionUID = 6540419150264946144L;

  public static final String FORM_NAME = "reportParametersForm";

  private static final String SELECT_MONTH = "selectMonth";

  private List<ReportParameterInfo> reportParameterInfoList;

  // user selected params
  private String parentNodeId;
  private Long paxId;
  private Long userId;
  private String drilldownPromoId;
  private String displayType;
  private Boolean clearForm;
  private Long quizId;
  private Long qqId;
  private Long budgetId;
  private Long budgetSegmentId;
  private String department;

  // system generated params
  private int pageNumber;
  private int sortedOn;
  private String sortedBy;
  private int resultsPerPage;
  private String method;
  private String exportViewType;
  private String exportViewName;
  private String extractType;
  private boolean isChangeFilterPage;
  private boolean isDashboardPage;
  private boolean nodeAndBelow = true;
  // Used for individual report
  private String paxFirstName;
  private String paxLastName;
  private Long managerUserId;
  private String avatarUrl;

  // Engagement params
  private String isEngagement;
  private String periodStartDate;
  private String periodEndDate;
  private Long engagementPromotionId;

  private String rootParentNodeId;

  /**
   * Clear user populated parameters.  Need to save fields which are passed on from front-end code.
   */
  public void clearForm()
  {
    parentNodeId = null;
    paxId = null;
    userId = null;
    drilldownPromoId = null;
    displayType = null;
    clearForm = null;
    reportParameterInfoList = null;
    nodeAndBelow = true;
    paxFirstName = null;
    paxLastName = null;
    budgetId = null;
    managerUserId = null;
    rootParentNodeId = null;
  }

  public String getSearchCriteria()
  {
    String criteria = "";
    if ( reportParameterInfoList != null )
    {
      for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
      {
        String name = reportParameterInfo.getName();
        String value = reportParameterInfo.getParameterValue();
        if ( value != null )
        {
          if ( criteria.length() != 0 )
          {
            criteria = criteria + ", ";
          }
          criteria = criteria + name + " " + value;
        }
      }
    }
    return criteria;
  }

  /**
   * Build a Map of all report parameters.
   * 
   * @return Map
   * @throws ServiceErrorException
   */
  public Map<String, Object> getReportParameters()
  {
    Map<String, Object> reportParameters = new HashMap<String, Object>();
    if ( reportParameterInfoList != null )
    {
      for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
      {
        String value = reportParameterInfo.getParameterValue();
        if ( reportParameterInfo.getName() != null && reportParameterInfo.getName().equals( SELECT_MONTH ) )
        {
          value = getFormattedSelectMonth( value );
        }
        reportParameters.put( reportParameterInfo.getName(), value );
      }
    }

    if ( isChangeFilterPage || isDashboardPage )
    {
      if ( !StringUtils.isEmpty( (String)reportParameters.get( "parentNodeId" ) ) )
      {
        // this.parentNodeId = Long.parseLong( (String)reportParameters.get( "parentNodeId" ) );
        this.parentNodeId = (String)reportParameters.get( "parentNodeId" );
      }
    }

    if ( isChangeFilterPage )
    {
      this.sortedBy = "asc";
    }
    // ok - this appears to be the best location to thwart the SQL Injection
    if ( null != this.sortedBy && ( !this.sortedBy.equalsIgnoreCase( "asc" ) && !this.sortedBy.equalsIgnoreCase( "desc" ) ) )
    {
      this.sortedBy = "asc";
    }

    reportParameters.put( "sortedBy", sortedBy );

    if ( rootParentNodeId == null || rootParentNodeId.equals( parentNodeId ) )
    {
      rootParentNodeId = parentNodeId;
    }
    reportParameters.put( "rootParentNodeId", rootParentNodeId );
    reportParameters.put( "parentNodeId", parentNodeId );
    reportParameters.put( "paxId", getPaxId() );
    reportParameters.put( "displayType", displayType );
    reportParameters.put( "pageNumber", pageNumber );
    reportParameters.put( "sortedOn", sortedOn );
    reportParameters.put( "languageCode", getLanguageCode() );
    reportParameters.put( "resultsPerPage", getResultsPerPage() );
    reportParameters.put( "userId", getUserId() );
    reportParameters.put( "quizId", getQuizId() );
    reportParameters.put( "qqId", getQqId() );
    reportParameters.put( "managerUserId", getManagerUserId() );
    reportParameters.put( "nodeAndBelow", isNodeAndBelow() );
    addRowNumbers( reportParameters );

    /* These two are used for only budget report */
    reportParameters.put( "filterPromotionId", reportParameters.get( "promotionId" ) );
    reportParameters.put( "drilldownPromoId", drilldownPromoId );

    if ( reportParameters.get( "promotionId" ) == null )
    {
      reportParameters.put( "promotionId", drilldownPromoId );
    }
    reportParameters.put( "budgetId", budgetId );

    reportParameters.put( "extractType", extractType );

    /* Below line is overriding the department values making it to null. So removing it */
    // reportParameters.put("department", department);
    /* this parameter is used to show currency label on cash budget balance report */
    reportParameters.put( "currencyCode", "( " + UserManager.getUserPrimaryCountryCurrencyCode() + " )" );
    return reportParameters;
  }

  private void addRowNumbers( Map<String, Object> reportParameters )
  {
    int rowNumStart = 0;
    int maxResultsPerPage = Report.MAX_ROWS_TO_DISPLAY;
    if ( resultsPerPage > 0 )
    {
      maxResultsPerPage = resultsPerPage;
    }
    if ( pageNumber > 1 )
    {
      rowNumStart = ( pageNumber - 1 ) * maxResultsPerPage;
    }
    int rowNumEnd = rowNumStart + maxResultsPerPage + 1;
    reportParameters.put( "rowNumStart", rowNumStart );
    reportParameters.put( "rowNumEnd", rowNumEnd );
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param anActionMapping
   * @param aRequest
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping anActionMapping, HttpServletRequest aRequest )
  {

    ActionErrors actionErrors = super.validate( anActionMapping, aRequest );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    return actionErrors;
  }

  public String getParentNodeId()
  {
    return parentNodeId;
  }

  public void setParentNodeId( String parentNodeId )
  {
    this.parentNodeId = parentNodeId;
  }

  public void setPaxId( Long paxId )
  {
    this.paxId = paxId;
  }

  public Long getPaxId()
  {
    return paxId;
  }

  public Long getQuizId()
  {
    return quizId;
  }

  public void setQuizId( Long quizId )
  {
    this.quizId = quizId;
  }

  public Long getQqId()
  {
    return qqId;
  }

  public void setQqId( Long qqId )
  {
    this.qqId = qqId;
  }

  public void setDrilldownPromoId( String drilldownPromoId )
  {
    this.drilldownPromoId = drilldownPromoId;
  }

  public String getDrilldownPromoId()
  {
    return drilldownPromoId;
  }

  public void setDisplayType( String displayType )
  {
    this.displayType = displayType;
  }

  public String getDisplayType()
  {
    return displayType;
  }

  public int getPageNumber()
  {
    return pageNumber;
  }

  public void setPageNumber( int pageNumber )
  {
    this.pageNumber = pageNumber;
  }

  public String getLanguageCode()
  {
    return UserManager.getLocale().toString();
  }

  public int getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( int sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public int getResultsPerPage()
  {
    return resultsPerPage;
  }

  public void setResultsPerPage( int resultsPerPage )
  {
    this.resultsPerPage = resultsPerPage;
  }

  public List<ReportParameterInfo> getReportParameterInfoList()
  {
    return reportParameterInfoList;
  }

  public void setReportParameterInfoList( List<ReportParameterInfo> reportParameterInfoList )
  {
    this.reportParameterInfoList = reportParameterInfoList;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getManagerUserId()
  {
    return managerUserId;
  }

  public void setManagerUserId( Long managerUserId )
  {
    this.managerUserId = managerUserId;
  }

  public String getIsEngagement()
  {
    return isEngagement;
  }

  public void setIsEngagement( String isEngagement )
  {
    this.isEngagement = isEngagement;
  }

  public String getPeriodStartDate()
  {
    return periodStartDate;
  }

  public void setPeriodStartDate( String periodStartDate )
  {
    this.periodStartDate = periodStartDate;
  }

  public String getPeriodEndDate()
  {
    return periodEndDate;
  }

  public void setPeriodEndDate( String periodEndDate )
  {
    this.periodEndDate = periodEndDate;
  }

  public Long getEngagementPromotionId()
  {
    return engagementPromotionId;
  }

  public void setEngagementPromotionId( Long engagementPromotionId )
  {
    this.engagementPromotionId = engagementPromotionId;
  }

  public Long getPromotionIdAsLong()
  {
    Object promotionId = getReportParameters().get( "promotionId" );
    if ( promotionId instanceof String )
    {
      String promoId = (String)promotionId;
      return StringUtils.isBlank( promoId ) || promoId.contains( "," ) ? null : Long.valueOf( promoId );
    }
    else if ( promotionId instanceof Long )
    {
      return (Long)promotionId;
    }
    return null;
  }

  public String getPromotionId()
  {
    return getReportParameters().get( "promotionId" ).toString();
  }

  public String getExportViewType()
  {
    return exportViewType;
  }

  public void setExportViewType( String exportViewType )
  {
    this.exportViewType = exportViewType;
  }

  public String getExportViewName()
  {
    return exportViewName;
  }

  public void setExportViewName( String exportViewName )
  {
    this.exportViewName = exportViewName;
  }

  public void setClearForm( Boolean clearForm )
  {
    this.clearForm = clearForm;
  }

  public Boolean getClearForm()
  {
    return clearForm;
  }

  public boolean isChangeFilterPage()
  {
    return isChangeFilterPage;
  }

  public void setChangeFilterPage( boolean isChangeFilterPage )
  {
    this.isChangeFilterPage = isChangeFilterPage;
  }

  public boolean isDashboardPage()
  {
    return isDashboardPage;
  }

  public void setDashboardPage( boolean isDashboardPage )
  {
    this.isDashboardPage = isDashboardPage;
  }

  public boolean isNodeAndBelow()
  {
    return nodeAndBelow;
  }

  public void setNodeAndBelow( boolean nodeAndBelow )
  {
    this.nodeAndBelow = nodeAndBelow;
  }

  public String getPaxFirstName()
  {
    return paxFirstName;
  }

  public void setPaxFirstName( String paxFirstName )
  {
    this.paxFirstName = paxFirstName;
  }

  public String getPaxLastName()
  {
    return paxLastName;
  }

  public void setPaxLastName( String paxLastName )
  {
    this.paxLastName = paxLastName;
  }

  public Long getBudgetId()
  {
    return budgetId;
  }

  public void setBudgetId( Long budgetId )
  {
    this.budgetId = budgetId;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getExtractType()
  {
    return extractType;
  }

  public void setExtractType( String extractType )
  {
    this.extractType = extractType;
  }

  public String getRootParentNodeId()
  {
    return rootParentNodeId;
  }

  public void setRootParentNodeId( String rootParentNodeId )
  {
    this.rootParentNodeId = rootParentNodeId;
  }

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public String getFormattedSelectMonth( String month )
  {
    if ( month != null && month.length() == 8 )
    {
      return month.substring( 4, 6 ) + "/" + month.substring( 6, month.length() ) + "/" + month.substring( 0, 4 );
    }
    return month;
  }

}
