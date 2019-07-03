const Node = {
	create: function(object) {
		let element = typeof object == 'string' ? document.createElement(object) : object;
		return {
			element: element,
			add: function(node) {
				this.node().appendChild(node.node());
				return this;
			},
			node: function() {
				return this.element;
			},
			clone: function() {
				return this.node().cloneNode(true);  /* Clone whole Object. not only the Element */
			},
			text: function(text) {
				return text ? this.set('innerText', text) : this.get('innerText');
			},
			html: function(html) {
				return html ? this.set('innerHTML', html) : this.get('innerHTML');
			},
			val: function(val) {
				return val ? this.set('value', val) : this.get('value');
			},
			on: function(event, func) {
				this.node().addEventListener(event, func);
			},
			set: function(key, val) {
				this.node()[key] = val;
				return this;
			},
			get: function(key) {
				return this.node()[key];
			},
			byId: function(id) {
				return Node.byElement(document.getElementById(id));
			},
			byClass: function(className) {
				return Array.from(this.node().getElementsByClassName(className)).map(element => Node.byElement(element));
			},
			byTag: function(tagName) {
				return Array.from(this.node().getElementsByTagName(tagName)).map(element => Node.byElement(element));
			},
			src: function(src) {
				return src ? this.set('src', src) : this.get('src');
			},
			css: function(css) {
				return css ? this.set('style', css) : this.get('style');
			},
			id: function(id) {
				return id ? this.set('id', id) : this.get('id');
			},
			fadeIn: function() {
				this.node().style.display = 'block';
			},
			fadeOut: function() {
				this.node().style.display = null;
			}
		};
	},
	byElement: function(element) {
		return this.create(element);
	}
}

const $ = function(ref) {
	if(typeof ref == 'string') {
		if(ref.startsWith('#')) {
			return $(document).byId(ref.substring(1, ref.length));
		}
		else if(ref.startsWith('.')) {
			return $(document).byClass(ref.substring(1, ref.length));
		}
		else {
			return $(document).byTag(ref);
		}
	}
	else {
		return Node.byElement(ref);
	}
}

const Components = {};

const Component = {
	create: function(template) {
		let code = template.html();
		Components[template.id().toUpperCase()] = {
			get: function(args) {
				let html = code;
				if(args) {
					args.each(function(key, val) {
						html = html.replace(RegExp('\\$\\{' + key + '\\}', 'g'), val);
					});
					return html;
				}
			}
		}
	}
}

Object.defineProperty(Object.prototype, 'each', {
	  writable: false,
	  enumerable: false,
	  configurable: false,
	  value: function(func) {
		  if(typeof this == 'array') {
			  for(let i = 0; i < this.length; i++) func ? func(i, this[i]) : console.log('[' + i + ']: ' + this[i]);
		  }
		  if(typeof this == 'object') {
			  for(let i in this) func ? func(i, this[i]) : console.log('[' + i + ']: ' + this[i]);
		  }
	  }
});