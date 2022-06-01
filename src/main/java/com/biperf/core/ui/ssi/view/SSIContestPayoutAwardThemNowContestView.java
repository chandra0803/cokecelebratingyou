
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.ssi.SSIContest;

/**
 * 
 * SSIContestPayoutAwardThemNowContestView.
 * 
 * @author Patelp
 * @since Feb 5, 2015
 * @version 1.0
 */

public class SSIContestPayoutAwardThemNowContestView extends SSIContestPayoutContestView
{

  public SSIContestPayoutAwardThemNowContestView( SSIContest ssiContest, String ssiContestClientState, List<Currency> currencies, String sysUrl, List<SSIContestBillCodeView> billCodes )
  {
    super( ssiContest, ssiContestClientState, currencies, billCodes );
    setNextUrl( sysUrl + "/ssi/manageContestPayoutAwardThemNow.do" );
  }

}
