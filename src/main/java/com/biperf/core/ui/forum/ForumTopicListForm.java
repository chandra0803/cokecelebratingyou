/**
 * 
 */

package com.biperf.core.ui.forum;

import com.biperf.core.ui.BaseForm;

/**
 * @author poddutur
 * 
 */
public class ForumTopicListForm extends BaseForm
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------
  public static final String FORM_NAME = "forumTopicListForm";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private static final long serialVersionUID = 1L;
  private String topicCmAssetCode;
  private String method;
  private String[] deleteForumTopics;

  public String getTopicCmAssetCode()
  {
    return topicCmAssetCode;
  }

  public void setTopicCmAssetCode( String topicCmAssetCode )
  {
    this.topicCmAssetCode = topicCmAssetCode;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String[] getDeleteForumTopics()
  {
    return deleteForumTopics;
  }

  public void setDeleteForumTopics( String[] deleteForumTopics )
  {
    this.deleteForumTopics = deleteForumTopics;
  }

}
