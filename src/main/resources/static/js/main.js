$(document).ready(function() {
	$(".viSlider").slick({
		dots: true, //네이베이션 사용여부
		arrows: false, //화살표 사용여부
		prevArrow: $(".premium-n"), //이전 화살표
		nextArrow: $(".premium-p"), //다음 화살표
		autoplay: true, //자동넘김
		autoplaySpeed: 3500, //자동넘김 속도
		speed: 1000, // 슬라이드 속도
		infinite: true, // 무한슬라이
		swipe: true, //스와이프
		initialSlide: 0, //슬라이드 시작순서
		slidesToShow: 1, //보여질 슬라이드 갯수
		slidesToScroll: 1, //넘겨질 슬라이드 갯수
		fade: true,
		customPaging: function(slider, i) {
			var target_txt = $(slider.$slides[i]).attr("data-slide-nav");

			var thumb = "<span>" + target_txt + "</span>";

			return thumb;
		},
	});

	$(".gallSlider").slick({
		dots: true, //네이베이션 사용여부
		arrows: false, //화살표 사용여부
		prevArrow: $(".premium-n"), //이전 화살표
		nextArrow: $(".premium-p"), //다음 화살표
		autoplay: true, //자동넘김
		autoplaySpeed: 3500, //자동넘김 속도
		speed: 1000, // 슬라이드 속도
		infinite: true, // 무한슬라이
		swipe: true, //스와이프
		initialSlide: 0, //슬라이드 시작순서
		slidesToShow: 3, //보여질 슬라이드 갯수
		slidesToScroll: 1, //넘겨질 슬라이드 갯수
		// fade: true,
	});
});

function toggleChatContainer() {
	var chatContainer = document.getElementById('chatContainer');
	chatContainer.classList.toggle('hidden');
}


