
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;

/**
 * 
 * SSIContestPayoutObjectivesView.
 * 
 * @author kandhi
 * @since Dec 1, 2014
 * @version 1.0
 */
public class SSIContestPayoutObjectivesView
{
  private List<SSIContestMessageValueBean> messages;
  private SSIContestPayoutObjectivesContestView contestJson;

  public SSIContestPayoutObjectivesView( SSIContest ssiContest, String ssiContestClientState, List<Currency> currencies, String sysUrl, List<SSIContestBillCodeView> billCodes )
  {
    contestJson = new SSIContestPayoutObjectivesContestView( ssiContest, ssiContestClientState, currencies, sysUrl, billCodes );
  }

  public List<SSIContestMessageValueBean> getMessages()
  {
    return messages;
  }

  public void setMessages( List<SSIContestMessageValueBean> messages )
  {
    this.messages = messages;
  }

  public SSIContestPayoutObjectivesContestView getContestJson()
  {
    return contestJson;
  }

  public void setContestJson( SSIContestPayoutObjectivesContestView contestJson )
  {
    this.contestJson = contestJson;
  }

}
