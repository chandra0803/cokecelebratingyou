
package com.biperf.core.dao.reports;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.value.plateauawards.PlateauAwardCodeIssuanceReportValue;

public interface PlateauAwardReportsDAO extends DAO
{

  // ***************************************//
  // -------- AWARD LEVEL ACTIVITY -------- //
  // ***************************************//

  public Map<String, Object> getAwardLevelActivitySummaryResults( Map<String, Object> reportParameters );

  public Map<String, Object> getPlateauAwardActivityChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getPercentagePlateauAwardActivityChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getAwardLevelActivityTeamLevelResults( Map<String, Object> reportParameters );

  // ***************************************//
  // -------- AWARD ITEM SELECTION -------- //
  // ***************************************//

  public Map<String, Object> getItemSelectionSummaryResults( Map<String, Object> reportParameters );

  public Map<String, Object> getPlateauAwardSelectionChartResults( Map<String, Object> reportParameters );

  public Map<String, Object> getTopRedeemedAwardsChartResults( Map<String, Object> reportParameters );

  // **************************************//
  // -------- AWARD CODE ISSUANCE -------- //
  // **************************************//

  public List<PlateauAwardCodeIssuanceReportValue> getCodeIssuanceSummaryResults( Map<String, Object> reportParameters );

  public int getCodeIssuanceSummaryResultsSize( Map<String, Object> reportParameters );

  public PlateauAwardCodeIssuanceReportValue getCodeIssuanceSummaryResultsTotals( Map<String, Object> reportParameters );

  public List<PlateauAwardCodeIssuanceReportValue> getAwardCodeStatusByPercentageChartResults( Map<String, Object> reportParameters );

  public List<PlateauAwardCodeIssuanceReportValue> getAwardCodeStatusByCountChartResults( Map<String, Object> reportParameters );

  public Map getParticipantAwardLevelExtract( Map<String, Object> reportParameters );
}
