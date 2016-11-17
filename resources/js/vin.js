//保存vin detail
jQuery(function(){
	jQuery("#vin_detail_save").click(function() {
		$("tr.vin_item").each(function(i, tr) {
			$this = $(this);
		    var vin = $this.find("a.vin_detail_vin").html();
		    var car_type = $this.find("input.vin_detail_car_type").val();
		    var miles = $this.find("input.vin_detail_miles").val();
			var post_data = {
					'vin':				vin,
					'car_type':	car_type,
					'miles':			miles
			};

			jQuery.ajax({
				url:				'/CarKeeperServer/pdd/vin/setDetail/',
				type:			'post',
				data:				post_data,
				dataType:		'html',
				async: 			false,
				success:function(backJsonData){
				    alert("vin.js 保存成功!");
				},
				error:function(err){
				    alert("vin.js 错误");
				}
			});
			
		});
        alert("complete");
	});
});