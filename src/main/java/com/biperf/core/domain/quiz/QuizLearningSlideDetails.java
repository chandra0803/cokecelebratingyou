/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/quiz/QuizLearningObject.java,v $
 */

package com.biperf.core.domain.quiz;

import java.util.List;

/**
 * QuizQuestion.
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
 * <td>sharafud</td>
 * <td>Oct 10, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizLearningSlideDetails
{
  private int slideNumber;
  private Long quizFormId;
  private List<QuizLearningDetails> detailList;

  public QuizLearningSlideDetails()
  {
    super();
  }

  public int getSlideNumber()
  {
    return slideNumber;
  }

  public void setSlideNumber( int slideNumber )
  {
    this.slideNumber = slideNumber;
  }

  public List<QuizLearningDetails> getDetailList()
  {
    return detailList;
  }

  public void setDetailList( List<QuizLearningDetails> detailList )
  {
    this.detailList = detailList;
  }

  public Long getQuizFormId()
  {
    return quizFormId;
  }

  public void setQuizFormId( Long quizFormId )
  {
    this.quizFormId = quizFormId;
  }

}
