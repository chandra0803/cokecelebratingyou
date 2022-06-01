/**
 * 
 */

package com.biperf.core.service.instantpoll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.promotion.InstantPoll;
import com.biperf.core.domain.promotion.InstantPollAudience;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.instantpoll.InstantPollAudienceFormBean;
import com.biperf.core.value.instantpoll.InstantPollsListbean;

/**
 * @author poddutur
 *
 */
public interface InstantPollService extends SAO
{
  public static final String BEAN_NAME = "instantPollService";

  public InstantPoll saveInstantPoll( InstantPoll instantPoll, SurveyQuestion surveyQuestionObject, Set<InstantPollAudience> instantPollAudienceSet, List<String> answersList, String question )
      throws ServiceErrorException;

  public List<Long> getEligibleInstantPollIds( Long userId );

  public List<InstantPoll> getInstantPollsForTileDisplay( Long userId, AssociationRequestCollection associationRequestCollection );

  public InstantPoll getInstantPollByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  List<InstantPollsListbean> getAllInstantPollsList();

  InstantPoll getInstantPollById( Long instantPollId );

  List<InstantPollAudienceFormBean> getAudienceByInstantPollId( Long instantPollId );

  SurveyQuestion getSurveyQuestionById( Long instantPollId );

  void deleteInstantPolls( List instantPollIdList ) throws ServiceErrorException;

  public InstantPoll saveInstantPoll( InstantPoll instantPoll, Set<InstantPollAudience> instantPollAudienceSet ) throws ServiceErrorException;

  public List<BigDecimal> getUsersListOfSpecifyAudienceByInstantPollId( Long instantPollId );

  public List getSurveyQuestionResponseByInstantPollId( Long instantPollId );

}
