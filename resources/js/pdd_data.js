//-------请求数据并显示pddData图表-------
function GetDataNShowChart(route_id, cmd, start_time, end_time)		{
	var tt = document.createElement('div'),
	  leftOffset = -(~~$('html').css('padding-left').replace('px', '') + ~~$('body').css('margin-left').replace('px', '')),
	  topOffset = -32;
	tt.className = 'ex-tooltip';
	document.body.appendChild(tt);
	
	var post_data = {
			'route_id':			route_id,
			'cmd':			cmd,
			'start_time':	start_time,
			'end_time':	end_time
	};

	jQuery.ajax({
		url:				'/CarKeeperServer/pdd/getPddData/',
		type:			'post',
		data:				post_data,
		dataType:		'json',
		async: 			false,
		success:function(backJsonData){
			data = backJsonData;
		},
		error:function(err){
			alert('error = ' + err);
		}
	});
	
	var opts = {
		  "tickHintX": "2",
		  "mouseover": function (d, i) {
			var pos = $(this).offset();
			$(tt).text(d.x + ': ' + d.y)
			  .css({top: topOffset + pos.top, left: pos.left + leftOffset})
			  .show();
		  },
		  "mouseout": function (x) {
			$(tt).hide();
		  }
	};
	$('#myChart').empty();
	new xChart('line-dotted', data, '#myChart', opts);
}

//-------click event on cmd list item-------
var current_pdd_data_obj;
jQuery(function(){
	jQuery("#cmdList h5 a").click(function() {
		//change color to notify which is currently shown in page.
		if(current_pdd_data_obj)		{
			$(current_pdd_data_obj).css("color", "#000");
		}
		current_pdd_data_obj = this;
		$(this).css("color", "#999");
		
		//get data from server and show in chart
		var route_id = $("#pdd_data_route_id").val();
		var cmd = this.title;
		var start_time = $("#route_start_time").val();
		var end_time = $("#route_end_time").val();

		GetDataNShowChart(route_id, cmd, start_time, end_time);
	});
});

//-------开始时间，结束时间输入框失去焦点事件-------
jQuery(function(){
	$("#route_start_time").focusout(function() {
		var start_time = $("#route_start_time").val();
		if(!/^\d+$/.test(start_time))		{
			$("#route_start_time").val("0");
		}
	});
	
	$("#route_end_time").focusout(function() {
		var end_time = $("#route_end_time").val();
		if(!/^\d+$/.test(end_time))		{
			$("#route_end_time").val("0");
		}		
	});
});


//-------定位图表悬浮窗位置-------
jQuery(function(){	
	var position = $("#cmdList").position();
	var height = $("#cmdList").height();
	var width = $("#cmdList").width();
	
	$("#chart").css({top:position.top+8, left:position.left+width+4});
});

$(window).resize(function() {
	var position = $("#cmdList").position();
	var height = $("#cmdList").height();
	var width = $("#cmdList").width();
	
	$("#chart").css({top:position.top+8, left:position.left+width+4});
});

