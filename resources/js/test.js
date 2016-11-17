function Foo()		{
	var tt = document.createElement('div'),
	  leftOffset = -(~~$('html').css('padding-left').replace('px', '') + ~~$('body').css('margin-left').replace('px', '')),
	  topOffset = -32;
	tt.className = 'ex-tooltip';
	document.body.appendChild(tt);
	
	jQuery.ajax({
		url:'test/test/',
		type:'post',
		data:{"name":"123"},
		dataType:'json',
		async: false,
		success:function(backJsonData){
			data = backJsonData;
		},
		error:function(err){
			alert('error = ' + err);
		}
	});
	
	var opts = {
			  "dataFormatX": function (x) { return d3.time.format('%Y-%m-%d').parse(x); },
			  "tickFormatX": function (x) { return d3.time.format('%j')(x); },
			  "tickHintX": "16",
			  "mouseover": function (d, i) {
				var pos = $(this).offset();
				$(tt).text(d3.time.format('%A')(d.x) + ': ' + d.y)
				  .css({top: topOffset + pos.top, left: pos.left + leftOffset})
				  .show();
			  },
			  "mouseout": function (x) {
				$(tt).hide();
			  }
			};
	alert("1");
	var myChart = new xChart('line-dotted', data, '#myChart', opts);
	alert("2");
}

$(function(){
	jQuery("#h2t").click(function() {
		   alert("123");
	});
});

jQuery(function(){
	jQuery("#cmdList h5").click(function() {
		   alert("123");
	});
});

		