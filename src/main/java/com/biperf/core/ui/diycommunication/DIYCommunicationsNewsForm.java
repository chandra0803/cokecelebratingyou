
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

@SuppressWarnings( "serial" )
public class DIYCommunicationsNewsForm extends BaseForm
{

  public static final String NAME = "diyCommunicationsNewsForm";

  private String newsStartDate;
  private String newsEndDate;
  private Map<Integer, AudienceResults> audienceSelGroup = new HashMap<Integer, AudienceResults>();
  private Map<Integer, ResourceList> newsContent = new HashMap<Integer, ResourceList>();
  private Long diyCommunicationsId;
  private Long newsId;
  private String chooseLanguage = UserManager.getLocale().toString();
  private String defaultLanguage;
  private String method;
  private String newsHeadline;
  private String comments;

  // upload image fields
  private FormFile image;
  private FormFile replacementImage;
  private String sizes;

  public String getNewsStartDate()
  {
    return newsStartDate;
  }

  public void setNewsStartDate( String newsStartDate )
  {
    this.newsStartDate = newsStartDate;
  }

  public String getNewsEndDate()
  {
    return newsEndDate;
  }

  public void setNewsEndDate( String newsEndDate )
  {
    this.newsEndDate = newsEndDate;
  }

  public void setDiyCommunicationsId( Long diyCommunicationsId )
  {
    this.diyCommunicationsId = diyCommunicationsId;
  }

  public Long getDiyCommunicationsId()
  {
    return diyCommunicationsId;
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

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
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

  public ResourceList getNewsContent( int index )
  {
    ResourceList bean = newsContent.get( index );
    if ( bean == null )
    {
      bean = new ResourceList();
      newsContent.put( index, bean );
    }
    return bean;
  }

  public List<ResourceList> getNewsContentList()
  {
    return new ArrayList<ResourceList>( newsContent.values() );
  }

  public FormFile getImage()
  {
    return image;
  }

  public void setImage( FormFile image )
  {
    this.image = image;
  }

  public FormFile getReplacementImage()
  {
    return replacementImage;
  }

  public void setReplacementImage( FormFile replacementImage )
  {
    this.replacementImage = replacementImage;
  }

  public Long getNewsId()
  {
    return newsId;
  }

  public void setNewsId( Long newsId )
  {
    this.newsId = newsId;
  }

  public String getNewsHeadline()
  {
    return newsHeadline;
  }

  public void setNewsHeadline( String newsHeadline )
  {
    this.newsHeadline = newsHeadline;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public String getSizes()
  {
    return sizes;
  }

  public void setSizes( String sizes )
  {
    this.sizes = sizes;
  }

  public DIYCommunications toDomain( DIYCommunications diyCommunications, String defaultLanguage )
  {
    for ( ResourceList resource : getNewsContentList() )
    {
      if ( defaultLanguage != null && resource.getLanguage().equals( defaultLanguage ) )
      {
        diyCommunications.setContentTitle( resource.getHeadline() );
        break;
      }
    }
    diyCommunications.setStartDate( DateUtils.toDate( this.newsStartDate ) );
    diyCommunications.setEndDate( DateUtils.toDate( this.newsEndDate ) );
    diyCommunications.setCommunicationType( "news" );
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
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "diyCommunications.errors.AUDIENCE_REQUIRED" ) );
    }

    if ( this.newsContent == null || this.newsContent.isEmpty() )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "diyCommunications.errors.CONTENT_REQUIRED" ) );
    }

    if ( getNewsContentList() != null && !getNewsContentList().isEmpty() )
    {
      Set<String> valuesSet = new HashSet<String>();

      for ( Iterator<ResourceList> iter = getNewsContentList().iterator(); iter.hasNext(); )
      {
        ResourceList resourceLocaleCode = iter.next();
        valuesSet.add( resourceLocaleCode.getLanguage() );
      }
      if ( valuesSet.size() != getNewsContentList().size() )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "diyCommunications.errors.DUPLICATE_CONTENT" ) );
      }
    }

    Date startDate;
    Date endDate;
    try
    {
      startDate = DateUtils.toStartDate( newsStartDate );
      endDate = DateUtils.toEndDate( newsEndDate );

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
      request.setAttribute( "serverReturnedErrorForNews", Boolean.TRUE );
    }
    return errors;
  }

}
