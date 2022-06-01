
package com.biperf.core.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;

import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ManagerOverrideGoalLevel;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SelectGoalUtil;

public class GoalQuestValueBean implements Cloneable, Serializable
{

  private static final long serialVersionUID = 3256726164994536240L;

  private GoalQuestPromotion promotion;
  private PaxGoal paxGoal;
  private boolean owner;
  private boolean manager; // Fix 25646
  private Long awardAmountEarned;
  private String dateOfLastProgress;
  private boolean showProgramRules;
  private String nodeName; // Node name award earned for - for owners only
  private Long percentageAchieved; // For Pax this is the percentage of the goal amount achieved.
                                   // For
  // managers this is the percentage of the team that achieved for manager selects
  // or actual team achievement override types
  private boolean orderPlaced = false;
  private boolean itemDiscontinued = false;
  private boolean awardEarned = false;
  private AbstractGoalLevel goalLevel;
  private URL imageUrl;
  private String imageName;
  private URL detailImageURL;
  private String shoppingURL;
  private String imageCopy;
  private List paxPartners;
  private String partnerPaxName;
  private boolean partner;
  private BigDecimal partnerPayout;
  private String servletContext;

  public String getPartnerPaxName()
  {
    return partnerPaxName;
  }

  public void setPartnerPaxName( String partnerPaxName )
  {
    this.partnerPaxName = partnerPaxName;
  }

  public boolean isGoalSelected()
  {
    return paxGoal != null && paxGoal.getGoalLevel() != null;
  }

  public String getServletContext()
  {
    return servletContext;
  }

  public void setServletContext( String servletContext )
  {
    this.servletContext = servletContext;
  }

  public boolean isGoalSelectable( String timeZoneId )
  {
    if ( !owner || promotion.getOverrideStructure().equals( ManagerOverrideStructure.lookup( ManagerOverrideStructure.OVERRIDE_PERCENT ) ) )
    {
      return isInGoalSelection( timeZoneId );
    }
    return false;
  }

  public boolean isGoalSelectable()
  {
    if ( !owner || promotion.getOverrideStructure().equals( ManagerOverrideStructure.lookup( ManagerOverrideStructure.OVERRIDE_PERCENT ) ) )
    {
      return isInGoalSelection();
    }
    return false;
  }

  public boolean isInGoalSelection( String timeZoneId )
  {
    return !promotion.isIssueAwardsRun()
        && DateUtils.isDateBetween( new Date(), DateUtils.toStartDate( promotion.getGoalCollectionStartDate() ), DateUtils.toEndDate( promotion.getGoalCollectionEndDate() ), timeZoneId );
  }

  public boolean isInGoalSelection()
  {
    return !promotion.isIssueAwardsRun()
        && DateUtils.isTodaysDateBetween( DateUtils.toStartDate( promotion.getGoalCollectionStartDate() ), DateUtils.toEndDate( promotion.getGoalCollectionEndDate() ) );
  }

  /**
   * @return boolean to indicate if promotion name needs to be wrapped in next line in home page
   */
  public boolean isPromotionNameSplit()
  {
    return promotion != null && promotion.getName().length() > 22;
  }

  /**
   * @return String[]
   */
  public String[] getSplitPromotionNames()
  {
    String promotionName = promotion.getName();
    if ( promotionName != null && promotionName.length() > 0 )
    {
      int size = promotionName.length() / 22 + 1;
      String[] promotionNames = new String[size];
      int subStringIndex1 = 0;
      int subStringIndex2 = 22;
      for ( int i = 0; i <= size - 1; i++ )
      {
        if ( i == size - 1 )
        {
          promotionNames[i] = promotionName.substring( subStringIndex1 );
        }
        else
        {
          promotionNames[i] = promotionName.substring( subStringIndex1, subStringIndex2 );
        }
        subStringIndex1 = subStringIndex2;
        subStringIndex2 = subStringIndex2 + 22;
      }
      return promotionNames;
    }
    return null;
  }

  /**
   * @return if an award has been earned then returns that value.
   *            other the value of the award for the selected goal level.  If no goal selected return null
   */
  public Long getAwardValue()
  {
    if ( isAwardEarned() )
    {
      return awardAmountEarned;
    }
    if ( getGoalLevel() != null )
    {
      if ( getGoalLevel().isManagerOverrideGoalLevel() )
      {
        ManagerOverrideGoalLevel managerOverrideGoalLevel = (ManagerOverrideGoalLevel)getGoalLevel();
        if ( managerOverrideGoalLevel.getTeamAchievementPercent() != null )
        {
          BigDecimal managerAward = managerOverrideGoalLevel.getManagerAward();
          if ( managerAward != null )
          {
            return new Long( managerAward.longValue() );
          }
        }
      }
      else
      {
        BigDecimal awardAmount = null;
        GoalLevel goalLevel = (GoalLevel)getGoalLevel();
        awardAmount = goalLevel.getAward();
        if ( awardAmount != null )
        {
          return new Long( awardAmount.longValue() );
        }
      }
    }
    return null;
  }

  public String getFormattedGoalAmount()
  {
    BigDecimal goalAmount = getGoalAmount();
    if ( promotion.getAchievementPrecision().getCode().equals( "two" ) )
    {
      goalAmount = goalAmount.setScale( 2 );
    }
    if ( goalAmount != null )
    {
      if ( getGoalLevel().isManagerOverrideGoalLevel() )
      {
        ManagerOverrideGoalLevel managerOverrideGoalLevel = (ManagerOverrideGoalLevel)getGoalLevel();
        if ( managerOverrideGoalLevel.getTeamAchievementPercent() != null )
        {
          return goalAmount.toString(); // bug fix:38085
        }
      }
    }
    if ( goalAmount != null )
    {
      return goalAmount.toString();
    }
    return null;
  }

  private BigDecimal getGoalAmount()
  {
    return SelectGoalUtil.getCalculatedGoalAmount( getGoalLevel(), paxGoal );
  }

  public BigDecimal getPercentageToGoal()
  {
    if ( getGoalLevel() != null )
    {
      if ( !getGoalLevel().isManagerOverrideGoalLevel() )
      {
        BigDecimal goalAmount = getGoalAmount();
        if ( goalAmount != null && paxGoal.getCurrentValue() != null )
        {
          BigDecimal currentPercentage = goalAmount.divide( paxGoal.getCurrentValue(), 2, BigDecimal.ROUND_DOWN );
          return currentPercentage.movePointRight( 2 );
        }
      }
    }
    return null;
  }

  public String getFormattedGoalSelectionEndDate()
  {
    return DateUtils.toDisplayString( promotion.getGoalCollectionEndDate() );
  }

  public PaxGoal getPaxGoal()
  {
    return paxGoal;
  }

  public void setPaxGoal( PaxGoal paxGoal )
  {
    this.paxGoal = paxGoal;
  }

  public GoalQuestPromotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( GoalQuestPromotion promotion )
  {
    this.promotion = promotion;
  }

  public boolean isOwner()
  {
    return owner;
  }

  public void setOwner( boolean owner )
  {
    this.owner = owner;
  }

  public boolean isManager()
  {
    return manager;
  }

  public void setManager( boolean manager )
  {
    this.manager = manager;
  }

  public AbstractGoalLevel getGoalLevel()
  {
    // return goalLevel if set from GoalQuestPromotion else return from PaxGoal
    return goalLevel != null ? goalLevel : paxGoal != null ? paxGoal.getGoalLevel() : null;
  }

  public Long getAwardAmountEarned()
  {
    return awardAmountEarned;
  }

  public void setAwardAmountEarned( Long awardAmountEarned )
  {
    this.awardAmountEarned = awardAmountEarned;
  }

  public String getDateOfLastProgress()
  {
    return dateOfLastProgress;
  }

  public void setDateOfLastProgress( String dateOfLastProgress )
  {
    this.dateOfLastProgress = dateOfLastProgress;
  }

  public boolean isShowProgramRules()
  {
    return showProgramRules;
  }

  public void setShowProgramRules( boolean showProgramRules )
  {
    this.showProgramRules = showProgramRules;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public Long getPercentageAchieved()
  {
    return percentageAchieved;
  }

  public void setPercentageAchieved( Long percentageAchieved )
  {
    this.percentageAchieved = percentageAchieved;
  }

  public void setGoalLevel( AbstractGoalLevel goalLevel )
  {
    this.goalLevel = goalLevel;
  }

  public String getImageName()
  {
    return imageName;
  }

  public void setImageName( String imageName )
  {
    this.imageName = imageName;
  }

  public URL getImageUrl()
  {
    return imageUrl;
  }

  public void setImageUrl( URL imageUrl )
  {
    this.imageUrl = imageUrl;
  }

  public URL getDetailImageURL()
  {
    return detailImageURL;
  }

  public void setDetailImageURL( URL detailImageURL )
  {
    this.detailImageURL = detailImageURL;
  }

  public boolean isOrderPlaced()
  {
    return orderPlaced;
  }

  public void setOrderPlaced( boolean orderPlaced )
  {
    this.orderPlaced = orderPlaced;
  }

  public boolean isItemDiscontinued()
  {
    return itemDiscontinued;
  }

  public void setItemDiscontinued( boolean itemDiscontinued )
  {
    this.itemDiscontinued = itemDiscontinued;
  }

  public String getShoppingURL()
  {
    return shoppingURL;
  }

  public void setShoppingURL( String shoppingURL )
  {
    this.shoppingURL = shoppingURL;
  }

  public boolean isAwardEarned()
  {
    return awardEarned;
  }

  public void setAwardEarned( boolean awardEarned )
  {
    this.awardEarned = awardEarned;
  }

  public String getImageCopy()
  {
    return imageCopy;
  }

  public void setImageCopy( String imageCopy )
  {
    this.imageCopy = imageCopy;
  }

  public List getPaxPartners()
  {
    return paxPartners;
  }

  public void setPaxPartners( List paxPartners )
  {
    this.paxPartners = paxPartners;
  }

  public boolean isPartner()
  {
    return partner;
  }

  public void setPartner( boolean partner )
  {
    this.partner = partner;
  }

  public BigDecimal getPartnerPayout()
  {
    return partnerPayout;
  }

  public void setPartnerPayout( BigDecimal partnerPayout )
  {
    this.partnerPayout = partnerPayout;
  }

}
