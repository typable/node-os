const origin = {lat: 48.510680, lng: 8.652559};

let map;

$(window).on('load', function() {
	// TODO
});

function initMap() {
	map = new google.maps.Map($('#map').node(), {
		center: origin,
		zoom: 13,
		disableDefaultUI: true,
		draggableCursor: 'pointer',
		fullscreenControl: true,
		mapTypeId: 'roadmap',
		mapTypeControl: true,
		mapTypeControlOptions: {
			style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
			mapTypeIds: ['roadmap', 'hybrid']
		}
	});
	map.addListener('resize', e => {
		map.set('gestureHandling', 'all');
	});
	new google.maps.Marker({
		position: origin,
		map: map
	});
}