
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * SSIContestPayoutObjectivesView.
 * 
 * @author Patel
 * @since May 12, 2015
 * @version 1.0
 */
@JsonInclude( value = Include.NON_NULL )
public class SSIContestDataCollectionView
{
  private List<SSIContestMessageValueBean> messages;
  private SSIContestDataCollectionDataView contestJson;

  public SSIContestDataCollectionView( SSIContest contest, String ssiContestClientState, ClaimForm claimForm, String sysUrl )
  {
    contestJson = new SSIContestDataCollectionDataView( contest, ssiContestClientState, claimForm, sysUrl );
  }

  public List<SSIContestMessageValueBean> getMessages()
  {
    return messages;
  }

  public void setMessages( List<SSIContestMessageValueBean> messages )
  {
    this.messages = messages;
  }

  public SSIContestDataCollectionDataView getContestJson()
  {
    return contestJson;
  }

  public void setContestJson( SSIContestDataCollectionDataView contestJson )
  {
    this.contestJson = contestJson;
  }

}
