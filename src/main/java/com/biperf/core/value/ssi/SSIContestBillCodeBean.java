
package com.biperf.core.value.ssi;

import com.biperf.core.domain.enums.BillCodeSSIType;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SSIContestBillCodeBean
{

  private Long contestId;
  private Long sortOrder;
  private String billCode;
  private String trackBillCodeBy;
  private String customValue;
  private String cmAsseCode;
  private Long promoBillCodeOrder;

  public String getCmAsseCode()
  {
    return cmAsseCode;
  }

  public void setCmAsseCode( String cmAsseCode )
  {
    this.cmAsseCode = cmAsseCode;
  }

  private String cmAsseKey;

  public String getCmAsseKey()
  {
    return cmAsseKey;
  }

  public void setCmAsseKey( String cmAsseKey )
  {
    this.cmAsseKey = cmAsseKey;
  }

  public SSIContestBillCodeBean()
  {

  }

  public SSIContestBillCodeBean( Long contestId, Long sortOrder, String billCode, String trackBillCodeBy, String customValue, String cmAssetofBillCodeName )
  {
    this.contestId = contestId;
    this.sortOrder = sortOrder;
    this.trackBillCodeBy = trackBillCodeBy;
    this.billCode = billCode;
    this.customValue = customValue;
    this.cmAsseCode = cmAssetofBillCodeName;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public Long getSortOrder()
  {
    return sortOrder;
  }

  public void setSortOrder( Long sortOrder )
  {
    this.sortOrder = sortOrder;
  }

  public String getTrackBillCodeBy()
  {
    return trackBillCodeBy;
  }

  public void setTrackBillCodeBy( String trackBillCodeBy )
  {
    this.trackBillCodeBy = trackBillCodeBy;
  }

  public String getBillCode()
  {
    return billCode;
  }

  public void setBillCode( String billCode )
  {
    this.billCode = billCode;
  }

  public String getCustomValue()
  {
    return customValue;
  }

  public void setCustomValue( String customValue )
  {
    this.customValue = customValue;
  }

  public String getDisplayName()
  {
    BillCodeSSIType trackBy = BillCodeSSIType.lookup( getTrackBillCodeBy() );
    return trackBy.getName() + " - " + CmsResourceBundle.getCmsBundle().getString( getCmAsseCode() + "." + getCmAsseKey() );
  }

  public Long getPromoBillCodeOrder()
  {
    return promoBillCodeOrder;
  }

  public void setPromoBillCodeOrder( Long promoBillCodeOrder )
  {
    this.promoBillCodeOrder = promoBillCodeOrder;
  }
}
