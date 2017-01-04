
//公共页面刷新
function commonRefresh(){
	if(parent.refreshPage){
		parent.refreshPage();
	}else{
		if(contextPath == ""){
			location.href = "/";
		}else{
			location.href = contextPath;
		}
	}
}