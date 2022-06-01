
package com.biperf.core.process.client;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.client.TcccCountryRule;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.process.BaseProcessImpl;
import com.biperf.core.service.client.CokeProcessService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.client.ParticipantEmployerValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * CokeNonMilestoneServiceAnniversaryEmailProcess.
 * 
 * @author Dudam
 * @since Feb 9, 2018
 * @version 1.0
 * 
 * This process is created as part of WIP #42683
 */
public class CokeNonMilestoneServiceAnniversaryEmailProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "cokeNonMilestoneServiceAnniversaryEmailProcess";
  public static final String MESSAGE_NAME = "Coke Non Milestone Service Anniversary Email";

  private CokeProcessService cokeProcessService;
  private NodeService nodeService;

  private static final Log logger = LogFactory.getLog( CokeNonMilestoneServiceAnniversaryEmailProcess.class );

  private Long priorDays;
  private Long futureDays;

  public void onExecute()
  {
    logger.info( "processName: " + BEAN_NAME + " - Coke Non Milestone Service Anniversary Email Process started with Prior Days: " + priorDays + " Future Days: " + futureDays );
    addComment( "processName: " + BEAN_NAME + "  - Coke Non Milestone Service Anniversary Email Process started with Prior Days: " + priorDays + " Future Days: " + futureDays );

    // Annual Anniversary which includes(1,2,3,4,5,6,7,8,9,10,11 etc) years
    List<ParticipantEmployerValueBean> anniversaryEmployeeValueBeans = cokeProcessService.getNonMilestoneServiceAnniversies( priorDays, futureDays );

    // Sixth month Anniversary which includes(today - 6 months)
    Date sixthMonthBefore = DateUtils.addMonths( -6 );
    Date priorDate = DateUtils.getDateAfterNumberOfDays( sixthMonthBefore, -priorDays.intValue() );
    Date futureDate = DateUtils.getDateAfterNumberOfDays( sixthMonthBefore, futureDays.intValue() );
    List<ParticipantEmployerValueBean> sixthMonthAnniversaryEmployeeValueBeans = cokeProcessService.getNonMilestoneServiceSixthMonthAnniversies( priorDate, futureDate );

    if ( ( anniversaryEmployeeValueBeans != null && anniversaryEmployeeValueBeans.size() > 0 )
        || sixthMonthAnniversaryEmployeeValueBeans != null && sixthMonthAnniversaryEmployeeValueBeans.size() > 0 )
    {
      int totalAnniversaryEmployees = ( anniversaryEmployeeValueBeans != null ? anniversaryEmployeeValueBeans.size() : 0 )
          + ( sixthMonthAnniversaryEmployeeValueBeans != null ? sixthMonthAnniversaryEmployeeValueBeans.size() : 0 );
      int validAnniversaryEmployees = 0;
      if ( anniversaryEmployeeValueBeans != null && anniversaryEmployeeValueBeans.size() > 0 )
      {
        validAnniversaryEmployees = checkEligiablityAndSendEmail( anniversaryEmployeeValueBeans, validAnniversaryEmployees, true );
      }
      if ( sixthMonthAnniversaryEmployeeValueBeans != null && sixthMonthAnniversaryEmployeeValueBeans.size() > 0 )
      {
        validAnniversaryEmployees = checkEligiablityAndSendEmail( sixthMonthAnniversaryEmployeeValueBeans, validAnniversaryEmployees, false );
      }
      logger.info( "processName: " + BEAN_NAME + " - Completed Coke Milestone Service Anniversary Email Process, Total Anniversary Employees: " + totalAnniversaryEmployees
          + " & Valid Employees(Active & Terms Accepted): " + validAnniversaryEmployees );
      addComment( "processName: " + BEAN_NAME + " - Completed Coke Milestone Service Anniversary Email Process, Total Anniversary Employees: " + totalAnniversaryEmployees
          + " & Valid Employees(Active & Terms Accepted): " + validAnniversaryEmployees );
    }
    else
    {
      logger.info( "processName: " + BEAN_NAME + " - Completed Coke Milestone Service Anniversary Email Process with ZERO anniversary employees." );
      addComment( "processName: " + BEAN_NAME + " - Completed Coke Milestone Service Anniversary Email Process with ZERO anniversary employees." );
    }
  }

  private int checkEligiablityAndSendEmail( List<ParticipantEmployerValueBean> employeeValueBeans, int validAnniversaryEmployees, boolean isAnnual )
  {
    try
    {
      for ( ParticipantEmployerValueBean employeeValueBean : employeeValueBeans )
      {
        Participant participant = participantService.getParticipantById( employeeValueBean.getUserId() );
        //participant
        if ( isEligiableToReceiveAnniversaryEmail( employeeValueBean, participant, isAnnual ) )
        {
        	
          sendAnniversaryEmail( employeeValueBean, participant, isAnnual );
          buildCCsendAnniversaryEmail( employeeValueBean, participant, isAnnual, true);
          validAnniversaryEmployees++;
        }
      }
    }
    catch( Exception e )
    {
      logger.error( "An exception occurred while sending " + MESSAGE_NAME + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
    return validAnniversaryEmployees;
  }

  private boolean isEligiableToReceiveAnniversaryEmail( ParticipantEmployerValueBean employeeValueBean, Participant participant, boolean isAnnual )
  {
    int anniversaryYear = getAnniversaryYear( employeeValueBean );
    if ( participant.isActive() && participant.isTermsAccepted() )
    {
      // Check anniversary is not multiple of 5
      if ( ( isAnnual && anniversaryYear % 5 != 0 ) || !isAnnual )
      {
        boolean sendEmail = false;
        // Get user country characteristic value
        String userCountryCharValue = userService.getUserCountryCharValue( participant.getId() );
        
        // Get country rule records by using the user country characteristic value
        List<TcccCountryRule> countryRules = cokeProcessService.getCountryRuleByUserCharCountry( userCountryCharValue );
        
        if ( countryRules != null )
        {
          String userServiceCharValue = userService.getUserServiceCharValue( participant.getId() );
          if ( StringUtil.isNullOrEmpty( userServiceCharValue ) )
          {
            for ( TcccCountryRule countryRule : countryRules )
            {
              if ( StringUtil.isNullOrEmpty( countryRule.getBusinessUnit() ) )
              {
                sendEmail = countryRule.isAllowOnlinePurl();
                break;
              }
            }
          }
          else
          {
            boolean matchFound = false;
            for ( TcccCountryRule countryRule : countryRules )
            {
              if ( userServiceCharValue.equals( countryRule.getBusinessUnit() ) )
              {
                sendEmail = countryRule.isAllowOnlinePurl();
                matchFound = true;
                break;
              }
            }
            if ( !matchFound )
            {
              for ( TcccCountryRule countryRule : countryRules )
              {
                if ( StringUtil.isNullOrEmpty( countryRule.getBusinessUnit() ) )
                {
                  sendEmail = countryRule.isAllowOnlinePurl();
                  break;
                }
              }
            }
          }
        }
        return sendEmail;
      }
      else
      {
        return false;
      }
    }
    else
    {
      return false;
    }
  }

  private int getAnniversaryYear( ParticipantEmployerValueBean anniversaryEmployeeValueBean )
  {
    return DateUtils.getNumberOfYears( anniversaryEmployeeValueBean.getHireDate() );
  }

  private void sendAnniversaryEmail( ParticipantEmployerValueBean employeeValueBean, Participant participant, boolean isAnnual )
  {
	
    Mailing mailing = composeMail( MessageService.COKE_NON_MILESTONE_SERVICE_ANNIVERSARY_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );
    mailing.addMailingRecipient( addRecipient( participant ) );

    Map<String, Object> objectMap = new HashMap<>();
   
    objectMap.put( "firstName", participant.getFirstName() );
    if ( isAnnual )
    {
      int year = getAnniversaryYear( employeeValueBean );
      objectMap.put( "anniversaryNumber", String.valueOf( year ) );
      if ( year == 1 )
        objectMap.put( "anniversaryLabel", CmsResourceBundle.getCmsBundle().getString( "client.nonmilestone.anniversary.YEAR" ) );
      else
        objectMap.put( "anniversaryLabel", CmsResourceBundle.getCmsBundle().getString( "client.nonmilestone.anniversary.YEARS" ) );
    }
    else
    {
      int months = DateUtils.monthsBetweenNowAndHireDate( employeeValueBean.getHireDate() );
      objectMap.put( "anniversaryNumber", String.valueOf( months ) );
      objectMap.put( "anniversaryLabel", CmsResourceBundle.getCmsBundle().getString( "client.nonmilestone.anniversary.MONTHS" ) );
    }
    mailingService.submitMailing( mailing, objectMap );
  }
   /*Custom for wip # 46521  starts*/
  private void buildCCsendAnniversaryEmail( ParticipantEmployerValueBean employeeValueBean, Participant participant, boolean isAnnual, boolean isManager )
  {
    Mailing mailing = composeMail( MessageService.COKE_NON_MILESTONE_SERVICE_ANNIVERSARY_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );
    
    User manager =   nodeService.getNodeOwnerForUser(participant, participant.getPrimaryUserNode().getNode() );
    mailing.addMailingRecipient( addRecipient( manager ) );
    
    Map<String, Object> objectMap = new HashMap<>();
    if( manager != null )
    {
    	 objectMap.put("manager", isManager);
    	 objectMap.put( "managerFirstName", manager.getFirstName() );
    	 objectMap.put("managerLastName", manager.getLastName() );
    	 objectMap.put( "RecipientfirstName", participant.getFirstName() );
    	 objectMap.put( "RecipientlastName", participant.getLastName() );
    }
   
    objectMap.put( "firstName", participant.getFirstName() );
    if ( isAnnual )
    {
      int year = getAnniversaryYear( employeeValueBean );
      objectMap.put( "anniversaryNumber", String.valueOf( year ) );
      if ( year == 1 )
        objectMap.put( "anniversaryLabel", CmsResourceBundle.getCmsBundle().getString( "client.nonmilestone.anniversary.YEAR" ) );
      else
        objectMap.put( "anniversaryLabel", CmsResourceBundle.getCmsBundle().getString( "client.nonmilestone.anniversary.YEARS" ) );
    }
    else
    {
      int months = DateUtils.monthsBetweenNowAndHireDate( employeeValueBean.getHireDate() );
      objectMap.put( "anniversaryNumber", String.valueOf( months ) );
      objectMap.put( "anniversaryLabel", CmsResourceBundle.getCmsBundle().getString( "client.nonmilestone.anniversary.MONTHS" ) );
    }
    mailingService.submitMailing( mailing, objectMap );
  }
  /*Custom for wip # 46521  end*/
  public Long getPriorDays()
  {
    return priorDays;
  }

  public void setPriorDays( Long priorDays )
  {
    this.priorDays = priorDays;
  }

  public Long getFutureDays()
  {
    return futureDays;
  }

  public void setFutureDays( Long futureDays )
  {
    this.futureDays = futureDays;
  }

  public CokeProcessService getCokeProcessService()
  {
    return cokeProcessService;
  }

  public void setCokeProcessService( CokeProcessService cokeProcessService )
  {
    this.cokeProcessService = cokeProcessService;
  }
  
  public void setNodeService ( NodeService nodeService )
  {
	  this.nodeService = nodeService;
  }
}
