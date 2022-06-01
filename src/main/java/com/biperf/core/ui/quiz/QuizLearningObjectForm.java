
package com.biperf.core.ui.quiz;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.value.PurlMediaUploadValue;

public class QuizLearningObjectForm extends BaseActionForm
{
  private static final int MEDIA_DEFAULT_SIZE = 20;

  private FormFile fileAsset;
  private FormFile fileAssetVideo;
  private FormFile fileAssetPdf;
  private List<PurlMediaUploadValue> mediaUploads;

  private String mediaUrl;
  private String mediaFilePath;
  private String globalUniqueId;
  private String quizLearningText;
  private String quizLearningTextFull;
  private String imageUrl;
  private String pdfUrl;
  private String videoUrl;
  private String videoUrlMp4;
  private String videoUrl3gp;
  private String videoUrlOgg;
  private String videoUrlWebm;

  private String method;

  private Long quizFormId;
  private String quizFormName;
  private String uploadType;
  private int slideNumber;
  private String[] pdfUploadStringRow;
  private String[] pdfQuizTextString;
  private String pdfText;
  private String[] deleteSlides;

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );
    quizLearningText = "";
    mediaUploads = getEmptyMediaUploads( MEDIA_DEFAULT_SIZE );
  }

  private List<PurlMediaUploadValue> getEmptyMediaUploads( int count )
  {
    List<PurlMediaUploadValue> list = new ArrayList<PurlMediaUploadValue>();
    for ( int i = 0; i < count; ++i )
    {
      list.add( new PurlMediaUploadValue() );
    }
    return list;
  }

  public void load( String leftColumn,
                    String rightColumn,
                    String uploadTypeInput,
                    int numberColumns,
                    int slideNumber,
                    String mediaPath,
                    String videoUrlMp4,
                    String videoUrlWebm,
                    String videoUrl3gp,
                    String videoUrlOgg )
  {
    this.slideNumber = slideNumber;
    if ( numberColumns == 1 )
    {
      this.quizLearningTextFull = rightColumn;
    }
    else
    {
      this.quizLearningText = rightColumn;
      this.mediaFilePath = mediaPath;

      if ( uploadTypeInput.equalsIgnoreCase( "image" ) )
      {
        this.uploadType = uploadTypeInput;
        this.imageUrl = leftColumn;
      }
      else if ( uploadTypeInput.equalsIgnoreCase( "video" ) )
      {
        this.uploadType = uploadTypeInput;
        this.videoUrl = leftColumn;
        this.videoUrlMp4 = videoUrlMp4;
        this.videoUrlWebm = videoUrlWebm;
        this.videoUrl3gp = videoUrl3gp;
        this.videoUrlOgg = videoUrlOgg;
      }
      else
      {
        this.uploadType = uploadTypeInput;
        this.pdfUrl = leftColumn;
      }
    }

    // this.questions = quiz.getQuizQuestions();

  }

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    /*
     * if(request.getParameter("method").equals("processPhoto")||request.getParameter("method").
     * equals("processVideo")) { if(this.getFileAsset()!=null && this.getFileAsset().getFileSize() >
     * 3000000) { actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
     * "recognition.select.card.SIZE_LIMIT" ) ); } if(this.getFileAsset()==null) { actionErrors.add(
     * ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "Please select a valid file to Upload")); }
     * if(this.getFileAsset()!=null) { //actionErrors.add(
     * this.file.getFileName(),"Image size should be less than 3MB" ); String imageExt =
     * this.getFileAsset().getContentType(); String[] imgNext= imageExt.split( "/" );
     * if(this.uploadType!=null) { if(this.uploadType.equalsIgnoreCase( "image" )) {
     * if(!(imgNext[1].equals( "gif" ) || imgNext[1].equals( "jpg" ) || imgNext[1].equals( "jpeg" )
     * || imgNext[1].equals( "x-png" ) || imgNext[1].equals( "png" ) || imgNext[1].equals( "pjpeg"
     * ))) { actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
     * "recognition.select.card.INVALID_EXTENSION" ) ); } } else if(uploadType.equalsIgnoreCase(
     * "pdf" )) { actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
     * "Please select a valid PDF")); } } else { actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new
     * ActionMessage( "Please select a valid file to Upload")); } } }
     */

    /*
     * if (getFileAsset() == null || getFileAsset().getFileName()==null ||
     * getFileAsset().getFileName().length()==0) { actionErrors.add("file",new
     * ActionMessage("File is required")); }
     */
    /*
     * if(request.getParameter("method").equals("saveLearningObject")) { if(this.uploadType!=null) {
     * if(this.getFileAsset()==null) { actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new
     * ActionMessage( "Please select a valid file to upload")); } } else { this.fileAsset=null; } }
     */
    return actionErrors;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public FormFile getFileAsset()
  {
    return fileAsset;
  }

  public void setFileAsset( FormFile fileAsset )
  {
    this.fileAsset = fileAsset;
  }

  public List<PurlMediaUploadValue> getMediaUploads()
  {
    return mediaUploads;
  }

  public void setMediaUploads( List<PurlMediaUploadValue> mediaUploads )
  {
    this.mediaUploads = mediaUploads;
  }

  public String getGlobalUniqueId()
  {
    return globalUniqueId;
  }

  public void setGlobalUniqueId( String globalUniqueId )
  {
    this.globalUniqueId = globalUniqueId;
  }

  public String getMediaUrl()
  {
    return mediaUrl;
  }

  public void setMediaUrl( String mediaUrl )
  {
    this.mediaUrl = mediaUrl;
  }

  public FormFile getFileAssetPdf()
  {
    return fileAssetPdf;
  }

  public void setFileAssetPdf( FormFile fileAssetPdf )
  {
    this.fileAssetPdf = fileAssetPdf;
  }

  public FormFile getFileAssetVideo()
  {
    return fileAssetVideo;
  }

  public void setFileAssetVideo( FormFile fileAssetVideo )
  {
    this.fileAssetVideo = fileAssetVideo;

  }

  public String getQuizLearningText()
  {
    return quizLearningText;
  }

  public String getQuizLearningTextFull()
  {
    return quizLearningTextFull;
  }

  public void setQuizLearningTextFull( String quizLearningTextFull )
  {
    this.quizLearningTextFull = quizLearningTextFull;
  }

  public void setQuizLearningText( String quizLearningText )
  {
    this.quizLearningText = quizLearningText;
  }

  public Long getQuizFormId()
  {
    return quizFormId;
  }

  public void setQuizFormId( Long quizFormId )
  {
    this.quizFormId = quizFormId;
  }

  public String getQuizFormName()
  {
    return quizFormName;
  }

  public void setQuizFormName( String quizFormName )
  {
    this.quizFormName = quizFormName;
  }

  public String getUploadType()
  {
    return uploadType;
  }

  public String getVideoUrl()
  {
    return videoUrl;
  }

  public void setVideoUrl( String videoUrl )
  {
    this.videoUrl = videoUrl;
  }

  public String[] getDeleteSlides()
  {
    return deleteSlides;
  }

  public void setDeleteSlides( String[] deleteSlides )
  {
    this.deleteSlides = deleteSlides;
  }

  public void setUploadType( String uploadType )
  {
    this.uploadType = uploadType;
  }

  public String getMediaFilePath()
  {
    return mediaFilePath;
  }

  public String getImageUrl()
  {
    return imageUrl;
  }

  public void setImageUrl( String imageUrl )
  {
    this.imageUrl = imageUrl;
  }

  public String getPdfUrl()
  {
    return pdfUrl;
  }

  public void setPdfUrl( String pdfUrl )
  {
    this.pdfUrl = pdfUrl;
  }

  public void setMediaFilePath( String mediaFilePath )
  {
    this.mediaFilePath = mediaFilePath;
  }

  public int getSlideNumber()
  {
    return slideNumber;
  }

  public void setSlideNumber( int slideNumber )
  {
    this.slideNumber = slideNumber;
  }

  public String getPdfText()
  {
    return pdfText;
  }

  public void setPdfText( String pdfText )
  {
    this.pdfText = pdfText;
  }

  public String[] getPdfUploadStringRow()
  {
    return pdfUploadStringRow;
  }

  public void setPdfUploadStringRow( String[] pdfUploadStringRow )
  {
    this.pdfUploadStringRow = pdfUploadStringRow;
  }

  public String[] getPdfQuizTextString()
  {
    return pdfQuizTextString;
  }

  public void setPdfQuizTextString( String[] pdfQuizTextString )
  {
    this.pdfQuizTextString = pdfQuizTextString;
  }

  public String getVideoUrlMp4()
  {
    return videoUrlMp4;
  }

  public void setVideoUrlMp4( String videoUrlMp4 )
  {
    this.videoUrlMp4 = videoUrlMp4;
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

  public String getVideoUrlWebm()
  {
    return videoUrlWebm;
  }

  public void setVideoUrlWebm( String videoUrlWebm )
  {
    this.videoUrlWebm = videoUrlWebm;
  }

}
