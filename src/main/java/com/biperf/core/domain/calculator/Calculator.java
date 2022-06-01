/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/calculator/Calculator.java,v $
 */

package com.biperf.core.domain.calculator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.CalculatorAwardType;
import com.biperf.core.domain.enums.CalculatorStatusType;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * Calculator.
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
 * <td>May 20, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Calculator extends BaseDomain
{
  public static final String CM_CALC_WEIGHT_ASSET_PREFIX = "calculator_weight_data.weight.";
  public static final String CM_CALC_WEIGHT_ASSET_TYPE = "_CalculatorWeightData";
  public static final String CM_CALC_WEIGHT_SECTION = "calculator_weight_data";
  public static final String CM_CALC_WEIGHT_NAME_KEY = "CALCULATOR_WEIGHT_NAME";
  public static final String CM_CALC_WEIGHT_NAME_KEY_DESC = "Calculator Weight Name";

  public static final String CM_CALC_SCORE_ASSET_PREFIX = "calculator_score_data.score.";
  public static final String CM_CALC_SCORE_ASSET_TYPE = "_CalculatorScoreData";
  public static final String CM_CALC_SCORE_SECTION = "calculator_score_data";
  public static final String CM_CALC_SCORE_NAME_KEY = "CALCULATOR_SCORE_NAME";
  public static final String CM_CALC_SCORE_NAME_KEY_DESC = "Calculator Score Name";

  public static final String CM_CRITERION_ASSET_PREFIX = "calculator_criterion_data.criterion.";
  public static final String CM_CRITERION_ASSET_TYPE = "_CalculatorCriterionData";
  public static final String CM_CRITERION_SECTION = "calculator_criterion_data";
  public static final String CM_CRITERION_NAME_KEY = "CALCULATOR_CRITERION_NAME";
  public static final String CM_CRITERION_NAME_KEY_DESC = "Calculator Criterion Name";

  public static final String CM_CRITERION_RATING_ASSET_PREFIX = "calculator_criterion_rating.rating";
  public static final String CM_CRITERION_RATING_ASSET_TYPE = "_CalculatorCriterionRating";
  public static final String CM_CRITERION_RATING_SECTION = "calculator_criterion_rating";
  public static final String CM_CRITERION_RATING_KEY = "CRITERION_RATING";
  public static final String CM_CRITERION_RATING_KEY_DESC = "Criterion Rating";

  private String name;
  private String description;
  private boolean weightedScore;
  private boolean displayWeights;
  private String weightCMAssetName;
  private boolean displayScores;
  private String scoreCMAssetName;
  // private boolean awardTypeFixed;
  private CalculatorAwardType calculatorAwardType;

  private CalculatorStatusType calculatorStatusType;

  private List calculatorCriterion = new ArrayList();
  private Set promotions = new LinkedHashSet();
  private Set calculatorPayouts = new LinkedHashSet();
  private int promotionCount = 0;

  // transient and not stored in database; here for convenience
  private String weightLabel;
  private String scoreLabel;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof Calculator ) )
    {
      return false;
    }

    final Calculator calculator = (Calculator)object;

    if ( calculator.getName() != null && !calculator.getName().equals( this.getName() ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result = 0;

    result += this.getName() != null ? this.getName().hashCode() : 0;

    return result;
  }

  /**
   * Builds a String representation of this.
   * 
   * @return String
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();

    sb.append( "[CALCULATOR {" );
    sb.append( "calculator.id - " + this.getId() + ", " );
    sb.append( "calculator.name - " + this.getName() + ", " );
    sb.append( "status - " + this.getCalculatorStatusType().getCode() + ", " );
    sb.append( "}]" );
    return sb.toString();
  }

  /**
   * Does a deep copy of the Calculator. This is a customized implementation
   * of java.lang.Object#clone()
   * 
   * @see java.lang.Object#clone()
   * @param cloneWithChildren
   * @param newCalculatorName
   * @return Object
   */
  public Object deepCopy( boolean cloneWithChildren, String newCalculatorName )
  {
    Calculator calculator = new Calculator();
    calculator.setName( newCalculatorName );
    calculator.setDescription( this.getDescription() );
    calculator.setCalculatorStatusType( CalculatorStatusType.lookup( "undrconstr" ) );
    calculator.setCalculatorAwardType( this.calculatorAwardType );
    calculator.setDisplayScores( this.displayScores );
    calculator.setScoreCMAssetName( this.scoreCMAssetName );
    calculator.setDisplayWeights( this.displayWeights );
    calculator.setWeightCMAssetName( this.weightCMAssetName );
    calculator.setWeightedScore( this.weightedScore );
    calculator.setPromotions( new HashSet() );

    if ( cloneWithChildren )
    {
      Iterator iter = this.getCalculatorCriterion().iterator();
      while ( iter.hasNext() )
      {
        CalculatorCriterion criterionToCopy = (CalculatorCriterion)iter.next();
        calculator.addCalculatorCriterion( (CalculatorCriterion)criterionToCopy.deepCopy( true ) );
      }
      Iterator payoutIter = this.getCalculatorPayouts().iterator();
      while ( payoutIter.hasNext() )
      {
        CalculatorPayout payoutToCopy = (CalculatorPayout)payoutIter.next();
        calculator.addCalculatorPayout( (CalculatorPayout)payoutToCopy.deepCopy() );
      }

    }
    else
    {
      calculator.setCalculatorCriterion( new ArrayList() );
      calculator.setCalculatorPayouts( new LinkedHashSet() );
    }
    return calculator;
  }

  public CalculatorStatusType getCalculatorStatusType()
  {
    return calculatorStatusType;
  }

  public void setCalculatorStatusType( CalculatorStatusType calculatorStatusType )
  {
    this.calculatorStatusType = calculatorStatusType;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public boolean isDisplayScores()
  {
    return displayScores;
  }

  public void setDisplayScores( boolean displayScores )
  {
    this.displayScores = displayScores;
  }

  public boolean isDisplayWeights()
  {
    return displayWeights;
  }

  public void setDisplayWeights( boolean displayWeights )
  {
    this.displayWeights = displayWeights;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public int getPromotionCount()
  {
    if ( promotionCount == 0 && promotions != null )
    {
      promotionCount = promotions.size();
    }
    return promotionCount;
  }

  public void setPromotionCount( int promotionCount )
  {
    this.promotionCount = promotionCount;
  }

  public Set getPromotions()
  {
    return promotions;
  }

  public void setPromotions( Set promotions )
  {
    this.promotions = promotions;
    if ( promotions != null )
    {
      this.promotionCount = promotions.size();
    }
  }

  public String getScoreCMAssetName()
  {
    return scoreCMAssetName;
  }

  public void setScoreCMAssetName( String scoreCMAssetName )
  {
    this.scoreCMAssetName = scoreCMAssetName;
  }

  public String getWeightCMAssetName()
  {
    return weightCMAssetName;
  }

  public void setWeightCMAssetName( String weightCMAssetName )
  {
    this.weightCMAssetName = weightCMAssetName;
  }

  public boolean isWeightedScore()
  {
    return weightedScore;
  }

  public void setWeightedScore( boolean weightedScore )
  {
    this.weightedScore = weightedScore;
  }

  public List<CalculatorCriterion> getCalculatorCriterion()
  {
    return calculatorCriterion;
  }

  public void setCalculatorCriterion( List calculatorCriterion )
  {
    this.calculatorCriterion = calculatorCriterion;
  }

  public void addCalculatorCriterion( CalculatorCriterion calculatorCriterion )
  {
    calculatorCriterion.setCalculator( this );
    this.calculatorCriterion.add( calculatorCriterion );
  }

  public CalculatorCriterion getCriterion( Long criteriaId )
  {
    for ( CalculatorCriterion cc : getCalculatorCriterion() )
    {
      if ( cc.getId().equals( criteriaId ) )
      {
        return cc;
      }
    }

    return null;
  }

  public Set<CalculatorPayout> getCalculatorPayouts()
  {
    return calculatorPayouts;
  }

  public void setCalculatorPayouts( Set calculatorPayouts )
  {
    this.calculatorPayouts = calculatorPayouts;
  }

  public void addCalculatorPayout( CalculatorPayout calculatorPayout )
  {
    calculatorPayout.setCalculator( this );
    this.calculatorPayouts.add( calculatorPayout );
  }

  /**
   * Checks if the calculator is at a status that can be deleted.
   * 
   * @return boolean - Returns true if the calculator is "Under Construction" or "Completed" with no links
   *         to expired promotions, and false otherwise.
   */
  public boolean isDeleteable()
  {
    // first make sure there is a status type available to check
    if ( getCalculatorStatusType() == null )
    {
      return false;
    }

    if ( getCalculatorStatusType().isUnderConstruction() || getCalculatorStatusType().isCompleted() && getPromotions().isEmpty() )
    {
      return true;
    }

    return false;
  }

  /**
   * Get form assigned or not
   * 
   * @return true if form is assigned; return false if in any other status
   */
  public boolean isAssigned()
  {
    if ( calculatorStatusType != null )
    {
      return calculatorStatusType.getCode().equals( CalculatorStatusType.ASSIGNED );
    }

    return false;
  }

  /**
   * Get form complete or not
   * 
   * @return true if form is complete; return false if in any other status
   */
  public boolean isComplete()
  {
    if ( calculatorStatusType != null )
    {
      return calculatorStatusType.getCode().equals( CalculatorStatusType.COMPLETED );
    }

    return false;
  }

  /**
   * Get form under construction or not
   * 
   * @return true if form is under construction; return false if in any other status
   */
  public boolean isUnderConstruction()
  {
    if ( calculatorStatusType != null )
    {
      return calculatorStatusType.getCode().equals( CalculatorStatusType.UNDER_CONSTRUCTION );
    }

    return false;
  }

  /**
   * Get CM text for asset code and key
   * @param cmAssetCode
   * @param nameCmKey
   * @return String
   */
  public String getCmText( String cmAssetCode, String nameCmKey )
  {
    return ContentReaderManager.getText( cmAssetCode, nameCmKey );
  }

  public String getCmText( String cmAssetCodeSuffix, String cmAssetCode, String nameCmKey )
  {
    String finAssetCode = cmAssetCodeSuffix + "." + cmAssetCode;
    return ContentReaderManager.getText( finAssetCode, nameCmKey );
  }

  public CalculatorAwardType getCalculatorAwardType()
  {
    return calculatorAwardType;
  }

  public void setCalculatorAwardType( CalculatorAwardType calculatorAwardType )
  {
    this.calculatorAwardType = calculatorAwardType;
  }

  public String getScoreLabel()
  {
    return scoreLabel;
  }

  public void setScoreLabel( String scoreLabel )
  {
    this.scoreLabel = scoreLabel;
  }

  public String getWeightLabel()
  {
    return weightLabel;
  }

  public void setWeightLabel( String weightLabel )
  {
    this.weightLabel = weightLabel;
  }
}
