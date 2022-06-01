<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="pageNavViewTpl">

<!-- go back + title -->
<div id="pageNav" class="">

    <div class="container">
        <div id="pageNavLinks">
            {{#if home}}
            <p id="pageHomeLink">
                <a href="{{home.url}}"><i class="icon-home"></i><span>{{home.text}}</span></a>
            </p>
            {{/if}}

            {{#if back}}
            <p id="pageBackLink">
                {{#if back.formAction}}
                    <a id="backFormActionLink" formaction="{{back.formAction}}" formid="{{back.formId}}" href="#"><i class="icon-arrow-left"></i><span>{{back.text}}</span></a>
                {{/if}}
                {{#if back.url}}
                    <a href="{{back.url}}"><i class="icon-arrow-2-left"></i><span>{{back.text}}</span></a>
                {{/if}}
            </p>
            {{/if}}
        </div>

        {{#if pageTitle}}
        <h2 id="pageTitle">{{pageTitle}}</h2>
        {{/if}}
    </div>

</div>

</script>

<script>

	$(document).ready(function(){

		$("#backFormActionLink").click(function() {
			var formToSubmit = document.getElementById( $(this).attr( "formid" ) );
			formToSubmit.action = $(this).attr( "formaction" );
			formToSubmit.submit();
		});

	});

</script>



