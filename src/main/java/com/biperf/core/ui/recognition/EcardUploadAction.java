
package com.biperf.core.ui.recognition;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.utils.ImageUtils;

public class EcardUploadAction extends BaseRecognitionAction
{
  @SuppressWarnings( "unused" )
  private static final Log logger = LogFactory.getLog( EcardUploadAction.class );

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    EcardUploadForm form = (EcardUploadForm)actionForm;

    // Allowing for either image or video. Currently, video will be directed same as images -
    // eventually separate
    EcardUploadFileType fileType = null;
    if ( ImageUtils.isImageTypeValid( getFileExtension( form.getUploadAnImage().getFileName() ) ) )
    {
      fileType = EcardUploadFileType.IMAGE;
    }
    else if ( SupportedEcardVideoTypes.isSupported( getFileExtension( form.getUploadAnImage().getFileName() ) ) )
    {
      fileType = EcardUploadFileType.VIDEO;
    }
    else
    {
      writeJson( new EcardUploadBean( createUploadModalWebErrorMessage() ), response );
      return null;
    }

    // go ahead and attempt the upload!
    EcardUploadBean status = super.uploadEcard( form.getUploadAnImage(), Math.round( Math.random() * 100000 ) );

    // this was posted via a hidden iframe, so use html as the response content type
    // so as not to confuse the browser
    writeJson( status, response );
    return null;
  }

  // Override the method in BaseDispatch to not set application/json contentType
  protected void writeJson( Object bean, HttpServletResponse response ) throws IOException
  {
    super.writeAsJsonToResponse( bean, response, ContentType.HTML );
  }
}
