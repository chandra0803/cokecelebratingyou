
package com.biperf.core.service.claim;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.utils.StringUtil;

public class RecognitionClaimRecipient
{
  private Long userId;
  private Long awardQuantity;
  private Long awardLevelId;
  private String countryCode;
  // Client customization for WIP #42701 starts
  private Long cashAwardQuantity;
  private String cashCurrencyCode;
  private String cashPaxDivisionNumber;
  // Client customization for WIP #42701 ends 
  private Long nodeId;
  private String calculatorScore;
  private Boolean optOutAwards;
  private List<CalculatorResponseBean> calculatorResponseBeans = new ArrayList<CalculatorResponseBean>();

  protected RecognitionClaimRecipient( Long userId, Long nodeId, Long awardQuantity, Long awardLevelId, String countryCode )
  {
    this.userId = userId;
    this.nodeId = nodeId;   
    this.awardQuantity = awardQuantity;
    this.awardLevelId = awardLevelId;
    this.countryCode = countryCode;
  } 

  // Client customization for WIP #42701 starts
  protected RecognitionClaimRecipient( Long userId, Long nodeId, Long awardQuantity, Long awardLevelId, String countryCode, Boolean optOut, String currencyCode, String divisionKey )
  {
    this.userId = userId;
    this.nodeId = nodeId;
    if ( !optOut )
    {
      this.awardQuantity = awardQuantity;
    }
    this.awardLevelId = awardLevelId;
    this.countryCode = countryCode;
    this.optOutAwards = optOutAwards;
    this.cashAwardQuantity =  awardQuantity;
    this.cashCurrencyCode = currencyCode;
    this.cashPaxDivisionNumber = divisionKey;
  }
  // Client customization for WIP #42701 ends
  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( Long awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  public Long getAwardLevelId()
  {
    return awardLevelId;
  }

  public void setAwardLevelId( Long awardLevelId )
  {
    this.awardLevelId = awardLevelId;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getCalculatorScore()
  {
    return calculatorScore;
  }

  public void addToCalculatorScore( int ratingScore )
  {
    if ( StringUtil.isEmpty( calculatorScore ) )
    {
      calculatorScore = "0";
    }

    calculatorScore = String.valueOf( Integer.parseInt( calculatorScore ) + ratingScore );
  }

  public List<CalculatorResponseBean> getCalculatorResponseBeans()
  {
    return calculatorResponseBeans;
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

  public void addCalculatorResponse( Long criteriaId, Long ratingId )
  {
    if ( calculatorResponseBeans == null )
    {
      calculatorResponseBeans = new ArrayList<CalculatorResponseBean>();
    }
    calculatorResponseBeans.add( new CalculatorResponseBean( criteriaId, ratingId ) );
  }
  /* coke customization start */
  public Boolean getIsOptOut()
  {
    return optOutAwards;
  }

  public void setIsOptOut( Boolean isOptOut )
  {
    this.optOutAwards = optOutAwards;
  }
  // Client customization for WIP #42701 starts
  public Long getCashAwardQuantity()
  {
    return cashAwardQuantity;
  }

  public void setCashAwardQuantity( Long cashAwardQuantity )
  {
    this.cashAwardQuantity = cashAwardQuantity;
  }

  public String getCashCurrencyCode()
  {
    return cashCurrencyCode;
  }

  public void setCashCurrencyCode( String cashCurrencyCode )
  {
    this.cashCurrencyCode = cashCurrencyCode;
  }

  public String getCashPaxDivisionNumber()
  {
    return cashPaxDivisionNumber;
  }

  public void setCashPaxDivisionNumber( String cashPaxDivisionNumber )
  {
    this.cashPaxDivisionNumber = cashPaxDivisionNumber;
  }
  // Client customization for WIP #42701 ends
}
