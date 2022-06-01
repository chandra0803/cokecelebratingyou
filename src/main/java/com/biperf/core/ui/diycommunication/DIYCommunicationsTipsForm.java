
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

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.diycommunication.AudienceResults;
import com.biperf.core.value.diycommunication.ResourceList;

public class DIYCommunicationsTipsForm extends BaseForm
{
  private static final long serialVersionUID = 1L;
  public static final String NAME = "diyCommunicationsTipsForm";

  private String method;
  private String data;
  private String tipTitle;
  private String tipStartDate;
  private String tipEndDate;
  private Long tipId;
  private String chooseLanguage = UserManager.getLocale().toString();
  private String defaultLanguage;
  private String contribCommentInp;
  private Map<Integer, AudienceResults> audienceSelGroup = new HashMap<Integer, AudienceResults>();
  private Map<Integer, ResourceList> tipContent = new HashMap<Integer, ResourceList>();

  public DIYCommunications toDomain( DIYCommunications diyCommunications )
  {
    diyCommunications.setContentTitle( this.tipTitle );
    diyCommunications.setStartDate( DateUtils.toDate( this.tipStartDate ) );
    diyCommunications.setEndDate( DateUtils.toDate( this.tipEndDate ) );

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

    if ( this.tipContent == null || this.tipContent.isEmpty() )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "diyCommunications.errors.CONTENT_REQUIRED" ) );
    }

    if ( getTipContentList() != null && !getTipContentList().isEmpty() )
    {
      Set<String> valuesSet = new HashSet<String>();

      for ( Iterator<ResourceList> iter = getTipContentList().iterator(); iter.hasNext(); )
      {
        ResourceList resourceLocaleCode = iter.next();
        valuesSet.add( resourceLocaleCode.getLanguage() );
      }
      if ( valuesSet.size() != getTipContentList().size() )
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
      startDate = DateUtils.toStartDate( tipStartDate );
      endDate = DateUtils.toEndDate( tipEndDate );

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
      request.setAttribute( "serverReturnedErrorForTips", Boolean.TRUE );
    }

    return errors;
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

  public String getTipTitle()
  {
    return tipTitle;
  }

  public void setTipTitle( String tipTitle )
  {
    this.tipTitle = tipTitle;
  }

  public String getTipStartDate()
  {
    return tipStartDate;
  }

  public void setTipStartDate( String tipStartDate )
  {
    this.tipStartDate = tipStartDate;
  }

  public String getTipEndDate()
  {
    return tipEndDate;
  }

  public void setTipEndDate( String tipEndDate )
  {
    this.tipEndDate = tipEndDate;
  }

  public Long getTipId()
  {
    return tipId;
  }

  public void setTipId( Long tipId )
  {
    this.tipId = tipId;
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

  public String getContribCommentInp()
  {
    return contribCommentInp;
  }

  public void setContribCommentInp( String contribCommentInp )
  {
    this.contribCommentInp = contribCommentInp;
  }

  public int getTipCount()
  {
    if ( tipContent == null || tipContent.isEmpty() )
    {
      return 0;
    }
    return tipContent.size();
  }

  public ResourceList getTipContent( int index )
  {
    ResourceList bean = tipContent.get( index );
    if ( bean == null )
    {
      bean = new ResourceList();
      tipContent.put( index, bean );
    }
    return bean;
  }

  public List<ResourceList> getTipContentList()
  {
    return new ArrayList<ResourceList>( tipContent.values() );
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
