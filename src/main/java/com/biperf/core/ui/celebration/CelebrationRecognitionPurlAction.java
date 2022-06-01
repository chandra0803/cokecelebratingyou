/**
 * 
 */

package com.biperf.core.ui.celebration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.ui.celebration.CelebrationPurlCommentsTileView.CommentsView;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * @author poddutur
 *
 */
public class CelebrationRecognitionPurlAction extends BaseCelebrationAction
{
  private static final Log logger = LogFactory.getLog( CelebrationRecognitionPurlAction.class );

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return this.display( mapping, form, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      Long claimId = getClaimId( request );
      int rowNumStart = 0;
      int rowNumEnd = 4;
      if ( claimId != null )
      {
        List<PurlContributorComment> purlCommentsList = new ArrayList<PurlContributorComment>();
        PurlRecipient purlRecipient = getPurlService().getPurlRecipientByClaimId( claimId );
        purlCommentsList = getPurlService().getComments( purlRecipient.getId(), true, rowNumStart, rowNumEnd );
        if ( purlCommentsList.size() > 0 )
        {
          super.writeAsJsonToResponse( buildJsonResponseForPurlContributorList( purlCommentsList ), response );
          return null;
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    catch( NumberFormatException nfe )
    {
      // do nothing.
    }
    catch( Exception e )
    {
      logger.error( "Error displaying celebration purl recognition tile, Exception: " + e );
    }

    return null;
  }

  private CelebrationPurlCommentsTileView buildJsonResponseForPurlContributorList( List<PurlContributorComment> purlCommentsList )
  {
    CelebrationPurlCommentsTileView purlContributorsCommentsTileView = new CelebrationPurlCommentsTileView();
    List<CommentsView> purlComments = new ArrayList<CommentsView>();
    for ( PurlContributorComment purlContributorComment : purlCommentsList )
    {
      CommentsView commentsView = new CommentsView();
      commentsView.setId( purlContributorComment.getId().toString() );
      PurlContributor purlContributor = purlContributorComment.getPurlContributor();
      if ( purlContributor != null )
      {
        commentsView.setFirstName( purlContributor.getFirstName() );
        commentsView.setLastName( purlContributor.getLastName() );
        commentsView.setHasAvatar( purlContributor.getDisplayAvatarUrl() != null ? true : false );
        commentsView.setAvatarUrl( purlContributor.getDisplayAvatarUrl() );
        commentsView.setComment( purlContributorComment.getComments() );
        commentsView.setHasImg( purlContributorComment.getImageUrl() != null ? true : false );
        commentsView.setImgUrl( purlContributorComment.getImageUrl() != null ? purlContributorComment.getImageUrl() : null ); // bug
                                                                                                                              // fix
      }
      commentsView.setHasVid( purlContributorComment.getVideoUrl() != null ? true : false );

      if ( Objects.nonNull( purlContributorComment.getVideoUrl() ) )
      {
        String eCardVideoLink = null;
        if ( purlContributorComment.getVideoUrl().contains( ActionConstants.REQUEST_ID ) )
        {
          MTCVideo mtcVideo = getMTCVideoService().getMTCVideoByRequestId( purlContributorComment.getRequestId( purlContributorComment.getVideoUrl() ) );

          if ( Objects.nonNull( mtcVideo ) )
          {
            eCardVideoLink = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();

          }
          else
          {
            eCardVideoLink = purlContributorComment.getActualCardUrl( purlContributorComment.getVideoUrl() );

          }

          eCardVideoLink = FilenameUtils.removeExtension( eCardVideoLink );

        }
        else
        {
          eCardVideoLink = purlContributorComment.getVideoUrl();
        }
        commentsView.setPurlUrl( eCardVideoLink );
      }
      purlComments.add( commentsView );
    }
    purlContributorsCommentsTileView.setPurlComments( purlComments );
    return purlContributorsCommentsTileView;
  }

  private static PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

  protected static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  protected static MTCVideoService getMTCVideoService()
  {
    return (MTCVideoService)getService( MTCVideoService.BEAN_NAME );
  }
}
