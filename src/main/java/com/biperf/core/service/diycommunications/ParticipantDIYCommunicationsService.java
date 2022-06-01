
package com.biperf.core.service.diycommunications;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.news.NewsDetailsView;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.DIYCommunicationsFileUploadValue;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.diycommunication.ResourceList;
import com.objectpartners.cms.domain.Content;

public interface ParticipantDIYCommunicationsService extends SAO
{
  public static final String BEAN_NAME = "participantDIYCommunicationsService";

  public List<DIYCommunications> getActiveByCommunicationType( Long managerId, String communicationType );

  public List<DIYCommunications> getArchievedByCommunicationType( Long managerId, String communicationType );

  public DIYCommunications getDIYCommunicationsById( Long id );

  public List<Audience> getPublicAudienceList();

  public boolean validFileData( DIYCommunicationsFileUploadValue data );

  public DIYCommunicationsFileUploadValue uploadFileForCommunications( DIYCommunicationsFileUploadValue data, String communicationsType ) throws ServiceErrorException;

  public DIYCommunicationsFileUploadValue uploadPhotoForCommunications( DIYCommunicationsFileUploadValue data, String communicationsType ) throws ServiceErrorException;

  public boolean moveFileToWebdav( String mediaUrl );

  public boolean isValidImageFormat( String format );

  public DIYCommunications saveDIYCommunications( DIYCommunications diyCommunications ) throws UniqueConstraintViolationException, ServiceErrorException;

  public DIYCommunications saveBannerContents( DIYCommunications diycommunication, ResourceList bannerResource, List<String> audience, Map<String, Object> deafultLangMap )
      throws ServiceErrorException;

  public DIYCommunications saveResourceCenterResources( DIYCommunications diyCommunications, ResourceList resourceCenterContent, List<String> audienceNames, Map<String, Object> deafultLangMap )
      throws ServiceErrorException;

  public DIYCommunications saveTipsResources( DIYCommunications diyCommunications, ResourceList tipsContent, List<String> audienceNames, Map<String, Object> deafultLangMap )
      throws ServiceErrorException;

  public DIYCommunications saveNewsContents( DIYCommunications diycommunication, ResourceList newsContent, List<String> audience, Map<String, Object> deafultLangMap ) throws ServiceErrorException;

  public DIYCommunications getCommunicationsByTitleAndType( String communicationsTitle, String communicationsType );

  public List<DIYCommunications> getActiveByCommunicationType( String communicationtype );

  public List<Content> getDiyCommunications( String communicationTypeNews, String sectionCode );

  public List<FormattedValueBean> getAudienceParticipants( Long audienceId );

  public List<NewsDetailsView> getNewsList( String contextPath );

}
