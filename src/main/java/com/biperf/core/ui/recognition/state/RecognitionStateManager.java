
package com.biperf.core.ui.recognition.state;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.ui.recognition.SendRecognitionForm;
import com.biperf.core.utils.StringUtil;

public class RecognitionStateManager
{
  private static final String STATE_SESSION_KEY = SendRecognitionForm.class.getName() + ".SESSION_KEY";
  private static final String STATE_REQUEST_KEY = "recognitionState";

  private RecognitionStateManager()
  {
  }

  public static SendRecognitionForm get( HttpServletRequest request )
  {
    SendRecognitionForm form = (SendRecognitionForm)request.getSession().getAttribute( STATE_SESSION_KEY );
    if ( form == null )
    {
      form = new SendRecognitionForm();
    }
    form.setComments( StringUtil.escapeXml( form.getComments() ) );
    return form;
  }

  public static SendRecognitionForm getFromSession( HttpServletRequest request )
  {
    return (SendRecognitionForm)request.getSession().getAttribute( STATE_SESSION_KEY );

  }

  public static void remove( HttpServletRequest request )
  {
    request.getSession().removeAttribute( STATE_SESSION_KEY );
  }

  public static void store( SendRecognitionForm form, HttpServletRequest request )
  {
    if ( form != null )
    {
      request.getSession().setAttribute( STATE_SESSION_KEY, form );
    }
  }

  public static void addToRequest( SendRecognitionForm form, HttpServletRequest request )
  {
    request.setAttribute( STATE_REQUEST_KEY, form );
  }
}
