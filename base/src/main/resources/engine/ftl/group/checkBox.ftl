<div class="input-group-inline" >
<label style="margin-left :3px;
<#if g['required']&&EditMode != 'Readonly'&&g['edit'] > color:red;font-weight:bold;</#if>
	<#if g.height gt 1 >vertical-align:top;</#if>">${g.title}</label> 
<input type="checkbox" id="${panelObj.name}_${g.name}" name="${g.name}" >
<script>
$(function(){
$("#${panelObj.name}_${g.name}").click(function() {
	try{ 
		eval("${g.jsEvent}()");
	}catch(e){
		console.log(e.message);
	} 
});
})
</script>
</div> 

