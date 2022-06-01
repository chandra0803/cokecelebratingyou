/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/GoalLevelValueBean.java,v $
 */

package com.biperf.core.value;

import java.io.Serializable;

/**
 * AboutMeValueBean.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>meadows</td>
 * <td>Feb 2, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AboutMeValueBean implements Serializable
{
  private String aboutmeQuestion;
  private String aboutmeQuestioncode;
  private String aboutmeAnswer;
  private Long likesCount;

  public String getAboutmeQuestion()
  {
    return aboutmeQuestion;
  }

  public void setAboutmeQuestion( String aboutmeQuestion )
  {
    this.aboutmeQuestion = aboutmeQuestion;
  }

  public String getAboutmeAnswer()
  {
    return aboutmeAnswer;
  }

  public void setAboutmeAnswer( String aboutmeAnswer )
  {
    this.aboutmeAnswer = aboutmeAnswer;
  }

  public String getAboutmeQuestioncode()
  {
    return aboutmeQuestioncode;
  }

  public void setAboutmeQuestioncode( String aboutmeQuestioncode )
  {
    this.aboutmeQuestioncode = aboutmeQuestioncode;
  }

  public Long getLikesCount()
  {
    return likesCount;
  }

  public void setLikesCount( Long likesCount )
  {
    this.likesCount = likesCount;
  }
}
