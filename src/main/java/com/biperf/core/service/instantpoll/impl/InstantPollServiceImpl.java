/**
 * 
 */

package com.biperf.core.service.instantpoll.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.dao.instantpoll.InstantPollDAO;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.enums.SurveyQuestionStatusType;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.promotion.InstantPoll;
import com.biperf.core.domain.promotion.InstantPollAudience;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.InstantPollMailingProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.instantpoll.InstantPollService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.instantpoll.InstantPollAudienceFormBean;
import com.biperf.core.value.instantpoll.InstantPollsListbean;

/**
 * @author poddutur
 *
 */
public class InstantPollServiceImpl implements InstantPollService
{
  private CMAssetService cmAssetService;
  private InstantPollDAO instantPollDAO;
  private ProcessService processService;

  @Override
  public InstantPoll saveInstantPoll( InstantPoll instantPoll, SurveyQuestion surveyQuestionObject, Set<InstantPollAudience> instantPollAudienceSet, List<String> answersList, String question )
      throws ServiceErrorException
  {
    InstantPoll dbInstantPoll = null;
    boolean isNew = isNewInstantPoll( instantPoll.getId() );
    // --------------------------------------------------------------
    // Check to see if the InstantPoll already exists in the database
    // ---------------------------------------------------------------
    {
      if ( isNew )
      {
        dbInstantPoll = this.instantPollDAO.getInstantPollByName( instantPoll.getName() );
        if ( dbInstantPoll != null )
        {
          // if we found a record in the database with the given formName,
          // and our claimFormToSave ID is null or ZERO (trying to add a new one),
          // we are trying to insert a duplicate record.
          if ( instantPoll.getId() == null || instantPoll.getId().longValue() == 0 )
          {
            throw new ServiceErrorException( ServiceErrorMessageKeys.INSTANT_POLL_DUPLICATE_ERR );
          }
        }
      }
      try
      {
        instantPoll.getAudience().clear();
        for ( InstantPollAudience instantPollAudience : instantPollAudienceSet )
        {
          instantPoll.addAudience( instantPollAudience );
        }

        instantPoll.getSurveyQuestions().clear();
        SurveyQuestion surveyQuestion = surveyQuestionObject;
        String cmKeyFragment = cmAssetService.getUniqueKeyFragment();
        surveyQuestion.setCmAssetName( Survey.CM_QUESTION_ASSET_PREFIX + cmKeyFragment );
        CMDataElement cmDataElement = new CMDataElement( Survey.CM_QUESTION_NAME_KEY_DESC, Survey.CM_QUESTION_NAME_KEY, question, false );
        cmAssetService.createOrUpdateAsset( Survey.CM_QUESTION_SECTION, Survey.CM_QUESTION_ASSET_TYPE, Survey.CM_QUESTION_NAME_KEY_DESC, surveyQuestion.getCmAssetName(), cmDataElement );

        surveyQuestion.setSurvey( instantPoll );
        instantPoll.addSurveyQuestion( surveyQuestion );

        instantPoll.getSurveyQuestions().get( 0 ).getSurveyQuestionResponses().clear();
        for ( int i = 0; i < answersList.size(); i++ )
        {
          SurveyQuestionResponse surveyQuestionResponse = new SurveyQuestionResponse();
          surveyQuestionResponse.setStatusType( SurveyQuestionStatusType.lookup( "ACTIVE" ) );

          String cmKeyFragmentResponse = cmAssetService.getUniqueKeyFragment();
          CMDataElement cmDataElementResponse = new CMDataElement( Survey.CM_QUESTION_RESPONSE_KEY_DESC, Survey.CM_QUESTION_RESPONSE_KEY, (String)answersList.get( i ), false );
          surveyQuestionResponse.setCmAssetCode( Survey.CM_QUESTION_RESPONSE_ASSET_PREFIX + cmKeyFragmentResponse );
          cmAssetService.createOrUpdateAsset( Survey.CM_QUESTION_RESPONSE_SECTION,
                                              Survey.CM_QUESTION_RESPONSE_ASSET_TYPE,
                                              Survey.CM_QUESTION_RESPONSE_KEY_DESC,
                                              surveyQuestionResponse.getCmAssetCode(),
                                              cmDataElementResponse );

          surveyQuestionResponse.setSequenceNum( i );
          surveyQuestionResponse.setResponseCount( 0L );
          surveyQuestionResponse.setSurveyQuestion( surveyQuestion );
          surveyQuestion.addSurveyQuestionResponse( surveyQuestionResponse );
        }

        dbInstantPoll = this.instantPollDAO.saveInstantPoll( instantPoll );

        Long instantPollId = dbInstantPoll.getId();
        if ( isNew && dbInstantPoll.getNotifyPax() )
        {
          if ( instantPollId != null )
          {
            Process process = processService.createOrLoadSystemProcess( InstantPollMailingProcess.PROCESS_NAME, InstantPollMailingProcess.BEAN_NAME );
            LinkedHashMap parameterValueMap = new LinkedHashMap();
            parameterValueMap.put( "instantPollId", new String[] { instantPollId.toString() } );

            ProcessSchedule processSchedule = new ProcessSchedule();
            if ( dbInstantPoll.getSubmissionStartDate().after( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
            {
              processSchedule.setStartDate( dbInstantPoll.getSubmissionStartDate() );
              processSchedule.setTimeOfDayMillis( new Long( org.apache.commons.lang3.time.DateUtils.MILLIS_PER_HOUR ) );
            }
            else
            {
              processSchedule.setStartDate( new Date() );
              processSchedule.setTimeOfDayMillis( new Long( 0 ) );
            }

            processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

            processService.scheduleProcess( process, processSchedule, parameterValueMap, UserManager.getUserId() );
          }
        }
      }
      catch( ConstraintViolationException cve )
      {
        throw new ServiceErrorException( ServiceErrorMessageKeys.INSTANT_POLL_DUPLICATE_ERR, cve );
      }
    }
    return dbInstantPoll;
  }

  private boolean isNewInstantPoll( Long instantPollId )
  {
    if ( instantPollId == null || instantPollId.longValue() == 0 )
    {
      return true;
    }
    return false;
  }

  public List<Long> getEligibleInstantPollIds( Long userId )
  {
    return instantPollDAO.getEligibleInstantPollIds( userId );
  }

  public List<InstantPoll> getInstantPollsForTileDisplay( Long userId, AssociationRequestCollection associationRequestCollection )
  {
    return instantPollDAO.getInstantPollsForTileDisplay( userId, associationRequestCollection );
  }

  public InstantPoll getInstantPollByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return instantPollDAO.getInstantPollByIdWithAssociations( id, associationRequestCollection );
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setInstantPollDAO( InstantPollDAO instantPollDAO )
  {
    this.instantPollDAO = instantPollDAO;
  }

  @Override
  public List<InstantPollsListbean> getAllInstantPollsList()
  {
    return this.instantPollDAO.getAllInstantPollsList();
  }

  @Override
  public InstantPoll getInstantPollById( Long instantPollId )
  {
    return this.instantPollDAO.getInstantPollById( instantPollId );
  }

  @Override
  public List<InstantPollAudienceFormBean> getAudienceByInstantPollId( Long instantPollId )
  {
    return this.instantPollDAO.getAudienceByInstantPollId( instantPollId );
  }

  @Override
  public SurveyQuestion getSurveyQuestionById( Long instantPollId )
  {
    return this.instantPollDAO.getSurveyQuestionById( instantPollId );
  }

  @Override
  public void deleteInstantPolls( List instantPollIdList ) throws ServiceErrorException
  {
    Iterator idIter = instantPollIdList.iterator();

    while ( idIter.hasNext() )
    {
      this.deleteInstantPoll( (Long)idIter.next() );
    }
  }

  public void deleteInstantPoll( Long instantPollId )
  {
    InstantPoll instantPollToDelete = instantPollDAO.getInstantPollById( instantPollId );

    if ( instantPollToDelete != null )
    {
      instantPollDAO.deleteInstantPoll( instantPollToDelete );
    }
  }

  @Override
  public InstantPoll saveInstantPoll( InstantPoll instantPoll, Set<InstantPollAudience> instantPollAudienceSet ) throws ServiceErrorException
  {
    if ( instantPollAudienceSet.size() > 0 )
    {
      instantPoll.getAudience().clear();
    }
    for ( InstantPollAudience instantPollAudience : instantPollAudienceSet )
    {
      instantPoll.addAudience( instantPollAudience );
    }
    instantPoll = this.instantPollDAO.saveInstantPoll( instantPoll );
    return instantPoll;
  }

  public ProcessService getProcessService()
  {
    return processService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  @Override
  public List<BigDecimal> getUsersListOfSpecifyAudienceByInstantPollId( Long instantPollId )
  {
    return this.instantPollDAO.getUsersListOfSpecifyAudienceByInstantPollId( instantPollId );
  }

  @Override
  public List getSurveyQuestionResponseByInstantPollId( Long instantPollId )
  {
    return this.instantPollDAO.getSurveyQuestionResponseByInstantPollId( instantPollId );
  }

}
