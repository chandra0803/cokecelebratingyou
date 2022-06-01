/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/claim/ClaimFormStepEmailNotification.java,v $
 */

package com.biperf.core.domain.claim;

import java.io.Serializable;

import com.biperf.core.domain.enums.ClaimFormStepEmailNotificationType;

/**
 * ClaimFormStepEmailNotification.
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
 * <td>crosenquest</td>
 * <td>Jun 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormStepEmailNotification implements Serializable
{

  private Long id;

  /** ClaimFormStep */
  private ClaimFormStep claimFormStep;

  /** ClaimFormStepEmailNotificationType */
  private ClaimFormStepEmailNotificationType claimFormStepEmailNotificationType;

  /**
   * Default constructor required after copy constructor added.
   */
  public ClaimFormStepEmailNotification()
  {
    super();
    // default constructor
  }

  public Long getId()
  {
    return this.id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  /**
   * Constructor used to setup this.
   * 
   * @param claimFormStepEmailNotification
   * @param claimFormStep
   */
  public ClaimFormStepEmailNotification( ClaimFormStepEmailNotification claimFormStepEmailNotification, ClaimFormStep claimFormStep )
  {
    super();
    claimFormStepEmailNotificationType = claimFormStepEmailNotification.getClaimFormStepEmailNotificationType();
    this.claimFormStep = claimFormStep;
  }

  /**
   * Get the notification type.
   * 
   * @return ClaimFormStepEmailNotificationType
   */
  public ClaimFormStepEmailNotificationType getClaimFormStepEmailNotificationType()
  {
    return claimFormStepEmailNotificationType;
  }

  /**
   * Set the notification type.
   * 
   * @param claimFormStepEmailNotificationType
   */
  public void setClaimFormStepEmailNotificationType( ClaimFormStepEmailNotificationType claimFormStepEmailNotificationType )
  {
    this.claimFormStepEmailNotificationType = claimFormStepEmailNotificationType;
  }

  /**
   * Get the claimFormStep to which this notification is attached.
   * 
   * @return ClaimFormStep
   */
  public ClaimFormStep getClaimFormStep()
  {
    return claimFormStep;
  }

  /**
   * Set the claimFormStep to which this notification is attached.
   * 
   * @param claimFormStep
   */
  public void setClaimFormStep( ClaimFormStep claimFormStep )
  {
    this.claimFormStep = claimFormStep;
  }

  /**
   * Checks this for equality against the object param. Overridden from
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( object instanceof ClaimFormStepEmailNotification )
    {
      ClaimFormStepEmailNotification claimFormStepEmailNotification = (ClaimFormStepEmailNotification)object;

      if ( claimFormStepEmailNotification.getClaimFormStep() != null && !claimFormStepEmailNotification.getClaimFormStep().equals( this.getClaimFormStep() ) )
      {
        equals = false;
      }

      if ( claimFormStepEmailNotification.getClaimFormStepEmailNotificationType() != null
          && !claimFormStepEmailNotification.getClaimFormStepEmailNotificationType().getCode().equals( this.getClaimFormStepEmailNotificationType().getCode() ) )
      {
        equals = false;
      }
    }

    return equals;
  }

  /**
   * Generate the hashcode for this object. Overridden from
   * 
   * @see java.lang.Object#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;

    result = this.getClaimFormStep() != null ? this.getClaimFormStep().hashCode() : 0;
    result += this.getClaimFormStepEmailNotificationType() != null ? this.getClaimFormStepEmailNotificationType().hashCode() * 13 : 0;

    return result;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {

    final StringBuffer buf = new StringBuffer();
    buf.append( "ClaimFormStepEmailNotification [" );
    buf.append( "{id=" + this.getId() + "}, " );
    buf.append( "{ClaimFormStepEmailNotificationType=" + this.getClaimFormStepEmailNotificationType() + "}, " );
    buf.append( "]" );

    return buf.toString();
  }

}
