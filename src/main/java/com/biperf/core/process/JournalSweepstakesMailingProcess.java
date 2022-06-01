
package com.biperf.core.process;

import java.util.LinkedHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.process.Process;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;

/*
 * Created as part of Bug 73458
 * transactionalProcessProxyFactoryParent
 */
public class JournalSweepstakesMailingProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( JournalSweepstakesMailingProcess.class );

  public static final String BEAN_NAME = "journalSweepstakesMailingProcess";
  public static final String PROCESS_NAME = "Journal Sweepstakes Mailing Process";

  private JournalService journalService;

  // properties set from jobDataMap
  private String journalId;

  @Override
  protected void onExecute()
  {
    try
    {

      AssociationRequestCollection assocs = new AssociationRequestCollection();
      // Did not include budget. Not needed and may cause slowness.
      assocs.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION ) );
      assocs.add( new JournalAssociationRequest( JournalAssociationRequest.ACTIVITY_JOURNALS ) );
      assocs.add( new JournalAssociationRequest( JournalAssociationRequest.PARTICIPANT ) );
      assocs.add( new JournalAssociationRequest( JournalAssociationRequest.BILL_CODES ) );

      Journal journalEntry = journalService.getJournalById( new Long( getJournalId() ), assocs );

      Mailing mailing = journalService.buildSweepstakeMailing( journalEntry );
      mailingService.submitMailing( mailing, null );

      addComment( "Sweepstake mailing submitted for journal id : " + journalEntry.getId() );

    }
    catch( Exception e )
    {
      addComment( "Exception occurred for journal id: " + getJournalId() + " " + e.getMessage() );
      log.error( "Exception occurred for journal id: " + getJournalId(), e );
    }

  }

  public static void launchJournalSweepsMailingProcess( Long journalId )
  {
    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "journalId", new String[] { journalId.toString() } );

    ProcessService processService = (ProcessService)BeanLocator.getBean( ProcessService.BEAN_NAME );

    Process process = processService.createOrLoadSystemProcess( JournalSweepstakesMailingProcess.PROCESS_NAME, JournalSweepstakesMailingProcess.BEAN_NAME );

    processService.launchProcess( process, parameterValueMap, UserManager.getUserId() );
  }

  public String getJournalId()
  {
    return journalId;
  }

  public void setJournalId( String journalId )
  {
    this.journalId = journalId;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

}
