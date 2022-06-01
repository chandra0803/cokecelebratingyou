/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.ots.v1.reedeem;

import java.util.List;

import com.biperf.core.domain.ots.OTSBillCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author rajadura
 * @since Dec 14, 2017
 * 
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class CardInfo
{

  private String programNumber;
  private String batchNumber;
  private List<OTSBillCode> billCodes;
  private String cardReference;

  public String getProgramNumber()
  {
    return programNumber;
  }

  public String getCardReference()
  {
    return cardReference;
  }

  public void setCardReference( String cardReference )
  {
    this.cardReference = cardReference;
  }

  public void setProgramNumber( String programNumber )
  {
    this.programNumber = programNumber;
  }

  public String getBatchNumber()
  {
    return batchNumber;
  }

  public void setBatchNumber( String batchNumber )
  {
    this.batchNumber = batchNumber;
  }

  public List<OTSBillCode> getBillCodes()
  {
    return billCodes;
  }

  public void setBillCodes( List<OTSBillCode> billCodes )
  {
    this.billCodes = billCodes;
  }
}
