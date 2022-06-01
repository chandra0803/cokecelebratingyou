
package com.biperf.core.ui.engagement;

import java.util.List;

/**
 * 
 * EngagementRecognizedTreeView.
 * 
 * @author kandhi
 * @since Jun 23, 2014
 * @version 1.0
 */
public class EngagementRecognizedTreeView
{
  private EngagementRecognizedRootView root;
  private List<EngagementRecognizedParticipantView> participants;

  public EngagementRecognizedRootView getRoot()
  {
    return root;
  }

  public void setRoot( EngagementRecognizedRootView root )
  {
    this.root = root;
  }

  public List<EngagementRecognizedParticipantView> getParticipants()
  {
    return participants;
  }

  public void setParticipants( List<EngagementRecognizedParticipantView> participants )
  {
    this.participants = participants;
  }

}
