
package com.biperf.core.dao.diyquiz;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.diyquiz.DIYQuiz;

/**
 * 
 * DIYQuizDAO.
 * 
 * @author kandhi
 * @since Jul 10, 2013
 * @version 1.0
 */
public interface DIYQuizDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "diyQuizDAO";

  public DIYQuiz getQuizById( Long quizId );

  public DIYQuiz saveQuiz( DIYQuiz diyQuiz );

  public List<DIYQuiz> getAllQuizzesByPromotionId( Long promotionId );

  public List<DIYQuiz> getQuizByPromotionAndStatus( String status, Long promotionId );

  public List<Long> getActiveQuizzesForParticipantByPromotion( Long promotionId, Long participantId );

  public List<DIYQuiz> getActiveQuizListForParticipantByPromotion( Long promotionId, Long participantId );

  public List<DIYQuiz> getEligibleQuizzesForParticipantByPromotion( Long promotionId, Long participantId );

  public List<DIYQuiz> getAllQuizzesByPromotionIdAndBadgeRuleId( Long promotionId, Long badgeRuleId );
}
