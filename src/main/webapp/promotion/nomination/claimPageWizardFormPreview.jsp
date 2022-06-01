<!-- ======== CLAIM ADMIN FORM PREVIEW PAGE ======== -->

<div id="claimPageWizardFormPreviewView" class="page-content">
    <div id="claimForm" class="row">
        <div class="span12">
        	<div class="form-horizontal">
            	<jsp:include page="../nomination/nominationsWizardCustomFormElements.jsp"></jsp:include>
            </div>	
        </div>
    </div><!-- /#claimForm -->
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){
    //attach the view to an existing DOM element
        cpafp = new PageView({
            el:$('#claimPageWizardFormPreviewView'),
            pageNav : {},
            pageTitle : '',
            loggedIn: false
        });
    });
</script>