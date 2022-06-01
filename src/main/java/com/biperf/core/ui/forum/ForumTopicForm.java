/**
 * 
 */

package com.biperf.core.ui.forum;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.AudienceType;
import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ServiceLocator;
import com.biperf.core.value.forum.ForumAudienceFormBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author poddutur
 * 
 */
public class ForumTopicForm extends BaseForm
{
  /**
   * 
   */
  private static final long serialVersionUID = -1990007655451713358L;

  public static final String FORM_NAME = "forumTopicForm";

  public static final String PRIMARY = "primary";

  public static final String SPECIFY_AUDIENCE = "specify audience";

  private Long id;
  private String topicCmAssetCode;
  private String audienceType;
  private String stickyStartDate;
  private String stickyEndDate;
  private Date lastActivityDate;
  private String sortOrder;
  private String status;
  private String sticky;
  private String specifyOrder;
  private String primaryAudienceId;
  private String method;
  private String topicscmAsset;
  private Long userId;

  private List<ForumAudienceFormBean> primaryAudienceList = new ArrayList<ForumAudienceFormBean>();

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public List<ForumAudienceFormBean> getPrimaryAudienceListAsList()
  {
    return primaryAudienceList;
  }

  public void setPrimaryAudienceListAsList( List<ForumAudienceFormBean> primaryAudienceList )
  {
    this.primaryAudienceList = primaryAudienceList;
  }

  public String getPrimaryAudienceId()
  {
    return primaryAudienceId;
  }

  public void setPrimaryAudienceId( String primaryAudienceId )
  {
    this.primaryAudienceId = primaryAudienceId;
  }

  public String getTopicscmAsset()
  {
    return topicscmAsset;
  }

  public void setTopicscmAsset( String topicscmAsset )
  {
    this.topicscmAsset = topicscmAsset;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getTopicCmAssetCode()
  {
    return topicCmAssetCode;
  }

  public void setTopicCmAssetCode( String topicCmAssetCode )
  {
    this.topicCmAssetCode = topicCmAssetCode;
  }

  public String getStickyStartDate()
  {
    return stickyStartDate;
  }

  public void setStickyStartDate( String stickyStartDate )
  {
    this.stickyStartDate = stickyStartDate;
  }

  public String getStickyEndDate()
  {
    return stickyEndDate;
  }

  public void setStickyEndDate( String stickyEndDate )
  {
    this.stickyEndDate = stickyEndDate;
  }

  public String getSticky()
  {
    return sticky;
  }

  public void setSticky( String sticky )
  {
    this.sticky = sticky;
  }

  public String getSpecifyOrder()
  {
    return specifyOrder;
  }

  public void setSpecifyOrder( String specifyOrder )
  {
    this.specifyOrder = specifyOrder;
  }

  public String getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( String audienceType )
  {
    this.audienceType = audienceType;
  }

  public Date getLastActivityDate()
  {
    return lastActivityDate;
  }

  public void setLastActivityDate( Date lastActivityDate )
  {
    this.lastActivityDate = lastActivityDate;
  }

  public String getSortOrder()
  {
    return sortOrder;
  }

  public void setSortOrder( String sortOrder )
  {
    this.sortOrder = sortOrder;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  /**
   * adds an audience item to the list
   *
   * @param audience
   * @param forumAudienceType
   */
  public void addAudience( Audience audience, String forumAudienceType )
  {
    ForumAudienceFormBean audienceFormBean = new ForumAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );
    primaryAudienceList.add( audienceFormBean );
  }

  /**
   * removes any items that have been selected to be removed from the list
   *
   * @param forumAudienceType
   */
  public void removeItems( String audienceType )
  {
    Iterator it = null;
    if ( audienceType.equals( PRIMARY ) )
    {
      it = getPrimaryAudienceListAsList().iterator();
    }

    if ( it != null )
    {
      while ( it.hasNext() )
      {
        ForumAudienceFormBean audienceFormBean = (ForumAudienceFormBean)it.next();
        if ( audienceFormBean.isRemoved() )
        {
          it.remove();
        }
      }
    }
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    primaryAudienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "primaryAudienceListCount" ) );
  }

  private List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty ForumAudienceFormBean
      ForumAudienceFormBean forumAudienceBean = new ForumAudienceFormBean();
      valueList.add( forumAudienceBean );
    }

    return valueList;
  }

  private void loadAudience( Set audienceList, String audienceType )
  {
    PromotionAudience promotionAudience = null;
    Iterator audienceIter = null;
    audienceIter = audienceList.iterator();
    while ( audienceIter.hasNext() )
    {
      promotionAudience = (PromotionAudience)audienceIter.next();

      ForumAudienceFormBean audienceBean = new ForumAudienceFormBean();
      audienceBean.setId( promotionAudience.getId() );
      audienceBean.setAudienceId( promotionAudience.getAudience().getId() );
      audienceBean.setName( promotionAudience.getAudience().getName() );
      audienceBean.setSize( promotionAudience.getAudience().getSize() );
      audienceBean.setAudienceType( promotionAudience.getAudience().getAudienceType().getCode() );
      audienceBean.setVersion( promotionAudience.getAudience().getVersion() );
      if ( audienceType.equals( PRIMARY ) )
      {
        primaryAudienceList.add( audienceBean );
      }
    }
  }

  /**
   * Gets a Set of the Primary Audiences
   * @return
   */
  public Set getPrimaryAudienceSet()
  {
    Set tempPrimaryAudSet = new LinkedHashSet(); // primaryAudienceList;
    for ( Iterator primAudIter = primaryAudienceList.iterator(); primAudIter.hasNext(); )
    {
      ForumAudienceFormBean audienceBean = (ForumAudienceFormBean)primAudIter.next();
      Audience audience = getAudienceService().getAudienceById( audienceBean.getAudienceId(), null );
      tempPrimaryAudSet.add( audience );
    }
    return tempPrimaryAudSet;
  }

  /**
   * Accessor for the number of ForumAudienceFormBean objects in the list.
   *
   * @return int
   */
  public int getPrimaryAudienceListCount()
  {
    if ( primaryAudienceList == null )
    {
      return 0;
    }

    return primaryAudienceList.size();
  }

  /**
   * Accessor for the value list
   *
   * @param index
   * @return Single instance of ForumAudienceFormBean from the value list
   */
  public ForumAudienceFormBean getPrimaryAudienceList( int index )
  {
    try
    {
      return (ForumAudienceFormBean)primaryAudienceList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public int getPrimaryAudienceCount()
  {
    return primaryAudienceList != null ? primaryAudienceList.size() : 0;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    // Verify that Topic Name is entered
    if ( StringUtils.isEmpty( this.topicCmAssetCode ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "forum.topic.details.TOPIC_NAME" ) ) );
    }

    if ( this.sticky.equals( "true" ) )
    {
      if ( DateUtils.toDate( stickyStartDate ) == null )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "forum.topic.details.START_DATE_REQUIRED" ) ) );
      }
      if ( DateUtils.toDate( stickyEndDate ) == null )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "forum.topic.details.END_DATE_REQUIRED" ) ) );
      }
    }

    // Verify that end date, if used, is not in the past
    if ( DateUtils.toDate( stickyEndDate ) != null )
    {
      if ( DateUtils.toDate( stickyEndDate ).before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
      {
        // The date is before current date
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_TO_DATE ) );
      }
    }

    // Verify that end date is after start date
    if ( DateUtils.toDate( stickyStartDate ) != null && DateUtils.toDate( stickyEndDate ) != null && DateUtils.toDate( stickyStartDate ).after( DateUtils.toDate( stickyEndDate ) ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_START_DATE ) );
    }

    if ( this.specifyOrder.equals( "true" ) )
    {
      validateSortOrderValue( actionErrors );
    }
    if ( this.audienceType.equals( SPECIFY_AUDIENCE ) )
    {
      if ( this.primaryAudienceList.size() == 0 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "forum.library.SELECT_ATLEAST_ONE_AUDIENCE" ) ) );
      }
    }

    return actionErrors;
  }

  private void validateSortOrderValue( ActionErrors errors )
  {
    if ( this.sortOrder == null || this.sortOrder.length() == 0 )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "forum.topic.details.SPECIFY_ORDER_VALUE_EMPTY_ERROR" ) ) );
    }
    else
    {
      try
      {
        int sOrd = Integer.parseInt( this.sortOrder );

        if ( sOrd <= 0 )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE,
                      new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "forum.topic.details.SPECIFY_ORDER_MIN_MAX_VALUE_ERROR" ) ) );
        }

        else if ( sOrd > 99 )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "forum.topic.details.SPECIFY_ORDER_MAX_VALUE_ERROR" ) ) );
        }
      }
      catch( NumberFormatException e )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.errors.INVALID_SORT_ORDER" ) );
      }
    }

  }

  // ---------------------------------------------------------------------------
  // Load and To Domain Methods
  // ---------------------------------------------------------------------------

  public void loadDomainObject( ForumTopic forumTopic )
  {
    this.setId( forumTopic.getId() );
    this.setTopicCmAssetCode( forumTopic.getTopicNameFromCM() );
    this.setAudienceType( forumTopic.getAudienceType() );
    this.setStickyStartDate( DateUtils.toDisplayString( forumTopic.getStickyStartDate() ) );
    this.setStickyEndDate( DateUtils.toDisplayString( forumTopic.getStickyEndDate() ) );
    this.setLastActivityDate( forumTopic.getLastActivityDate() );
    this.setTopicscmAsset( forumTopic.getTopicNameFromCM() );
    if ( forumTopic.getSortOrder() != null )
    {
      this.setSortOrder( forumTopic.getSortOrder().toString() );
    }
    this.setStatus( forumTopic.getStatus() );
    this.setSpecifyOrder( forumTopic.getSortOrder() != null ? "true" : "false" );
    this.setSticky( forumTopic.getStickyEndDate() != null ? "true" : "false" );
  }

  public void loadDomainAudienceObject( List<ForumAudienceFormBean> forumAudienceFormBeanList )
  {
    this.setPrimaryAudienceListAsList( forumAudienceFormBeanList );
  }

  public ForumTopic toDomainObject()
  {
    ForumTopic forumTopic = new ForumTopic();
    populateDomainObject( forumTopic );
    return forumTopic;
  }

  public void populateDomainObject( ForumTopic forumTopic )
  {
    forumTopic.setId( isNewForumTopic() ? null : id );
    forumTopic.setTopicCmAssetCode( this.topicCmAssetCode );
    forumTopic.setAudienceType( this.audienceType );
    forumTopic.setSticky( this.getSticky().equals( "false" ) ? false : true );
    forumTopic.setSpecifyOrder( this.getSpecifyOrder().equals( "false" ) ? false : true );
    if ( forumTopic.getSticky() )
    {
      forumTopic.setStickyStartDate( DateUtils.toDate( this.stickyStartDate ) );
      forumTopic.setStickyEndDate( DateUtils.toDate( this.stickyEndDate ) );
    }
    else
    {
      forumTopic.setStickyStartDate( null );
      forumTopic.setStickyEndDate( null );
    }
    forumTopic.setLastActivityDate( null );
    if ( forumTopic.getSpecifyOrder() )
    {
      forumTopic.setSortOrder( Long.parseLong( this.sortOrder ) );
    }
    else
    {
      forumTopic.setSortOrder( null );
    }

    forumTopic.setStatus( "A" );

    if ( AudienceType.ALL_ACTIVE_PAX.equals( audienceType ) )
    {
      forumTopic.setAllActivePaxEntry( true );
    }
    else
    {
      forumTopic.setAllActivePaxEntry( false );
    }

    if ( AudienceType.SPECIFY_AUDIENCE.equals( audienceType ) )
    {
      forumTopic.setSpecifyAudienceEntry( true );
    }
    else
    {
      forumTopic.setSpecifyAudienceEntry( true );
    }

  }

  public void loadDefaultParameters( ForumTopic forumTopic )
  {
    this.setSpecifyOrder( forumTopic.getSortOrder() != null ? "true" : "false" );
    this.setSticky( forumTopic.getStickyEndDate() != null ? "true" : "false" );
    this.setAudienceType( forumTopic.getAudienceType() != null ? "specify audience" : "all active participants" );
  }

  /**
   * Returns true if the user is creating a new forum topic; returns false if the user is editing
   * an existing forum topic.
   * 
   * @return true if the user is creating a new forum topic; return false if the user is editing
   *         an existing forum topic.
   */
  private boolean isNewForumTopic()
  {
    return id == null || id.longValue() == 0;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)ServiceLocator.getService( AudienceService.BEAN_NAME );
  }

}
