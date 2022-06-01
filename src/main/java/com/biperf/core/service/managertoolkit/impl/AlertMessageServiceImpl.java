
package com.biperf.core.service.managertoolkit.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.managertoolkit.AlertMessageDAO;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.managertoolkit.AlertMessage;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.ManagerSendAlertProcess;
import com.biperf.core.service.managertoolkit.AlertMessageService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.ssi.SSIContestPaxClaimService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.UserManager;

public class AlertMessageServiceImpl implements AlertMessageService
{
  private AlertMessageDAO alertMessageDAO;

  private ProcessService processService;
  private SSIContestPaxClaimService ssiContestPaxClaimService;

  public List getActiveAlertMessagesByUserId( Long userId )
  {
    List<ParticipantAlert> alertMessageList = alertMessageDAO.getAlertMessageByUserId( userId );
    Set<Long> contestIds = getSsiContestPaxClaimService().getPaxClaimsForApprovalByContestId( alertMessageList );

    for ( Iterator<ParticipantAlert> iter = alertMessageList.iterator(); iter.hasNext(); )
    {
      ParticipantAlert paxAlert = iter.next();
      try
      {
        DateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd" );
        Date today = new Date();

        Date todayWithZeroTime = formatter.parse( formatter.format( today ) );

        if ( paxAlert.getAlertMessage().getExpiryDate() != null && paxAlert.getAlertMessage().getExpiryDate().before( todayWithZeroTime ) )
        {
          iter.remove();
        }
        else if ( paxAlert.getAlertMessage().getSsiAlertType() != null && paxAlert.getAlertMessage().getSsiAlertType().equalsIgnoreCase( SSIContest.CONTEST_ROLE_CLAIM_APPROVER )
            && ( !contestIds.contains( paxAlert.getAlertMessage().getContestId() ) ) )
        {
          iter.remove();
        }

      }
      catch( ParseException e )
      {

        e.printStackTrace();
      }
    }

    return alertMessageList;
  }

  public void createManagerSendAlert( AlertMessage alertMessage, Long managerId, Long proxyId, boolean includeChildNode )
  {
    // schedule job
    Process process = processService.createOrLoadSystemProcess( ManagerSendAlertProcess.PROCESS_NAME, ManagerSendAlertProcess.BEAN_NAME );

    Boolean orgUnitAndBelow = (Boolean)includeChildNode;

    String subject = "";
    String message = "";
    String messageTo = "";
    String date = "";
    Boolean sendEmail = new Boolean( false );

    if ( alertMessage != null )
    {
      subject = alertMessage.getSubject();
      message = alertMessage.getMessage();
      messageTo = alertMessage.getMessageTo();
      date = DateUtils.toDisplayString( alertMessage.getExpiryDate(), LocaleUtils.getLocale( UserManager.getLocale().toString() ) );
      sendEmail = Boolean.valueOf( alertMessage.isSendEmail() );
    }

    LinkedHashMap parameterValueMap = new LinkedHashMap();

    parameterValueMap.put( "managerId", new String[] { managerId.toString() } );

    parameterValueMap.put( "orgUnitAndBelow", new String[] { orgUnitAndBelow.toString() } );

    parameterValueMap.put( "subject", new String[] { subject } );
    parameterValueMap.put( "date", new String[] { date } );
    parameterValueMap.put( "message", new String[] { message } );
    parameterValueMap.put( "messageTo", new String[] { messageTo } );
    parameterValueMap.put( "sendEmail", new String[] { sendEmail.toString() } );
    if ( proxyId != null )
    {
      parameterValueMap.put( "proxyId", new String[] { proxyId.toString() } );
    }

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( new Date() );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    processService.scheduleProcess( process, processSchedule, parameterValueMap, managerId );

  }

  public AlertMessage getAlertMessageById( Long id ) throws ServiceErrorException
  {
    return alertMessageDAO.getAlertMessageById( id );
  }

  public List<AlertMessage> getAlertMessageByContestId( Long contestId )
  {
    return alertMessageDAO.getAlertMessageByContestId( contestId );
  }

  public void setAlertMessageDAO( AlertMessageDAO alertMessageDAO )
  {
    this.alertMessageDAO = alertMessageDAO;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  public SSIContestPaxClaimService getSsiContestPaxClaimService()
  {
    return ssiContestPaxClaimService;
  }

  public void setSsiContestPaxClaimService( SSIContestPaxClaimService ssiContestPaxClaimService )
  {
    this.ssiContestPaxClaimService = ssiContestPaxClaimService;
  }

}
