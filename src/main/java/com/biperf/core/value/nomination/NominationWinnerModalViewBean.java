
package com.biperf.core.value.nomination;

import java.util.ArrayList;
import java.util.List;

public class NominationWinnerModalViewBean
{
  private List<NominationWinnerModalDetailsViewBean> messages = new ArrayList<NominationWinnerModalDetailsViewBean>();

  public List<NominationWinnerModalDetailsViewBean> getMessages()
  {
    return messages;
  }

  public void setMessages( List<NominationWinnerModalDetailsViewBean> messages )
  {
    this.messages = messages;
  }
}
