
package com.biperf.core.dao.reports;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * RecognitionPurlActivityReportsDAO
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
public interface RecognitionPurlActivityReportsDAO extends DAO
{
  public static final String BEAN_NAME = "recognitionPurlActivityDAO";

  public Map<String, Object> getSummaryTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getPurlActivityChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getOverallContributorsChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getPurlReceipientsChartResults( Map<String, Object> reportParameters );

  public Map getExtractResults( Map<String, Object> reportParameters );

  public Map<String, Object> getParticipantTabularResults( Map<String, Object> reportParameters );
}
