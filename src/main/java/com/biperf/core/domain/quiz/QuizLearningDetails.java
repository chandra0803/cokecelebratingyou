/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/quiz/QuizLearningDetails.java,v $
 */

package com.biperf.core.domain.quiz;

/**
 * QuizQuestion.
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
 * <td>sharafud</td>
 * <td>Oct 10, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizLearningDetails implements Comparable
{
  private String leftColumn;
  private String rightColumn;
  private String filePath;
  private String videoUrlMp4 = "";
  private String videoUrlWebm = "";
  private String videoUrl3gp = "";
  private String videoUrlOgg = "";

  public QuizLearningDetails()
  {
    super();
  }

  public String getLeftColumn()
  {
    return leftColumn;
  }

  public void setLeftColumn( String leftColumn )
  {
    this.leftColumn = leftColumn;
  }

  public String getRightColumn()
  {
    return rightColumn;
  }

  public void setRightColumn( String rightColumn )
  {
    this.rightColumn = rightColumn;
  }

  public String getFilePath()
  {
    return filePath;
  }

  public void setFilePath( String filePath )
  {
    this.filePath = filePath;
  }

  public String getVideoUrlMp4()
  {
    return videoUrlMp4;
  }

  public void setVideoUrlMp4( String videoUrlMp4 )
  {
    this.videoUrlMp4 = videoUrlMp4;
  }

  public String getVideoUrlWebm()
  {
    return videoUrlWebm;
  }

  public void setVideoUrlWebm( String videoUrlWebm )
  {
    this.videoUrlWebm = videoUrlWebm;
  }

  public String getVideoUrl3gp()
  {
    return videoUrl3gp;
  }

  public void setVideoUrl3gp( String videoUrl3gp )
  {
    this.videoUrl3gp = videoUrl3gp;
  }

  public String getVideoUrlOgg()
  {
    return videoUrlOgg;
  }

  public void setVideoUrlOgg( String videoUrlOgg )
  {
    this.videoUrlOgg = videoUrlOgg;
  }

  public int compareTo( Object object ) throws ClassCastException
  {
    if ( ! ( object instanceof QuizLearningDetails ) )
    {
      throw new ClassCastException( "A QuizLearningDetails was expected." );
    }
    QuizLearningDetails quizLearning = (QuizLearningDetails)object;
    return this.getLeftColumn().compareTo( quizLearning.getLeftColumn() );

  }

}
