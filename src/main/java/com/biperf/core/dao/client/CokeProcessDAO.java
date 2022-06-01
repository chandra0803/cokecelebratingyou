
package com.biperf.core.dao.client;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.client.TcccCountryRule;
import com.biperf.core.value.client.CokeDayPaxValueBean;
import com.biperf.core.value.client.ParticipantEmployerValueBean;
import com.biperf.core.value.client.CokePushRptRecipientBean;

public interface CokeProcessDAO extends DAO
{
  public static final String BEAN_NAME = "cokeProcessDAO";

  public Map<String, Object> callCokeFindServiceAnniversarySP( Long startDays, Long endDays );
  
  // Client customization for WIP #42683 starts
  public List<ParticipantEmployerValueBean> getNonMilestoneServiceAnniversies( Long priorDays, Long futureDays );
  
  public List<ParticipantEmployerValueBean> getNonMilestoneServiceSixthMonthAnniversies( Date priorDate, Date futureDate );
  
  public List<TcccCountryRule> getCountryRuleByUserCharCountry( String countryCode );
  // Client customization for WIP #42683 ends
  
  public List<CokeDayPaxValueBean> getCokeDayServicePax(); //Client customization for WIP #44575

  public List<CokePushRptRecipientBean> getPushProcessReports();//Client customization for WIP #57733
  
  public int updatePushProcessRecipient(List prRecipientIds, String msgStatus);//Client customization for WIP #57733
  
  public List<CokePushRptRecipientBean> getPushProcessReportsByUserId(Long userId, String divisionNumber);//Client customization for WIP #57733
  public List<CokePushRptRecipientBean> getPushProcessReportsForAdmin();//Client customization for WIP #57733
  
  public List getPushProcessDivisionsByUserId( Long userId );//Client customization for WIP #57733
  
  public int generateBunchballUsersData();
  
  public int generateBunchballGrpMgtData();
  
  public int generateBunchballActivityData();

}
