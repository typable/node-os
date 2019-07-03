let overlay;

$(window).on('load', function() {
	
	overlay = new Overlay('#overlay');
});

class Overlay {
	constructor(id) {
		this.node = $(id);
		this.count = 0;
	}
	open() {
		if(this.count == 0) {
			this.node.fadeIn();
		}
		this.count++;
	}
	close() {
		this.count--;
		if(this.count == 0) {
			this.node.fadeOut();
		}
	}
}

class Dialog {
	constructor(id) {
		this.node = $(id);
		this.isOpen = false;
	}
	open() {
		overlay.open();
		this.node.fadeIn();
	}
	close() {
		overlay.close();
		this.node.fadeOut();
	}
}

class Viewer {
	constructor(id) {
		this.node = $(id);
		this.isOpen = false;
	}
	open() {
		if(!this.isOpen) {
			overlay.open();
			overlay.node.node().style.cursor = 'pointer';
			this.node.fadeIn();
			this.isOpen = true;
		}
	}
	close() {
		if(this.isOpen) {
			overlay.close();
			overlay.node.node().style.cursor = null;
			this.node.fadeOut();
			this.isOpen = false;
		}
	}
}