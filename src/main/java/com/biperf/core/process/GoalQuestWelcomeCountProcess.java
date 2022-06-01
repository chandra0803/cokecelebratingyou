/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/process/GoalQuestWelcomeCountProcess.java,v $
 */

package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.domain.participant.Participant;

/**
 *
 */
public class GoalQuestWelcomeCountProcess extends GoalQuestWelcomeProcess
{
  /**
   * Bean Name
   */
  public static final String BEAN_NAME = "goalQuestWelcomeCountProcess";

  private static final Log log = LogFactory.getLog( GoalQuestWelcomeCountProcess.class );

  public GoalQuestWelcomeCountProcess()
  {
    super();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    super.onExecute();
  }

  protected MailingBatch applyBatch( String processName )
  {
    return null;
  }

  protected void updateUser( Participant participant )
  {

  }

  public boolean isNotice()
  {
    return true;
  }

}
