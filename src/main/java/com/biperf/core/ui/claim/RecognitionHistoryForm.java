/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claim/RecognitionHistoryForm.java,v $
 */

package com.biperf.core.ui.claim;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.ui.BaseForm;

/*
 * RecognitionHistoryForm <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Jul
 * 15, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class RecognitionHistoryForm extends BaseForm
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The key to the HTTP request attribute that refers to this form.
   */
  public static final String FORM_NAME = "recognitionHistoryForm";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The promotion selected by the user. Used to filter the claim list on the Nomination/Recognition
   * History page.
   */
  private Long promotionId;

  /**
   * The start date specified by the user. Used to filter the claim list on the
   * Nomination/Recognition History page.
   */
  private String startDate;

  /**
   * The end date specified by the user. Used to filter the claim list on the Nomination/Recognition
   * History page.
   */
  private String endDate;

  /**
   * The ID of the participant who submitted or won the nomination or recognition.
   */
  private Long submitterId;

  /**
   * The ID of the user who submitted the claims in the history list. May be null.
   */
  private Long proxyUserId;

  /**
   * If <code>type</code> is "nomination," then the user is viewing nomination history. If
   * <code>type</code> is "recognition" or null, then the user is viewing recognition history.
   */
  private String promotionTypeCode;

  /**
   * If the <code>mode</code> is "sent," the the user is viewing nominations
   * submitted/recognitions sent. If the <code>mode</code> is "received," then the user is viewing
   * nominations won/recognitions received.
   */
  private String mode;

  private Long activityId;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getEndDate()
  {
    return endDate;
  }

  public String getMode()
  {
    return mode;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public Long getProxyUserId()
  {
    return proxyUserId;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public Long getSubmitterId()
  {
    return submitterId;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public void setMode( String mode )
  {
    this.mode = mode;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public void setProxyUserId( Long proxyUserId )
  {
    this.proxyUserId = proxyUserId;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public void setSubmitterId( Long submitterId )
  {
    this.submitterId = submitterId;
  }

  // ---------------------------------------------------------------------------
  // Predicate Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns true if the user is viewing the nomination history list; returns false otherwise.
   * 
   * @return true if the user is viewing the nomination history list; return false otherwise.
   */
  public boolean isNomination()
  {
    return promotionTypeCode != null && promotionTypeCode.equals( PromotionType.NOMINATION );
  }

  /**
   * Returns true if the claims in the history list were submitted by a proxy; returns false
   * otherwise.
   * 
   * @return true if the claims in the history list were submitted by a proxy; returns false
   *         otherwise.
   */
  public boolean isProxy()
  {
    return proxyUserId != null;
  }

  /**
   * Returns true if the user is viewing the recognition history list; returns false otherwise.
   * 
   * @return true if the user is viewing the recognition history list; return false otherwise.
   */
  public boolean isRecognition()
  {
    return promotionTypeCode == null || promotionTypeCode.equals( PromotionType.RECOGNITION );
  }

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

}
