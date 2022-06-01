
package com.biperf.core.ui.nomination.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.ui.BaseForm;

public class CalculatorInfoForm extends BaseForm
{
  private Long promotionId;
  private Long participantId;
  private Long levelId;

  private Map<Integer, CriteriaBean> criteriaBeans = new HashMap<Integer, CriteriaBean>();

  public CriteriaBean getCriteria( int index )
  {
    CriteriaBean bean = criteriaBeans.get( index );
    if ( bean == null )
    {
      bean = new CriteriaBean();
      criteriaBeans.put( index, bean );
    }
    return bean;
  }

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getLevelId()
  {
    return levelId;
  }

  public void setLevelId( Long levelId )
  {
    this.levelId = levelId;
  }

  public List<CriteriaBean> getCriteriaBeans()
  {
    return new ArrayList<CriteriaBean>( criteriaBeans.values() );
  }
}
