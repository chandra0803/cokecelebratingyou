/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/journal/ActivityJournal.java,v $
 */

package com.biperf.core.domain.journal;

import java.io.Serializable;
import java.sql.Timestamp;

import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.AuditCreateInterface;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.utils.UserManager;

/*
 * ActivityJournal <p>Associates a journal transaction with an activity.</p> <b>Change History:</b><br>
 * <table border="1"> <tr> <th>Author</th> <th>Date</th> <th>Version</th> <th>Comments</th>
 * </tr> <tr> <td>Thomas Eaton</td> <td>Aug 10, 2005</td> <td>1.0</td> <td>created</td>
 * </tr> </table> </p>
 * 
 *
 */

public class ActivityJournal implements AuditCreateInterface, Serializable
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
  private Journal journal;

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
  public ActivityJournal()
  {
    auditCreateInfo.setCreatedBy( UserManager.getUserId() );
    auditCreateInfo.setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  /**
   * Constructs an <code>ActivityJournal</code> object.
   * 
   * @param activity an activity.
   * @param journal a journal transaction.
   */
  public ActivityJournal( Activity activity, Journal journal )
  {
    this();

    this.activity = activity;
    this.journal = journal;
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
   * Returns the journal component of this activity/journal association.
   * 
   * @return the journal component of this activity/journal association.
   */
  public Journal getJournal()
  {
    return journal;
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

  /**
   * Sets the journal component of this activity/journal association.
   * 
   * @param journal the journal component of this activity/journal association.
   */
  public void setJournal( Journal journal )
  {
    this.journal = journal;
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

    if ( ! ( obj instanceof ActivityJournal ) )
    {
      return false;
    }

    final ActivityJournal that = (ActivityJournal)obj;

    if ( getActivity() != null ? !getActivity().equals( that.getActivity() ) : that.getActivity() != null )
    {
      return false;
    }
    if ( getJournal() != null ? !getJournal().equals( that.getJournal() ) : that.getJournal() != null )
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
    result = 31 * result + ( getJournal() != null ? getJournal().hashCode() : 0 );

    return result;
  }
}
