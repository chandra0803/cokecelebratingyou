
package com.biperf.core.domain.fileload;

import java.util.Date;

public class AutoVinImportRecord extends ImportRecord
{
  /**
   * The Login ID of the user (participant) whose base objective to be modified by the Pax Base File Load.
   */
  private String loginId;
  /**
   * The ID of a participant whose base objective to be modified by the Pax Base File Load.
   */
  private Long userId;
  /**
   * The first name of the user (participant) whose base objective to be modified by the Pax Base File Load.
   */
  private String firstName;
  /**
   * The last name of the user (participant) whose base objective to be modified by the Pax Base File Load.
   */
  private String lastName;
  /**
   * The email address of the user (participant) whose base objective to be modified by the Pax Base File Load.
   */
  private String emailAddress;
  /**
   * The VIN# represents the vehicle identification number of a car sold by the participant.
   */
  private String vin;
  /**
   * The model represents the vehicle model of a car sold by the participant.
   */
  private String model;
  /**
   * Was this a sale or a return? "S" or "R"
   */
  private String transactionType;
  /**
   * Date the transaction was completed
   */
  private Date salesDate;
  /**
   * Date the vehicle was picked up/delivered
   */
  private Date deliveryDate;
  /**
   * The dealerCode represents where (dealership) the car was sold.
   */
  private String dealerCode;
  /**
   * The name of the dealer where the car was sold.
   */
  private String dealerName;

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
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

  public String getLoginId()
  {
    return loginId;
  }

  public void setLoginId( String loginId )
  {
    this.loginId = loginId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  /**
   * Ensure equality between this and the object param. Overridden from
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

    if ( ! ( object instanceof GoalQuestProgressImportRecord ) )
    {
      return false;
    }

    GoalQuestProgressImportRecord goalQuestProgressImportRecord = (GoalQuestProgressImportRecord)object;

    if ( this.getId() != null && this.getId().equals( goalQuestProgressImportRecord.getId() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
  }

  /**
   * Constructs a <code>String</code> with all attributes
   * in name = value format.
   *
   * @return a <code>String</code> representation 
   * of this object.
   */
  public String toString()
  {
    final String TAB = "    ";

    String retValue = "";

    retValue = "GoalQuestProgressImportRecord ( " + super.toString() + TAB + "loginId = " + this.loginId + TAB + "userId = " + this.userId + TAB + "firstName = " + this.firstName + TAB + "lastName = "
        + this.lastName + TAB + "emailAddress = " + this.emailAddress + TAB + "vin = " + this.vin + TAB + "model = " + this.model + TAB + "transactionType = " + this.transactionType + TAB
        + "salesDate = " + this.salesDate + TAB + "deliveryDate = " + this.deliveryDate + TAB + "dealerCode= " + this.dealerCode + TAB + "dealerName = " + this.dealerName + TAB + " )";

    return retValue;
  }

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

  public String getTransactionType()
  {
    return transactionType;
  }

  public void setTransactionType( String transactionType )
  {
    this.transactionType = transactionType;
  }

  public String getVin()
  {
    return vin;
  }

  public void setVin( String vin )
  {
    this.vin = vin;
  }

}
