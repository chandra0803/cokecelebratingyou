
package com.biperf.core.ui.purl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.PurlContributorInviteValue;
import com.biperf.core.value.PurlMediaUploadValue;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PurlContributionForm extends BaseActionForm
{
  private static final int INVITE_DEFAULT_SIZE = 1000;
  private static final int MEDIA_DEFAULT_SIZE = 20;

  private PurlContributor purlContributor;
  private PurlContributorComment comment;
  private FormFile fileAsset;
  private List<PurlContributorInviteValue> invites;
  private List<PurlMediaUploadValue> mediaUploads;

  private String mediaUrl;
  private String globalUniqueId;

  private String method;
  private String data;
  
  /*Customization for WIP 32479 starts here*/
  private String emailAddress;
  private String captchaResponse;
  /*Customization for WIP 32479 ends here*/
  
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );

    purlContributor = new PurlContributor();
    purlContributor.setPurlRecipient( new PurlRecipient() );

    comment = new PurlContributorComment();

    invites = getEmptyInvites( INVITE_DEFAULT_SIZE );
    mediaUploads = getEmptyMediaUploads( MEDIA_DEFAULT_SIZE );
  }
  
  /*Customization for WIP 32479 starts here*/
  
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( "unsubscribe".equals( method ) )
    {
      if ( StringUtil.isEmpty( emailAddress ) )
      {
        actionErrors.add( "emailAddress", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.EMAIL_ADDRESS" ) ) );
      }
      else if ( !GenericValidator.isEmail( this.emailAddress ) )
      {
        actionErrors.add( "emailAddress", new ActionMessage( "system.errors.EMAIL", CmsResourceBundle.getCmsBundle().getString( "self.enrollment.pax.registration.EMAIL_ADDRESS" ) ) );
      }
      
      if ( null == captchaResponse || captchaResponse.length() == 0 )
      {
        actionErrors.add( "captchaResponse", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "purl.tnc.error.params.CAPTCHA_CODE" ) ) );
      }
    }

    return actionErrors;
  }
  
  /*Customization for WIP 32479 ends here*/
  private List<PurlContributorInviteValue> getEmptyInvites( int count )
  {
    List<PurlContributorInviteValue> list = new ArrayList<PurlContributorInviteValue>();
    for ( int i = 0; i < count; ++i )
    {
      list.add( new PurlContributorInviteValue() );
    }
    return list;
  }

  private List<PurlMediaUploadValue> getEmptyMediaUploads( int count )
  {
    List<PurlMediaUploadValue> list = new ArrayList<PurlMediaUploadValue>();
    for ( int i = 0; i < count; ++i )
    {
      list.add( new PurlMediaUploadValue() );
    }
    return list;
  }

  public PurlContributor getPurlContributor()
  {
    return purlContributor;
  }

  public void setPurlContributor( PurlContributor purlContributor )
  {
    this.purlContributor = purlContributor;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public void setData( String data )
  {
    this.data = data;
  }

  public String getData()
  {
    return data;
  }

  public FormFile getFileAsset()
  {
    return fileAsset;
  }

  public void setFileAsset( FormFile fileAsset )
  {
    this.fileAsset = fileAsset;
  }

  public List<PurlContributorInviteValue> getInvites()
  {
    return invites;
  }

  public void setInvites( List<PurlContributorInviteValue> invites )
  {
    this.invites = invites;
  }

  public List<PurlMediaUploadValue> getMediaUploads()
  {
    return mediaUploads;
  }

  public void setMediaUploads( List<PurlMediaUploadValue> mediaUploads )
  {
    this.mediaUploads = mediaUploads;
  }

  public PurlContributorComment getComment()
  {
    return comment;
  }

  public void setComment( PurlContributorComment comment )
  {
    this.comment = comment;
  }

  public String getGlobalUniqueId()
  {
    return globalUniqueId;
  }

  public void setGlobalUniqueId( String globalUniqueId )
  {
    this.globalUniqueId = globalUniqueId;
  }

  public String getMediaUrl()
  {
    return mediaUrl;
  }

  public void setMediaUrl( String mediaUrl )
  {
    this.mediaUrl = mediaUrl;
  }

  /*Customization for WIP 32479 starts here*/
  
  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public String getCaptchaResponse()
  {
    return captchaResponse;
  }

  public void setCaptchaResponse( String captchaResponse )
  {
    this.captchaResponse = captchaResponse;
  }
  
  /*Customization for WIP 32479 ends here*/
}
