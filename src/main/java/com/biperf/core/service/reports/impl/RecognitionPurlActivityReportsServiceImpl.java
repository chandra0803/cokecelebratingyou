
package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.RecognitionPurlActivityReportsDAO;
import com.biperf.core.service.reports.RecognitionPurlActivityReportsService;

/**
 * RecognitionPurlActivityReportsServiceImpl
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
 * <td>Aug 31, 2012</td>
 * <td>1.0</td>
 * <td>Intital version</td>
 * </tr>
 * </table>
 * 
 * @author kandhi
 */
public class RecognitionPurlActivityReportsServiceImpl implements RecognitionPurlActivityReportsService
{

  private RecognitionPurlActivityReportsDAO recognitionPurlActivityReportsDAO;

  @Override
  public Map<String, Object> getSummaryTabularResults( Map<String, Object> reportParameters )
  {
    return recognitionPurlActivityReportsDAO.getSummaryTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getParticipantTabularResults( Map<String, Object> reportParameters )
  {
    return recognitionPurlActivityReportsDAO.getParticipantTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getPurlActivityChartResults( Map<String, Object> reportParameters )
  {
    return recognitionPurlActivityReportsDAO.getPurlActivityChartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getOverallContributorsChartResults( Map<String, Object> reportParameters )
  {
    return recognitionPurlActivityReportsDAO.getOverallContributorsChartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getPurlReceipientsChartResults( Map<String, Object> reportParameters )
  {
    return recognitionPurlActivityReportsDAO.getPurlReceipientsChartResults( reportParameters );
  }

  @Override
  public Map getExtractResults( Map<String, Object> reportParameters )
  {
    return recognitionPurlActivityReportsDAO.getExtractResults( reportParameters );
  }

  public void setRecognitionPurlActivityReportsDAO( RecognitionPurlActivityReportsDAO recognitionPurlActivityReportsDAO )
  {
    this.recognitionPurlActivityReportsDAO = recognitionPurlActivityReportsDAO;
  }

}
