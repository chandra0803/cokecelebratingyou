
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIPromotion;

/**
 * 
 * SSIContestPayoutContestView.
 * 
 * @author kandhi
 * @since Dec 22, 2014
 * @version 1.0
 */
public class SSIContestPayoutContestView
{
  private String ssiContestClientState;
  private Long id;
  private String measureType;
  private List<SSIContestCurrencyTypeView> currencyTypes;
  private String currencyTypeId;
  private boolean optionShowPayoutTypeOther;
  private boolean optionShowPayoutTypePoints;
  private String payoutType;
  private String otherPayoutTypeId;
  private boolean includeStackRanking;
  protected String nextUrl;
  private Boolean billCodeRequired;
  private String billTo;

  private List<SSIPaxContestBadgeView> badges;

  private List<SSIContestBillCodeView> billCodes;

  public List<SSIContestBillCodeView> getBillCodes()
  {
    return billCodes;
  }

  public void setBillCodes( List<SSIContestBillCodeView> billCodes )
  {
    this.billCodes = billCodes;
  }

  public SSIContestPayoutContestView( SSIContest ssiContest, String ssiContestClientState, List<Currency> currencies, List<SSIContestBillCodeView> billCodes )
  {
    this.id = ssiContest.getId();
    this.ssiContestClientState = ssiContestClientState;
    this.measureType = ssiContest.getActivityMeasureType() != null ? ssiContest.getActivityMeasureType().getCode() : null;
    this.currencyTypeId = ssiContest.getActivityMeasureCurrencyCode();

    this.optionShowPayoutTypePoints = ssiContest.getPromotion().getAllowAwardPoints();
    this.optionShowPayoutTypeOther = ssiContest.getPromotion().getAllowAwardOther();

    this.payoutType = ssiContest.getPayoutType() != null ? ssiContest.getPayoutType().getCode() : null;
    if ( ssiContest.getPayoutType() != null && ssiContest.getPayoutType().getCode().equals( SSIPayoutType.OTHER_CODE ) )
    {
      this.otherPayoutTypeId = ssiContest.getPayoutOtherCurrencyCode();
    }
    this.includeStackRanking = ssiContest.isIncludeStackRank();
    currencyTypes = new ArrayList<SSIContestCurrencyTypeView>();
    if ( currencies != null )
    {
      for ( Currency currency : currencies )
      {
        currencyTypes.add( new SSIContestCurrencyTypeView( currency.getCurrencyCode().toLowerCase(), currency.getDisplayCurrencyName(), currency.getCurrencySymbol() ) );
      }
    }
    this.billCodeRequired = ssiContest.getPromotion().isBillCodesActive();
    if ( this.billCodeRequired )
    {
      if ( ssiContest.getBillPayoutCodeType() != null )
      {
        this.billTo = ssiContest.getBillPayoutCodeType().getCode();
      }
    }
    if ( !ssiContest.getContestType().getCode().equals( SSIContestType.DO_THIS_GET_THAT ) )
    {
      populateBadges( ssiContest );
    }

    this.billCodes = billCodes;
  }

  private void populateBadges( SSIContest contest )
  {
    SSIPromotion ssiPromotion = contest.getPromotion();
    Badge badge = ssiPromotion.getBadge();

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

  public String getSsiContestClientState()
  {
    return ssiContestClientState;
  }

  public void setSsiContestClientState( String ssiContestClientState )
  {
    this.ssiContestClientState = ssiContestClientState;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getMeasureType()
  {
    return measureType;
  }

  public void setMeasureType( String measureType )
  {
    this.measureType = measureType;
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

  public boolean isIncludeStackRanking()
  {
    return includeStackRanking;
  }

  public void setIncludeStackRanking( boolean includeStackRanking )
  {
    this.includeStackRanking = includeStackRanking;
  }

  public String getNextUrl()
  {
    return nextUrl;
  }

  public void setNextUrl( String nextUrl )
  {
    this.nextUrl = nextUrl;
  }

  public Boolean getBillCodeRequired()
  {
    return billCodeRequired;
  }

  public void setBillCodeRequired( Boolean billCodeRequired )
  {
    this.billCodeRequired = billCodeRequired;
  }

  public String getBillTo()
  {
    return billTo;
  }

  public void setBillTo( String billTo )
  {
    this.billTo = billTo;
  }

  public List<SSIPaxContestBadgeView> getBadges()
  {
    return badges;
  }

  public void setBadges( List<SSIPaxContestBadgeView> badges )
  {
    this.badges = badges;
  }
}
