//SetFocus 判断逻辑
var ${name}=function (){
	${logBegin}
	if(eval("${In}")){
		$('#${In}').next('span').find('input').focus();
		${ifyes}
	}else{
		${ifno}
	}
 }



