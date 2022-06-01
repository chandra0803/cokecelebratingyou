
package com.biperf.core.service.reports.impl;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.reports.PlateauAwardReportsDAO;
import com.biperf.core.service.reports.PlateauAwardReportsService;
import com.biperf.core.value.plateauawards.PlateauAwardCodeIssuanceReportValue;

public class PlateauAwardReportsServiceImpl implements PlateauAwardReportsService
{

  private PlateauAwardReportsDAO plateauAwardReportsDAO;

  // ***************************************//
  // -------- AWARD LEVEL ACTIVITY -------- //
  // ***************************************//

  @Override
  public Map<String, Object> getAwardLevelActivitySummaryResults( Map<String, Object> reportParameters )
  {
    return plateauAwardReportsDAO.getAwardLevelActivitySummaryResults( reportParameters );
  }

  @Override
  public Map<String, Object> getPlateauAwardActivityChartResults( Map<String, Object> reportParameters )
  {
    return plateauAwardReportsDAO.getPlateauAwardActivityChartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getPercentagePlateauAwardActivityChartResults( Map<String, Object> reportParameters )
  {
    return plateauAwardReportsDAO.getPercentagePlateauAwardActivityChartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getAwardLevelActivityTeamLevelResults( Map<String, Object> reportParameters )
  {
    return plateauAwardReportsDAO.getAwardLevelActivityTeamLevelResults( reportParameters );
  }

  // ***************************************//
  // -------- AWARD ITEM SELECTION -------- //
  // ***************************************//
  @Override
  public Map<String, Object> getItemSelectionSummaryResults( Map<String, Object> reportParameters )
  {
    return plateauAwardReportsDAO.getItemSelectionSummaryResults( reportParameters );
  }

  @Override
  public Map<String, Object> getPlateauAwardSelectionChartResults( Map<String, Object> reportParameters )
  {
    return plateauAwardReportsDAO.getPlateauAwardSelectionChartResults( reportParameters );
  }

  @Override
  public Map<String, Object> getTopRedeemedAwardsChartResults( Map<String, Object> reportParameters )
  {
    return plateauAwardReportsDAO.getTopRedeemedAwardsChartResults( reportParameters );
  }

  // **************************************//
  // -------- AWARD CODE ISSUANCE -------- //
  // **************************************//

  @Override
  public List<PlateauAwardCodeIssuanceReportValue> getCodeIssuanceSummaryResults( Map<String, Object> reportParameters )
  {
    return plateauAwardReportsDAO.getCodeIssuanceSummaryResults( reportParameters );
  }

  @Override
  public int getCodeIssuanceSummaryResultsSize( Map<String, Object> reportParameters )
  {
    return plateauAwardReportsDAO.getCodeIssuanceSummaryResultsSize( reportParameters );
  }

  @Override
  public PlateauAwardCodeIssuanceReportValue getCodeIssuanceSummaryResultsTotals( Map<String, Object> reportParameters )
  {
    return plateauAwardReportsDAO.getCodeIssuanceSummaryResultsTotals( reportParameters );
  }

  @Override
  public List<PlateauAwardCodeIssuanceReportValue> getAwardCodeStatusByPercentageChartResults( Map<String, Object> reportParameters )
  {
    return plateauAwardReportsDAO.getAwardCodeStatusByPercentageChartResults( reportParameters );
  }

  @Override
  public List<PlateauAwardCodeIssuanceReportValue> getAwardCodeStatusByCountChartResults( Map<String, Object> reportParameters )
  {
    return plateauAwardReportsDAO.getAwardCodeStatusByCountChartResults( reportParameters );
  }

  public Map getParticipantAwardLevelExtract( Map<String, Object> reportParameters )
  {
    return plateauAwardReportsDAO.getParticipantAwardLevelExtract( reportParameters );
  }

  /**
   * 
   */
  public void setPlateauAwardReportsDAO( PlateauAwardReportsDAO plateauAwardReportsDAO )
  {
    this.plateauAwardReportsDAO = plateauAwardReportsDAO;
  }
}
