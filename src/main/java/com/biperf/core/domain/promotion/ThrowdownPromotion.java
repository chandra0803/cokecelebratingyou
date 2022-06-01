
package com.biperf.core.domain.promotion;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import com.biperf.core.domain.enums.BaseUnitPosition;
import com.biperf.core.domain.enums.TeamUnavailableResolverType;
import com.biperf.core.domain.enums.ThrowdownAchievementPrecision;
import com.biperf.core.domain.enums.ThrowdownPromotionType;
import com.biperf.core.domain.enums.ThrowdownRoundingMethod;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Audience;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

@SuppressWarnings( "serial" )
public class ThrowdownPromotion extends Promotion
{
  private int numberOfRounds = 0;
  private int lengthOfRound = 0;
  private int daysPriorToRoundStartSchedule = 2;// default to 2
  private Date headToHeadStartDate;
  private ThrowdownPromotionType throwdownPromotionType;
  private TeamUnavailableResolverType teamUnavailableResolverType;
  private ThrowdownRoundingMethod roundingMethod;
  private ThrowdownAchievementPrecision achievementPrecision;
  private BaseUnitPosition baseUnitPosition;
  private boolean displayTeamProgress;
  private boolean smackTalkAvailable;
  private String baseUnit;

  private Set<Division> divisions = new HashSet<Division>();
  private Set<StackStandingPayoutGroup> stackStandingPayoutGroups = new LinkedHashSet<StackStandingPayoutGroup>();

  public int getLengthOfRound()
  {
    return lengthOfRound;
  }

  public void setLengthOfRound( int lengthOfRound )
  {
    this.lengthOfRound = lengthOfRound;
  }

  public int getNumberOfRounds()
  {
    return numberOfRounds;
  }

  public void setNumberOfRounds( int numberOfRounds )
  {
    this.numberOfRounds = numberOfRounds;
  }

  public Date getHeadToHeadStartDate()
  {
    return headToHeadStartDate;
  }

  public void setHeadToHeadStartDate( Date headToHeadStartDate )
  {
    this.headToHeadStartDate = headToHeadStartDate;
  }

  public TeamUnavailableResolverType getTeamUnavailableResolverType()
  {
    return teamUnavailableResolverType;
  }

  public void setTeamUnavailableResolverType( TeamUnavailableResolverType teamUnavailableResolverType )
  {
    this.teamUnavailableResolverType = teamUnavailableResolverType;
  }

  public ThrowdownPromotionType getThrowdownPromotionType()
  {
    return throwdownPromotionType;
  }

  public void setThrowdownPromotionType( ThrowdownPromotionType throwdownPromotionType )
  {
    this.throwdownPromotionType = throwdownPromotionType;
  }

  @SuppressWarnings( "unchecked" )
  public Set<Audience> getSpectatorAudiences()
  {
    Set<Audience> audiences = new HashSet<Audience>();
    audiences.addAll( getPrimaryAudiences() );
    audiences.addAll( getAllCompetitorAudiences() );
    return audiences;
  }

  public Set<Audience> getAllCompetitorAudiences()
  {
    Set<Audience> audiences = new HashSet<Audience>();
    for ( Division division : divisions )
    {
      Set<DivisionCompetitorsAudience> competitors = division.getCompetitorsAudience();
      for ( DivisionCompetitorsAudience audience : competitors )
      {
        audiences.add( audience.getAudience() );
      }
    }
    return audiences;
  }

  public Set<Division> getDivisions()
  {
    return divisions;
  }

  public void setDivisions( Set<Division> divisions )
  {
    this.divisions = divisions;
  }

  public ThrowdownPromotion()
  {
    super();
  }

  @Override
  public boolean hasParent()
  {
    return false;
  }

  @Override
  public boolean isClaimFormUsed()
  {
    return false;
  }

  public Set<StackStandingPayoutGroup> getStackStandingPayoutGroups()
  {
    return stackStandingPayoutGroups;
  }

  public void setStackStandingPayoutGroups( Set<StackStandingPayoutGroup> stackStandingPayoutGroups )
  {
    this.stackStandingPayoutGroups = stackStandingPayoutGroups;
  }

  public String getBaseUnit()
  {
    return baseUnit;
  }

  public String getBaseUnitText()
  {
    String baseUnit = null;
    if ( this.baseUnit != null )
    {
      baseUnit = CmsResourceBundle.getCmsBundle().getString( this.baseUnit, Promotion.TD_PROMO_BASE_UNIT_KEY_PREFIX );
    }
    return StringEscapeUtils.unescapeHtml4( baseUnit );
  }

  public void setBaseUnit( String baseUnit )
  {
    this.baseUnit = baseUnit;
  }

  public BaseUnitPosition getBaseUnitPosition()
  {
    return baseUnitPosition;
  }

  public void setBaseUnitPosition( BaseUnitPosition baseUnitPosition )
  {
    this.baseUnitPosition = baseUnitPosition;
  }

  /**
   * Add a stackStandingPayoutGroup to stackStandingPayoutGroups
   * 
   * @param stackStandingPayoutGroup
   */
  public void addStackStandingPayoutGroup( StackStandingPayoutGroup stackStandingPayoutGroup )
  {
    stackStandingPayoutGroup.setPromotion( this );
    this.stackStandingPayoutGroups.add( stackStandingPayoutGroup );
  }

  public void addDivision( Division division )
  {
    division.setPromotion( this );
    this.divisions.add( division );
  }

  /**
   * Return the StackStandingPayoutGroup for the given nodeType, or null if none exists.
   */
  public StackStandingPayoutGroup getStackStandingPayoutGroup( NodeType nodeType )
  {
    StackStandingPayoutGroup matchingStackStandingPayoutGroup = null;
    for ( Iterator<StackStandingPayoutGroup> iter = stackStandingPayoutGroups.iterator(); iter.hasNext(); )
    {
      StackStandingPayoutGroup stackStandingPayoutGroup = iter.next();
      if ( !stackStandingPayoutGroup.isHierarchyPayoutGroup() && stackStandingPayoutGroup.getNodeType().equals( nodeType ) )
      {
        matchingStackStandingPayoutGroup = stackStandingPayoutGroup;
        break;
      }
    }
    return matchingStackStandingPayoutGroup;
  }

  public boolean hasStackStandingPayoutGroupForNodeType( NodeType nodeType )
  {
    return getStackStandingPayoutGroup( nodeType ) != null;
  }

  public Division getDivision( Division division )
  {
    for ( Iterator<Division> iter = divisions.iterator(); iter.hasNext(); )
    {
      Division div = iter.next();
      if ( div.getId().equals( division.getId() ) )
      {
        return div;
      }
    }
    return null;
  }

  public int getDaysPriorToRoundStartSchedule()
  {
    return daysPriorToRoundStartSchedule;
  }

  public void setDaysPriorToRoundStartSchedule( int daysPriorToRoundStartSchedule )
  {
    this.daysPriorToRoundStartSchedule = daysPriorToRoundStartSchedule;
  }

  public boolean isDisplayTeamProgress()
  {
    return displayTeamProgress;
  }

  public void setDisplayTeamProgress( boolean displayTeamProgress )
  {
    this.displayTeamProgress = displayTeamProgress;
  }

  @JsonProperty( "smackTalkAvailable" )
  public boolean isSmackTalkAvailable()
  {
    return smackTalkAvailable;
  }

  public void setSmackTalkAvailable( boolean smackTalkAvailable )
  {
    this.smackTalkAvailable = smackTalkAvailable;
  }

  public ThrowdownRoundingMethod getRoundingMethod()
  {
    return roundingMethod;
  }

  public void setRoundingMethod( ThrowdownRoundingMethod roundingMethod )
  {
    this.roundingMethod = roundingMethod;
  }

  public ThrowdownAchievementPrecision getAchievementPrecision()
  {
    return achievementPrecision;
  }

  public void setAchievementPrecision( ThrowdownAchievementPrecision achievementPrecision )
  {
    this.achievementPrecision = achievementPrecision;
  }

  /**
     * Does a deep copy of the promotion and its children if specified. This is a customized
     * implementation of
     * 
     * @see java.lang.Object#clone()
     * @param cloneWithChildren
     * @param newPromotionName
     * @param newChildPromotionNameHolders
     * @return Object
     * @throws CloneNotSupportedException
     */
  public Object deepCopy( boolean cloneWithChildren, String newPromotionName, List newChildPromotionNameHolders ) throws CloneNotSupportedException
  {
    ThrowdownPromotion clonedPromotion = (ThrowdownPromotion)super.deepCopy( cloneWithChildren, newPromotionName, newChildPromotionNameHolders );

    // copy the promotionPrimaryAudiences
    clonedPromotion.setPromotionPrimaryAudiences( new LinkedHashSet() );
    for ( Iterator promotionSubmitterAudiencesIter = this.getPromotionPrimaryAudiences().iterator(); promotionSubmitterAudiencesIter.hasNext(); )
    {
      PromotionPrimaryAudience promotionSubmitterAudience = (PromotionPrimaryAudience)promotionSubmitterAudiencesIter.next();
      clonedPromotion.addPromotionPrimaryAudience( (PromotionPrimaryAudience)promotionSubmitterAudience.clone() );
    }

    // copy the stackStandingPayoutGroups
    clonedPromotion.setStackStandingPayoutGroups( new LinkedHashSet<StackStandingPayoutGroup>() );
    for ( Iterator<StackStandingPayoutGroup> iter = this.getStackStandingPayoutGroups().iterator(); iter.hasNext(); )
    {
      StackStandingPayoutGroup promotionStackStandingPayoutGroup = iter.next();
      clonedPromotion.addStackStandingPayoutGroup( (StackStandingPayoutGroup)promotionStackStandingPayoutGroup.clone() );
    }

    // copy the divisions
    clonedPromotion.setDivisions( new LinkedHashSet<Division>() );
    for ( Iterator<Division> divisionIter = this.getDivisions().iterator(); divisionIter.hasNext(); )
    {
      Division division = divisionIter.next();
      clonedPromotion.addDivision( (Division)division.clone() );
    }

    return clonedPromotion;
  }

  /**
   * @param notificationTypeCode
   * @return boolean true if notification required else false
   */
  public boolean isNotificationRequired( String notificationTypeCode )
  {
    if ( this.getPromotionNotifications() != null )
    {
      for ( Iterator<PromotionNotification> notificationsIter = this.getPromotionNotifications().iterator(); notificationsIter.hasNext(); )
      {
        PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notificationsIter.next();
        // if notification is set up for a particular Email Notification Type then return true
        if ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equals( notificationTypeCode ) )
        {
          if ( promotionNotificationType.getNotificationMessageId() != -1 )
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  public StackStandingPayoutGroup getHierarchyStackStandingPayoutGroup()
  {
    StackStandingPayoutGroup matchingStackStandingPayoutGroup = null;
    for ( Iterator<StackStandingPayoutGroup> iter = stackStandingPayoutGroups.iterator(); iter.hasNext(); )
    {
      StackStandingPayoutGroup stackStandingPayoutGroup = iter.next();
      if ( stackStandingPayoutGroup.isHierarchyPayoutGroup() )
      {
        matchingStackStandingPayoutGroup = stackStandingPayoutGroup;
        break;
      }
    }
    return matchingStackStandingPayoutGroup;
  }

  public boolean hasHierarchyStackStandingPayoutGroup()
  {
    return getHierarchyStackStandingPayoutGroup() != null;
  }

  public static Comparator<ThrowdownPromotion> UpperCaseNameComparator = new Comparator<ThrowdownPromotion>()
  {
    @Override
    public int compare( ThrowdownPromotion td1, ThrowdownPromotion td2 )
    {
      return td1.getUpperCaseName().compareTo( td2.getUpperCaseName() );
    }
  };

}
