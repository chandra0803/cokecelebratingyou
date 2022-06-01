/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/quiz/QuizLearningObject.java,v $
 */

package com.biperf.core.domain.quiz;

import com.biperf.core.domain.BaseDomain;

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
 * <td>Oct 2, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizLearningObject extends BaseDomain
{

  private Quiz quiz;
  private String status;
  private String contentResourceCMCode = "";
  private int slideNumber;

  public static final String ACTIVE_STATUS = "A";
  public static final String INACTIVE_STATUS = "I";
  public static final String QUIZ_SECTION_CODE = "quiz_learning_data";
  public static final String QUIZ_LEARNING_CMASSET_TYPE_NAME = "Quiz Learning Type";
  public static final String QUIZ_LEARNING_CMASSET_LEFT_KEY = "LEFT_COLUMN";
  public static final String QUIZ_LEARNING_CMASSET_RIGHT_KEY = "RIGHT_COLUMN";
  public static final String QUIZ_LEARNING_CMASSET_FILE_PATH_KEY = "FILE_PATH";
  public static final String QUIZ_LEARNING_CMASSET_VIDEO_MP4_KEY = "VIDEO_MP4_URL";
  public static final String QUIZ_LEARNING_CMASSET_VIDEO_WEBM_KEY = "VIDEO_WEBM_URL";
  public static final String QUIZ_LEARNING_CMASSET_VIDEO_3GP_KEY = "VIDEO_3GP_URL";
  public static final String QUIZ_LEARNING_CMASSET_VIDEO_OGG_KEY = "VIDEO_OGG_URL";
  public static final String QUIZ_LEARNING_CMASSET_NAME = "Quiz Learning Slides Text";
  public static final String QUIZ_LEARNING_CMASSET_PREFIX = "quiz_learning_data.learning";

  public QuizLearningObject()
  {
    super();
  }

  public Quiz getQuiz()
  {
    return quiz;
  }

  public void setQuiz( Quiz quiz )
  {
    this.quiz = quiz;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getContentResourceCMCode()
  {
    return contentResourceCMCode;
  }

  public void setContentResourceCMCode( String contentResourceCMCode )
  {
    this.contentResourceCMCode = contentResourceCMCode;
  }

  public int getSlideNumber()
  {
    return slideNumber;
  }

  public void setSlideNumber( int slideNumber )
  {
    this.slideNumber = slideNumber;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( object == null )
    {
      return false;
    }
    if ( getClass() != object.getClass() )
    {
      return false;
    }
    QuizLearningObject other = (QuizLearningObject)object;
    if ( quiz == null )
    {
      if ( other.quiz != null )
      {
        return false;
      }
    }
    else if ( !quiz.equals( other.quiz ) )
    {
      return false;
    }
    if ( contentResourceCMCode == null )
    {
      if ( other.contentResourceCMCode != null )
      {
        return false;
      }
    }
    else if ( !contentResourceCMCode.equals( other.contentResourceCMCode ) )
    {
      return false;
    }
    if ( status == null )
    {
      if ( other.status != null )
      {
        return false;
      }
    }
    else if ( !status.equals( other.status ) )
    {
      return false;
    }
    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( quiz == null ? 0 : quiz.hashCode() );
    result = prime * result + ( contentResourceCMCode == null ? 0 : contentResourceCMCode.hashCode() );
    result = prime * result + ( status == null ? 0 : status.hashCode() );
    return result;
  }

  /**
   * Builds a String representation of this.
   * 
   * @return String
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();

    sb.append( "[QUIZLearningObject {" );
    sb.append( "quiz.id - " + this.quiz.getId() + ", " );
    sb.append( "ContentResourceCode - " + this.contentResourceCMCode + ", " );
    sb.append( "status - " + this.status + ", " );
    sb.append( "Slide Number - " + this.slideNumber + ", " );
    sb.append( "}]" );
    return sb.toString();
  }

}
