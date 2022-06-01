
package com.biperf.core.ui.participant;

import java.io.Serializable;

import com.biperf.core.domain.participant.ParticipantIdentifier;
import com.biperf.core.utils.StringUtil;

@SuppressWarnings( "serial" )
public class ParticipantIdentifierBean implements Serializable
{
  private String name;
  private Long participantIdentifierId = null;
  private boolean selected = false;
  private String type = null;
  private String label;
  private String description;
  private Long characteristicId = null;
  private boolean userCharacteristic = false;
  private String visibility;

  public ParticipantIdentifierBean( ParticipantIdentifier paxIdentifier )
  {
    this.participantIdentifierId = paxIdentifier.getId();
    this.selected = paxIdentifier.isSelected();

    // check to see if CM context is value
    this.label = paxIdentifier.getCMLabelValue();
    if ( label != null && label.startsWith( "???" + ParticipantIdentifier.CM_PAX_IDENTIFIER_SECTION ) )
    {
      this.label = "";
    }

    String cmDescription = paxIdentifier.getCMDescriptionValue();

    if ( !StringUtil.isEmpty( cmDescription ) && !cmDescription.startsWith( "???" + ParticipantIdentifier.CM_PAX_IDENTIFIER_SECTION ) )
    {
      this.description = cmDescription;
    }

    if ( null != paxIdentifier.getParticipantIdentifierType() )
    {
      this.name = paxIdentifier.getParticipantIdentifierType().getCode();
      this.type = paxIdentifier.getParticipantIdentifierType().getCode();
    }
    else
    {
      this.name = paxIdentifier.getCharacteristic().getCharacteristicName();
      this.characteristicId = paxIdentifier.getCharacteristic().getId();
      this.userCharacteristic = true;
      this.visibility = paxIdentifier.getCharacteristic().getVisibility().getCode();
    }
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public Long getParticipantIdentifierId()
  {
    return participantIdentifierId;
  }

  public void setParticipantIdentifierId( Long participantIdentifierId )
  {
    this.participantIdentifierId = participantIdentifierId;
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected( boolean selected )
  {
    this.selected = selected;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel( String label )
  {
    this.label = label;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public Long getCharacteristicId()
  {
    return characteristicId;
  }

  public void setCharacteristicId( Long characteristicId )
  {
    this.characteristicId = characteristicId;
  }

  public boolean isUserCharacteristic()
  {
    return userCharacteristic;
  }

  public void setUserCharacteristic( boolean userCharacteristic )
  {
    this.userCharacteristic = userCharacteristic;
  }

  public String getType()
  {
    return this.type;
  }

  public String getVisibility()
  {
    return visibility;
  }

  public void setVisibility( String visibility )
  {
    this.visibility = visibility;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( characteristicId == null ) ? 0 : characteristicId.hashCode() );
    result = prime * result + ( ( description == null ) ? 0 : description.hashCode() );
    result = prime * result + ( ( label == null ) ? 0 : label.hashCode() );
    result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
    result = prime * result + ( ( participantIdentifierId == null ) ? 0 : participantIdentifierId.hashCode() );
    result = prime * result + ( selected ? 1231 : 1237 );
    result = prime * result + ( ( type == null ) ? 0 : type.hashCode() );
    result = prime * result + ( userCharacteristic ? 1231 : 1237 );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    ParticipantIdentifierBean other = (ParticipantIdentifierBean)obj;
    if ( characteristicId == null )
    {
      if ( other.characteristicId != null )
      {
        return false;
      }
    }
    else if ( !characteristicId.equals( other.characteristicId ) )
    {
      return false;
    }
    if ( description == null )
    {
      if ( other.description != null )
      {
        return false;
      }
    }
    else if ( !description.equals( other.description ) )
    {
      return false;
    }
    if ( label == null )
    {
      if ( other.label != null )
      {
        return false;
      }
    }
    else if ( !label.equals( other.label ) )
    {
      return false;
    }
    if ( name == null )
    {
      if ( other.name != null )
      {
        return false;
      }
    }
    else if ( !name.equals( other.name ) )
    {
      return false;
    }
    if ( participantIdentifierId == null )
    {
      if ( other.participantIdentifierId != null )
      {
        return false;
      }
    }
    else if ( !participantIdentifierId.equals( other.participantIdentifierId ) )
    {
      return false;
    }
    if ( selected != other.selected )
    {
      return false;
    }
    if ( type == null )
    {
      if ( other.type != null )
      {
        return false;
      }
    }
    else if ( !type.equals( other.type ) )
    {
      return false;
    }
    if ( userCharacteristic != other.userCharacteristic )
    {
      return false;
    }
    return true;
  }
}
