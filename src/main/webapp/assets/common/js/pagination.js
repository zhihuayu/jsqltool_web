/**
 * 
 * 分页插件
 */
(function($) {
	
	function MyPage($dom,opts){
		var me=this;
		 me.dom=$dom;
		
		var defaults={
				pageNumber:1,
				pageSize:100,
				pageList: [50,100,150,200],
				onSelectPage:function(pageNumber, pageSize){
					 me.dom.pagination('loading');
					alert('pageNumber:'+pageNumber+',pageSize:'+pageSize);
					 me.dom.pagination('loaded');
				}

		 }
		
		
		function init(){
			
		}
		
		
	}
	
	

})(jQuery)