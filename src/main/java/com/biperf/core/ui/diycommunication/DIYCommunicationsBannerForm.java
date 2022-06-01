
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
public class DIYCommunicationsBannerForm extends BaseForm
{

  public static final String NAME = "diyCommunicationsBannerForm";

  private String bannerTitle;
  private String bannerStartDate;
  private String bannerEndDate;
  private Map<Integer, AudienceResults> audienceSelGroup = new HashMap<Integer, AudienceResults>();
  private Map<Integer, ResourceList> bannerContent = new HashMap<Integer, ResourceList>();
  private Long diyCommunicationsId;
  private Long bannerId;
  private String chooseLanguage = UserManager.getLocale().toString();
  private String defaultLanguage;
  private String bannerLink;
  private FormFile bannerContentDoc;
  private String bannerURL;
  private String method;

  // upload image fields
  private FormFile image;
  private FormFile replacementImage;
  private String sizes;

  public String getBannerTitle()
  {
    return bannerTitle;
  }

  public void setBannerTitle( String bannerTitle )
  {
    this.bannerTitle = bannerTitle;
  }

  public String getBannerStartDate()
  {
    return bannerStartDate;
  }

  public void setBannerStartDate( String bannerStartDate )
  {
    this.bannerStartDate = bannerStartDate;
  }

  public String getBannerEndDate()
  {
    return bannerEndDate;
  }

  public void setBannerEndDate( String bannerEndDate )
  {
    this.bannerEndDate = bannerEndDate;
  }

  public void setDiyCommunicationsId( Long diyCommunicationsId )
  {
    this.diyCommunicationsId = diyCommunicationsId;
  }

  public Long getDiyCommunicationsId()
  {
    return diyCommunicationsId;
  }

  public Long getBannerId()
  {
    return bannerId;
  }

  public void setBannerId( Long bannerId )
  {
    this.bannerId = bannerId;
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

  public String getBannerLink()
  {
    return bannerLink;
  }

  public void setBannerLink( String bannerLink )
  {
    this.bannerLink = bannerLink;
  }

  public FormFile getBannerContentDoc()
  {
    return bannerContentDoc;
  }

  public void setBannerContentDoc( FormFile bannerContentDoc )
  {
    this.bannerContentDoc = bannerContentDoc;
  }

  public String getBannerURL()
  {
    return bannerURL;
  }

  public void setBannerURL( String bannerURL )
  {
    this.bannerURL = bannerURL;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getSizes()
  {
    return sizes;
  }

  public void setSizes( String sizes )
  {
    this.sizes = sizes;
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

  public ResourceList getBannerContent( int index )
  {
    ResourceList bean = bannerContent.get( index );
    if ( bean == null )
    {
      bean = new ResourceList();
      bannerContent.put( index, bean );
    }
    return bean;
  }

  public List<ResourceList> getBannerContentList()
  {
    return new ArrayList<ResourceList>( bannerContent.values() );
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

  public DIYCommunications toDomain( DIYCommunications diyCommunications )
  {
    diyCommunications.setContentTitle( this.bannerTitle );
    diyCommunications.setStartDate( DateUtils.toDate( this.bannerStartDate ) );
    diyCommunications.setEndDate( DateUtils.toDate( this.bannerEndDate ) );
    diyCommunications.setCommunicationType( "banner" );
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

    if ( this.bannerContent == null || this.bannerContent.isEmpty() )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "diyCommunications.errors.CONTENT_REQUIRED" ) );
    }

    if ( getBannerContentList() != null && !getBannerContentList().isEmpty() )
    {
      Set<String> valuesSet = new HashSet<String>();

      for ( Iterator<ResourceList> iter = getBannerContentList().iterator(); iter.hasNext(); )
      {
        ResourceList resourceLocaleCode = iter.next();
        valuesSet.add( resourceLocaleCode.getLanguage() );
      }
      if ( valuesSet.size() != getBannerContentList().size() )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "diyCommunications.errors.DUPLICATE_CONTENT" ) );
      }
    }

    Date startDate;
    Date endDate;
    try
    {
      startDate = DateUtils.toStartDate( bannerStartDate );
      endDate = DateUtils.toEndDate( bannerEndDate );

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
      request.setAttribute( "serverReturnedErrorForBanner", Boolean.TRUE );
    }

    return errors;
  }

}
