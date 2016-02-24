$(document).ready(function(){
	setFooter ();
	// 按下转换调式按钮
	$('#start').click(function() {
		getMode();
		translateText();
		editAfterareaHeight ();
		setFooter ();
		// reset ();
		if ($(window).width()<=1000) {
			$("html,body").animate({scrollTop: $("#aftertext").offset().top - 10}, 400);
		}; //当窗口宽度小于1000时自动滚动到转换后的textarea位置
	});
	// 当调式选择有变化时获取调式
	$('.selectarea').change(function() {
		getMode();
	});
	// 对转换前textarea添加效果
	$('#beforetext').focus(function() {
		$('#textarea1').addClass('focus');
	});
	$('#beforetext').blur(function() {
		$('#textarea1').removeClass('focus');
	});
	// 当输入文字时对textarea高度和页脚位置的调整
	$('#beforetext').bind('input propertychange', function(){
		editBeforeareaHeight ();
		setFooter();
	});
	// 按下清除
	$('#cleanall').click(function() {
		$('#beforetext').val("");
		editBeforeareaHeight ();
		translateText ();
		editAfterareaHeight ();
		// reset ();
		setFooter ();
	});
	// 按下全选
	$('#selectall').click(function() {
		$('#aftertext').focus().select();
	});
	// 当窗口大小变化时2调整页脚
	$(window).bind('resize', function() {
		setFooter();
	});
	 // 当滚动条滚动时限制隐藏返回顶部按钮
	$(window).scroll(function(){
		if ($(window).scrollTop()>200){
			$("#totop").fadeIn(150);
		}else{
			$("#totop").fadeOut(150);
		};
	});
	// 返回顶部按钮按下后返回顶部
	$('#totop').click(function() {
		 $('body,html').animate({scrollTop:0},200);
	});
	console.log("制作人：%cIwYvI","color:blue");
	console.log("制作时间：%c2015年4月","color:green");
});
var beforemodeval = 1,
	aftermodeval = 1,
	transmode = 0,
	upcount = 0,
	islower = 0,
	ishigher = 0,
	tonelevel= 0,
	index = {"1":0,"#1":1,"2":2,"#2":3,"3":4,"4":5,"#4":6,"5":7,"#5":8,"6":9,"#6":10,"7":11};
function getWidth () {return $('body').width();};
// 获取body宽度
function setFooter () {
	var windowheight = $(window).height();
	var documentheight = $('html').height();
	if(documentheight>(windowheight+1)){
		$('#blank').css('height', '1px');
		$('footer').css('position', 'static');
		return 0;
	};
	if (documentheight<=(windowheight+1)) {
		$('#blank').css('height', '160px');
		$('footer').css('position', 'fixed');
		return 0;
	};
};
// 调整页脚的位置
function editAfterareaHeight () {
	$('#aftertext').css('height', 'auto');
	$('#aftertext').css('height', ($('#aftertext').prop('scrollHeight')- 33 +'px'));
};
// 修改转换后的textarea长度
function editBeforeareaHeight () {
	$('#beforetext').css('height', 'auto');
	$('#beforetext').css('height', ($('#beforetext').prop('scrollHeight')- 33 +'px'));
	var textareaval = $('#beforetext').val();
	if (textareaval!="") {
		$('#cleanall').show('fast');
	}else{
		$('#cleanall').hide('fast');
	};
};

// 修改转换前的textarea长度
function translateText () {
	var textareaval = getTextareaval ();

	var finaltext = Change(textareaval,transmode,false);

	$('#aftertext').val(finaltext);
	if (textareaval!="") {
		$('#selectall').show('fast');
	}else{
		$('#selectall').hide('fast');
	};
};


function getTextareaval () {
	var textareaval = $('#beforetext').val();
	return textareaval;
};
// 获取输入内容


// 修改内容
function getMode () {
	beforemodeval = $('#beforemode').val();
	aftermodeval = $('#aftermode').val();
	getTransmode();
}
// 获取调式

function getTransmode () {
	// var tempmode = Math.abs(aftermodeval - beforemodeval);
	// var n = 0;
	// if(tempmode >=7){
	// 	tempmode = 12 - tempmode;
	// 	n = 1;
	// };
	// if (aftermodeval>=beforemodeval) {
	// 	transmode = tempmode;
	// 	if (n) {transmode = -transmode};
	// }else{
	// 	transmode = -tempmode;
	// 	if (n) {transmode = -transmode};
	// };
	transmode = beforemodeval - aftermodeval;
};
