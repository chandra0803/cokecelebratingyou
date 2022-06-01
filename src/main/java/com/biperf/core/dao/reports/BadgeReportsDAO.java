
package com.biperf.core.dao.reports;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * 
 * BadgeReportsDAO.
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
 * <td>kandhi</td>
 * <td>Nov 29, 2012</td>
 * <td>1.0</td>
 * <td>Initial version</td>
 * </tr>
 * </table>
 * 
 */
public interface BadgeReportsDAO extends DAO
{

  public Map getBadgeActivityExtractResults( Map<String, Object> reportParameters );

  public Map<String, Object> getBadgeActivityByOrgSummaryTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getBadgesEarnedBarChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getBadgeActivityTeamLevelTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getBadgeActivityParticipantLevelTabularResults( Map<String, Object> reportParameters );

}
