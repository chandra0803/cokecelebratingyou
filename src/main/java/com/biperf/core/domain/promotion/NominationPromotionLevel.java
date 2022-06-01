/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.promotion;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringEscapeUtils;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * @author poddutur
 * @since Feb 10, 2016
 */
public class NominationPromotionLevel extends BaseDomain implements Cloneable
{
  private static final long serialVersionUID = -2677657758414634787L;

  private NominationPromotion nominationPromotion;

  private PromotionAwardsType awardPayoutType;
  private boolean nominationAwardAmountTypeFixed;
  private BigDecimal nominationAwardAmountFixed;
  private BigDecimal nominationAwardAmountMin;
  private BigDecimal nominationAwardAmountMax;
  private String payoutDescription;
  private String payoutDescriptionAssetCode;
  private BigDecimal payoutValue;
  private String payoutCurrency;
  private Long quantity;
  private Calculator calculator;
  private String levelLabel;
  private String levelLabelAssetCode;

  private Long levelIndex;

  public NominationPromotionLevel deepCopy()
  {
    NominationPromotionLevel clone = new NominationPromotionLevel();

    clone.setLevelLabel( this.getLevelLabel() );
    clone.setLevelLabelAssetCode( this.getLevelLabelAssetCode() );
    clone.setAwardPayoutType( this.getAwardPayoutType() );
    clone.setNominationAwardAmountTypeFixed( this.isNominationAwardAmountTypeFixed() );
    clone.setNominationAwardAmountFixed( this.getNominationAwardAmountFixed() );
    clone.setNominationAwardAmountMin( this.getNominationAwardAmountMin() );
    clone.setNominationAwardAmountMax( this.getNominationAwardAmountMax() );
    clone.setPayoutDescription( this.getPayoutDescription() );
    clone.setPayoutDescriptionAssetCode( this.getPayoutDescriptionAssetCode() );
    clone.setPayoutValue( this.getPayoutValue() );
    clone.setPayoutCurrency( this.getPayoutCurrency() );
    clone.setQuantity( this.getQuantity() );
    clone.setCalculator( this.getCalculator() );
    clone.setLevelIndex( this.getLevelIndex() );

    return clone;
  }

  public NominationPromotion getNominationPromotion()
  {
    return nominationPromotion;
  }

  public void setNominationPromotion( NominationPromotion nominationPromotion )
  {
    this.nominationPromotion = nominationPromotion;
  }

  public BigDecimal getNominationAwardAmountFixed()
  {
    return nominationAwardAmountFixed;
  }

  public void setNominationAwardAmountFixed( BigDecimal nominationAwardAmountFixed )
  {
    this.nominationAwardAmountFixed = nominationAwardAmountFixed;
  }

  public BigDecimal getNominationAwardAmountMin()
  {
    return nominationAwardAmountMin;
  }

  public void setNominationAwardAmountMin( BigDecimal nominationAwardAmountMin )
  {
    this.nominationAwardAmountMin = nominationAwardAmountMin;
  }

  public BigDecimal getNominationAwardAmountMax()
  {
    return nominationAwardAmountMax;
  }

  public void setNominationAwardAmountMax( BigDecimal nominationAwardAmountMax )
  {
    this.nominationAwardAmountMax = nominationAwardAmountMax;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public String getPayoutDescriptionAssetCode()
  {
    return payoutDescriptionAssetCode;
  }

  public void setPayoutDescriptionAssetCode( String payoutDescriptionAssetCode )
  {
    this.payoutDescriptionAssetCode = payoutDescriptionAssetCode;
  }

  public BigDecimal getPayoutValue()
  {
    return payoutValue;
  }

  public void setPayoutValue( BigDecimal payoutValue )
  {
    this.payoutValue = payoutValue;
  }

  public String getPayoutCurrency()
  {
    return payoutCurrency;
  }

  public void setPayoutCurrency( String payoutCurrency )
  {
    this.payoutCurrency = payoutCurrency;
  }

  public Long getQuantity()
  {
    return quantity;
  }

  public void setQuantity( Long quantity )
  {
    this.quantity = quantity;
  }

  public PromotionAwardsType getAwardPayoutType()
  {
    return awardPayoutType;
  }

  public void setAwardPayoutType( PromotionAwardsType awardPayoutType )
  {
    this.awardPayoutType = awardPayoutType;
  }

  public boolean isNominationAwardAmountTypeFixed()
  {
    return nominationAwardAmountTypeFixed;
  }

  public void setNominationAwardAmountTypeFixed( boolean nominationAwardAmountTypeFixed )
  {
    this.nominationAwardAmountTypeFixed = nominationAwardAmountTypeFixed;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( awardPayoutType == null ? 0 : awardPayoutType.hashCode() );
    result = prime * result + ( calculator == null ? 0 : calculator.hashCode() );
    result = prime * result + ( levelIndex == null ? 0 : levelIndex.hashCode() );
    result = prime * result + ( levelLabel == null ? 0 : levelLabel.hashCode() );
    result = prime * result + ( levelLabelAssetCode == null ? 0 : levelLabelAssetCode.hashCode() );
    result = prime * result + ( nominationAwardAmountFixed == null ? 0 : nominationAwardAmountFixed.hashCode() );
    result = prime * result + ( nominationAwardAmountMax == null ? 0 : nominationAwardAmountMax.hashCode() );
    result = prime * result + ( nominationAwardAmountMin == null ? 0 : nominationAwardAmountMin.hashCode() );
    result = prime * result + ( nominationAwardAmountTypeFixed ? 1231 : 1237 );
    result = prime * result + ( nominationPromotion == null ? 0 : nominationPromotion.hashCode() );
    result = prime * result + ( payoutCurrency == null ? 0 : payoutCurrency.hashCode() );
    result = prime * result + ( payoutDescription == null ? 0 : payoutDescription.hashCode() );
    result = prime * result + ( payoutDescriptionAssetCode == null ? 0 : payoutDescriptionAssetCode.hashCode() );
    result = prime * result + ( payoutValue == null ? 0 : payoutValue.hashCode() );
    result = prime * result + ( quantity == null ? 0 : quantity.hashCode() );
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
    NominationPromotionLevel other = (NominationPromotionLevel)obj;
    if ( awardPayoutType == null )
    {
      if ( other.awardPayoutType != null )
      {
        return false;
      }
    }
    else if ( !awardPayoutType.equals( other.awardPayoutType ) )
    {
      return false;
    }
    if ( calculator == null )
    {
      if ( other.calculator != null )
      {
        return false;
      }
    }
    else if ( !calculator.equals( other.calculator ) )
    {
      return false;
    }
    if ( levelIndex == null )
    {
      if ( other.levelIndex != null )
      {
        return false;
      }
    }
    else if ( !levelIndex.equals( other.levelIndex ) )
    {
      return false;
    }
    if ( levelLabel == null )
    {
      if ( other.levelLabel != null )
      {
        return false;
      }
    }
    else if ( !levelLabel.equals( other.levelLabel ) )
    {
      return false;
    }
    if ( levelLabelAssetCode == null )
    {
      if ( other.levelLabelAssetCode != null )
      {
        return false;
      }
    }
    else if ( !levelLabelAssetCode.equals( other.levelLabelAssetCode ) )
    {
      return false;
    }
    if ( nominationAwardAmountFixed == null )
    {
      if ( other.nominationAwardAmountFixed != null )
      {
        return false;
      }
    }
    else if ( !nominationAwardAmountFixed.equals( other.nominationAwardAmountFixed ) )
    {
      return false;
    }
    if ( nominationAwardAmountMax == null )
    {
      if ( other.nominationAwardAmountMax != null )
      {
        return false;
      }
    }
    else if ( !nominationAwardAmountMax.equals( other.nominationAwardAmountMax ) )
    {
      return false;
    }
    if ( nominationAwardAmountMin == null )
    {
      if ( other.nominationAwardAmountMin != null )
      {
        return false;
      }
    }
    else if ( !nominationAwardAmountMin.equals( other.nominationAwardAmountMin ) )
    {
      return false;
    }
    if ( nominationAwardAmountTypeFixed != other.nominationAwardAmountTypeFixed )
    {
      return false;
    }
    if ( nominationPromotion == null )
    {
      if ( other.nominationPromotion != null )
      {
        return false;
      }
    }
    else if ( !nominationPromotion.equals( other.nominationPromotion ) )
    {
      return false;
    }
    if ( payoutCurrency == null )
    {
      if ( other.payoutCurrency != null )
      {
        return false;
      }
    }
    else if ( !payoutCurrency.equals( other.payoutCurrency ) )
    {
      return false;
    }
    if ( payoutDescription == null )
    {
      if ( other.payoutDescription != null )
      {
        return false;
      }
    }
    else if ( !payoutDescription.equals( other.payoutDescription ) )
    {
      return false;
    }
    if ( payoutDescriptionAssetCode == null )
    {
      if ( other.payoutDescriptionAssetCode != null )
      {
        return false;
      }
    }
    else if ( !payoutDescriptionAssetCode.equals( other.payoutDescriptionAssetCode ) )
    {
      return false;
    }
    if ( payoutValue == null )
    {
      if ( other.payoutValue != null )
      {
        return false;
      }
    }
    else if ( !payoutValue.equals( other.payoutValue ) )
    {
      return false;
    }
    if ( quantity == null )
    {
      if ( other.quantity != null )
      {
        return false;
      }
    }
    else if ( !quantity.equals( other.quantity ) )
    {
      return false;
    }
    return true;
  }

  public Long getLevelIndex()
  {
    return levelIndex;
  }

  public void setLevelIndex( Long levelIndex )
  {
    this.levelIndex = levelIndex;
  }

  public Calculator getCalculator()
  {
    return calculator;
  }

  public void setCalculator( Calculator calculator )
  {
    this.calculator = calculator;
  }

  public String getLevelLabel()
  {
    return levelLabel;
  }

  public void setLevelLabel( String levelLabel )
  {
    this.levelLabel = levelLabel;
  }

  public String getLevelLabelAssetCode()
  {
    return levelLabelAssetCode;
  }

  public void setLevelLabelAssetCode( String levelLabelAssetCode )
  {
    this.levelLabelAssetCode = levelLabelAssetCode;
  }

  public String getLevelLabelFromCM()
  {
    String levelLabel = null;
    if ( this.levelLabelAssetCode != null )
    {
      levelLabel = CmsResourceBundle.getCmsBundle().getString( this.levelLabelAssetCode, Promotion.PROMOTION_LEVEL_LABEL_NAME_KEY_PREFIX );
    }
    return StringEscapeUtils.unescapeHtml4( levelLabel );
  }

  public String getPayoutDescriptionFromCM()
  {
    String payoutDescription = null;
    if ( this.payoutDescriptionAssetCode != null )
    {
      payoutDescription = CmsResourceBundle.getCmsBundle().getString( this.payoutDescriptionAssetCode, Promotion.PAYOUT_DESCRIPTION_KEY_PREFIX );
    }
    return StringEscapeUtils.unescapeHtml4( payoutDescription );
  }

}
