function getBaseUrl()	{	
	var path = window.location.href;
	var base_url = path.substring(0, path.lastIndexOf("CarKeeperServer")+"CarKeeperServer".length);
	return base_url;
}

$(function(){
	jQuery("#login_submit").click(function(e) {
	    var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

		jQuery.ajax({
			url:'./login/',
			type:'post',
			data:$("#login_form").serialize(),
			dataType:'html',
			async: false,
			beforeSend: function(request) {
                request.setRequestHeader(header, token);
            },
			success:function(backData)	{
			    alert(backData);
				if(backData == "wrong username or password")		{
					alert(backData);
				}
				else if(backData == "success")		{
					var baseUrl = getBaseUrl();
					window.location = baseUrl+"/pdd/vin/";
				}
			},
			error:function(err){
				alert('login error = ' + err);
			}
		});
		e.preventDefault();
	});
});	

//$(function(){
//	jQuery("#login_test").click(function() {
//		var data = "\r\nprivateToken=\"12345654321_123\"";
//		var regexp = /privateToken=\"(\*?\w+)\"/g;
//		var match = regexp.exec(data);
//		alert(match[1]);
//	});
//});	
//
