
package com.biperf.core.ui.participant;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.PurlContributorCommentStatus;
import com.biperf.core.domain.enums.PurlContributorMediaStatus;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ArrayUtil;

public class PurlAdminMaintainAction extends BaseDispatchAction
{

  /**
   * Delete under construction promotions
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deletePurlSubmissionContent( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    PurlAdminMaintainForm purlAdminMaintainForm = (PurlAdminMaintainForm)form;

    List<Long> commentIdsList = null;

    if ( purlAdminMaintainForm.getDeleteComments() != null && purlAdminMaintainForm.getDeleteComments().length > 0 )
    {
      commentIdsList = convertStringArrayToList( purlAdminMaintainForm.getDeleteComments() );

      Iterator<Long> promotionIdIter = commentIdsList.iterator();

      while ( promotionIdIter.hasNext() )
      {
        this.deleteComment( promotionIdIter.next() );
      }
    }

    if ( purlAdminMaintainForm.getDeletePhotos() != null && purlAdminMaintainForm.getDeletePhotos().length > 0 )
    {
      List<Long> photoIdsList = convertStringArrayToList( purlAdminMaintainForm.getDeletePhotos() );

      Iterator<Long> promotionIdIter = photoIdsList.iterator();

      while ( promotionIdIter.hasNext() )
      {
        Long photoId = promotionIdIter.next();
        if ( commentIdsList != null && !commentIdsList.contains( photoId ) )
        {
          this.deletePhoto( photoId );
        }
        else
        {
          this.deletePhoto( photoId );
        }
      }
    }

    if ( purlAdminMaintainForm.getDeleteVideos() != null && purlAdminMaintainForm.getDeleteVideos().length > 0 )
    {
      List<Long> videoIdsList = convertStringArrayToList( purlAdminMaintainForm.getDeleteVideos() );

      Iterator<Long> promotionIdIter = videoIdsList.iterator();

      while ( promotionIdIter.hasNext() )
      {
        Long videoId = promotionIdIter.next();
        if ( commentIdsList != null && !commentIdsList.contains( videoId ) )
        {
          this.deleteVideo( videoId );
        }
        else
        {
          this.deleteVideo( videoId );
        }
      }
    }

    return forward;
  }

  private void deleteComment( Long purlContributorCommentId )
  {
    PurlContributorComment comment = getPurlContributorComment( purlContributorCommentId );

    comment.setStatus( PurlContributorCommentStatus.lookup( PurlContributorCommentStatus.INACTIVE ) );
    comment.setImageStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.INACTIVE ) );
    comment.setVideoStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.INACTIVE ) );

    getPurlService().savePurlContributorComment( comment );
  }

  private void deletePhoto( Long purlContributorCommentId )
  {
    PurlContributorComment comment = getPurlContributorComment( purlContributorCommentId );
    comment.setImageStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.INACTIVE ) );
    getPurlService().savePurlContributorComment( comment );
  }

  private void deleteVideo( Long purlContributorCommentId )
  {
    PurlContributorComment comment = getPurlContributorComment( purlContributorCommentId );
    comment.setVideoStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.INACTIVE ) );
    getPurlService().savePurlContributorComment( comment );
  }

  private List<Long> convertStringArrayToList( String[] ids )
  {
    // Convert String[] of purl contributor comment ids to Long[]
    List<Long> purlContributorCommentIds = ArrayUtil.convertStringArrayToListOfLongObjects( ids );
    return purlContributorCommentIds;
  }

  private PurlContributorComment getPurlContributorComment( Long id )
  {
    PurlContributorComment comment = getPurlService().getPurlContributorCommentById( id );
    return comment;
  }

  private PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }
}
