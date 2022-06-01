/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/journal/Journal.java,v $
 */

package com.biperf.core.domain.journal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Journal represents any kind of earning
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
 * <td>OPI Admin</td>
 * <td>Jul 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * <tr>
 * <td>Brian Repko</td>
 * <td>Aug 14, 2006</td>
 * <td>1.1</td>
 * <td>added billing codes</td>
 * </tr>
 * </table>
 * </p>
 * 
 *
 */
public class Journal extends BaseDomain
{
  /**
   * for journal_type Discretionary
   */
  public static final String DISCRETIONARY = "Discretionary";
  public static final String AWARD = "Award";
  public static final String SWEEPSTAKES = "Sweeps";
  public static final String MANAGER_OVERRIDE = "Mgr Payout";
  public static final String PARTNER_AWARD = "PartnerAward";
  public static final String GOALQUEST_AWARD = "GoalQuest Award";
  public static final String CHALLENGEPOINT_BASIC_AWARD = "CP Basic Award";
  public static final String CHALLENGEPOINT_CPACHIEVEMENT_AWARD = "CP Achmt Award";
  public static final String CHALLENGEPOINT_FINAL_BASIC_AWARD = "CP Final Basic";
  public static final String STACK_RANK = "StackRank";
  public static final String THROWDOWN_HEAD2HEAD = "Head2Head";
  public static final String STACK_STANDING = "StackStanding";
  public static final String BADGE_POINTS = "Badge";

  // Using guid for equals and hash code because there isn't a known business key.
  private String guid;
  private Participant participant;
  private String accountNumber;
  private String accountNumberDecrypted;// code fix for 18477
  private Date transactionDate;
  private JournalTransactionType transactionType;
  private Long transactionAmount;
  private BigDecimal transactionCashAmount;
  private BigDecimal budgetValue;
  private String transactionDescription;
  private Set activityJournals = new HashSet();
  private String comments;
  private String journalType = AWARD;
  private JournalStatusType journalStatusType;
  private PromotionApprovalOptionReasonType reasonType;
  private Promotion promotion;
  private Budget budget;
  private Date processStartDate;
  private Date processEndDate;
  private PromotionAwardsType awardPayoutType;

  private boolean isReversal = false;

  // transient variable to separate file load deposit
  private boolean isFileLoadDeposit = false;
  private String userCurrency;

  // Client customizations for WIP #43735 starts
  private Long cashAwardQty;
  private String cashCurrency;
  private Date cashExtractedDate;
  // Client customization for WIP #43735 ends
  
  @JsonManagedReference
  private Set<JournalBillCode> billCodes = new HashSet<JournalBillCode>();

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  /**
   * Constructs a <code>Journal</code> object.
   */
  public Journal()
  {
    // empty default constructor
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public String getJournalType()
  {
    return journalType;
  }

  public void setJournalType( String journalType )
  {
    this.journalType = journalType;
  }

  public String getAccountNumber()
  {
    return accountNumberDecrypted;
  }

  public void setAccountNumber( String accountNumber )
  {
    this.accountNumber = accountNumber;
    this.accountNumberDecrypted = accountNumber;
  }

  public String getDisplayTransactionDate()
  {
    if ( this.transactionDate != null )
    {
      return DateUtils.toDisplayString( this.transactionDate );
    }
    else
    {
      return "";
    }
  }

  public Date getTransactionDate()
  {
    return transactionDate;
  }

  public void setTransactionDate( Date transactionDate )
  {
    this.transactionDate = transactionDate;
  }

  public JournalTransactionType getTransactionType()
  {
    return transactionType;
  }

  public void setTransactionType( JournalTransactionType transactionType )
  {
    this.transactionType = transactionType;
  }

  public String getDisplayTransactionAmount()
  {
    if ( this.transactionAmount != null )
    {
      return NumberFormatUtil.getLocaleBasedNumberFormat( this.transactionAmount );
    }
    else
    {
      return "";
    }
  }

  public Long getTransactionAmount()
  {
    return transactionAmount;
  }

  public void setTransactionAmount( Long transactionAmount )
  {
    this.transactionAmount = transactionAmount;
  }

  public BigDecimal getBudgetValue()
  {
    return budgetValue;
  }

  public void setBudgetValue( BigDecimal budgetValue )
  {
    this.budgetValue = budgetValue;
  }

  public String getTransactionDescription()
  {
    return transactionDescription;
  }

  public void setTransactionDescription( String transactionDescription )
  {
    this.transactionDescription = transactionDescription;
  }

  /**
   * Adds the given activities to this journal transaction.
   * 
   * @param activities the activities to add to this journal transaction.
   */
  public void addActivities( Set activities )
  {
    Iterator iter = activities.iterator();
    while ( iter.hasNext() )
    {
      Activity activity = (Activity)iter.next();
      this.addActivityJournal( new ActivityJournal( activity, this ) );
    }
  }

  /**
   * Adds an activity to this journal transaction.
   * 
   * @param activityJournal the activity to add to this journal transaction.
   */
  public void addActivityJournal( ActivityJournal activityJournal )
  {
    activityJournal.setJournal( this );
    this.activityJournals.add( activityJournal );
  }

  /**
   * Returns the activities associated with this journal transaction.
   * 
   * @return the activities associated with this journal transaction, as a <code>Set</code> of
   *         {@link ActivityJournal} objects.
   */
  public Set getActivityJournals()
  {
    return activityJournals;
  }

  /**
   * Sets the activities associated with this journal transaction.
   * 
   * @param activityJournals the activities associated with this journal transaction, as a
   *          <code>Set</code> of {@link ActivityJournal} objects.
   */
  public void setActivityJournals( Set activityJournals )
  {
    this.activityJournals = activityJournals;
  }

  /**
   * Returns the business identifier for this journal transaction.
   * 
   * @return the business identifier for this journal transaction.
   */
  public String getGuid()
  {
    return guid;
  }

  /**
   * Sets the business identifier for this journal transaction.
   * 
   * @param guid the business identifier for this journal transaction.
   */
  public void setGuid( String guid )
  {
    this.guid = guid;
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
    if ( ! ( o instanceof Journal ) )
    {
      return false;
    }

    final Journal journal = (Journal)o;

    if ( getGuid() != null && !getGuid().equals( journal.getGuid() ) )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return getGuid() == null ? 0 : getGuid().hashCode();
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public JournalStatusType getJournalStatusType()
  {
    return journalStatusType;
  }

  public void setJournalStatusType( JournalStatusType journalStatusType )
  {
    this.journalStatusType = journalStatusType;
  }

  public PromotionApprovalOptionReasonType getReasonType()
  {
    return reasonType;
  }

  public void setReasonType( PromotionApprovalOptionReasonType reasonType )
  {
    this.reasonType = reasonType;
  }

  /**
   * @return value of budget property
   */
  public Budget getBudget()
  {
    return budget;
  }

  /**
   * @param budget value for budget property
   */
  public void setBudget( Budget budget )
  {
    this.budget = budget;
  }

  /**
   * @return value of promotion property
   */
  public Promotion getPromotion()
  {
    return promotion;
  }

  /**
   * @param promotion value for promotion property
   */
  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public Journal deepCopy()
  {
    Journal clone = new Journal();
    clone.setParticipant( this.getParticipant() );
    clone.setAccountNumber( this.getAccountNumber() );
    clone.setTransactionDate( this.getTransactionDate() );
    clone.setTransactionType( this.getTransactionType() );
    clone.setTransactionAmount( this.getTransactionAmount() );
    clone.setTransactionCashAmount( this.getTransactionCashAmount() );
    clone.setAwardPayoutType( this.getAwardPayoutType() );
    clone.setTransactionDescription( this.getTransactionDescription() );
    clone.setActivityJournals( this.getActivityJournals() );
    clone.setComments( this.getComments() );
    clone.setJournalType( this.getJournalType() );
    clone.setJournalStatusType( this.getJournalStatusType() );
    clone.setReasonType( this.getReasonType() );
    clone.setBudget( this.getBudget() );
    clone.setReversal( this.isReversal() );
    clone.setBillCodes( this.getBillCodes() );
    return clone;
  }

  public Date getProcessEndDate()
  {
    return processEndDate;
  }

  public void setProcessEndDate( Date processEndDate )
  {
    this.processEndDate = processEndDate;
  }

  public Date getProcessStartDate()
  {
    return processStartDate;
  }

  public void setProcessStartDate( Date processStartDate )
  {
    this.processStartDate = processStartDate;
  }

  public boolean isReversal()
  {
    return isReversal;
  }

  public void setReversal( boolean isReversal )
  {
    this.isReversal = isReversal;
  }

  public boolean isFileLoadDeposit()
  {
    return isFileLoadDeposit;
  }

  public void setFileLoadDeposit( boolean isFileLoadDeposit )
  {
    this.isFileLoadDeposit = isFileLoadDeposit;
  }

  public String getUserCurrency()
  {
    return userCurrency;
  }

  public void setUserCurrency( String userCurrency )
  {
    this.userCurrency = userCurrency;
  }

  public Set<JournalBillCode> getBillCodes()
  {
    return billCodes;
  }

  public void setBillCodes( Set<JournalBillCode> billCodes )
  {
    this.billCodes = billCodes;
  }

  public PromotionAwardsType getAwardPayoutType()
  {
    return awardPayoutType;
  }

  public void setAwardPayoutType( PromotionAwardsType awardPayoutType )
  {
    this.awardPayoutType = awardPayoutType;
  }

  public BigDecimal getTransactionCashAmount()
  {
    return transactionCashAmount;
  }

  public void setTransactionCashAmount( BigDecimal transactionCashAmount )
  {
    this.transactionCashAmount = transactionCashAmount;
  }


  // Client customizations for WIP #43735 starts
  public Long getCashAwardQty()
  {
    return cashAwardQty;
  }

  public void setCashAwardQty( Long cashAwardQty )
  {
    this.cashAwardQty = cashAwardQty;
  }

  public String getCashCurrency()
  {
    return cashCurrency;
  }

  public void setCashCurrency( String cashCurrency )
  {
    this.cashCurrency = cashCurrency;
  }

  public Date getCashExtractedDate()
  {
    return cashExtractedDate;
  }

  public void setCashExtractedDate( Date cashExtractedDate )
  {
    this.cashExtractedDate = cashExtractedDate;
  }
  // Client customizations for WIP #43735 ends
}
