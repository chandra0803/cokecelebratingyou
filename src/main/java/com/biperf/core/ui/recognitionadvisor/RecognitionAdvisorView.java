
package com.biperf.core.ui.recognitionadvisor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_EMPTY )
public class RecognitionAdvisorView implements Serializable
{
  private static final long serialVersionUID = 1L;
  @JsonProperty( "responseCode" )
  private int responseCode;
  @JsonProperty( "responseMessage" )
  private String responseMessage;
  @JsonProperty( "content" )
  private String content;
  @JsonProperty( "raModel" )
  private boolean raModel;
  @JsonProperty( "raTotalParticipants" )
  private Long raTotalParticipants;
  @JsonProperty( "messages" )
  private Message messages;
  @JsonProperty( "raEndModal" )
  private boolean raEndModal;
  @JsonProperty( "raEndModalTotalParticipants" )
  private Long raEndModalTotalParticipants;
  @JsonProperty( "raReminderParticipants" )
  RAReminderParticipantsValueBean raReminderParticipants;
  @JsonProperty( "tableRowNumStart" )
  private Long tableRowNumStart;
  @JsonProperty( "tableRowNumEnd" )
  private Long tableRowNumEnd;
  @JsonProperty( "newHireAvailable" )
  private String newHireAvailable;
  @JsonProperty( "overDueAvailable" )
  private String overDueAvailable;
  @JsonProperty( "upComingAvailable" )
  private String upComingAvailable;
  private Integer newHireTotalcount;
  private Integer overDueTotalcount;

  public Message getMessages()
  {
    return messages;
  }

  public void setMessages( Message messages )

  {
    this.messages = messages;
  }

  public int getResponseCode()
  {
    return responseCode;
  }

  public String getResponseMessage()
  {
    return responseMessage;
  }

  public String getContent()
  {
    return content;
  }

  public void setContent( String content )
  {
    this.content = content;
  }

  public boolean isRaModel()
  {
    return raModel;
  }

  public void setRaModel( boolean raModel )
  {
    this.raModel = raModel;
  }

  public Long getRaTotalParticipants()
  {
    return raTotalParticipants;
  }

  public void setRaTotalParticipants( Long raTotalParticipants )
  {
    this.raTotalParticipants = raTotalParticipants;
  }

  public void setResponseCode( int responseCode )
  {
    this.responseCode = responseCode;
  }

  public void setResponseMessage( String responseMessage )
  {
    this.responseMessage = responseMessage;
  }

  class Message
  {
    String[] messages;

    public String[] getMessages()
    {
      return messages;
    }

    public void setMessages( String[] messages )
    {
      this.messages = messages;
    }

  }

  public RAReminderParticipantsValueBean getRaReminderParticipants()
  {
    return raReminderParticipants;
  }

  public void setRaReminderParticipants( RAReminderParticipantsValueBean raReminderParticipants )
  {
    this.raReminderParticipants = raReminderParticipants;
  }

  public Long getRaEndModalTotalParticipants()
  {
    return raEndModalTotalParticipants;
  }

  public void setRaEndModalTotalParticipants( Long raEndModalTotalParticipants )
  {
    this.raEndModalTotalParticipants = raEndModalTotalParticipants;
  }

  public boolean isRaEndModal()
  {
    return raEndModal;
  }

  public void setRaEndModal( boolean raEndModal )
  {
    this.raEndModal = raEndModal;
  }

  public Long getTableRowNumStart()
  {
    return tableRowNumStart;
  }

  public void setTableRowNumStart( Long tableRowNumStart )
  {
    this.tableRowNumStart = tableRowNumStart;
  }

  public Long getTableRowNumEnd()
  {
    return tableRowNumEnd;
  }

  public void setTableRowNumEnd( Long tableRowNumEnd )
  {
    this.tableRowNumEnd = tableRowNumEnd;
  }

  public String getNewHireAvailable()
  {
    return newHireAvailable;
  }

  public void setNewHireAvailable( String newHireAvailable )
  {
    this.newHireAvailable = newHireAvailable;
  }

  public String getOverDueAvailable()
  {
    return overDueAvailable;
  }

  public void setOverDueAvailable( String overDueAvailable )
  {
    this.overDueAvailable = overDueAvailable;
  }

  public String getUpComingAvailable()
  {
    return upComingAvailable;
  }

  public void setUpComingAvailable( String upComingAvailable )
  {
    this.upComingAvailable = upComingAvailable;
  }

  @JsonIgnore
  public Integer getNewHireTotalcount()
  {
    return newHireTotalcount;
  }

  public void setNewHireTotalcount( Integer newHireTotalcount )
  {
    this.newHireTotalcount = newHireTotalcount;
  }

  @JsonIgnore
  public Integer getOverDueTotalcount()
  {
    return overDueTotalcount;
  }

  public void setOverDueTotalcount( Integer overDueTotalcount )
  {
    this.overDueTotalcount = overDueTotalcount;
  }

}
