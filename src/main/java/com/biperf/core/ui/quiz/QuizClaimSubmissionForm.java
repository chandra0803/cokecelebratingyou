/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/quiz/QuizClaimSubmissionForm.java,v $
 */

package com.biperf.core.ui.quiz;

import com.biperf.core.ui.BaseForm;

/**
 * <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Jun 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizClaimSubmissionForm extends BaseForm
{
  public static final String FORM_NAME = "quizClaimSubmissionForm";

  private String method;
  private String answerIndex;
  private String claimSubmissionReturnUrl;
  private String promotionId;
  private String claimId;
  private String selectedNode;
  private String data;
  // newly added
  private String selectedAnswer;

  /**
   * @return value of claimId property
   */
  public String getClaimId()
  {
    return claimId;
  }

  /**
   * @param claimId value for claimId property
   */
  public void setClaimId( String claimId )
  {
    this.claimId = claimId;
  }

  /**
   * @return value of promotionId property
   */
  public String getPromotionId()
  {
    return promotionId;
  }

  /**
   * @param promotionId value for promotionId property
   */
  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * @return value of answerIndex property
   */
  public String getAnswerIndex()
  {
    return answerIndex;
  }

  /**
   * @param answerIndex value for answer property
   */
  public void setAnswerIndex( String answerIndex )
  {
    this.answerIndex = answerIndex;
  }

  /**
   * @return value of claimSubmissionReturnUrl property
   */
  public String getClaimSubmissionReturnUrl()
  {
    return claimSubmissionReturnUrl;
  }

  /**
   * @param claimSubmissionReturnUrl value for claimSubmissionReturnUrl property
   */
  public void setClaimSubmissionReturnUrl( String claimSubmissionReturnUrl )
  {
    this.claimSubmissionReturnUrl = claimSubmissionReturnUrl;
  }

  /**
   * @return value of method property
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * @param method value for method property
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * @return value of selectedNode property
   */
  public String getSelectedNode()
  {
    return selectedNode;
  }

  /**
   * @param selectedNode value for selectedNode property
   */
  public void setSelectedNode( String selectedNode )
  {
    this.selectedNode = selectedNode;
  }

  public void setData( String data )
  {
    this.data = data;
  }

  public String getData()
  {
    return data;
  }

  public String getSelectedAnswer()
  {
    return selectedAnswer;
  }

  public void setSelectedAnswer( String selectedAnswer )
  {
    this.selectedAnswer = selectedAnswer;
  }

}
