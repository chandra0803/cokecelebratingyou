/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.goalquest;

import java.math.BigDecimal;
import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.enums.GoalQuestPaxActivityStatus;
import com.biperf.core.domain.enums.GoalQuestPaxActivityType;
import com.biperf.core.domain.enums.ProgressTransactionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;

/*
 * GoalQuestParticipantActivity <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>meadows</td> <td>Jan 1, 2007</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class GoalQuestParticipantActivity extends BaseDomain
{
  private GoalQuestPaxActivityType type;
  private GoalQuestPaxActivityStatus status;
  private GoalQuestPromotion goalQuestPromotion;
  private Participant participant;
  private BigDecimal quantity;
  private Date submissionDate;
  private Activity payoutActivity;

  // Automotive only
  private boolean automotive;
  private String vin;
  private String model;
  private ProgressTransactionType transactionType;
  private Date salesDate;
  private Date deliveryDate;
  private String dealerCode;
  private String dealerName;

  public String getDealerCode()
  {
    return dealerCode;
  }

  public void setDealerCode( String dealerCode )
  {
    this.dealerCode = dealerCode;
  }

  public String getDealerName()
  {
    return dealerName;
  }

  public void setDealerName( String dealerName )
  {
    this.dealerName = dealerName;
  }

  public Date getDeliveryDate()
  {
    return deliveryDate;
  }

  public void setDeliveryDate( Date deliveryDate )
  {
    this.deliveryDate = deliveryDate;
  }

  public String getModel()
  {
    return model;
  }

  public void setModel( String model )
  {
    this.model = model;
  }

  public Date getSalesDate()
  {
    return salesDate;
  }

  public void setSalesDate( Date salesDate )
  {
    this.salesDate = salesDate;
  }

  public ProgressTransactionType getTransactionType()
  {
    return transactionType;
  }

  public void setTransactionType( ProgressTransactionType transactionType )
  {
    this.transactionType = transactionType;
  }

  public Activity getPayoutActivity()
  {
    return payoutActivity;
  }

  public void setPayoutActivity( Activity payoutActivity )
  {
    this.payoutActivity = payoutActivity;
  }

  public GoalQuestPromotion getGoalQuestPromotion()
  {
    return goalQuestPromotion;
  }

  public void setGoalQuestPromotion( GoalQuestPromotion goalQuestPromotion )
  {
    this.goalQuestPromotion = goalQuestPromotion;
  }

  public boolean isAutomotive()
  {
    return automotive;
  }

  public void setAutomotive( boolean automotive )
  {
    this.automotive = automotive;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public BigDecimal getQuantity()
  {
    return quantity;
  }

  public void setQuantity( BigDecimal quantity )
  {
    this.quantity = quantity;
  }

  public GoalQuestPaxActivityStatus getStatus()
  {
    return status;
  }

  public void setStatus( GoalQuestPaxActivityStatus status )
  {
    this.status = status;
  }

  public Date getSubmissionDate()
  {
    return submissionDate;
  }

  public void setSubmissionDate( Date submissionDate )
  {
    this.submissionDate = submissionDate;
  }

  public GoalQuestPaxActivityType getType()
  {
    return type;
  }

  public void setType( GoalQuestPaxActivityType type )
  {
    this.type = type;
  }

  public String getVin()
  {
    return vin;
  }

  public void setVin( String vin )
  {
    this.vin = vin;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof GoalQuestParticipantActivity ) )
    {
      return false;
    }

    final GoalQuestParticipantActivity goalQuestParticipantActivity = (GoalQuestParticipantActivity)o;

    if ( !getId().equals( goalQuestParticipantActivity.getId() ) )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return getId() == null ? 0 : getId().hashCode();
  }

}
