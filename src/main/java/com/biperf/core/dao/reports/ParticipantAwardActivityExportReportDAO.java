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
public interface ParticipantAwardActivityExportReportDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "participantAwardActivityExportReportDAO";

  Map getParticipantAwardActivityExportReportExtract( Map<String, Object> reportParameters );

}
