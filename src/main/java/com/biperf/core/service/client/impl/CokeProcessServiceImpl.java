
package com.biperf.core.service.client.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.biperf.core.dao.client.CokeProcessDAO;
import com.biperf.core.domain.client.TcccCountryRule;
import com.biperf.core.service.client.CokeProcessService;
import com.biperf.core.value.client.CokeDayPaxValueBean;
import com.biperf.core.value.client.ParticipantEmployerValueBean;
import com.biperf.core.value.client.CokePushRptRecipientBean;

public class CokeProcessServiceImpl implements CokeProcessService
{

  private CokeProcessDAO cokeProcessDAO;

  @Override
  public Map<String, Object> callCokeFindServiceAnniversaryProc( Long startDays, Long endDays )
  {
    return cokeProcessDAO.callCokeFindServiceAnniversarySP( startDays, endDays );
  }
  
  // Client customization for WIP #42683 starts
  @Override
  public List<ParticipantEmployerValueBean> getNonMilestoneServiceAnniversies( Long priorDays, Long futureDays )
  {
    return cokeProcessDAO.getNonMilestoneServiceAnniversies( priorDays, futureDays );
  }
  
  @Override
  public List<ParticipantEmployerValueBean> getNonMilestoneServiceSixthMonthAnniversies( Date priorDate, Date futureDate )
  {
    return cokeProcessDAO.getNonMilestoneServiceSixthMonthAnniversies( priorDate, futureDate );
  }
  
  @Override
  public List<TcccCountryRule> getCountryRuleByUserCharCountry( String countryCode )
  {
    return cokeProcessDAO.getCountryRuleByUserCharCountry( countryCode );
  }
  // Client customization for WIP #42683 ends
  
  // Client customization for WIP #42683 starts
  
//Client customization for WIP #57733 starts

 public List<CokePushRptRecipientBean> getPushProcessReports()
 {
   return cokeProcessDAO.getPushProcessReports();
 }

 public int updatePushProcessRecipient( List prRecipientIds, String msgStatus )
 {
	 return cokeProcessDAO.updatePushProcessRecipient( prRecipientIds, msgStatus );
 }

 public List<CokePushRptRecipientBean> getPushProcessReportsByUserId( Long userId, String divisionNumber )
 {
	  return cokeProcessDAO.getPushProcessReportsByUserId( userId, divisionNumber );
 }
//Client customization for WIP #57733 ends

 public List<CokePushRptRecipientBean> getPushProcessReportsForAdmin()
 {
   return cokeProcessDAO.getPushProcessReportsForAdmin( );
 }
 
 public List getPushProcessDivisionsByUserId( Long userId )
 {
   return cokeProcessDAO.getPushProcessDivisionsByUserId( userId);
 }
  
  @Override
  public List<CokeDayPaxValueBean> getCokeDayServicePax()
  {
    return cokeProcessDAO.getCokeDayServicePax();
  }
  
  

  public CokeProcessDAO getCokeProcessDAO()
  {
    return cokeProcessDAO;
  }

  public void setCokeProcessDAO( CokeProcessDAO cokeProcessDAO )
  {
    this.cokeProcessDAO = cokeProcessDAO;
  }

  @Override
  public int generateBunchballUsersData()
  {
    return this.cokeProcessDAO.generateBunchballUsersData();
  }

  @Override
  public int generateBunchballGrpMgtData()
  {
    return this.cokeProcessDAO.generateBunchballGrpMgtData();
  }

  @Override
  public int generateBunchballActivityData()
  {
    return this.cokeProcessDAO.generateBunchballActivityData();
  }

}
