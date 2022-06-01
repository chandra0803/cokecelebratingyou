/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/RecognitionReportsService.java,v $
 *
 */

package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * RecognitionReportsService.
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
public interface RecognitionReportsService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "recognitionReportsService";

  public Map<String, Object> getRecognitionGivenPieResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionGivenByTimeBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionPointsGivenBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionPointsGivenByTimeBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionGivenParticipationRatePieResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionGivenParticipationRateBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionSummaryResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionGivenDetailResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionReceivedDetailResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionsGivenByParticipantResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionPointsGivenByParticipantResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionsGivenByPromotionResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionPointsGivenByPromotionResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionsGivenMetricsResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionsGivenParticipationRateByPaxResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionSummaryGivenByParticipantResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionsReceivedByParticipantResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionPointsReceivedByParticipantResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionsReceivedByPromotionResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionPointsReceivedByPromotionResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionsReceivedMetricsResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionsReceivedParticipationRateByPaxResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionSummaryReceivedByParticipantResults( Map<String, Object> reportParameters );

  public Map getRecognitionActivityReportExtract( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionReceivedPieResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionReceivedByTimeBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionReceivedParticipationRateBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionReceivedParticipationRatePieResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionPointsReceivedBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionPointsByTimeReceivedBarResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionReceivedSummaryResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionsReceivedScatterPlotResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionActivityReceivedByParticipantResults( Map<String, Object> reportParameters );

  public Map<String, Object> getRecognitionActivityGivenByParticipantResults( Map<String, Object> reportParameters );
}
