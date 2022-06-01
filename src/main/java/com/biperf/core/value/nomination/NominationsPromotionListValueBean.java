
package com.biperf.core.value.nomination;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the over-arching information needed when submitting a nomination
 */
public class NominationsPromotionListValueBean
{
  private int totalEligiblePromotionCount;
  private NominationSubmitDataPromotionValueBean promotion;
  List<NodeValueBean> nodes = new ArrayList<NominationsPromotionListValueBean.NodeValueBean>();

  /**
   * Default constructor. 
   */
  public NominationsPromotionListValueBean()
  {
  }

  @JsonProperty( "totalPromotionCount" )
  public int getTotalEligiblePromotionCount()
  {
    return totalEligiblePromotionCount;
  }

  public void setTotalEligiblePromotionCount( int totalEligiblePromotionCount )
  {
    this.totalEligiblePromotionCount = totalEligiblePromotionCount;
  }

  @JsonProperty( "promotion" )
  public NominationSubmitDataPromotionValueBean getPromotion()
  {
    return promotion;
  }

  public void setPromotion( NominationSubmitDataPromotionValueBean promotion )
  {
    this.promotion = promotion;
  }

  public static class NodeValueBean
  {

    private Long id;
    private String name;
    private boolean selected;

    public NodeValueBean()
    {
    }

    public NodeValueBean( Long id, String name )
    {
      this.id = id;
      this.name = name;
    }

    @JsonProperty( "id" )
    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    @JsonProperty( "name" )
    public String getName()
    {
      return name;
    }

    public void setName( String name )
    {
      this.name = name;
    }

    @JsonProperty( "selected" )
    public boolean isSelected()
    {
      return selected;
    }

    public void setSelected( boolean selected )
    {
      this.selected = selected;
    }

  }

  @JsonProperty( "nodes" )
  public List<NodeValueBean> getNodes()
  {
    return nodes;
  }

  public void setNodes( List<NodeValueBean> nodes )
  {
    this.nodes = nodes;
  }

  public void addNode( NodeValueBean node )
  {
    nodes.add( node );
  }

}
