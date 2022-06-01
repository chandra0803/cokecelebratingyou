
package com.biperf.core.dao.client;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.client.CareerMomentsView;
import com.biperf.core.domain.client.ClientProfileComment;
import com.biperf.core.domain.client.ClientProfileLike;
import com.biperf.core.domain.client.JobChangesDatum;
import com.biperf.core.domain.client.NewHiresDatum;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.value.client.CokeCommentsLikes;

public interface CokeCareerMomentsDAO extends DAO
{
  public static final String BEAN_NAME = "cokeCareerMomentsDAO";

  public List<NewHiresDatum> getCareerMomentsHireDataForModule();
  
  public List<JobChangesDatum> getCareerMomentsJobDataForModule();
  
  public List<ClientProfileComment> getAllProfileCommentsByUserId(Long userId);
  
  public ClientProfileComment saveComment(ClientProfileComment clientComment);
  
  public ClientProfileLike saveLike(ClientProfileLike clientLike);
  
  public Map<Long, Long> getAboutMeLikesByUserId(Long userId, List<AboutMe> aboutMeQuestions);
  
  public void removeAboutMeLike( final Long userId, final Long profileUserId, final Long aboutMeId );
  
  public List<ClientProfileComment> getMainLevelProfileCommentsByUserId(Long userId);
  
  public List<ClientProfileComment> getSubLevelProfileComments(Long profileUserId, Long mainCommentId);
  
  public int getLikesCountByCommentId(Long commentId);
  
  public Map<Long, Boolean> getMyAboutMeLikes(List<AboutMe> aboutMeQuestions, Participant pax);
  
  public int getLikesCountByAboutMeId(Long aboutMeId);
  
  public void removeCommentLike( final Long userId, final Long profileUserId, final Long commentId );
  
  public List<CareerMomentsView> getCareerMomentsJobDataForDetail(String tabType, int current, String recType, String listVal, String locale, String username);
  
  public List<CokeCommentsLikes> getCareerMomentsLikesCommentsCount(String date);

}
