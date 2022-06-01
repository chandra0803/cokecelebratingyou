/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/AbstractRecognitionPromotion.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.biperf.core.domain.enums.ClaimAwardType;
import com.biperf.core.domain.enums.PromotionBehaviorType;
import com.biperf.core.domain.multimedia.ECard;

public abstract class AbstractRecognitionPromotion extends Promotion
{

  public static final String AWARD_STRUCTURE_LEVEL = "level";

  public static final String PAX_CUSTOM_DISPLAY_ORDER = "custom";
  public static final String PAX_STANDARDORRANDOM_DISP_ORDER = "standardorrandom";
  public static final String PAX_ALPHABETICAL_DISPLAY_ORDER = "alphabetical";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // AWARD FIELDS

  /**
   * If true, issue awards to participants who are recognized as part of this
   * promotion; if false, do not.
   */
  private boolean awardActive;

  /**
   * If true, the amount of the award given to the recognizee is fixed--it
   * cannot be set by the recognizer or the approver. If false, the recognizer
   * and/or approver can set the award amount.
   */
  private boolean awardAmountTypeFixed;

  /**
   * The fixed award amount.
   */
  private Long awardAmountFixed;

  /**
   * The minimum award amount. Used only when <code>awardActive</code> is
   * true and <code>awardAmountTypeFixed</code> is false.
   */
  private Long awardAmountMin;

  /**
   * The maximum award amount. Used only when <code>awardActive</code> is
   * true and <code>awardAmountTypeFixed</code> is false.
   */
  private Long awardAmountMax;

  // BEHAVIOR FIELDS

  /**
   * If true, let the recognizer to specify the behavior for which the
   * recognizee is being recognized; if false, do not.
   */
  private boolean behaviorActive;

  /**
   * Identifies the behaviors that can be recognized under this promotion.
   */
  private Set promotionBehaviors = new LinkedHashSet();

  // BUDGET FIELDS

  /**
   * If true, load budgets from a file; if false, do not.
   */
  private boolean fileloadBudgetAmount;

  // CARD FIELDS

  /**
   * If true, let the recognizer send a promotion card to the recognizee; if
   * false, do not.
   */
  private boolean cardActive;

  /**
   * The recognizee's e-mail address.
   */
  private String cardClientEmailAddress;

  /**
   * If true, the recognizer has specified the information need to delivery a
   * promotion card to the recognizee; if false, the recognizer has not.
   */
  private boolean cardClientSetupDone;

  /**
   * Identifies the e-cards that can be sent to recognizees under this
   * promotion.
   */
  private Set<PromotionECard> promotionECard = new LinkedHashSet<>();

  /**
   * Whether to show cards in random, alphabetical, or custom order.
   */
  private String paxDisplayOrder;

  // CERTIFICATE FIELDS

  /**
   * If true, issue certificates to participants who are recognized as part of
   * this promotion; if false, do not.
   */
  private boolean includeCertificate;

  /**
   * Choose Award Structure like Item or Level
   */
  private String awardStructure;

  /*
   * Choose APQ Conversion
   */
  private boolean apqConversion;

  /*
   * Set to true if recipeint should not be notified
   */
  private boolean noNotification;

  private boolean featuredAwardsEnabled;

  private boolean allowYourOwnCard;

  private boolean drawYourOwnCard;

  private boolean allowPublicRecognition;

  private boolean allowPromotionPrivate;
  
  //client customization start
  private boolean allowMeme;
  private boolean allowSticker;
  private boolean allowUploadOwnMeme;

  // ---------------------------------------------------------------------------
  // Copy Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a deep copy of this object.
   *
   * @param cloneWithChildren
   * @param newPromotionName
   *            the promotion name to be assigned to the copy.
   * @param newChildPromotionNameHolders
   * @return a deep copy of this <code>AbstractRecognitionPromotion</code>
   *         object.
   * @throws CloneNotSupportedException
   *             if one of this objects component objects does not support
   *             cloning.
   */
  public Object deepCopy( boolean cloneWithChildren, String newPromotionName, List newChildPromotionNameHolders ) throws CloneNotSupportedException
  {
    AbstractRecognitionPromotion clonedPromotion = (AbstractRecognitionPromotion)super.deepCopy( cloneWithChildren, newPromotionName, newChildPromotionNameHolders );

    // award properties
    clonedPromotion.setAwardActive( this.isAwardActive() );
    clonedPromotion.setAwardAmountTypeFixed( this.isAwardAmountTypeFixed() );
    clonedPromotion.setAwardAmountFixed( this.getAwardAmountFixed() );
    clonedPromotion.setAwardAmountMax( this.getAwardAmountMax() );
    clonedPromotion.setAwardAmountMin( this.getAwardAmountMin() );
    clonedPromotion.setFeaturedAwardsEnabled( this.isFeaturedAwardsEnabled() );

    clonedPromotion.setCalculator( this.getCalculator() );
    clonedPromotion.setScoreBy( this.getScoreBy() );

    // Merchandise option property
    clonedPromotion.setAwardStructure( this.awardStructure );
    clonedPromotion.setNoNotification( this.noNotification );

    // behavior properties
    clonedPromotion.setBehaviorActive( this.isBehaviorActive() );

    clonedPromotion.setPromotionBehaviors( new LinkedHashSet() );
    if ( getPromotionBehaviors() != null )
    {
      for ( Iterator iter = getPromotionBehaviors().iterator(); iter.hasNext(); )
      {
        PromotionBehavior behavior = (PromotionBehavior)iter.next();
        if ( clonedPromotion.isRecognitionPromotion() )
        {
          clonedPromotion.addPromotionBehavior( behavior.getPromotionBehaviorType() );
        }
        else
        {
          clonedPromotion.addPromotionBehavior( behavior.getPromotionBehaviorType(), behavior.getBehaviorOrder() );
        }
      }
    }

    // budget properties
    clonedPromotion.setFileloadBudgetAmount( this.isFileloadBudgetAmount() );

    // card properties
    clonedPromotion.setCardActive( this.isCardActive() );
    clonedPromotion.setCardClientSetupDone( this.isCardClientSetupDone() );
    clonedPromotion.setCardClientEmailAddress( this.getCardClientEmailAddress() );

    clonedPromotion.setPromotionECard( new TreeSet() );
    if ( getPromotionECard() != null )
    {
      for ( Iterator iter = this.getPromotionECard().iterator(); iter.hasNext(); )
      {
        PromotionECard card = (PromotionECard)iter.next();
        clonedPromotion.addECard( card.deepCopy() );
      }
    }

    // certificate properties
    clonedPromotion.setIncludeCertificate( this.isIncludeCertificate() );

    if ( clonedPromotion.getBudgetMaster() != null )
    {
      if ( !clonedPromotion.getBudgetMaster().isMultiPromotion() )
      {
        // if the BudgetMaster is not shareable across promotions, clear
        // it out
        clonedPromotion.setBudgetMaster( null );
      }
    }

    clonedPromotion.setAllowPublicRecognition( this.isAllowPublicRecognition() );

    clonedPromotion.setAllowPromotionPrivate( this.isAllowPromotionPrivate() );
    
    //client customization start
    clonedPromotion.setAllowMeme( this.allowMeme );
    clonedPromotion.setAllowSticker( this.allowSticker );
    clonedPromotion.setAllowUploadOwnMeme( this.allowUploadOwnMeme );
    //client customization end
    return clonedPromotion;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public void addECard( PromotionECard promotionECard )
  {
    promotionECard.setPromotion( this );
    this.promotionECard.add( promotionECard );
  }

  public void addPromotionEcard( ECard eCard, String orderNumber )
  {
    PromotionECard promotionEcard = new PromotionECard( this, eCard, orderNumber );
    this.getPromotionECard().add( promotionEcard );
  }

  public void addPromotionBehavior( PromotionBehaviorType behavior )
  {
    PromotionBehavior promotionBehavior = new PromotionBehavior( this, behavior );
    promotionBehaviors.add( promotionBehavior );
  }

  public void addPromotionBehavior( PromotionBehaviorType behavior, String behaviorOrder )
  {
    PromotionBehavior promotionBehavior = new PromotionBehavior( this, behavior, behaviorOrder );
    promotionBehaviors.add( promotionBehavior );
  }

  public boolean isAwardActive()
  {
    return awardActive;
  }

  public Long getAwardAmountFixed()
  {
    return awardAmountFixed;
  }

  public Long getAwardAmountMax()
  {
    return awardAmountMax;
  }

  public Long getAwardAmountMin()
  {
    return awardAmountMin;
  }

  public boolean isBehaviorActive()
  {
    return behaviorActive;
  }

  public boolean isCardActive()
  {
    return cardActive;
  }

  public String getCardClientEmailAddress()
  {
    return cardClientEmailAddress;
  }

  public boolean isCardClientSetupDone()
  {
    return cardClientSetupDone;
  }

  public boolean isFileloadBudgetAmount()
  {
    return fileloadBudgetAmount;
  }

  public boolean isIncludeCertificate()
  {
    return includeCertificate;
  }

  public Set<PromotionBehavior> getPromotionBehaviors()
  {
    return promotionBehaviors;
  }

  public Set<PromotionECard> getPromotionECard()
  {
    return promotionECard;
  }

  public ECard getPromotionECardBy( Long ecardId )
  {
    if ( promotionECard != null )
    {
      for ( PromotionECard card : promotionECard )
      {
        if ( card.geteCard().getId().equals( ecardId ) )
        {
          return card.geteCard();
        }
      }
    }

    return null;
  }

  public String getPaxDisplayOrder()
  {
    return paxDisplayOrder;
  }

  public void setPaxDisplayOrder( String paxDisplayOrder )
  {
    this.paxDisplayOrder = paxDisplayOrder;
  }

  public void setAwardActive( boolean awardActive )
  {
    this.awardActive = awardActive;
  }

  public void setAwardAmountFixed( Long awardAmountFixed )
  {
    this.awardAmountFixed = awardAmountFixed;
  }

  public void setAwardAmountMax( Long awardAmountMax )
  {
    this.awardAmountMax = awardAmountMax;
  }

  public void setAwardAmountMin( Long awardAmountMin )
  {
    this.awardAmountMin = awardAmountMin;
  }

  public void setAwardAmountTypeFixed( boolean awardAmountTypeFixed )
  {
    this.awardAmountTypeFixed = awardAmountTypeFixed;
  }

  public void setBehaviorActive( boolean behaviourActive )
  {
    this.behaviorActive = behaviourActive;
  }

  public void setCardActive( boolean cardActive )
  {
    this.cardActive = cardActive;
  }

  public void setCardClientEmailAddress( String cardClientEmailAddress )
  {
    this.cardClientEmailAddress = cardClientEmailAddress;
  }

  public void setCardClientSetupDone( boolean cardClientSetupDone )
  {
    this.cardClientSetupDone = cardClientSetupDone;
  }

  public void setFileloadBudgetAmount( boolean fileloadBudgetAmount )
  {
    this.fileloadBudgetAmount = fileloadBudgetAmount;
  }

  public void setIncludeCertificate( boolean includeCertificate )
  {
    this.includeCertificate = includeCertificate;
  }

  public void setPromotionBehaviors( Set promotionBehaviors )
  {
    this.promotionBehaviors = promotionBehaviors;
  }

  public void setPromotionECard( Set promotionECard )
  {
    this.promotionECard = promotionECard;
  }

  // ---------------------------------------------------------------------------
  // Predicate Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns false because recognition type promotions cannot have child
   * promotions.
   *
   * @return false.
   */
  public boolean hasParent()
  {
    return false;
  }

  /**
   * Returns true because recognition type promotions use claim forms.
   *
   * @return true.
   */
  public boolean isClaimFormUsed()
  {
    return true;
  }

  public boolean isAwardAmountTypeFixed()
  {
    /*
     * if(this.calculator != null ) return calculator.isAwardTypeFixed();
     */
    return awardAmountTypeFixed;
  }

  public String getAwardStructure()
  {
    return awardStructure;
  }

  public void setAwardStructure( String awardStructure )
  {
    this.awardStructure = awardStructure;
  }

  public boolean isApqConversion()
  {
    return apqConversion;
  }

  public void setApqConversion( boolean apqConversion )
  {
    this.apqConversion = apqConversion;
  }

  public boolean isNoNotification()
  {
    return noNotification;
  }

  public void setNoNotification( boolean noNotification )
  {
    this.noNotification = noNotification;
  }

  public boolean isFeaturedAwardsEnabled()
  {
    return featuredAwardsEnabled;
  }

  public void setFeaturedAwardsEnabled( boolean featuredAwardsEnabled )
  {
    this.featuredAwardsEnabled = featuredAwardsEnabled;
  }

  public boolean isAwardStructureLevel()
  {
    if ( this.awardStructure == null )
    {
      return false;
    }
    return AWARD_STRUCTURE_LEVEL.equals( this.awardStructure );
  }

  public void setAllowYourOwnCard( boolean allowYourOwnCard )
  {
    this.allowYourOwnCard = allowYourOwnCard;
  }

  public boolean isAllowYourOwnCard()
  {
    return allowYourOwnCard;
  }

  public void setDrawYourOwnCard( boolean drawYourOwnCard )
  {
    this.drawYourOwnCard = drawYourOwnCard;
  }

  public boolean isDrawYourOwnCard()
  {
    return drawYourOwnCard;
  }

  public void setAllowPublicRecognition( boolean allowPublicRecognition )
  {
    this.allowPublicRecognition = allowPublicRecognition;
  }

  public boolean isAllowPublicRecognition()
  {
    return allowPublicRecognition;
  }

  public boolean isAllowPromotionPrivate()
  {
    return allowPromotionPrivate;
  }

  public void setAllowPromotionPrivate( boolean allowPromotionPrivate )
  {
    this.allowPromotionPrivate = allowPromotionPrivate;
  }

  /**
   * Based on award setup, this takes into account promotion type, award type, specifier type, etc 
   */
  public ClaimAwardType getClaimAwardType()
  {
    if ( isAwardActive() )
    {
      if ( isNominationPromotion() )
      {
        /*
         * && ( (NominationPromotion)this ).getAwardSpecifierType() != null && ( (
         * (NominationPromotion)this ).getAwardSpecifierType().isApprover() )
         */
        return ClaimAwardType.NONE;
      }
      else
      {
        if ( getAwardType().isPointsAwardType() )
        {
          if ( isAwardAmountTypeFixed() )
          {
            return ClaimAwardType.POINTS_FIXED;
          }
          else if ( getCalculator() != null )
          {
            return ClaimAwardType.CALCULATED;
          }
          else
          {
            return ClaimAwardType.POINTS_RANGE;
          }
        }
        else if ( getAwardType().isMerchandiseAwardType() )
        {
          if ( getCalculator() != null )
          {
            if ( getScoreBy() == null || getScoreBy().isScoreByGiver() )
            {
              return ClaimAwardType.CALCULATED;
            }
            else
            {
              return ClaimAwardType.NONE;
            }
          }
          else
          {
            return ClaimAwardType.LEVELS;
          }
        }
      }
    }

    return ClaimAwardType.NONE;
  }

  public boolean isAllowMeme()
  {
    return allowMeme;
  }

  public void setAllowMeme( boolean allowMeme )
  {
    this.allowMeme = allowMeme;
  }

  public boolean isAllowSticker()
  {
    return allowSticker;
  }

  public void setAllowSticker( boolean allowSticker )
  {
    this.allowSticker = allowSticker;
  }

  public boolean isAllowUploadOwnMeme()
  {
    return allowUploadOwnMeme;
  }

  public void setAllowUploadOwnMeme( boolean allowUploadOwnMeme )
  {
    this.allowUploadOwnMeme = allowUploadOwnMeme;
  }
}
