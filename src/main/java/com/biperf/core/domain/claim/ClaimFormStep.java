/*
 (c) 2005 BI, Inc.  All rights reserved.
 File: ClaimFormStep.java
 
 Change History:

 Author       Date      Version  Comments
 -----------  --------  -------  -----------------------------
 crosenquest      Jun 2, 2005   1.0      Created
 
 */

package com.biperf.core.domain.claim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author crosenquest
 */
public class ClaimFormStep extends FormStep
{
  public static final String CM_CLAIM_FORM_STEP_NAME_KEY_PREFIX = "CLAIM_FORM_STEP_NAME_";
  public static final String CM_CLAIM_FORM_STEP_NAME_KEY_DESC_SUFFIX = " Name";

  private boolean salesRequired = false;
  private List claimFormStepElements = new ArrayList();
  private Set claimFormStepEmailNotifications = new LinkedHashSet();

  /**
   * Empty Contructor
   */
  public ClaimFormStep()
  {
    super();
  }

  /**
   * Copy Constructor
   * 
   * @param claimFormStepToCopy
   * @throws CloneNotSupportedException
   */
  public ClaimFormStep( ClaimFormStep claimFormStepToCopy ) throws CloneNotSupportedException
  {
    this.setCmKeyFragment( claimFormStepToCopy.getCmKeyFragment() );

    // TODO - Name out of CM
    // OLD WAY - this.setName( claimFormStepToCopy.getName() );
    // ?? New Way?? - How do we copy CM data?
    // Do we want the fragment?

    this.setSalesRequired( claimFormStepToCopy.isSalesRequired() );

    Iterator iterEmail = claimFormStepToCopy.getClaimFormStepEmailNotifications().iterator();
    while ( iterEmail.hasNext() )
    {
      this.addClaimFormStepEmailNotification( new ClaimFormStepEmailNotification( (ClaimFormStepEmailNotification)iterEmail.next(), this ) );
    }

    Iterator iter = claimFormStepToCopy.getClaimFormStepElements().iterator();
    while ( iter.hasNext() )
    {

      ClaimFormStepElement copiedClaimFormStepElement = (ClaimFormStepElement) ( (ClaimFormStepElement)iter.next() ).clone();

      this.addClaimFormStepElement( copiedClaimFormStepElement );
    }
  }

  /**
   * Get the Set of ClaimFormStepEmailNotification.
   * 
   * @return Set
   */
  public Set getClaimFormStepEmailNotifications()
  {
    return this.claimFormStepEmailNotifications;
  }

  /**
   * Add a claimFormStepEmailNotification to this claimFormStep.
   * 
   * @param claimFormStepEmailNotification
   */
  public void addClaimFormStepEmailNotification( ClaimFormStepEmailNotification claimFormStepEmailNotification )
  {
    claimFormStepEmailNotification.setClaimFormStep( this );
    this.claimFormStepEmailNotifications.add( claimFormStepEmailNotification );
  }

  /**
   * Set the set of emailNotifications onto this ClaimFormStep.
   * 
   * @param claimFormStepEmailNotifications
   */
  public void setClaimFormStepEmailNotifications( Set claimFormStepEmailNotifications )
  {
    this.claimFormStepEmailNotifications = claimFormStepEmailNotifications;
  }

  /**
   * Get the elements for this claimFormStep.
   * 
   * @return List
   */
  public List<ClaimFormStepElement> getClaimFormStepElements()
  {
    return this.claimFormStepElements;
  }

  /**
   * Return the number of elements for this claimFormStep.
   * 
   * @return int
   */
  public int getNumberOfClaimFormStepElements()
  {
    int size = 0;
    if ( this.claimFormStepElements != null )
    {
      size = this.claimFormStepElements.size();
    }
    return size;
  }

  /**
   * Set the list of Elements onto this claimFormStep.
   * 
   * @param claimFormStepElements
   */
  public void setClaimFormStepElements( List claimFormStepElements )
  {
    this.claimFormStepElements = claimFormStepElements;
  }

  /**
   * Add a new Element to this claimFormStep.
   * 
   * @param claimFormStepElement
   */
  public void addClaimFormStepElement( ClaimFormStepElement claimFormStepElement )
  {
    claimFormStepElement.setClaimFormStep( this );
    this.claimFormStepElements.add( claimFormStepElement );
  }

  /**
   * Get the claimForm from this claimFormStep.
   * 
   * @return ClaimForm
   */
  public ClaimForm getClaimForm()
  {
    return (ClaimForm)this.form;
  }

  /**
   * Set the claimForm for this ClaimFormStep.
   * 
   * @param claimForm
   */
  public void setClaimForm( Form claimForm )
  {
    this.form = claimForm;
  }

  /**
   * Set the sales required boolean flag.
   * 
   * @return boolean
   */
  public boolean isSalesRequired()
  {
    return this.salesRequired;
  }

  /**
   * Set the boolean value for the sales requirement.
   * 
   * @param salesRequired
   */
  public void setSalesRequired( boolean salesRequired )
  {
    this.salesRequired = salesRequired;
  }

  /**
   * Ensure equality between this and the object param.
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {

    boolean equals = true;

    if ( object instanceof ClaimFormStep )
    {
      ClaimFormStep claimFormStep = (ClaimFormStep)object;

      if ( claimFormStep.getId() != null && claimFormStep.getId().longValue() != this.getId().longValue() )
      {
        equals = false;
      }

      if ( claimFormStep.getCmKeyFragment() != null && !claimFormStep.getCmKeyFragment().equals( this.getCmKeyFragment() ) )
      {
        equals = false;
      }

      // if ( claimFormStep.getName() != null && !claimFormStep.getName().equals( this.getName() ) )
      // equals = false;

      if ( claimFormStep.getClaimForm() != null && !claimFormStep.getClaimForm().equals( this.getClaimForm() ) )
      {
        equals = false;
      }
    }

    return equals;

  }

  /**
   * Generated hashCode required by Hiberate for this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result = super.hashCode();

    result = this.getClaimForm() != null ? this.getClaimForm().hashCode() * 13 : 0;

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
    buf.append( "ClaimFormStep [" );
    buf.append( "{id=" + super.getId() + "}, " );
    buf.append( "{cmKeyFragment=" + this.cmKeyFragment + "}, " );
    buf.append( "{isSalesRequired=" + this.salesRequired + "}, " );
    buf.append( "]" );

    return buf.toString();
  }

  /**
   * Appends the Step Name key with the Key fragment
   * 
   * @return String - CM Key value of Name
   */
  public String getCmKeyForName()
  {
    return CM_CLAIM_FORM_STEP_NAME_KEY_PREFIX + this.getCmKeyFragment();
  }

}
