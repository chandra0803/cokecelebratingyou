
package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.underarmour.UnderArmourService;
import com.biperf.core.service.underarmour.impl.GetActigraphyResult;

/**
 * GoalQuestUADataRetrievalProcess.
 * 
 * The UA data is retried from UA micro service based on user request and
 * processed against from the promotion which is applicable, the manipulated
 * data will be stored in client database
 */
public class GoalQuestUADataRetrievalProcess extends BaseProcessImpl
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The name used to get an object of this class from the Spring application
   * context.
   */
  public static final String BEAN_NAME = "goalQuestUADataRetrievalProcess";

  private static final Log log = LogFactory.getLog( GoalQuestUADataRetrievalProcess.class );

  private UnderArmourService underArmourService;

  // ---------------------------------------------------------------------------
  // Process Methods
  // ---------------------------------------------------------------------------

  protected void onExecute()
  {

    // If required parameters are missing, write invocation log and stop.
    if ( isRequiredProcessParametersMissing() )
    {
      String msg = new String( "Required Parameters missing while executing " + BEAN_NAME + " is null. Process invocation ID = " + getProcessInvocationId() );
      log.warn( msg );
      addComment( msg );
    }
    else
    {
      try
      {
        GetActigraphyResult result = underArmourService.getActigraphyData();
        addComment( "Number of participants updated: " + result.getNumberParticipantsUpdated() );
      }
      catch( Exception e )
      {
        StringBuffer failureMsg = new StringBuffer( "Error Occurred while processing the GoalQuest Process." + " The error caused by: " );
        failureMsg.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
        log.error( failureMsg, e );
        addComment( failureMsg.toString() );
      }
    }
  } // END onExecute()

  /**
   * Not all Process Parameters for this process are required.
   * 
   * @return true if all required parameters are found, else false
   */
  private boolean isRequiredProcessParametersMissing()
  {
    return false;
  }

  public void setUnderArmourService( UnderArmourService underArmourService )
  {
    this.underArmourService = underArmourService;
  }

}
