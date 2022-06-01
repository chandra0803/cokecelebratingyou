
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class SSIContestApprovalObjectiveDetailView
{

  private String payoutType;
  private boolean includeBonus;
  private List<PaxView> results;
  private String objectiveAmountTotal;
  private String payoutTotal;
  private String valueTotal;
  private String bonusPayoutTotal;
  private String bonusPayoutCapTotal;
  private String sortedOn;
  private String sortedBy;
  private long total;
  private int paxPerPage;
  private int currentPage;

  public SSIContestApprovalObjectiveDetailView()
  {

  }

  public SSIContestApprovalObjectiveDetailView( int paxPerPage,
                                                int currentPage,
                                                Integer paxCount,
                                                List<SSIContestParticipant> participants,
                                                SSIContestPayoutObjectivesTotalsValueBean contestPayoutTotalsValueBean )
  {
    results = new ArrayList<PaxView>();
    SSIContest ssiContest = participants.get( 0 ).getContest();
    int precision = SSIContestUtil.getPrecision( ssiContest.getActivityMeasureType().getCode() );

    for ( SSIContestParticipant participant : participants )
    {
      results.add( new PaxView( participant, ssiContest, precision ) );
    }
    this.setPayoutType( ssiContest.getPayoutType().getCode() );
    this.setIncludeBonus( ssiContest.isIncludeBonus() );
    this.setObjectiveAmountTotal( SSIContestUtil.getFormattedValue( contestPayoutTotalsValueBean.getObjectiveAmountTotal(), precision ) );
    this.setPayoutTotal( SSIContestUtil.getFormattedValue( contestPayoutTotalsValueBean.getObjectivePayoutTotal(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
    this.setBonusPayoutTotal( SSIContestUtil.getFormattedValue( contestPayoutTotalsValueBean.getBonusPayoutTotal(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
    this.setBonusPayoutCapTotal( SSIContestUtil.getFormattedValue( contestPayoutTotalsValueBean.getBonusPayoutCapTotal(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
    this.setTotal( paxCount );
    this.setPaxPerPage( paxPerPage );
    this.setCurrentPage( currentPage );
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public boolean isIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( boolean includeBonus )
  {
    this.includeBonus = includeBonus;
  }

  public List<PaxView> getResults()
  {
    return results;
  }

  public void setResults( List<PaxView> results )
  {
    this.results = results;
  }

  public String getObjectiveAmountTotal()
  {
    return objectiveAmountTotal;
  }

  public void setObjectiveAmountTotal( String objectiveAmountTotal )
  {
    this.objectiveAmountTotal = objectiveAmountTotal;
  }

  public String getPayoutTotal()
  {
    return payoutTotal;
  }

  public void setPayoutTotal( String payoutTotal )
  {
    this.payoutTotal = payoutTotal;
  }

  public String getValueTotal()
  {
    return valueTotal;
  }

  public void setValueTotal( String valueTotal )
  {
    this.valueTotal = valueTotal;
  }

  public String getBonusPayoutTotal()
  {
    return bonusPayoutTotal;
  }

  public void setBonusPayoutTotal( String bonusPayoutTotal )
  {
    this.bonusPayoutTotal = bonusPayoutTotal;
  }

  public String getBonusPayoutCapTotal()
  {
    return bonusPayoutCapTotal;
  }

  public void setBonusPayoutCapTotal( String bonusPayoutCapTotal )
  {
    this.bonusPayoutCapTotal = bonusPayoutCapTotal;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public long getTotal()
  {
    return total;
  }

  public void setTotal( long total )
  {
    this.total = total;
  }

  public int getPaxPerPage()
  {
    return paxPerPage;
  }

  public void setPaxPerPage( int paxPerPage )
  {
    this.paxPerPage = paxPerPage;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  class PaxView
  {
    private Long id;
    private String firstName;
    private String lastName;
    private Objective objective;
    private Bonus bonus;

    public PaxView()
    {

    }

    public PaxView( SSIContestParticipant participant, SSIContest ssiContest, int precision )
    {
      this.setId( participant.getParticipant().getId() );
      this.setFirstName( participant.getParticipant().getFirstName() );
      this.setLastName( participant.getParticipant().getLastName() );
      this.setObjective( new Objective( participant, ssiContest, precision ) );
      this.setBonus( new Bonus( participant, precision ) );
    }

    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public String getFirstName()
    {
      return firstName;
    }

    public void setFirstName( String firstName )
    {
      this.firstName = firstName;
    }

    public String getLastName()
    {
      return lastName;
    }

    public void setLastName( String lastName )
    {
      this.lastName = lastName;
    }

    public Objective getObjective()
    {
      return objective;
    }

    public void setObjective( Objective objective )
    {
      this.objective = objective;
    }

    public Bonus getBonus()
    {
      return bonus;
    }

    public void setBonus( Bonus bonus )
    {
      this.bonus = bonus;
    }

    class Objective
    {
      private String name;
      private String amount;
      private String payoutAmount;
      private String payoutDescription;
      private String value;

      public Objective()
      {

      }

      public Objective( SSIContestParticipant participant, SSIContest ssiContest, int precision )
      {
        this.setName( ssiContest.getSameObjectiveDescription() ? ssiContest.getActivityDescription() : participant.getActivityDescription() );
        this.setAmount( SSIContestUtil.getFormattedValue( participant.getObjectiveAmount(), precision ) );
        this.setPayoutAmount( SSIContestUtil.getFormattedValue( participant.getObjectivePayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        this.setPayoutDescription( participant.getObjectivePayoutDescription() );
        this.setValue( SSIContestUtil.getFormattedValue( participant.getObjectiveAmount(), precision ) );
      }

      public String getName()
      {
        return name;
      }

      public void setName( String name )
      {
        this.name = name;
      }

      public String getAmount()
      {
        return amount;
      }

      public void setAmount( String amount )
      {
        this.amount = amount;
      }

      public String getPayoutAmount()
      {
        return payoutAmount;
      }

      public void setPayoutAmount( String payoutAmount )
      {
        this.payoutAmount = payoutAmount;
      }

      public String getPayoutDescription()
      {
        return payoutDescription;
      }

      public void setPayoutDescription( String payoutDescription )
      {
        this.payoutDescription = payoutDescription;
      }

      public String getValue()
      {
        return value;
      }

      public void setValue( String value )
      {
        this.value = value;
      }

    }

    class Bonus
    {
      private String forEvery;
      private String payoutAmount;
      private String payoutCap;

      public Bonus()
      {

      }

      public Bonus( SSIContestParticipant participant, int precision )
      {
        this.setForEvery( SSIContestUtil.getFormattedValue( participant.getObjectiveBonusIncrement(), precision ) );
        this.setPayoutAmount( SSIContestUtil.getFormattedValue( participant.getObjectiveBonusPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        this.setPayoutCap( SSIContestUtil.getFormattedValue( participant.getObjectiveBonusCap(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
      }

      public String getForEvery()
      {
        return forEvery;
      }

      public void setForEvery( String forEvery )
      {
        this.forEvery = forEvery;
      }

      public String getPayoutAmount()
      {
        return payoutAmount;
      }

      public void setPayoutAmount( String payoutAmount )
      {
        this.payoutAmount = payoutAmount;
      }

      public String getPayoutCap()
      {
        return payoutCap;
      }

      public void setPayoutCap( String payoutCap )
      {
        this.payoutCap = payoutCap;
      }

    }
  }

}
