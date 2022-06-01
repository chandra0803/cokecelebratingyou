
package com.biperf.core.ui.nomination;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the over-arching information needed when submitting a nomination
 */
public class NominationsPromotionListViewBean
{
  private int totalEligiblePromotionCount;
  private NominationSubmitDataPromotionViewBean promotion = new NominationSubmitDataPromotionViewBean();
  List<NodeViewBean> nodes = new ArrayList<NominationsPromotionListViewBean.NodeViewBean>();

  /**
   * Default constructor. 
   */
  public NominationsPromotionListViewBean()
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
  public NominationSubmitDataPromotionViewBean getPromotion()
  {
    return promotion;
  }

  public void setPromotion( NominationSubmitDataPromotionViewBean promotion )
  {
    this.promotion = promotion;
  }

  public static class NodeViewBean
  {

    private Long id;
    private String name;
    private boolean selected;

    public NodeViewBean()
    {
    }

    public NodeViewBean( Long id, String name )
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
  public List<NodeViewBean> getNodes()
  {
    return nodes;
  }

  public void setNodes( List<NodeViewBean> nodes )
  {
    this.nodes = nodes;
  }

  public void addNode( NodeViewBean node )
  {
    nodes.add( node );
  }

}
