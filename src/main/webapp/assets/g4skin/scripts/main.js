/* 
========================================
G4
Main JavaScript
Version: 1.0
Date: 26 October 2009
Author: Joel Schou, Front End Developer, User Experience Team

Copyright (c) 2009 BI(R). All Rights Reserved. BI(R) is a trade name of Schoeneckers, Inc.
www.biworldwide.com
========================================
*/

$(window).load(function () {

    pageIsLoaded();
    
});

// this can be outside of document.ready because it relies on the jQuery .live event binding
processModalKills("body");

$(document).ready(function() {
    
    // dynamically inserted to properly reset floating columns in stupid IE6,IE7
    if( jQuery.support.cssFloat == false ) {
        $("div.column2b, div.column3c, div.column3bc, div.column4d, div.column4bcd").after('<br class="ieshim" />');
        $("dl.tablelike dt").before('<br class="ieshim" />');
    }
    
    // handle drop-downs
    $("#nav > li").hover( function() {
        $(this).find("ul").show();
    }, function() {
        $(this).find("ul").hide();
    });
    
    // Initialize tabbed panels
    $('.tabbed').each(function() {
        var r = $(this);
        
        r.find(".tabs li.tab").removeClass("select").show();
        r.find(".panels li.panel").hide();
        r.find(".panels li.default").show();
    
        // Handle clicks on tabbed panels tabs
        r.find(".tabs .tab a").click( function(e) {
            e.preventDefault();
        
            // set the container holding this set of tabs
            // var container = $(this).parents(".tabbed");
        
        /* ----- only necessary for default panel support >>> ----- */
            // if there is an li.default present, set the isadefault variable to true
            var isadefault = r.find(".panels li.default").size() > 0;
            // if( r.find(".panels li.default").size() > 0 ) {
            //     var isadefault = true;
            // }
        
            // if we're clicking on the already selected tab, set the noclick variable to true
            var noclick = $(this).parents('.tab').hasClass("select");
            // if( $(this).parents('.tab').hasClass("select") === true ) {
            //     var noclick = true;
            // }
        /* ----- <<< only necessary for default panel support ----- */
        
            function tabPanelHide() {
                // remove all the select classes from tabs and make sure they're visible
                r.find(".tabs li.tab").removeClass("select").show();
                // hide all panels and remove all the select classes from panels
                r.find(".panels li.panel").fadeOut().removeClass("select");
            }
        
        /* ----- only necessary for default panel support >>> ----- */
            // if we're clicking on a selected tab
            if( noclick === true ) {
            
                // if a default panel is present, fade it in
                if( isadefault === true ) {
                
                    tabPanelHide();
                
                    // select the default panel
                    r.find(".panels li.default").fadeIn().addClass("select");

                }
            
                // if there is no default panel, do nothing
                else {
                    return false;
                }
            
            }
        
            // if we're clicking on an unselected tab
            else {
        /* ----- <<< only necessary for default panel support ----- */
                
                tabPanelHide();
        
                // get the index of the clicked on tab
                var index = r.find(".tabs .tab").index( $(this).parents('.tab') );
                // select the clicked on tab
                $(this).parents('.tab').addClass("select");
            
                // select the proper panel
                r.find(".panels li.panel:not(.default):eq("+index+")").fadeIn().addClass("select");

        /* ----- only necessary for default panel support >>> ----- */            
            }
        
        });
    
        r.find(".tab:eq(0) a").click();
    });
    
    // process links that open in a new window
    processNewWindowLinks("body");
    
    
    // process any potential tooltips
    processToolTips("body");
    
    
    // process any potential modals
    processModals("body");
    (typeof openModalCount == 'undefined') ? openModalCount = 0 : null;
    (typeof modalStackOrder == 'undefined') ? modalStackOrder = new Array() : null;
    $(window).keydown(function(e){
        switch(e.keyCode) {
            case 27:
                $("#modal"+modalStackOrder[openModalCount-1]+" a.button-close-modal").click();
                break;
        }
    });
    $(window).scroll(function() {
        if( $('html').hasClass('modalon') ) {
            var fullHeight = ( $('#container').outerHeight() > $(window).height() ) ? $('#container').outerHeight() : $(window).height();
            $("#modaloverlay").css({'height': fullHeight});
            
            $('.modal').each(function() {
                var id = $(this).attr('id').replace(/modal/,'');
                sizeModal(id);
            });
        }
    })
    
    
    // process form elements
    processFormElements("body");
    
    
    // pagelayout1 prettytop page width fanciness
    if( $(".prettytop #submain").size() > 0 ) {
        
        $(".prettytop #submain").addClass("dynamic").after('<div class="thepretty"></div>');
    
        // Center #container in the window
        function prettyTopAdjust() {
            var main_width = $("#main").width();

            var window_width = Math.max( $("body").width() , main_width );
            var pretty_width = $(".prettytop .thepretty").width();
            var margin_width = (window_width - main_width) / 2;
            var width_offset = Math.ceil(pretty_width - margin_width);
            
            if( pretty_width > margin_width ) {
                $(".prettytop #subheader")
                    .css({ 'padding-right' : (margin_width + width_offset) , 'padding-left' : margin_width });
                $(".prettytop #subheader h3, .prettytop #submain")
                    .width( main_width - width_offset );
            }
        }
        // Fire it on the first page load
        prettyTopAdjust();
    
        // Fire it on every window resize (could get burdensome on crappy computers/browsers)
        $(window).resize(function(){
            prettyTopAdjust();
        });
    }
    
    // pagelayout1 prettybot page fanciness
    if( $(".prettybot").size() > 0 ) {
        $(".prettybot #footer").prepend('<div class="thepretty"></div>');
    }
    
    // pagelayout2 prettytop page width fanciness
    if( $(".pagelayout2 .prettytop #subnav").size() > 0 ) {
        $(".pagelayout2 .prettytop #subnav").before('<div class="thepretty"></div>');
    }

    // an attempt to fix table cells that don't have anything in them
    $("td:empty").html(" ");
    

});


function pageIsLoaded() {
    if( $("#loading").length > 0 && $("#loading").parents(".modal").css("display") == "block" ) {
        $("#loading a").click();
    }
    //setTimeout( function() { $("#loading").parents(".modal").remove() }, 1000);
}


function processNewWindowLinks(scope) {
    // scope should be passed as a jQuery selector
    var root = scope;
    
    // Process all links that open in new windows
    $(root+" a[rel=window]").click( function(e) {
        e.preventDefault();
        window.open(this.href);
    });
}


function processToolTips(scope) {
    // scope should be passed as a jQuery selector
    var root = scope;
    var random = Math.random()*1000*Math.PI;
    
    // Process all links that trigger tooltips
    $(root+" [rel*=tooltip]").each( function(i) {
        // Assign incremental IDs to all tooltip triggers IF they don't already have an ID
        // i++;
        $(this).attr("id", ($(this).attr('id') || "tt-triggerid_"+Math.floor( random*Math.sqrt(i+1) )));
        
        // build the tooltips
        var title = $(this).attr("title");
        title = title.replace(/{/g,"<").replace(/}/g,">");
        $("body").append('<div class="tooltip" id="tooltip_'+Math.floor( random*Math.sqrt(i+1) )+'_'+$(this).attr('id')+'"><div>' + title + '</div></div>');
        $(this).attr("title",'');


        // Handle hovers on tooltip triggers
        $(this).mouseenter( function() {
            var target = this;
            var ttid = 'tooltip_'+Math.floor( random*Math.sqrt(i+1) )+'_'+$(this).attr('id');
            generateToolTip = setTimeout( function() { showToolTip(target,ttid); }, 500);
        })
        .mouseleave( function() {
            clearTimeout(generateToolTip);
            generateToolTip = '';
            var target = this;
            var ttid = 'tooltip_'+Math.floor( random*Math.sqrt(i+1) )+'_'+$(this).attr('id');
            hideToolTip(target,ttid);
        });
    });
    
    // Process complex tooltip links
    $(root+" [rel*='tooltip[']").each( function(i) {
        // var id = $(this).attr("id").replace(/trigger/,'tooltip');
        var id = 'tooltip_'+Math.floor( random*Math.sqrt(i+1) )+'_'+$(this).attr('id');
        var rel = $(this).attr("rel").replace(/tooltip\[/,'').replace(/\]/,'');
        $("#"+id).addClass(rel);
    });
}
function showToolTip(target,id) {
    var triggeroffset = $(target).offset();
    var triggerwidth = $(target).outerWidth();
    var tooltipheight = $("#"+id).outerHeight();

    $("#"+id).css({
        top : triggeroffset.top-tooltipheight,
        left : triggeroffset.left+(triggerwidth/2)
    });
    $("#"+id).show().animate({
        opacity: 1,
        top: triggeroffset.top-tooltipheight
    });
}
function hideToolTip(target,id) {
    // hide the tooltip right away
    killToolTip = setTimeout( function() { $("#"+id).animate({opacity: 0}).queue( function() { $(this).hide().dequeue(); }); }, 0 );
}


function processModals(scope) {
    // scope should be passed as a jQuery selector
    var root = scope;
    
    // add the overlay
    if( $("#modaloverlay").length < 1 ) {
        $("body").append('<div id="modaloverlay"><iframe src="about:blank" id="modaloverlayiframe" /></div>');
    }
    
    // Process all links that call modals
    $(root+" [rel*=tomodal]").each( function(i) {
        // Assign incremental IDs to all modals
        i++;
        var random = Math.floor( Math.random()*10000*Math.PI*i );
            
        // Handle clicks on modal triggers
        $(this).click( function(e) {
            e.preventDefault();
            
            if( $(this).hasClass('button-disabled') == false || $(this).hasClass('fancy-disabled') == false ) {

                processModalExtensions(this);
        
                // make an allowance for static modals not using the auto-generated random identifier
                if( modalMethod == 'static' ) {
                    random = staticRandom;
                }
            
                showModal(random, { method : modalMethod, cache : modalCache, source : modalSource, noclose : modalNoClose, width : modalWidth });
            
            }

        });
        
    });
    
    // Process all buttons/forms that have a modal as the action
    $(root+" button.tomodal").each( function(i) {
        // Assign incremental IDs to all modals
        i++;
        var random = Math.floor( Math.random()*10000*Math.PI*i );

        // Handle clicks on modal triggers
        $(this).parents("form").submit( function() {
            
            alert("forms that launch a modal need to be updated. I have to figure out how to pass the options with valid HTML");
            //showModal(this,random,"action");
            
            // prevent form from submitting
            return false;
        });
    });
}

// Process all triggers that kill modals
function processModalKills(scope) {
    // scope should be passed as a jQuery selector
    var root = scope;
    
    $(root+" *[rel*=killmodal]").live("click", function(e) {
        e.preventDefault();
        
        if( $(this).hasClass('button-disabled') == false || $(this).hasClass('fancy-disabled') == false ) {
            var extended = $(this).attr("rel").replace(/killmodal\[/,'').replace(/\]/,'');
            switch(extended) {
                case 'killmodal':
                    closeModal("current");
                    break;
                case 'all':
                    closeModal("all");
                    break;
            } // /switch
        } // /if
        
    });
}

// process the click extensions
function processModalExtensions(trigger) {

    // handle extended options
    var extended = $(trigger).attr("rel").match(/tomodal\[\S+\]/g);
    if( extended != null ) {
        extended = extended.toString().replace(/tomodal\[/,'').replace(/\]/,'');

        // check for an alternate method
        if( extended.match(/static/) == "static" ) {
            modalMethod = "static";
            modalSource = "";
            staticRandom = new Number( ($(trigger).is('a')) ? $(trigger).attr("href").replace(/#modal/,'') : $(trigger).attr("formaction").replace(/#modal/,'') );
            modalCache = true;
        }
        else if ( extended.match(/iframe/) == "iframe" ) {
            modalMethod = "iframe";
            modalSource = ($(trigger).is('a')) ? $(trigger).attr("href") : $(trigger).attr("formaction");
            modalCache = false;
        }
        else {
            modalMethod = "ajax";
            modalSource = ($(trigger).is('a')) ? $(trigger).attr("href") : $(trigger).attr("formaction");
            modalCache = false;
        }
        
        // check for caching ajax-supplied code
        if( extended.match(/cacheon/) == "cacheon" ) {
            modalCache = true;
        }
        // check for NOT caching static-supplied code
        else if ( extended.match(/cacheoff/) == "cacheoff" ) {
            modalCache = false;
        }
        
        // check for non-closable modals
        if( extended.match(/noclose/) == "noclose" ) {
            modalNoClose = true;
        } else {
            modalNoClose = false;
        }
        
        // check for wide modals
        if( extended.match(/wider/) == "wider" ) {
            modalWidth = "wider";
        }
        else if ( extended.match(/widest/) == "widest" ) {
            modalWidth = "widest";
        }
        else {
            modalWidth = undefined;
        }
    }
    else {
        modalMethod = "ajax";
        modalCache = false;
        modalSource = ($(trigger).is('a')) ? $(trigger).attr("href") : $(trigger).attr("formaction");
        modalNoClose = false;
        modalWidth = undefined;
    }
}

// modal functions
function showModal(random,options) {
    // random is an integer
    
    var options  =  (typeof options != 'undefined')             ?  options               :  new Array();
    var method   =  (typeof options['method'] != 'undefined')   ?  options['method']     :  'ajax';
    var cache    =  (typeof options['cache'] != 'undefined')    ?  options['cache']      :  false;
    var source   =  (typeof options['source'] != 'undefined')   ?  options['source']     :  '';
    var noclose  =  (typeof options['noclose'] != 'undefined')  ?  options['noclose']    :  false;
    var width    =  (typeof options['width'] != 'undefined')    ?  ' '+options['width']  :  '';
    
    /* Options
    method == ajax(default), static, iframe
    cache == false(default) or true -- NOTE: only relevant for method = ajax
    source == null(default), [string], [string] -- NOTE: depends on, and matches up with, method
    noclose == false(default) or true
    width == null(default), wider, or widest
    */
    
    // create the modal frame for ajax/iframe modals
    if( $("#modal"+random).length < 1 && (method == 'ajax' || method == 'iframe') ) {
        $("body").append('<div class="modal'+width+'" id="modal'+random+'"><div class="content"></div></div>');
    }
    
    // if the modal exists
    if( $("#modal"+random).length > 0 ) {
    
        // add the overlay if it doesn't yet exist
        if( $("#modaloverlay").length < 1 ) {
            $("body").append('<div id="modaloverlay"><iframe src="about:blank" id="modaloverlayiframe" /></div>');
        }
    
        // start the modal count and stackorder if they haven't been
        (typeof openModalCount == 'undefined') ? openModalCount = 0 : null;
        (typeof modalStackOrder == 'undefined') ? modalStackOrder = new Array() : null;
        
    
        // necessary because IE6 can't handle fixed positioning AND it shows form elements through the overlay
        // var modalTop = $(this).scrollTop();
        // console.log(this);
        // modalPosition = $("#modaloverlay").css("position");
        var fullHeight = ( $('#container').outerHeight() > $(window).height() ) ? $('#container').outerHeight() : $(window).height();
        // if( modalPosition == "absolute" ) {
            $("#modaloverlay").css({'height': fullHeight});
            // $("select").css({ 'visibility' : 'hidden' });
            // $("#modal"+random).find("select").css({ 'visibility' : 'visible' });
        // }
        
        
        // if getting the source from a local URL via ajax
        if( method == "ajax" && $("#modal"+random).hasClass("static") == false ) {
            $("#modal"+random).find("div").load(source, function() {
            
                // size the modal so it doesn't overflow the window
                sizeModal(random);

                // run processing functions on the new code
                processToolTips("#modal"+random);
                processNewWindowLinks("#modal"+random);
                processModals("#modal"+random);
                processFormElements("#modal"+random);

                showit();
            
            });
        }
        // if getting the source from a remote URL via iframe
        else if( method == "iframe" ) {
            $("#modal"+random).addClass("iframe").find("div.content").append('<iframe src="'+source+'">Your browser does not support frames. You may <a href="'+source+'" target="_blank">visit the source of this page</a>.</iframe>');

            // size the modal so it doesn't overflow the window
            sizeModal(random);
            showit();
        }
        // if getting the source from static HTML
        else if( method == "static" || $("#modal"+random).hasClass("static") == true ) {
            // size the modal so it doesn't overflow the window
            sizeModal(random);
            showit();
        }

    
        // if a width is set in the options but not yet applied
        if( width != '' && $("#modal"+random).hasClass(width) == false ) {
            $("#modal"+random).addClass(width);
        }
    
        // keep the modal around if caching is turned on
        if( cache == true ) {
            $("#modal"+random).addClass("static");
        }
    
        // if modal is closable
        if( noclose == false && $("#modal"+random+" a.button-close-modal").length < 1 ) {
            $("#modal"+random).prepend('<a class="button-close-modal" href="#" rel="killmodal">close</a>');
        }
        // if modal is nonclosable
        if( noclose == true && $("#modal"+random+" a.button-close-modal").length >= 1 ) {
            $("#modal"+random+"a.button-close-modal").remove();
        }
    
        // necessary because IE6 can't handle fixed positioning
        // if( modalPosition == "absolute" ) {
            // apparently FF has trouble calculating the CSS positioning values for hidden elements. That hardcoded 100 isn't ideal.
            // var modalOffset = new Number( $("#modal"+random).css("top").replace(/px/,'') );
            // $(".modal").css("top",modalTop+modalOffset);
            // $(".modal").css("top",modalTop);
            // $(window).scrollTop(modalTop);
        // }
        
    } // end if modal exists
    else {
        alert("Modal could not be opened.");
    }
    
    function showit() {
        // console.log('showit');
        var modalTop = $(window).scrollTop();
        // console.log('modalTop2 '+modalTop);
        // show the overlay if not already visible
        if( openModalCount == 0 ) {
            $('html').css({
                'width' : $(window).width(),
                'overflow' : 'hidden'
            }).addClass('modalon');
            $("#modaloverlay").fadeIn(125);
            $(window).scrollTop(modalTop);
        }

        // Hide other modals
        $(".modal:visible:not(#modal"+random+")").stop().animate({ opacity : 0 },125);

        $("#modal"+random).fadeIn(125, function() {
            sizeModal(random);
        });

        modalStackOrder[openModalCount] = random;
        openModalCount++;
    }
    
}

function sizeModal(id) {
    // console.log('sizeModal');
    // id is an integer
    var modalstring = ( typeof id == 'number' ) ? "#modal"+id : ".modal" ;
    
    // don't run this on modals in the process of being closed, otherwise they will stick on the screen
    if( !$(modalstring).hasClass('closing') ) {
    
        // determine how much space is available for the modal
        var modalTop = $(window).scrollTop();
        // console.log('modalTop '+modalTop);
        var windowHeight = $(window).height();
        // console.log('windowHeight '+windowHeight);
        var bodyHeight = $('body').height();
        // console.log('bodyHeight '+bodyHeight);
        var modalHeight = $(modalstring).outerHeight();
        // console.log('modalHeight '+modalHeight);
        var offset = $(modalstring).offset();
        // console.log('offset '+offset.top);
        var position = $(modalstring).position();
        // console.log('position '+position.top);
        // var modalOffset = offset.top-position.top;
        var modalOffset = windowHeight/10;
        // console.log('modalOffset '+modalOffset);
        // var modalOffset = new Number( $(modalstring).css("top").replace(/px/,'') );
    
        // var scrolled = $('body').scrollTop();
        // $('#modaloverlay').css("top",scrolled);
    
        if( windowHeight < (modalHeight + (modalOffset*2)) ) {
            var modalSlush = modalHeight - $(modalstring).height();
            // console.log('modalSlush '+modalSlush);

            var newModalHeight = windowHeight - (modalOffset*2) - modalSlush;
            // console.log('newModalHeight '+newModalHeight);
            if( $(modalstring+" .scroller").length < 1 ) {
                $(modalstring+" .content").wrap('<div class="scroller"></div>');
            }
            $(modalstring+" .scroller").height(newModalHeight);

            // if there is an iframe
            if( $(modalstring+" iframe").length > 0 ) {
                $(modalstring+" iframe").height(newModalHeight);
            }
        
            // if there is a functions bar
            if( $(modalstring+" .functions").length > 0 ) {
                $(modalstring+" .scroller").after( $(modalstring+" .functions") );
            }
        
            modalHeight = newModalHeight + modalSlush;
        }
        // console.log('modalHeight '+modalHeight);
        var modalTop = $(window).scrollTop();
        // console.log('modalTop '+modalTop);
        $(window).scrollTop(modalTop);
        // var newModalTop = modalTop + (windowHeight/2);
        // var newModalMarginTop = modalHeight/2;
        var newModalTop = modalTop + windowHeight;
        var newModalMarginTop = modalHeight + (windowHeight-modalHeight)/3*2;
        $(modalstring).stop().animate({
            'opacity' : 1,
            'top' : newModalTop,
            'margin-top' : -newModalMarginTop
        },125);
        // console.log('-------------');
        
    } // end if 'closing'
    
}

function closeModal(howmany) {
    openModalCount--;

    if( howmany == "current" || howmany == null ) {
        $("#modal"+modalStackOrder[openModalCount])
            .addClass('closing')
            .stop()
            .fadeOut(125)
            .animate({
                opacity : 0
            }, 125, function() {
                if( $(this).hasClass("static") == false ) {
                    $(this).remove();
                }
                else {
                    $(this).removeClass('closing');
                }
            });
        if( openModalCount == 0 ) {
            $("#modaloverlay").fadeOut(125,function() {
                $('html').css({
                    'overflow' : 'auto',
                    'overflow-x' : 'visible',
                    'width' : 'auto'
                }).removeClass('modalon');
            });
            
            // necessary because IE6 shows form elements through the overlay
            // if( modalPosition == "absolute" ) {
            //     $("select").css({ 'visibility' : 'visible' });
            // }
        }
        else {
            $("#modal"+modalStackOrder[openModalCount-1]).animate({ opacity : 1 },125);
            
            // necessary because IE6 shows form elements through the overlay
            // if( modalPosition == "absolute" ) {
            //     $("#modal"+modalStackOrder[openModalCount-1]).find("select").css({ 'visibility' : 'visible' });
            // }
        }
    }
    else if( howmany == "all" ) {
        openModalCount = 0;
        $(".modal:visible").stop().fadeOut(125).animate({ opacity : '' },125, function() {
            if( $(this).hasClass("static") == false ) {
                $(this).remove();
            }
        });
        $("#modaloverlay").fadeOut(125,function() {
            $('html').css({
                'overflow' : 'auto',
                'overflow-x' : 'visible',
                'width' : 'auto'
            }).removeClass('modalon');
        });
        
        // necessary because IE6 shows form elements through the overlay
        // if( modalPosition == "absolute" ) {
        //     $("select").css({ 'visibility' : 'visible' });
        // }
    }
}

function processFormElements(scope) {
    // scope should be passed as a jQuery selector
    var root = scope;
    
    // initialize field ghosts
    $(root+" label .ghost").each( function() {
        
        var v = $(this).parent().find('input,textarea,select').val();
        if( v == '' || v == undefined ) {
            $(this).parent().addClass('ghosted');                
        }
        
        $(this).parent().find('input,textarea,select').focus(function() {
            $(this).parent().removeClass('ghosted');
        });
        $(this).parent().find('input,textarea,select').blur(function() {
            var v = $(this).val();
            if( v == '' || v == undefined ) {
                $(this).parent().addClass('ghosted');                
            }
        });
    });

    // initialize datepicker widgets
    if( $(root+" input.usedatepicker").length > 0 ) {
        $(root+" input.usedatepicker").datepicker({
            showAnim: 'fadeIn',
            changeMonth: true,
            changeYear: true
        }).after('<span class="icon"></span>');
        $("#ui-datepicker-div a[href='#']").live('click',function(e) {
            e.preventDefault();
        });
    }
    
    //initialize custom datepicker widgets, dates in specified range only will be enabled rest of the calender is disabled for selection.
    if( $(root+" input.customdatepicker").length > 0 ) {
    	var currentDate = new Date(); //set current date
    	var validDays = parseInt( $('input.customdatepicker').data('custom') );
    	var targetDate = new Date(); //set base for target date so currentDate does not get overridden

    	targetDate.setTime(currentDate.getTime());
    	targetDate.setDate(targetDate.getDate() + validDays);
    	targetDate.toDateString();
        
                                        
		$(root+" input.customdatepicker").datepicker({
		showAnim: 'fadeIn',
		changeMonth: true,
		changeYear: true,
        beforeShowDay: function (date) {
        	if( date >= currentDate && targetDate >= date ){
        		return[true, ''];
        	}
        	return [false, ''];

        }
		}).after('<span class="icon"></span>');
		$("#ui-datepicker-div a[href='#']").live('click',function(e) {
		e.preventDefault();
		});
	}


    // initialize select form fields
    $(root+" .content label select").each( function() {
        // var selectvalext = $(this).find("option:selected").text();
        // if( $(this).parents("label").hasClass("multi") === false ) {
        //     $(this).css({ 'opacity' : 0 }).parent().prepend('<span class="faux">'+selectvalext+'</span>');
        // }
        // $(this).change( function() {
        //     var selectvalext = $(this).find("option:selected").text();
        //     $(this).attr("title",selectvalext);
        //     if( $(this).parents("label").hasClass("multi") === false ) {
        //         $(this).parent().find(".faux").text(selectvalext);
        //     }
        // });
        // $(this).focus( function() {
        //     $(this).parent().addClass("focus");
        // });
        // $(this).blur( function() {
        //     $(this).parent().removeClass("focus");
        // });
    });

    // handle clicks on reset buttons for special select fields
    $(root+" button[type=reset]").click(function() {
        $(root+" .content label:not(.multi) select").each(function() {
            var resetval = $(this).find("option:eq(0)").text();
            $(this).attr("title","").parent().find(".faux").text(resetval);
        });
    });

    // initialize password and search fields
    $(root+" input[type=password],"+root+" input[type=search],"+root+" label.search input.text").after('<span class="icon"></span>');
    
    // initialize multimove widgets
    $(root+" div.multimove").each( function() {
        
        var subroot = this;
        
        // set the widths of the select boxes
        function setSelectWidth() {
            $(subroot).find("span.select").each( function() {
                $(this).find("select").width( $(this).width() );
            });
        }
        setSelectWidth();
        
        // add titles to all options in case they're too long
        $(subroot).find("option").each(function() {
    		$(this).attr("title", $(this).text() );
    	});
        
        // click on a select all link
        $(subroot).find("a.selectall").click(function() {
    		$(this).next().find("option").attr("selected","selected");
    	});
    	
    	// click on an add/remove link
    	$(subroot).find(".movers a").click(function() {
    	    var thisIndex = $(subroot).find(".movers a").index(this);
    	    store = $(subroot).find("select:eq("+thisIndex+")").find("option:selected");
    	    $(subroot).find("select:not(:eq("+thisIndex+"))").prepend(store);
    		store.attr("selected","");
            
            if( $(subroot).find(".destination option").length == 0 ) {
                $(subroot).find(".order .atoz a").addClass("button-disabled");
            } else {
                $(subroot).find(".order .atoz a").removeClass("button-disabled");
            }
    	});
    	
    	// disable/enable the move buttons
    	$(subroot).find("select").focus(function() {
    	    var thisIndex = $(subroot).find("select").index(this);
    	    $(subroot).find(".movers a:eq("+thisIndex+")").removeClass("button-disabled");
    	}).blur( function() {
    	    var thisIndex = $(subroot).find("select").index(this);
    	    $(subroot).find(".movers a:eq("+thisIndex+")").addClass("button-disabled");
    	});
    	
    	// Alphabetizing the source list whenever it changes
        $(subroot).find(".movers .remove a").click(function() {
            alphabetizeSelect( $(subroot).find(".source select") );
    	    testOrderButtons();
        });
    	
    	// disabled/enable the reordering buttons
    	$(subroot).find(".destination select").focus(function() {
    	    testOrderButtons();
        }).change(function(){
            testOrderButtons();
        }).blur(function() {
            testOrderButtons();
        });
        
        // Handling clicks on the order buttons
        $(subroot).find(".order li:not(.atoz) a").click(function() {
            // determine which button we've clicked
            var moveDirection = $(subroot).find(".order li:not(.atoz) a").index(this);
            
            if( $(this).hasClass("button-disabled") == false ) {
                
                if( moveDirection == 0 ) {
                    $(subroot).find(".destination option:selected:first").prev().before( $(subroot).find(".destination option:selected") );
                }
                else {
                    $(subroot).find(".destination option:selected:last").next().after( $(subroot).find(".destination option:selected") );
                }
            }
            testOrderButtons();
        });
    	
    	// Alphabetizing the destination list when the a button is clicked
        $(subroot).find(".order .atoz a").click(function() {
            alphabetizeSelect( $(subroot).find(".destination select") );
    	    testOrderButtons();
        });
        
        function testOrderButtons() {
            $(subroot).find(".order .moveup a").addClass("button-disabled");
            $(subroot).find(".order .movedn a").addClass("button-disabled");
            
            if( $(subroot).find(".destination option:selected").length > 0 && $(subroot).find(".destination option:selected").length < $(subroot).find(".destination option").length ) {
                
                if( $(subroot).find(".destination option:first").attr("selected") == false ) {
                    $(subroot).find(".order .moveup a").removeClass("button-disabled");
                }
                if( $(subroot).find(".destination option:last").attr("selected") == false ) {
                    $(subroot).find(".order .movedn a").removeClass("button-disabled");
                }
                if( $(subroot).find(".destination option:first").attr("selected") == true && $(subroot).find(".destination option:last").attr("selected") == true ) {
                    $(subroot).find(".order .moveup a").removeClass("button-disabled");
                    $(subroot).find(".order .movedn a").removeClass("button-disabled");
                }
            }
        }
        
        function alphabetizeSelect(select) {

            // Set up an array for the select contents
            var selectContents = new Array();

            // Cycle through all of the options to get their values and text
            $(select).children("option").each(function() {
                selectContents.push( $(this).text()+"::"+$(this).attr("value") );
            });

            // Sort the array
            selectContents.sort(alphanum);

            // Initialize the select options string
            selectString = "";

            // Build the string
            for( var index in selectContents ) {
                var tempString = selectContents[index];
                var tempArray = tempString.split("::");
                selectString += "<option value=\""+tempArray[1]+"\">"+tempArray[0]+"</option>\n";
            }

            // Update the contents of the select with the new string
            $(select).html(selectString);
        }
    	
    	// handle clicks on the tools links
    	$(subroot).find(".tools a").click( function() {
    	    
    	    var thisIs = $(this).parent().attr("class");
    	    var isThisOn = $(this).hasClass("button-cancel");
    	    
    	    isThisOn == false ? $(this).addClass("button-cancel") : $(this).removeClass("button-cancel");
    	    
    	    switch(thisIs) {
                case 'taller':
                    isThisOn == false ? $(subroot).addClass("tall").find(".taller span span").text("shorter") : $(subroot).removeClass("tall").find(".taller span span").text("taller");
                    break;
                case 'wider':
                    isThisOn == false ? $(subroot).addClass("wide").find(".wider span span").text("narrower") : $(subroot).removeClass("wide").find(".wider span span").text("wider");
                    break;
                case 'reset':
                    $(subroot).removeClass("wide tall");
                    $(subroot).find(".tools a.button-cancel").removeClass("button-cancel")
                    $(subroot).find(".wider span span").text("wider")
                    $(subroot).find(".taller span span").text("taller");
                    $(this).addClass("button-disabled");
                    break;
            }
    	    
    	    var triggerCount = $(subroot).find(".tools a.button-cancel").length;
    	    
    	    triggerCount == 0 ? $(subroot).find(".tools li.reset a").addClass("button-disabled") : $(subroot).find(".tools li.reset a").removeClass("button-disabled");
    	    
    	});
    });
    
    // de-style radio button and checkbox fields
    $(root+" input[type=radio], "+root+" input[type=checkbox]").addClass("rdochk");
    
    // handle ajax-submitted forms
    $(root+" form.viaajax").submit( function() {
        var form = this;
        var parent = $(this).parent();
        var method = $(this).attr("method");
        var action = $(this).attr("action");
        var id = $(this).attr("id");
        
        switch(method) {
            case 'get':
                $.get(action, function(data) {
                    $(form).replaceWith(data);
                });
                break;
            case 'post':
                $.post(action, $("#"+id).serialize(), function(data) {
                    $(form).replaceWith(data);
                });
        }
        doBGFade( $(parent),[245,255,159],[255,255,255],'transparent',75,20,4 );
        return false;
    });
}


function initCarousel(index,options) {
    // index is an ID

    /* ----- procedural setup >>>> ----- */
    var root = index;
    
    var options        =  (typeof options != 'undefined')                   ?  options                   :  new Array();
    var transition     =  (typeof options['transition'] != 'undefined')     ?  options['transition']     :  'slide';
    var timer          =  (typeof options['timer'] != 'undefined')          ?  options['timer']          :  5000;
    var showcontrols   =  (typeof options['showcontrols'] != 'undefined')   ?  options['showcontrols']   :  'instant';
    var addindexes     =  (typeof options['addindexes'] != 'undefined')     ?  options['addindexes']     :  false;
    var addtitles      =  (typeof options['addtitles'] != 'undefined')      ?  options['addtitles']      :  false;
    var autoplay       =  (typeof options['autoplay'] != 'undefined')       ?  options['autoplay']       :  false;
    var lazypages      =  (typeof options['lazypages'] != 'undefined')      ?  options['lazypages']      :  false;
    var lazyitemlimit  =  (typeof options['lazyitemlimit'] != 'undefined')  ?  options['lazyitemlimit']  :  horseCount;
    
    /* Options
    transition == slide(default) or fade
    timer == 5000(default) or [integer]
    showcontrols == instant(default) or animate
    addindexes == false(default) or true
    addtitles == false(default) or true
    autoplay == false(default) or true
    lazypages == false(default) or true
    lazyitemlimit == horseCount(default) or [integer]
    */
    
    
    if( lazypages == true ) {
        // add placeholder horses for lazy-loaded carousels
        for( i = 1; i <= lazyitemlimit; i++ ) {
            $("#"+root+" ul.horses").append("<li class=\"horse lazyplaceholder\"></li>");
        }
        
        // fill in the horses via ajax if the option is turned on
        var trigger = $("#"+root+" .carousel-interact .controls .prev a");
        loadLazyData();
    }
    
    var horseCount = $("#"+root+" .horses li.horse:visible").size();
    var startHorse = 0;
    var carouselPosition = 0;

    // get the width of the wrapper window
    var trackSize = $("#"+root+" .carousel").width();
    // get the width of an individual item
    var horseSize = $("#"+root+" .horses li:first").outerWidth(true);

    // figure out how many items fit in the window
    var horsesPerPage = Math.floor(trackSize / horseSize);
    // figure out how many times the carousel would have to slide to show all the items
    var pagesOfHorses = Math.ceil(horseCount / horsesPerPage);


    if( transition == 'fade' ) {
        $("#"+root).addClass('carousel-transition-fade');

        // set the width of the list of shows
        $("#"+root+" ul.horses").css("width", trackSize);
        
        // set all the horses to absolute positioning
        $("#"+root+" ul.horses li.horse").css("position", "absolute").hide();
        
        $("#"+root+" ul.horses li.horse:eq("+startHorse+")").show();
        
        var startPage = startHorse;

    } else {
        $("#"+root).addClass('carousel-transition-default');
    
        // set the width of the list of shows
        var horsesSize = trackSize * pagesOfHorses;
        $("#"+root+" ul.horses").css("width", horsesSize);

        // set the page to that of the current show
        var startPage = Math.floor(
            startHorse / horsesPerPage
        );
    
        $("#"+root+" .horses").css({ left: "-"+(startPage * trackSize)+"px" });

    }
    
    // add index icons if the option is turned on
    if( addindexes == true ) {
        $("#"+root).addClass('carousel-indexes-on');
        // recalculate the width of the wrapper window
        trackSize = $("#"+root+" .carousel").width();
        // recalculate the width of an individual item
        horseSize = $("#"+root+" .horses li:first").outerWidth(true);
        
        if( lazypages == true ) {
            var url = $("#"+root+" .carousel-interact .controls .prev a").attr("href");
        } else {
            var url = '';
        }
        // build the indexes list
        $("#"+root+" .carousel-interact").append("<ol class=\"indexes\"></ol>");
        for( i = 0; i < pagesOfHorses; i++ ) {
            var horseclass = $("#"+root+" .horses li.horse:eq("+i+")").attr("class")
                .replace(/horse/,'')
                .replace(/select/,'')
                .replace(/lazyplaceholder/,'')
                .replace(/ /,'');
            $("#"+root+" ol.indexes").append('<li class="index"><a href="' + url + '#slide' + (i+1) + '">' + (i+1) + '</a></li>');
            if( horseclass != '' ) { $("#"+root+" .indexes li.index:eq("+i+")").addClass(horseclass); }
        }
        $("#"+root+" ol.indexes li.index:eq(0)").addClass("select");
    }
    
    
    // show the controls if there is more than one page of horses
    if( pagesOfHorses > 1 ) {
        $("#"+root).addClass('carousel-controls-on');
        // recalculate the width of the wrapper window
        trackSize = $("#"+root+" .carousel").width();
        // recalculate the width of an individual item
        horseSize = $("#"+root+" .horses li:first").outerWidth(true);
        
        if( showcontrols == "animate" ) {
            $("#"+root+" .carousel-interact").show().animate({ bottom: "0px" }, 500);
        } else {
            $("#"+root+" .carousel-interact").show();
        }
    }
    
    // add the titles scroller if the option is turned on
    if( addtitles == true ) {
        $("#"+root).addClass('carousel-titles-on');
        
        $("#"+root+" .carousel-interact").append("<ol class=\"titles\"></ol>");
        
        var interactwidth = $("#"+root+" .carousel-interact").outerWidth(true);
        var controlswidth = $("#"+root+" .carousel-interact .controls").outerWidth(true);
        var indexeswidth = $("#"+root+" .carousel-interact .indexes").outerWidth(true);
        $("#"+root+" .carousel-interact .titles").width(interactwidth - controlswidth - indexeswidth);
        
        $("#"+root+" .horses li.horse").each( function(i) {
            i++;
            var thistitle = $(this).find("h2").text();
            $("#"+root+" ol.titles").append("<li class=\"title\">" + thistitle + "</li>");
        });
    }


    var currentIndex = startPage;
    
    
    if( lazypages == true ) {
        // var trigger = $("#"+root+" .carousel-interact .controls .prev a");
        // loadLazyData();
    }

    // set the thumbnail of the current horse
    $("#"+root+" .horses li.horse:eq(" + startHorse + ")").addClass("select");

    // move the carousel into place for the first item
    if( pagesOfHorses > 1 ) {
        manuSlide(startPage);
    }
    
    // autoplay the slideshow
    var autoplaystate;
    var horseInterval;
    if( autoplay == true ) {
        startAutoPlay();
    }
    
    
    /* ----- <<<< procedural setup ----- */

    /* ----- general functions >>>> ----- */

    // manual click on a particular horse
    function manuSlide(page) {
        oldIndex = currentIndex;
        currentIndex = page;
        
        slideShowsList(currentIndex);
        $("#"+root+" .indexes .index").eq(oldIndex).removeClass("select");
        $("#"+root+" .indexes .index").eq(currentIndex).addClass("select");
    }

    // step from one horse to another
    function stepSlide(direction) {
        oldIndex = currentIndex;
        
        // check to see which direction we're moving
        switch(direction) {
        
            // move forward
            case 'next':
                currentIndex = (oldIndex + 1) % pagesOfHorses;
                break;
        
            // move backward
            case 'prev':
        
                // if the departing horse is not the first one
                if( oldIndex > 0 ) {
                    currentIndex = (oldIndex - 1) % pagesOfHorses;
                }
        
                // if the departing horse is the first one
                else if( oldIndex == 0 ) {
                    currentIndex = pagesOfHorses - 1;
                }
        
                break;
        
        } // /switch
        
        // if we're loading additional horses via ajax...
        // if( lazypages == true ) { trigger = $("#"+root+" .controls ." + direction + " a"); loadLazyDataIncremental(); }
        
        // slideShowsList(currentIndex);
        slideShowsList(direction);
        $("#"+root+" .indexes .index").eq(oldIndex).removeClass("select");
        $("#"+root+" .indexes .index").eq(currentIndex).addClass("select");
    }
    
    
    // handle autoplay slides
    function autoSlide() {
        stepSlide("next");
        
        // integration with dynamic toppers
        if( $("#"+root).hasClass("dynamictopperinitialized") == true ) {
            stepToppers(root,oldIndex,"next");
        }
    }


    // slide the carousel
    function slideShowsList(direction) {
        // clicking on an index link passes a number to this function
        if(typeof direction == "number") {
            // the difference of the new location to the old one is how far we're going
            var slideDistance = Math.abs(oldIndex - direction);
            // which way we're going
            var slideDirection = ((oldIndex - direction) >= 0) ? 1 : -1 ;
        }
        // clicking on a prev/next passes a string to this function
        else {
            // we're always going just one slide
            var slideDistance = 1;
            // which way we're going
            var slideDirection = ( direction == "next" ) ? -1 : 1;
        }
        
        // TODO: clean this up. Some of the base calculations can be moved to the top
        // the distance we want to travel FROM ZERO is the width of the window (horsesPerPage * horseSize) times the direction we're going
        // Note, that FROM ZERO is very important. This is an absolute move, NOT a relative one
        var normalSlide = horsesPerPage * horseSize * slideDirection;
        // the max distance we can travel is ... complicated. It just works.
        var maxSlide = horseCount * horseSize - trackSize + $("#"+root+" .horses").innerWidth() - $("#"+root+" .horses").width();
        // if our new location * the distance we want to go is bigger than the max distance of travel AND there are more horses than fit on a page...
        if( Math.abs(currentIndex*normalSlide) >= maxSlide && horseCount >= horsesPerPage ) {
            // ...set the position to the far right side
            carouselPosition = -maxSlide;
        }
        // otherwise, if our new location * the distance we want to go is less than or equal to 0...
        else if( Math.abs(currentIndex*normalSlide) <= 0 ) {
            // ...set the position to zero
            carouselPosition = 0;
        }
        // otherwise...
        else {
            // ...set the position to the normal sliding distance times the number of spots to go
            carouselPosition += normalSlide * slideDistance;
        }
        
        if( transition == 'fade' ) {
            $("#"+root+" .horses li.horse:not(:eq("+slideTo+"))").fadeOut(500);
            $("#"+root+" .horses li.horse:eq("+slideTo+")").fadeIn(500);
        } else {
            $("#"+root+" .horses").animate({ left: carouselPosition+"px" }, 500 );
        }
        
        if(currentIndex == pagesOfHorses-1) {
            $("#"+root).addClass('carousel-next-disabled');
            $("#"+root+" .carousel-interact .controls .next").addClass('next-disabled');
        }
        else {
            $("#"+root).removeClass('carousel-next-disabled');
            $("#"+root+" .carousel-interact .controls .next").removeClass('next-disabled');
        }
        
        if(currentIndex == 0) {
            $("#"+root).addClass('carousel-prev-disabled');
            $("#"+root+" .carousel-interact .controls .prev").addClass('prev-disabled');
        }
        else {
            $("#"+root).removeClass('carousel-prev-disabled');
            $("#"+root+" .carousel-interact .controls .prev").removeClass('prev-disabled');
        }
    }
    
    function startAutoPlay() {
        horseInterval = setInterval(autoSlide,timer); //timer set in options
        autoplaystate = "playing";
        $("#"+root+" .controls .playpause a").removeClass().addClass(autoplaystate).text("playing").attr("title","Pause show");
    }
    function stopAutoPlay() {
        clearInterval(horseInterval);
        autoplaystate = "paused";
        $("#"+root+" .controls .playpause a").removeClass().addClass(autoplaystate).text("paused").attr("title","Play show");
    }
    function restartAutoPlay() {
        if( autoplay == true ) {
            clearInterval(horseInterval);
            horseInterval = setInterval(autoSlide,timer); //timer set in options
        }
    }
    // function pauseAutoPlay() {
    //     clearInterval(horseInterval);
    // }
    // function unpauseAutoPlay() {
    //     if( autoplaystate == "playing" ) {
    //         horseInterval = setInterval(autoSlide,timer); //timer set in options
    //     }
    // }
    
    
    // this is the new function that loads the entire carousel of horses immediately
    // see next function for more info
    function loadLazyData() {
        var lazyURL = $(trigger).attr("href").replace(/[#]\S+/g,''); // the replace removes #anchors on the URL
        
        // do the Ajax call to retrieve more items
        $.ajax({
          url: lazyURL,
          dataType: "html",
          success: function(data) {
              
              // turn the string returned into an array of objects
              var horses = $(data).filter(function(){ return $(this).is('li.horse') });
              for( i = 0; i < horseCount; i++ ) {
                  $("#"+root+" li.horse:eq("+i+")").replaceWith(horses[i]);
              }
              
              // process the tooltips, etc. on the new horses
              processToolTips("#"+root+" ul.horses");
              processNewWindowLinks("#"+root+" ul.horses");
              processModals("#"+root+" ul.horses");
          }
        });
    }
    
    // this is the old function that loads horses for JUST the visible window. Appears to be a bit buggy
    function loadLazyDataIncremental() {
        var fillStart = currentIndex * horsesPerPage; // e.g. 2 * 5 (third page * five horses per page) = 10
        var fillEnd = fillStart + horsesPerPage; // e.g. 10 + 5 (fillStart + five horses per page) = 15
        var lazyURL = $(trigger).attr("href").replace(/[#]\S+/g,''); // the replace removes #anchors on the URL
        
        // do the Ajax call to retrieve more items
        $.get(lazyURL, function(data) {
            
            // splits the returned HTML into an array
            var returned = data.split("<li class=\"ajaxremove\"></li>");
            
            // we're replacing [horsesPerPage] items
            for( i = 0; i < horsesPerPage; i++ ) {
                
                // check to see if the horse we want to replace has the placeholder class on it
                if( $("#"+root+" .horses li.horse:eq("+(fillStart+i)+")").hasClass("lazyplaceholder") == true ) {
                    
                    // replace the placeholder horse with the retrieved horse
                    $("#"+root+" .horses li.horse:eq("+(fillStart+i)+")").replaceWith(returned[fillStart+i]);
                    
                    // process the tooltips, etc. on the new horses
                    processToolTips("#"+root+" .horses li.horse:eq("+(fillStart+i)+")");
                    processNewWindowLinks("#"+root+" .horses li.horse:eq("+(fillStart+i)+")");
                    processModals("#"+root+" .horses li.horse:eq("+(fillStart+i)+")");
                }
            }
        });
    }

    /* ----- <<<< general functions ----- */


    /* ----- interactivity >>>> ----- */
    
    // hover the entire carousel
    // $("#"+root).hover( function() {
    //     pauseAutoPlay();
    // }, function() {    
    //     unpauseAutoPlay();
    // });

    // click on the next button
    $("#"+root+" .controls .next a").click(function(e) {
        e.preventDefault();
        if(currentIndex != pagesOfHorses-1) {
            stepSlide("next");
            ( addindexes == true ) ? stopAutoPlay() : restartAutoPlay() ;
        }
    });

    // click on the prev button
    $("#"+root+" .controls .prev a").click(function(e) {
        e.preventDefault();
        if( currentIndex != 0 ) {
            stepSlide("prev");
            ( addindexes == true ) ? stopAutoPlay() : restartAutoPlay() ;
        }
    });

    // click on the play/pause button
    $("#"+root+" .controls .playpause a").click(function(e) {
        e.preventDefault();
        if(autoplaystate == "paused") {
            startAutoPlay();
        } else {
            stopAutoPlay();
        }      
    });

    // hover an index button
    $("#"+root+" .indexes .index").hover(function() {
        var thisPage = $("#"+root+" .indexes .index").index(this);
        $("#"+root+" .titles .title").addClass("hoverhidden");
        $("#"+root+" .titles .title:eq("+thisPage+")").removeClass("hoverhidden").addClass("hovered");
    },
    function(){
        $("#"+root+" .titles .title").hide().removeClass("hovered").removeClass("hoverhidden");
    });

    // click on an index button
    $("#"+root+" .indexes .index a").click(function(e) {
        e.preventDefault();
        var thisPage = $("#"+root+" .indexes .index a").index(this);
        manuSlide(thisPage);
        // if we're loading additional horses via ajax...
        // if( lazypages == true ) { trigger = this; loadLazyDataIncremental(); }
        stopAutoPlay();
    });

    // swipe support
    $("#"+root).swipe({
        swipeLeft: swipeCarousel,
        swipeRight: swipeCarousel,
        allowPageScroll:"vertical"
    });
    function swipeCarousel(event,direction) {
        if( direction == 'left' ) {
            $("#"+root+" .controls .next a").click();
        }
        else {
            $("#"+root+" .controls .prev a").click();
        }
    }

    /* ----- <<<< interactive buttons ----- */
}


function initMyGoal(index) {
    // index is an ID
    var root = index;
    progressBar("#"+root+" #the-progress .progressbar",1000);
}


function initPoll(index) {
    // index is an ID
    var root = index;
    var actionURL = $("#"+root+" form.pollform").attr("action");
    
    $("#"+root+" form.pollform").submit( function() {
        $.post(actionURL, function(data) {
            $("#"+root+" form.pollform").replaceWith(data);
            
            $("#"+root+" div.pollresults li").each( function(i) {
                var scope = "#"+root+" div.pollresults li:eq("+i+")";
                progressBar(scope,0);
            });
        });
        return false;
    });
     
}


function initDynamicToppers(index,options) {
    // index is an ID

    var options  =  (typeof options != 'undefined')          ?  options          :  new Array();
    var next     =  (typeof options['next'] != 'undefined')  ?  options['next']  :  null;
    var prev     =  (typeof options['prev'] != 'undefined')  ?  options['prev']  :  null;
    var jump     =  (typeof options['jump'] != 'undefined')  ?  options['jump']  :  null;
    
    /* Options
    index == ID of the module with the dynamic topper [string]
    next == jQuery selector string for a link that increments the topper [string] (optional)
    prev == jQuery selector string for a link that decrements the topper [string] (optional)
    jump == jQuery selector string for an array of list items that jump the topper to a specific index [string] (optional)
    */
    
    var root = index;
    var currentIndex = 0;
    var oldIndex;
    
    // create an array of the topper values
    var toppers = new Array();
    $("#"+root+" ol.dynamictopper li").each( function(i) {
        toppers[i] = $(this).text();
    });

    // click on the next button
    $("#"+root+" "+next).click(function(e) {
        e.preventDefault();
        currentIndex = stepToppers(root,currentIndex,"next");
    });

    // click on the prev button
    $("#"+root+" "+prev).click(function(e) {
        e.preventDefault();
        currentIndex = stepToppers(root,currentIndex,"prev");
    });

    // click on an index button
    $("#"+root+" "+jump).click(function(e) {
        e.preventDefault();
        var thisIndex = $("#"+root+" "+jump).index(this);
        setTopper(root,toppers,thisIndex);
        currentIndex = thisIndex;
    });
    
    // declare the module to be initialized so other functions can interact with the toppers
    $("#"+root).addClass("dynamictopperinitialized");
}
// we need this function to be global so it can be accessed from other functions
// step through the toppers
function stepToppers(root,currentIndex,direction) {
    // root is an ID
    
    // create an array of the topper values
    var toppers = new Array();
    $("#"+root+" ol.dynamictopper li").each( function(i) {
        toppers[i] = $(this).text();
    });
    
    var numberOfToppers = toppers.length;
    var oldIndex = currentIndex;

    // check to see which direction we're moving
    switch(direction) {

        // move forward
        case 'next':
            currentIndex = (oldIndex + 1) % numberOfToppers;
            break;

        // move backward
        case 'prev':

            // if the departing index is not the first one
            if( oldIndex > 0 ) {
                currentIndex = (oldIndex - 1) % numberOfToppers;
            }

            // if the departing index is the first one
            else if( oldIndex == 0 ) {
                currentIndex = numberOfToppers - 1;
            }
    
            break;
    
    } // /switch
    
    setTopper(root,toppers,currentIndex);
    return currentIndex;
}
// we need this function to be global so it can be accessed from other functions
// set the topper
function setTopper(root,toppers,value) {
    // index is an ID
    $("#"+root+" span.dynamictopper").text( toppers[value] );
}


function progressBar(scope, timeout) {
    // scope is a jQuery string
    // timeout in milliseconds
    
    var fullwidth = $(scope).width();
    var percent = new Number( $(scope+" span.value").text().replace(/%/,'') );
    var progress = Math.floor( Math.min( percent , 100) * fullwidth / 100 );
    var valuewidth = $(scope+" p.progress span").innerWidth();
    
    if( valuewidth > progress ) {
        $(scope+" p.progress span").addClass("little").css({ "margin-right" : -valuewidth });
    }
    
    $(scope+" p.progress").css({ width : 0 });
    
    function setBar() {
        $(scope+" p.progress").animate({ width : progress }, 1000).queue( function() {
            if( percent == 100 ) {
                $(scope).addClass("success");
            }
        });
    }
    
    setTimeout(setBar, timeout);
}


// this is an attempt to fix ireport tables
function processIReport() {
    // only if the table exists
    if( $("#ireport-table").length > 0 ) {
        
        // assign the crud-table class to the whole thing and remove any inline styling
        $("#ireport-table table").addClass("crud-table").removeAttr("style");
        // look for a width assigned as an attribute
        // if it exists, set the width to auto and allow the attribute to control things
        $("#ireport-table table[width]").css("width","auto");
        // find the header cells. They all have a background color
        // assign the proper classes, remove the inline styles, and then put the proper class on the table row
        $("#ireport-table td[style*=background],#ireport-table td[style*=BACKGROUND]")
            .addClass("crud-table-header-row").removeAttr("style")
            .parent().addClass("crud-table-header-row");
        // find all the sortable headers based on the sortReport javascript in the link
        // add the proper class and then tack on a unique ID gleened from that sortReport link to be used further down
        $("#ireport-table a[href*=sortReport]").each( function() {
            var thisID = $(this).attr("href").replace(/javascript:sortReport\('/,'').replace(/'\);/,'');
            $(this).parent().addClass("row-sortable").attr("id", "ir"+thisID);
        });
        // find all the table cells that are NOT in the header
        // check to see if they have bolded text
        // those with bolded text receive the bold class, the others get the regular one
        $("#ireport-table td:not(.crud-table-header-row)").each(function() {
            if( $(this).css("font-weight") == "bold" || $(this).find("span").css("font-weight") == "bold" )  {
                $(this).addClass("crud-content-bold")
            }
            else {
                $(this).addClass("crud-content");
            }
        });
        // remove all spacer gifs and their parent cells
        $("#ireport-table img[src*=spacer],#ireport-table img[src*='image=px']").parent().remove();
        // find all the newly empty table rows and remove them
        $("#ireport-table tr").each(function() {
            if( $(this).find("td").length == 0 ) {
                $(this).remove();
            }
        });
        // find the first non-header cell and then run a function on each of its siblings
        // check to see if they have right-aligned text
        // assign an additional right-align class if so
        $("#ireport-table td.crud-content:eq(0)").parent().find("td").each(function(i) {
            if( $(this).css("text-align") == "right" ) {
                $("#ireport-table td.crud-table-header-row:eq("+i+")").addClass("right-align");
            }
        });
        // find all span tags and remove the style attribute
        $("#ireport-table span").removeAttr("style");
        //$("#ireport-table td").removeAttr("colspan").removeAttr("rowspan");
        // use the GUP to pull the sorted column out of the query string
        // also use the GUP to pull the sorting direction out of the query string
        var sortedBy = gup("sortColumnName");
        var sortedDir = ( gup("inverseOrder") == "true" ) ? "row-sorted-asc" : "row-sorted-des";
        // find the column header that matches the query string sort order (the aforementioned unique ID above)
        // apply the proper sorted by class
        $("#ir"+sortedBy).addClass(sortedDir);
        
    }
}


// This function sorts things properly by alphanumeric
function alphanum(a, b) {
	function chunkify(t) {
		var tz = [], x = 0, y = -1, n = 0, i, j;
		
		while (i = (j = t.charAt(x++)).charCodeAt(0)) {
			var m = (i == 46 || (i >=48 && i <= 57));
			
			if (m !== n) {
				tz[++y] = "";
				n = m;
			}
			
			tz[y] += j;
		}
		
		return tz;
	}

	var aa = chunkify(a);
	var bb = chunkify(b);

	for (x = 0; aa[x] && bb[x]; x++) {
		if (aa[x] !== bb[x]) {
			var c = Number(aa[x]), d = Number(bb[x]);
			
			if (c == aa[x] && d == bb[x]) {
				return c - d;
			}
			else {
				return (aa[x] > bb[x]) ? 1 : -1;
			}
		}
	}
	
	return aa.length - bb.length;
}


// These two functions are to create the "yellow fade" background effect for items changed via Ajax or other DOM manipulation. They were dug up here: http://stackoverflow.com/questions/848797/yellow-fade-effect-with-jquery/975130#975130
function easeInOut(minValue,maxValue,totalSteps,actualStep,powr) {
    var delta = maxValue - minValue;
    var stepp = minValue+(Math.pow(((1 / totalSteps)*actualStep),powr)*delta);
    return Math.ceil(stepp)
}
function doBGFade(elem,startRGB,endRGB,finalColor,steps,intervals,powr) {
    if (elem.bgFadeInt) window.clearInterval(elem.bgFadeInt);
    var actStep = 0;
    elem.bgFadeInt = window.setInterval(
        function() {
            elem.css("backgroundColor", "rgb("+
                    easeInOut(startRGB[0],endRGB[0],steps,actStep,powr)+","+
                    easeInOut(startRGB[1],endRGB[1],steps,actStep,powr)+","+
                    easeInOut(startRGB[2],endRGB[2],steps,actStep,powr)+")"
            );
            actStep++;
            if (actStep > steps) {
            elem.css("backgroundColor", finalColor);
            window.clearInterval(elem.bgFadeInt);
            }
        }
        ,intervals)
}


// using Dave's favorite function of all time!
if(typeof gup == 'undefined') {
    function gup( name ) {
        name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");  
        var regexS = "[\\?&]"+name+"=([^&#]*)";  
        var regex = new RegExp( regexS );  
        var results = regex.exec( window.location.href );  
    
        if( results == null ) {
            return "";
        } else {
            return results[1];
        }
    }
}
