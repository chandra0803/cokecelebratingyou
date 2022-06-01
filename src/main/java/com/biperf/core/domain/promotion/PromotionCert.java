/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionCert.java,v $
 */

package com.biperf.core.domain.promotion;

import java.sql.Timestamp;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.utils.UserManager;

/**
 * PromotionCert.
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
 * <td>meadows</td>
 * <td>Sep 6, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionCert extends BaseDomain implements Comparable
{

  private static final long serialVersionUID = 8618059577580176768L;
  private AuditCreateInfo auditCreateInfo;

  /** Promotion */
  private Promotion promotion;
  /** Certificate */
  private String certificateId;

  private String orderNumber;

  public PromotionCert()
  {
    auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( UserManager.getUserId() );
    auditCreateInfo.setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  public PromotionCert( Promotion promotion, String certificateId )
  {
    this();
    this.promotion = promotion;
    this.certificateId = certificateId;
  }

  public PromotionCert( Promotion promotion, String certificateId, String orderNumber )
  {
    this();
    this.promotion = promotion;
    this.certificateId = certificateId;
    this.orderNumber = orderNumber;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public String getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( String certificateId )
  {
    this.certificateId = certificateId;
  }

  /**
   * Overridden from @see java.lang.Object#hashCode()
   * @return
   */
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( certificateId == null ? 0 : certificateId.hashCode() );
    result = prime * result + ( promotion == null ? 0 : promotion.hashCode() );
    // result = prime * result + ( ( orderNumber == null ) ? 0 : orderNumber.hashCode() );
    return result;
  }

  /**
   * Overridden from @see java.lang.Object#equals(java.lang.Object)
   * @param obj
   * @return
   */
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
    final PromotionCert other = (PromotionCert)obj;
    if ( certificateId == null )
    {
      if ( other.certificateId != null )
      {
        return false;
      }
    }
    else if ( !certificateId.equals( other.certificateId ) )
    {
      return false;
    }
    /*
     * if ( orderNumber == null ) { if ( other.orderNumber != null ) return false; } else if (
     * !orderNumber.equals( other.orderNumber ) ) return false;
     */
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
    return true;
  }

  /**
   * Overridden from @see java.lang.Comparable#compareTo(java.lang.Object)
   * @param object
   * @return int
   * @throws ClassCastException
   */
  public int compareTo( Object object ) throws ClassCastException
  {
    if ( ! ( object instanceof PromotionCert ) )
    {
      throw new ClassCastException( "A PromotionCert was expected." );
    }
    PromotionCert promotionCert = (PromotionCert)object;
    return this.getCertificateId().compareTo( promotionCert.getCertificateId() );

  }

  public PromotionCert deepCopy()
  {
    PromotionCert clone = new PromotionCert();
    clone.setCertificateId( this.certificateId );
    // clone.setOrderNumber(this.orderNumber);
    return clone;
  }

  public String getOrderNumber()
  {
    return orderNumber;
  }

  public void setOrderNumber( String orderNumber )
  {
    this.orderNumber = orderNumber;
  }

}
