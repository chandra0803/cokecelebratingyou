<!-- ======== CLAIM ADMIN FORM PREVIEW PAGE ======== -->

<div id="claimPageAdminFormPreviewView" class="page-content">
    <div id="claimForm" class="row-fluid">
        <div class="span12">
        	<div class="form-horizontal">
            	<jsp:include page="../productclaim/customFormElements.jsp"></jsp:include>
            </div>
        </div>
    </div><!-- /#claimForm -->
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){
    //attach the view to an existing DOM element
        cpafp = new PageView({
            el:$('#claimPageAdminFormPreviewView'),
            pageNav : {},
            pageTitle : '',
            loggedIn: false
        });
    });
</script>
