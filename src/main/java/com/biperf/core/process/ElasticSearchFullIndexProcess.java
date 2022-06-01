
package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.participant.AutoCompleteService;

public class ElasticSearchFullIndexProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( ElasticSearchFullIndexProcess.class );

  public static final String BEAN_NAME = "elasticSearchFullIndexProcess";

  private AutoCompleteService autoCompleteService = null;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  protected void onExecute()
  {
    try
    {
      Long indexCount = autoCompleteService.indexAllActiveParticipants();
      addComment( "Successfully indexed " + indexCount + " Participants" );
    }
    catch( Exception e )
    {
      log.error( e );
      addComment( "An Exception occurred. " + "\n" + e.toString() + "\n" + " See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  public AutoCompleteService getAutoCompleteService()
  {
    return autoCompleteService;
  }

  public void setAutoCompleteService( AutoCompleteService autoCompleteService )
  {
    this.autoCompleteService = autoCompleteService;
  }
}
