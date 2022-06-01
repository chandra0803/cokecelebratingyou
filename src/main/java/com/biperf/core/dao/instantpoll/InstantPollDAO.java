/**
 * 
 */

package com.biperf.core.dao.instantpoll;

import java.math.BigDecimal;
import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.InstantPoll;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.value.instantpoll.InstantPollAudienceFormBean;
import com.biperf.core.value.instantpoll.InstantPollsListbean;

/**
 * @author poddutur
 *
 */
public interface InstantPollDAO extends DAO
{

  InstantPoll saveInstantPoll( InstantPoll instantPoll );

  SurveyQuestionResponse saveSurveyQuestionResponse( SurveyQuestionResponse surveyQuestionResponse );

  public List<Long> getEligibleInstantPollIds( Long userId );

  public List<InstantPoll> getInstantPollsForTileDisplay( Long userId, AssociationRequestCollection associationRequestCollection );

  public InstantPoll getInstantPollByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  InstantPoll getInstantPollByName( String name );

  InstantPoll getInstantPollById( Long id );

  List<InstantPollsListbean> getAllInstantPollsList();

  List<InstantPollAudienceFormBean> getAudienceByInstantPollId( Long instantPollId );

  SurveyQuestion getSurveyQuestionById( Long instantPollId );

  void deleteInstantPoll( InstantPoll instantPollToDelete );

  List<BigDecimal> getUsersListOfSpecifyAudienceByInstantPollId( Long instantPollId );

  List getSurveyQuestionResponseByInstantPollId( Long instantPollId );

}
