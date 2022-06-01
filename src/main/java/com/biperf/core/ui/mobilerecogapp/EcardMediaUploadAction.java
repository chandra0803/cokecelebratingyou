
package com.biperf.core.ui.mobilerecogapp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.multimedia.VideoUploadDetail;
import com.biperf.core.service.multimedia.MultimediaService;
import com.biperf.core.ui.recognition.EcardUploadAction;
import com.biperf.core.ui.recognition.EcardUploadBean;
import com.biperf.core.ui.recognition.EcardUploadForm;

public class EcardMediaUploadAction extends EcardUploadAction
{

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    EcardUploadForm form = (EcardUploadForm)actionForm;

    // go ahead and attempt the upload!
    EcardUploadBean status = super.uploadEcard( form.getUploadAnImage(), Math.round( Math.random() * 100000 ) );

    // Some mobile app trickery, here. The app cannot be updated to deal with being sent both a
    // video and preview image.
    // Instead, we are going to send them only the video (where the image would normally go) - the
    // app will know it's a video and show it that way.
    // We'll save both URLs in a table, and when the recognition is submitted, we'll have one we can
    // use to look the other up.
    if ( StringUtils.isNotBlank( status.getVideoUrl() ) )
    {
      VideoUploadDetail videoUploadDetail = new VideoUploadDetail();
      videoUploadDetail.setVideoUrl( status.getVideoUrl() );
      videoUploadDetail.setThumbnailUrl( status.getImageUrl() );
      getMultimediaService().saveVideoUploadDetail( videoUploadDetail );

      WebErrorMessage message = status.getMessages() != null && !status.getMessages().isEmpty() ? status.getMessages().get( 0 ) : null;
      status = new EcardUploadBean( message, status.isSuccess(), status.getOwnCardName(), videoUploadDetail.getVideoUrl(), videoUploadDetail.getThumbnailUrl() );
    }

    writeJson( status, response );
    return null;
  }

  @Override
  protected void writeJson( Object bean, HttpServletResponse response ) throws IOException
  {
    super.writeAsJsonToResponse( bean, response );
  }

  private MultimediaService getMultimediaService()
  {
    return (MultimediaService)getService( MultimediaService.BEAN_NAME );
  }
}
