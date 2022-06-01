/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.claim;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;

/**
 * .
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
 * <td>wadzinsk</td>
 * <td>Mar 2, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MinimumQualifierStatus extends BaseDomain
{

  private Participant submitter;
  private PromotionPayoutGroup promotionPayoutGroup;
  private boolean minQualifierMet;
  private int completedQuantity;

  /**
   * @return value of completedQuantity property
   */
  public int getCompletedQuantity()
  {
    return completedQuantity;
  }

  /**
   * @param completedQuantity value for completedQuantity property
   */
  public void setCompletedQuantity( int completedQuantity )
  {
    this.completedQuantity = completedQuantity;
  }

  /**
   * @return value of minQualifierMet property
   */
  public boolean isMinQualifierMet()
  {
    return minQualifierMet;
  }

  /**
   * @param minQualifierMet value for minQualifierMet property
   */
  public void setMinQualifierMet( boolean minQualifierMet )
  {
    this.minQualifierMet = minQualifierMet;
  }

  /**
   * @return value of promotionPayoutGroup property
   */
  public PromotionPayoutGroup getPromotionPayoutGroup()
  {
    return promotionPayoutGroup;
  }

  /**
   * @param promotionPayoutGroup value for promotionPayoutGroup property
   */
  public void setPromotionPayoutGroup( PromotionPayoutGroup promotionPayoutGroup )
  {
    this.promotionPayoutGroup = promotionPayoutGroup;
  }

  /**
   * @return value of submitter property
   */
  public Participant getSubmitter()
  {
    return submitter;
  }

  /**
   * @param submitter value for submitter property
   */
  public void setSubmitter( Participant submitter )
  {
    this.submitter = submitter;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ( submitter == null ? 0 : submitter.hashCode() );
    result = PRIME * result + ( promotionPayoutGroup == null ? 0 : promotionPayoutGroup.hashCode() );
    return result;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   * @param obj
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
    if ( ! ( obj instanceof MinimumQualifierStatus ) )
    {
      return false;
    }
    final MinimumQualifierStatus other = (MinimumQualifierStatus)obj;
    if ( submitter == null )
    {
      if ( other.submitter != null )
      {
        return false;
      }
    }
    else if ( !submitter.equals( other.getSubmitter() ) )
    {
      return false;
    }
    if ( promotionPayoutGroup == null )
    {
      if ( other.promotionPayoutGroup != null )
      {
        return false;
      }
    }
    else if ( !promotionPayoutGroup.equals( other.getPromotionPayoutGroup() ) )
    {
      return false;
    }
    return true;
  }

}
