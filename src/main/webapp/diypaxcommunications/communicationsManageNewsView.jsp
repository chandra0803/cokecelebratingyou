<%@ include file="/include/taglib.jspf"%>

<!-- ======== COMMUNICATIONS MANAGE BANENRS PAGE ======== -->

<div id="communicationsManageNewsView" class="page-content">
  <div class="pagination pagination-right paginationControls first"></div>
            <!--subTpl.paginationTpl=

                {{#if pagination}}
                    {{#with pagination}}
                    <ul>
                        <li class="first{{#if first.state}} {{first.state}}{{/if}}" data-page="{{first.page}}">
                            <a href="#"><i class="icon-double-angle-left"></i>&nbsp;</a>
                        </li>
                        <li class="prev{{#if prev.state}} {{prev.state}}{{/if}}" data-page="{{prev.page}}">
                            <a href="#"><i class="icon-angle-left"></i> Previous</a>
                        </li>
                        {{#each pages}}
                        <li {{#if state}}class="{{state}}"{{/if}} data-page="{{page}}">
                            <a href="#">{{#if isgap}}&#8230;{{else}}{{page}}{{/if}}</a>
                        </li>
                        {{/each}}
                        <li class="next{{#if next.state}} {{next.state}}{{/if}}" data-page="{{next.page}}">
                            <a href="#">Next <i class="icon-angle-right"></i></a>
                        </li>
                        <li class="last{{#if last.state}} {{last.state}}{{/if}}" data-page="{{last.page}}">
                            <a href="#">&nbsp;<i class="icon-double-angle-right"></i></a>
                        </li>
                    </ul>
                    {{/with}}
                {{/if}}

            subTpl-->
  </div>
</div>