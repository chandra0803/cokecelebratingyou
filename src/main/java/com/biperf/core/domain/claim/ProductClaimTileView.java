/**
 * 
 */

package com.biperf.core.domain.claim;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author poddutur
 *
 */
// This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class ProductClaimTileView
{
  private List<ClaimApprovals> claimApprovals = new ArrayList<ClaimApprovals>();

  @JsonProperty( "claimApprovals" )
  public List<ClaimApprovals> getClaimApprovals()
  {
    return claimApprovals;
  }

  public void setClaimApprovals( List<ClaimApprovals> claimApprovals )
  {
    this.claimApprovals = claimApprovals;
  }

  public static class ClaimApprovals
  {
    @JsonProperty( "promotionId" )
    private Long promotionId;
    @JsonProperty( "claimId" )
    private Long claimId;
    @JsonProperty( "promotionName" )
    private String promotionName;
    @JsonProperty( "numberSubmitted" )
    private int numberSubmitted;
    @JsonProperty( "numberOfApprovals" )
    private int numberOfApprovals;
    @JsonProperty( "promotionEndDate" )
    private String promotionEndDate;
    @JsonProperty( "promotionStartDate" )
    private String promotionStartDate;

    public Long getPromotionId()
    {
      return promotionId;
    }

    public void setPromotionId( Long promotionId )
    {
      this.promotionId = promotionId;
    }

    public Long getClaimId()
    {
      return claimId;
    }

    public void setClaimId( Long claimId )
    {
      this.claimId = claimId;
    }

    public String getPromotionName()
    {
      return promotionName;
    }

    public void setPromotionName( String promotionName )
    {
      this.promotionName = promotionName;
    }

    public int getNumberSubmitted()
    {
      return numberSubmitted;
    }

    public void setNumberSubmitted( int numberSubmitted )
    {
      this.numberSubmitted = numberSubmitted;
    }

    public int getNumberOfApprovals()
    {
      return numberOfApprovals;
    }

    public void setNumberOfApprovals( int numberOfApprovals )
    {
      this.numberOfApprovals = numberOfApprovals;
    }

    public String getPromotionEndDate()
    {
      return promotionEndDate;
    }

    public void setPromotionEndDate( String promotionEndDate )
    {
      this.promotionEndDate = promotionEndDate;
    }

    public String getPromotionStartDate()
    {
      return promotionStartDate;
    }

    public void setPromotionStartDate( String promotionStartDate )
    {
      this.promotionStartDate = promotionStartDate;
    }

  }

}
