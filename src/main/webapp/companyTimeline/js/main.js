
$(document).ready(function() {

  var owl1 = $("#owl-demo1");
  owl1.owlCarousel({
      items : 1, //10 items above 1000px browser width
      itemsDesktop : [1000,1], //5 items between 1000px and 901px
      itemsDesktopSmall : [900,1], // betweem 900px and 601px
      itemsTablet: [600,1], //2 items between 600 and 0
      itemsMobile : false // itemsMobile disabled - inherit from itemsTablet option
  });
  $(".car1 .next").click(function(){owl1.trigger('owl.next');})
  $(".car1 .prev").click(function(){owl1.trigger('owl.prev');})
  $(".car1 .play").click(function(){owl1.trigger('owl.play',1000);})
  $(".car1 .stop").click(function(){owl1.trigger('owl.stop');})

  var owl2 = $("#owl-demo2");
  owl2.owlCarousel({
      items : 1, //10 items above 1000px browser width
      itemsDesktop : [1000,1], //5 items between 1000px and 901px
      itemsDesktopSmall : [900,1], // betweem 900px and 601px
      itemsTablet: [600,1], //2 items between 600 and 0
      itemsMobile : false // itemsMobile disabled - inherit from itemsTablet option
  });
  $(".car2 .next").click(function(){owl2.trigger('owl.next');})
  $(".car2 .prev").click(function(){owl2.trigger('owl.prev');})
  $(".car2 .play").click(function(){owl2.trigger('owl.play',1000);})
  $(".car2 .stop").click(function(){owl2.trigger('owl.stop');})

  var owl3 = $("#owl-demo3");
  owl3.owlCarousel({
      items : 1, //10 items above 1000px browser width
      itemsDesktop : [1000,1], //5 items between 1000px and 901px
      itemsDesktopSmall : [900,1], // betweem 900px and 601px
      itemsTablet: [600,1], //2 items between 600 and 0
      itemsMobile : false // itemsMobile disabled - inherit from itemsTablet option
  });
  $(".car3 .next").click(function(){owl3.trigger('owl.next');})
  $(".car3 .prev").click(function(){owl3.trigger('owl.prev');})
  $(".car3 .play").click(function(){owl3.trigger('owl.play',1000);})
  $(".car3 .stop").click(function(){owl3.trigger('owl.stop');})

  var owl4 = $("#owl-demo4");
  owl4.owlCarousel({
      items : 1, //10 items above 1000px browser width
      itemsDesktop : [1000,1], //5 items between 1000px and 901px
      itemsDesktopSmall : [900,1], // betweem 900px and 601px
      itemsTablet: [600,1], //2 items between 600 and 0
      itemsMobile : false // itemsMobile disabled - inherit from itemsTablet option
  });
  $(".car4 .next").click(function(){owl4.trigger('owl.next');})
  $(".car4 .prev").click(function(){owl4.trigger('owl.prev');})
  $(".car4 .play").click(function(){owl4.trigger('owl.play',1000);})
  $(".car4 .stop").click(function(){owl4.trigger('owl.stop');})

  var owl5 = $("#owl-demo5");
  owl5.owlCarousel({
      items : 1, //10 items above 1000px browser width
      itemsDesktop : [1000,1], //5 items between 1000px and 901px
      itemsDesktopSmall : [900,1], // betweem 900px and 601px
      itemsTablet: [600,1], //2 items between 600 and 0
      itemsMobile : false // itemsMobile disabled - inherit from itemsTablet option
  });
  $(".car5 .next").click(function(){owl5.trigger('owl.next');})
  $(".car5 .prev").click(function(){owl5.trigger('owl.prev');})
  $(".car5 .play").click(function(){owl5.trigger('owl.play',1000);})
  $(".car5 .stop").click(function(){owl5.trigger('owl.stop');})



  //intro parallax
  $(".introblock").waypoint(function(down) {

    var $bgobj = $(this);
    var backgroundPos = $bgobj.css('backgroundPosition').split(" ");
    var xPos = backgroundPos[0],
        yPos = parseFloat(backgroundPos[1]);//value minus "px""

    var $window = $(window);
    var lastScrollTop = 0;

      $(window).scroll(function(event){
         var st = $(this).scrollTop();
         if (st > lastScrollTop){
            //downscroll code
            yPos = yPos - 2;
            // Put together our final background position
            var coords = ' 50% '+ yPos + 'px';
            // Move the background
            $bgobj.css({ backgroundPosition: coords });

         } else {
            // upscroll code
            yPos = yPos + 1;
            // Put together our final background position
            var coords = ' 50% '+ yPos + 'px';
            // Move the background
            $bgobj.css({ backgroundPosition: coords });

         }
         lastScrollTop = st;
      });

  }, { offset: '10%' });
  //end intro parallax

  //carousel parallax
  $('.owl-carousel').each(function(){

    $(this).waypoint(function(down) {

      //fade out the image
      $(this).find(".itemInner").animate({"opacity":.2},1000);

      //fade in the content
      $(this).find(".itemContent").animate({"opacity":1},1000);

      // parallax stuff
          var $bgobj = $(this).find('.itemInner');
          var backgroundPos = $bgobj.css('backgroundPosition').split(" ");
          var xPos = backgroundPos[0],
              yPos = parseFloat(backgroundPos[1]);//value minus "px"

          var $window = $(window);
          var lastScrollTop = 0;

            $(window).scroll(function(event){
               var st = $(this).scrollTop();
               if (st > lastScrollTop){
                  //downscroll code
                  yPos = yPos + 2;
                  // Put together our final background position
                  var coords = ' 50% '+ yPos + 'px';
                  // Move the background
                  $bgobj.css({ backgroundPosition: coords });
               }

               else {
                  // upscroll code
                  yPos = yPos - 2;
                  // Put together our final background position
                  var coords = ' 50% '+ yPos + 'px';
                  // Move the background
                  $bgobj.css({ backgroundPosition: coords });
               }
               lastScrollTop = st;
            });

    }, { offset: '40%' });

  });
  //end carousel parallax

  //map functions

  //show points timed sequentially when map is visible
  $('.mapInner').waypoint(function(down) {
      $(".mapPoint").each(function(index) {
        $(this).delay(500*index).fadeIn(500);
      });
  }, { offset: '70%' });

  //show tooltips on click
  $(".mapPoint").each(function(){
      $(this).on("mouseenter mouseleave touchstart", function(){
        $(this).find(".flyout").fadeToggle();
      });
  });

}); // end doc ready

