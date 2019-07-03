class Viewer {
	constructor(wrapper) {
		this.wrapper = wrapper;
		/** open() **/
		$('.img-box').each(function(key, val) {
			let img = val;
			img.byTag('img')[0].on('load', function(e) {
				img.byTag('img')[0].css('opacity: 1;');
				img.on('click', function(e) {
					let box = wrapper.byClass('img-view')[0].byClass('img-box-view')[0];
					box.html(img.html());
					wrapper.css('display: flex;');
				});
			});
		});
		/** close() **/
		this.wrapper.on('click', function(e) {
			let node = e.srcElement;
			if(node == wrapper.node() || node == wrapper.byClass('img-view')[0].node()) {
				wrapper.css('display: none;');
			}
		});
	}
}
