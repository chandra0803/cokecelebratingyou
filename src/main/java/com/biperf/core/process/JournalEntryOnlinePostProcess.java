
package com.biperf.core.process;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.promotion.PostProcessJobs;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.promotion.PostProcessJobsService;

public class JournalEntryOnlinePostProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( JournalEntryOnlinePostProcess.class );

  public static final String BEAN_NAME = "journalEntryOnlinePostProcess";
  public static final String PROCESS_NAME = "Journal Entry Online Post Process";

  private JournalService journalService;
  private PostProcessJobsService postProcessJobsService;

  // properties set from jobDataMap
  private String postProcessJobsId;

  @SuppressWarnings( "unchecked" )
  @Override
  protected void onExecute()
  {
    PostProcessJobs postProcessJobs = null;

    try
    {
      postProcessJobs = postProcessJobsService.getPostProcessJobsById( new Long( postProcessJobsId ) );

      if ( !postProcessJobs.isFired() )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION ) );

        Journal journalEntry = journalService.getJournalById( postProcessJobs.getJournalId(), associationRequestCollection );
        journalService.claimJournalPostProcess( journalEntry );

        postProcessJobs.setFired( true );
        postProcessJobs.setFiredDate( new Date() );
        postProcessJobsService.savePostProcessJobs( postProcessJobs );

        addComment( "postProcessJobsId : " + postProcessJobsId + " journalId : " + journalEntry.getId() + " processed successfully" );
      }
      else
      {
        addComment( "postProcessJobsId : " + postProcessJobsId + " has already been processed" );
      }
    }
    catch( Exception e )
    {
      Long journalId = ( postProcessJobs != null ) ? postProcessJobs.getJournalId() : null;
      String msg = buildExceptionMessage( e, journalId );
      addComment( msg );
      log.error( msg, e );
    }
  }

  private String buildExceptionMessage( Exception e, Long journalId )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "Exception occured on postProcessJobsId: " + postProcessJobsId + "." );
    sb.append( "This could happen if the Journal wasn't saved correctly. Please verify that the Journal with Id " + journalId + " exists in the database: Exception Message: " );
    sb.append( e.getMessage() );
    return sb.toString();
  }

  public String getPostProcessJobsId()
  {
    return postProcessJobsId;
  }

  public void setPostProcessJobsId( String postProcessJobsId )
  {
    this.postProcessJobsId = postProcessJobsId;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public void setPostProcessJobsService( PostProcessJobsService postProcessJobsService )
  {
    this.postProcessJobsService = postProcessJobsService;
  }

}
