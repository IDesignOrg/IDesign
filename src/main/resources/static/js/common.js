$(document).ready(function() {
	console.log('zz')
	$(window).scroll(function() {
		if ($(this).scrollTop() > 500) {
			$(".header").addClass("active");
		} else {
			$(".header").removeClass("active");
		}
	});
});

