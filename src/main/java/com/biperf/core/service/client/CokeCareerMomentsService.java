/*
 * (c) 2020 BI, Inc.  All rights reserved.
 * $Source$
 */
package com.biperf.core.service.client;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.client.CareerMomentsModuleSet;
import com.biperf.core.domain.client.CareerMomentsModuleSets;
import com.biperf.core.domain.client.CareerMomentsSets;
import com.biperf.core.domain.client.CareerMomentsView;
import com.biperf.core.domain.client.ClientProfileComment;
import com.biperf.core.domain.client.ClientProfileLike;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.purl.PurlCelebrationSet;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.PurlFileUploadValue;
import com.biperf.core.value.client.ClientCommentsValueBean;
import com.biperf.core.value.client.CokeCommentsLikes;

/**
 * TODO Javadoc for CokeCareerMomentsService.
 * 
 * @author yelamanc
 * @since Mar 3, 2020
 * @version 1.0
 */
public interface CokeCareerMomentsService extends SAO
{
  public final String BEAN_NAME = "cokeCareerMomentsService";
  
  public CareerMomentsModuleSet getCareerMomentsModuleData();
  
  public Map<Long, Long> getAboutMeLikesByUserId(Long userId, List<AboutMe> aboutMeQuestions);
  
  public ClientProfileLike saveLike(ClientProfileLike clientLike);
  
  public void removeAboutMeLike( final Long userId, final Long profileUserId, final Long aboutMeId );
  
  public ClientCommentsValueBean getProfileCommentsByUserId(Long userId);
  
  public ClientProfileComment saveComment(ClientProfileComment clientComment)throws ServiceErrorException;
  
  public Map<Long, Boolean> getMyAboutMeLikes(List<AboutMe> aboutMeQuestions, Participant pax);
  
  public int getLikesCountByAboutMeId(Long aboutMeId);
  
  public void removeCommentLike( final Long userId, final Long profileUserId, final Long commentId );
  
  public int getLikesCountByCommentId(Long commentId);
  
  public PurlFileUploadValue uploadPhotoForContributor( PurlFileUploadValue data ) throws ServiceErrorException;
  
  public PurlFileUploadValue uploadVideoForContributor( PurlFileUploadValue data ) throws ServiceErrorException;
  
  public List<CareerMomentsView> getCareerMomentsDataForDetail(String tabType, int current, String recType, String listVal, String locale, String username);
  
  public List<CokeCommentsLikes> getCareerMomentsLikesCommentsCount(String date);
}
