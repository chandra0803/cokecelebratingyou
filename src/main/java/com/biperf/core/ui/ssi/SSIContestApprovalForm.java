
package com.biperf.core.ui.ssi;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.ui.BaseActionForm;

/**
 * 
 * SSIContestApprovalForm.
 * 
 * @author kandhi
 * @since Dec 15, 2014
 * @version 1.0
 */
public class SSIContestApprovalForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;

  private String method;
  private String initializationJson = "";
  private int page;
  private int total;
  private String set;
  private String sortedOn;
  private String sortedBy;
  private String comment;

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getInitializationJson()
  {
    return initializationJson;
  }

  public void setInitializationJson( String initializationJson )
  {
    this.initializationJson = initializationJson;
  }

  public int getPage()
  {
    return page;
  }

  public void setPage( int page )
  {
    this.page = page;
  }

  public int getTotal()
  {
    return total;
  }

  public void setTotal( int total )
  {
    this.total = total;
  }

  public String getSet()
  {
    return set;
  }

  public void setSet( String set )
  {
    this.set = set;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment( String comment )
  {
    this.comment = comment;
  }

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = new ActionErrors();
    if ( this.comment != null && this.comment.length() > 1000 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "ssi_contest.generalInfo.DENIAL_REASON_EXCEEDS_LENGTH" ) );
    }
    return actionErrors;
  }

}
