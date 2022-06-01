package com.biperf.core.value.hc;

import java.util.LinkedList;
import java.util.List;

public class AccountSyncRequest
{
  
  private List<AccountSyncParticipantDetails> participantDetails = new LinkedList<>();

  public List<AccountSyncParticipantDetails> getParticipantDetails()
  {
    return participantDetails;
  }

  public void setParticipantDetails( List<AccountSyncParticipantDetails> participantDetails )
  {
    this.participantDetails = participantDetails;
  }

}
