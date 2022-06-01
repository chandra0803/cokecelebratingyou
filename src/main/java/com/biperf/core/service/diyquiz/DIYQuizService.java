
package com.biperf.core.service.diyquiz;

import java.util.List;

import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/**
 * 
 * DIYQuizService.
 * 
 * @author kandhi
 * @since Jul 9, 2013
 * @version 1.0
 */
public interface DIYQuizService extends SAO
{
  public static final String BEAN_NAME = "diyQuizService";

  public DIYQuiz getQuizById( Long id );

  public DIYQuiz saveDIYQuiz( DIYQuiz diyQuiz, Long promotionId ) throws ServiceErrorException;

  public List<DIYQuiz> getAllQuizzesByPromotionId( Long promotionId );

  public List<DIYQuiz> getEligibleQuizzesForParticipantByPromotion( Long promotionId, Long participantId );

  public DIYQuiz getQuizByIdWithAssociations( Long id, AssociationRequestCollection associationRequests );

  public List<DIYQuiz> getQuizByPromotionAndStatus( String status, Long promotionId );

  public boolean isValidImageFormat( String format );

  public void deleteQuiz( Long quizId ) throws ServiceErrorException;

  public void deleteQuizes( List quizIdList ) throws ServiceErrorException;

  public void checkDuplicateQuizName( String quizName, Long quizId ) throws ServiceErrorException;

  public MailingRecipient getMailingRecipient( Participant pax );
}
