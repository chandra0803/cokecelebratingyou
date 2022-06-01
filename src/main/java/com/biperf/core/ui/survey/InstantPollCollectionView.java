
package com.biperf.core.ui.survey;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.promotion.InstantPoll;

public class InstantPollCollectionView
{
  private String[] messages = {};
  private List<InstantPollValueBean> polls = new ArrayList<InstantPollValueBean>();

  public InstantPollCollectionView( List<InstantPoll> instantPolls )
  {
    for ( InstantPoll instantPoll : instantPolls )
    {
      InstantPollValueBean installPollBean = new InstantPollValueBean( instantPoll );
      polls.add( installPollBean );
    }
  }

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public List<InstantPollValueBean> getPolls()
  {
    return polls;
  }

  public void setPolls( List<InstantPollValueBean> polls )
  {
    this.polls = polls;
  }

}
