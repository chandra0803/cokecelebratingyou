
package com.biperf.core.service.engagement;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.engagement.EngagementEligManager;
import com.biperf.core.domain.engagement.EngagementEligUser;
import com.biperf.core.service.SAO;
import com.biperf.core.value.EngagementChartValueBean;
import com.biperf.core.value.EngagementDashboardValueBean;
import com.biperf.core.value.EngagementPromotionData;
import com.biperf.core.value.EngagementSummaryValueBean;
import com.biperf.core.value.EngagementTeamMembersValueBean;
import com.biperf.core.value.EngagementTeamsValueBean;

/**
 * 
 * EngagementService.
 * 
 * @author kandhi
 * @since May 23, 2014
 * @version 1.0
 */
public interface EngagementService extends SAO
{
  /** * BEAN_NAME is the reference for spring ** */
  public static final String BEAN_NAME = "engagementService";

  public List<EngagementChartValueBean> getRecognitionsByPromoChartData( Map<String, Object> extractParams ) throws Exception;

  public List<EngagementChartValueBean> getUniqueRecognitionsChartData( Map<String, Object> extractParams ) throws Exception;

  public List<EngagementChartValueBean> getLoginVisitsChartData( Map<String, Object> extractParams ) throws Exception;

  public EngagementDashboardValueBean getDashboardTeamData( Map<String, Object> extractParams ) throws Exception;

  public EngagementDashboardValueBean getDashboardUserData( Map<String, Object> extractParams ) throws Exception;

  public EngagementSummaryValueBean getSummaryData( String mode, Long userId, Locale userLocale, String timeframeType, int endMonth, int endYear );

  public List<EngagementTeamMembersValueBean> sortTeamMembers( Map<String, Object> queryParams );

  public List<EngagementTeamsValueBean> sortTeams( Map<String, Object> queryParams );

  public Map<String, Object> getEngagementRecognized( Map<String, Object> queryParams );

  public boolean isParticipantInManagerAudience( Long userId );

  public Map<String, Object> getNotificationsData( Map<String, Object> queryParams );

  public List<EngagementEligManager> getAllEligibleManagers();

  public List<EngagementEligUser> getAllEligibleParticipants();

  public List<String> getAllEligiblePromotions();

  public Map deleteEngagementPromotion( Long promotionId ) throws Exception;

  String getEngagementManagerModuleAppAudienceTypeByUserId( Long userId );

  public EngagementPromotionData getLiveEngagementPromotionData();

  public Set<Long> getAllEligiblePromotionIds( Long promotionId );

}
