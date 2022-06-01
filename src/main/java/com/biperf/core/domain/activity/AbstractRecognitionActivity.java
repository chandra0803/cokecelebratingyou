/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/activity/AbstractRecognitionActivity.java,v $
 */

package com.biperf.core.domain.activity;

import com.biperf.core.domain.merchandise.MerchOrder;

/**
 * RecognitionActivity.
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
 * <td>zahler</td>
 * <td>Oct 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class AbstractRecognitionActivity extends Activity
{
  private boolean submitter;
  private Long awardQuantity;
  private MerchOrder merchOrder;

  public AbstractRecognitionActivity()
  {
    // empty constructor
  }

  public AbstractRecognitionActivity( String guid )
  {
    super( guid );
  }

  public Long getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( Long awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  public boolean isSubmitter()
  {
    return submitter;
  }

  public void setSubmitter( boolean submitter )
  {
    this.submitter = submitter;
  }

  public MerchOrder getMerchOrder()
  {
    return merchOrder;
  }

  public void setMerchOrder( MerchOrder merchOrder )
  {
    this.merchOrder = merchOrder;
  }

}
