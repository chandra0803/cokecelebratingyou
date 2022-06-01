
package com.biperf.core.ui.recognition.purl;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.hierarchy.Node;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PresetSearchFiltersBean
{
  private String name;
  private String instruction;
  private List<PresetSearchFiltersItem> searchFilters = new ArrayList<PresetSearchFiltersItem>();
  private static final String TYPE = "location";
  private String TEAM = CmsResourceBundle.getCmsBundle().getString( "participant.search.TEAM" );

  public PresetSearchFiltersBean( List<Node> childNodes, String nameValue, String instructionValue )
  {
    setName( nameValue );
    setInstruction( instructionValue );
    for ( Node node : childNodes )
    {
      searchFilters.add( new PresetSearchFiltersItem( TYPE, TEAM, node.getId(), node.getName() ) );
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

  public String getInstruction()
  {
    return instruction;
  }

  public void setInstruction( String instruction )
  {
    this.instruction = instruction;
  }

  @JsonProperty( "filters" )
  public List<PresetSearchFiltersItem> getSearchFilters()
  {
    return searchFilters;
  }

  public void setSearchFilters( List<PresetSearchFiltersItem> searchFilters )
  {
    this.searchFilters = searchFilters;
  }

  public static class PresetSearchFiltersItem
  {
    private String type;
    private String typeName;
    private Long id;
    private String name;

    public PresetSearchFiltersItem( String type, String typeName, Long id, String name )
    {
      this.type = type;
      this.typeName = typeName;
      this.id = id;
      this.name = name;
    }

    public String getType()
    {
      return type;
    }

    public void setType( String type )
    {
      this.type = type;
    }

    public String getTypeName()
    {
      return typeName;
    }

    public void setTypeName( String typeName )
    {
      this.typeName = typeName;
    }

    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public String getName()
    {
      return name;
    }

    public void setName( String name )
    {
      this.name = name;
    }

  }

}
