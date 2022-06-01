/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/quiz/Quiz.java,v $
 */

package com.biperf.core.domain.quiz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.claim.Form;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.QuizType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.utils.DateUtils;

/**
 * Quiz.
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
 * <td>crosenquest</td>
 * <td>Oct 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Quiz extends Form
{

  public static final String CM_QUESTION_ASSET_PREFIX = "quiz_question_data.question.";
  public static final String CM_QUESTION_ASSET_TYPE = "_QuizQuestionData";
  public static final String CM_QUESTION_SECTION = "quiz_question_data";
  public static final String CM_QUESTION_NAME_KEY = "QUESTION_NAME";
  public static final String CM_QUESTION_NAME_KEY_DESC = "Question Name";

  public static final String CM_QUESTION_ANSWER_ASSET_PREFIX = "quiz_question_answer.answer";
  public static final String CM_QUESTION_ANSWER_ASSET_TYPE = "_QuizQuestionAnswer";
  public static final String CM_QUESTION_ANSWER_SECTION = "quiz_question_answer";
  public static final String CM_QUESTION_ANSWER_KEY = "QUESTION_ANSWER";
  public static final String CM_QUESTION_ANSWER_KEY_DESC = "Question Answer";
  public static final String CM_QUESTION_ANSWER_EXPLANATION_KEY = "QUESTION_ANSWER_EXPLANATION";
  public static final String CM_QUESTION_ANSWER_EXPLANATION_KEY_DESC = "Question Answer Explanation";

  private int numberOfQuestionsAsked;
  private int passingScore;
  private List<QuizQuestion> quizQuestions = new ArrayList<QuizQuestion>();
  private Set promotions = new LinkedHashSet();
  private Set<QuizLearningObject> learningObjects = new LinkedHashSet<QuizLearningObject>();
  private int promotionCount = 0;
  private Set<QuizClaim> quizClaims = new LinkedHashSet<QuizClaim>();

  private QuizType quizType;

  public Quiz()
  {
    super();
  }

  public QuizType getQuizType()
  {
    return this.quizType;
  }

  public void setQuizType( QuizType quizType )
  {
    this.quizType = quizType;
  }

  /**
   * Return actual number of questions to be asked, regardless of quiz type.
   */
  public int getActualNumberOfQuestionsAsked()
  {
    int actual;
    if ( quizType.getCode().equals( QuizType.FIXED ) )
    {
      actual = getActiveQuestions().size();
    }
    else
    {
      // random
      actual = numberOfQuestionsAsked;
    }

    return actual;
  }

  /**
   * NOTE: Only represents number of questions asked for random quizes: For actual number of
   * questions asked for either quiz type, use {@link #getActualNumberOfQuestionsAsked()}
   */
  public int getNumberOfQuestionsAsked()
  {
    return numberOfQuestionsAsked;
  }

  public void setNumberOfQuestionsAsked( int numberOfQuestionsAsked )
  {
    this.numberOfQuestionsAsked = numberOfQuestionsAsked;
  }

  public int getPassingScore()
  {
    return passingScore;
  }

  public void setPassingScore( int passingScore )
  {
    this.passingScore = passingScore;
  }

  public List<QuizQuestion> getQuizQuestions()
  {
    return this.quizQuestions;
  }

  public void setQuizQuestions( List<QuizQuestion> quizQuestions )
  {
    this.quizQuestions = quizQuestions;
  }

  public Set getPromotions()
  {
    return promotions;
  }

  public void setPromotions( Set promotions )
  {
    this.promotions = promotions;
    if ( promotions != null )
    {
      this.promotionCount = promotions.size();
    }
  }

  /**
   * Add a question to this quiz.
   * 
   * @param quizQuestion
   */
  public void addQuizQuestion( QuizQuestion quizQuestion )
  {
    quizQuestion.setQuiz( this );
    this.quizQuestions.add( quizQuestion );
  }

  /**
   * Add a learning object to this quiz.
   * 
   * @param quizQuestion
   */
  public void addQuizLearningObject( QuizLearningObject quizLearningObject )
  {
    quizLearningObject.setQuiz( this );
    this.learningObjects.add( quizLearningObject );
  }

  /**
   * Checks if the quiz is at a status that can be deleted.
   * 
   * @return boolean - Returns true if the quiz is "Under Construction" or "Completed" with no links
   *         to expired promotions, and false otherwise.
   */
  public boolean isDeleteable()
  {
    // first make sure there is a status type available to check
    if ( getClaimFormStatusType() == null )
    {
      return false;
    }

    if ( getClaimFormStatusType().isUnderConstruction() || getClaimFormStatusType().isCompleted() && getPromotions().isEmpty() )
    {
      return true;
    }

    return false;
  }

  /**
   * Does a deep copy of the Quiz and its children if specified. This is a customized implementation
   * of java.lang.Object#clone()
   * 
   * @see java.lang.Object#clone()
   * @param cloneWithChildren
   * @param newQuizName
   * @return Object
   * @throws ServiceErrorException 
   */
  public Object deepCopy( boolean cloneWithChildren, String newQuizName ) throws ServiceErrorException
  {
    Quiz quiz = new Quiz();
    quiz.setName( newQuizName );
    quiz.setDescription( this.getDescription() );
    quiz.setClaimFormStatusType( ClaimFormStatusType.lookup( "undrconstr" ) );
    quiz.setNumberOfQuestionsAsked( this.getNumberOfQuestionsAsked() );
    quiz.setPassingScore( this.getPassingScore() );
    quiz.setQuizType( this.getQuizType() );
    quiz.setPromotions( new HashSet() );

    if ( cloneWithChildren )
    {
      Iterator iter = this.getQuizQuestions().iterator();
      while ( iter.hasNext() )
      {
        QuizQuestion questionToCopy = (QuizQuestion)iter.next();

        quiz.addQuizQuestion( (QuizQuestion)questionToCopy.deepCopy( true ) );
      }
    }
    else
    {
      quiz.setQuizQuestions( new ArrayList() );
    }
    return quiz;
  }

  /**
   * Builds a String Representation of this. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append( super.toString() + "-" );
    sb.append( "[QUIZ {" );
    sb.append( "name: " + this.getName() + ", " );
    sb.append( "numberOfQuestionsAsked: " + this.numberOfQuestionsAsked + ", " );
    sb.append( "passingScore: " + this.passingScore );
    sb.append( "}]" );
    return sb.toString();
  }

  /**
   * Check equality with this and the object param. Overridden from
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    return super.equals( object );
  }

  /**
   * Return all required active questions for this quiz.
   */
  public List getRequiredActiveQuestions()
  {
    ArrayList requiredActiveQuestions = new ArrayList();
    for ( Iterator iter = quizQuestions.iterator(); iter.hasNext(); )
    {
      QuizQuestion quizQuestion = (QuizQuestion)iter.next();
      if ( quizQuestion.isRequired() && quizQuestion.isActive() )
      {
        requiredActiveQuestions.add( quizQuestion );
      }

    }
    return requiredActiveQuestions;
  }

  /**
   * Return all optional active questions for this quiz.
   */
  public List getOptionalActiveQuestions()
  {
    ArrayList optionalActiveQuestions = new ArrayList();
    for ( Iterator iter = quizQuestions.iterator(); iter.hasNext(); )
    {
      QuizQuestion quizQuestion = (QuizQuestion)iter.next();
      if ( !quizQuestion.isRequired() && quizQuestion.isActive() )
      {
        optionalActiveQuestions.add( quizQuestion );
      }

    }
    return optionalActiveQuestions;
  }

  /**
   * Return all active questions.
   */
  public List getActiveQuestions()
  {
    ArrayList activeQuestions = new ArrayList();

    for ( Iterator iter = quizQuestions.iterator(); iter.hasNext(); )
    {
      QuizQuestion quizQuestion = (QuizQuestion)iter.next();
      if ( quizQuestion.isActive() )
      {
        activeQuestions.add( quizQuestion );
      }

    }

    return activeQuestions;
  }

  public int getPromotionCount()
  {
    if ( promotionCount == 0 && promotions != null )
    {
      promotionCount = promotions.size();
    }
    return promotionCount;
  }

  public void setPromotionCount( int promotionCount )
  {
    this.promotionCount = promotionCount;
  }

  public Set<QuizLearningObject> getLearningObjects()
  {
    return learningObjects;
  }

  public void setLearningObjects( Set<QuizLearningObject> learningObjects )
  {
    this.learningObjects = learningObjects;
  }

  public Set<QuizClaim> getQuizClaims()
  {
    return quizClaims;
  }

  public void setQuizClaims( Set<QuizClaim> quizClaims )
  {
    this.quizClaims = quizClaims;
  }

  public String getDisplayLastUpdatedDate()
  {
    return DateUtils.toDisplayString( this.getAuditUpdateInfo().getDateModified() );
  }
}
