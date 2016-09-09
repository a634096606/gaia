
//公共页面刷新
function commonRefresh(){
	debugger;
	if(parent.refreshPage){
		parent.refreshPage();
	}else{
		location.href = contextPath;
	}
}