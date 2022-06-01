
package com.biperf.core.value.hc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticipantSyncResponse implements Serializable
{
  private static final long serialVersionUID = 1L;

  /** Usernames of participants that were synced, mapped to data about that participant */
  private Map<String, ParticipantData> successfulUsernames = new HashMap<>();

  /** Usernames of participants that are not in the Honeycomb system */
  private List<String> missingParticipants = new ArrayList<>();

  public static class ParticipantData
  {
    private Long honeycombUserId;
    
    public Long getHoneycombUserId()
    {
      return honeycombUserId;
    }
    
    public void setHoneycombUserId( Long honeycombUserId )
    {
      this.honeycombUserId = honeycombUserId;
    }
  }
  
  public Map<String, ParticipantData> getSuccessfulUsernames()
  {
    return successfulUsernames;
  }

  public void setSuccessfulUsernames( Map<String, ParticipantData> successfulUsernames )
  {
    this.successfulUsernames = successfulUsernames;
  }

  public List<String> getMissingParticipants()
  {
    return missingParticipants;
  }

  public void setMissingParticipants( List<String> missingParticipants )
  {
    this.missingParticipants = missingParticipants;
  }

}