/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/goalquest/GoalQuestReportAction.java,v $
 *
 */

package com.biperf.core.ui.reports.goalquest;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.reports.GoalQuestReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportParametersForm;

/**
 * GoalQuestReportAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Sep 6, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author drahn
 *
 */
public abstract class GoalQuestReportAction extends BaseReportsAction
{

  @SuppressWarnings( "unused" )
  private static final Log logger = LogFactory.getLog( GoalQuestReportAction.class );

  protected GoalQuestReportsService getGoalQuestReportsService()
  {
    return (GoalQuestReportsService)getService( GoalQuestReportsService.BEAN_NAME );
  }

  protected void determineGoalLevelDisplay( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestSelectionValidLevelNumbers( form.getReportParameters() );
    List<Long> validGoalLevels = (List<Long>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "displayLevel1", validGoalLevels.contains( 1L ) );
    request.setAttribute( "displayLevel2", validGoalLevels.contains( 2L ) );
    request.setAttribute( "displayLevel3", validGoalLevels.contains( 3L ) );
    request.setAttribute( "displayLevel4", validGoalLevels.contains( 4L ) );
    request.setAttribute( "displayLevel5", validGoalLevels.contains( 5L ) );
    request.setAttribute( "displayLevel6", validGoalLevels.contains( 6L ) );
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.goalquest.extract";
  }

}
