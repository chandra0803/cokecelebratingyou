
package com.biperf.core.service.engagement.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.biperf.core.dao.engagement.EngagementDAO;
import com.biperf.core.domain.engagement.EngagementEligManager;
import com.biperf.core.domain.engagement.EngagementEligUser;
import com.biperf.core.service.engagement.EngagementService;
import com.biperf.core.value.EngagementChartValueBean;
import com.biperf.core.value.EngagementDashboardValueBean;
import com.biperf.core.value.EngagementPromotionData;
import com.biperf.core.value.EngagementSummaryValueBean;
import com.biperf.core.value.EngagementTeamMembersValueBean;
import com.biperf.core.value.EngagementTeamsValueBean;

/**
 * 
 * EngagementServiceImpl.
 * 
 * @author kandhi
 * @since May 23, 2014
 * @version 1.0
 */
public class EngagementServiceImpl implements EngagementService
{

  private EngagementDAO engagementDAO;

  public void setEngagementDAO( EngagementDAO engagementDAO )
  {
    this.engagementDAO = engagementDAO;
  }

  @Override
  public EngagementDashboardValueBean getDashboardTeamData( Map<String, Object> extractParams ) throws Exception
  {
    return engagementDAO.getDashboardTeamData( extractParams );
  }

  @Override
  public EngagementDashboardValueBean getDashboardUserData( Map<String, Object> extractParams ) throws Exception
  {
    return engagementDAO.getDashboardUserData( extractParams );
  }

  @Override
  public EngagementSummaryValueBean getSummaryData( String mode, Long userId, Locale userLocale, String timeframeType, int endMonth, int endYear )
  {
    return engagementDAO.getSummaryData( mode, userId, userLocale, timeframeType, endMonth, endYear );
  }

  @Override
  public List<EngagementChartValueBean> getRecognitionsByPromoChartData( Map<String, Object> extractParams ) throws Exception
  {
    return engagementDAO.getRecognitionsByPromoChartData( extractParams );
  }

  @Override
  public List<EngagementChartValueBean> getUniqueRecognitionsChartData( Map<String, Object> extractParams ) throws Exception
  {
    return engagementDAO.getUniqueRecognitionsChartData( extractParams );
  }

  @Override
  public List<EngagementChartValueBean> getLoginVisitsChartData( Map<String, Object> extractParams ) throws Exception
  {
    return engagementDAO.getLoginVisitsChartData( extractParams );
  }

  @Override
  public List<EngagementTeamMembersValueBean> sortTeamMembers( Map<String, Object> extractParams )
  {
    return engagementDAO.sortTeamMembers( extractParams );
  }

  @Override
  public boolean isParticipantInManagerAudience( Long userId )
  {
    return engagementDAO.isParticipantInManagerAudience( userId );
  }

  @Override
  public List<EngagementTeamsValueBean> sortTeams( Map<String, Object> extractParams )
  {
    return engagementDAO.sortTeams( extractParams );
  }

  @Override
  public Map<String, Object> getEngagementRecognized( Map<String, Object> queryParams )
  {
    return engagementDAO.getEngagementRecognized( queryParams );
  }

  @Override
  public Map<String, Object> getNotificationsData( Map<String, Object> queryParams )
  {
    return engagementDAO.getNotificationsData( queryParams );
  }

  @Override
  public List<EngagementEligManager> getAllEligibleManagers()
  {
    return engagementDAO.getAllEligibleManagers();
  }

  @Override
  public List<EngagementEligUser> getAllEligibleParticipants()
  {
    return engagementDAO.getAllEligibleParticipants();
  }

  @Override
  public List<String> getAllEligiblePromotions()
  {
    return engagementDAO.getAllEligiblePromotions();
  }

  @Override
  public Map deleteEngagementPromotion( Long promotionId ) throws Exception
  {
    return engagementDAO.deleteEngagementPromotion( promotionId );
  }

  @Override
  public String getEngagementManagerModuleAppAudienceTypeByUserId( Long userId )
  {
    return engagementDAO.getEngagementManagerModuleAppAudienceTypeByUserId( userId );
  }
  
  @Override
  public EngagementPromotionData getLiveEngagementPromotionData()
  {
    return engagementDAO.getLiveEngagementPromotionData();
  }
  
  @Override
  public Set<Long> getAllEligiblePromotionIds( Long promotionId )
  {
    return engagementDAO.getAllEligiblePromotionIds( promotionId );
  }

}
