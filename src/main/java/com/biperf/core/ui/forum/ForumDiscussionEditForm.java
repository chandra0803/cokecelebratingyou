/**
 * 
 */

package com.biperf.core.ui.forum;

import com.biperf.core.ui.BaseForm;

/**
 * @author poddutur
 *
 */
public class ForumDiscussionEditForm extends BaseForm
{

  /**
   * 
   */
  private static final long serialVersionUID = 6411046669833029404L;

  public static final String FORM_NAME = "forumDiscussionEditForm";

  private String method;

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

}
