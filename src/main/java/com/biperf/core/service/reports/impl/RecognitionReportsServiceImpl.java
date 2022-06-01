/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/impl/RecognitionReportsServiceImpl.java,v $
 */

package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.RecognitionReportsDAO;
import com.biperf.core.service.reports.RecognitionReportsService;

/**
 * RecognitionReportsServiceImpl.
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
 * <td>Aug 3, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class RecognitionReportsServiceImpl implements RecognitionReportsService
{
  private RecognitionReportsDAO recognitionReportsDAO;

  @Override
  public Map<String, Object> getRecognitionGivenPieResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionGivenPieResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionGivenByTimeBarResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionGivenByTimeBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsGivenBarResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionPointsGivenBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsGivenByTimeBarResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionPointsGivenByTimeBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionGivenParticipationRatePieResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionGivenParticipationRatePieResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionGivenParticipationRateBarResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionGivenParticipationRateBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionSummaryResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionSummaryResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionGivenDetailResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionGivenDetailResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionReceivedDetailResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionReceivedDetailResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsGivenByParticipantResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionsGivenByParticipantResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsGivenByParticipantResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionPointsGivenByParticipantResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsGivenByPromotionResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionsGivenByPromotionResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsGivenByPromotionResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionPointsGivenByPromotionResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsGivenMetricsResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionsGivenMetricsResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsGivenParticipationRateByPaxResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionsGivenParticipationRateByPaxResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionSummaryGivenByParticipantResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionSummaryGivenByParticipantResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsReceivedByParticipantResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionsReceivedByParticipantResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsReceivedByParticipantResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionPointsReceivedByParticipantResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsReceivedByPromotionResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionsReceivedByPromotionResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsReceivedByPromotionResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionPointsReceivedByPromotionResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsReceivedMetricsResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionsReceivedMetricsResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsReceivedParticipationRateByPaxResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionsReceivedParticipationRateByPaxResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionSummaryReceivedByParticipantResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionSummaryReceivedByParticipantResults( reportParameters );
  }

  public void setRecognitionReportsDAO( RecognitionReportsDAO recognitionReportsDAO )
  {
    this.recognitionReportsDAO = recognitionReportsDAO;
  }

  @Override
  public Map getRecognitionActivityReportExtract( Map<String, Object> reportParameters )
  {
    if ( "receiver".equals( reportParameters.get( "giverReceiver" ) ) )
    {
      return recognitionReportsDAO.getRecognitionReceiverActivityReportExtract( reportParameters );
    }
    else
    {
      return recognitionReportsDAO.getRecognitionGiverActivityReportExtract( reportParameters );
    }
  }

  @Override
  public Map<String, Object> getRecognitionReceivedPieResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionReceivedPieResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionReceivedByTimeBarResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionReceivedByTimeBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionReceivedParticipationRateBarResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionReceivedParticipationRateBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionReceivedParticipationRatePieResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionReceivedParticipationRatePieResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsReceivedBarResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionPointsReceivedBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionPointsByTimeReceivedBarResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionPointsByTimeReceivedBarResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionReceivedSummaryResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionReceivedSummaryResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionsReceivedScatterPlotResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionsReceivedScatterPlotResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionActivityReceivedByParticipantResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionActivityReceivedByParticipantResults( reportParameters );
  }

  @Override
  public Map<String, Object> getRecognitionActivityGivenByParticipantResults( Map<String, Object> reportParameters )
  {
    return recognitionReportsDAO.getRecognitionActivityGivenByParticipantResults( reportParameters );
  }
}
