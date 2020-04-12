function jump(url, data) {
	if (!data) {
		window.location.href = url;
	} else {
		var fd = document.createElement('form');
		fd.method = 'POST';
		fd.setAttribute('type', "hiddent");
		for ( var key in data) {
			var el = document.createElement('input');
			el.setAttribute("name", key);
			el.setAttribute("type", 'hidden');
			fd.appendChild(el);
			el.value = data[key];
		}
		document.body.appendChild(fd);
		fd.action = url;
		fd.submit();
	}
}

function ajaxSubmitDefered(url, async, params) {
	var promise=$.Deferred();
	if (async == undefined || async == null)
		async = true;
	$.ajax({
		cache : false,
		async : async,
		url : url,
		timeout : 30000,
		data : params,
		type : 'post',
		dataType : 'json',
		success : function(data) {
			promise.resolve(data);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			promise.reject(XMLHttpRequest, textStatus, errorThrown);
		}
	});
	return promise.promise();
}


function ajaxSubmit(url, cache, async, params, callback) {
	if (cache == undefined || cache == null)
		cache = false;
	if (async == undefined || async == null)
		async = true;
	var loading = null;
		loading = layer.open({type:3,isOutAnim: false,anim: -1,success:function(layero, index){
			ajax(index);
		}});
	function closeLayer(loading){
		if(loading != null && loading != undefined)
			layer.close(loading);
	}
	function ajax(index){
		$.ajax({
			cache : cache,
			async : async,
			url : url,
			timeout : 30000,
			data : params,
			type : 'post',
			dataType : 'json',
			success : function(data) {
				try{
					if (callback != null && typeof callback == 'function') {
						if (!callback(data)) {
							// return;
						}
					}
				}catch(e){
					closeLayer(index)
					throw e;
				}
				closeLayer(index)
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				closeLayer(index)
			}
		});
	}
	


}

// 日期格式化
function Format(now, mask) {
	if (!now) {
		return;
	}
	var d = new Date(now);
	var zeroize = function(value, length) {
		if (!length)
			length = 2;
		value = String(value);
		for (var i = 0, zeros = ''; i < (length - value.length); i++) {
			zeros += '0';
		}
		return zeros + value;
	};

	return mask
			.replace(
					/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g,
					function($0) {
						switch ($0) {
						case 'd':
							return d.getDate();
						case 'dd':
							return zeroize(d.getDate());
						case 'ddd':
							return [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri',
									'Sat' ][d.getDay()];
						case 'dddd':
							return [ 'Sunday', 'Monday', 'Tuesday',
									'Wednesday', 'Thursday', 'Friday',
									'Saturday' ][d.getDay()];
						case 'M':
							return d.getMonth() + 1;
						case 'MM':
							return zeroize(d.getMonth() + 1);
						case 'MMM':
							return [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
									'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ][d
									.getMonth()];
						case 'MMMM':
							return [ 'January', 'February', 'March', 'April',
									'May', 'June', 'July', 'August',
									'September', 'October', 'November',
									'December' ][d.getMonth()];
						case 'yy':
							return String(d.getFullYear()).substr(2);
						case 'yyyy':
							return d.getFullYear();
						case 'h':
							return d.getHours() % 12 || 12;
						case 'hh':
							return zeroize(d.getHours() % 12 || 12);
						case 'H':
							return d.getHours();
						case 'HH':
							return zeroize(d.getHours());
						case 'm':
							return d.getMinutes();
						case 'mm':
							return zeroize(d.getMinutes());
						case 's':
							return d.getSeconds();
						case 'ss':
							return zeroize(d.getSeconds());
						case 'l':
							return zeroize(d.getMilliseconds(), 3);
						case 'L':
							var m = d.getMilliseconds();
							if (m > 99)
								m = Math.round(m / 10);
							return zeroize(m);
						case 'tt':
							return d.getHours() < 12 ? 'am' : 'pm';
						case 'TT':
							return d.getHours() < 12 ? 'AM' : 'PM';
						case 'Z':
							return d.toUTCString().match(/[A-Z]+$/);
							// Return quoted strings with the surrounding quotes
							// removed
						default:
							return $0.substr(1, $0.length - 2);
						}
					});
}

// yyyy-MM-dd HH:mm:ss
function stringToDate(dateStr) {
	if (!dateStr)
		return;
	dateStr = dateStr.replace(/-/g, '/');
	return new Date(dateStr);
}

// textbox和combobox上增加校验框
function addValidateBox($input){
	$input.prev("input").addClass("validatebox-invalid");
	$input.parents("span").first().addClass("textbox-invalid");
}

function removeValidateBox($input){
	$input.prev("input").removeClass("validatebox-invalid");
	$input.parents("span").first().removeClass("textbox-invalid");
}


