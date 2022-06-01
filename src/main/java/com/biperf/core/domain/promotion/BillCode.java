
package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.BillCodeNominationType;
import com.biperf.core.domain.enums.BillCodeRecognitionType;
import com.biperf.core.domain.enums.BillCodeSSIType;
import com.biperf.util.StringUtils;


public abstract class BillCode extends BaseDomain
{

  private static final String SEPARATOR = "_";

  private Promotion promotion;
  private String trackBillCodeBy;
  private Long sortOrder;
  private String billCode;
  private String customValue;

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( promotion == null ? 0 : promotion.hashCode() );
    result = prime * result + ( sortOrder == null ? 0 : sortOrder.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    BillCode other = (BillCode)obj;
    if ( promotion == null )
    {
      if ( other.promotion != null )
      {
        return false;
      }
    }
    else if ( !promotion.equals( other.promotion ) )
    {
      return false;
    }
    if ( sortOrder == null )
    {
      if ( other.sortOrder != null )
      {
        return false;
      }
    }
    else if ( !sortOrder.equals( other.sortOrder ) )
    {
      return false;
    }
    return true;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public Long getSortOrder()
  {
    return sortOrder;
  }

  public void setSortOrder( Long sortOrder )
  {
    this.sortOrder = sortOrder;
  }

  public String getBillCode()
  {
    return billCode;
  }

  public void setBillCode( String billCode )
  {
    if ( !StringUtils.isEmpty( billCode ) )
    {
      String giverPrefix = BillCodeRecognitionType.GIVER + SEPARATOR;
      String receiverPrefix = BillCodeRecognitionType.RECEIVER + SEPARATOR;
      String nominatorPrefix = BillCodeNominationType.NOMINATOR + SEPARATOR;
      String nomineePrefix = BillCodeNominationType.NOMINEE + SEPARATOR;
      String creatorPrefix = BillCodeSSIType.CREATOR + SEPARATOR;
      String participantPrefix = BillCodeSSIType.PARTICIPANT + SEPARATOR;

      if ( billCode.startsWith( giverPrefix ) )
      {
        billCode = billCode.substring( billCode.indexOf( SEPARATOR ) + 1 );
        this.trackBillCodeBy = BillCodeRecognitionType.GIVER;
      }
      else if ( billCode.startsWith( receiverPrefix ) )
      {
        billCode = billCode.substring( billCode.indexOf( SEPARATOR ) + 1 );
        this.trackBillCodeBy = BillCodeRecognitionType.RECEIVER;
      }
      else if ( billCode.startsWith( nominatorPrefix ) )
      {
        billCode = billCode.substring( billCode.indexOf( SEPARATOR ) + 1 );
        this.trackBillCodeBy = BillCodeNominationType.NOMINATOR;
      }
      else if ( billCode.startsWith( nomineePrefix ) )
      {
        billCode = billCode.substring( billCode.indexOf( SEPARATOR ) + 1 );
        this.trackBillCodeBy = BillCodeNominationType.NOMINEE;
      }
      else if ( billCode.startsWith( creatorPrefix ) )
      {
        billCode = billCode.substring( billCode.indexOf( SEPARATOR ) + 1 );
        this.trackBillCodeBy = BillCodeSSIType.CREATOR;
      }
      else if ( billCode.startsWith( participantPrefix ) )
      {
        billCode = billCode.substring( billCode.indexOf( SEPARATOR ) + 1 );
        this.trackBillCodeBy = BillCodeSSIType.PARTICIPANT;
      }

    }
    this.billCode = billCode;
  }

  public String getCustomValue()
  {
    return customValue;
  }

  public void setCustomValue( String customValue )
  {
    if ( !StringUtils.isEmpty( customValue ) )
    {
      String giverPrefix = BillCodeRecognitionType.GIVER + SEPARATOR;
      String receiverPrefix = BillCodeRecognitionType.RECEIVER + SEPARATOR;
      String nominatorPrefix = BillCodeNominationType.NOMINATOR + SEPARATOR;
      String nomineePrefix = BillCodeNominationType.NOMINEE + SEPARATOR;
      String creatorPrefix = BillCodeSSIType.CREATOR + SEPARATOR;
      String participantPrefix = BillCodeSSIType.PARTICIPANT + SEPARATOR;

      if ( customValue.startsWith( giverPrefix ) )
      {
        customValue = customValue.substring( customValue.indexOf( SEPARATOR ) + 1 );
        this.trackBillCodeBy = BillCodeRecognitionType.GIVER;
      }
      else if ( customValue.startsWith( receiverPrefix ) )
      {
        customValue = customValue.substring( customValue.indexOf( SEPARATOR ) + 1 );
        this.trackBillCodeBy = BillCodeRecognitionType.RECEIVER;
      }
      else if ( customValue.startsWith( nominatorPrefix ) )
      {
        customValue = customValue.substring( customValue.indexOf( SEPARATOR ) + 1 );
        this.trackBillCodeBy = BillCodeNominationType.NOMINATOR;
      }
      else if ( customValue.startsWith( nomineePrefix ) )
      {
        customValue = customValue.substring( customValue.indexOf( SEPARATOR ) + 1 );
        this.trackBillCodeBy = BillCodeNominationType.NOMINEE;
      }
      else if ( customValue.startsWith( creatorPrefix ) )
      {
        customValue = customValue.substring( customValue.indexOf( SEPARATOR ) + 1 );
        this.trackBillCodeBy = BillCodeSSIType.CREATOR;
      }
      else if ( customValue.startsWith( participantPrefix ) )
      {
        customValue = customValue.substring( customValue.indexOf( SEPARATOR ) + 1 );
        this.trackBillCodeBy = BillCodeSSIType.PARTICIPANT;
      }
    }
    this.customValue = customValue;
  }

  public String getTrackBillCodeBy()
  {
    return trackBillCodeBy;
  }

  public void setTrackBillCodeBy( String trackBillCodeBy )
  {
    this.trackBillCodeBy = trackBillCodeBy;
  }

}
