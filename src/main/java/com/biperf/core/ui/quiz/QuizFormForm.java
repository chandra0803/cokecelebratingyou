/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/quiz/QuizFormForm.java,v $
 */

package com.biperf.core.ui.quiz;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.QuizType;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.utils.StringUtil;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * QuizFormForm.
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
public class QuizFormForm extends BaseActionForm
{
  private static final String COPY_QUIZ_NAME_ASSET_KEY = "quiz.form.NEW_NAME";
  private static final String QUIZ_NAME_ASSET_KEY = "quiz.form.NAME";
  private static final String QUIZ_TYPE_ASSET_KEY = "quiz.form.DISPLAY_METHOD";
  private static final String NUMBER_TO_ASK_ASSET_KEY = "quiz.form.NUMBER_TO_ASK";
  private static final String PASSING_SCORE_ASSET_KEY = "quiz.form.PASSING_SCORE";

  private String method;
  private Long quizFormId;
  private String quizFormName;
  private String copyQuizFormName;
  private String description;
  private String quizType;
  private String numberToAsk;
  private String passingScore;
  private String status;
  private boolean quizAssigned;
  private boolean active;
  private List quizLearningObjects;

  /**
   * Load the form
   * 
   * @param quiz
   */
  public void load( Quiz quiz )
  {
    if ( quiz != null )
    {
      this.numberToAsk = String.valueOf( quiz.getNumberOfQuestionsAsked() );
      this.passingScore = String.valueOf( quiz.getPassingScore() );
      this.quizFormId = quiz.getId();
      this.quizFormName = quiz.getName();
      this.description = quiz.getDescription() == null ? null : convertLineBreaks( quiz.getDescription() );
      this.status = quiz.getClaimFormStatusType().getCode();
      if ( quiz.getQuizType() != null )
      {
        this.quizType = quiz.getQuizType().getName().toLowerCase();
      }
      this.quizAssigned = quiz.isAssigned();
    }
  }

  public void loadLearningObjects( List quizLearningDetails )
  {
    this.quizLearningObjects = quizLearningDetails;
  }

  private String convertLineBreaks( String text )
  {
    return StringUtil.convertLineBreaks( text );
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return Quiz
   */
  public Quiz toDomainObject()
  {
    Quiz quiz = new Quiz();
    quiz.setId( this.quizFormId );
    quiz.setName( this.quizFormName );
    quiz.setDescription( this.description );
    ClaimFormStatusType statusType = null;
    if ( this.status != null )
    {
      statusType = ClaimFormStatusType.lookup( this.status );
    }
    if ( statusType == null )
    {
      statusType = ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ); // default
      // status
    }
    quiz.setClaimFormStatusType( statusType );
    if ( quizType.equals( "random" ) )
    {
      if ( this.numberToAsk != null && !this.numberToAsk.equals( "" ) )
      {
        quiz.setNumberOfQuestionsAsked( Integer.parseInt( this.numberToAsk ) );
      }
    }
    else
    {
      quiz.setNumberOfQuestionsAsked( 0 );
    }
    quiz.setPassingScore( Integer.parseInt( this.passingScore ) );
    quiz.setQuizType( QuizType.lookup( this.quizType ) );
    return quiz;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( method != null )
    {
      if ( method.equals( "copy" ) )
      {
        if ( StringUtils.isEmpty( copyQuizFormName ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( COPY_QUIZ_NAME_ASSET_KEY ) ) );
        }
      }
      else if ( method.equals( "save" ) )
      {
        if ( StringUtils.isEmpty( quizFormName ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( QUIZ_NAME_ASSET_KEY ) ) );
        }
        if ( StringUtils.isEmpty( quizType ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( QUIZ_TYPE_ASSET_KEY ) ) );
        }
        else
        {
          int passingScoreValue = 0;

          if ( passingScore == null )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( PASSING_SCORE_ASSET_KEY ) ) );
          }
          else
          {
            try
            {
              passingScoreValue = Integer.parseInt( passingScore );
              if ( passingScoreValue <= 0 )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.SUB_ZERO", CmsResourceBundle.getCmsBundle().getString( PASSING_SCORE_ASSET_KEY ) ) );
              }
            }
            catch( NumberFormatException e )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.NONNUMERIC", CmsResourceBundle.getCmsBundle().getString( PASSING_SCORE_ASSET_KEY ) ) );
            }
          }
          if ( quizType.equals( QuizType.RANDOM ) )
          {
            if ( StringUtils.isEmpty( numberToAsk ) )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( NUMBER_TO_ASK_ASSET_KEY ) ) );
            }
            else
            {
              int value = 0;
              try
              {
                value = Integer.parseInt( numberToAsk );
                if ( value <= 0 )
                {
                  actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.notification.errors.SUB_ZERO", CmsResourceBundle.getCmsBundle().getString( NUMBER_TO_ASK_ASSET_KEY ) ) );
                }
              }
              catch( NumberFormatException e )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.NONNUMERIC", CmsResourceBundle.getCmsBundle().getString( NUMBER_TO_ASK_ASSET_KEY ) ) );
              }

              if ( passingScoreValue > value )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "quiz.errors.PASSING_SCORE_TOO_HIGH", CmsResourceBundle.getCmsBundle().getString( PASSING_SCORE_ASSET_KEY ) ) );
              }
            }
          }
        }
      }
    }

    return actionErrors;
  }

  public String getQuizType()
  {
    return quizType;
  }

  public void setQuizType( String quizType )
  {
    this.quizType = quizType;
  }

  public String getNumberToAsk()
  {
    return numberToAsk;
  }

  public void setNumberToAsk( String numberToAsk )
  {
    this.numberToAsk = numberToAsk;
  }

  public String getPassingScore()
  {
    return passingScore;
  }

  public void setPassingScore( String passingScore )
  {
    this.passingScore = passingScore;
  }

  public String getQuizFormName()
  {
    return quizFormName;
  }

  public void setQuizFormName( String quizFormName )
  {
    this.quizFormName = quizFormName;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getQuizFormId()
  {
    return quizFormId;
  }

  public void setQuizFormId( Long quizFormId )
  {
    this.quizFormId = quizFormId;
  }

  public String getCopyQuizFormName()
  {
    return copyQuizFormName;
  }

  public void setCopyQuizFormName( String copyQuizFormName )
  {
    this.copyQuizFormName = copyQuizFormName;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public boolean isQuizAssigned()
  {
    return quizAssigned;
  }

  public void setQuizAssigned( boolean quizLive )
  {
    this.quizAssigned = quizLive;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

}
