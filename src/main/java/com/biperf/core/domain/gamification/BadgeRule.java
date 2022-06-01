
package com.biperf.core.domain.gamification;

import java.util.Locale;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.BadgeLevelType;
import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.util.CmsResourceBundle;

public class BadgeRule extends BaseDomain implements Comparable<BadgeRule>
{

  private static final long serialVersionUID = 1L;

  public static final String BADGE_RULES_SECTION_CODE = "gamification";// Section Code
  public static final String BADGE_RULES_CMASSET_TYPE_NAME = "Name Type"; // Type Name
  public static final String BADGE_RULES_CMASSET_TYPE_KEY = "HTML_KEY"; // Type Key
  public static final String BADGE_RULES_CMASSET_NAME = "Badge Rule Text"; // CM ASSET NAME //String
                                                                           // Key
  public static final String BADGE_RULES_CMASSET_PREFIX = "gamification.rulestext."; // Code

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private Badge badgePromotion;
  private String badgeLibraryCMKey;
  private String badgeName;
  private String badgeDescription;
  private Long minimumQualifier;
  private Long maximumQualifier;
  private String levelName;
  private BadgeLevelType levelType;
  private String behaviorName;
  private Long countryId;
  private String badgeLibDisplayName;
  private String promotionNames;
  private String countryCode;
  private ParticipantType participantType = ParticipantType.NONE;

  // Badge setup Admin
  private Long badgePoints;
  private boolean eligibleForSweepstake;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Badge getBadgePromotion()
  {
    return badgePromotion;
  }

  public void setBadgePromotion( Badge badgePromotion )
  {
    this.badgePromotion = badgePromotion;
  }

  public String getBadgeLibraryCMKey()
  {
    return badgeLibraryCMKey;
  }

  public void setBadgeLibraryCMKey( String badgeLibraryCMKey )
  {
    this.badgeLibraryCMKey = badgeLibraryCMKey;
  }

  public String getBadgeName()
  {
    return badgeName;
  }

  public void setBadgeName( String badgeName )
  {
    this.badgeName = badgeName;
  }

  public String getBadgeDescription()
  {
    return badgeDescription;
  }

  public void setBadgeDescription( String badgeDescription )
  {
    this.badgeDescription = badgeDescription;
  }

  public Long getMinimumQualifier()
  {
    return minimumQualifier;
  }

  public void setMinimumQualifier( Long minimumQualifier )
  {
    this.minimumQualifier = minimumQualifier;
  }

  public Long getMaximumQualifier()
  {
    return maximumQualifier;
  }

  public void setMaximumQualifier( Long maximumQualifier )
  {
    this.maximumQualifier = maximumQualifier;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public String getBehaviorName()
  {
    return behaviorName;
  }

  public void setBehaviorName( String behaviorName )
  {
    this.behaviorName = behaviorName;
  }

  public String getBehaviorDisplayName()
  {
    String behaviorName = this.getBehaviorName();
    if ( behaviorName != null )
    {
      if ( PromoRecognitionBehaviorType.lookup( this.getBehaviorName() ) != null )
      {
        return PromoRecognitionBehaviorType.lookup( this.getBehaviorName() ).getName();
      }
      else
      {
        return PromoNominationBehaviorType.lookup( this.getBehaviorName() ).getName();
      }
    }
    else
    {
      return "";
    }
  }

  public void setBehaviorDisplayName( String behaviorDisplayName )
  {
  }

  public Long getCountryId()
  {
    return countryId;
  }

  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  public String getPromotionNames()
  {
    return promotionNames;
  }

  public void setPromotionNames( String promotionNames )
  {
    this.promotionNames = promotionNames;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getBadgeLibDisplayName()
  {
    return badgeLibDisplayName;
  }

  public void setBadgeLibDisplayName( String badgeLibDisplayName )
  {
    this.badgeLibDisplayName = badgeLibDisplayName;
  }

  public String getBadgeNameTextFromCM()
  {
    String rulesText = "";
    if ( this.badgeName != null )
    {
      rulesText = CmsResourceBundle.getCmsBundle().getString( this.badgeName, BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY );
    }

    return rulesText.trim();
  }

  public String getBadgeNameTextFromCM( Locale locale )
  {
    String rulesText = "";
    if ( this.badgeName != null )
    {
      rulesText = getCMAssetService().getString( this.badgeName, BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY, locale, true );
    }

    return rulesText.trim();
  }

  public String getBadgeDescriptionTextFromCM()
  {
    String rulesText = "";
    if ( this.badgeDescription != null )
    {
      rulesText = CmsResourceBundle.getCmsBundle().getString( this.badgeDescription, BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY );
    }

    if ( rulesText.startsWith( "???" ) )
    {
      return "";
    }

    return rulesText.trim();
  }

  public ParticipantType getParticipantType()
  {
    return participantType;
  }

  public void setParticipantType( ParticipantType participantType )
  {
    this.participantType = participantType;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------
  public String toString()
  {
    return ToStringBuilder.reflectionToString( Badge.class );
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( badgeLibraryCMKey == null ? 0 : badgeLibraryCMKey.hashCode() );
    result = prime * result + ( badgeName == null ? 0 : badgeName.hashCode() );
    result = prime * result + ( badgePromotion == null ? 0 : badgePromotion.hashCode() );
    result = prime * result + ( participantType == null ? 0 : participantType.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    BadgeRule other = (BadgeRule)obj;
    if ( badgeLibraryCMKey == null )
    {
      if ( other.badgeLibraryCMKey != null )
      {
        return false;
      }
    }
    else if ( !badgeLibraryCMKey.equals( other.badgeLibraryCMKey ) )
    {
      return false;
    }
    if ( badgeName == null )
    {
      if ( other.badgeName != null )
      {
        return false;
      }
    }
    else if ( !badgeName.equals( other.badgeName ) )
    {
      return false;
    }
    if ( badgePromotion == null )
    {
      if ( other.badgePromotion != null )
      {
        return false;
      }
    }
    else if ( !badgePromotion.equals( other.badgePromotion ) )
    {
      return false;
    }
    if ( participantType != other.participantType )
    {
      return false;
    }
    return true;
  }

  public int compareTo( BadgeRule object ) throws ClassCastException
  {
    BadgeRule badgeRule = (BadgeRule)object;
    return this.getId().compareTo( badgeRule.getId() );
  }

  public void setLevelType( BadgeLevelType levelType )
  {
    this.levelType = levelType;
  }

  public BadgeLevelType getLevelType()
  {
    return levelType;
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

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)BeanLocator.getBean( CMAssetService.BEAN_NAME );
  }

}
