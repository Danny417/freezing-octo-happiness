var xmlHttpReq = null;
var swiper = new Swiper('.swiper-container', {
	slidesPerView:2,
	loop: true,
	//3D Flow:
	tdFlow: {
		rotate : 50,
		stretch :0,
		depth: 100,
		modifier : 1,
		shadows : true
	}
});
var lat, lng, acc, searchStr;
$(window).resize( function() {
	$(".swiper-slide, .swiper-wrapper").css('height', window.innerHeight*0.5);
});
function GetLocation(location) {
    lat = location.coords.latitude;
    lng = location.coords.longitude;
    acc = location.coords.accuracy;
	$('[id = "quickSearch"]').attr('href', '/SearchController?num=1&lat0='+lat+'&lng0='+lng+'&acc0='+acc);
}
$(document).ready(function() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(GetLocation);
    } else{alert("Geolocation is not supported by this browser.");}	
});