var xmlHttpReq = null;
var swiper = new Swiper('.swiper-container', {
	slidesPerView:3,
	loop: true,
	loopAdditionalSlides:2,
	//3D Flow:
	tdFlow: {
		rotate : 50,
		stretch :0,
		depth: 100,
		modifier : 1,
		shadows : true
	}
});