
package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.IndividualReportsDAO;
import com.biperf.core.service.reports.IndividualReportsService;

public class IndividualReportsServiceImpl implements IndividualReportsService
{
  private IndividualReportsDAO individualReportsDAO;

  public void setIndividualReportsDAO( IndividualReportsDAO individualReportsDAO )
  {
    this.individualReportsDAO = individualReportsDAO;
  }

  // =============================
  // INDIVIDUAL ACTIVITY REPORT
  // =============================

  @Override
  public Map<String, Object> getIndividualActivityTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityPointsReceivedChart( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityPointsReceivedChart( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityPointsGivenReceivedChart( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityPointsGivenReceivedChart( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityTotalActivityChart( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityTotalActivityChart( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityMetricsChart( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityMetricsChart( reportParameters );
  }

  // ===================================================
  // INDIVIDUAL ACTIVITY REPORT - AWARDS RECEIVED
  // ===================================================
  @Override
  public Map<String, Object> getIndividualActivityAwardsReceivedTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityAwardsReceivedTabularResults( reportParameters );
  }

  // ===================================================
  // INDIVIDUAL ACTIVITY REPORT - NOMINATIONS RECEIVED
  // ===================================================

  @Override
  public Map<String, Object> getIndividualActivityNominationsReceivedTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityNominationsReceivedTabularResults( reportParameters );
  }

  // ===================================================
  // INDIVIDUAL ACTIVITY REPORT - NOMINATIONS GIVEN
  // ===================================================

  @Override
  public Map<String, Object> getIndividualActivityNominationsGivenTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityNominationsGivenTabularResults( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - PRODUCT CLAIM
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityProductClaimTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityProductClaimTabularResults( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - RECOGNITIONS GIVEN
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityRecognitionsGivenTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityRecognitionsGivenTabularResults( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - RECOGNITIONS RECEIVED
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityRecognitionsReceivedTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityRecognitionsReceivedTabularResults( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - QUIZ
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityQuizTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityQuizTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityQuizDetailResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityQuizDetailResults( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - THROWDOWN
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityThrowdownTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityThrowdownTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityThrowdownDetailResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityThrowdownDetailResults( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - EXTRACTS
  // ==================================================

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map getIndividualActivityReportExtract( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityReportExtract( reportParameters );
  }

  // ==================================================
  // INDIVIDUAL ACTIVITY REPORT - END EXTRACTS
  // ==================================================

  @Override
  public Map<String, Object> getIndividualActivityGoalQuestTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityGoalQuestTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityChallengePointTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityChallengePointTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityOnTheSpotTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityOnTheSpotTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getIndividualActivityBadgeTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivityBadgeTabularResults( reportParameters );
  }

  public Map<String, Object> getIndividualActivitySSIContestTabularResults( Map<String, Object> reportParameters )
  {
    return individualReportsDAO.getIndividualActivitySSIContestTabularResults( reportParameters );
  }

}
