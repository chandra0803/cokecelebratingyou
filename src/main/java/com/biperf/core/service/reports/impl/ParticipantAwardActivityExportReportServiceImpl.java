/**
 * 
 */

package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.ParticipantAwardActivityExportReportDAO;
import com.biperf.core.service.reports.ParticipantAwardActivityExportReportService;

/**
 * @author poddutur
 *
 */
public class ParticipantAwardActivityExportReportServiceImpl implements ParticipantAwardActivityExportReportService
{
  ParticipantAwardActivityExportReportDAO participantAwardActivityExportReportDAO;

  @Override
  public Map getParticipantAwardActivityExportReportExtract( Map<String, Object> reportParameters )
  {
    return participantAwardActivityExportReportDAO.getParticipantAwardActivityExportReportExtract( reportParameters );
  }

  public void setParticipantAwardActivityExportReportDAO( ParticipantAwardActivityExportReportDAO participantAwardActivityExportReportDAO )
  {
    this.participantAwardActivityExportReportDAO = participantAwardActivityExportReportDAO;
  }

}
