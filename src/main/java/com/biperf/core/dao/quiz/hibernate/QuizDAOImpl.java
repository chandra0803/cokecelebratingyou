/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/quiz/hibernate/QuizDAOImpl.java,v $
 */

package com.biperf.core.dao.quiz.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.quiz.QuizDAO;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizLearningObject;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * QuizDAOImpl.
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
 */
public class QuizDAOImpl extends BaseDAO implements QuizDAO
{

  /**
   * Get a list of all quizzes. Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#getAll()
   * @return List
   */
  public List getAll()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.quiz.AllQuizList" ).list();
  }

  /**
   * Get the quiz by Id. Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#getQuizById(java.lang.Long)
   * @param quizId
   * @return Quiz
   */
  public Quiz getQuizById( Long quizId )
  {
    return (Quiz)getSession().get( Quiz.class, quizId );
  }

  /**
   * Get a Quiz with the proper associations loaded Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#getQuizByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @return Quiz
   */
  public Quiz getQuizByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Quiz quiz = (Quiz)getSession().get( Quiz.class, id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( quiz );
    }
    return quiz;
  }

  /**
   * Get quiz by name. Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#getQuizByName(java.lang.String)
   * @param name
   * @return Quiz
   */
  public Quiz getQuizByName( String name )
  {
    return (Quiz)getSession().getNamedQuery( "com.biperf.core.domain.quiz.QuizByName" ).setString( "name", name.toUpperCase() ).uniqueResult();
  }

  /**
   * Saves a quiz to the database. Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#saveQuiz(com.biperf.core.domain.quiz.Quiz)
   * @param quiz
   * @return Quiz
   */
  public Quiz saveQuiz( Quiz quiz )
  {
    getSession().save( quiz );

    return quiz;
  }

  /**
   * Update the quiz. Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#updateQuiz(com.biperf.core.domain.quiz.Quiz)
   * @param quiz
   * @return Quiz
   * @throws ConstraintViolationException
   */
  public Quiz updateQuiz( Quiz quiz ) throws ConstraintViolationException
  {

    quiz = (Quiz)HibernateUtil.saveOrUpdateOrShallowMerge( quiz );

    // Note: need to do the flush to get the Constraint Violation to bubble up.
    getSession().flush();

    return quiz;
  }

  /**
   * Deletes a quiz from the database. Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#deleteQuiz(com.biperf.core.domain.quiz.Quiz)
   * @param quiz
   */
  public void deleteQuiz( Quiz quiz )
  {
    getSession().delete( quiz );
  }

  /**
   * Get the quizQuestion by the Id param. Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#getQuizQuestionById(java.lang.Long)
   * @param quizQuestionId
   * @return QuizQuestion
   */
  public QuizQuestion getQuizQuestionById( Long quizQuestionId )
  {
    return (QuizQuestion)getSession().get( QuizQuestion.class, quizQuestionId );
  }

  /**
   * Get a list of quizQuestions by the quiz id to which they're associated. Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#getQuizQuestionsByQuizId(java.lang.Long)
   * @param quizId
   * @return List
   */
  public List getQuizQuestionsByQuizId( Long quizId )
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.quiz.QuizQuestionsByQuizId" ).setParameter( "quizId", quizId ).list();
  }

  /**
   * Get the quizQuestionAnswer by the id param Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#getQuizQuestionAnswerById(java.lang.Long)
   * @param quizQuestionAnswerId
   * @return QuizQuestionAnswer
   */
  public QuizQuestionAnswer getQuizQuestionAnswerById( Long quizQuestionAnswerId )
  {
    return (QuizQuestionAnswer)getSession().get( QuizQuestionAnswer.class, quizQuestionAnswerId );
  }

  /**
   * Get a list of quizQuestionAnswers which are associated to the quizQuestionId param. Overridden
   * from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#getQuizQuestionAnswersByQuizQuestionId(java.lang.Long)
   * @param quizQuestionId
   * @return List
   */
  public List getQuizQuestionAnswersByQuizQuestionId( Long quizQuestionId )
  {
    List answerList = getSession().getNamedQuery( "com.biperf.core.domain.quiz.QuizQuestionAnswerByQuizQuestionId" ).setParameter( "quizQuestionId", quizQuestionId ).list();
    return answerList;
  }

  /**
   * Delete the quizQuestionAnswer. Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#deleteQuizQuestionAnswer(java.lang.Long)
   * @param quizQuestionAnswerId
   */
  public void deleteQuizQuestionAnswer( Long quizQuestionAnswerId )
  {
    getSession().delete( getQuizQuestionAnswerById( quizQuestionAnswerId ) );
  }

  /**
   * Delete the quizAnswer. Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#deleteQuizQuestion(java.lang.Long)
   * @param quizQuestionId
   */
  public void deleteQuizQuestion( Long quizQuestionId )
  {
    getSession().delete( getQuizQuestionById( quizQuestionId ) );
  }

  /**
   * Update the quizQuestionAnswer. Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#updateQuizQuestionAnswer(com.biperf.core.domain.quiz.QuizQuestionAnswer)
   * @param quizQuestionAnswer
   * @return QuizQuestionAnswer
   */
  public QuizQuestionAnswer updateQuizQuestionAnswer( QuizQuestionAnswer quizQuestionAnswer )
  {
    return (QuizQuestionAnswer)HibernateUtil.saveOrUpdateOrShallowMerge( quizQuestionAnswer );
  }

  public QuizQuestion getQuizQuestionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    QuizQuestion quizQuestion = (QuizQuestion)getSession().get( QuizQuestion.class, id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( quizQuestion );
    }
    return quizQuestion;
  }

  /**
   * Gets a list of all complete or assigned quizzes. Overridden from
   * 
   * @see com.biperf.core.dao.quiz.QuizDAO#getAllCompletedAndAssignedQuizzes()
   * @return List
   */
  public List getAllCompletedAndAssignedQuizzes()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.quiz.AllCompletedAndAssignedQuizzes" ).list();
  }

  public QuizLearningObject saveQuizLearning( QuizLearningObject quizLearning )
  {
    getSession().saveOrUpdate( quizLearning );

    return quizLearning;
  }

  public void mergeQuizLearning( QuizLearningObject quizLearning )
  {
    getSession().merge( quizLearning );
  }

  public List<QuizLearningObject> getQuizLearningObjectById( Long id )
  {
    /*
     * List quizLearningSlideList = getSession().getNamedQuery(
     * "com.biperf.core.domain.quiz.QuizLearningObjectsByQuizId" ) .setParameter( "quizFormId", id
     * ).list(); return quizLearningSlideList;
     */

    Criteria crit = getSession().createCriteria( QuizLearningObject.class );
    crit.add( Restrictions.eq( "quiz.id", id ) );
    crit.add( Restrictions.eq( "status", QuizLearningObject.ACTIVE_STATUS ) );
    crit.addOrder( Order.asc( "slideNumber" ) );
    return crit.list();
  }

  public int getNextSlideIdForQuiz( Long id )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.quiz.getNextSlideNumber" );
    query.setParameter( "quizLibId", id );
    query.setParameter( "status", QuizLearningObject.ACTIVE_STATUS );
    Integer slideNumber = (Integer)query.uniqueResult();
    return slideNumber;
  }

  public QuizLearningObject getQuizLearningObjectForSlide( Long quizId, int slideNumber )
  {
    Criteria crit = getSession().createCriteria( QuizLearningObject.class );
    crit.add( Restrictions.eq( "quiz.id", quizId ) );
    crit.add( Restrictions.eq( "slideNumber", slideNumber ) );
    crit.add( Restrictions.eq( "status", QuizLearningObject.ACTIVE_STATUS ) );
    QuizLearningObject quizLearningObj = (QuizLearningObject)crit.list().get( 0 );
    return quizLearningObj;
  }

}
