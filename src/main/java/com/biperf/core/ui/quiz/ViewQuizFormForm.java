/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/quiz/ViewQuizFormForm.java,v $
 */

package com.biperf.core.ui.quiz;

import java.util.List;

import com.biperf.core.domain.quiz.Quiz;

/**
 * ViewQuizFormForm.
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
public class ViewQuizFormForm extends QuizFormForm
{
  private String quizQuestionId;
  private String newQuestionSequenceNum;
  private List questions;
  private List quizLearningObjects;
  private String[] deleteIds;

  /**
   * Load the form
   * 
   * @param quiz
   */
  public void load( Quiz quiz )
  {
    super.load( quiz );
    this.questions = quiz.getQuizQuestions();

  }

  public void loadLearningObjects( List quizLearningDetails )
  {
    this.quizLearningObjects = quizLearningDetails;

  }

  public List getQuestions()
  {
    return questions;
  }

  public void setQuestions( List questions )
  {
    this.questions = questions;
  }

  public int getQuestionsSize()
  {
    int size = 0;
    if ( questions != null )
    {
      size = questions.size();
    }
    return size;
  }

  public String getNewQuestionSequenceNum()
  {
    return newQuestionSequenceNum;
  }

  public void setNewQuestionSequenceNum( String newQuestionSequenceNum )
  {
    this.newQuestionSequenceNum = newQuestionSequenceNum;
  }

  public String getQuizQuestionId()
  {
    return quizQuestionId;
  }

  public void setQuizQuestionId( String quizQuestionId )
  {
    this.quizQuestionId = quizQuestionId;
  }

  public String[] getDeleteIds()
  {
    return deleteIds;
  }

  public void setDeleteIds( String[] deleteIds )
  {
    this.deleteIds = deleteIds;
  }

  public List getQuizLearningObjects()
  {
    return quizLearningObjects;
  }

  public void setQuizLearningObjects( List quizLearningObjects )
  {
    this.quizLearningObjects = quizLearningObjects;
  }

}
