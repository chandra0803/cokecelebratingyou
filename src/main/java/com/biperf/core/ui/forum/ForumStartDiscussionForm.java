/**
 * 
 */

package com.biperf.core.ui.forum;

import com.biperf.core.ui.BaseForm;

/**
 * @author poddutur
 *
 */
public class ForumStartDiscussionForm extends BaseForm
{

  private static final long serialVersionUID = 792245432362461873L;

  public static final String FORM_NAME = "forumStartDiscussionForm";

  private Long topicId;
  private String title;
  private String text;
  private String topicName;
  private String method;
  private String avatarURL;

  public Long getTopicId()
  {
    return topicId;
  }

  public void setTopicId( Long topicId )
  {
    this.topicId = topicId;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  public String getTopicName()
  {
    return topicName;
  }

  public void setTopicName( String topicName )
  {
    this.topicName = topicName;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getAvatarURL()
  {
    return avatarURL;
  }

  public void setAvatarURL( String avatarURL )
  {
    this.avatarURL = avatarURL;
  }

}
