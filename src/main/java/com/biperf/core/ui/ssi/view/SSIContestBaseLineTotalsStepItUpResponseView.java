
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

public class SSIContestBaseLineTotalsStepItUpResponseView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private SSIContestBaseLineTotalsView contestJson;

  public SSIContestBaseLineTotalsStepItUpResponseView( Double baselineTotal )
  {
    this.contestJson = new SSIContestBaseLineTotalsView( baselineTotal );
  }

  public SSIContestBaseLineTotalsStepItUpResponseView( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public SSIContestBaseLineTotalsView getContestJson()
  {
    return contestJson;
  }

  public void setContestJson( SSIContestBaseLineTotalsView contestJson )
  {
    this.contestJson = contestJson;
  }

  public class SSIContestBaseLineTotalsView
  {
    private Double baselineTotal;

    public SSIContestBaseLineTotalsView( Double baselineTotal )
    {
      this.baselineTotal = baselineTotal;
    }

    public Double getBaselineTotal()
    {
      return baselineTotal;
    }

    public void setBaselineTotal( Double baselineTotal )
    {
      this.baselineTotal = baselineTotal;
    }

  }
}
