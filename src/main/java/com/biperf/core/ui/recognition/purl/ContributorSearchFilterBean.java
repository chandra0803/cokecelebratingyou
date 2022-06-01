
package com.biperf.core.ui.recognition.purl;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.hierarchy.Node;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ContributorSearchFilterBean
{

  private String name;
  private String instruction;
  private List<ContributorSearchFiltersItem> searchFilters = new ArrayList<ContributorSearchFiltersItem>();
  private static final String TYPE = "location";
  private static final String TEAM = "Team";

  public ContributorSearchFilterBean( List<Node> childNodes, String nameValue, String instructionValue )
  {
    setName( nameValue );
    setInstruction( instructionValue );
    for ( Node node : childNodes )
    {
      searchFilters.add( new ContributorSearchFiltersItem( TYPE, TEAM, node.getId(), node.getName() ) );
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
  public List<ContributorSearchFiltersItem> getSearchFilters()
  {
    return searchFilters;
  }

  public void setSearchFilters( List<ContributorSearchFiltersItem> searchFilters )
  {
    this.searchFilters = searchFilters;
  }

  public static class ContributorSearchFiltersItem
  {
    private String type;
    private String typeName;
    private Long id;
    private String name;

    public ContributorSearchFiltersItem( String type, String typeName, Long id, String name )
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
