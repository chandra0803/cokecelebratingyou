<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<fieldset class="formSection ecardsSection" id="recognitionFieldsetECards" style="display:none">
  <h3 class="headline_3"><cms:contentText key="SEND_ECARDS" code="recognition.submit"/></h3>

	<!-- WIP #62895 Changes start -->
                        <!-- Please note that there also other changes inside the file related to this feature. Search the repo for commit with message "WIP #62895 - Meme & Sticker tool changes" to see all the changes -->
                        <ul class="nav nav-tabs hide" id="recognitionPageEcardSectionTabSelectedTabs">
                            <li class="active">
                                <a data-toggle="tab" href="#ecardsSec" data-tab-name="ecards"><span><cms:contentText key="ECARD_TAB_TITLE" code="coke.meme"/></span></a>
                            </li>            
                            <li>
                                <a data-toggle="tab" href="#memeSec" data-tab-name="memes"><span><cms:contentText key="MEME_TAB_TITLE" code="coke.meme"/></span></a>
                            </li>
                        </ul>
                        <!-- WIP #62895 Changes end -->
                        <!-- Please note that there also other changes inside the file related to this feature. Search the repo for commit with message "WIP #62895 - Meme & Sticker tool changes" to see all the changes -->
                        
  <div id="drawToolShell">
    <!-- dynamice - draw tool widget -->
  </div>

  <!-- dynamic - backbone view -->
</fieldset><!-- /#recognitionFieldsetECards -->
