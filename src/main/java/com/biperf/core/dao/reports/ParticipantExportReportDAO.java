/**
 * 
 */

package com.biperf.core.dao.reports;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * @author poddutur
 *
 */
public interface ParticipantExportReportDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "participantExportReportDAO";

  @SuppressWarnings( "rawtypes" )
  public Map getParticipantExportReportExtractResults( Map<String, Object> reportParameters );

}
