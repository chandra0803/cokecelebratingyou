
package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * RecognitionPurlActivityReportsService
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
public interface RecognitionPurlActivityReportsService extends SAO
{
  public static String BEAN_NAME = "recognitionPurlActivityReportsService";

  public Map<String, Object> getSummaryTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getPurlActivityChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getOverallContributorsChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getPurlReceipientsChartResults( Map<String, Object> reportParameters );

  public Map getExtractResults( Map<String, Object> reportParameters );

  public Map<String, Object> getParticipantTabularResults( Map<String, Object> reportParameters );

}
