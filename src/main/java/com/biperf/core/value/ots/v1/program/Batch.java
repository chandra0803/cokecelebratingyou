
package com.biperf.core.value.ots.v1.program;

import java.util.List;

import com.biperf.core.domain.ots.OTSBillCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class Batch
{
  private String batchNumber;
  private List<BatchDescription> batchDescriptions;
  private List<OTSBillCode> billCodes;
  private boolean isBillCodesActive;

  public String getBatchNumber()
  {
    return batchNumber;
  }

  public void setBatchNumber( String batchNumber )
  {
    this.batchNumber = batchNumber;
  }

  public List<BatchDescription> getBatchDescription()
  {
    return batchDescriptions;
  }

  public void setBatchDescription( List<BatchDescription> batchDescriptions )
  {
    this.batchDescriptions = batchDescriptions;
  }

  public List<OTSBillCode> getOTSBillCodes()
  {
    return billCodes;
  }

  public void setOTSBillCodes( List<OTSBillCode> billCodes )
  {
    this.billCodes = billCodes;
  }

  public boolean isBillCodesActive()
  {
    return isBillCodesActive;
  }

  public boolean getIsBillCodesActive()
  {
    return isBillCodesActive;
  }

  public void setBillCodesActive( boolean isBillCodesActive )
  {
    this.isBillCodesActive = isBillCodesActive;
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
