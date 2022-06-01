
package com.biperf.core.domain.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.claim.Form;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SurveyType;
import com.biperf.core.domain.goalquest.PromotionGoalQuestSurvey;
import com.biperf.core.utils.DateUtils;

public class Survey extends Form
{
  public static final String CM_QUESTION_ASSET_PREFIX = "survey_question_data.question.";
  public static final String CM_QUESTION_ASSET_TYPE = "_SurveyQuestionData";
  public static final String CM_QUESTION_SECTION = "survey_question_data";
  public static final String CM_QUESTION_NAME_KEY = "QUESTION_NAME";
  public static final String CM_QUESTION_NAME_KEY_DESC = "Question Name";

  public static final String CM_QUESTION_RESPONSE_ASSET_PREFIX = "survey_question_response.response";
  public static final String CM_QUESTION_RESPONSE_ASSET_TYPE = "_SurveyQuestionResponse";
  public static final String CM_QUESTION_RESPONSE_SECTION = "survey_question_response";
  public static final String CM_QUESTION_RESPONSE_KEY = "QUESTION_RESPONSE";
  public static final String CM_QUESTION_RESPONSE_KEY_DESC = "Question Response";

  private List<SurveyQuestion> surveyQuestions = new ArrayList<SurveyQuestion>();

  private SurveyType surveyType;
  private int promotionCount = 0;
  private Set<SurveyPromotion> promotions = new LinkedHashSet<SurveyPromotion>();
  private int gqPromotionCount = 0;
  private Set<PromotionGoalQuestSurvey> gqPromotions = new LinkedHashSet<PromotionGoalQuestSurvey>();
  private PromotionType promotionModuleType;

  public Survey()
  {
    super();
  }

  /**
   * Checks if the survey is at a status that can be deleted.
   * 
   * @return boolean - Returns true if the survey is "Under Construction" or "Completed" with no links
   *         to expired promotions, and false otherwise.
   */
  public boolean isDeleteable()
  {
    // first make sure there is a status type available to check
    if ( getClaimFormStatusType() == null )
    {
      return false;
    }

    if ( getClaimFormStatusType().isUnderConstruction() || getClaimFormStatusType().isCompleted() && getPromotions().isEmpty() && getGqPromotions().isEmpty() )
    {
      return true;
    }

    return false;
  }

  /**
   * Does a deep copy of the Survey and its children if specified. This is a customized implementation
   * of java.lang.Object#clone()
   * 
   * @see java.lang.Object#clone()
   * @param cloneWithChildren
   * @param newSurveyName
   * @return Object
   */
  public Object deepCopy( boolean cloneWithChildren, String newSurveyName, String surveyFormType )
  {
    Survey survey = new Survey();
    survey.setName( newSurveyName );
    survey.setDescription( this.getDescription() );
    survey.setClaimFormStatusType( ClaimFormStatusType.lookup( "undrconstr" ) );
    survey.setSurveyType( this.getSurveyType() );
    survey.setPromotionModuleType( PromotionType.lookup( surveyFormType ) );

    if ( cloneWithChildren )
    {
      Iterator iter = this.getSurveyQuestions().iterator();
      while ( iter.hasNext() )
      {
        SurveyQuestion questionToCopy = (SurveyQuestion)iter.next();

        survey.addSurveyQuestion( (SurveyQuestion)questionToCopy.deepCopy( true ) );
      }
    }
    else
    {
      survey.setSurveyQuestions( new ArrayList() );
    }
    return survey;
  }

  /**
   * Return all active questions.
   */
  public List<SurveyQuestion> getActiveQuestions()
  {
    ArrayList<SurveyQuestion> activeQuestions = new ArrayList<SurveyQuestion>();

    for ( Iterator iter = surveyQuestions.iterator(); iter.hasNext(); )
    {
      SurveyQuestion surveyQuestion = (SurveyQuestion)iter.next();
      if ( surveyQuestion.isActive() )
      {
        activeQuestions.add( surveyQuestion );
      }
    }

    return activeQuestions;
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
    sb.append( "[SUREVEY {" );
    sb.append( "name: " + this.getName() + ", " );
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

  public List<SurveyQuestion> getSurveyQuestions()
  {
    return surveyQuestions;
  }

  public void setSurveyQuestions( List<SurveyQuestion> surveyQuestions )
  {
    this.surveyQuestions = surveyQuestions;
  }

  public Set<SurveyPromotion> getPromotions()
  {
    return promotions;
  }

  public void setPromotions( Set<SurveyPromotion> promotions )
  {
    this.promotions = promotions;
    if ( promotions != null )
    {
      this.promotionCount = promotions.size();
    }
  }

  public Set<PromotionGoalQuestSurvey> getGqPromotions()
  {
    return gqPromotions;
  }

  public void setGqPromotions( Set<PromotionGoalQuestSurvey> promotions )
  {
    this.gqPromotions = promotions;
    if ( promotions != null )
    {
      this.gqPromotionCount = promotions.size();
    }
  }

  public void addSurveyQuestion( SurveyQuestion surveyQuestion )
  {
    surveyQuestion.setSurvey( this );
    this.surveyQuestions.add( surveyQuestion );
  }

  public SurveyType getSurveyType()
  {
    return surveyType;
  }

  public void setSurveyType( SurveyType surveyType )
  {
    this.surveyType = surveyType;
  }

  public String getDisplayLastUpdatedDate()
  {
    return DateUtils.toDisplayString( this.getAuditUpdateInfo().getDateModified() );
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

  public PromotionType getPromotionModuleType()
  {
    return promotionModuleType;
  }

  public void setPromotionModuleType( PromotionType promotionModuleType )
  {
    this.promotionModuleType = promotionModuleType;
  }

  public int getGqPromotionCount()
  {
    return gqPromotionCount;
  }

  public void setGqPromotionCount( int gqPromotionCount )
  {
    this.gqPromotionCount = gqPromotionCount;
  }

}
