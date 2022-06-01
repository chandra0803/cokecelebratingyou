
package com.biperf.core.ui.recognition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;
import com.biperf.core.domain.calculator.CalculatorPayout;
import com.biperf.core.domain.enums.CalculatorAwardType;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.RecognitionBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PromotionNodeCheckBean
{
  private Node node;
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private boolean nodeClaimableForPromotionAndParticipant;
  private RecognitionCalculator recognitionCalculator;

  public void setData( com.biperf.core.domain.hierarchy.Node nodeById, Budget budget )
  {
    node = new Node( nodeById, budget );
  }

  public void setCalculatorData( Calculator calculator )
  {
    if ( calculator != null )
    {
      recognitionCalculator = new RecognitionCalculator( calculator );
    }
  }

  public RecognitionCalculator getRecognitionCalculator()
  {
    return recognitionCalculator;
  }

  public void setAwardLevelDataForPayTable( List<RecognitionBean.AwardLevelBean> awardBeans )
  {
    if ( recognitionCalculator != null )
    {
      recognitionCalculator.setAwardLevelDataForPayTable( awardBeans );
    }
  }

  public static class Node extends NameableBean
  {
    private Long budgetId;
    private int amount;
    private int amountUsed;
    private boolean hardCap;
    // Client customization wip# 25589 start
    private boolean utilizeParentBudget;
    private String budgetOwner;
    // Client customization wip# 25589 end


    public Node( com.biperf.core.domain.hierarchy.Node nodeById, Budget budget )
    {
      setId( nodeById.getId() );

      if ( budget != null )
      {
        // if ( budget.getNode() != null )
        // {
        // setName( budget.getNode().getName() );
        // }
        // else if ( budget.getUser() != null )
        // {
        // setName( budget.getUser().getNameLFMNoComma() );
        // }
        setName( nodeById.getName() );

        budgetId = budget.getId();
        amount = (int)Math.floor( budget.getCurrentValue().doubleValue() );
        if ( budget.getOriginalValue() != null && budget.getCurrentValue() != null )
        {
          amountUsed = BudgetUtils.getBudgetDisplayValue( budget.getOriginalValue().subtract( budget.getCurrentValue() ) );
        }

        hardCap = true;
        
        // Client customization wip# 25589 start
        utilizeParentBudget = budget.isParentBudgetUsed();
        if ( utilizeParentBudget && budget.getNode() != null )
        {
          budgetOwner = budget.getNode().getName();
        }
        // Client customization wip# 25589 end

      }
    }

    public void reset()
    {
      budgetId = null;
      amount = 0;
      amountUsed = 0;
      hardCap = true;
      // Client customization wip# 25589 start
      utilizeParentBudget = false;
      budgetOwner = null;
      // Client customization wip# 25589 end

    }
    
    public boolean isUtilizeParentBudget()
    {
      return utilizeParentBudget;
    }

    public void setUtilizeParentBudget( boolean utilizeParentBudget )
    {
      this.utilizeParentBudget = utilizeParentBudget;
    }

    public String getBudgetOwner()
    {
      return budgetOwner;
    }

    public void setBudgetOwner( String budgetOwner )
    {
      this.budgetOwner = budgetOwner;
    }

    public Long getBudgetId()
    {
      return budgetId;
    }

    public void setBudgetId( Long budgetId )
    {
      this.budgetId = budgetId;
    }

    public int getAmount()
    {
      return amount;
    }

    public void setAmount( int amount )
    {
      this.amount = amount;
    }

    public int getAmountUsed()
    {
      return amountUsed;
    }

    public void setAmountUsed( int amountUsed )
    {
      this.amountUsed = amountUsed;
    }

    public boolean isHardCap()
    {
      return hardCap;
    }

    public void setHardCap( boolean hardCap )
    {
      this.hardCap = hardCap;
    }

  }

  public static class RecognitionCalculator
  {
    private CalculatorAttributes attributes;
    private List<Criteria> criteria;
    private PayTable payTable;

    public RecognitionCalculator( Calculator calculator )
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

    void setAwardLevelDataForPayTable( List<RecognitionBean.AwardLevelBean> awardBeans )
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

    void setAwardLevelDataForPayTable( List<RecognitionBean.AwardLevelBean> awardBeans )
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

    void updatePayoutFrom( RecognitionBean.AwardLevelBean awardLevelBean )
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

  public Node getNode()
  {
    // if there are any error messages, remove any budget information.
    if ( messages == null || !messages.isEmpty() )
    {
      if ( this.getNode() != null )
      {
        this.getNode().reset();
      }
    }

    return node;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public void setNodeClaimableForPromotionAndParticipant( boolean nodeClaimableForPromotionAndParticipant )
  {
    this.nodeClaimableForPromotionAndParticipant = nodeClaimableForPromotionAndParticipant;
  }

  public void setNode( Node node )
  {
    this.node = node;
  }
}
