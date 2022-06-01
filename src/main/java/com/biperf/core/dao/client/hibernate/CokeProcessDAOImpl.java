
package com.biperf.core.dao.client.hibernate;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.client.CokeProcessDAO;
import com.biperf.core.domain.client.CareerMomentsView;
import com.biperf.core.domain.client.TcccCountryRule;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.client.CokeDayPaxValueBean;
import com.biperf.core.value.client.ParticipantEmployerValueBean;
import com.biperf.core.value.client.CokePushRptRecipientBean;

public class CokeProcessDAOImpl extends BaseDAO implements CokeProcessDAO
{
  private DataSource dataSource;

  @Override
  public Map<String, Object> callCokeFindServiceAnniversarySP( Long startDays, Long endDays )
  {
    CokeFindServiceAnniversarySP proc = new CokeFindServiceAnniversarySP( dataSource );
    return proc.executeProcedure( startDays, endDays );
  }

  // Client customization for WIP #42683 starts
  @SuppressWarnings( "unchecked" )
  @Override
  public List<ParticipantEmployerValueBean> getNonMilestoneServiceAnniversies( Long priorDays, Long futureDays )
  {
    Query query = getSession().createSQLQuery( " SELECT USER_ID,HIRE_DATE FROM PARTICIPANT_EMPLOYER WHERE TERMINATION_DATE IS NULL "
        + " AND TO_CHAR(HIRE_DATE, 'mm-dd')>= TO_CHAR(SYSDATE-:priorDays,'mm-dd') "
        + " AND TO_CHAR(HIRE_DATE, 'mm-dd')<= TO_CHAR(SYSDATE+:futureDays,'mm-dd') " );
    query.setParameter( "priorDays", priorDays );
    query.setParameter( "futureDays", futureDays );
    query.setResultTransformer( new ParticipantEmployerValueBeanMapper() );
    return query.list();
  }
  
  @SuppressWarnings( "unchecked" )
  @Override
  public List<ParticipantEmployerValueBean> getNonMilestoneServiceSixthMonthAnniversies( Date priorDate, Date futureDate )
  {
    Query query = getSession().createSQLQuery( " SELECT USER_ID,HIRE_DATE FROM PARTICIPANT_EMPLOYER WHERE TERMINATION_DATE IS NULL "
        + " AND TO_CHAR(HIRE_DATE, 'mm-dd-yyyy')>= TO_CHAR(:priorDate,'mm-dd-yyyy') "
        + " AND TO_CHAR(HIRE_DATE, 'mm-dd-yyyy')<= TO_CHAR(:futureDate,'mm-dd-yyyy') " );
    query.setParameter( "priorDate", priorDate );
    query.setParameter( "futureDate", futureDate );
    query.setResultTransformer( new ParticipantEmployerValueBeanMapper() );
    return query.list();
  }
  
  @SuppressWarnings( "serial" )
  private class ParticipantEmployerValueBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      ParticipantEmployerValueBean employerValueBean = new ParticipantEmployerValueBean();
      employerValueBean.setUserId( extractLong( tuple[0] ) );
      employerValueBean.setHireDate( extractDate( tuple[1] ) );
      return employerValueBean;
    }
  }
  
  @SuppressWarnings( "unchecked" )
  public List<TcccCountryRule> getCountryRuleByUserCharCountry( String countryCode )
  {
    Query query = getSession().createSQLQuery( " SELECT * FROM ADIH_COUNTRY_RULE where country_code=:countryCode AND DATE_END IS null " ).addEntity( TcccCountryRule.class );
    query.setParameter( "countryCode", countryCode );
    return query.list();
  } 
  // Client customization for WIP #42683 ends
  
  //Client customization for WIP #44575 starts
  @SuppressWarnings( "unchecked" )
  @Override
  public List<CokeDayPaxValueBean> getCokeDayServicePax()
  {
	// TODO Auto-generated method stub
		
	  Query query = getSession().createSQLQuery( " SELECT * from TABLE(pkg_get_coke_day_email.fnc_get_coke_day_email) " );	
			query.setResultTransformer( new CokeDayResultTransformer());
			return query.list();
  }

  @SuppressWarnings( "serial" )
  private class CokeDayResultTransformer extends BaseResultTransformer
  {
     @Override
     public Object transformTuple( Object[] tuple, String[] aliases )
     {
    	 CokeDayPaxValueBean bean = new CokeDayPaxValueBean();
       
       bean.setUserId(extractLong( tuple[0] ) );
       bean.setFirstName(extractString( tuple[1] ) );
       bean.setLastName(extractString( tuple[2] ) );
       bean.setHireDate(extractString( tuple[3] ) );
      
       
       return bean;
     }
   }
//Client customization for WIP #44575 ends
  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  } 
 
  //Client customization for WIP #57733 start

  public List<CokePushRptRecipientBean> getPushProcessReports()
  {
    Query query = getSession().createSQLQuery( " SELECT PR_RECIPIENT_ID,PR_BATCH_ID,PROCESS_STATUS,REPORT_FOLDER,REPORT_PERIOD,USER_ID,"
    		+ "RECIPIENT_NAME,DIVISION_NUMBER,EMAIL_ADDR,LANGUAGE_ID,REPORT_NAME,FILE_NAME,FROM_DATE,TO_DATE,ERROR_COUNT,DIVISION_NAME " + "FROM ADIH_PUSH_REPORT_RECIPIENT WHERE PROCESS_STATUS = 'msg_staged' AND "
        + "PR_BATCH_ID IN (SELECT PR_BATCH_ID FROM ADIH_PUSH_REPORT_WORK WHERE PROCESS_STATUS = 'complete') " );
    query.setResultTransformer( new CokePushRptRecipientBeanMapper() );
    return query.list();
  }

  @SuppressWarnings( "serial" )
  private class CokePushRptRecipientBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      CokePushRptRecipientBean cokePushRptRecipientBean = new CokePushRptRecipientBean();

      cokePushRptRecipientBean.setPrRecipientId( extractLong( tuple[0] ) );
      cokePushRptRecipientBean.setPrBatchId( extractLong( tuple[1] ) );
      cokePushRptRecipientBean.setProcessStatus( extractString( tuple[2] ) );

      cokePushRptRecipientBean.setReportFolder( extractString( tuple[3] ) );
      cokePushRptRecipientBean.setReportPeriod( extractString( tuple[4] ) );
      cokePushRptRecipientBean.setUserId( extractLong( tuple[5] ) );
      cokePushRptRecipientBean.setRecipientName( extractString( tuple[6] ) );
      cokePushRptRecipientBean.setDivisionNumber( extractString( tuple[7] ) );
      cokePushRptRecipientBean.setEmailAddr( extractString( tuple[8] ) );
      cokePushRptRecipientBean.setLanguageId( extractString( tuple[9] ) );
      cokePushRptRecipientBean.setReportName( extractString( tuple[10] ) );
      cokePushRptRecipientBean.setFileName( extractString( tuple[11] ) );
      cokePushRptRecipientBean.setFromDate( extractDate( tuple[12] ) );
      cokePushRptRecipientBean.setToDate( extractDate( tuple[13] ) );
      cokePushRptRecipientBean.setErrorCount( extractLong( tuple[14] ) );
      cokePushRptRecipientBean.setDivisionName( extractString( tuple[15] ) );

      return cokePushRptRecipientBean;
    }
  }

  public int updatePushProcessRecipient( List prRecipientIds, String msgStatus )
  {
	    Query query = getSession().createSQLQuery( " update adih_push_report_recipient set process_status =:msgStatus where pr_recipient_id in (:prrecipientids) " );
	    query.setParameter( "msgStatus", msgStatus );
    query.setParameterList( "prrecipientids", prRecipientIds );
    return query.executeUpdate();
  }

  public List<CokePushRptRecipientBean> getPushProcessReportsByUserId( Long userId, String divisionNumber )
  {
    Query query = getSession().createSQLQuery( " SELECT PR_RECIPIENT_ID,PR_BATCH_ID,PROCESS_STATUS,REPORT_FOLDER,REPORT_PERIOD,USER_ID,"
    		+ "RECIPIENT_NAME,DIVISION_NUMBER,EMAIL_ADDR,LANGUAGE_ID,REPORT_NAME,FILE_NAME,FROM_DATE,TO_DATE,ERROR_COUNT,DIVISION_NAME " + "FROM ADIH_PUSH_REPORT_RECIPIENT WHERE PROCESS_STATUS = 'msg_staged' AND "
    		+ "PR_BATCH_ID IN (SELECT PR_BATCH_ID FROM ADIH_PUSH_REPORT_WORK WHERE PROCESS_STATUS = 'complete')  AND USER_ID=:userId AND DIVISION_NUMBER=:divisionNumber order by user_id, division_number, process_order " );
    query.setParameter( "userId", userId );
    query.setParameter( "divisionNumber", divisionNumber );
    query.setResultTransformer( new CokePushRptRecipientBeanMapper() );
    return query.list();
  }
//Client customization for WIP #57733 end
  
  public List<CokePushRptRecipientBean> getPushProcessReportsForAdmin()
  {
    Query query = getSession().createSQLQuery( " SELECT PR_RECIPIENT_ID,PR_BATCH_ID,PROCESS_STATUS,REPORT_FOLDER,REPORT_PERIOD,USER_ID,"
    		+ "RECIPIENT_NAME,DIVISION_NUMBER,EMAIL_ADDR,LANGUAGE_ID,REPORT_NAME,FILE_NAME,FROM_DATE,TO_DATE,ERROR_COUNT,DIVISION_NAME " + "FROM ADIH_PUSH_REPORT_RECIPIENT WHERE PROCESS_STATUS = 'msg_staged' AND IS_ADMIN=1" );
    query.setResultTransformer( new CokePushRptRecipientBeanMapper() );
    return query.list();
  }

  public List getPushProcessDivisionsByUserId( Long userId )
  {
    Query query = getSession().createSQLQuery( "select distinct(division_number) From adih_push_report_recipient where user_id =:userId" );
    query.setParameter( "userId", userId );
    return query.list();
  }
  
  public int generateBunchballUsersData()
  {
    CallPrcBunchballUsers procedure = new CallPrcBunchballUsers( dataSource );
    Map results = procedure.executeProcedure();
    return (int)results.get( "p_out_result_code" );
  }
  
  public int generateBunchballGrpMgtData()
  {
    CallPrcBunchballGroupMgt procedure = new CallPrcBunchballGroupMgt( dataSource );
    Map results = procedure.executeProcedure();
    return (int)results.get( "p_out_result_code" );
  }
  
  public int generateBunchballActivityData()
  {
    CallPrcBunchballActivity procedure = new CallPrcBunchballActivity( dataSource );
    Map results = procedure.executeProcedure();
    return (int)results.get( "p_out_result_code" );
  }

}
