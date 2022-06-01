
package com.biperf.core.ui.quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizTakeMaterial
{
  private Long id;
  private int pageNumber;
  private String type;
  private String text;
  private List<QuizTakeLearningFile> files = new ArrayList<QuizTakeLearningFile>();

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public int getPageNumber()
  {
    return pageNumber;
  }

  public void setPageNumber( int pageNumber )
  {
    this.pageNumber = pageNumber;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  public List<QuizTakeLearningFile> getFiles()
  {
    return files;
  }

  public void setFiles( List<QuizTakeLearningFile> files )
  {
    this.files = files;
  }

}
