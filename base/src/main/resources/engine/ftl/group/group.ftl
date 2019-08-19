<#-- 中fram包含的group -->
<#list panelObj.controlList as g>
	<#if "${g.type}" == "TextBox" || "${g.type}" == "DateBox" || "${g.type}" == "TimeBox" || "${g.type}" == "DateTimeBox" || "${g.type}" == "PopupBox" || "${g.type}" == "PasswordBox">
		<#include "group/input.ftl"><#if "${g.column}" !=""><br></#if>
	</#if>
	<#if "${g['type']}" == "Button">
	<#include "group/button.ftl">
	</#if>
	<#if "${g['type']}" == "CheckBox">
	<#include "group/checkBox.ftl">
	</#if>
	<#if "${g['type']}" == "Label">
	<#include "group/label.ftl">
	<#if "${g.column}" !="">
	<br>
	</#if>
	</#if>
	<#if "${g['type']}" == "ComboBox">
	<#include "group/select.ftl">
		<#if "${g.column}" !="">
		<br>
		</#if>
	</#if>
	<#if "${g['type']}" == "MultipleComboBox">
	<#include "group/multipleSelect.ftl">
		<#if "${g.column}" !="">
		<br>
		</#if>
	</#if>
	<#if "${g['type']}" == "FileBox">
	<#assign fileIdx = fileIdx + 1>
	<#include "group/upload.ftl">
		<#if "${g.column}" !="">
		<br>
		</#if>
	</#if>
	<#if "${g['type']}" == "Hidden">
	<input type="hidden" name="${g.name}" id="${panelObj.name}_${g.name}">
	</#if>
	<#if "${g['type']}" == "KindEditor">
	<#include "group/kindEditor.ftl">
		<#if "${g.column}" !="">
		<br>
		</#if>
	</#if>
	<#if "${g['type']}" == "UEditor">
		<#include "group/ueditor.ftl"><#if "${g.column}" !=""><br></#if>
	</#if>	
	<#if "${g['type']}" == "RadioButton">
		<#include "group/radiobutton.ftl">
	</#if>		
</#list>
