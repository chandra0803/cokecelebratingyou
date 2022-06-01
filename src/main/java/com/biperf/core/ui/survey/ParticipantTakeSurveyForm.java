
package com.biperf.core.ui.survey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.objectpartners.cms.util.ContentReaderManager;

public class ParticipantTakeSurveyForm extends BaseForm
{
  public static final String FORM_NAME = "participantTakeSurveyForm";

  private Promotion promotion;
  private String id;
  private String surveyText;
  private String promotionId;
  private List<SurveyQuestionValueBean> surveyQuestionList;
  private String data;
  private String ansIndex;
  private String nodes;

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getSurveyText()
  {
    return surveyText;
  }

  public void setSurveyText( String surveyText )
  {
    this.surveyText = surveyText;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public List<SurveyQuestionValueBean> getSurveyQuestionList()
  {
    if ( surveyQuestionList == null )
    {
      surveyQuestionList = new ArrayList<SurveyQuestionValueBean>();
    }
    return surveyQuestionList;
  }

  public void setSurveyQuestionList( List<SurveyQuestionValueBean> surveyQuestionList )
  {
    this.surveyQuestionList = surveyQuestionList;
  }

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( surveyQuestionList != null )
    {
      for ( Iterator<SurveyQuestionValueBean> surveyformList = getSurveyQuestionList().iterator(); surveyformList.hasNext(); )
      {
        SurveyQuestionValueBean formBean = surveyformList.next();
        if ( StringUtils.isBlank( formBean.getAnswerIndex() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "survey.question_response", "RESPONSE_REQUIRED" ) ) );
        }
      }
    }

    return actionErrors;
  }

  protected List<SurveyQuestionValueBean> getEmptyValueList( int valueListCount )
  {
    List<SurveyQuestionValueBean> valueList = new ArrayList<SurveyQuestionValueBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      SurveyQuestionValueBean formBean = new SurveyQuestionValueBean();
      valueList.add( formBean );
    }

    return valueList;
  }

  protected void createEmptyList( int valueListCount )
  {
    if ( getSurveyQuestionListSize() <= 0 )
    {
      for ( int i = 0; i < valueListCount; i++ )
      {
        SurveyQuestionValueBean formBean = new SurveyQuestionValueBean();
        getSurveyQuestionList().add( formBean );
      }
    }

  }

  public int getSurveyQuestionListSize()
  {
    if ( this.surveyQuestionList != null )
    {
      return this.surveyQuestionList.size();
    }
    return 0;
  }

  public void setData( String data )
  {
    this.data = data;
  }

  public String getData()
  {
    return data;
  }

  public String getAnsIndex()
  {
    return ansIndex;
  }

  public void setAnsIndex( String ansIndex )
  {
    this.ansIndex = ansIndex;
  }

  public String getNodes()
  {
    return nodes;
  }

  public void setNodes( String nodes )
  {
    this.nodes = nodes;
  }

}
