$(document).ready(function () {
  // 스크롤 이벤트
  function scrollAniEvent() {
    $(".ani, .ani_L, .ani_R, .ani_T, .ani_O").each(function () {
      const windHeight = $(window).innerHeight();
      const scrollTop = $(window).scrollTop();
      const viewportTop = scrollTop + windHeight;

      const elementOffset = $(this).offset().top;
      const elementOffsetBtm = elementOffset + $(this).outerHeight() / 2.8;

      if (viewportTop > elementOffsetBtm) {
        $(this).addClass("aniIn");
        $(this).addClass("aniIn_T");
        $(this).addClass("aniIn_L");
        $(this).addClass("aniIn_R");
        $(this).addClass("aniIn_O");
      } else {
        $(this).removeClass("aniIn");
        $(this).removeClass("aniIn_T");
        $(this).removeClass("aniIn_L");
        $(this).removeClass("aniIn_R");
        $(this).removeClass("aniIn_O");
      }
    });
  }

  scrollAniEvent();

  $(window).on("scroll", function () {
    scrollAniEvent();
    // didScroll = true;
  });
});

// $(document).ready(function () {
//   var didScroll = false;

//   function debounce(func, wait) {
//     var timeout;
//     return function () {
//       var context = this,
//         args = arguments;
//       var later = function () {
//         timeout = null;
//         if (didScroll) {
//           func.apply(context, args);
//           didScroll = false;
//         }
//       };
//       clearTimeout(timeout);
//       timeout = setTimeout(later, wait);
//     };
//   }

//   function scrollAniEvent() {
//     $(".ani, .ani_L, .ani_R, .ani_T, .ani_O").each(function () {
//       const windHeight = $(window).innerHeight();
//       const scrollTop = $(window).scrollTop();
//       const viewportTop = scrollTop + windHeight;

//       const elementOffset = $(this).offset().top;
//       const elementOffsetBtm = elementOffset + $(this).outerHeight() / 2.8;

//       if (viewportTop > elementOffsetBtm) {
//         $(this).addClass("aniIn aniIn_T aniIn_L aniIn_R aniIn_O");
//       } else {
//         $(this).removeClass("aniIn aniIn_T aniIn_L aniIn_R aniIn_O");
//       }
//     });
//   }

//   // Initial call
//   scrollAniEvent();

//   // Debounced scroll event
//   $(window).on(
//     "scroll",
//     debounce(function () {
//       didScroll = true;
//     }, 250)
//   );

//   // Call the scrollAniEvent when scrolling stops
//   setInterval(function () {
//     if (didScroll) {
//       scrollAniEvent();
//       didScroll = false;
//     }
//   }, 100);
// });
