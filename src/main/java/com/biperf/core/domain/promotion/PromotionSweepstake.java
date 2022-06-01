/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionSweepstake.java,v $
 *
 */

package com.biperf.core.domain.promotion;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;

/**
 * PromotionSweepstake <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>jenniget</td>
 * <td>Oct 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author jenniget
 *
 */
public class PromotionSweepstake extends BaseDomain
{
  private Promotion promotion;
  private Date startDate;
  private Date endDate;
  private String guid;
  private boolean processed;
  private Set winners = new LinkedHashSet(); // Contains PromotionSweepstakeWinners

  /**
   * public constructor that sets the createdBy and DateCreated properties. We do this since
   * Hibernate does not treat this as an entity - thus the HibernateAuditInterceptor is not used.
   */
  public PromotionSweepstake()
  {
    guid = GuidUtils.generateGuid();
    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  /**
   * Public constructor to to build this with a promotion.
   * 
   * @param promotion
   */
  public PromotionSweepstake( Promotion promotion )
  {
    guid = GuidUtils.generateGuid();
    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
    this.promotion = promotion;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof PromotionSweepstake ) )
    {
      return false;
    }

    final PromotionSweepstake sweep = (PromotionSweepstake)o;

    if ( getGuid() != null ? !getGuid().equals( sweep.getGuid() ) : sweep.getGuid() != null )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return 29 * ( getGuid() != null ? getGuid().hashCode() : 0 );
  }

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "PromotionSweepstake" );
    buf.append( ",id=" ).append( getId() );
    buf.append( ",guid=" ).append( guid );
    buf.append( ",promotion=" ).append( promotion );
    buf.append( ",startDate=" ).append( startDate );
    buf.append( ",endDate=" ).append( endDate );
    buf.append( ",processed=" ).append( processed );
    buf.append( '}' );
    return buf.toString();
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  public boolean isProcessed()
  {
    return processed;
  }

  public void setProcessed( boolean processed )
  {
    this.processed = processed;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public Set getWinners()
  {
    return winners;
  }

  public void setWinners( Set winners )
  {
    this.winners = winners;
  }

  public void addWinner( PromotionSweepstakeWinner winner )
  {
    winner.setPromotionSweepstake( this );
    this.winners.add( winner );
  }

  public String getGuid()
  {
    return guid;
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }
}
