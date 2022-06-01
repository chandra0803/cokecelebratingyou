
package com.biperf.core.domain.participant;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AwardBanqResponseView
{
  private int errCode = 1;
  private String accountNo;
  private String balanceAvailable;
  private String depositAmount;
  private String depositDate;
  private String programNum;
  private String redemptionAmount;
  private String status;

  public int getErrCode()
  {
    return errCode;
  }

  public void setErrCode( int errCode )
  {
    this.errCode = errCode;
  }

  public String getAccountNo()
  {
    return accountNo;
  }

  public void setAccountNo( String accountNo )
  {
    this.accountNo = accountNo;
  }

  public String getBalanceAvailable()
  {
    return balanceAvailable;
  }

  public void setBalanceAvailable( String balanceAvailable )
  {
    this.balanceAvailable = balanceAvailable;
  }

  public String getDepositAmount()
  {
    return depositAmount;
  }

  public void setDepositAmount( String depositAmount )
  {
    this.depositAmount = depositAmount;
  }

  public String getDepositDate()
  {
    return depositDate;
  }

  public void setDepositDate( String depositDate )
  {
    this.depositDate = depositDate;
  }

  public String getProgramNum()
  {
    return programNum;
  }

  public void setProgramNum( String programNum )
  {
    this.programNum = programNum;
  }

  public String getRedemptionAmount()
  {
    return redemptionAmount;
  }

  public void setRedemptionAmount( String redemptionAmount )
  {
    this.redemptionAmount = redemptionAmount;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }
}
