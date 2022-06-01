/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/challengepoint/ChallengepointReportAction.java,v $
 *
 */

package com.biperf.core.ui.reports.challengepoint;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.reports.ChallengePointReportsService;
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
public abstract class ChallengepointReportAction extends BaseReportsAction
{

  @SuppressWarnings( "unused" )
  private static final Log logger = LogFactory.getLog( ChallengepointReportAction.class );

  protected ChallengePointReportsService getChallengepointReportsService()
  {
    return (ChallengePointReportsService)getService( ChallengePointReportsService.BEAN_NAME );
  }

  protected void determineGoalLevelDisplay( ReportParametersForm form, HttpServletRequest request )
  {
    Map<String, Object> resultsMap = getChallengepointReportsService().getChallengePointSelectionValidLevelNumbers( form.getReportParameters() );
    List<Long> validGoalLevels = (List<Long>)resultsMap.get( "p_out_data" );
    if ( validGoalLevels != null )
    {
      request.setAttribute( "displayLevel1", validGoalLevels.contains( 1L ) );
      request.setAttribute( "displayLevel2", validGoalLevels.contains( 2L ) );
      request.setAttribute( "displayLevel3", validGoalLevels.contains( 3L ) );
      request.setAttribute( "displayLevel4", validGoalLevels.contains( 4L ) );
      request.setAttribute( "displayLevel5", validGoalLevels.contains( 5L ) );
      request.setAttribute( "displayLevel6", validGoalLevels.contains( 6L ) );
    }
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.challengepoint.extract";
  }

}
