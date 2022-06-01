
package com.biperf.core.service.ssi.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.dao.ssi.SSIPromotionDAO;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.domain.ssi.SSIPromotionContestApprovalLevel1Audience;
import com.biperf.core.domain.ssi.SSIPromotionContestApprovalLevel2Audience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.SSIFileUpload;

/**
 * 
 * SSIPromotionServiceImpl.
 * 
 * @author kandhi
 * @since Oct 29, 2014
 * @version 1.0
 */
public class SSIPromotionServiceImpl implements SSIPromotionService
{
  private SSIPromotionDAO ssiPromotionDAO;
  private AudienceService audienceService;
  private PromotionService promotionService;
  private ParticipantService participantService;
  private SystemVariableService systemVariableService;
  private FileUploadStrategy appDataDirFileUploadStrategy;
  private FileUploadStrategy webdavFileUploadStrategy;
  private CMAssetService cmAssetService;
  private FileUploadStrategy awsFileUploadStrategy;

  @Override
  public SSIPromotion getLiveSSIPromotion()
  {
    return ssiPromotionDAO.getLiveSSIPromotion();
  }

  public SSIPromotion getLiveSSIPromotionWithAssociations( AssociationRequestCollection associationRequestCollection )
  {
    SSIPromotion promotion = this.ssiPromotionDAO.getLiveSSIPromotion();
    for ( Iterator<AssociationRequest> iterator = associationRequestCollection.iterator(); iterator.hasNext(); )
    {
      AssociationRequest req = iterator.next();
      req.execute( promotion );
    }
    return promotion;
  }

  @Override
  public SSIPromotion getLiveOrCompletedSSIPromotion()
  {
    return ssiPromotionDAO.getLiveOrCompletedSSIPromotion();
  }

  public void setSsiPromotionDAO( SSIPromotionDAO ssiPromotionDAO )
  {
    this.ssiPromotionDAO = ssiPromotionDAO;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  @Override
  public boolean isPaxInContestCreatorAudience( Long userId )
  {
    boolean inAudience = false;
    SSIPromotion ssiPromotion = ssiPromotionDAO.getLiveSSIPromotion();
    if ( ssiPromotion != null )
    {

      if ( ssiPromotion.getPrimaryAudienceType().isAllActivePaxType() )
      {
        inAudience = true;
      }
      else if ( ssiPromotion.getPrimaryAudienceType().isSpecifyAudienceType() )
      {
        for ( Object object : ssiPromotion.getPrimaryAudiences() )
        {
          Audience audience = (Audience)object;
          if ( this.audienceService.isParticipantInAudience( userId, audience ) )
          {
            inAudience = true;
            break;
          }
        }
      }
    }
    return inAudience;
  }

  /**
   * Get the contest approvers by level
   * @param promotionId
   * @return
   */
  @Override
  public Map<String, Set<Participant>> getAllowedContestApprovers( Long promotionId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SSI_CONTEST_APP_LVL_1_AUDIENCE ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SSI_CONTEST_APP_LVL_2_AUDIENCE ) );
    SSIPromotion ssiPromotion = (SSIPromotion)promotionService.getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
    Set<Participant> level1Approvers = getLevel1ContestApproversParticipants( ssiPromotion.getContestApprovalLevel1Audiences() );
    Set<Participant> level2Approvers = getLevel2ContestApproversParticipants( ssiPromotion.getContestApprovalLevel2Audiences() );
    Map<String, Set<Participant>> contestApprovers = new HashMap<String, Set<Participant>>();
    contestApprovers.put( "contest_approver_level_1", level1Approvers );
    contestApprovers.put( "contest_approver_level_2", level2Approvers );
    return contestApprovers;
  }

  private Set<Participant> getLevel1ContestApproversParticipants( Set<SSIPromotionContestApprovalLevel1Audience> level1ApproverAudiences )
  {
    Set<Participant> level1Approvers = new HashSet<Participant>();
    List<Long> audienceIdList = new ArrayList<Long>();
    for ( Iterator<SSIPromotionContestApprovalLevel1Audience> iter = level1ApproverAudiences.iterator(); iter.hasNext(); )
    {
      audienceIdList.add( iter.next().getAudience().getId() );
    }
    if ( audienceIdList.size() > 0 )
    {
      List<Participant> paxList = participantService.getAllParticipantsByAudienceIds( audienceIdList );
      for ( Iterator<Participant> iter = paxList.iterator(); iter.hasNext(); )
      {
        Participant pax = iter.next();
        if ( !UserManager.getUserId().equals( pax.getId() ) )
        {
          level1Approvers.add( pax );
        }
      }
    }
    return level1Approvers;
  }

  private Set<Participant> getLevel2ContestApproversParticipants( Set<SSIPromotionContestApprovalLevel2Audience> level2ApproverAudiences )
  {
    Set<Participant> level2Approvers = new HashSet<Participant>();
    List<Long> audienceIdList = new ArrayList<Long>();
    for ( Iterator<SSIPromotionContestApprovalLevel2Audience> iter = level2ApproverAudiences.iterator(); iter.hasNext(); )
    {
      audienceIdList.add( iter.next().getAudience().getId() );
    }
    if ( audienceIdList.size() > 0 )
    {
      List<Participant> paxList = participantService.getAllParticipantsByAudienceIds( audienceIdList );
      for ( Iterator<Participant> iter = paxList.iterator(); iter.hasNext(); )
      {
        Participant pax = iter.next();
        if ( !UserManager.getUserId().equals( pax.getId() ) )
        {
          level2Approvers.add( pax );
        }
      }
    }
    return level2Approvers;
  }

  public SSIFileUpload uploadPdfForSSIGuide( SSIFileUpload data ) throws ServiceErrorException
  {
    if ( data.getType().equalsIgnoreCase( "pdf" ) || data.getType().equalsIgnoreCase( "doc" ) || data.getType().equalsIgnoreCase( "docx" ) )
    {
      try
      {
        data.setFull( ImageUtils.getSSIContestGuidePath( data.getType(), data.getId(), data.getName() ) );
        if ( AwsUtils.isAws() )
        {
          awsFileUploadStrategy.uploadFileData( data.getFull(), data.getData() );
        }
        else
        {
          appDataDirFileUploadStrategy.uploadFileData( data.getFull(), data.getData() );
        }
      }
      catch( Exception e )
      {
        throw new ServiceErrorException( "quiz.learning.IMAGE_UPLOAD_FAILED:" + e );
      }
    }
    else
    {
      throw new ServiceErrorException( "quiz.learning.PDF_UPLOAD_INVALID" );
    }
    return data;
  }

  public boolean moveFileToWebdav( String mediaUrl ) throws ServiceErrorException
  {
    try
    {
      byte[] media = appDataDirFileUploadStrategy.getFileData( mediaUrl );
      webdavFileUploadStrategy.uploadFileData( mediaUrl, media );
      appDataDirFileUploadStrategy.delete( mediaUrl );
      return true;
    }
    catch( ServiceErrorException see )
    {
      throw see;
    }
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setAppDataDirFileUploadStrategy( FileUploadStrategy appDataDirFileUploadStrategy )
  {
    this.appDataDirFileUploadStrategy = appDataDirFileUploadStrategy;
  }

  public void setWebdavFileUploadStrategy( FileUploadStrategy webdavFileUploadStrategy )
  {
    this.webdavFileUploadStrategy = webdavFileUploadStrategy;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setAwsFileUploadStrategy( FileUploadStrategy awsFileUploadStrategy )
  {
    this.awsFileUploadStrategy = awsFileUploadStrategy;
  }

  public String buildSSIContestGuideUrl( String filePath )
  {
    String docUrlPath = getSSIContestGuideUrl( filePath );
    String targetUrl = getHtmlPdfString( docUrlPath );

    return targetUrl;
  }

  public String getSSIContestGuideUrl( String filePath )
  {
    String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = systemVariableService.getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    if ( AwsUtils.isAws() )
    {
      siteUrlPrefix = systemVariableService.getContextName();
    }
    return siteUrlPrefix + "-cm/cm3dam" + '/' + filePath;
  }

  private String getHtmlPdfString( String url )
  {
    StringBuilder htmlPdfString = new StringBuilder();

    if ( url.contains( "</a>" ) )
    {
      htmlPdfString.append( url );
    }
    else
    {
      htmlPdfString.append( "<a target='_blank' href='" + url + "'>" );
      htmlPdfString.append( "guide link" );
      htmlPdfString.append( "</a>" );
    }
    htmlPdfString.append( "<br>" );

    return htmlPdfString.toString();
  }

}
