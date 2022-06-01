<%@ include file="/include/taglib.jspf"%>

<div id="drawToolView">
    <div class="row-fluid">
        <div class="span12">
            <label class="checkbox">
                <input type="checkbox" id="addAnECard"> <cms:contentText key="ADD_ECARD" code="recognition.submit"/>
            </label>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span12">
            <%-- <div id="drawingCountInformation">
                <span id="eCardThumbnailPagerInformation"> {{selectedeCard}} - {{lastShowneCard}} of {{totaleCards}} </span>
            </div>--%>
            <div id="drawingTool" style="">

                <div class="" id="eCardThumbnailContainerParent">
                    <div id="eCardThumbnailPager" class="">
                        <a class="btn" data-pager="prev" title="<cms:contentText key="PREVIOUS" code="system.button"/>"><i class="icon-arrow-1-up"></i></a>
                        <a class="btn" data-pager="next" title="<cms:contentText key="NEXT" code="system.button"/>"><i class="icon-arrow-1-down"></i></a>

                        <!--subTpl.eCardThumbnailPagerMeta=
                            <span id="eCardThumbnailPagerMeta"><span class="range"><cms:contentText key="PAGE" code="report.display.page"/> {{actualStep}}</span></span>
                        subTpl-->
                    </div>

                    <div class="" id="eCardThumbnailContainer">

                        <ul id="eCardThumbnailSelect" class="">
                            <!--dynamic content-->
                        </ul>

                    </div>
                </div><!-- /#eCardThumbnailContainerParent -->

                <div id="drawToolContainer" class="pull-left">
                    <div id="drawToolQTips">
                        <div id="drawToolStatusContainer">

                            <div class="drawToolQTip beginToolTipTouch" style="display:none;">
                                <div class="drawToolQTipContentContainer ">
                                    <p><cms:contentText key="ECARD_SELECTED" code="recognition.submit"/></p>
                                    <button class="btn editMode"> <cms:contentText key="EDIT" code="recognition.submit"/> <i class="icon-pencil1"></i></button>
                                    <button class="btn doneEditing"> <cms:contentText key="CONTINUE" code="recognition.submit"/> <i class="icon-ok"></i></button>
                                </div>
                            </div>

                            <div class="drawToolQTip pauseToolTipTouch" style="display:none;">
                                <div class="drawToolQTipContentContainer ">
                                    <p><cms:contentText key="DONE" code="recognition.submit"/></p>
                                    <button class="btn editMode"> <cms:contentText key="EDIT" code="recognition.submit"/> <i class="icon-pencil1"></i></button>
                                    <button class="btn doneEditing"> <cms:contentText key="CONTINUE" code="recognition.submit"/> <i class="icon-ok"></i></button>
                                </div>
                            </div>

                            <div class="drawToolQTip beginToolTip" style="display:none;">
                                <div class="drawToolQTipContentContainer ">
                                    <p><cms:contentText key="SELECT_CARD" code="recognition.submit"/></p>
                                </div>
                            </div>

							{{#if canDraw}}
                            	<div class="drawToolQTip editingDisabledToolTip" style="display:none;">
                                    <div class="drawToolQTipContentContainer ">
                                        <p><cms:contentText key="DRAWING_DISABLED" code="recognition.submit"/></p>
                                    </div>
                            	</div>
                            {{/if}}

                            <div class="drawToolQTip clearToolTip" style="display:none;">
                                <div class="drawToolQTipContentContainer ">
                                    <p><cms:contentText key="WARNING" code="recognition.submit"/></p>
                                    <button class="btn btn-primary" id="clearImageConfirm"> <cms:contentText key="CLEAR" code="recognition.submit"/> <i class="icon-trash"></i></button>
                                    <button class="btn" id="clearImageCancel"> <cms:contentText key="CANCEL" code="system.button"/> <i class="icon-ban"></i></button>
                                </div>
                            </div>
                        </div><!-- /#drawToolStatusContainer -->
                    </div><!-- /#drawToolQTips -->
                    
                    <!-- Customization - Below span is to make the font available before rendering to canvas -->
                    <span style="font-family:'TCCCUnityTextBold'">&nbsp;</span>
                    <div id="drawToolMenu">
                        <div class="btn-toolbar">

                            <div class="btn-group drawTools">
                                <button class="btn" id="pencilButton" data-tool-name="pencil" title="<cms:contentText key="PENCIL" code="system.button"/>"><i class="icon-pencil1"></i></button>
                                <button class="btn" id="treeButton" data-tool-name="tree" title="<cms:contentText key="TREE" code="system.button"/>" style="display:none"><i class="icon-leaf"></i></button>
                                <button class="btn" id="starButton" data-tool-name="star" title="<cms:contentText key="STAR" code="system.general"/>" style="display:none"><i class="icon-star"></i></button>
                                <button class="btn" id="eraserButton" data-tool-name="eraser" title="<cms:contentText key="ERASER" code="system.button"/>"><i class="icon-eraser"></i></button>
                            </div>
                            
                            <!-- WIP #62895 Changes start -->                            
                                <div class="btn-group memeDrawTools" style="display: none;">
                                        <div class="btn-group" id="drawToolFontPickSize">
                                            <button class="btn dropdown-toggle" data-toggle="dropdown" title="fontsize">
                                                <span class="draw"><span class="bigText">A</span><span class="smallText">A</span></span>
                                                <span class="size"></span> 
                                                <span class="caret"></span>
                                            </button>
                                            <ul class="dropdown-menu" id="fontSizeSelect">
                                                {{#each memeFontSizes}}
                                                <li data-font-size="{{this}}" class="fontSizeSelectList">
                                                    <span style="font-size:{{#gte this 24}}24{{else}}{{this}}{{/gte}}px">{{this}}</span>
                                                </li>
                                                {{/each}}
                                            </ul>
                                        </div>
                                        <button class="btn isAction forceBorderRadius" id="textToolToggle" data-tool-name="texttype" title="Text Tool"><span>T</span></button>
                                        <div class="btn-group" id="drawToolPickColor">
                                            <button class="btn dropdown-toggle" data-toggle="dropdown" title="color">
                                                <i class="icon-blur"></i> <span class="caret"></span>
                                            </button>
                                            <ul class="dropdown-menu" id="colorSelectMenu">
                                                {{#each colors}}
                                                <li><div class="colorSelect" style="background:\#{{hex}}" data-hex-color-code="{{this.hex}}" title="{{this.title}}"></div></li>
                                                {{/each}}
                                            </ul>
                                        </div>
                                        <button class="btn isAction forceBorderRadius" id="clearImage" title="clear"><i class="icon-trash"></i></button>
                                </div>
                                <!-- WIP #62895 Changes end -->

                            <div class="btn-group" id="drawToolPickSize">
                                <button class="btn dropdown-toggle" data-toggle="dropdown" title="<cms:contentText key="SIZE" code="system.button"/>">
                                    <span class="draw"><i class="icon-brush-1"></i></span><span class="erase"><i class="icon-brush-1"></i></span><span class="size"></span> <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu" id="lineWidthSelect">
                                    {{#each sizes}}
                                    <li data-brush-width="{{this}}"><span class="draw"><i class="icon-brush-1"></i></span><span class="erase"><i class="icon-brush-1"></i></span></li>
                                    {{/each}}
                                </ul>
                            </div>

                            <div class="btn-group" id="drawToolPickColor">
                                <button class="btn dropdown-toggle" data-toggle="dropdown" title="<cms:contentText key="COLOR" code="system.button"/>">
                                    <i class="icon-blur"></i> <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu" id="colorSelectMenu">
                                    {{#each colors}}
                                    <li><div class="colorSelect" style="background:\#{{hex}}" data-hex-color-code="{{this.hex}}" title="{{this.title}}"></div></li>
                                    {{/each}}
                                </ul>
                            </div>

                            <div class="btn-group pull-right clearToolTipContainer">
                                <button class="btn" id="clearImage" title="<cms:contentText key="CLEAR" code="system.button"/>"><i class="icon-trash"></i></button>
                            </div>

                            <div id="drawingCompleteContainer" class="btn-group pull-right">
                                <button class="btn" id="drawingComplete" title="<cms:contentText key="FINISH_DRAWING" code="system.button"/>"><i class="icon-diskette-2"></i></button>
                            </div>
                        </div>

                    </div><!-- /#drawToolMenu -->

                    <div id="drawToolHiddenInputs">
                        <input type="hidden" id="drawToolCardType" name="cardType" value="">
                        <input type="hidden" id="drawToolVideoUrl" name="videoUrl" value="">
                        <input type="hidden" id="drawToolCardId" name="cardId" value="">
                        <input type="hidden" id="drawToolCardUrl" name="cardUrl" value="">
                        <input type="hidden" id="drawToolDrawingData" name="drawingData" value="">
                        <input type="hidden" id="drawToolCardData" name="cardData" value="">
                    </div>

                    <div id="wPaint" class="">
                        <div id="bgContainer" class=""></div>

                        <!-- this is where the canvas sits -->
                    </div><!-- /#wPaint -->
                    <!-- WIP #62895 Changes start     -->
                        <div id="ecardStickers" style="display:none;">
                            <button class="btn stickersToggleBtn" type="button"><cms:contentText key="STICKERS" code="coke.meme"/><i class="icon-arrow-1-down badgeBtnCaret"></i></button>
                            <div class="ecardStickersHolder" style="display:none;">
                                <div class="stickersMaxLimitError">
                                    <span><cms:contentText key="MAX_STICKERS_MSG" code="coke.meme"/></span>
                                </div>
                                <ul class="ecardStickerList">
                                    <!-- Dynamic -->
                                </ul>
                            </div>
                        </div>
                        <!-- WIP #62895 Changes end     -->
                </div><!-- /#drawToolContainer -->

            </div><!-- /#drawingTool -->
        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->

    <div id="uploadError" data-toggle="modal">
        <div id="uploadErrorModal" class="modal hide fade">
            <div class="modal-header">
                <button data-dismiss="modal" class="close" type="button"><i class="icon-close"></i></button>
                <h2><cms:contentText code='recognition.submit' key='UPLOAD_FAILED' /></h2>
                <strong><cms:contentText code='recognition.submit' key='INVALID_FORMAT' /></strong>
            </div>
            <div class="modal-body">
                <div class="row-fluid">
                    <img class="upload-img span1" src="${pageContext.request.contextPath}/assets/img/camera-up.png">
                    <p class="upload-fail-text span3"><strong><cms:contentText code='recognition.submit' key='SUPPORTED_IMAGE_FORMATS' /></strong><br/>
                        ${supportedImageTypes}<br/>
                        <cms:contentTemplateText code='recognition.submit' key='FILE_SIZE_LIMIT' args='${maxImageSize}' delimiter=','/>
                    </p>
                </div>
                <div class="row-fluid">
                    <img class="upload-img span1" src="${pageContext.request.contextPath}/assets/img/video-up.png">
                    <p  class="upload-fail-text span3"><strong><cms:contentText code='recognition.submit' key='SUPPORTED_VIDEO_FORMATS' /></strong><br/>
                        ${supportedVideoTypes}<br/>
                        <cms:contentTemplateText code='recognition.submit' key='FILE_SIZE_LIMIT' args='${maxVideoSize}' delimiter=','/>
                    </p>
                </div>

            </div>
        </div>
    </div>
</div><!-- /#drawToolView -->
