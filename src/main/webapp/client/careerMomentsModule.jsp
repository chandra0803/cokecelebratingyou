<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== CAREER MOMENTS MODULE ======== -->
<script type="text/template" id="careerMomentsModuleTpl">
<div class="module-liner">
    <div class="module-content">
        <h3 class="module-title"><cms:contentText key="PAGE_TITLE" code="coke.careermoments"/></h3>
    </div>
    <div class="careerMomentsList">
    </div>
    <!--subTpl.careerMomentsNewHireTpl=
        <div class="careerMomentsModuleTop">
            {{#if newHires}}
            <h2 class="careerMomentsModuleTitle multipleCelebrations">
                <cms:contentText key="NEWHIRE_PAGE_TITLE" code="coke.careermoments"/>
            </h2>
            <h2 class="careerMomentsModuleTitle oneCelebration" style="display:none">
                New Employee
            </h2>            
            <a href="<%=RequestUtils.getBaseURI(request)%>/careerMomentsPage.do" class="viewMore"><cms:contentText key="VIEW_MORE" code="purl.celebration.module"/></a>
            {{else}}

            {{/if}}
            <p class="infoWrapper">Get to know the <span id="newHireCount"></span> employees who started the week of {{newHires.displayDate}}</p> 
        </div>
        <div class="careerMomentsModuleContent">
            {{#gte newHires.count 1}}         
            <div id="newHireCarousel" class="carousel">
                    {{#each newHires.newHiresData}}
                    <div class="item">
                        <div class='item-inner-wrapper'>
                            {{#if avatarUrl}}
                            <img src="{{this.avatarUrl}}" alt="" class="avatar" />
                            {{else}}
                            <span class="avatar">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                            {{/if}}                            
                            </span>
                            
                            <span class="personalInfo">
                                <a href="#" class="profile-popover" data-participant-ids="[{{id}}]">{{firstName}}<br />{{lastName}}</a>
                                <span class="position" title="{{positionType}}">{{positionType}}
                                </span>								
								<div>  
								<beacon:authorize ifNotGranted="LOGIN_AS">                             
                               		 <a href="{{contributeUrl}}" class="commentLink"> <i class="icon-message-2"></i> <cms:contentText key="COMMENT" code="coke.careermoments"/> </a>                                                                
                                	 <a href="#" data-cheers-promotion-id=""  data-participant-ids="{{id}}" class="cheers-popover"><i class="icon-cheers"></i><cms:contentText key="CHEERS" code="client.cheers.recognition" /></a>
								</beacon:authorize>								
								</div>								
                            </span>
                        </div>
                    </div>
                    {{/each}}
            </div>
            {{else}}
                <div class="careerMomentsEmpty">
                    <p>There are no New Hires</p>
                    <a href="" class="btn btn-primary">View Past Celebrations</a>
                </div>
            {{/gte}}
        </div>
    subTpl-->

    <!--subTpl.careerMomentsJobChangeTpl=
        <div class="careerMomentsModuleTop">
            {{#if jobChanges}}
            <h2 class="careerMomentsModuleTitle multipleCelebrations">
                <cms:contentText key="JOBCHANGE_PAGE_TITLE" code="coke.careermoments"/>
            </h2>
                      
            <a href="<%=RequestUtils.getBaseURI(request)%>/careerMomentsPage.do" class="viewMore"><cms:contentText key="VIEW_MORE" code="purl.celebration.module"/></a>
            {{else}}

            {{/if}}
            <p class="infoWrapper">
            Congratulate these <span id="jobChangeCount"></span> employees who are changing their status! </p>  

        </div>
        <div class="careerMomentsModuleContent">            
            {{#gte jobChanges.count 1}}
            <div id="jobChangeCarousel" class="carousel">
                    {{#each jobChanges.jobChangesData}}
                    <div class="item">
                        <div class='item-inner-wrapper'>
                            {{#if avatarUrl}}
                            <img src="{{avatarUrl}}" alt="" class="avatar" />
                            {{else}}
                            <span class="avatar">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                            {{/if}}                            
                            <span class="personalInfo">
                                <a href="#" class="profile-popover" data-participant-ids="[{{id}}]">{{firstName}}<br />{{lastName}}</a>
                                <span class="position">{{positionType}}
                                </span>								
								<div>
								<beacon:authorize ifNotGranted="LOGIN_AS">                           
                               		 <a href="{{contributeUrl}}" class="commentLink"> <i class="icon-message-2"></i> Comment </a>
                                	 <a href="#" data-cheers-promotion-id=""  data-participant-ids="{{id}}" class="cheers-popover"><i class="icon-cheers"></i><cms:contentText key="CHEERS" code="client.cheers.recognition" /></a>
								</beacon:authorize>
								</div> 
                            </span>
                        </div>
                    </div>
                    {{/each}}
            </div>
            {{else}}
                <div class="careerMomentsEmpty">
                    <p>There is no new Job Changes</p>
                    <a href="" class="btn btn-primary">View Past Celebrations</a>
                </div>
            {{/gte}}

        </div>
    subTpl-->

</div>
</script>
