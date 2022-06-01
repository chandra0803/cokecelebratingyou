
package com.biperf.core.ui.productclaim;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.user.UserNode;
import com.biperf.core.value.ClaimInfoBean;
import com.biperf.core.value.NodeBean;
import com.biperf.core.value.ProductClaimBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( Include.NON_NULL )
public class ProductClaimStartBean
{
  private List<ProductClaimBean> promotions;
  private List<NodeBean> nodes;
  private ClaimInfoBean claim;

  public ProductClaimStartBean()
  {
  }

  public ProductClaimStartBean( List<ProductClaimBean> claimPromotions, List<UserNode> userNodes, ClaimInfoBean productClaim )
  {
    // promotions
    setProductClaimPromotions( claimPromotions );

    // nodes
    setUserNodes( userNodes );

    // claimInfo

    setClaimInfo( productClaim );

  }

  @JsonProperty( "promotions" )
  public List<ProductClaimBean> getPromotions()
  {
    return promotions;
  }

  @JsonProperty( "nodes" )
  public List<NodeBean> getNodes()
  {
    return nodes;
  }

  public void setPromotions( List<ProductClaimBean> promotions )
  {
    this.promotions = promotions;
  }

  public void setNodes( List<NodeBean> nodes )
  {
    this.nodes = nodes;
  }

  public final void setUserNodes( List<UserNode> userNodes )
  {
    if ( userNodes != null && !userNodes.isEmpty() )
    {
      nodes = new ArrayList<NodeBean>( userNodes.size() );
      for ( UserNode userNode : userNodes )
      {
        nodes.add( new NodeBean( userNode ) );
      }
    }
  }

  public final void setProductClaimPromotions( List<ProductClaimBean> claimPromotions )
  {
    if ( claimPromotions != null && !claimPromotions.isEmpty() )
    {
      promotions = new ArrayList<ProductClaimBean>();

      for ( ProductClaimBean p : claimPromotions )
      {
        promotions.add( p );
      }
    }
  }

  public final void setClaimInfo( ClaimInfoBean productClaim )
  {
    if ( productClaim != null )
    {
      claim = productClaim;
    }
  }

  public ClaimInfoBean getClaim()
  {
    return claim;
  }

  public void setClaim( ClaimInfoBean claim )
  {
    this.claim = claim;
  }
}
