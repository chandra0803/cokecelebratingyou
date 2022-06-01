/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/goalquest/ParticipantSurveyResponseForm.java,v $
 */

package com.biperf.core.ui.goalquest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.survey.SurveyQuestionValueBean;
import com.biperf.core.ui.utils.RequestUtils;
import com.objectpartners.cms.util.ContentReaderManager;

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
public class ParticipantSurveyResponseForm extends BaseForm
{
  public static final String FORM_NAME = "participantSurveyResponseForm";

  private GoalQuestPromotion promotion;
  private String id;
  private String surveyText;
  private String promotionId;
  private List<SurveyQuestionValueBean> surveyQuestionList;

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

  public GoalQuestPromotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( GoalQuestPromotion promotion )
  {
    this.promotion = promotion;
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

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    int goalCount = RequestUtils.getOptionalParamInt( request, "surveyQuestionListSize" );
    surveyQuestionList = getEmptyValueList( goalCount );
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
        surveyQuestionList.add( formBean );
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

}
