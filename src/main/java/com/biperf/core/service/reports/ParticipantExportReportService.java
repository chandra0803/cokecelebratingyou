/**
 * 
 */

package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * @author poddutur
 *
 */
public interface ParticipantExportReportService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "participantExportReportService";

  @SuppressWarnings( "rawtypes" )
  public Map getParticipantExportReportExtract( Map<String, Object> reportParameters );

}
