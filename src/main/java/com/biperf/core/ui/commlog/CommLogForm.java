/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/commlog/CommLogForm.java,v $
 */

package com.biperf.core.ui.commlog;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.domain.commlog.CommLogComment;
import com.biperf.core.domain.enums.CommLogCategoryType;
import com.biperf.core.domain.enums.CommLogReasonType;
import com.biperf.core.domain.enums.CommLogSourceType;
import com.biperf.core.domain.enums.CommLogStatusType;
import com.biperf.core.domain.enums.CommLogUrgencyType;
import com.biperf.core.domain.user.User;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

/**
 * CommLogForm.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author </th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Ashok Attada</td>
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CommLogForm extends BaseForm
{
  public static final String FORM_NAME = "commLogForm";

  private String method;
  private String userId;

  private Long commLogId;
  private String statusType;
  private String sourceType;
  private String categoryType;
  private String reasonType;
  private String urgencyType;
  private String newComment;
  private String commentHistory;

  private String assignedToUserId;
  private String assignedByUserId;

  private User user;
  private String assignedToName;

  private String statusDesc;
  private String sourceDesc;
  private String categoryDesc;
  private String reasonDesc;
  private String urgencyDesc;

  private long version;
  private long id;
  private long dateCreated;
  private String createdBy;

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

    if ( sourceType == null || "".equals( sourceType ) )
    {
      actionErrors.add( "sourceType", new ActionMessage( "communication_log.errors.SOURCE_REQUIRED" ) );
    }

    if ( categoryType == null || "".equals( categoryType ) )
    {
      actionErrors.add( "categoryType", new ActionMessage( "communication_log.errors.CATEGORY_REQUIRED" ) );
    }

    if ( reasonType == null || "".equals( reasonType ) )
    {
      actionErrors.add( "reasonType", new ActionMessage( "communication_log.errors.REASON_REQUIRED" ) );
    }

    if ( statusType == null || "".equals( statusType ) )
    {
      actionErrors.add( "statusType", new ActionMessage( "communication_log.errors.STATUS_REQUIRED" ) );
    }

    if ( urgencyType == null || "".equals( urgencyType ) )
    {
      actionErrors.add( "urgencyType", new ActionMessage( "communication_log.errors.URGENCY_REQUIRED" ) );
    }

    if ( newComment == null || "".equals( newComment ) )
    {
      actionErrors.add( "newComment", new ActionMessage( "communication_log.errors.COMMENTS_REQUIRED" ) );
    }

    return actionErrors;
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // empty block
  } // end reset

  /**
   * Load the values from the CommLog to the form
   * 
   * @param commLog
   */

  public void load( CommLog commLog )
  {
    this.commLogId = commLog.getId();
    if ( commLog.getUser() != null )
    {
      this.userId = String.valueOf( commLog.getUser().getId() );
    }

    this.sourceType = commLog.getCommLogSourceType().getCode();
    this.sourceDesc = commLog.getCommLogSourceType().getName();

    this.categoryType = commLog.getCommLogCategoryType().getCode();
    this.categoryDesc = commLog.getCommLogCategoryType().getName();

    this.reasonType = commLog.getCommLogReasonType().getCode();
    this.reasonDesc = commLog.getCommLogReasonType().getName();

    this.statusType = commLog.getCommLogStatusType().getCode();
    this.statusDesc = commLog.getCommLogStatusType().getName();

    this.urgencyType = commLog.getCommLogUrgencyType().getCode();
    this.urgencyDesc = commLog.getCommLogUrgencyType().getName();

    if ( commLog.getAssignedByUser() != null )
    {
      this.assignedByUserId = commLog.getAssignedByUser().getId().toString();
    }

    if ( commLog.getComments() != null )
    {
      Iterator commentsIterator = commLog.getComments().iterator();
      this.commentHistory = "";
      while ( commentsIterator.hasNext() )
      {
        CommLogComment commLogComment = (CommLogComment)commentsIterator.next();
        this.commentHistory += DateUtils.toDisplayTimeString( commLogComment.getAuditCreateInfo().getDateCreated() );// .toString();
        this.commentHistory += " - ";
        if ( commLogComment.getCommentUser() != null )
        {
          this.commentHistory += commLogComment.getCommentUser().getNameLFMWithComma();
        }
        this.commentHistory += "\n";
        this.commentHistory += commLogComment.getComments();
        this.commentHistory += "\n\n";
      }
    }

    if ( commLog.getAssignedToUser() != null )
    {
      this.assignedToUserId = commLog.getAssignedToUser().getId().toString();
      this.assignedToName = commLog.getAssignedToUser().getNameLFMWithComma();
    }
    else
    {
      // this is rquired when they go from close/deferred to open
      // if not we will take care of not sending this in action save/update
      this.assignedToUserId = UserManager.getUserId().toString();
      this.assignedToName = UserManager.getNameLFMWithComma();
    }

    this.createdBy = commLog.getAuditCreateInfo().getCreatedBy().toString();
    this.dateCreated = commLog.getAuditCreateInfo().getDateCreated().getTime();
    this.id = commLog.getId().longValue();
    this.version = commLog.getVersion().longValue();
  }

  /**
   * Builds a full domain object for update from the form.
   * 
   * @return CommLog
   */

  public CommLog toDomainObject()
  {
    CommLog commLog = new CommLog();

    if ( this.commLogId != null && this.commLogId.longValue() != 0 )
    {
      // update
      commLog.setId( this.commLogId );
      commLog.setVersion( new Long( this.version ) );
    }
    commLog.setCommLogSourceType( CommLogSourceType.lookup( this.sourceType ) );
    commLog.setCommLogCategoryType( CommLogCategoryType.lookup( this.categoryType ) );
    commLog.setCommLogReasonType( CommLogReasonType.lookup( this.reasonType ) );
    commLog.setCommLogStatusType( CommLogStatusType.lookup( this.statusType ) );
    commLog.setCommLogUrgencyType( CommLogUrgencyType.lookup( this.urgencyType ) );
    commLog.setMessageType( CommLog.MESSAGE_TYPE_EMAIL );

    return commLog;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * @return String
   */
  public String getCreatedBy()
  {
    return createdBy;
  }

  /**
   * @param createdBy
   */
  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  /**
   * @return long
   */
  public long getDateCreated()
  {
    return dateCreated;
  }

  /**
   * @param dateCreated
   */
  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  /**
   * @return long
   */
  public long getId()
  {
    return id;
  }

  /**
   * @param id
   */
  public void setId( long id )
  {
    this.id = id;
  }

  /**
   * @return long
   */
  public long getVersion()
  {
    return version;
  }

  /**
   * @param version
   */
  public void setVersion( long version )
  {
    this.version = version;
  }

  /**
   * @return userId
   */
  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getCategoryDesc()
  {
    return categoryDesc;
  }

  public void setCategoryDesc( String categoryDesc )
  {
    this.categoryDesc = categoryDesc;
  }

  public String getReasonDesc()
  {
    return reasonDesc;
  }

  public void setReasonDesc( String reasonDesc )
  {
    this.reasonDesc = reasonDesc;
  }

  public String getSourceDesc()
  {
    return sourceDesc;
  }

  public void setSourceDesc( String sourceDesc )
  {
    this.sourceDesc = sourceDesc;
  }

  public String getStatusDesc()
  {
    return statusDesc;
  }

  public void setStatusDesc( String statusDesc )
  {
    this.statusDesc = statusDesc;
  }

  public String getUrgencyDesc()
  {
    return urgencyDesc;
  }

  public void setUrgencyDesc( String urgencyDesc )
  {
    this.urgencyDesc = urgencyDesc;
  }

  public String getCategoryType()
  {
    return categoryType;
  }

  public void setCategoryType( String categoryType )
  {
    this.categoryType = categoryType;
  }

  public String getReasonType()
  {
    return reasonType;
  }

  public void setReasonType( String reasonType )
  {
    this.reasonType = reasonType;
  }

  public String getSourceType()
  {
    return sourceType;
  }

  public void setSourceType( String sourceType )
  {
    this.sourceType = sourceType;
  }

  public String getStatusType()
  {
    return statusType;
  }

  public void setStatusType( String statusType )
  {
    this.statusType = statusType;
  }

  public String getUrgencyType()
  {
    return urgencyType;
  }

  public void setUrgencyType( String urgencyType )
  {
    this.urgencyType = urgencyType;
  }

  public String getNewComment()
  {
    return newComment;
  }

  public void setNewComment( String newComment )
  {
    this.newComment = newComment;
  }

  public void setCommLogId( Long commLogId )
  {
    this.commLogId = commLogId;
  }

  public Long getCommLogId()
  {
    return commLogId;
  }

  public String getAssignedToName()
  {
    return assignedToName;
  }

  public String getAssignedByUserId()
  {
    return assignedByUserId;
  }

  public void setAssignedByUserId( String assignedByUserId )
  {
    this.assignedByUserId = assignedByUserId;
  }

  public String getAssignedToUserId()
  {
    return assignedToUserId;
  }

  public void setAssignedToUserId( String assignedToUserId )
  {
    this.assignedToUserId = assignedToUserId;
  }

  public void setAssignedToName( String assignedToName )
  {
    this.assignedToName = assignedToName;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public String getCommentHistory()
  {
    return commentHistory;
  }

  public void setCommentHistory( String commentHistory )
  {
    this.commentHistory = commentHistory;
  }

}
