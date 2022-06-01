
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.utils.PageConstants;

/**
 * 
 * SSIContestPayoutObjectivesContestView.
 * 
 * @author kandhi
 * @since Dec 1, 2014
 * @version 1.0
 */
public class SSIContestPayoutObjectivesContestView extends SSIContestPayoutContestView
{
  private boolean optionShowBadges;
  private Long badgeId;
  private String stackRankingOrder;
  private boolean includeBonus;
  private Boolean sameActivityDescriptionForAll;
  private Double contestGoal;
  private String activityDescription;

  public SSIContestPayoutObjectivesContestView( SSIContest ssiContest, String ssiContestClientState, List<Currency> currencies, String sysUrl, List<SSIContestBillCodeView> billCodes )
  {
    super( ssiContest, ssiContestClientState, currencies, billCodes );
    setNextUrl( sysUrl + PageConstants.SSI_MANAGE_OBJECTIVES );
    this.includeBonus = ssiContest.isIncludeBonus();
    this.sameActivityDescriptionForAll = ssiContest.getSameObjectiveDescription();
    this.stackRankingOrder = ssiContest.getStackRankOrder();
    this.contestGoal = ssiContest.getContestGoal();
    this.activityDescription = ssiContest.getActivityDescription();

    SSIPromotion ssiPromotion = ssiContest.getPromotion();
    Badge badge = ssiPromotion.getBadge();

    this.optionShowBadges = badge != null ? true : false;

    if ( ssiContest.getBadgeRule() != null )
    {
      this.badgeId = ssiContest.getBadgeRule().getId();
    }
  }

  public boolean isOptionShowBadges()
  {
    return optionShowBadges;
  }

  public void setOptionShowBadges( boolean optionShowBadges )
  {
    this.optionShowBadges = optionShowBadges;
  }

  public Long getBadgeId()
  {
    return badgeId;
  }

  public void setBadgeId( Long badgeId )
  {
    this.badgeId = badgeId;
  }

  public String getStackRankingOrder()
  {
    return stackRankingOrder;
  }

  public void setStackRankingOrder( String stackRankingOrder )
  {
    this.stackRankingOrder = stackRankingOrder;
  }

  public boolean isIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( boolean includeBonus )
  {
    this.includeBonus = includeBonus;
  }

  public Boolean getSameActivityDescriptionForAll()
  {
    return sameActivityDescriptionForAll;
  }

  public void setSameActivityDescriptionForAll( Boolean sameActivityDescriptionForAll )
  {
    this.sameActivityDescriptionForAll = sameActivityDescriptionForAll;
  }

  public Double getContestGoal()
  {
    return contestGoal;
  }

  public void setContestGoal( Double contestGoal )
  {
    this.contestGoal = contestGoal;
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }
}
