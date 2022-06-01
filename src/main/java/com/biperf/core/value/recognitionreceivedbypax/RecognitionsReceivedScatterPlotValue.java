/**
 * 
 */

package com.biperf.core.value.recognitionreceivedbypax;

import java.util.ArrayList;
import java.util.List;

/**
 * @author poddutur
 *
 */
public class RecognitionsReceivedScatterPlotValue
{
  private Long recognitionCount;
  private Long daysSinceLastRec;
  private String participantName;
  private List<String> participants = new ArrayList<String>();

  public RecognitionsReceivedScatterPlotValue()
  {
    super();
  }

  public Long getRecognitionCount()
  {
    return recognitionCount;
  }

  public void setRecognitionCount( Long recognitionCount )
  {
    this.recognitionCount = recognitionCount;
  }

  public Long getDaysSinceLastRec()
  {
    return daysSinceLastRec;
  }

  public void setDaysSinceLastRec( Long daysSinceLastRec )
  {
    this.daysSinceLastRec = daysSinceLastRec;
  }

  public String getParticipantName()
  {
    return participantName;
  }

  public void setParticipantName( String participantName )
  {
    this.participantName = participantName;
  }

  public List<String> getParticipants()
  {
    return participants;
  }

  public void setParticipants( List<String> participants )
  {
    this.participants = participants;
  }

}
