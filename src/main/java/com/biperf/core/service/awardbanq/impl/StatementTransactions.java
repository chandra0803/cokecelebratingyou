
package com.biperf.core.service.awardbanq.impl;

import java.util.Date;

import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NumberFormatUtil;

public class StatementTransactions
{
  private String cardNo;
  private String description;
  private String orderNo;
  private int pointQuantity;
  private String transactionDate;
  private String transactionType;

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getOrderNo()
  {
    return orderNo;
  }

  public void setOrderNo( String orderNo )
  {
    this.orderNo = orderNo;
  }

  public int getPointQuantity()
  {
    return pointQuantity;
  }

  public void setPointQuantity( int pointQuantity )
  {
    this.pointQuantity = pointQuantity;
  }

  public String getTransactionDate()
  {
    return transactionDate;
  }

  public void setTransactionDate( String transactionDate )
  {
    this.transactionDate = transactionDate;
  }

  public String getTransactionType()
  {
    return transactionType;
  }

  public void setTransactionType( String transactionType )
  {
    this.transactionType = transactionType;
  }

  public String getFormattedTransactionDate()
  {
    return DateUtils.toDateFormat( transactionDate );
  }

  public String getFormattedTransactionAmount()
  {
    return NumberFormatUtil.getLocaleBasedNumberFormat( pointQuantity );
  }

  public Date getTransactionDateAsDate()
  {
    return DateUtils.toDate( getFormattedTransactionDate() );
  }

  public String getCardNo()
  {
    return cardNo;
  }

  public void setCardNo( String cardNo )
  {
    this.cardNo = cardNo;
  }

}
