
package com.biperf.core.ui.purl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.purl.TranslatedPurlContributorComment;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;

public class PurlTranslateAction extends BaseDispatchAction
{

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    final Long PURL_RECIPIENT_ID = RequestUtils.getRequiredParamLong( request, "purlRecipientId" );

    List<TranslatedPurlContributorComment> translatedComments = getPurlService().getTranslatedContributorCommentsFor( PURL_RECIPIENT_ID );

    TranslateResponse translatedResponse = new TranslateResponse( translatedComments );

    super.writeAsJsonToResponse( translatedResponse, response );

    return null;
  }

  private PurlService getPurlService()
  {
    return (PurlService)BeanLocator.getBean( PurlService.BEAN_NAME );
  }

  private static class TranslateResponse
  {
    private final List<TranslatedPurlContributorComment> comments;

    public TranslateResponse( List<TranslatedPurlContributorComment> comments )
    {
      this.comments = comments;
    }

    public List<TranslatedPurlContributorComment> getComments()
    {
      return comments;
    }
  }
}
