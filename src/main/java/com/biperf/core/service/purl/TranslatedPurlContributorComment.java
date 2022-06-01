
package com.biperf.core.service.purl;

public class TranslatedPurlContributorComment
{
  private final Long id;
  private final String commentText;

  public TranslatedPurlContributorComment( Long id, String commentText )
  {
    this.id = id;
    this.commentText = commentText;
  }

  public Long getId()
  {
    return id;
  }

  public String getCommentText()
  {
    return commentText;
  }
}
