
package com.biperf.core.value.nomination;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class NominationPastWinnersViewBean
{
  List<String> messages = new ArrayList<String>();

  List<NominationApprovalsViewBean> nominationApprovals = new ArrayList<NominationApprovalsViewBean>();

  public List<String> getMessages()
  {
    return messages;
  }

  public void setMessages( List<String> messages )
  {
    this.messages = messages;
  }

  public List<NominationApprovalsViewBean> getNominationApprovals()
  {
    return nominationApprovals;
  }

  public void setNominationApprovals( List<NominationApprovalsViewBean> nominationApprovals )
  {
    this.nominationApprovals = nominationApprovals;
  }
}
