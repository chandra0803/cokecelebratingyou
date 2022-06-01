/**
 * 
 */

package com.biperf.core.ui.instantpoll;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SurveyQuestionStatusType;
import com.biperf.core.domain.enums.SurveyResponseType;
import com.biperf.core.domain.enums.SurveyType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.promotion.InstantPoll;
import com.biperf.core.domain.promotion.InstantPollAudience;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ServiceLocator;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.instantpoll.InstantPollAudienceFormBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author poddutur
 *
 */
public class InstantPollForm extends BaseForm
{
  private static final long serialVersionUID = 7672077111121957068L;
  public static final String FORM_NAME = "instantPollForm";

  public static final String PRIMARY = "primary";

  public static final String SPECIFY_AUDIENCE = "specifyaudience";

  private Long id;
  private String method;
  private String[] deleteInstantPolls;
  private String startDate;
  private String endDate;
  private String question;
  private Long questionId;
  private String questionStatus;
  private String answer1;
  private String answer2;
  private String answer3;
  private String answer4;
  private String answer5;
  private boolean notifyParticipant;
  private String audienceType;
  private String pollStatus;
  private String primaryAudienceId;
  private Long userId;
  private List<InstantPollAudienceFormBean> primaryAudienceList = new ArrayList<InstantPollAudienceFormBean>();

  // INSTANTPOLL
  private Long surveyFormId;
  private String surveyFormName;
  private String copySurveyFormName;
  private String description;
  private String status;
  private boolean surveyAssigned;
  private boolean active;
  private String surveyFormType;
  private String surveyFormTypeDesc;

  // INSTANTPOLL QUESTION ANSWERS
  private Long surveyQuestionResponseId;
  private String surveyQuestionResponseText;
  private String surveyQuestionResponseCmAssetCode;
  private String surveyQuestionResponseStatus;

  private Long surveyQuestionId;
  private String surveyQuestionText;
  private String surveyQuestionStatus;
  private String surveyQuestionStatusText;

  private String surveyName;
  private boolean surveyLive;

  private boolean openEnded;
  private boolean openEndedRequired;
  private boolean surveyPromotion;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String[] getDeleteInstantPolls()
  {
    return deleteInstantPolls;
  }

  public void setDeleteInstantPolls( String[] deleteInstantPolls )
  {
    this.deleteInstantPolls = deleteInstantPolls;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getQuestion()
  {
    return question;
  }

  public void setQuestion( String question )
  {
    this.question = question;
  }

  public String getAnswer1()
  {
    return answer1;
  }

  public void setAnswer1( String answer1 )
  {
    this.answer1 = answer1;
  }

  public String getAnswer2()
  {
    return answer2;
  }

  public void setAnswer2( String answer2 )
  {
    this.answer2 = answer2;
  }

  public String getAnswer3()
  {
    return answer3;
  }

  public void setAnswer3( String answer3 )
  {
    this.answer3 = answer3;
  }

  public String getAnswer4()
  {
    return answer4;
  }

  public void setAnswer4( String answer4 )
  {
    this.answer4 = answer4;
  }

  public String getAnswer5()
  {
    return answer5;
  }

  public void setAnswer5( String answer5 )
  {
    this.answer5 = answer5;
  }

  public boolean isNotifyParticipant()
  {
    return notifyParticipant;
  }

  public void setNotifyParticipant( boolean notifyParticipant )
  {
    this.notifyParticipant = notifyParticipant;
  }

  public String getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( String audienceType )
  {
    this.audienceType = audienceType;
  }

  public String getPrimaryAudienceId()
  {
    return primaryAudienceId;
  }

  public void setPrimaryAudienceId( String primaryAudienceId )
  {
    this.primaryAudienceId = primaryAudienceId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public List<InstantPollAudienceFormBean> getPrimaryAudienceListAsList()
  {
    return primaryAudienceList;
  }

  public void setPrimaryAudienceListAsList( List<InstantPollAudienceFormBean> primaryAudienceList )
  {
    this.primaryAudienceList = primaryAudienceList;
  }

  public Long getSurveyFormId()
  {
    return surveyFormId;
  }

  public void setSurveyFormId( Long surveyFormId )
  {
    this.surveyFormId = surveyFormId;
  }

  public String getSurveyFormName()
  {
    return surveyFormName;
  }

  public void setSurveyFormName( String surveyFormName )
  {
    this.surveyFormName = surveyFormName;
  }

  public String getCopySurveyFormName()
  {
    return copySurveyFormName;
  }

  public void setCopySurveyFormName( String copySurveyFormName )
  {
    this.copySurveyFormName = copySurveyFormName;
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

  public boolean isSurveyAssigned()
  {
    return surveyAssigned;
  }

  public void setSurveyAssigned( boolean surveyAssigned )
  {
    this.surveyAssigned = surveyAssigned;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getSurveyFormType()
  {
    return surveyFormType;
  }

  public void setSurveyFormType( String surveyFormType )
  {
    this.surveyFormType = surveyFormType;
  }

  public String getSurveyFormTypeDesc()
  {
    return surveyFormTypeDesc;
  }

  public void setSurveyFormTypeDesc( String surveyFormTypeDesc )
  {
    this.surveyFormTypeDesc = surveyFormTypeDesc;
  }

  public Long getQuestionId()
  {
    return questionId;
  }

  public void setQuestionId( Long questionId )
  {
    this.questionId = questionId;
  }

  public String getQuestionStatus()
  {
    return questionStatus;
  }

  public void setQuestionStatus( String questionStatus )
  {
    this.questionStatus = questionStatus;
  }

  public Long getSurveyQuestionResponseId()
  {
    return surveyQuestionResponseId;
  }

  public void setSurveyQuestionResponseId( Long surveyQuestionResponseId )
  {
    this.surveyQuestionResponseId = surveyQuestionResponseId;
  }

  public String getSurveyQuestionResponseText()
  {
    return surveyQuestionResponseText;
  }

  public void setSurveyQuestionResponseText( String surveyQuestionResponseText )
  {
    this.surveyQuestionResponseText = surveyQuestionResponseText;
  }

  public String getSurveyQuestionResponseCmAssetCode()
  {
    return surveyQuestionResponseCmAssetCode;
  }

  public void setSurveyQuestionResponseCmAssetCode( String surveyQuestionResponseCmAssetCode )
  {
    this.surveyQuestionResponseCmAssetCode = surveyQuestionResponseCmAssetCode;
  }

  public String getSurveyQuestionResponseStatus()
  {
    return surveyQuestionResponseStatus;
  }

  public void setSurveyQuestionResponseStatus( String surveyQuestionResponseStatus )
  {
    this.surveyQuestionResponseStatus = surveyQuestionResponseStatus;
  }

  public Long getSurveyQuestionId()
  {
    return surveyQuestionId;
  }

  public void setSurveyQuestionId( Long surveyQuestionId )
  {
    this.surveyQuestionId = surveyQuestionId;
  }

  public String getSurveyQuestionText()
  {
    return surveyQuestionText;
  }

  public void setSurveyQuestionText( String surveyQuestionText )
  {
    this.surveyQuestionText = surveyQuestionText;
  }

  public String getSurveyQuestionStatus()
  {
    return surveyQuestionStatus;
  }

  public void setSurveyQuestionStatus( String surveyQuestionStatus )
  {
    this.surveyQuestionStatus = surveyQuestionStatus;
  }

  public String getSurveyQuestionStatusText()
  {
    return surveyQuestionStatusText;
  }

  public void setSurveyQuestionStatusText( String surveyQuestionStatusText )
  {
    this.surveyQuestionStatusText = surveyQuestionStatusText;
  }

  public String getSurveyName()
  {
    return surveyName;
  }

  public void setSurveyName( String surveyName )
  {
    this.surveyName = surveyName;
  }

  public boolean isSurveyLive()
  {
    return surveyLive;
  }

  public void setSurveyLive( boolean surveyLive )
  {
    this.surveyLive = surveyLive;
  }

  public boolean isOpenEnded()
  {
    return openEnded;
  }

  public void setOpenEnded( boolean openEnded )
  {
    this.openEnded = openEnded;
  }

  public boolean isOpenEndedRequired()
  {
    return openEndedRequired;
  }

  public void setOpenEndedRequired( boolean openEndedRequired )
  {
    this.openEndedRequired = openEndedRequired;
  }

  public boolean isSurveyPromotion()
  {
    return surveyPromotion;
  }

  public void setSurveyPromotion( boolean surveyPromotion )
  {
    this.surveyPromotion = surveyPromotion;
  }

  public String getPollStatus()
  {
    return pollStatus;
  }

  public void setPollStatus( String pollStatus )
  {
    this.pollStatus = pollStatus;
  }

  /**
   * adds an audience item to the list
   *
   * @param audience
   * @param instantPollAudienceType
   */
  public void addAudience( Audience audience, String instantPollAudienceType )
  {
    InstantPollAudienceFormBean audienceFormBean = new InstantPollAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );
    primaryAudienceList.add( audienceFormBean );
  }

  /**
   * removes any items that have been selected to be removed from the list
   *
   * @param instantPollAudienceType
   */
  public void removeItems( String audienceType )
  {
    Iterator it = null;
    if ( audienceType.equals( PRIMARY ) )
    {
      it = getPrimaryAudienceListAsList().iterator();
    }

    if ( it != null )
    {
      while ( it.hasNext() )
      {
        InstantPollAudienceFormBean audienceFormBean = (InstantPollAudienceFormBean)it.next();
        if ( audienceFormBean.isRemoved() )
        {
          it.remove();
        }
      }
    }
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    primaryAudienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "primaryAudienceListCount" ) );
  }

  private List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty InstantPollAudienceFormBean
      InstantPollAudienceFormBean instantPollAudienceFormBean = new InstantPollAudienceFormBean();
      valueList.add( instantPollAudienceFormBean );
    }

    return valueList;
  }

  public void loadDomainAudienceObject( List<InstantPollAudienceFormBean> instantPollAudienceFormBeanList )
  {
    this.setPrimaryAudienceListAsList( instantPollAudienceFormBeanList );
  }

  /**
   * Gets a Set of the Primary Audiences
   * @return
   */
  public Set getPrimaryAudienceSet()
  {
    Set tempPrimaryAudSet = new LinkedHashSet(); // primaryAudienceList;
    for ( Iterator primAudIter = primaryAudienceList.iterator(); primAudIter.hasNext(); )
    {
      InstantPollAudienceFormBean audienceBean = (InstantPollAudienceFormBean)primAudIter.next();
      Audience audience = getAudienceService().getAudienceById( audienceBean.getAudienceId(), null );
      tempPrimaryAudSet.add( audience );
    }
    return tempPrimaryAudSet;
  }

  /**
   * Accessor for the number of InstantPollAudienceFormBean objects in the list.
   *
   * @return int
   */
  public int getPrimaryAudienceListCount()
  {
    if ( primaryAudienceList == null )
    {
      return 0;
    }

    return primaryAudienceList.size();
  }

  /**
   * Accessor for the value list
   *
   * @param index
   * @return Single instance of InstantPollAudienceFormBean from the value list
   */
  public InstantPollAudienceFormBean getPrimaryAudienceList( int index )
  {
    try
    {
      return (InstantPollAudienceFormBean)primaryAudienceList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public int getPrimaryAudienceCount()
  {
    return primaryAudienceList != null ? primaryAudienceList.size() : 0;
  }

  public void loadDefaultParameters()
  {
    this.setAudienceType( "allactivepaxaudience" );
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );
    int count = 0;

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( DateUtils.toDate( this.startDate ) == null )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "instantpoll.library.START_DATE" ) ) );
    }
    if ( DateUtils.toDate( this.endDate ) == null )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "instantpoll.library.END_DATE" ) ) );
    }

    if ( id == null || id == 0 )
    {
      // Verify that start date is not in the past
      if ( DateUtils.toDate( this.startDate ) != null )
      {
        if ( DateUtils.toDate( this.startDate ).before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
        {
          // The date is before current date
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_START_BEFORE_TO_DATE ) );
        }
      }
    }

    // Verify that end date, if used, is not in the past
    if ( DateUtils.toDate( this.endDate ) != null )
    {
      if ( DateUtils.toDate( this.endDate ).before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
      {
        // The date is before current date
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_TO_DATE ) );
      }
    }

    // Verify that end date is after start date
    if ( DateUtils.toDate( this.startDate ) != null && DateUtils.toDate( this.endDate ) != null && DateUtils.toDate( this.startDate ).after( DateUtils.toDate( this.endDate ) ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_START_DATE ) );
    }

    if ( !isDisabledFlag() )
    {
      if ( this.question.isEmpty() )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "instantpoll.library.QUESTION" ) ) );
      }

      if ( !this.answer1.isEmpty() )
      {
        count++;
      }
      if ( !this.answer2.isEmpty() )
      {
        count++;
      }
      if ( !this.answer3.isEmpty() )
      {
        count++;
      }
      if ( !this.answer4.isEmpty() )
      {
        count++;
      }
      if ( !this.answer5.isEmpty() )
      {
        count++;
      }

      if ( count < 2 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "instantpoll.library.ATLEAST_TWO_ANSWERS_REQUIRED" ) ) );
      }

      if ( this.audienceType.equals( SPECIFY_AUDIENCE ) )
      {
        if ( this.primaryAudienceList.size() == 0 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "forum.library.SELECT_ATLEAST_ONE_AUDIENCE" ) ) );
        }
      }
    }

    return actionErrors;
  }

  public void loadDomainObject( InstantPoll instantPoll, List surveyQuestionresponseList )
  {
    this.setId( instantPoll.getId() );
    this.setSurveyFormId( instantPoll.getId() );
    this.setPollStatus( instantPoll.getStatus() );
    this.setAudienceType( instantPoll.getAudienceType() );
    this.setStartDate( DateUtils.toDisplayString( instantPoll.getSubmissionStartDate() ) );
    this.setEndDate( DateUtils.toDisplayString( instantPoll.getSubmissionEndDate() ) );
    this.setNotifyParticipant( instantPoll.getNotifyPax().booleanValue() );

    for ( int i = 0; i < surveyQuestionresponseList.size(); i++ )
    {
      if ( this.answer1 == null )
      {
        this.setAnswer1( (String)surveyQuestionresponseList.get( i ) );
      }
      else if ( this.answer2 == null )
      {
        this.setAnswer2( (String)surveyQuestionresponseList.get( i ) );
      }
      else if ( this.answer3 == null )
      {
        this.setAnswer3( (String)surveyQuestionresponseList.get( i ) );
      }
      else if ( this.answer4 == null )
      {
        this.setAnswer4( (String)surveyQuestionresponseList.get( i ) );
      }
      else
      {
        this.setAnswer5( (String)surveyQuestionresponseList.get( i ) );
      }
    }
  }

  public void loadDomainObject( SurveyQuestion surveyQuestion )
  {
    this.setQuestion( surveyQuestion.getCmAssetName() );
    for ( SurveyQuestionResponse surveyQuestionResponse : surveyQuestion.getSurveyQuestionResponses() )
    {
      if ( this.answer1 == null )
      {
        this.answer1 = surveyQuestionResponse.getCmAssetCode();
      }
      else if ( this.answer2 == null )
      {
        this.answer2 = surveyQuestionResponse.getCmAssetCode();
      }
      else if ( this.answer3 == null )
      {
        this.answer3 = surveyQuestionResponse.getCmAssetCode();
      }
      else if ( this.answer4 == null )
      {
        this.answer4 = surveyQuestionResponse.getCmAssetCode();
      }
      else
      {
        this.answer5 = surveyQuestionResponse.getCmAssetCode();
      }
    }
  }

  /**
   * Load the form
   * 
   * @param survey
   */
  public void load( Survey survey )
  {
    if ( survey != null )
    {
      this.surveyFormId = survey.getId();
      this.surveyFormName = survey.getName();
      this.description = survey.getDescription() == null ? null : convertLineBreaks( survey.getDescription() );
      this.status = survey.getClaimFormStatusType().getCode();
      this.surveyAssigned = survey.isAssigned();
      this.surveyFormType = survey.getPromotionModuleType().getCode();
      this.surveyFormTypeDesc = survey.getPromotionModuleType().getName();
    }
  }

  private String convertLineBreaks( String text )
  {
    return StringUtil.convertLineBreaks( text );
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return Survey
   */
  public InstantPoll toDomainObject( InstantPoll instantPoll )
  {
    if ( instantPoll == null )
    {
      instantPoll = new InstantPoll();
    }

    instantPoll.setId( !isNewInstantPoll( instantPoll ) ? instantPoll.getId() : this.surveyFormId );
    instantPoll.setName( !isNewInstantPoll( instantPoll ) ? instantPoll.getName() : "InstantPoll" + DateUtils.getCurrentDate().getTime() );
    instantPoll.setDescription( this.description );
    ClaimFormStatusType statusType = null;
    if ( this.status != null )
    {
      statusType = ClaimFormStatusType.lookup( this.status );
    }
    if ( statusType == null )
    {
      statusType = ClaimFormStatusType.lookup( ClaimFormStatusType.ASSIGNED ); // default
                                                                               // status
    }
    instantPoll.setPromotionModuleType( PromotionType.lookup( PromotionType.INSTANTPOLL ) );
    instantPoll.setClaimFormStatusType( statusType );
    instantPoll.setSurveyType( SurveyType.lookup( SurveyType.RANDOM ) );
    instantPoll.setSubmissionStartDate( DateUtils.toDate( this.startDate ) );
    instantPoll.setSubmissionEndDate( DateUtils.toDate( this.endDate ) );
    instantPoll.setAudienceType( this.audienceType );
    instantPoll.setStatus( !isNewInstantPoll( instantPoll ) ? instantPoll.getStatus() : "active" );
    if ( this.notifyParticipant )
    {
      instantPoll.setNotifyPax( true );
    }
    else
    {
      instantPoll.setNotifyPax( false );
    }
    return instantPoll;
  }

  private boolean isNewInstantPoll( InstantPoll instantPoll )
  {
    return instantPoll != null && instantPoll.getId() != null ? false : true;
  }

  public SurveyQuestion toDomainSurveyQuestionObject( SurveyQuestion surveyQuestion )
  {
    if ( surveyQuestion == null )
    {
      surveyQuestion = new SurveyQuestion();
    }
    surveyQuestion.setCmAssetName( this.question );
    surveyQuestion.setStatusType( SurveyQuestionStatusType.lookup( "ACTIVE" ) );
    surveyQuestion.setResponseType( SurveyResponseType.lookup( SurveyResponseType.STANDARD_RESPONSE ) );

    return surveyQuestion;
  }

  public Set<InstantPollAudience> toDomainInstantPollAudienceSet()
  {
    Set<InstantPollAudience> audienceSet = new HashSet<InstantPollAudience>();
    InstantPollAudience instantPollAudience = null;
    if ( !this.primaryAudienceList.isEmpty() )
    {
      for ( InstantPollAudienceFormBean instantPollAudienceFormBean : this.primaryAudienceList )
      {
        instantPollAudience = new InstantPollAudience();
        if ( instantPollAudienceFormBean.getAudienceId() != null )
        {
          Audience audience = getAudienceService().getAudienceById( instantPollAudienceFormBean.getAudienceId(), null );
          instantPollAudience.setAudience( audience );
        }
        audienceSet.add( instantPollAudience );
      }
    }
    return audienceSet;
  }

  public boolean isDisabledFlag()
  {
    boolean isDisabledFlag = false;
    if ( id != null && !id.equals( new Long( 0 ) ) )
    {
      Date submissionStartDate = DateUtils.toDate( startDate );
      Date currentDate = DateUtils.getCurrentDate();
      isDisabledFlag = submissionStartDate != null && currentDate.after( submissionStartDate ) ? true : false;
    }
    return isDisabledFlag;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)ServiceLocator.getService( AudienceService.BEAN_NAME );
  }

}
