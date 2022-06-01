/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/QuizPromotion.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.quiz.Quiz;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Promotion.
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
 * <td>sedey</td>
 * <td>Oct 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizPromotion extends Promotion
{

  private Set<PromotionCert> promotionCertificates = new HashSet<PromotionCert>();

  private boolean allowUnlimitedAttempts;
  private int maximumAttempts;
  private boolean includePassingQuizCertificate;
  private boolean awardActive;
  private Long awardAmount;
  private Quiz quiz;
  private String quizDetails;

  /**
   * Constructor
   */
  public QuizPromotion()
  {
    super();
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
    QuizPromotion clonedPromotion = (QuizPromotion)super.deepCopy( cloneWithChildren, newPromotionName, newChildPromotionNameHolders );

    if ( clonedPromotion.getBudgetMaster() != null )
    {
      if ( !clonedPromotion.getBudgetMaster().isMultiPromotion() )
      {
        // if the BudgetMaster is not shareable across promotions, clear
        // it out
        clonedPromotion.setBudgetMaster( null );
      }
    }
    clonedPromotion.setPromotionCertificates( new HashSet() );
    if ( getPromotionCertificates() != null )
    {
      for ( Iterator iter = this.getPromotionCertificates().iterator(); iter.hasNext(); )
      {
        PromotionCert certificate = (PromotionCert)iter.next();
        clonedPromotion.addCertificate( certificate.deepCopy() );
      }
    }
    return clonedPromotion;
  }

  public Set<PromotionCert> getPromotionCertificates()
  {
    return promotionCertificates;
  }

  public void setPromotionCertificates( Set<PromotionCert> promotionCertificates )
  {
    this.promotionCertificates = promotionCertificates;
  }

  public void addCertificate( PromotionCert promotionCertificate )
  {
    promotionCertificate.setPromotion( this );
    this.promotionCertificates.add( promotionCertificate );
  }

  public boolean isAllowUnlimitedAttempts()
  {
    return allowUnlimitedAttempts;
  }

  public void setAllowUnlimitedAttempts( boolean allowUnlimitedAttempts )
  {
    this.allowUnlimitedAttempts = allowUnlimitedAttempts;
  }

  public boolean isAwardActive()
  {
    return awardActive;
  }

  public void setAwardActive( boolean awardActive )
  {
    this.awardActive = awardActive;
  }

  public Long getAwardAmount()
  {
    return awardAmount;
  }

  public void setAwardAmount( Long awardAmount )
  {
    this.awardAmount = awardAmount;
  }

  public boolean isIncludePassingQuizCertificate()
  {
    return includePassingQuizCertificate;
  }

  public void setIncludePassingQuizCertificate( boolean includePassingQuizCertificate )
  {
    this.includePassingQuizCertificate = includePassingQuizCertificate;
  }

  public int getMaximumAttempts()
  {
    return maximumAttempts;
  }

  public void setMaximumAttempts( int maximumAttempts )
  {
    this.maximumAttempts = maximumAttempts;
  }

  public boolean hasParent()
  {
    return false;
  }

  public Quiz getQuiz()
  {
    return quiz;
  }

  public void setQuiz( Quiz quiz )
  {
    this.quiz = quiz;
  }

  /**
   * Get has sweepstakes to process Note: Promotion needs to be fully hydrated with sweepstakes
   * 
   * @return boolean returns true if there are sweepstakes to process
   */
  public boolean getHasSweepstakesToProcess()
  {
    if ( this.promotionSweepstakes != null )
    {
      Iterator iter = this.promotionSweepstakes.iterator();
      while ( iter.hasNext() )
      {
        PromotionSweepstake sweepstake = (PromotionSweepstake)iter.next();
        if ( !sweepstake.isProcessed() )
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Get has sweepstakes history (sweepstakes that have been processed) Note: Promotion needs to be
   * fully hydrated with sweepstakes
   * 
   * @return boolean returns true if there is a sweepstakes history
   */
  public boolean getHasSweepstakesHistory()
  {
    if ( this.promotionSweepstakes != null )
    {
      Iterator iter = this.promotionSweepstakes.iterator();
      while ( iter.hasNext() )
      {
        PromotionSweepstake sweepstake = (PromotionSweepstake)iter.next();
        if ( sweepstake.isProcessed() )
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.promotion.Promotion#isClaimFormUsed()
   */
  public boolean isClaimFormUsed()
  {
    return false;
  }

  public String getQuizDetails()
  {
    return quizDetails;
  }

  public void setQuizDetails( String quizDetails )
  {
    this.quizDetails = quizDetails;
  }

  public String getQuizDetailsText()
  {
    String quizDetailsText = null;
    if ( this.quizDetails != null )
    {
      quizDetailsText = CmsResourceBundle.getCmsBundle().getString( this.quizDetails, Promotion.QUIZ_PROMOTION_DETAILS_CM_ASSET_TYPE_KEY );
    }

    return quizDetailsText;
  }

}
