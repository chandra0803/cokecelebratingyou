
package com.biperf.core.value.diycommunication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.participant.Audience;
import com.biperf.core.value.FormattedValueBean;

public class AudienceResults
{
  private String id;
  private boolean selected;
  private String group;
  private String participantAmount;
  private String participantExportLink;
  private List<ParticipantList> participantList = new ArrayList<ParticipantList>();

  public AudienceResults()
  {

  }

  public AudienceResults( Audience audience, List<FormattedValueBean> fvbList, List<com.objectpartners.cms.domain.Audience> savedAudienceList, List<Audience> selectedAudienceList )
  {
    this.setId( audience.getId().toString() );
    this.setGroup( audience.getName() );
    this.setParticipantAmount( Integer.toString( audience.getSize() ) );
    if ( savedAudienceList != null && !savedAudienceList.isEmpty() )
    {
      for ( Iterator<com.objectpartners.cms.domain.Audience> iter = savedAudienceList.iterator(); iter.hasNext(); )
      {
        com.objectpartners.cms.domain.Audience savedAudience = iter.next();
        if ( savedAudience.getName().equals( audience.getName() ) )
        {
          this.setSelected( true );
          break;
        }
      }
    }
    if ( selectedAudienceList != null && !selectedAudienceList.isEmpty() )
    {
      for ( Iterator<Audience> iter = selectedAudienceList.iterator(); iter.hasNext(); )
      {
        Audience savedAudience = iter.next();
        if ( savedAudience.getName().equals( audience.getName() ) )
        {
          this.setSelected( true );
          break;
        }
      }
    }

    for ( Iterator<FormattedValueBean> iter = fvbList.iterator(); iter.hasNext(); )
    {
      FormattedValueBean fvBean = iter.next();
      participantList.add( new ParticipantList( fvBean ) );
    }
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public boolean getSelected()
  {
    return selected;
  }

  public void setSelected( boolean selected )
  {
    this.selected = selected;
  }

  public void setGroup( String group )
  {
    this.group = group;
  }

  public String getGroup()
  {
    return group;
  }

  public String getParticipantAmount()
  {
    return participantAmount;
  }

  public void setParticipantAmount( String participantAmount )
  {
    this.participantAmount = participantAmount;
  }

  public List<ParticipantList> getParticipantList()
  {
    return participantList;
  }

  public void setParticipantList( List<ParticipantList> participantList )
  {
    this.participantList = participantList;
  }

  public String getParticipantExportLink()
  {
    return participantExportLink;
  }

  public void setParticipantExportLink( String participantExportLink )
  {
    this.participantExportLink = participantExportLink;
  }

}
