/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/participant/ParticipantEmploymentListForm.java,v $
 *
 */

package com.biperf.core.ui.participant;

import com.biperf.core.ui.BaseForm;

/**
 * ParticipantEmploymentListForm <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>May 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class ParticipantEmploymentListForm extends BaseForm
{
  public static final String FORM_NAME = "participantEmploymentListForm";

  private long userId;

  public long getUserId()
  {
    return userId;
  }

  public void setUserId( long userId )
  {
    this.userId = userId;
  }
}
