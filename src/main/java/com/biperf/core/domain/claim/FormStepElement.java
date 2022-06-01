/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/FormStepElement.java,v $
 */

package com.biperf.core.domain.claim;

import com.biperf.core.domain.BaseDomain;

/**
 * FormStepElement.
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
public abstract class FormStepElement extends BaseDomain
{
  protected String cmKeyFragment;
  protected String description;
  protected boolean required = false;

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public boolean isRequired()
  {
    return required;
  }

  public void setRequired( boolean required )
  {
    this.required = required;
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
    else
    {
      if ( object instanceof FormStepElement )
      {
        FormStepElement castObject = (FormStepElement)object;
        if ( cmKeyFragment.equals( castObject.cmKeyFragment ) && description.equals( castObject.description ) && required == castObject.required )
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return 0;
  }

}
