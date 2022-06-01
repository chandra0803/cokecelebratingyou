
package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RecreateCriteriaAudienceParticipantsProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( RecreateCriteriaAudienceParticipantsProcess.class );

  public static final String BEAN_NAME = "recreateCriteriaAudienceParticipantsProcess";
  public static final String MESSAGE_NAME = "Recreate Criteria Audience Participants";

  // properties set from jobDataMap
  String audienceId;

  @Override
  protected void onExecute()
  {
    try
    {
      getAudienceService().recreateCriteriaAudienceParticipants( new Long( audienceId ) );

      String comment = "Recreate Criteria Audience Participants completed successfully. Audience ID is " + audienceId;
      addComment( comment );
    }
    catch( Exception e )
    {
      logErrorMessage( e );

      String comment = "An exception occurred while recreating the participant list for criteria audiences. Audience ID is " + audienceId;
      addComment( comment );
    }
  }

  public void setAudienceId( String audienceId )
  {
    this.audienceId = audienceId;
  }

}
