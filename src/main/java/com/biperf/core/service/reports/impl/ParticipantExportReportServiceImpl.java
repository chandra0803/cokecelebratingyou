/**
 * 
 */

package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.ParticipantExportReportDAO;
import com.biperf.core.service.reports.ParticipantExportReportService;

/**
 * @author poddutur
 *
 */
public class ParticipantExportReportServiceImpl implements ParticipantExportReportService
{
  ParticipantExportReportDAO participantExportReportDAO;

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map getParticipantExportReportExtract( Map<String, Object> reportParameters )
  {
    return participantExportReportDAO.getParticipantExportReportExtractResults( reportParameters );
  }

  public void setParticipantExportReportDAO( ParticipantExportReportDAO participantExportReportDAO )
  {
    this.participantExportReportDAO = participantExportReportDAO;
  }

}
