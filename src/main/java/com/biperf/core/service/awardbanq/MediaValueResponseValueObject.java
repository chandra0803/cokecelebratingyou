
package com.biperf.core.service.awardbanq;

public class MediaValueResponseValueObject
{
  private String bpomCurrency;
  private Double bpomPrgBdgtvalue;
  private Double bpomPrgCashvalue;
  private String bpomPrgNbr;
  private String type;
  private int errCode;
  private String errDescription;

  public MediaValueResponseValueObject()
  {

  }

  public String getBpomCurrency()
  {
    return bpomCurrency;
  }

  public void setBpomCurrency( String bpomCurrency )
  {
    this.bpomCurrency = bpomCurrency;
  }

  public Double getBpomPrgBdgtvalue()
  {
    return bpomPrgBdgtvalue;
  }

  public void setBpomPrgBdgtvalue( Double bpomPrgBdgtvalue )
  {
    this.bpomPrgBdgtvalue = bpomPrgBdgtvalue;
  }

  public Double getBpomPrgCashvalue()
  {
    return bpomPrgCashvalue;
  }

  public void setBpomPrgCashvalue( Double bpomPrgCashvalue )
  {
    this.bpomPrgCashvalue = bpomPrgCashvalue;
  }

  public String getBpomPrgNbr()
  {
    return bpomPrgNbr;
  }

  public void setBpomPrgNbr( String bpomPrgNbr )
  {
    this.bpomPrgNbr = bpomPrgNbr;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public int getErrCode()
  {
    return errCode;
  }

  public void setErrCode( int errCode )
  {
    this.errCode = errCode;
  }

  public String getErrDescription()
  {
    return errDescription;
  }

  public void setErrDescription( String errDescription )
  {
    this.errDescription = errDescription;
  }

}
