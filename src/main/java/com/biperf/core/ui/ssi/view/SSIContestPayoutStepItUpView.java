
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;

/**
 * 
 * SSIContestPayoutStepItUpView.
 * 
 * @author patelp
 * @since Jan 9, 2015
 * @version 1.0
 */

public class SSIContestPayoutStepItUpView
{
  private List<SSIContestMessageValueBean> messages;
  private SSIContestPayoutStepItUpContestView contestJson;

  public SSIContestPayoutStepItUpView( SSIContest contest, String clientState, List<Currency> currencies, String sysUrl, List<SSIContestBillCodeView> billCodes )
  {
    this.contestJson = new SSIContestPayoutStepItUpContestView( contest, clientState, currencies, sysUrl, billCodes );
  }

  public List<SSIContestMessageValueBean> getMessages()
  {
    return messages;
  }

  public void setMessages( List<SSIContestMessageValueBean> messages )
  {
    this.messages = messages;
  }

  public SSIContestPayoutStepItUpContestView getContestJson()
  {
    return contestJson;
  }

  public void setContestJson( SSIContestPayoutStepItUpContestView contestJson )
  {
    this.contestJson = contestJson;
  }

}
