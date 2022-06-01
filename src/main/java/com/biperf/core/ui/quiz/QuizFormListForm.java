/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/quiz/QuizFormListForm.java,v $
 */

package com.biperf.core.ui.quiz;

import com.biperf.core.ui.BaseForm;

/**
 * QuizFormListForm transfer object.
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
 * <td>jenniget</td>
 * <td>Oct 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizFormListForm extends BaseForm
{
  private String method;
  private String quizFormId;
  private String[] deleteUnderConstructionIds;
  private String[] deleteCompletedIds;

  public String[] getDeleteCompletedIds()
  {
    return deleteCompletedIds;
  }

  public void setDeleteCompletedIds( String[] deleteCompletedIds )
  {
    this.deleteCompletedIds = deleteCompletedIds;
  }

  public String[] getDeleteUnderConstructionIds()
  {
    return deleteUnderConstructionIds;
  }

  public void setDeleteUnderConstructionIds( String[] deleteUnderConstructionIds )
  {
    this.deleteUnderConstructionIds = deleteUnderConstructionIds;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getQuizFormId()
  {
    return quizFormId;
  }

  public void setQuizFormId( String quizFormId )
  {
    this.quizFormId = quizFormId;
  }
}
