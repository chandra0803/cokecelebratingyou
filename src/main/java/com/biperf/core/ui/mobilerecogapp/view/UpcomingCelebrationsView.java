
package com.biperf.core.ui.mobilerecogapp.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.purl.PurlCelebration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class UpcomingCelebrationsView
{

  List<PurlCelebration> celebrations = new ArrayList<PurlCelebration>();
  private int celebrationCount;
  private boolean newSAEnabled;

  public List<PurlCelebration> getCelebrations()
  {
    return celebrations;
  }

  public void setCelebrations( List<PurlCelebration> celebrations )
  {
    this.celebrations = celebrations;
  }

  public int getCelebrationCount()
  {
    return celebrationCount;
  }

  public void setCelebrationCount( int celebrationCount )
  {
    this.celebrationCount = celebrationCount;
  }

  public boolean isNewSAEnabled()
  {
    return newSAEnabled;
  }

  public void setNewSAEnabled( boolean newSAEnabled )
  {
    this.newSAEnabled = newSAEnabled;
  }

}
