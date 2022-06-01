
package com.biperf.core.ui.diycommunication;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.diycommunication.AudienceResults;
import com.biperf.core.value.diycommunication.ResourceList;

public class DIYCommunicationsResourceCenterForm extends BaseForm
{
  private static final long serialVersionUID = 1L;
  public static final String NAME = "diyCommunicationsResourceCenterForm";

  private String resourceTitle;
  private String resourceStartDate;
  private String resourceEndDate;
  private Map<Integer, AudienceResults> audienceSelGroup = new HashMap<Integer, AudienceResults>();
  private Map<Integer, ResourceList> resourceContent = new HashMap<Integer, ResourceList>();
  private Long resourceContentId;
  private String chooseLanguage = UserManager.getLocale().toString();
  private String defaultLanguage;
  private String resourceContentLinkTitle;
  private String resourceCenterLink;
  private FormFile resourceContentDoc;
  private String resourceContentURL;
  private String method;
  private String data;

  public DIYCommunications toDomain( DIYCommunications diyCommunications )
  {
    diyCommunications.setContentTitle( this.resourceTitle );
    diyCommunications.setStartDate( DateUtils.toDate( this.resourceStartDate ) );
    diyCommunications.setEndDate( DateUtils.toDate( this.resourceEndDate ) );

    return diyCommunications;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    if ( this.audienceSelGroup == null || this.audienceSelGroup.isEmpty() )
    {
      // actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED",
      // CmsResourceBundle.getCmsBundle().getString( PROMO_MODULE_ASSET_KEY ) ) );
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "diyCommunications.errors.AUDIENCE_REQUIRED" ) );
    }

    if ( this.resourceContent == null || this.resourceContent.isEmpty() )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "diyCommunications.errors.CONTENT_REQUIRED" ) );
    }

    if ( getResourceContentList() != null && !getResourceContentList().isEmpty() )
    {
      Set<String> valuesSet = new HashSet<String>();

      for ( Iterator<ResourceList> iter = getResourceContentList().iterator(); iter.hasNext(); )
      {
        ResourceList resourceLocaleCode = iter.next();
        valuesSet.add( resourceLocaleCode.getLanguage() );
      }
      if ( valuesSet.size() != getResourceContentList().size() )
      {
        // actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
        // "survey.form.DESCRIPTION_SIZE_EXCEEDED" ) );
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "diyCommunications.errors.DUPLICATE_CONTENT" ) );
      }
    }

    Date startDate;
    Date endDate;
    try
    {
      startDate = DateUtils.toStartDate( resourceStartDate );
      endDate = DateUtils.toEndDate( resourceEndDate );

      if ( endDate.before( startDate ) )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "diyCommunications.errors.START_END_DATES" ) );
      }

      if ( endDate.before( DateUtils.toEndDate( new Date() ) ) )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "diyCommunications.errors.CURRENT_DATE" ) );
      }
    }
    catch( ParseException e )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "diyCommunications.errors.INVALID_DATE" ) );
    }

    if ( !errors.isEmpty() )
    {
      request.setAttribute( "serverReturnedErrorForResourceCenter", Boolean.TRUE );
    }

    return errors;
  }

  private Long diyCommunicationsId;

  public String getResourceTitle()
  {
    return resourceTitle;
  }

  public void setResourceTitle( String resourceTitle )
  {
    this.resourceTitle = resourceTitle;
  }

  public String getResourceStartDate()
  {
    return resourceStartDate;
  }

  public void setResourceStartDate( String resourceStartDate )
  {
    this.resourceStartDate = resourceStartDate;
  }

  public String getResourceEndDate()
  {
    return resourceEndDate;
  }

  public void setResourceEndDate( String resourceEndDate )
  {
    this.resourceEndDate = resourceEndDate;
  }

  public Long getResourceContentId()
  {
    return resourceContentId;
  }

  public void setResourceContentId( Long resourceContentId )
  {
    this.resourceContentId = resourceContentId;
  }

  public String getChooseLanguage()
  {
    return chooseLanguage;
  }

  public void setChooseLanguage( String chooseLanguage )
  {
    this.chooseLanguage = chooseLanguage;
  }

  public String getDefaultLanguage()
  {
    return defaultLanguage;
  }

  public void setDefaultLanguage( String defaultLanguage )
  {
    this.defaultLanguage = defaultLanguage;
  }

  public String getResourceContentLinkTitle()
  {
    return resourceContentLinkTitle;
  }

  public void setResourceContentLinkTitle( String resourceContentLinkTitle )
  {
    this.resourceContentLinkTitle = resourceContentLinkTitle;
  }

  public String getResourceCenterLink()
  {
    return resourceCenterLink;
  }

  public void setResourceCenterLink( String resourceCenterLink )
  {
    this.resourceCenterLink = resourceCenterLink;
  }

  public FormFile getResourceContentDoc()
  {
    return resourceContentDoc;
  }

  public void setResourceContentDoc( FormFile resourceContentDoc )
  {
    this.resourceContentDoc = resourceContentDoc;
  }

  public String getResourceContentURL()
  {
    return resourceContentURL;
  }

  public void setResourceContentURL( String resourceContentURL )
  {
    this.resourceContentURL = resourceContentURL;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getData()
  {
    return data;
  }

  public void setData( String data )
  {
    this.data = data;
  }

  public Long getDiyCommunicationsId()
  {
    return diyCommunicationsId;
  }

  public void setDiyCommunicationsId( Long diyCommunicationsId )
  {
    this.diyCommunicationsId = diyCommunicationsId;
  }

  public int getResourceContentCount()
  {
    if ( resourceContent == null || resourceContent.isEmpty() )
    {
      return 0;
    }
    return resourceContent.size();
  }

  public ResourceList getResourceContent( int index )
  {
    ResourceList bean = resourceContent.get( index );
    if ( bean == null )
    {
      bean = new ResourceList();
      resourceContent.put( index, bean );
    }
    return bean;
  }

  public List<ResourceList> getResourceContentList()
  {
    return new ArrayList<ResourceList>( resourceContent.values() );
  }

  public int getAudienceSelGroupCount()
  {
    if ( audienceSelGroup == null || audienceSelGroup.isEmpty() )
    {
      return 0;
    }
    return audienceSelGroup.size();
  }

  public AudienceResults getAudienceSelGroup( int index )
  {
    AudienceResults bean = audienceSelGroup.get( index );
    if ( bean == null )
    {
      bean = new AudienceResults();
      audienceSelGroup.put( index, bean );
    }
    return bean;
  }

  public List<AudienceResults> getAudienceSelGroupList()
  {
    return new ArrayList<AudienceResults>( audienceSelGroup.values() );
  }

}
