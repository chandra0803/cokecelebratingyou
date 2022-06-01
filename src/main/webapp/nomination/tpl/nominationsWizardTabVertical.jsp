<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
{{#each .}}
<div class="wtTabVert{{#if isActive}} active{{/if}}"
    data-tab-uid="{{id}}"
    data-tab-name="{{name}}"
    data-tab-content="{{contentSel}}"
    data-tab-state="{{state}}" >
    <div class="wtTabVertLiner">
        <div class="wtvNumber">
            <span class="wtvTabIncomplete">{{wtvNumber}}</span>
            <span class="wtvTabComplete"><i class="icon-check-circle"></i></span>
            <span class="wtvTabEdit"><i class="icon-pencil2"></i></span>
            <span class="wtvTabError"><i class="icon-warning-circle"></i></span>
        </div>
        <div class="wtvName">
            <h2 class="wtvTabName">{{wtvName}}</h2>
            <h2 class="wtvDisplay"></h2>
        </div>
    </div>
</div><!-- /.wtTabVert -->
{{/each}}
