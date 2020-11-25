// Title: Tigra Fader
// URL: http://www.softcomplex.com/products/tigra_fader/
// Version: 1.0 (commented source)
// Date: 07/23/2007
// Notes: This script is free. Visit official site for further details.

function tFader (a_items, a_tpl) {

	// validate parameters and set defaults
	if (!a_items) return alert("items structure is missing");
	if (typeof(a_items) != 'object') return alert("format of the items structure is incorrect");
	if (a_items[a_items.length - 1] == null) return alert("last element of the items structure is undefined");
	if (!a_tpl) a_tpl = [];
	for (var i = 0; i < A_TSLIDEDEFS.length; i += 2)
		if (a_tpl[A_TSLIDEDEFS[i]] == null)
			a_tpl[A_TSLIDEDEFS[i]] = A_TSLIDEDEFS[i + 1];

	// save config parameters in the slider object
	this.a_tpl   = a_tpl;
	this.a_items = a_tpl.random ? tslide_randomize(a_items) : a_items;

	// initialize parameters, assign methods	
	this.n_currentSlide = 0;
	this.f_goto  = tslide_goto;
	this.f_run   = function () { this.b_running = 1; this.f_goto(); };
	this.f_stop  = function () { this.b_running = 0; clearTimeout(this.o_timerS); };
	this.f_fadeIn  = tslide_fadeIn;
	this.f_fadeOut = tslide_fadeOut;
	this.f_slideOp = tslide_slideOp;

	// register in the global collection	
	if (!window.A_SLIDES)
		window.A_SLIDES = [];
	this.n_id = window.A_SLIDES.length;
	window.A_SLIDES[this.n_id] = this;

	// generate control's HTML
	var s_attributes = ' '
		+ (a_tpl['css']    ? ' class="'  + a_tpl['css']    + '"' : '')
		+ (a_tpl['width']  ? ' width="'  + a_tpl['width']  + '"' : '')
		+ (a_tpl['height'] ? ' height="' + a_tpl['height'] + '"' : '')
		+ (a_tpl['alt']    ? ' title="'  + a_tpl['alt']    + '" alt="' + a_tpl['alt'] + '"' : '');

	this.a_imgRefs = [];
	document.write ('<img src="', this.a_items[0] , '"', s_attributes, ' name="tslide', this.n_id, '_0" />');
	this.a_imgRefs[0] = document.images['tslide' + this.n_id + '_0'];
	this.n_currentSlide = 0;

	// exit on old browsers
	if (!this.a_imgRefs[0] || !this.a_imgRefs[0].style || this.a_imgRefs[0].style.marginLeft == null)
		return;

	for (var i = 1; i < this.a_items.length; i++) {
		document.write('<img src="', this.a_items[i] , '"', s_attributes, ' name="tslide', this.n_id, '_', i, '" style="position:relative;z-index:-1;filter:alpha(opacity=100);" />');
		this.a_imgRefs[i] = document.images['tslide' + this.n_id + '_' + i];
		this.a_imgRefs[i].style.marginLeft = '-' + this.a_tpl.width + 'px';
		this.f_slideOp(i, 0);
		this.a_imgRefs[i].style.zIndex = i;
	}

	// calculate transition variables
	this.n_timeDec = Math.round(this.a_tpl['transtime'] * 1e3 / this.a_tpl['steps']);
	this.n_opacDec = Math.round(100 / this.a_tpl['steps']);

	// run this sucker
	this.f_run();
}

function tslide_goto (n_slide, b_now) {

	// cancel any scheduled transitions	
	if (this.o_timerS) {
		clearTimeout(this.o_timerS);
		this.o_timerS = null;
		if (this.n_nextSlide) {
			this.f_slideOp(this.n_nextSlide, 0);
			this.n_nextSlide = null;
		}
	}

	// determine the next slide
	this.n_nextSlide = (n_slide == null ? this.n_currentSlide + 1 : n_slide) % this.a_items.length;
	if (this.n_nextSlide == this.n_currentSlide) return;
	
	// schedule transition
	this.o_timerS = setTimeout('A_SLIDES[' + this.n_id + '].f_fade' + (this.n_nextSlide > this.n_currentSlide ? 'In' : 'Out') + '()', (b_now ? 0 : this.a_tpl['slidetime'] * 1e3));
}

function tslide_fadeIn (n_opacity) {
	// new transition
	if (n_opacity == null) {
		n_opacity = 0;
	}
	n_opacity += this.n_opacDec;
	// end of transition
	if (n_opacity > 99) {
		this.f_slideOp(this.n_nextSlide, 99);
		this.f_slideOp(this.n_currentSlide, 0);
		this.n_currentSlide = this.n_nextSlide;
		this.n_nextSlide = null;
		return this.f_run();
	}
	// set transparency
	this.f_slideOp(this.n_nextSlide, n_opacity);

	// cycle
	this.o_timerT = setTimeout('A_SLIDES[' + this.n_id + '].f_fadeIn(' + n_opacity + ')', this.n_timeDec);
}
function tslide_fadeOut (n_opacity) {
	// new transition
	if (n_opacity == null) {
		n_opacity = 99;
		this.f_slideOp(this.n_nextSlide, 99);
	}
	n_opacity -= this.n_opacDec;
	// end of transition
	if (n_opacity < 0) {
		this.f_slideOp(this.n_currentSlide, 0);
		this.n_currentSlide = this.n_nextSlide;
		this.n_nextSlide = null;
		return this.f_run();
	}
	// set transparency
	this.f_slideOp(this.n_currentSlide, n_opacity);

	// cycle
	this.o_timerT = setTimeout('A_SLIDES[' + this.n_id + '].f_fadeOut(' + n_opacity + ')', this.n_timeDec);
}

function tslide_slideOp (n_slide, n_opacity) {
	if (!n_slide) return;
	var e_slide = this.a_imgRefs[n_slide];
	tslide_setOpacity(e_slide, n_opacity);
}

function tslide_randomize (a_source) {
	var n_index,
		a_items = [];
	while (a_source.length) {
		n_index = Math.ceil(Math.random() * a_source.length) - 1;
		a_items[a_items.length] = a_source[n_index];
		a_source[n_index] = a_source[a_source.length - 1];
		a_source.length = a_source.length - 1;
	}
	return a_items;
}

// cross-browser opacity
var s_uaApp  = navigator.userAgent.toLowerCase();
if (s_uaApp.indexOf('opera') != -1 || s_uaApp.indexOf('safari') != -1)
	window.tslide_setOpacity = function (e_element, n_opacity) {
		e_element.style.opacity = n_opacity / 100;
	};
else if (s_uaApp.indexOf('gecko') != -1)
	window.tslide_setOpacity = function (e_element, n_opacity) {
		e_element.style.MozOpacity = n_opacity / 100;
	};
else if (s_uaApp.indexOf('msie') != -1)
	window.tslide_setOpacity = function (e_element, n_opacity) {
		try { e_element.filters.alpha.opacity = n_opacity } catch (e) {};
	};
else
	window.tslide_setOpacity = null;

// defaults
var A_TSLIDEDEFS = [
	'steps', 40,
	'css', '',
	'transtime', 0.5,
	'slidetime', 2
];