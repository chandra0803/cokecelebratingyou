
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;

/**
 * 
 * SSIContestPayoutDoThisGetThatView.
 * 
 * @author kandhi
 * @since Dec 22, 2014
 * @version 1.0
 */
public class SSIContestPayoutDoThisGetThatView
{
  private List<SSIContestMessageValueBean> messages;
  private SSIContestPayoutDoThisGetThatContestView contestJson;

  public SSIContestPayoutDoThisGetThatView( SSIContest ssiContest,
                                            String ssiContestClientState,
                                            int participantsCount,
                                            List<Currency> currencies,
                                            String sysUrl,
                                            List<SSIContestBillCodeView> billCodes )
  {
    contestJson = new SSIContestPayoutDoThisGetThatContestView( ssiContest, ssiContestClientState, participantsCount, currencies, sysUrl, billCodes );
  }

  public List<SSIContestMessageValueBean> getMessages()
  {
    return messages;
  }

  public void setMessages( List<SSIContestMessageValueBean> messages )
  {
    this.messages = messages;
  }

  public SSIContestPayoutDoThisGetThatContestView getContestJson()
  {
    return contestJson;
  }

  public void setContestJson( SSIContestPayoutDoThisGetThatContestView contestJson )
  {
    this.contestJson = contestJson;
  }
}
