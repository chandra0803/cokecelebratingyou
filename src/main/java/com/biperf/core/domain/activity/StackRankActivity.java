/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.activity;

import com.biperf.core.domain.promotion.StackRankParticipant;

/*
 * StackRankActivity <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar 10, 2006</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class StackRankActivity extends Activity
{
  /**
   * Information about a stack rank participant and his or her stack rank.
   */
  private StackRankParticipant stackRankParticipant;

  /**
   * Constructs a <code>StackRankActivity</code> object.
   */
  public StackRankActivity()
  {
    // empty constructor
  }

  /**
   * Constructs a <code>StackRankActivity</code> object.
   * 
   * @param guid this object's unique business identifier.
   */
  public StackRankActivity( String guid )
  {
    super( guid );
  }

  public StackRankParticipant getStackRankParticipant()
  {
    return stackRankParticipant;
  }

  public void setStackRankParticipant( StackRankParticipant stackRankParticipant )
  {
    this.stackRankParticipant = stackRankParticipant;
  }
}
