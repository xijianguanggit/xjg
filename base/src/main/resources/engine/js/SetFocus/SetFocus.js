//SetFocus 判断逻辑
var ${name}=function (){
	${logBegin}
	if(eval("${In}")){
		debugger;
		$("#${In}").textbox({
            inputEvents: $.extend({}, $.fn.textbox.defaults.inputEvents, {
             keypress: function (e) {
	             if (e.keyCode == 13) {
	            	 ${ifyes}
	             }else{
	            	 ${ifno}
	             }
             }
         })
         });
	}else{
		${ifno}
	}
 }
