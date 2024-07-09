$(document).ready(function() {
	$(window).scroll(function() {
		if ($(this).scrollTop() > 500) {
			$(".header").addClass("active");
		} else {
			$(".header").removeClass("active");
		}
	});
});

