
package com.biperf.core.domain.homepage;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.ModuleAppAudienceCodeType;
import com.biperf.core.domain.enums.ModuleAppAudienceType;
import com.biperf.core.domain.enums.TileMappingType;
import com.biperf.core.domain.participant.Audience;

@SuppressWarnings( "serial" )
public class ModuleApp extends BaseDomain
{
  private String name;
  private String description;
  private boolean mobileEnabled;
  private boolean adminAudienceSetup;
  private String availableSizes;
  private String appName;
  private String templateName;
  private String viewName;
  private boolean isActive = true;

  // defines the overall audience type
  private ModuleAppAudienceType audienceType;

  // defines the implementation of the class that determines if user is in the audience.
  // This enum is value is used to find the correct Strategy in the applicationContext.xml
  // which implements the ModuleAppAudienceStrategyFactory
  private ModuleAppAudienceCodeType appAudienceType;

  // links this ModuleApp to the UI layer
  private TileMappingType tileMappingType;

  private Set<Audience> audiences = new LinkedHashSet<Audience>();

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getAvailableSizes()
  {
    return availableSizes;
  }

  public void setAvailableSizes( String availableSizes )
  {
    this.availableSizes = availableSizes;
  }

  public ModuleAppAudienceCodeType getAppAudienceType()
  {
    return appAudienceType;
  }

  public void setAppAudienceType( ModuleAppAudienceCodeType appAudienceType )
  {
    this.appAudienceType = appAudienceType;
  }

  public String getAppName()
  {
    return appName;
  }

  public void setAppName( String appName )
  {
    this.appName = appName;
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
    ModuleApp other = (ModuleApp)obj;
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
    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( name == null ? 0 : name.hashCode() );
    return result;
  }

  public boolean isMobileEnabled()
  {
    return mobileEnabled;
  }

  public void setMobileEnabled( boolean mobileEnabled )
  {
    this.mobileEnabled = mobileEnabled;
  }

  public ModuleAppAudienceType getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( ModuleAppAudienceType audienceType )
  {
    this.audienceType = audienceType;
  }

  public TileMappingType getTileMappingType()
  {
    return tileMappingType;
  }

  public void setTileMappingType( TileMappingType tileMappingType )
  {
    this.tileMappingType = tileMappingType;
  }

  public Set<Audience> getAudiences()
  {
    return audiences;
  }

  public void setAudiences( Set<Audience> audiences )
  {
    this.audiences = audiences;
  }

  public boolean isAdminAudienceSetup()
  {
    return adminAudienceSetup;
  }

  public void setAdminAudienceSetup( boolean adminAudienceSetup )
  {
    this.adminAudienceSetup = adminAudienceSetup;
  }

  public String getTemplateName()
  {
    return templateName;
  }

  public void setTemplateName( String templateName )
  {
    this.templateName = templateName;
  }

  public String getViewName()
  {
    return viewName;
  }

  public void setViewName( String viewName )
  {
    this.viewName = viewName;
  }

  public boolean isActive()
  {
    return isActive;
  }

  public void setActive( boolean isActive )
  {
    this.isActive = isActive;
  }

}
