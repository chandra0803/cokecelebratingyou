
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.ssi.SSIContestStackRankPayout;
import com.biperf.core.utils.SSIContestUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SSIContestApprovalRankDetailView
{

  private String payoutType;
  private List<Rank> ranks;
  private String payoutTotal;
  private Boolean hasBadge = Boolean.FALSE;

  public SSIContestApprovalRankDetailView()
  {

  }

  public SSIContestApprovalRankDetailView( List<SSIContestStackRankPayout> rankPayouts, String siteUrl, List<BadgeLibrary> badgeLibraryList, boolean hasBadge, String payoutPrefix )
  {
    this.hasBadge = hasBadge;
    SSIContestStackRankPayout payout = rankPayouts.get( 0 );
    Long total = 0L;
    for ( SSIContestStackRankPayout rankPayout : rankPayouts )
    {
      total += rankPayout.getPayoutAmount();
    }
    ranks = new ArrayList<Rank>();
    this.payoutType = payout.getContest().getPayoutType().getCode();
    if ( SSIPayoutType.POINTS_CODE.equals( this.payoutType ) )
    {
      this.payoutTotal = SSIContestUtil.getFormattedValue( total, SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + " " + payout.getContest().getPayoutType().getName();
    }
    else
    {
      this.payoutTotal = payoutPrefix + SSIContestUtil.getFormattedValue( total, SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
    }
    for ( SSIContestStackRankPayout rankPayout : rankPayouts )
    {
      if ( rankPayout.getBadgeRule() != null && hasBadge )
      {
        for ( BadgeLibrary badgeLibrary : badgeLibraryList )
        {
          if ( rankPayout.getBadgeRule().getBadgeLibraryCMKey().equals( badgeLibrary.getBadgeLibraryId() ) )
          {
            this.ranks
                .add( new Rank( SSIContestUtil.getFormattedValue( rankPayout.getPayoutAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ),
                                rankPayout.getPayoutDescription(),
                                rankPayout.getRankPosition(),
                                rankPayout.getBadgeRule().getBadgeNameTextFromCM(),
                                badgeLibrary.getEarnedImageSmall(),
                                siteUrl ) );
            break;
          }
        }
      }
      else
      {
        this.ranks.add( new Rank( SSIContestUtil.getFormattedValue( rankPayout.getPayoutAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ),
                                  rankPayout.getPayoutDescription(),
                                  rankPayout.getRankPosition() ) );
      }
    }
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public List<Rank> getRanks()
  {
    return ranks;
  }

  public void setRanks( List<Rank> ranks )
  {
    this.ranks = ranks;
  }

  public String getPayoutTotal()
  {
    return payoutTotal;
  }

  public void setPayoutTotal( String payoutTotal )
  {
    this.payoutTotal = payoutTotal;
  }

  @JsonProperty( "hasBadge" )
  public Boolean getHasBadge()
  {
    return hasBadge;
  }

  public void setHasBadge( Boolean hasBadge )
  {
    this.hasBadge = hasBadge;
  }

  class Rank
  {

    private Long number;
    private String payoutDescription;
    private String payoutAmount;
    private ContestBadge badge;

    public Rank()
    {

    }

    public Rank( String payoutAmount, String payoutDescription, Long rank )
    {
      this.payoutAmount = payoutAmount;
      this.payoutDescription = payoutDescription;
      this.number = rank;
    }

    public Rank( String payoutAmount, String payoutDescription, Long rank, String name, String badgeUrl, String siteUrl )
    {
      this.payoutAmount = payoutAmount;
      this.payoutDescription = payoutDescription;
      this.number = rank;
      this.badge = new ContestBadge( name, badgeUrl, siteUrl );
    }

    public Long getNumber()
    {
      return number;
    }

    public void setNumber( Long number )
    {
      this.number = number;
    }

    public String getPayoutDescription()
    {
      return payoutDescription;
    }

    public void setPayoutDescription( String payoutDescription )
    {
      this.payoutDescription = payoutDescription;
    }

    public String getPayoutAmount()
    {
      return payoutAmount;
    }

    public void setPayoutAmount( String payoutAmount )
    {
      this.payoutAmount = payoutAmount;
    }

    public ContestBadge getBadge()
    {
      return badge;
    }

    public void setBadge( ContestBadge badge )
    {
      this.badge = badge;
    }

  }

  class ContestBadge
  {
    private String name;
    private String url;

    public ContestBadge()
    {

    }

    public ContestBadge( String name, String badgeUrl, String siteUrl )
    {
      this.name = name;
      this.url = siteUrl + badgeUrl;
    }

    public String getName()
    {
      return name;
    }

    public void setName( String name )
    {
      this.name = name;
    }

    public String getUrl()
    {
      return url;
    }

    public void setUrl( String url )
    {
      this.url = url;
    }

  }

}
