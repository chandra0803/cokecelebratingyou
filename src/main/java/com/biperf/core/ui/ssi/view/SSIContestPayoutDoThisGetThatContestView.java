
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.utils.PageConstants;

/**
 * 
 * SSIContestPayoutDoThisGetThatContestView.
 * 
 * @author kandhi
 * @since Dec 22, 2014
 * @version 1.0
 */
public class SSIContestPayoutDoThisGetThatContestView extends SSIContestPayoutContestView
{
  private int participantCount;
  private List<SSIContestActivityView> activities;

  public SSIContestPayoutDoThisGetThatContestView( SSIContest ssiContest,
                                                   String ssiContestClientState,
                                                   int participantsCount,
                                                   List<Currency> currencies,
                                                   String sysUrl,
                                                   List<SSIContestBillCodeView> billCodes )
  {
    super( ssiContest, ssiContestClientState, currencies, billCodes );
    setNextUrl( sysUrl + PageConstants.SSI_MANAGE_DO_THIS_GET_THAT );
    this.participantCount = participantsCount;
    if ( ssiContest.getContestActivities() != null )
    {
      activities = new ArrayList<SSIContestActivityView>();
      for ( SSIContestActivity ssiContestActivity : ssiContest.getContestActivities() )
      {
        activities.add( new SSIContestActivityView( ssiContest, ssiContestActivity, participantsCount ) );
      }
      Collections.sort( activities );
    }
  }

  public int getParticipantCount()
  {
    return participantCount;
  }

  public void setParticipantCount( int participantCount )
  {
    this.participantCount = participantCount;
  }

  public List<SSIContestActivityView> getActivities()
  {
    return activities;
  }

  public void setActivities( List<SSIContestActivityView> activities )
  {
    this.activities = activities;
  }

}
