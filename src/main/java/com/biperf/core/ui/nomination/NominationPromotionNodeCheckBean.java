
package com.biperf.core.ui.nomination;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;
import com.biperf.core.domain.calculator.CalculatorPayout;
import com.biperf.core.domain.enums.CalculatorAwardType;
import com.biperf.core.value.NominationBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class NominationPromotionNodeCheckBean
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private NominationCalculator nominationsCalculator;

  public void setCalculatorData( Calculator calculator )
  {
    if ( calculator != null )
    {
      nominationsCalculator = new NominationCalculator( calculator );
    }
  }

  public NominationCalculator getNominationsCalculator()
  {
    return nominationsCalculator;
  }

  public void setNominationsCalculator( NominationCalculator nominationsCalculator )
  {
    this.nominationsCalculator = nominationsCalculator;
  }

  public void setAwardLevelDataForPayTable( List<NominationBean.AwardLevelBean> awardBeans )
  {
    if ( nominationsCalculator != null )
    {
      nominationsCalculator.setAwardLevelDataForPayTable( awardBeans );
    }
  }

  public static class NominationCalculator
  {
    private CalculatorAttributes attributes;
    private List<Criteria> criteria;
    private PayTable payTable;

    public NominationCalculator( Calculator calculator )
    {
      attributes = new CalculatorAttributes( calculator );

      if ( calculator.getCalculatorCriterion() != null && !calculator.getCalculatorCriterion().isEmpty() )
      {
        criteria = new ArrayList<Criteria>( calculator.getCalculatorCriterion().size() );
        for ( CalculatorCriterion cc : calculator.getCalculatorCriterion() )
        {
          criteria.add( new Criteria( cc ) );
        }
      }

      if ( calculator.getCalculatorPayouts() != null && !calculator.getCalculatorPayouts().isEmpty() )
      {
        payTable = new PayTable( calculator.getCalculatorAwardType(), calculator.getCalculatorPayouts() );
      }
    }

    public CalculatorAttributes getAttributes()
    {
      return attributes;
    }

    public List<Criteria> getCriteria()
    {
      return criteria;
    }

    public PayTable getPayTable()
    {
      return payTable;
    }

    void setAwardLevelDataForPayTable( List<NominationBean.AwardLevelBean> awardBeans )
    {
      if ( payTable != null )
      {
        payTable.setAwardLevelDataForPayTable( awardBeans );
      }
    }
  }

  public static class CalculatorAttributes
  {
    private boolean hasWeight;
    private boolean hasScore;
    private boolean showPayTable;
    private String weightLabel;
    private String scoreLabel;
    private String awardType;

    public CalculatorAttributes( Calculator calculator )
    {
      hasWeight = calculator.isDisplayWeights();
      hasScore = calculator.isDisplayScores();
      showPayTable = calculator.isDisplayScores();
      weightLabel = CmsResourceBundle.getCmsBundle().getString( calculator.getWeightCMAssetName(), Calculator.CM_CALC_WEIGHT_NAME_KEY );
      scoreLabel = CmsResourceBundle.getCmsBundle().getString( calculator.getScoreCMAssetName(), Calculator.CM_CALC_SCORE_NAME_KEY );
      awardType = calculator.getCalculatorAwardType().getCode();
    }

    public String getAwardType()
    {
      return awardType;
    }

    public boolean isHasScore()
    {
      return hasScore;
    }

    public boolean isHasWeight()
    {
      return hasWeight;
    }

    public String getScoreLabel()
    {
      return scoreLabel;
    }

    public boolean isShowPayTable()
    {
      return showPayTable;
    }

    public String getWeightLabel()
    {
      return weightLabel;
    }
  }

  public static class Criteria
  {
    private Long id;
    private String label;
    private List<CriteriaRating> ratings;

    public Criteria( CalculatorCriterion cc )
    {
      id = cc.getId();
      label = cc.getCriterionText();

      if ( cc.getCriterionRatings() != null && !cc.getCriterionRatings().isEmpty() )
      {
        ratings = new ArrayList<CriteriaRating>( cc.getCriterionRatings().size() );
        for ( CalculatorCriterionRating ccr : cc.getCriterionRatings() )
        {
          ratings.add( new CriteriaRating( ccr ) );
        }
      }
    }

    public Long getId()
    {
      return id;
    }

    public String getLabel()
    {
      return label;
    }

    public List<CriteriaRating> getRatings()
    {
      return ratings;
    }
  }

  public static class CriteriaRating
  {
    private Long id;
    private String label;
    private int value;

    public CriteriaRating( CalculatorCriterionRating ccr )
    {
      this.id = ccr.getId();
      this.label = ccr.getRatingText();
      this.value = ccr.getRatingValue();
    }

    public Long getId()
    {
      return id;
    }

    public String getLabel()
    {
      return label;
    }

    public int getValue()
    {
      return value;
    }

  }

  public static class PayTable
  {
    private List<PayTableRow> rows = new ArrayList<PayTableRow>();

    public PayTable( CalculatorAwardType awardType, Set<CalculatorPayout> payouts )
    {
      if ( payouts != null && !payouts.isEmpty() )
      {
        rows = new ArrayList<PayTableRow>( payouts.size() );
        for ( CalculatorPayout cp : payouts )
        {
          rows.add( new PayTableRow( awardType, cp ) );
        }
      }
    }

    public List<PayTableRow> getRows()
    {
      return rows;
    }

    void setAwardLevelDataForPayTable( List<NominationBean.AwardLevelBean> awardBeans )
    {
      if ( rows != null && awardBeans != null && rows.size() == awardBeans.size() )
      {
        for ( int i = 0; i < rows.size(); i++ )
        {
          PayTableRow row = rows.get( i );
          row.updatePayoutFrom( awardBeans.get( i ) );
        }
      }
    }
  }

  public static class PayTableRow
  {
    private String score;
    private String payout;

    public PayTableRow( CalculatorAwardType awardType, CalculatorPayout cp )
    {
      this.score = cp.getLowScore() + "-" + cp.getHighScore();

      if ( awardType.isMerchLevelAward() || awardType.isFixedAward() )
      {
        this.payout = String.valueOf( cp.getLowAward() );
      }
      else if ( awardType.isRangeAward() )
      {
        this.payout = cp.getLowAward() + "-" + cp.getHighAward();
      }
    }

    public String getPayout()
    {
      return payout;
    }

    public String getScore()
    {
      return score;
    }

    void updatePayoutFrom( NominationBean.AwardLevelBean awardLevelBean )
    {
      payout = awardLevelBean.getName();
    }
  }

  public void addErrorMessage( WebErrorMessage message )
  {
    if ( messages == null )
    {
      messages = new ArrayList<WebErrorMessage>();
    }

    messages.add( message );
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }
}
