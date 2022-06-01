
package com.biperf.core.ui.groups;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.BaseJsonView;
import com.biperf.core.value.ParticipantPreviewBean;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ParticipantGroupView extends BaseJsonView
{
  @JsonProperty( "groupId" )
  private Long groupId;
  @JsonProperty( "groupName" )
  private String groupName;
  @JsonProperty( "groupCreatedBy" )
  private ParticipantPreviewBean groupCreatedBy;
  @JsonProperty( "dateCreated" )
  private Date dateCreated;
  @JsonProperty( "groupMemebers" )
  public List<ParticipantPreviewBean> participants = new ArrayList<ParticipantPreviewBean>();

  public String getGroupName()
  {
    return groupName;
  }

  public void setGroupName( String groupName )
  {
    this.groupName = groupName;
  }

  public ParticipantPreviewBean getGroupCreatedBy()
  {
    return groupCreatedBy;
  }

  public void setGroupCreatedBy( ParticipantPreviewBean groupCreatedBy )
  {
    this.groupCreatedBy = groupCreatedBy;
  }

  public Date getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( Date dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public List<ParticipantPreviewBean> getParticipants()
  {
    return participants;
  }

  public void setParticipants( List<ParticipantPreviewBean> participants )
  {
    this.participants = participants;
  }

  public void addParticipant( ParticipantPreviewBean participant )
  {
    if ( this.participants != null )
    {
      this.participants.add( participant );
    }
  }

  public Long getGroupId()
  {
    return groupId;
  }

  public void setGroupId( Long groupId )
  {
    this.groupId = groupId;
  }
}
