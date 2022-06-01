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
public interface ParticipantAwardActivityExportReportService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "participantAwardActivityExportReportService";

  @SuppressWarnings( "rawtypes" )
  public Map getParticipantAwardActivityExportReportExtract( Map<String, Object> reportParameters );

}
