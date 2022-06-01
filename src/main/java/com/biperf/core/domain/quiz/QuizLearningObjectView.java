/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/quiz/QuizLearningObjectView.java,v $
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
 * <td>Oct 8, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizLearningObjectView
{
  private String status;
  private String media;
  private String id;
  private String full;
  private String thumb;
  private String filename;
  private String imageurl;
  private String fail;
  private Long quizFormId;
  private String videoUrlMp4;
  private String videoUrlWebm;
  private String videoUrl3gp;
  private String videoUrlOgg;

  public QuizLearningObjectView()
  {
    super();
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getMedia()
  {
    return media;
  }

  public void setMedia( String media )
  {
    this.media = media;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getFull()
  {
    return full;
  }

  public void setFull( String full )
  {
    this.full = full;
  }

  public String getThumb()
  {
    return thumb;
  }

  public void setThumb( String thumb )
  {
    this.thumb = thumb;
  }

  public String getFilename()
  {
    return filename;
  }

  public void setFilename( String filename )
  {
    this.filename = filename;
  }

  public String getImageurl()
  {
    return imageurl;
  }

  public void setImageurl( String imageurl )
  {
    this.imageurl = imageurl;
  }

  public String getFail()
  {
    return fail;
  }

  public void setFail( String fail )
  {
    this.fail = fail;
  }

  public Long getQuizFormId()
  {
    return quizFormId;
  }

  public void setQuizFormId( Long quizFormId )
  {
    this.quizFormId = quizFormId;
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

}
