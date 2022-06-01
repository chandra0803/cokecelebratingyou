<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== CAREER MOMENTS PAGE ======== -->

<div id="careerMomentsPageView" class="page-content">

    <div class="page-liner careerMomentsItemsCont" data-msg-empty="No celebrations found">
        <div class="row-fluid">
            <div class="span12">
                <div class="page-topper">
                    <div class="careerMomentsView">
                        <form class="careerMomentsSelectView">
                            <!-- <span class="toggleLabel">&nbsp;</span> -->

                            <div class="control-group" id="cmPastPresentSelect">
                                <div class="controls">

                                    <input type="radio" class="cmSelectRadio" id="cmPastPresentSelectA" name="cmPastPresentSelect" value="current" checked>

                                    <label class="radio btn btn-primary" for="cmPastPresentSelectA" style="display: inline-block;">
                                    <cms:contentText key="CURRENT" code="coke.careermoments"/>
                                    </label>

                                    <input type="radio" class="cmSelectRadio" id="cmPastPresentSelectB" name="cmPastPresentSelect" value="past">

                                    <label class="radio btn" for="cmPastPresentSelectB" style="display: inline-block;">
                                    <cms:contentText key="PREVIOUS" code="coke.careermoments"/>
                                    </label>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="careerMomentsFilterWrap">
                        <span class="filterLabel">Filter By:</span>

                        <div class="filterTokens">
                            <!-- dynamic from CareerMomentsPageView.js-->
                        </div>

                        <div class="dropdown">
                            <ul class="dropdown-menu careerMomentsTabs" role="menu" aria-labelledby="dLabel">
                                
                            </ul>
                        </div>
                    </div>

                    <div class="careerMomentsSearch">
                        <form id="careerMomentsSearchForm">
                            <div class="selected-filters">
                                <label for="careerMomentsNameInput"><cms:contentText key="SEARCH" code="coke.careermoments"/>: </label>

                                <span class="selected-filter" style="display:none">
                                    <span class="filter-bold"></span>
                                        <span class="removeSearchToken">
                                            <i class="icon icon-cancel-circle" ></i>
                                        </span>
                                </span>
                            </div>

                            <div class="input-append input-append-inside validateme searchWrap dropdown"
                                data-validate-fail-msgs='{"minlength":"<cms:contentText key="ENTER_ATLEAST_TWO" code="purl.celebration.module"/>"}'
                                data-validate-flags='minlength'
                                data-validate-min-length="2">

                                <input name="careerMomentsNameInput" id="careerMomentsNameInput" type="text" placeholder="Last Name"
                                        data-autocomp-delay="500"
                                        data-autocomp-min-chars="2"
                                        data-autocomp-url="${pageContext.request.contextPath}/recognitionWizard/recipientSearch.do?method=doAutoComplete">

                                <ul class="dropdown-menu cmSearchDropdownMenu" role="menu"
                                    data-msg-instruction="Start typing please."
                                    data-msg-no-results="No results found, please refine text.">
                                    <!-- dynamic -->
                                </ul>

                                <button class="btn btn-link add-on searchBtn"><i class="icon-magnifier-1"></i></button>
                                <div class="spinnerWrap"></div>
                            </div>

                            <div class="searchList" style="display: none" data-msg-no-results="No celebrations found, please refine search.">
                                 <!-- dynamic from purlCelebrateSearchItem.html -->
                            </div>
                        </form>
                    </div>
                    <div class="careerMomentsTypeWrapper">
                        <div class="btn-group">
                           <a class="btn dropdown-toggle cmBtn btn-primary" data-toggle="dropdown" href="#">
                           <span class="cmBtnContent">
                           <i class=" "></i>
                           <span class="cmName"><cms:contentText key="NEW_HIRES" code="coke.careermoments"/></span>
                           </span>
                           <i class="icon-arrow-1-down"></i>
                           </a>
                           <ul class="dropdown-menu cmItems">
                              <li class="cmItem">
                                 <a href="#" data-cm-id="newhire">
                                 <cms:contentText key="NEW_HIRES" code="coke.careermoments"/>
                                 </a>
                              </li>
                              <li class="cmItem">
                                 <a href="#" data-cm-id="jobchange">
                                 <cms:contentText key="JOB_CHANGES" code="coke.careermoments"/>
                                 </a>
                              </li>                              
                           </ul>
                           <input type="hidden" class="selectedCMType" name="selectedCMType" value=""></input>
                        </div>
                    </div>
                </div><!-- /.page-topper -->
            </div><!-- /.span12 -->
        </div><!-- /.row-fluid -->

        <div class="row-fluid">
            <div class="span12">

                <div class="careerMomentsItems" data-url="careerMomentsData.do?method=fetchDataForDetail">
                    <!-- dynamic - careerMomentsSet.html -->
                </div>

            </div><!-- /.span12 -->
        </div><!-- /.row-fluid -->
    </div>
</div><!-- /#careerMomentsPageView -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script type="text/javascript">
    $(document).ready(function() {
    var careerMomentsDropDowns = ${dropdownJson};
    
    G5.props.URL_JSON_CAREERMOMENTS_DATA = G5.props.URL_ROOT+'careerMomentsData.do?method=fetchDataForDetail';
    G5.props.URL_JSON_CAREERMOMENTS_SEARCH_RESULTS = G5.props.URL_ROOT+'careerMomentsData.do?method=fetchDataForDetail';
    
        //attach the view to an existing DOM element
        var cmpv = new CareerMomentsPageView({
            el:$('#careerMomentsPageView'),
            cmdd : careerMomentsDropDowns,
            pageNav : {
                back : {
                    text : 'Back',
                    url : 'layout.html'
                },
                home : {
                    text : 'Home',
                    url : 'layout.html?tplPath=base/tpl/&amp;tpl=modulesPage.html'
                }
            },
            pageTitle : 'Career Moments'
        });
    });
</script>
