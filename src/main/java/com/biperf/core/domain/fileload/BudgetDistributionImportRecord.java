
package com.biperf.core.domain.fileload;

import java.math.BigDecimal;

import com.biperf.core.domain.enums.BudgetType;

/**
 * 
 * BudgetDistributionImportRecord.
 * 
 * @author chowdhur
 * @since Oct 21, 2015
 */
public class BudgetDistributionImportRecord extends ImportRecord
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private Long selectedBudgetMasterId;
  private Long selectedBudgetTimePeriodId;

  private String srcBudgetMasterName;
  private String srcBudgetTimePeriodName;
  private Long orgUnitId;
  private String srcBudgetMasterDisplayName;

  private String srcBudgetOwnerLoginId;
  private String srcBudgetOwnerName;
  private Long srcBudgetMasterId;
  private Long srcBudgetTimePeriodId;
  private Long srcBudgetId;
  private BigDecimal srcBudgetAmount;
  private BudgetType budgetType;
  private String transferToOwner1;
  private BigDecimal amountOwner1;
  private String transferToOwner2;
  private BigDecimal amountOwner2;
  private String transferToOwner3;
  private BigDecimal amountOwner3;

  public Long getSelectedBudgetMasterId()
  {
    return selectedBudgetMasterId;
  }

  public void setSelectedBudgetMasterId( Long selectedBudgetMasterId )
  {
    this.selectedBudgetMasterId = selectedBudgetMasterId;
  }

  public Long getSelectedBudgetTimePeriodId()
  {
    return selectedBudgetTimePeriodId;
  }

  public void setSelectedBudgetTimePeriodId( Long selectedBudgetTimePeriodId )
  {
    this.selectedBudgetTimePeriodId = selectedBudgetTimePeriodId;
  }

  public String getSrcBudgetMasterName()
  {
    return srcBudgetMasterName;
  }

  public void setSrcBudgetMasterName( String srcBudgetMasterName )
  {
    this.srcBudgetMasterName = srcBudgetMasterName;
  }

  public String getSrcBudgetTimePeriodName()
  {
    return srcBudgetTimePeriodName;
  }

  public void setSrcBudgetTimePeriodName( String srcBudgetTimePeriodName )
  {
    this.srcBudgetTimePeriodName = srcBudgetTimePeriodName;
  }

  public String getSrcBudgetMasterDisplayName()
  {
    return srcBudgetMasterDisplayName;
  }

  public void setSrcBudgetMasterDisplayName( String srcBudgetMasterDisplayName )
  {
    this.srcBudgetMasterDisplayName = srcBudgetMasterDisplayName;
  }

  public String getSrcBudgetOwnerLoginId()
  {
    return srcBudgetOwnerLoginId;
  }

  public void setSrcBudgetOwnerLoginId( String srcBudgetOwnerLoginId )
  {
    this.srcBudgetOwnerLoginId = srcBudgetOwnerLoginId;
  }

  public String getSrcBudgetOwnerName()
  {
    return srcBudgetOwnerName;
  }

  public void setSrcBudgetOwnerName( String srcBudgetOwnerName )
  {
    this.srcBudgetOwnerName = srcBudgetOwnerName;
  }

  public Long getSrcBudgetMasterId()
  {
    return srcBudgetMasterId;
  }

  public void setSrcBudgetMasterId( Long srcBudgetMasterId )
  {
    this.srcBudgetMasterId = srcBudgetMasterId;
  }

  public Long getSrcBudgetTimePeriodId()
  {
    return srcBudgetTimePeriodId;
  }

  public void setSrcBudgetTimePeriodId( Long srcBudgetTimePeriodId )
  {
    this.srcBudgetTimePeriodId = srcBudgetTimePeriodId;
  }

  public Long getSrcBudgetId()
  {
    return srcBudgetId;
  }

  public void setSrcBudgetId( Long srcBudgetId )
  {
    this.srcBudgetId = srcBudgetId;
  }

  public BigDecimal getSrcBudgetAmount()
  {
    return srcBudgetAmount;
  }

  public void setSrcBudgetAmount( BigDecimal srcBudgetAmount )
  {
    this.srcBudgetAmount = srcBudgetAmount;
  }

  public BudgetType getBudgetType()
  {
    return budgetType;
  }

  public void setBudgetType( BudgetType budgetType )
  {
    this.budgetType = budgetType;
  }

  public String getTransferToOwner1()
  {
    return transferToOwner1;
  }

  public void setTransferToOwner1( String transferToOwner1 )
  {
    this.transferToOwner1 = transferToOwner1;
  }

  public BigDecimal getAmountOwner1()
  {
    return amountOwner1;
  }

  public void setAmountOwner1( BigDecimal amountOwner1 )
  {
    this.amountOwner1 = amountOwner1;
  }

  public String getTransferToOwner2()
  {
    return transferToOwner2;
  }

  public void setTransferToOwner2( String transferToOwner2 )
  {
    this.transferToOwner2 = transferToOwner2;
  }

  public BigDecimal getAmountOwner2()
  {
    return amountOwner2;
  }

  public void setAmountOwner2( BigDecimal amountOwner2 )
  {
    this.amountOwner2 = amountOwner2;
  }

  public String getTransferToOwner3()
  {
    return transferToOwner3;
  }

  public void setTransferToOwner3( String transferToOwner3 )
  {
    this.transferToOwner3 = transferToOwner3;
  }

  public BigDecimal getAmountOwner3()
  {
    return amountOwner3;
  }

  public void setAmountOwner3( BigDecimal amountOwner3 )
  {
    this.amountOwner3 = amountOwner3;
  }

  public Long getOrgUnitId()
  {
    return orgUnitId;
  }

  public void setOrgUnitId( Long orgUnitId )
  {
    this.orgUnitId = orgUnitId;
  }

  /**
   * Returns true if the given object and this object are equal; returns false otherwise.
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object the object to compare to this object.
   * @return true if the given object and this object are equal; false otherwise.
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof BudgetDistributionImportRecord ) )
    {
      return false;
    }

    BudgetDistributionImportRecord budgetDistributionImportRecord = (BudgetDistributionImportRecord)object;

    if ( this.getId() != null && this.getId().equals( budgetDistributionImportRecord.getId() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Returns the hashcode for this object.
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return the hashcode for this object.
   */
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
  }

  @Override
  public String toString()
  {
    return "BudgetDistributionImportRecord [selectedBudgetMasterId=" + selectedBudgetMasterId + ", selectedBudgetTimePeriodId=" + selectedBudgetTimePeriodId + ", srcBudgetMasterName="
        + srcBudgetMasterName + ", srcBudgetTimePeriodName=" + srcBudgetTimePeriodName + ", srcBudgetMasterDisplayName=" + srcBudgetMasterDisplayName + ", srcBudgetOwnerLoginId="
        + srcBudgetOwnerLoginId + ", srcBudgetOwnerName=" + srcBudgetOwnerName + ", srcBudgetMasterId=" + srcBudgetMasterId + ", srcBudgetTimePeriodId=" + srcBudgetTimePeriodId + ", srcBudgetId="
        + srcBudgetId + ", srcBudgetAmount=" + srcBudgetAmount + ", budgetType=" + budgetType + ", transferToOwner1=" + transferToOwner1 + ", amountOwner1=" + amountOwner1 + ", transferToOwner2="
        + transferToOwner2 + ", amountOwner2=" + amountOwner2 + ", transferToOwner3=" + transferToOwner3 + ", amountOwner3=" + amountOwner3 + "]";
  }

  /**
   * Returns a string representation of this object.
   * 
   * @see java.lang.Object#toString()
   * @return a string representation of this object.
   */

}
