


$.when({a:"1"},function(){
	return 2;
},{b:3})
  .done(function(a,b,c){
	  // 返回的值也是以数组的形式形成的
	  var i=10/0;
//	 throw new Error('sdfsdf');
	  console.log(arguments)
  }).always(function(){
	  console.log("sdfsd");
  });