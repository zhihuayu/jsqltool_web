/**
 * 主要用于设置datagrid相关公用参数和方法的公用js
 */

function getColumnsSetting(columns,dbType){
			  var  result=[];
			  var winWidth=$(window).width();
			  var columnWidth=winWidth*0.1;
			  if(columns.length<=10){
				  columnWidth=winWidth*(1/columns.length)-10;
			  }
			$.each(columns,function(v,i){
					var c={
					  resizable:true
					}
					c.field=this.alias;
					c.title=this.alias;
					if(dbType){
						if(dbType.indexOf("ORACLE")==-1 || (this.alias != "ROWID" && this.alias != "ROW_ID"))
							c.editor=getEdit(this.typeName);
					}
					c.width=columnWidth;
					c.allData=this;
					result.push(c);
			});
			return result;
}
		
function getColumnsData(records,cls){
			var result=[];
			if(records){
				$.each(records,function(){
					var d={};
					var curD=this;
					$.each(cls,function(v,i){
						d[i.field]=curD.values[v];
					});
					d.allData=curD;
					result.push(d);
				});
			}
			return result;
		}


function getEdit(jdbcType){
	var result={};
	if(jdbcType && (jdbcType+"").toUpperCase()=='DATE'){
		result.type="datebox";
		result.options={
				//配置formatter，只返回年月 之前是这样的d.getFullYear() + '-' +(d.getMonth()); 
			    formatter: function (d) { 
			    	var result=Format(d,"yyyy-MM-dd");
			        return result; 
			    },
			    parser:function(s){
				       return stringToDate(s);
				    },
		};
	}else if(jdbcType &&(jdbcType+"").toUpperCase()=='TIMESTAMP'){
		result.type="datebox";
		result.options={
				//配置formatter，只返回年月 之前是这样的d.getFullYear() + '-' +(d.getMonth()); 
			    formatter: function (d) { 
			        return Format(d,"yyyy-MM-dd HH:mm:ss"); 
			    },
			    parser:function(s){
			       return stringToDate(s);
			    },
			    onClickIcon:function(){
			       //alert("good");
			    },
			    init:function(){
			    	alert("init")
			    }
		};
	}else{
		result.type="text";
		result.options={};
	}
	
	return result;
}