
package com.biperf.core.process;

import java.util.LinkedHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;

/**
 * AwardBanQDepositRetryProcess is a process that retries depositing a journal entry to AwardBanQ.
 * 
 *
 */
public class AwardBanQDepositRetryProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( AwardBanQDepositRetryProcess.class );

  public static final String PROCESS_NAME = "AwardBanQ Deposit Retry Process";
  public static final String BEAN_NAME = "awardBanQDepositRetryProcess";

  private Long journalId = null;
  private int retry = 0;
  private JournalService journalService;

  public void setJournalId( String journalId )
  {
    this.journalId = new Long( journalId );
  }

  public void setRetry( String retry )
  {
    this.retry = Integer.parseInt( retry );
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  /**
   * If retries are enabled, launch a retry process and return true. If retries are not enabled,
   * return false.
   * 
   * @param journalEntry
   * @return
   */
  public static boolean launchDepositRetryProcess( Journal journalEntry )
  {
    boolean isRetriable = false;

    if ( getMaxRetryCount() > 0 )
    {
      isRetriable = true;
      reschedule( journalEntry.getId(), 1 );
    }

    return isRetriable;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  protected void onExecute()
  {
    log.debug( "process " + getProcessInvocationId() + ":" + toString() );

    try
    {
      execute();
    }
    catch( Exception e )
    {
      log.error( "Unable to execute process invocation " + getProcessInvocationId(), e );
      addComment( "An exception occurred while retrying an AwardBanQ deposit.  " + "See the log file for additional information." );
    }
  }

  private void execute()
  {
    int maxRetryCount = getMaxRetryCount();
    boolean isRetriable = retry + 1 <= maxRetryCount;

    try
    {
      if ( retryDeposit( isRetriable ) )
      {
        addComment( "Journal ID " + journalId + " Retry #" + retry + " was successful." );
      }
      else
      {
        // another failure
        addComment( "Journal ID " + journalId + " Retry #" + retry + " (out of max " + maxRetryCount + ") failed.  See the log file for additional information." );

        if ( isRetriable )
        {
          reschedule( journalId, retry + 1 );
        }
      }
    }
    catch( ServiceErrorException e )
    {
      log.error( e.getServiceErrorsCMText() );
    }
  }

  private boolean retryDeposit( boolean isRetriable ) throws ServiceErrorException
  {
    return journalService.processDepositRetryJournal( journalId, isRetriable );
  }

  private static void reschedule( Long journalId, int retry )
  {
    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "journalId", new String[] { journalId.toString() } );
    parameterValueMap.put( "retry", new String[] { Integer.toString( retry ) } );

    ProcessService processService = (ProcessService)BeanLocator.getBean( ProcessService.BEAN_NAME );

    Process process = processService.createOrLoadSystemProcess( AwardBanQDepositRetryProcess.PROCESS_NAME, AwardBanQDepositRetryProcess.BEAN_NAME );

    processService.launchProcess( process, parameterValueMap, UserManager.getUserId(), new Long( getRetryDelay() ) );
  }

  private static int getMaxRetryCount()
  {
    int maxRetryCount = 3;

    SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );

    PropertySetItem property = systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_DEPOSIT_RETRY_COUNT );
    if ( property != null )
    {
      maxRetryCount = property.getIntVal();
    }

    return maxRetryCount;
  }

  private static long getRetryDelay()
  {
    long retryDelay = 600;

    SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );

    PropertySetItem property = systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_DEPOSIT_RETRY_DELAY );
    if ( property != null )
    {
      retryDelay = property.getLongVal();
    }

    return retryDelay * 1000;
  }
}
