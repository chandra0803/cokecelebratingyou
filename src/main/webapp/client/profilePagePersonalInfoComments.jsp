
<%@ page import="java.util.*"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.*"%>
<%@ include file="/include/taglib.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.objectpartners.cms.domain.Content"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>


{{#if _isEmpty}}
    <div class="row-fluid commentsItemWrapper isEmptyMessage">
        <div class="span12">
            <div class="alert alert-info">No contributions yet.</div>
        </div>
    </div>
    {{else}}
    <div class="row-fluid commentsItemWrapper comment_{{commentId}}" data-id="{{commentId}}">
        <div class="span12 commentItem">
            <div class="innerCommentWrapper">
                <!-- need to update json to have first name and last name -->
                <span class="avatarwrap avatarImg">
                    {{#if userInfo.0.profilePhoto}}
                        <img src="{{userInfo.0.profilePhoto}}"  />
                    {{else}}
                        <div class="avatar-initials">{{trimString userInfo.0.firstName 0 1}}{{trimString userInfo.0.lastName 0 1}}</div>
                    {{/if}}
                </span>
                <div class="indent">
                    <div class="userInfo" data-id="{{userInfo.0.contributorID}}">
                        {{userInfo.0.lastName}} {{userInfo.0.firstName}}
                    </div>
                    {{#if commentText}}
                    <div class="text">
                        {{commentText}}
                    </div>
                    <!--div class="translateTextLink"><a href="clientState=XXX"  data-id="{{commentId}}">Translate</a></div-->
                    {{/if}}

                    {{#or videoHtml media}}
                    <div class="media">
                        {{#if videoHtml}}
                            {{#each videoHtml}}
                            <div class="videoWrapper">
                                <div class="responsiveVideoContainer">
                                    {{{this}}}
                                </div>
                            </div>
                            {{/each}}
                        {{else}}
                            {{#if videoWebLink}}
                                <a class="mediaLink" href="{{videoWebLink}}" target="_blank">{{videoWebLink}}</a>
                                <i class="icon-share-1"></i>
                            {{/if}}
                        {{/if}}

                        {{#if media}}
                            {{#if media.0.video}}
                            <div class="videoWrapper">
                                <div class="responsiveVideoContainer">
                                    <video id="careerMomentContribVideo{{commentId}}" class="video-js vjs-default-skin vjs-16-9 vjs-big-play-centered" controls preload="auto" data-setup="{}">
                                        <source src="{{media.0.video.src}}" type="video/{{media.0.video.fileType}}">
                                    </video>
                                </div>
                            </div>
                            {{/if}}

                            {{#if media.0.photo.src}}
                                <img src="{{media.0.photo.src}}">
                            {{/if}}

                            
                        {{/if}}
                    </div>
                    {{/or}}

                    <div class="likeAndCommentsWrapper" data-contributor-id="{{userInfo.0.contributorID}}" data-comment-id="{{commentId}}" id="likeAndCommentsWrapper_{{commentId}}">
                        <div class="mask" style="display:none"></div>                      
                        {{#if isLiked}}
                            <i class="icon-star"></i>Likes</a>      
                            <span id="likeCounts_{{commentId}}"><span class="numLikers">({{numLikers}})</span></span>
                            
                        {{else}}
                                <i class="icon-star"></i>Like</a>     
                                <span id="likeCounts_{{commentId}}" ><span class="numLikers">({{numLikers}})</span></span>         
                        {{/if}}
                        
                        <i class="icon-message-2"></i>Comment <span id="commentCounts_{{commentId}}">({{numComments}})</span>
                        
                        <div class="app-row comment-block">
                            <div class="app-col commentInputWrapper">
                                <div class="levelOneComments">
                                        {{#each levelOneComments}}
                                        <div class="app-row comment-block clearfix publicProfileSubComments comment{{commentId}}" data-comment-id="{{commentId}}">

                                                <div class="app-col">
                                                    <div class="avatarwrap commenterAvatar">
                                                        <a class="profile-popover " data-participant-ids="[{{commenter.id}}]">
                                                            {{#if commenter.avatarUrl}}
                                                                <img alt="{{commenter.firstName}} {{commenter.lastName}}" src="{{commenter.avatarUrl}}" />
                                                            {{else}}
                                                                <span class="avatar-initials">{{trimString commenter.firstName 0 1}}{{trimString commenter.lastName 0 1}}</span>
                                                            {{/if}}
                                                        </a>
                                                    </div>
                        
                                                </div>
                        
                                                <div class="app-col comment {{#if isMine}}mine{{/if}}">
                        
                                                    <p>
                                                        <a class="profile-popover" href="#" data-participant-ids="[{{commenter.id}}]">
                                                            {{commenter.firstName}} {{commenter.lastName}}
                                                        </a>
                                                        <div class="readMore" data-read-more-num-lines="2" data-msg-read-more="more">
                                                            {{comment}}
                                                        </div>
                                                        <div class="likeAndCommentsWrapper" data-comment-id="{{levelOneId}}">
                                                            <div class="mask" style="display:none"></div>                      
                                                            {{#if isLiked}}
                                                                <i class="icon-star"></i>Unlike      
                                                                <span id="likeCounts_{{levelOneId}}"><span class="numLikers">({{numLikers}})</span></span>
                                                                
                                                            {{else}}
                                                                <i class="icon-star"></i>Like</a>     
                                                                <span id="likeCounts_{{levelOneId}}" ><span class="numLikers">({{numLikers}})</span></span>         
                                                            {{/if}}    
                                                        </div>                                
                        
                                                    </p>
                                                </div>
                                            </div>
                                            {{/each}}
                                </div>                                                    
                            </div><!-- /.commentInputWrapper -->
                        </div>
                    </div>
                </div><!-- /.indent -->
            </div><!-- /.innerCommentWrapper -->
        </div>
    </div>
{{/if}}