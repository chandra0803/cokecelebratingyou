/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/Form.java,v $
 */

package com.biperf.core.domain.claim;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.ClaimFormStatusType;

/**
 * Form.
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
 * <td>Oct 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class Form extends BaseDomain
{

  private String name;
  private String description;
  private ClaimFormStatusType claimFormStatusType;

  public ClaimFormStatusType getClaimFormStatusType()
  {
    return claimFormStatusType;
  }

  public void setClaimFormStatusType( ClaimFormStatusType claimFormStatusType )
  {
    this.claimFormStatusType = claimFormStatusType;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * Get form assigned or not
   * 
   * @return true if form is assigned; return false if in any other status
   */
  public boolean isAssigned()
  {
    if ( claimFormStatusType != null )
    {
      return claimFormStatusType.getCode().equals( ClaimFormStatusType.ASSIGNED );
    }

    return false;
  }

  /**
   * Get form complete or not
   * 
   * @return true if form is complete; return false if in any other status
   */
  public boolean isComplete()
  {
    if ( claimFormStatusType != null )
    {
      return claimFormStatusType.getCode().equals( ClaimFormStatusType.COMPLETED );
    }

    return false;
  }

  /**
   * Get form under construction or not
   * 
   * @return true if form is under construction; return false if in any other status
   */
  public boolean isUnderConstruction()
  {
    if ( claimFormStatusType != null )
    {
      return claimFormStatusType.getCode().equals( ClaimFormStatusType.UNDER_CONSTRUCTION );
    }

    return false;
  }

  /**
   * Overridden from
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

    if ( ! ( object instanceof Form ) )
    {
      return false;
    }
    final Form form = (Form)object;

    if ( getName() != null ? !getName().equals( form.getName() ) : form.getName() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return getName() != null ? getName().hashCode() : 0;
  } // end hashCode

}
