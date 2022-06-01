
package com.biperf.core.ui.ssi.view;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.utils.BeanLocator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SSIContestBillCodeView
{

  private String billCode; // form of trackby_billcode
  private String billCodeName;
  private String index;
  private String customValue;
  private String promoBillCodeOrder;

  public SSIContestBillCodeView()
  {
  }

  public SSIContestBillCodeView( String billCode, String billCodeName, String index, String customValue )
  {
    this.billCode = billCode;
    this.billCodeName = billCodeName;
    this.index = index;
    this.customValue = customValue;
  }

  @JsonProperty( "customValue" )
  public String getCustomValue()
  {
    return customValue;
  }

  public void setCustomValue( String customValue )
  {
    this.customValue = customValue;
  }

  @JsonProperty( "billCode" )
  public String getBillCode()
  {
    return billCode;
  }

  public void setBillCode( String billCode )
  {
    this.billCode = billCode;
  }

  @JsonProperty( "billCodeName" )
  public String getBillCodeName()
  {
    return billCodeName;
  }

  public void setBillCodeName( String billCodeName )
  {
    this.billCodeName = billCodeName;
  }

  @JsonIgnore
  public String getIndex()
  {
    return index;
  }

  public void setIndex( String index )
  {
    this.index = index;
  }

  @JsonProperty( "index" )
  public Long getIndexAsLong()
  {
    return StringUtils.isNotBlank( index ) ? Long.valueOf( index ) : null;
  }

  @JsonIgnore
  public String getBillCodeDomainName( Long promotionId )
  {
    String[] s = getBillCode().split( "_" );
    String val = s[1];
    /*
     * Since we are changing the value of the drop down, Bill_code in the DB table will get
     * affected. To avoid the issue, if the bill code from Form does not match with any of the
     * actual bill codes, before setting the same in table, changing back to custom Value
     */
    List<String> billCodes = getPromotionService().getAllUniqueBillCodes( promotionId );

    long billCodeCounter = 0;
    if ( billCodes != null )
    {
      billCodeCounter = billCodes.stream().filter( billCode -> billCode.equalsIgnoreCase( val ) ).count();
    }
    if ( billCodeCounter == 0 )
    {
      s[1] = "customValue";
    }

    return s.length > 1 ? s[1] : null;
  }

  @JsonIgnore
  public String getTrackBy()
  {
    String[] s = getBillCode().split( "_" );
    return s.length > 0 ? s[0] : null;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  public String getPromoBillCodeOrder()
  {
    return promoBillCodeOrder;
  }

  public void setPromoBillCodeOrder( String promoBillCodeOrder )
  {
    this.promoBillCodeOrder = promoBillCodeOrder;
  }
}
