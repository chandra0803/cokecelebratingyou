
package com.biperf.core.value;

import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ClaimInfoBean
{
  private Long proxyUserId;
  private Long promotionId;
  private Long submitterId;
  private SelectedNodeBean theSelectedNode;
  private ProductClaimInfoBean claimInfoBean;
  private Long timePeriodId;
  private Long activityId;

  @JsonIgnore
  private ClaimFormStep claimFormStep;

  public ClaimInfoBean()
  {
  }

  public ClaimInfoBean( ProductClaim productClaim, Node theSelectedNode )
  {
    if ( productClaim.getProxyUser() != null )
    {
      this.proxyUserId = productClaim.getProxyUser().getId();
    }
    this.promotionId = productClaim.getPromotion().getId();
    this.claimInfoBean = new ProductClaimInfoBean( productClaim );
    this.submitterId = productClaim.getSubmitter().getId();
    createClaimFormSteps( (ProductClaimPromotion)productClaim.getPromotion() );
    this.theSelectedNode = new SelectedNodeBean( theSelectedNode );
  }

  private void createClaimFormSteps( ProductClaimPromotion promotion )
  {
    ClaimFormStep claimFormStep = new ClaimFormStep();
    List claimFormSteps = promotion.getClaimForm().getClaimFormSteps();
    Iterator iter = claimFormSteps.iterator();
    if ( iter.hasNext() )
    {
      claimFormStep = (ClaimFormStep)iter.next();
    }
    this.claimFormStep = claimFormStep;
  }

  public class SelectedNodeBean
  {
    private Long id;
    private String name;

    public SelectedNodeBean( Node selectedNode )
    {
      this.id = selectedNode.getId();
      this.name = selectedNode.getName();
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

  public class ProductClaimInfoBean
  {
    private Long id;
    private String name;
    private boolean status;

    public ProductClaimInfoBean( ProductClaim claim )
    {
      this.id = claim.getId();
      this.status = claim.isOpen();
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

    public boolean isStatus()
    {
      return status;
    }

    public void setStatus( boolean status )
    {
      this.status = status;
    }
  }

  public Long getProxyUserId()
  {
    return proxyUserId;
  }

  public void setProxyUserId( Long proxyUserId )
  {
    this.proxyUserId = proxyUserId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getSubmitterId()
  {
    return submitterId;
  }

  public void setSubmitterId( Long submitterId )
  {
    this.submitterId = submitterId;
  }

  public ClaimFormStep getClaimFormStep()
  {
    return claimFormStep;
  }

  public void setClaimFormStep( ClaimFormStep claimFormStep )
  {
    this.claimFormStep = claimFormStep;
  }

  public SelectedNodeBean getTheSelectedNode()
  {
    return theSelectedNode;
  }

  public void setTheSelectedNode( SelectedNodeBean theSelectedNode )
  {
    this.theSelectedNode = theSelectedNode;
  }

  public ProductClaimInfoBean getClaimInfoBean()
  {
    return claimInfoBean;
  }

  public void setClaimInfoBean( ProductClaimInfoBean claimInfoBean )
  {
    this.claimInfoBean = claimInfoBean;
  }

  public Long getTimePeriodId()
  {
    return timePeriodId;
  }

  public void setTimePeriodId( Long timePeriodId )
  {
    this.timePeriodId = timePeriodId;
  }

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }
}
