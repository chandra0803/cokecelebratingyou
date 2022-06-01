/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/merchandise/ActivityMerchOrder.java,v $
 */

package com.biperf.core.domain.merchandise;

import java.io.Serializable;
import java.sql.Timestamp;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.utils.UserManager;

public class ActivityMerchOrder implements AuditCreateInterface, Serializable
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * One of zero or more activities transactions associated with the given journal transaction.
   */
  private Activity activity;

  /**
   * One of zero or more journal transactions associated with the given activity.
   */
  private MerchOrder merchOrder;

  /**
   * Information about the creation of this ActivityJournal object.
   */
  private AuditCreateInfo auditCreateInfo = new AuditCreateInfo();

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  /**
   * Constructs an <code>ActivityJournal</code> object.
   */
  public ActivityMerchOrder()
  {
    auditCreateInfo.setCreatedBy( UserManager.getUserId() == null ? new Long( 0 ) : UserManager.getUserId() );
    auditCreateInfo.setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  /**
   * Constructs an <code>ActivityJournal</code> object.
   * 
   * @param activity an activity.
   * @param journal a journal transaction.
   */
  public ActivityMerchOrder( Activity activity, MerchOrder merchOrder )
  {
    this();

    this.activity = activity;
    this.merchOrder = merchOrder;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the activity component of this activity/journal association.
   * 
   * @return the activity component of this activity/journal association.
   */
  public Activity getActivity()
  {
    return activity;
  }

  /**
   * Returns information about the creation of this domain object.
   * 
   * @return information about the creation of this domain object.
   */
  public AuditCreateInfo getAuditCreateInfo()
  {
    return auditCreateInfo;
  }

  /**
   * Sets the activity component of this activity/journal association.
   * 
   * @param activity the activity component of this activity/journal association.
   */
  public void setActivity( Activity activity )
  {
    this.activity = activity;
  }

  /**
   * Sets information about the creation of this domain object.
   * 
   * @param auditCreateInfo information about the creation of this domain object.
   */
  public void setAuditCreateInfo( AuditCreateInfo auditCreateInfo )
  {
    this.auditCreateInfo = auditCreateInfo;
  }

  public MerchOrder getMerchOrder()
  {
    return merchOrder;
  }

  public void setMerchOrder( MerchOrder merchOrder )
  {
    this.merchOrder = merchOrder;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns true if this object and the given object are equal; returns false otherwise.
   * 
   * @param obj the object to compare for equality with this object.
   * @return true if this object and the given object are equal; false otherwise.
   */
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }

    if ( ! ( obj instanceof ActivityMerchOrder ) )
    {
      return false;
    }

    final ActivityMerchOrder that = (ActivityMerchOrder)obj;

    if ( getActivity() != null ? !getActivity().equals( that.getActivity() ) : that.getActivity() != null )
    {
      return false;
    }
    if ( getMerchOrder() != null ? !getMerchOrder().equals( that.getMerchOrder() ) : that.getMerchOrder() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Returns this object's hashcode.
   * 
   * @return this object's hashcode.
   */
  public int hashCode()
  {
    int result;

    result = getActivity() != null ? getActivity().hashCode() : 0;
    result = 31 * result + ( getMerchOrder() != null ? getMerchOrder().hashCode() : 0 );

    return result;
  }
}
