
package com.biperf.core.ui.recognition;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.biperf.core.domain.enums.ShippingMethodType;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.service.claim.CalculatorResponseBean;
import com.biperf.core.ui.user.ShippingAddressFormBean;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.databeans.order.ShippingHandlingChargesDataBean;
import com.biperf.databeans.order.TaxInformationDataBean;
import com.biperf.util.StringUtils;

/**
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
 * <td>David Potosky</td>
 * <td>Oct 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimRecipientFormBean implements Serializable, Comparable
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final BigDecimal ONE_HUNDRED = new BigDecimal( 100.0 );
  private FormattedValueBean formattedValueBean = new FormattedValueBean();
  private String firstName = "";
  private String lastName = "";
  private String emailAddr;
  private String departmentType = "";
  private String positionType = "";
  private String countryCode;
  private String countryName;
  private String jobName;
  private String departmentName;
  private String awardQuantity;
  private Long nodeId;
  private String nodeName;
  private String calculatorScore;
  private String lowAward;
  private String highAward;
  private Long levelId;
  private String levelName;
  private Integer levelValue;
  private List calculatorResponseBeans = new ArrayList();
  private ShippingAddressFormBean shippingAddressFormBean = new ShippingAddressFormBean();
  private String productId;
  private String productDescription;
  private String productThumbNailImgURL;
  private String productCopy;
  private String productDetailImgURL;
  private long productPrice = 0;
  private String giftWrapId;
  private String giftWrapThumbNailImgURL;
  private long giftWrapPrice = 0;
  private boolean cardable;
  private String cardProductId;
  private String occasion;
  private String message;
  private String shippingMethod;
  private ShippingHandlingChargesDataBean shippingHandlingChargesDataBean;
  private TaxInformationDataBean taxInformationDataBean;
  private boolean giftable;
  private boolean fastShippable = false;
  private String wrapType;
  private Collection paperWrapProducts;
  private Collection boxWrapProducts;
  private List enclosureCardOccasions;
  private Collection geoCodes;
  private String geoCode;
  private MerchOrder merchOrder;

  // Client customizations for wip #42701 starts
  private String currency;
  private Long awardMin;
  private Long awardMax;

  public String getCurrency()
  {
    return currency;
  }

  public void setCurrency( String currency )
  {
    this.currency = currency;
  }

  public Long getAwardMin()
  {
    return awardMin;
  }

  public void setAwardMin( Long awardMin )
  {
    this.awardMin = awardMin;
  }

  public Long getAwardMax()
  {
    return awardMax;
  }

  public void setAwardMax( Long awardMax )
  {
    this.awardMax = awardMax;
  }
  // Client customizations for wip #42701 ends
  // ---------------------------------------------------------------------------
  // Compare To, Equals, and Hash Code Methods
  // ---------------------------------------------------------------------------

  public Collection getBoxWrapProducts()
  {
    return boxWrapProducts;
  }

  public void setBoxWrapProducts( Collection boxWrapProducts )
  {
    this.boxWrapProducts = boxWrapProducts;
  }

  public List getEnclosureCardOccasions()
  {
    return enclosureCardOccasions;
  }

  public void setEnclosureCardOccasions( List enclosureCardOccasions )
  {
    this.enclosureCardOccasions = enclosureCardOccasions;
  }

  public Collection getPaperWrapProducts()
  {
    return paperWrapProducts;
  }

  public void setPaperWrapProducts( Collection paperWrapProducts )
  {
    this.paperWrapProducts = paperWrapProducts;
  }

  public boolean isCardable()
  {
    return cardable;
  }

  public void setCardable( boolean cardable )
  {
    this.cardable = cardable;
  }

  public boolean isGiftable()
  {
    return giftable;
  }

  public void setGiftable( boolean giftable )
  {
    this.giftable = giftable;
  }

  public long getProductPrice()
  {
    return productPrice;
  }

  public BigDecimal getProductPriceForDisplay()
  {
    return getValueForDisplay( productPrice );
  }

  public void setProductPrice( String productPrice )
  {
    // try{
    // this.productPrice = new BigDecimal(productPrice);
    // }
    // catch(NumberFormatException e){
    // this.productPrice = new BigDecimal("0");
    // }
  }

  public void setProductPrice( long productPrice )
  {
    this.productPrice = productPrice;
  }

  public String getWrapType()
  {
    return wrapType;
  }

  public void setWrapType( String wrapType )
  {
    this.wrapType = wrapType;
  }

  public String getGiftWrapId()
  {
    return giftWrapId;
  }

  public void setGiftWrapId( String giftWrapId )
  {
    this.giftWrapId = giftWrapId;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public String getOccasion()
  {
    return occasion;
  }

  public void setOccasion( String occasion )
  {
    this.occasion = occasion;
  }

  public String getProductId()
  {
    return productId;
  }

  public void setProductId( String productId )
  {
    this.productId = productId;
  }

  public String getShippingMethod()
  {
    return shippingMethod;
  }

  public void setShippingMethod( String shippingMethod )
  {
    this.shippingMethod = shippingMethod;
  }

  public int compareTo( Object object ) throws ClassCastException
  {
    if ( ! ( object instanceof ClaimRecipientFormBean ) )
    {
      throw new ClassCastException( "A claimRecipientForm was expected." );
    }
    ClaimRecipientFormBean claimRecipientForm = (ClaimRecipientFormBean)object;

    return this.getFormattedValueBean().getValue().compareTo( claimRecipientForm.getFormattedValueBean().getValue() );
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
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

    if ( ! ( object instanceof ClaimRecipientFormBean ) )
    {
      return false;
    }

    final ClaimRecipientFormBean valueBean = (ClaimRecipientFormBean)object;

    if ( getFormattedValueBean() != null ? !getFormattedValueBean().equals( valueBean.getFormattedValueBean() ) : valueBean.getFormattedValueBean() != null )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return getFormattedValueBean() != null ? getFormattedValueBean().hashCode() : 0;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getAwardQuantity()
  {
    return awardQuantity;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName( String jobName )
  {
    this.jobName = jobName;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  public FormattedValueBean getFormattedValueBean()
  {
    return formattedValueBean;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setAwardQuantity( String awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public void setFormattedValueBean( FormattedValueBean formattedValueBean )
  {
    this.formattedValueBean = formattedValueBean;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public List getCalculatorResponseBeans()
  {
    return calculatorResponseBeans;
  }

  public void setCalculatorResponseBeans( List calculatorResponseBeans )
  {
    this.calculatorResponseBeans = calculatorResponseBeans;
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of CalculatorResponseBean from the value list
   */
  public CalculatorResponseBean getCalculatorResponseBeans( int index )
  {
    try
    {
      return (CalculatorResponseBean)calculatorResponseBeans.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  /**
   * Accessor for the number of CalculatorResponseBean objects in the list.
   * 
   * @return int
   */
  public int getCalculatorResponseBeansCount()
  {
    if ( calculatorResponseBeans == null )
    {
      return 0;
    }

    return calculatorResponseBeans.size();
  }

  public String getCalculatorScore()
  {
    return calculatorScore;
  }

  public void setCalculatorScore( String calculatorScore )
  {
    this.calculatorScore = calculatorScore;
  }

  public String getHighAward()
  {
    return highAward;
  }

  public void setHighAward( String highAward )
  {
    this.highAward = highAward;
  }

  public String getLowAward()
  {
    return lowAward;
  }

  public void setLowAward( String lowAward )
  {
    this.lowAward = lowAward;
  }

  public Long getLevelId()
  {
    return levelId;
  }

  public void setLevelId( Long levelId )
  {
    this.levelId = levelId;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public Integer getLevelValue()
  {
    return levelValue;
  }

  public void setLevelValue( Integer levelValue )
  {
    this.levelValue = levelValue;
  }

  public String getDepartmentType()
  {
    return departmentType;
  }

  public void setDepartmentType( String departmentType )
  {
    this.departmentType = departmentType;
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

  public String getEmailAddr()
  {
    return emailAddr;
  }

  public void setEmailAddr( String emailAddr )
  {
    this.emailAddr = emailAddr;
  }

  public String getPositionType()
  {
    return positionType;
  }

  public void setPositionType( String positionType )
  {
    this.positionType = positionType;
  }

  public ShippingAddressFormBean getShippingAddressFormBean()
  {
    return shippingAddressFormBean;
  }

  public void setShippingAddressFormBean( ShippingAddressFormBean shippingAddressFormBean )
  {
    this.shippingAddressFormBean = shippingAddressFormBean;
  }

  public List getStateList()
  {
    return ShippingAddressFormBean.getStateListByCountryCode( countryCode );
  }

  public String getProductCopy()
  {
    return productCopy;
  }

  public void setProductCopy( String productCopy )
  {
    this.productCopy = productCopy;
  }

  public String getProductDescription()
  {
    return productDescription;
  }

  public void setProductDescription( String productDescription )
  {
    this.productDescription = productDescription;
  }

  public String getProductDetailImgURL()
  {
    return productDetailImgURL;
  }

  public void setProductDetailImgURL( String productDetailImgURL )
  {
    this.productDetailImgURL = productDetailImgURL;
  }

  public String getProductThumbNailImgURL()
  {
    return productThumbNailImgURL;
  }

  public void setProductThumbNailImgURL( String productThumbNailImgURL )
  {
    this.productThumbNailImgURL = productThumbNailImgURL;
  }

  public long getGiftWrapPrice()
  {
    return giftWrapPrice;
  }

  public BigDecimal getGiftWrapPriceForDisplay()
  {
    return getValueForDisplay( giftWrapPrice );
  }

  public void setGiftWrapPrice( String price )
  {
    // try{
    // this.giftWrapPrice = new BigDecimal(price);
    // }
    // catch(NumberFormatException e){
    // this.giftWrapPrice = new BigDecimal("0");
    // }
  }

  public void setGiftWrapPrice( long giftWrapPrice )
  {
    this.giftWrapPrice = giftWrapPrice;
  }

  public String getGiftWrapThumbNailImgURL()
  {
    return giftWrapThumbNailImgURL;
  }

  public void setGiftWrapThumbNailImgURL( String giftWrapThumbNailImgURL )
  {
    this.giftWrapThumbNailImgURL = giftWrapThumbNailImgURL;
  }

  public String getCardProductId()
  {
    return cardProductId;
  }

  public void setCardProductId( String cardProductId )
  {
    this.cardProductId = cardProductId;
  }

  public long getShippingHandlingCharges()
  {
    if ( this.getShippingHandlingChargesDataBean() != null && ShippingMethodType.isValidShippingMethod( shippingMethod ) )
    {
      return shippingHandlingChargesDataBean.getShippingHandlingCharges( shippingMethod );
    }
    return 0;
  }

  public BigDecimal getShippingHandlingChargesForDisplay()
  {
    return getValueForDisplay( getShippingCharges() );
  }

  public long getShippingCharges()
  {
    if ( this.getShippingHandlingChargesDataBean() != null && ShippingMethodType.isValidShippingMethod( shippingMethod ) )
    {
      return shippingHandlingChargesDataBean.getShippingCharge( shippingMethod );
    }
    return 0;
  }

  public long getHandlingCharges()
  {
    if ( this.getShippingHandlingChargesDataBean() != null && ShippingMethodType.isValidShippingMethod( shippingMethod ) )
    {
      return shippingHandlingChargesDataBean.getHandlingCharge( shippingMethod );
    }
    return 0;
  }

  public long getTotalCharges()
  {
    long shippingTaxCharge = this.getShippingHandlingCharges() + this.getTaxCharges();
    return this.getProductPrice() + this.getGiftWrapPrice() + shippingTaxCharge;
  }

  public BigDecimal getTotalChargesForDisplay()
  {
    return getValueForDisplay( getTotalCharges() );
  }

  public String getShippingMessage()
  {
    StringBuffer name = new StringBuffer();
    if ( ShippingMethodType.isValidShippingMethod( shippingMethod ) )
    {
      name.append( ShippingMethodType.lookup( shippingMethod ).getName() );
      name.append( "<br>" );
    }

    return name.toString();
  }

  public ShippingHandlingChargesDataBean getShippingHandlingChargesDataBean()
  {
    return shippingHandlingChargesDataBean;
  }

  public void setShippingHandlingChargesDataBean( ShippingHandlingChargesDataBean shippingHandlingChargesDataBean )
  {
    this.shippingHandlingChargesDataBean = shippingHandlingChargesDataBean;
  }

  public long getTaxCharges()
  {
    long taxCharges = 0;
    if ( this.getShippingHandlingChargesDataBean() != null && !StringUtils.isEmpty( shippingMethod ) )
    {
      taxCharges += shippingHandlingChargesDataBean.getTaxCharges( shippingMethod );
    }
    if ( this.getTaxInformationDataBean() != null )
    {
      taxCharges += this.getTaxInformationDataBean().getTotalProductTax();
    }
    return taxCharges;
  }

  public BigDecimal getTaxChargesForDisplay()
  {
    return getValueForDisplay( getTaxCharges() );
  }

  public long getTotalProductTax()
  {
    if ( this.getTaxInformationDataBean() != null )
    {
      return taxInformationDataBean.getTotalProductTax();
    }
    return 0;
  }

  public TaxInformationDataBean getTaxInformationDataBean()
  {
    return taxInformationDataBean;
  }

  public void setTaxInformationDataBean( TaxInformationDataBean taxInformationDataBean )
  {
    this.taxInformationDataBean = taxInformationDataBean;
  }

  private BigDecimal getValueForDisplay( long longValue )
  {
    BigDecimal adjustedValue = new BigDecimal( longValue );
    return adjustedValue.divide( ONE_HUNDRED, 2, BigDecimal.ROUND_HALF_UP );
  }

  public Collection getGeoCodes()
  {
    return geoCodes;
  }

  public void setGeoCodes( Collection geoCodes )
  {
    this.geoCodes = geoCodes;
  }

  public int getGeoCodeCount()
  {
    if ( getGeoCodes() != null )
    {
      return getGeoCodes().size();
    }
    return 0;
  }

  public String getGeoCode()
  {
    return geoCode;
  }

  public void setGeoCode( String geoCode )
  {
    this.geoCode = geoCode;
  }

  public MerchOrder getMerchOrder()
  {
    return merchOrder;
  }

  public void setMerchOrder( MerchOrder merchOrder )
  {
    this.merchOrder = merchOrder;
  }

  public boolean isFastShippable()
  {
    return fastShippable;
  }

  public void setFastShippable( boolean fastShippable )
  {
    this.fastShippable = fastShippable;
  }
}
