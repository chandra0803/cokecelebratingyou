
package com.biperf.core.domain.participant;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.enums.ParticipantIdentifierType;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ParticipantIdentifier extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  private ParticipantIdentifierType participantIdentifierType;
  private boolean selected = false;
  private String cmAssetCode;

  private String label;
  private String description;

  private Characteristic characteristic;

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public ParticipantIdentifierType getParticipantIdentifierType()
  {
    return participantIdentifierType;
  }

  public void setParticipantIdentifierType( ParticipantIdentifierType participantIdentifierType )
  {
    this.participantIdentifierType = participantIdentifierType;
  }

  public String getLabel()
  {
    return label;
  }

  public String getDescription()
  {
    return description;
  }

  public String getCMLabelValue()
  {
    if ( label == null || label.trim().length() == 0 )
    {
      label = CmsResourceBundle.getCmsBundle().getString( cmAssetCode, PAX_IDENTIFIER_LABEL_KEY );
    }
    return label;
  }

  public void setLabel( String label )
  {
    this.label = label;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getCMDescriptionValue()
  {
    if ( description == null || description.trim().length() == 0 )
    {
      description = CmsResourceBundle.getCmsBundle().getString( cmAssetCode, PAX_IDENTIFIER_DESCRIPTION_KEY );
    }
    return description;
  }

  public Characteristic getCharacteristic()
  {
    return characteristic;
  }

  public void setCharacteristic( Characteristic characteristic )
  {
    this.characteristic = characteristic;
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected( boolean selected )
  {
    this.selected = selected;
  }

  public static final String PAX_IDENTIFIER_ASSET_TYPE = "PARTICIPANT_IDENTIFIER";
  public static final String PAX_IDENTIFIER_LABEL_KEY = "LABEL";
  public static final String PAX_IDENTIFIER_DESCRIPTION_KEY = "DESCRIPTION";
  public static final String CM_PAX_IDENTIFIER_SECTION = "participant_identifier";
  public static final String CM_PAX_IDENTIFIER_ASSET_NAME = "Participant Identifier Element";

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( characteristic == null ) ? 0 : characteristic.hashCode() );
    result = prime * result + ( ( cmAssetCode == null ) ? 0 : cmAssetCode.hashCode() );
    result = prime * result + ( ( description == null ) ? 0 : description.hashCode() );
    result = prime * result + ( ( label == null ) ? 0 : label.hashCode() );
    result = prime * result + ( ( participantIdentifierType == null ) ? 0 : participantIdentifierType.hashCode() );
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
    ParticipantIdentifier other = (ParticipantIdentifier)obj;
    if ( characteristic == null )
    {
      if ( other.characteristic != null )
      {
        return false;
      }
    }
    else if ( !characteristic.equals( other.characteristic ) )
    {
      return false;
    }
    if ( cmAssetCode == null )
    {
      if ( other.cmAssetCode != null )
      {
        return false;
      }
    }
    else if ( !cmAssetCode.equals( other.cmAssetCode ) )
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
    if ( participantIdentifierType == null )
    {
      if ( other.participantIdentifierType != null )
      {
        return false;
      }
    }
    else if ( !participantIdentifierType.equals( other.participantIdentifierType ) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "ParticipantIdentifier [participantIdentifierType=" + participantIdentifierType + ", selected=" + selected + ", cmAssetCode=" + cmAssetCode + ", characteristic=" + characteristic + "]";
  }

}
