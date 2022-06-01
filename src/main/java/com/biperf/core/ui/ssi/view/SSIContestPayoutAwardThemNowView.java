
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;

/**
 * 
 * SSIContestPayoutAwardThemNowView.
 * 
 * @author patelp
 * @since Feb 05 2015
 * @version 1.0
 */

public class SSIContestPayoutAwardThemNowView
{
  private List<SSIContestMessageValueBean> messages = new ArrayList<SSIContestMessageValueBean>();
  private SSIContestPayoutAwardThemNowContestView contestJson;

  public SSIContestPayoutAwardThemNowView()
  {

  }

  public SSIContestPayoutAwardThemNowView( SSIContest ssiContest, String ssiContestClientState, List<Currency> currencies, String sysUrl, List<SSIContestBillCodeView> billCodes )
  {
    contestJson = new SSIContestPayoutAwardThemNowContestView( ssiContest, ssiContestClientState, currencies, sysUrl, billCodes );
  }

  public List<SSIContestMessageValueBean> getMessages()
  {
    return messages;
  }

  public void setMessages( List<SSIContestMessageValueBean> messages )
  {
    this.messages = messages;
  }

  public SSIContestPayoutAwardThemNowContestView getContestJson()
  {
    return contestJson;
  }

  public void setContestJson( SSIContestPayoutAwardThemNowContestView contestJson )
  {
    this.contestJson = contestJson;
  }

}
