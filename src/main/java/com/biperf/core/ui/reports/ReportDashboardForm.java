/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/ReportDashboardForm.java,v $
 */

package com.biperf.core.ui.reports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.report.ReportDashboard;
import com.biperf.core.domain.report.ReportDashboardItem;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;

/**
 * ReportDashboardForm.
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
 * <td>arasi</td>
 * <td>May 13, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ReportDashboardForm extends BaseForm
{
  public static final String FORM_NAME = "reportDashboardForm";

  private String method;
  private String highlights;
  private List dashboardItems;
  private String dashboardItemId;

  public void reset( ActionMapping anActionMapping, HttpServletRequest request )
  {
    int itemCount = RequestUtils.getOptionalParamInt( request, "dashboardItemsSize" );
    dashboardItems = getEmptyValueList( itemCount );
  }

  /**
   * resets the value list with empty ReportDashboardItems
   * 
   * @param valueListCount
   * @return List
   */
  private List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      valueList.add( new ReportDashboardItem() );
    }

    return valueList;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    if ( method.equalsIgnoreCase( "cancel" ) )
    {
      return new ActionErrors();
    }

    ActionErrors actionErrors = super.validate( actionMapping, request );
    if ( highlights != null && highlights.length() > 2000 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "report.errors.HIGHLIGHTS_EXCEEDS_MAX_LENGTH" ) );
    }

    if ( actionErrors.size() == 0 )
    {
      String trimmedHighlights = highlights.replaceAll( "&nbsp;", " " );
      trimmedHighlights = trimmedHighlights.replaceAll( "<br />", " " );
      trimmedHighlights = trimmedHighlights.replaceAll( "<.*?>", " " );
      if ( trimmedHighlights.trim().equals( "" ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "report.dashboard.HIGHLIGHTS_EMPTY" ) );
      }
    }

    if ( getDashboardItems() != null )
    {
      int position1 = 0;
      int position2 = 0;
      int position3 = 0;
      int position4 = 0;
      int position5 = 0;
      int position6 = 0;
      for ( Iterator itemIter = getDashboardItems().iterator(); itemIter.hasNext(); )
      {
        ReportDashboardItem dashboardItem = (ReportDashboardItem)itemIter.next();
        int positionNumber = 0;
        // if ( dashboardItem.getPositionNumber() != null )
        // {
        // positionNumber = dashboardItem.getPositionNumber().intValue();
        // }
        if ( positionNumber == 1 )
        {
          position1 += 1;
        }
        else if ( positionNumber == 2 )
        {
          position2 += 1;
        }
        else if ( positionNumber == 3 )
        {
          position3 += 1;
        }
        else if ( positionNumber == 4 )
        {
          position4 += 1;
        }
        else if ( positionNumber == 5 )
        {
          position5 += 1;
        }
        else if ( positionNumber == 6 )
        {
          position6 += 1;
        }
      }
      if ( position1 > 1 || position2 > 1 || position3 > 1 || position4 > 1 || position5 > 1 || position6 > 1 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "report.errors.DUPLICATE_POSITION" ) );

      }
    }

    return actionErrors;
  }

  /**
   * Load this form with the ReportDashboard data.
   * 
   * @param reportDashboard
   */
  public void load( ReportDashboard reportDashboard )
  {
    this.highlights = reportDashboard.getHighlights();
    this.dashboardItems = reportDashboard.getReportDashboardItems();
  }

  public String getHighlights()
  {
    return highlights;
  }

  public void setHighlights( String highlights )
  {
    this.highlights = highlights;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getDashboardItemId()
  {
    return dashboardItemId;
  }

  public void setDashboardItemId( String dashboardItemId )
  {
    this.dashboardItemId = dashboardItemId;
  }

  public List getDashboardItems()
  {
    return dashboardItems;
  }

  public void setDashboardItems( List dashboardItems )
  {
    this.dashboardItems = dashboardItems;
  }

  public int getDashboardItemsSize()
  {
    if ( this.dashboardItems != null )
    {
      return this.dashboardItems.size();
    }
    return 0;
  }

}
