/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/process/ChallengepointWelcomeCountProcess.java,v $
 */

package com.biperf.core.process;

import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.domain.participant.Participant;

/**
 *
 */
public class ChallengepointWelcomeCountProcess extends ChallengepointWelcomeProcess
{
  /**
   * Bean Name
   */
  public static final String BEAN_NAME = "challengePointWelcomeCountProcess";

  public ChallengepointWelcomeCountProcess()
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
