/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/process/ProcessInvocationComment.java,v $
 */

package com.biperf.core.domain.process;

import com.biperf.core.domain.BaseDomain;

/**
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
 * <td>Nov 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessInvocationComment extends BaseDomain
{
  /**
   * "s" added to avoid hibernate issue, I think...
   */
  private String comments;

  private ProcessInvocation processInvocation;

  public ProcessInvocationComment()
  {
    super();
  }

  /**
   * @param comment
   */
  public ProcessInvocationComment( String comment )
  {
    this.comments = comment;
  }

  /**
   * @return value of comment property
   */
  public String getComments()
  {
    return comments;
  }

  /**
   * @param comments value for comment property
   */
  public void setComments( String comments )
  {
    this.comments = comments;
  }

  /**
   * @return value of processInvocation property
   */
  public ProcessInvocation getProcessInvocation()
  {
    return processInvocation;
  }

  /**
   * @param processInvocation value for processInvocation property
   */
  public void setProcessInvocation( ProcessInvocation processInvocation )
  {
    this.processInvocation = processInvocation;
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @param object
   * @return boolean
   * @see com.biperf.core.domain.BaseDomain#equals(Object)
   */
  public boolean equals( Object object )
  {
    // TODO: what's business key, might not matter.
    if ( this == object )
    {
      return true;
    }
    else if ( object instanceof ProcessInvocationComment )
    {
      ProcessInvocationComment castObj = (ProcessInvocationComment)object;
      if ( comments.equals( castObj.getComments() ) && processInvocation.equals( castObj.getProcessInvocation() ) )
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @return int
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   */
  public int hashCode()
  {
    // TODO: what's business key, might not matter.
    return 0;
  }

}
