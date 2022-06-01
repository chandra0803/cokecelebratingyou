
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.value.ssi.SSIContestValueBean;

/**
 * 
 * SSIContestAwardThemNowMainView.
 * 
 * @author kandhi
 * @since Feb 04, 2015
 * @version 1.0
 */
public class SSIContestAwardThemNowMainView extends SSIContestMainView
{
  private static final String URL_SAVE_GENERAL_INFO_ATN = "/ssi/saveGeneralInfoAwardThemNow.do";

  public static final String ATN_CREATE = "create";
  public static final String ATN_EDIT_INFO = "editInfo";
  public static final String ATN_ISSUE_MORE_AWARDS = "issueMoreAwards";

  private boolean optionShowBadges;
  private Long badgeId;
  private List<SSIPaxContestBadgeView> badges;
  private String measureType;
  private List<SSIContestCurrencyTypeView> currencyTypes;
  private String currencyTypeId;
  private String awardThemNowStatus;
  private boolean optionShowPayoutTypeOther;
  private boolean optionShowPayoutTypePoints;
  private String payoutType;
  private String otherPayoutTypeId;

  public SSIContestAwardThemNowMainView( SSIContest ssiContest,
                                         SSIPromotion ssiPromotion,
                                         String contestClientState,
                                         String contestType,
                                         SSIContestValueBean valueBean,
                                         List localeItems,
                                         String sysUrl,
                                         List<SSIContestBillCodeView> billCodes )
  {
    super( ssiContest, ssiPromotion, contestClientState, contestType, valueBean, localeItems, sysUrl, billCodes );

    setNextUrl( sysUrl + URL_SAVE_GENERAL_INFO_ATN + "?contestType=" + SSIContest.CONTEST_TYPE_AWARD_THEM_NOW );
    this.measureType = ssiContest.getActivityMeasureType() != null ? ssiContest.getActivityMeasureType().getCode() : null;
    this.currencyTypeId = ssiContest.getActivityMeasureCurrencyCode();

    this.optionShowPayoutTypePoints = ssiPromotion.getAllowAwardPoints();
    this.optionShowPayoutTypeOther = ssiPromotion.getAllowAwardOther();

    this.payoutType = ssiContest.getPayoutType() != null ? ssiContest.getPayoutType().getCode() : null;
    if ( ssiContest.getPayoutType() != null && ssiContest.getPayoutType().getCode().equals( SSIPayoutType.OTHER_CODE ) )
    {
      this.otherPayoutTypeId = ssiContest.getPayoutOtherCurrencyCode();
    }

    Badge badge = ssiPromotion.getBadge();

    this.optionShowBadges = badge != null ? true : false;

    if ( ssiContest.getBadgeRule() != null )
    {
      this.badgeId = ssiContest.getBadgeRule().getId();
    }

    // Iterate through the badge promotion and get all the promotion badge rules to be displayed
    if ( badge != null )
    {
      Set<BadgeRule> badgeRules = badge.getBadgeRules();
      if ( badgeRules != null )
      {
        badges = new ArrayList<SSIPaxContestBadgeView>();
        for ( BadgeRule badgeRule : badgeRules )
        {
          SSIPaxContestBadgeView badgeView = new SSIPaxContestBadgeView( badgeRule );
          badges.add( badgeView );
        }
      }
    }
  }

  public void setCurrencies( List<Currency> currencies )
  {
    currencyTypes = new ArrayList<SSIContestCurrencyTypeView>();
    for ( Currency currency : currencies )
    {
      currencyTypes.add( new SSIContestCurrencyTypeView( currency.getCurrencyCode().toLowerCase(), currency.getDisplayCurrencyName(), currency.getCurrencySymbol() ) );
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

  public List<SSIPaxContestBadgeView> getBadges()
  {
    return badges;
  }

  public void setBadges( List<SSIPaxContestBadgeView> badges )
  {
    this.badges = badges;
  }

  public String getMeasureType()
  {
    return measureType;
  }

  public void setMeasureType( String measureType )
  {
    this.measureType = measureType;
  }

  public List<SSIContestCurrencyTypeView> getCurrencyTypes()
  {
    return currencyTypes;
  }

  public void setCurrencyTypes( List<SSIContestCurrencyTypeView> currencyTypes )
  {
    this.currencyTypes = currencyTypes;
  }

  public String getCurrencyTypeId()
  {
    return currencyTypeId;
  }

  public void setCurrencyTypeId( String currencyTypeId )
  {
    this.currencyTypeId = currencyTypeId;
  }

  public boolean isOptionShowPayoutTypeOther()
  {
    return optionShowPayoutTypeOther;
  }

  public void setOptionShowPayoutTypeOther( boolean optionShowPayoutTypeOther )
  {
    this.optionShowPayoutTypeOther = optionShowPayoutTypeOther;
  }

  public boolean isOptionShowPayoutTypePoints()
  {
    return optionShowPayoutTypePoints;
  }

  public void setOptionShowPayoutTypePoints( boolean optionShowPayoutTypePoints )
  {
    this.optionShowPayoutTypePoints = optionShowPayoutTypePoints;
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public String getOtherPayoutTypeId()
  {
    return otherPayoutTypeId;
  }

  public void setOtherPayoutTypeId( String otherPayoutTypeId )
  {
    this.otherPayoutTypeId = otherPayoutTypeId;
  }

  public String getAwardThemNowStatus()
  {
    return awardThemNowStatus;
  }

  public void setAwardThemNowStatus( String awardThemNowStatus )
  {
    this.awardThemNowStatus = awardThemNowStatus;
  }

}
