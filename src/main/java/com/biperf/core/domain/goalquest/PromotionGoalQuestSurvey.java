
package com.biperf.core.domain.goalquest;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.Survey;

public class PromotionGoalQuestSurvey extends BaseDomain implements Cloneable
{
  private Promotion promotion;

  private Survey survey;

  public Object clone() throws CloneNotSupportedException
  {
    PromotionGoalQuestSurvey clonedPromotionGoalQuestSurvey = (PromotionGoalQuestSurvey)super.clone();
    clonedPromotionGoalQuestSurvey.setPromotion( null );
    clonedPromotionGoalQuestSurvey.setSurvey( this.getSurvey() );
    clonedPromotionGoalQuestSurvey.resetBaseDomain();
    return clonedPromotionGoalQuestSurvey;
  }

  public Survey getSurvey()
  {
    return survey;
  }

  public void setSurvey( Survey survey )
  {
    this.survey = survey;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( survey == null ? 0 : survey.hashCode() );
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
    PromotionGoalQuestSurvey other = (PromotionGoalQuestSurvey)obj;
    if ( survey == null )
    {
      if ( other.survey != null )
      {
        return false;
      }
    }
    else if ( !survey.equals( other.survey ) )
    {
      return false;
    }
    return true;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

}
