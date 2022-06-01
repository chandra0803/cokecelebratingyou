
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;

/**
 * SSIContestPayoutStackRankView.
 * 
 * @author dudam
 * @since Feb 16, 2015
 * @version 1.0
 */
public class SSIContestPayoutStackRankView
{
  private List<SSIContestMessageValueBean> messages;
  private SSIContestPayoutStackRankContestView contestJson;

  public SSIContestPayoutStackRankView( SSIContest contest, String clientState, List<Currency> currencies, String sysUrl, List<SSIContestBillCodeView> billCodes )
  {
    this.contestJson = new SSIContestPayoutStackRankContestView( contest, clientState, currencies, sysUrl, billCodes );
  }

  public List<SSIContestMessageValueBean> getMessages()
  {
    return messages;
  }

  public void setMessages( List<SSIContestMessageValueBean> messages )
  {
    this.messages = messages;
  }

  public SSIContestPayoutStackRankContestView getContestJson()
  {
    return contestJson;
  }

  public void setContestJson( SSIContestPayoutStackRankContestView contestJson )
  {
    this.contestJson = contestJson;
  }

}
