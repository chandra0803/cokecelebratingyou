
package com.biperf.core.ui.gamification;

/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/gamification/BadgeForm.java,v $
 */

/**
  * BudgetForm.
  * <p>
  * <b>Change History:</b><br>
  * <table border="1">
  * <tr>
  * <th>Author</th>
  * <th>Date</th>
  * <th>Version</th>
  * <th>Comments</th>
  * </tr>
  * <tr>
  * <td>sharafud</td>
  * <td>Aug 24, 2012</td>
  * <td>1.0</td>
  * <td>created</td>
  * </tr>
  * </table>
  * 
  *
  */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.BillCodeNominationType;
import com.biperf.core.domain.enums.BillCodeRecognitionType;
import com.biperf.core.domain.enums.BillCodeSSIType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.goalquest.GoalBadgeRule;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBillCode;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;

public class BadgeForm extends BaseForm
{
  private static final long serialVersionUID = 1L;

  private String badgeId;
  // Name to be inserted in badge table
  private String badgeSetupName;
  private String[] promotionIds;
  private String[] deleteBadges;
  private String selectedPromotion;
  private String selectedPromotionIds;
  private String displayDays;
  private String tileHighlightPeriod;
  private String badgeType;
  private String badgeCountType;
  private String rangeAmountMin;
  private String rangeAmountMax;
  private String maxQualifier;
  private String badgeLibraryId;
  // name to be inserted in badege_rule table
  private String badgeName;
  private String badgeDescription;
  private String[] behaviorNames;
  private String behaviorName;
  private String levelName;
  // Quiz,Nomination,recognition/PURL etc
  private String earnedSelectedCategory;
  private String pointRange;
  private String[] behaviorStringRow;
  private String[] levelStringRow;
  private String[] overallLevelStringRow;
  private String[] stackLevelStringRow;
  private String[] pointRangeStringRow;
  private String[] progressStringRow;
  private String[] fileLoadStringRow;
  private List<BadgeLibrary> badgeLibraryList;
  private List badgeRulesList;
  private int currentFileLoadTableSize;
  private int currentProgressTableSize;
  private int currentPointRangeTableSize;
  private List<BadgeRule> badgeRuleList;
  private long notificationMessageId;
  private int badgeRuleListSize;
  private String countryCode;
  private String badgeTypeName;
  private String behaviorCode;
  private String overallBadgeStringRow;
  private String undefeatedBadgeStringRow;
  private List<BadgeRule> stackTdBadgeRuleList;
  private List<BadgeRule> overallTdBadgeRuleList;
  private List<BadgeRule> undefeatedTdBadgeRuleList;
  private int stackTdBadgeRuleListSize = 0;
  private int overallTdBadgeRuleListSize = 0;
  private int undefeatedTdBadgeRuleListSize = 0;

  private Set<GoalBadgeRule> acheiverBadges = new LinkedHashSet();
  private Set<GoalBadgeRule> partnerBadges = new LinkedHashSet();

  // Points for badge
  private boolean taxable = true;
  private boolean billCodesActive;
  private String billCode1;
  private String customValue1;
  private String billCode2;
  private String customValue2;
  private String billCode3;
  private String customValue3;
  private String billCode4;
  private String customValue4;
  private String billCode5;
  private String customValue5;
  private String billCode6;
  private String customValue6;
  private String billCode7;
  private String customValue7;
  private String billCode8;
  private String customValue8;
  private String billCode9;
  private String customValue9;
  private String billCode10;
  private String customValue10;
  private Long badgePoints;
  private boolean eligibleForSweepstake;
  private Long allBehaviorPoints;
  private boolean includeAllBehaviorPoints = false;
  private String promotionTypeCode;
  private String startDate = DateUtils.displayDateFormatMask;

  // private String levelStringRow;

  public Set<GoalBadgeRule> getAcheiverBadges()
  {
    return acheiverBadges;
  }

  public void setAcheiverBadges( Set<GoalBadgeRule> acheiverBadges )
  {
    this.acheiverBadges = acheiverBadges;
  }

  public Set<GoalBadgeRule> getPartnerBadges()
  {
    return partnerBadges;
  }

  public void setPartnerBadges( Set<GoalBadgeRule> partnerBadges )
  {
    this.partnerBadges = partnerBadges;
  }

  public String getBadgeId()
  {
    return badgeId;
  }

  public void setBadgeId( String badgeId )
  {
    this.badgeId = badgeId;
  }

  public String getBadgeSetupName()
  {
    return badgeSetupName;
  }

  public void setBadgeSetupName( String badgeSetupName )
  {
    this.badgeSetupName = badgeSetupName;
  }

  public String[] getPromotionIds()
  {
    return promotionIds;
  }

  public void setPromotionIds( String[] promotionIds )
  {
    this.promotionIds = promotionIds;
  }

  public String getDisplayDays()
  {
    return displayDays;
  }

  public void setDisplayDays( String displayDays )
  {
    this.displayDays = displayDays;
  }

  public String getTileHighlightPeriod()
  {
    return tileHighlightPeriod;
  }

  public void setTileHighlightPeriod( String tileHighlightPeriod )
  {
    this.tileHighlightPeriod = tileHighlightPeriod;
  }

  public String getBadgeType()
  {
    return badgeType;
  }

  public void setBadgeType( String badgeType )
  {
    this.badgeType = badgeType;
  }

  public String getBadgeCountType()
  {
    return badgeCountType;
  }

  public void setBadgeCountType( String badgeCountType )
  {
    this.badgeCountType = badgeCountType;
  }

  public String getMaxQualifier()
  {
    return maxQualifier;
  }

  public void setMaxQualifier( String maxQualifier )
  {
    this.maxQualifier = maxQualifier;
  }

  public String getRangeAmountMin()
  {
    return rangeAmountMin;
  }

  public void setRangeAmountMin( String rangeAmountMin )
  {
    this.rangeAmountMin = rangeAmountMin;
  }

  public String getRangeAmountMax()
  {
    return rangeAmountMax;
  }

  public void setRangeAmountMax( String rangeAmountMax )
  {
    this.rangeAmountMax = rangeAmountMax;
  }

  public String getBadgeLibraryId()
  {
    return badgeLibraryId;
  }

  public void setBadgeLibraryId( String badgeLibraryId )
  {
    this.badgeLibraryId = badgeLibraryId;
  }

  public String getBadgeName()
  {
    return badgeName;
  }

  public void setBadgeName( String badgeName )
  {
    this.badgeName = badgeName;
  }

  public String getBehaviorCode()
  {
    return behaviorCode;
  }

  public void setBehaviorCode( String behaviorCode )
  {
    this.behaviorCode = behaviorCode;
  }

  public String getBadgeDescription()
  {
    return badgeDescription;
  }

  public void setBadgeDescription( String badgeDescription )
  {
    this.badgeDescription = badgeDescription;
  }

  public String[] getBehaviorNames()
  {
    return behaviorNames;
  }

  public void setBehaviorNames( String[] behaviorNames )
  {
    this.behaviorNames = behaviorNames;
  }

  public List<BadgeLibrary> getBadgeLibraryList()
  {
    return badgeLibraryList;
  }

  public void setBadgeLibraryList( List<BadgeLibrary> badgeLibraryList )
  {
    this.badgeLibraryList = badgeLibraryList;
  }

  public String getEarnedSelectedCategory()
  {
    return earnedSelectedCategory;
  }

  public String[] getBehaviorStringRow()
  {
    return behaviorStringRow;
  }

  public void setBehaviorStringRow( String[] behaviorStringRow )
  {
    this.behaviorStringRow = behaviorStringRow;
  }

  public void setEarnedSelectedCategory( String earnedSelectedCategory )
  {
    this.earnedSelectedCategory = earnedSelectedCategory;
  }

  public String getPointRange()
  {
    return pointRange;
  }

  public void setPointRange( String pointRange )
  {
    this.pointRange = pointRange;
  }

  public String[] getLevelStringRow()
  {
    return levelStringRow;
  }

  public void setLevelStringRow( String[] levelStringRow )
  {
    this.levelStringRow = levelStringRow;
  }

  public String[] getPointRangeStringRow()
  {
    return pointRangeStringRow;
  }

  public void setPointRangeStringRow( String[] pointRangeStringRow )
  {
    this.pointRangeStringRow = pointRangeStringRow;
  }

  public String[] getProgressStringRow()
  {
    return progressStringRow;
  }

  public void setProgressStringRow( String[] progressStringRow )
  {
    this.progressStringRow = progressStringRow;
  }

  public String[] getFileLoadStringRow()
  {
    return fileLoadStringRow;
  }

  public void setFileLoadStringRow( String[] fileLoadStringRow )
  {
    this.fileLoadStringRow = fileLoadStringRow;
  }

  public List getBadgeRulesList()
  {
    return badgeRulesList;
  }

  public void setBadgeRulesList( List badgeRulesList )
  {
    this.badgeRulesList = badgeRulesList;
  }

  public int getCurrentFileLoadTableSize()
  {
    return currentFileLoadTableSize;
  }

  public void setCurrentFileLoadTableSize( int currentFileLoadTableSize )
  {
    this.currentFileLoadTableSize = currentFileLoadTableSize;
  }

  public int getCurrentProgressTableSize()
  {
    return currentProgressTableSize;
  }

  public void setCurrentProgressTableSize( int currentProgressTableSize )
  {
    this.currentProgressTableSize = currentProgressTableSize;
  }

  public int getCurrentPointRangeTableSize()
  {
    return currentPointRangeTableSize;
  }

  public void setCurrentPointRangeTableSize( int currentPointRangeTableSize )
  {
    this.currentPointRangeTableSize = currentPointRangeTableSize;
  }

  public List<BadgeRule> getBadgeRuleList()
  {
    return badgeRuleList;
  }

  public void setBadgeRuleList( List<BadgeRule> badgeRuleList )
  {
    this.badgeRuleList = badgeRuleList;
  }

  public String getSelectedPromotion()
  {
    return selectedPromotion;
  }

  public void setSelectedPromotion( String selectedPromotion )
  {
    this.selectedPromotion = selectedPromotion;
  }

  public String getBehaviorName()
  {
    return PromoRecognitionBehaviorType.lookup( this.getBehaviorCode() ).getName();
    // return behaviorName;
  }

  public void setBehaviorName( String behaviorName )
  {
    this.behaviorName = behaviorName;
  }

  public long getNotificationMessageId()
  {
    return notificationMessageId;
  }

  public void setNotificationMessageId( long notificationMessageId )
  {
    this.notificationMessageId = notificationMessageId;
  }

  public int getBadgeRuleListSize()
  {
    return badgeRuleListSize;
  }

  public void setBadgeRuleListSize( int badgeRuleListSize )
  {
    this.badgeRuleListSize = badgeRuleListSize;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getBadgeTypeName()
  {
    return BadgeType.lookup( this.getBadgeType() ).getName();
  }

  public void setBadgeTypeName( String badgeTypeName )
  {
    this.badgeTypeName = badgeTypeName;
  }

  public String getSelectedPromotionIds()
  {
    return selectedPromotionIds;
  }

  public void setSelectedPromotionIds( String selectedPromotionIds )
  {
    this.selectedPromotionIds = selectedPromotionIds;
  }

  public String[] getDeleteBadges()
  {
    return deleteBadges;
  }

  public void setDeleteBadges( String[] deleteBadges )
  {
    this.deleteBadges = deleteBadges;
  }

  public void load( Badge badge )
  {
    if ( badge.getBadgeCountType() != null )
    {
      this.setBadgeCountType( badge.getBadgeCountType().getCode() );
    }
    this.setBadgeId( badge.getId().toString() );
    this.setBadgeSetupName( badge.getName() );
    if ( badge.getBadgePromotions() != null )
    {
      this.setPromotionIds( badge.getPromotionIdsAsArray( badge.getBadgePromotions() ) );
      this.setSelectedPromotion( badge.getPromotionNamesNoLine( badge.getBadgePromotions() ) );
      this.setSelectedPromotionIds( badge.getPromotionIds( badge.getBadgePromotions() ) );
    }
    if ( StringUtil.isEmpty( this.getSelectedPromotion() ) )
    {
      this.setSelectedPromotion( "No Promotion" );
      this.setSelectedPromotionIds( "-1" );
    }
    this.setBadgeType( badge.getBadgeType().getCode() );
    if ( badge.getBadgeType().getCode().equals( BadgeType.FILELOAD ) && badge.getBadgePromotions().isEmpty() && this.promotionIds == null )
    {
      this.setPromotionIds( new String[] { "-1" } );
    }
    if ( badge.getBadgeRules() != null )
    {
      List badgeRules = new ArrayList( badge.getBadgeRules() );
      java.util.Collections.sort( badgeRules );
      this.setBadgeRuleList( badgeRules );
      this.setBadgeRuleListSize( badgeRules.size() );
    }
    if ( badge.getDisplayEndDays() != null )
    {
      this.setDisplayDays( badge.getDisplayEndDays() + "" );
    }
    if ( badge.getTileHighlightPeriod() != null )
    {
      this.setTileHighlightPeriod( badge.getTileHighlightPeriod() + "" );
    }
    if ( badge.getNotificationMessageId() != null )
    {
      this.setNotificationMessageId( badge.getNotificationMessageId() );
    }
    if ( badge.getBadgeType() != null )
    {
      this.setBadgeTypeName( badge.getBadgeType().getCode() );
    }
    this.setTaxable( badge.isTaxable() );
    this.billCodesActive = badge.isBillCodesActive();

    loadPromotionBillCodes( badge.getPromotionBillCodes() );

    if ( badge.getAllBehaviorPoints() != null && badge.getAllBehaviorPoints() > 0 )
    {
      this.allBehaviorPoints = badge.getAllBehaviorPoints();
      this.includeAllBehaviorPoints = true;
    }
    else
    {
      this.allBehaviorPoints = null;
      this.includeAllBehaviorPoints = false;
    }

    if ( badge.getSubmissionStartDate() != null )
    {
      this.startDate = DateUtils.toDisplayString( badge.getSubmissionStartDate() );
    }
  }

  public List<PromotionBillCode> getPromoBillCodeList( Promotion promotion )
  {
    String trackBillCodeBy = null;

    if ( promotion.isRecognitionPromotion() )
    {
      trackBillCodeBy = BillCodeRecognitionType.lookup( BillCodeRecognitionType.RECEIVER ).getName();
    }
    else if ( promotion.isNominationPromotion() )
    {
      trackBillCodeBy = BillCodeRecognitionType.lookup( BillCodeNominationType.NOMINEE ).getName();
    }
    else if ( promotion.isSSIPromotion() )
    {
      trackBillCodeBy = BillCodeSSIType.lookup( BillCodeSSIType.PARTICIPANT ).getName();
    }

    List<PromotionBillCode> promoBillCodes = new ArrayList<PromotionBillCode>();

    if ( this.billCode1 != null && !this.billCode1.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 0 ), this.billCode1, this.customValue1, trackBillCodeBy ) );
    }
    if ( this.billCode2 != null && !this.billCode2.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 1 ), this.billCode2, this.customValue2, trackBillCodeBy ) );
    }
    if ( this.billCode3 != null && !this.billCode3.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 2 ), this.billCode3, this.customValue3, trackBillCodeBy ) );
    }
    if ( this.billCode4 != null && !this.billCode4.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 3 ), this.billCode4, this.customValue4, trackBillCodeBy ) );
    }
    if ( this.billCode5 != null && !this.billCode5.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 4 ), this.billCode5, this.customValue5, trackBillCodeBy ) );
    }
    if ( this.billCode6 != null && !this.billCode6.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 5 ), this.billCode6, this.customValue6, trackBillCodeBy ) );
    }
    if ( this.billCode7 != null && !this.billCode7.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 6 ), this.billCode7, this.customValue7, trackBillCodeBy ) );
    }
    if ( this.billCode8 != null && !this.billCode8.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 7 ), this.billCode8, this.customValue8, trackBillCodeBy ) );
    }
    if ( this.billCode9 != null && !this.billCode9.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 8 ), this.billCode9, this.customValue9, trackBillCodeBy ) );
    }
    if ( this.billCode10 != null && !this.billCode10.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 9 ), this.billCode10, this.customValue10, trackBillCodeBy ) );
    }
    return promoBillCodes;
  }

  private void loadPromotionBillCodes( List<PromotionBillCode> promotionBillCodes )
  {
    if ( promotionBillCodes != null && promotionBillCodes.size() > 0 )
    {
      Iterator promotionBillCodesList = promotionBillCodes.iterator();
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode1 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode1 != null )
        {
          this.billCode1 = promotionBillCode1.getBillCode();
          this.customValue1 = promotionBillCode1.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode2 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode2 != null )
        {
          this.billCode2 = promotionBillCode2.getBillCode();
          this.customValue2 = promotionBillCode2.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode3 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode3 != null )
        {
          this.billCode3 = promotionBillCode3.getBillCode();
          this.customValue3 = promotionBillCode3.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode4 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode4 != null )
        {
          this.billCode4 = promotionBillCode4.getBillCode();
          this.customValue4 = promotionBillCode4.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode5 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode5 != null )
        {
          this.billCode5 = promotionBillCode5.getBillCode();
          this.customValue5 = promotionBillCode5.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode6 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode6 != null )
        {
          this.billCode6 = promotionBillCode6.getBillCode();
          this.customValue6 = promotionBillCode6.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode7 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode7 != null )
        {
          this.billCode7 = promotionBillCode7.getBillCode();
          this.customValue7 = promotionBillCode7.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode8 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode8 != null )
        {
          this.billCode8 = promotionBillCode8.getBillCode();
          this.customValue8 = promotionBillCode8.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode9 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode9 != null )
        {
          this.billCode9 = promotionBillCode9.getBillCode();
          this.customValue9 = promotionBillCode9.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode10 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode10 != null )
        {
          this.billCode10 = promotionBillCode10.getBillCode();
          this.customValue10 = promotionBillCode10.getCustomValue();
        }
      }
    }
  }

  public void setOverallBadgeStringRow( String overallBadgeStringRow )
  {
    this.overallBadgeStringRow = overallBadgeStringRow;
  }

  public String getOverallBadgeStringRow()
  {
    return overallBadgeStringRow;
  }

  public void setUndefeatedBadgeStringRow( String undefeatedBadgeStringRow )
  {
    this.undefeatedBadgeStringRow = undefeatedBadgeStringRow;
  }

  public String getUndefeatedBadgeStringRow()
  {
    return undefeatedBadgeStringRow;
  }

  public void setOverallLevelStringRow( String[] overallLevelStringRow )
  {
    this.overallLevelStringRow = overallLevelStringRow;
  }

  public String[] getOverallLevelStringRow()
  {
    return overallLevelStringRow;
  }

  public void setStackTdBadgeRuleList( List<BadgeRule> stackTdBadgeRuleList )
  {
    this.stackTdBadgeRuleList = stackTdBadgeRuleList;
  }

  public List<BadgeRule> getStackTdBadgeRuleList()
  {
    return stackTdBadgeRuleList;
  }

  public void setOverallTdBadgeRuleList( List<BadgeRule> overallTdBadgeRuleList )
  {
    this.overallTdBadgeRuleList = overallTdBadgeRuleList;
  }

  public List<BadgeRule> getOverallTdBadgeRuleList()
  {
    return overallTdBadgeRuleList;
  }

  public void setUndefeatedTdBadgeRuleList( List<BadgeRule> undefeatedTdBadgeRuleList )
  {
    this.undefeatedTdBadgeRuleList = undefeatedTdBadgeRuleList;
  }

  public List<BadgeRule> getUndefeatedTdBadgeRuleList()
  {
    return undefeatedTdBadgeRuleList;
  }

  public void setStackTdBadgeRuleListSize( int stackTdBadgeRuleListSize )
  {
    this.stackTdBadgeRuleListSize = stackTdBadgeRuleListSize;
  }

  public int getStackTdBadgeRuleListSize()
  {
    return stackTdBadgeRuleListSize;
  }

  public void setOverallTdBadgeRuleListSize( int overallTdBadgeRuleListSize )
  {
    this.overallTdBadgeRuleListSize = overallTdBadgeRuleListSize;
  }

  public int getOverallTdBadgeRuleListSize()
  {
    return overallTdBadgeRuleListSize;
  }

  public void setUndefeatedTdBadgeRuleListSize( int undefeatedTdBadgeRuleListSize )
  {
    this.undefeatedTdBadgeRuleListSize = undefeatedTdBadgeRuleListSize;
  }

  public int getUndefeatedTdBadgeRuleListSize()
  {
    return undefeatedTdBadgeRuleListSize;
  }

  public void setStackLevelStringRow( String[] stackLevelStringRow )
  {
    this.stackLevelStringRow = stackLevelStringRow;
  }

  public String[] getStackLevelStringRow()
  {
    return stackLevelStringRow;
  }

  public boolean isTaxable()
  {
    return taxable;
  }

  public void setTaxable( boolean taxable )
  {
    this.taxable = taxable;
  }

  public boolean isBillCodesActive()
  {
    return billCodesActive;
  }

  public void setBillCodesActive( boolean billCodesActive )
  {
    this.billCodesActive = billCodesActive;
  }

  public String getBillCode1()
  {
    return billCode1;
  }

  public void setBillCode1( String billCode1 )
  {
    this.billCode1 = billCode1;
  }

  public String getCustomValue1()
  {
    return customValue1;
  }

  public void setCustomValue1( String customValue1 )
  {
    this.customValue1 = customValue1;
  }

  public String getBillCode2()
  {
    return billCode2;
  }

  public void setBillCode2( String billCode2 )
  {
    this.billCode2 = billCode2;
  }

  public String getCustomValue2()
  {
    return customValue2;
  }

  public void setCustomValue2( String customValue2 )
  {
    this.customValue2 = customValue2;
  }

  public String getBillCode3()
  {
    return billCode3;
  }

  public void setBillCode3( String billCode3 )
  {
    this.billCode3 = billCode3;
  }

  public String getCustomValue3()
  {
    return customValue3;
  }

  public void setCustomValue3( String customValue3 )
  {
    this.customValue3 = customValue3;
  }

  public String getBillCode4()
  {
    return billCode4;
  }

  public void setBillCode4( String billCode4 )
  {
    this.billCode4 = billCode4;
  }

  public String getCustomValue4()
  {
    return customValue4;
  }

  public void setCustomValue4( String customValue4 )
  {
    this.customValue4 = customValue4;
  }

  public String getBillCode5()
  {
    return billCode5;
  }

  public void setBillCode5( String billCode5 )
  {
    this.billCode5 = billCode5;
  }

  public String getCustomValue5()
  {
    return customValue5;
  }

  public void setCustomValue5( String customValue5 )
  {
    this.customValue5 = customValue5;
  }

  public String getBillCode6()
  {
    return billCode6;
  }

  public void setBillCode6( String billCode6 )
  {
    this.billCode6 = billCode6;
  }

  public String getCustomValue6()
  {
    return customValue6;
  }

  public void setCustomValue6( String customValue6 )
  {
    this.customValue6 = customValue6;
  }

  public String getBillCode7()
  {
    return billCode7;
  }

  public void setBillCode7( String billCode7 )
  {
    this.billCode7 = billCode7;
  }

  public String getCustomValue7()
  {
    return customValue7;
  }

  public void setCustomValue7( String customValue7 )
  {
    this.customValue7 = customValue7;
  }

  public String getBillCode8()
  {
    return billCode8;
  }

  public void setBillCode8( String billCode8 )
  {
    this.billCode8 = billCode8;
  }

  public String getCustomValue8()
  {
    return customValue8;
  }

  public void setCustomValue8( String customValue8 )
  {
    this.customValue8 = customValue8;
  }

  public String getBillCode9()
  {
    return billCode9;
  }

  public void setBillCode9( String billCode9 )
  {
    this.billCode9 = billCode9;
  }

  public String getCustomValue9()
  {
    return customValue9;
  }

  public void setCustomValue9( String customValue9 )
  {
    this.customValue9 = customValue9;
  }

  public String getBillCode10()
  {
    return billCode10;
  }

  public void setBillCode10( String billCode10 )
  {
    this.billCode10 = billCode10;
  }

  public String getCustomValue10()
  {
    return customValue10;
  }

  public void setCustomValue10( String customValue10 )
  {
    this.customValue10 = customValue10;
  }

  public Long getBadgePoints()
  {
    return badgePoints;
  }

  public void setBadgePoints( Long badgePoints )
  {
    this.badgePoints = badgePoints;
  }

  public boolean isEligibleForSweepstake()
  {
    return eligibleForSweepstake;
  }

  public void setEligibleForSweepstake( boolean eligibleForSweepstake )
  {
    this.eligibleForSweepstake = eligibleForSweepstake;
  }

  public Long getAllBehaviorPoints()
  {
    return allBehaviorPoints;
  }

  public void setAllBehaviorPoints( Long allBehaviorPoints )
  {
    this.allBehaviorPoints = allBehaviorPoints;
  }

  public boolean isIncludeAllBehaviorPoints()
  {
    return includeAllBehaviorPoints;
  }

  public void setIncludeAllBehaviorPoints( boolean includeAllBehaviorPoints )
  {
    this.includeAllBehaviorPoints = includeAllBehaviorPoints;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

}
