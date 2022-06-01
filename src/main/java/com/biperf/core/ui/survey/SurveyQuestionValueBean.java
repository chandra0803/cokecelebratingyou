/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/goalquest/SurveyQuestionValueBean.java,v $
 */

package com.biperf.core.ui.survey;

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
public class SurveyQuestionValueBean extends BaseForm
{
  private String questionId;
  private String answerIndex;

  public String getQuestionId()
  {
    return questionId;
  }

  public void setQuestionId( String questionId )
  {
    this.questionId = questionId;
  }

  public String getAnswerIndex()
  {
    return answerIndex;
  }

  public void setAnswerIndex( String answerIndex )
  {
    this.answerIndex = answerIndex;
  }

}
