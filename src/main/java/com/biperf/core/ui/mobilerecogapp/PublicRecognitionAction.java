
package com.biperf.core.ui.mobilerecogapp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.RecognitionClaimSource;

public class PublicRecognitionAction extends com.biperf.core.ui.publicrecognition.PublicRecognitionAction
{
  @Override
  public ActionForward fetchPublicRecognitions( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    return super.fetchPublicRecognitions( mapping, form, request, response );
  }

  @Override
  protected RecognitionClaimSource getRecognitionClaimSource()
  {
    return RecognitionClaimSource.MOBILE;
  }
}
