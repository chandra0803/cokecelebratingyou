
package com.biperf.core.value.diycommunication;

import java.util.ArrayList;
import java.util.List;

public class SelectAudienceParticipantData
{
  private SelectAudienceData audienceTable = new SelectAudienceData();

  public SelectAudienceParticipantData()
  {

  }

  public void setAudienceTable( SelectAudienceData audienceTable )
  {
    this.audienceTable = audienceTable;
  }

  public SelectAudienceData getAudienceTable()
  {
    return audienceTable;
  }

  public class SelectAudienceData
  {
    private List<AudienceResults> results = new ArrayList<AudienceResults>();

    public List<AudienceResults> getResults()
    {
      return results;
    }

    public void setResults( List<AudienceResults> results )
    {
      this.results = results;
    }
  }

}
