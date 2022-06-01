
package com.biperf.core.process;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.service.ssi.SSIContestService;

/**
 * This process updates status of ssi contest from pending to live if the contest start date is today
 */
public class SSIContestLaunchProcess extends BaseProcessImpl
{
  public static final String PROCESS_NAME = "SSI Contest Launch Process";
  public static final String BEAN_NAME = "ssiContestLaunchProcess";

  private SSIContestService ssiContestService;

  protected void onExecute()
  {
    int count = 0;
    try
    {
      List<SSIContest> contests = ssiContestService.getAllContestsToLaunch( SSIContestStatus.lookup( SSIContestStatus.PENDING ) );
      if ( contests != null && contests.size() > 0 )
      {
        for ( Iterator<SSIContest> iter = contests.iterator(); iter.hasNext(); )
        {
          SSIContest ssiContest = (SSIContest)iter.next();
          ssiContest.setStatus( SSIContestStatus.lookup( SSIContestStatus.LIVE ) );
          ssiContest.setDateLaunched( new Date() );
          ssiContestService.saveContest( ssiContest );
          count++;
        }
      }
    }
    catch( Exception e )
    {
      logErrorMessage( e );
      String errorMessage = "An exception occurred while running the process. Please See the log file for additional information." + "(process invocation ID = " + getProcessInvocationId() + ")";
      addComment( errorMessage );
    }
    addComment( "Total number of contests that have been updated from pending to live = " + count );

  }

  public void setSsiContestService( SSIContestService ssiContestService )
  {
    this.ssiContestService = ssiContestService;
  }

}
