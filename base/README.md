[TOC]

git clone http://gitcloud.tedus.cn/wangdanfeng/base-core.git -b develop 
------------

# 1 平台概述

## 1.1 平台简介

应用基础平台具有如下功能：
- 通过编写配置文件或操作可视化界面形式，无需编程，可实现应用软件的界面导航、增删改查、导入导出、打印、报表、工作流、安全管理、消息通知等功能。
- 通过实现平台扩展接口，定制开发Web界面、显示控件、逻辑、函数、插件，可实现个性化功能。

## 1.2 基本概念

todo

## 1.3 平台架构

todo：图

## 1.4 第三方依赖

### 1.4.1 异步邮件服务
#### 1.4.1.1 配置applicationContext.xml:

```
<import resource="redis-context.xml"/>
<!--<context:exclude-filter type="regex" expression="com.tedu.base.common.redis.*"/>-->
```

#### 1.4.1.2 配置redis-context.xml:


```
<redis:listener ref="mailMessageAdapter" method="handleMessage" serializer="jdkSerializer" 
			topic="user:mail_${profiles.active}"/><!--注意topic名称应保持项目唯一-->
```

#### 1.4.1.3 配置redis.properties：
redis服务IP、端口、口令等信息
#### 1.4.1.3 配置redis.properties：

#### 1.4.1.4 配置：
#邮件发送邮箱地址
msg.mail.account=tsoftware@tedu.cn
msg.mail.password=tsoftware.cn
msg.mail.SMTPHost=mail.tedu.cn
msg.mail.smtp.auth=true
mail.smtp.starttls.enable=true
#阿里云服务不支持25端口
mail.smtp.socketFactory.port=465
#不配置或false时为关闭debug，否则可查看到邮件发送详情或失败原因
mail.debuggable=false

#### 1.4.1.5 调用异步发送邮件功能：

```
Email mail = new Email();
String content = "通知";
mail.setSubject(empName+"预约了" +ObjectUtils.toString(newMap.get("checkDate"))+ "的体检");
mail.setAddress(address);
mail.setContent(content);
mail.setReceiveName(ObjectUtils.toString(newMap.get("empName")));
mail.setSentDate(new Date());
try {
		sendMessage.sendMessage(mailTopic, mail);
	} catch (Exception e) {
		FormLogger.logFlow("送入邮件发送队列失败," + e.getMessage(), FormLogger.LOG_TYPE_INFO,e);	
	}
```
#### 1.4.1.6 常见问题：
- redis topic未创建
- 邮件对象未构造完整，缺少必填属性


## 1.5 运行原理

以用户打开编辑界面，输入并保存数据为例，说明平台内部模块之间的运行原理。

```seq
用户->>浏览器:点击页面链接
浏览器->>服务接口:发送页面创建请求
服务接口->>服务接口:访问控制与服务路由
服务接口->>界面生成器:请求生成界面框架
界面生成器-->>浏览器:返回自动生成的界面框架
浏览器->>服务接口:发送页面数据装载请求
服务接口->>服务接口:访问控制与服务路由
服务接口->>服务:调用服务端数据查询逻辑
服务->>数据库:执行SQL语句，查询数据
服务->>规则解析器:调用规则表达式及函数，计算数据
服务-->>浏览器:返回结果数据
浏览器->>浏览器:执行客户端逻辑，填充数据
用户->>浏览器:输入数据
浏览器->>服务接口:发送数据
服务接口->>服务:调用服务端数据保存逻辑
服务->>规则解析器:调用规则表达式及函数，计算数据
服务->>数据库:执行SQL语句，保存数据
服务-->>浏览器:返回保存结果数据
浏览器->>浏览器:执行客户端逻辑，提示信息
```

## 1.6 术语表

todo：表格

# 2 表单定义文件

## 2.1 文件结构

表单定义文件采用XML格式，格式如下。
```XML
<?xml version="1.0" encoding="UTF-8"?>
<tsoftware>
	<model_layer>
		<object name="" table="" primary="" father="" unique="" order="">
			<property name="" type="" length="" validate="" field=""/>
		</object>
	</model_layer>
	<ui_layer>
		<ui name="" title="">
			<panel name="" type="" title="" object="">
				<control name="" type="" title="" property="" edit="" required="" format="" initial="" source="" target="" width="" height="" column=""/>
			</panel>
			<layout>
				<region location ="" scale="">
					<subregion panel="" location ="" scale=""/>
				</region>
			</layout>
			<flow trigger="" event="" filter="">
				<procedure name="" server="" logic="" ifyes="" ifno="" sync="">
					<param name="" value=""/>
				</procedure>
			</flow>
		</ui>
	</ui_layer>
</tsoftware>
```

其中，节点、属性名均为小写字母，属性值按大小写敏感处理。属性值分为如下两种类型。
- 普通类型：字符串，定义时无需使用单引号。
- 表达式类型：具体格式参见【5.3 表达式】。

## 2.2 节点说明

表单定义文件中，主要节点含义如下。

节点名 | 节点含义 | 约束条件 | 参见章节
--- | --- | --- | ---
tsoftware | 表单定义文件的根节点 | 一个文件只允许、必须定义一个
model_layer | 定义数据模型的节点 | 一个文件最多允许定义一个 | 3 数据模型
object | 定义数据模型中的对象 | 允许定义多个 | 3.2 object节点
property | 定义对象中的属性 | 允许定义多个 | 3.3 property节点 
ui_layer | 定义交互界面和主要业务逻辑的节点 | 一个文件最多允许定义一个 | 4 交互界面
ui | 定义交互界面 | 允许定义多个 | 4.2 ui节点
panel | 定义交互界面中的面板 | 允许定义多个 | 4.3 panel节点
control | 定义面板中的交互控件 | 允许定义多个 | 4.4 control节点
layout | 定义交互界面中面板的布局 | 一个界面只允许、必须定义一个 | 4.5 layout节点
region | 定义布局中的区域 | 允许定义多个 | 4.5.2 region节点
subregion | 定义区域中的子区域 | 允许定义多个 | 4.5.3 subregion节点
flow | 定义交互界面中的逻辑流 | 允许定义多个 | 4.6 flow节点
procedure | 定义逻辑流中的执行过程 | 允许定义多个 | 4.6.2 procedure节点
param | 定义执行过程中的输入参数 | 允许定义多个 | 4.6.3 param节点

## 2.3 存储位置

todo

## 2.4 命名规范

### 2.4.1 小驼峰法

使用a-z、A-Z、0-9字符组合成字符串，首字母必须是字母。除第一个单词之外，其他单词首字母大写。例如，empName。

### 2.4.2 大驼峰法

即帕斯卡命名法。使用a-z、A-Z、0-9字符组合成字符串，首字母必须是字母。每一个单词的首字母都采用大写字母。例如，PopupBox。

# 3 表单数据模型

## 3.1 概述

数据模型由多个数据对象（object）构成，object分为如下两类用途：
- 用于操作（增删改查）单张数据库表的object；
- 多张数据库表查询结果对应的object。

当业务简单时，上述两类object可以存在同一个object中。

object由多个property组成。一个property和一个数据库字段、查询结果字段对应，也可不对应。

## 3.2 object节点

### 3.2.1 name属性

- 含义：object名称
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：项目范围内全局唯一
- 取值范围：小驼峰法命名

### 3.2.2 table属性

- 含义：object对应的数据库表名
- 类型：普通
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：数据库存在的表名

### 3.2.3 primary属性

- 含义：object对应的数据库表中主键所在的字段名，如数据库表无主键，则为值唯一的字段名
- 类型：普通
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：数据库表中存在的字段名

### 3.2.4 father属性

- 含义：object对应的数据库表记录如是树结构，该属性表示指向父节点的字段名
- 类型：普通
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：数据库表中存在的字段名

### 3.2.5 unique属性

- 含义：object中property的唯一性约束规则
- 类型：普通
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：由property名称和操作符构成，操作符支持`,`和`|`两种符号，用操作符连接property名。`,`表示连接的property共同构成一条唯一性约束规则，`|`表示多条唯一性约束规则。例如，`prdCode|prdCata,prdName,prdSpec`表示两条唯一性约束规则，规则含义如下。
    - prdCode唯一；
    - prdCata+prdName+prdSpec唯一。

### 3.2.6 order属性

- 含义：object的排序规则
- 类型：普通
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：排序规则格式为`字段名 排序关键字,字段名 排序关键字,……`。排序关键字desc表示逆向排序，无排序关键字表示正向排序。排序优先级从左到右。

## 3.3 property节点

### 3.3.1 name属性

- 含义：property名称
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：同一object唯一
- 取值范围：小驼峰法命名

### 3.3.2 type属性

- 含义：property数据类型
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：无
- 取值范围：大驼峰法命名，支持如下类型。
    - String
    - Long
    - Double
    - Date

### 3.3.3 length属性

- 含义：property数据类型对应的长度
- 类型：普通
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：
    - String类型为自然数，表示字符个数，一个英文字符或一个汉字字符，均算一个字符。
    - Long类型为自然数，表示数字个数。例如，12345长度为5。
    - Double类型为m.n格式，m、n均为自然数，m表示数字个数，n表示小数个数且m>n。例如，123.45长度为5.2。

### 3.3.4 validate属性

- 含义：property数据输入合法性验证规则
- 类型：普通
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：逻辑表达式，返回布尔值

### 3.3.5 field属性

- 含义：property对应数据库表字段名
- 类型：普通
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：数据库表中存在的字段名

# 4 表单交互界面

## 4.1 概述

## 4.2 ui节点

### 4.2.1 name属性

- 含义：界面名称
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：项目范围内全局唯一
- 取值范围：小驼峰法命名

### 4.2.2 title属性

- 含义：界面显示标题
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：项目范围内全局唯一
- 取值范围：支持多语言的字符串

## 4.3 panel节点

### 4.3.1 name属性

- 含义：界面中的panel名称
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：同一界面唯一
- 取值范围：小驼峰法命名

### 4.3.2 type属性

- 含义：panel类型
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：无
- 取值范围：大驼峰法命名。支持的panel类型如下。

类型 | 说明
--- | ---
Toolbar	| 按钮栏
Group | 控件组
Grid | 表格控件
Tree | 树控件
Image | 图片控件

### 4.3.3 title属性

- 含义：panel显示标题
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：同一界面唯一
- 取值范围：支持多语言的字符串

### 4.3.4 object属性

- 含义：panel绑定的object名
- 类型：普通
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：存在的object名

### 4.3.5 optimize属性

- 含义：panel作为查询条件In参数时，是否进行查询优化
- 类型：普通
- 必填：否
- 默认值：N
- 唯一性：无
- 取值范围：Y表示是，N表示否

## 4.4 control节点

### 4.4.1 name属性

- 含义：panel下的control名称
- 类型：普通
- 必填：是
- 默认值：无
- 唯一性：同一panel唯一
- 取值范围：小驼峰法命名

### 4.4.2 type属性

- 含义：control类型
- 类型：普通
- 必填：是
- 默认值：无
- 唯一性：无
- 取值范围：大驼峰法命名。支持的control类型如下。

control类型 | 对应panel类型 | 说明
--- | --- | ---
Button | Toolbar、Grid | 按钮
TextBox | Group、Grid、Tree | 文本编辑框
DateBox | Group、Grid | 日期编辑框
ComboBox | Group、Grid | 下拉列表框
PopupBox | Group、Grid | 弹出对话框的编辑框
PasswordBox | Group | 密码编辑框
FileBox | Group | 文件上传框
ImageView | Image | 图片框
DataLink | Grid、Tree | 数据链接
Hidden | Group、Grid、Tree | 隐藏控件

### 4.4.3 title属性

- 含义：control显示标题
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：同一panel唯一
- 取值范围：支持多语言的字符串

### 4.4.4 property属性

- 含义：control绑定的property名
- 类型：普通
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：当前panel绑定的object中存在的property名

### 4.4.5 edit属性

- 含义：control是否能编辑
- 类型：普通
- 必填：否
- 默认值：N
- 唯一性：无
- 取值范围：Y表示可编辑，N表示只读、不可编辑。

### 4.4.6 required属性

- 含义：可编辑时是否必填
- 类型：普通
- 必填：否
- 默认值：N
- 唯一性：无
- 取值范围：Y表示必填，N表示不必填

### 4.4.7 format属性

- 含义：control数据显示格式
- 类型：表达式
- 必填：否
- 默认值：否
- 唯一性：无
- 取值范围：求值表达式

### 4.4.8 initial属性

- 含义：新增模式下，默认输入值的计算规则
- 类型：表达式
- 必填：否
- 默认值：否
- 唯一性：无
- 取值范围：求值表达式

### 4.4.9 source属性

- 含义：编辑、只读模式下，从数据库读取数据后，返回客户端前执行的计算规则
- 类型：表达式
- 必填：否
- 默认值：否
- 唯一性：无
- 取值范围：求值表达式

### 4.4.10 target属性

- 含义：新增、编辑模式下，向数据库保存数据前执行的计算规则
- 类型：表达式
- 必填：否
- 默认值：否
- 唯一性：无
- 取值范围：求值表达式

### 4.4.11 width属性

- 含义：control相对panel的宽度
- 类型：普通
- 必填：否
- 默认值：否
- 唯一性：无
- 取值范围：自然数

### 4.4.12 height属性

- 含义：control相对panel的高度
- 类型：普通
- 必填：否
- 默认值：否
- 唯一性：无
- 取值范围：自然数

### 4.4.13 column属性

- 含义：control在布局栅格中是否位于最后一列
- 类型：普通
- 必填：否
- 默认值：N
- 唯一性：无
- 取值范围：Y表示是，N表示否

### 4.4.13 alias属性

- 含义：带查询前缀，且所属Panel的optimize="Y"时，alias属性作为查询条件名
- 类型：普通
- 必填：当所在Panel的optimize="Y"时，必填
- 默认值：无
- 唯一性：无
- 取值范围：alias值参与查询时的SQL条件拼接，必须为所在层的合法表(别)名.字段

## 4.5 layout节点

### 4.5.1 布局方案

todo：图

### 4.5.2 region节点

#### 4.5.2.1 location属性

- 含义：region在界面布局中的位置
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：同一界面中值唯一
- 取值范围：region可以设置location为如下值，用以表示panel在界面布局中的位置。
    - North：位于布局最上方，
    - South：位于布局最下方，在North和South之间，
    - East：位于布局最右方，在North和South之间，
    - West：位于布局最左方，
    - Center：位于布局中间，

#### 4.5.2.2 scale属性

- 含义：region宽度或高度
- 类型：普通
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：可表示成百分比（数字%），或表示成像素（数字px）。当location属性为`North`、`South`时，scale表示高度；当location属性为`West`、`East`时，scale表示宽度。如panel是固定高度类型时，无需设置高度。

### 4.5.3 subregion节点

#### 4.5.3.1 panel属性

- 含义：subregion绑定的panel名称
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：同一panel在同一界面中仅出现一次
- 取值范围：当前界面中存在的panel名称

#### 4.5.3.2 location属性

- 含义：subregion在region中的位置
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：无
- 取值范围：同region的location属性。如同一个region值下，有多个subregion的location属性相同，则展现成tab页形式，一个panel一个tab页。

#### 4.5.3.3 scale属性

- 含义：subregion宽度或高度
- 类型：普通
- 必填：否
- 默认值：否
- 唯一性：无
- 取值范围：同region的scale属性。

## 4.6 flow节点

### 4.6.1 flow节点

#### 4.6.1.1 trigger属性

- 含义：触发flow的事件来源，事件可以属于界面、panel或control
- 类型：普通
- 必填：否
- 默认值：空表示当前界面
- 唯一性：trigger+event在同一个界面中唯一
- 取值范围：panelName表示panel名称，panelName.controlName表示control名称

#### 4.6.1.2 event属性

- 含义：flow的触发事件
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：trigger+event在同一个界面中唯一
- 取值范围：支持的触发事情如下。
    - OnLoad：界面元素被创建后。
    - OnClick：界面元素功能区域被点击后。
    - OnSelect：界面元素完成选择操作后。

支持的触发事件与界面、panel、control关系如下。

\ | OnLoad | OnClick | OnSelect
--- | :---: | :---: | :---:
界面 | √ | |
Toolbar | | |
Group | | |
Grid | | | √
Tree | | | √
Image | | |
Button | | √ |
TextBox | | |
DateBox | | | √
ComboBox | | √ | √
PopupBox | | √ | √
PasswordBox | | |
FileBox | | | √
ImageView | | √ |
DataLink | | √ |
Hidden | | |

#### 4.6.1.3 filter属性

- 含义：flow在当前状态下是否可以被触发
- 类型：表达式
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：逻辑表达式，返回布尔值

### 4.6.2 procedure节点

#### 4.6.2.1 name属性

- 含义：procedure名称
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：同一Flow唯一
- 取值范围：小驼峰法命名

#### 4.6.2.2 server属性

- 含义：服务端调用URL路径
- 类型：普通
- 必填：逻辑有服务端调用时必填？
- 默认值：无
- 唯一性：无
- 取值范围：URL相对路径，例如/issue/list

#### 4.6.2.3 logic属性

- 含义：flow调用的业务逻辑
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：无
- 取值范围：系统支持的逻辑名

#### 4.6.2.4 ifyes属性

- 含义：当前过程执行结果为真时的下一过程名
- 类型：普通
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：当前flow内的procedure名

#### 4.6.2.5 ifno属性

- 含义：当前过程执行结果为假时的下一过程名
- 类型：普通
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：当前flow内的procedure名

#### 4.6.2.6 sync属性

- 含义：当前过程执行期间，是否允许同flow的其它过程并行执行
- 类型：普通
- 必填：否
- 默认值：Y
- 唯一性：无
- 取值范围：Y表示不允许同flow过程并行执行，只能等当前过程执行完才能执行下一过程；N表示允许并行执行

### 4.6.3 param节点

#### 4.6.3.1 name属性
- 含义：procedure执行逻辑的输入参数名
- 类型：普通
- 必填：是
- 默认值：N/A
- 唯一性：当前procedure唯一
- 取值范围：逻辑定义的参数名，参见【5.2 逻辑】

#### 4.6.3.2 value属性
- 含义：procedure执行逻辑的输入参数值
- 类型：普通或表达式
- 必填：否
- 默认值：无
- 唯一性：无
- 取值范围：逻辑定义的参数取值范围，参见【5.2 逻辑】

# 5 表单业务模型

## 5.1 概述

## 5.2 逻辑

prcedure节点下的param子节点描述procedure对应logic的参数，param节点的name属性描述参数名称，value属性描述参数取值。

### 5.2.1 页面导航类

#### 5.2.1.1 Transition

- 功能说明：从当前页面导航进入新页面
- 参数定义：
    - To：新页面的ui名称，普通类型，必填
    - Mode：新页面的编辑模式，普通类型，必填，从如下选项中选择一个。
        - `Add`：新增模式，对话框中属性`edit="Y"`的control可编辑
        - `Edit`：编辑模式，对话框中属性`edit="Y"`的control可编辑
        - `Readonly`：只读模式，对话框中所有control均不可编辑
- 执行结果：无分支，该flow中此procedure后的procedure不执行
- 执行位置：客户端和服务端均执行

#### 5.2.1.2 Back

- 功能说明：从当前页面后退到上一页
- 参数定义：无
- 执行结果：无分支，该flow中此procedure后的procedure不执行
- 执行位置：客户端和服务端均执行

#### 5.2.1.3 Popup

- 功能说明：在当前页面上弹出模式对话框
- 参数定义：
    - To：弹出对话框的ui名称，普通类型，必填
    - Mode：弹出对话框的编辑模式，普通类型，必填，从如下选项中选择一个。
        - `Add`：新增模式，对话框中属性`edit="Y"`的control可编辑
        - `Edit`：编辑模式，对话框中属性`edit="Y"`的control可编辑
        - `Readonly`：只读模式，对话框中所有control均不可编辑
    - Window：弹出对话框的大小等外观信息，普通类型，非必填，默认为`Small`。
        - `Small`：小尺寸对话框
        - `Medium`：中尺寸对话框
        - `Large`：大尺寸对话框
        -  自定义对话框大小 格式  ： "像素宽|像素高" 或者 "50%|60%"
- 执行结果：
    - true：弹出对话框通知父页面需刷新
    - false：弹出对话框通知父页面无需刷新
- 执行位置：客户端和服务端均执行

#### 5.2.1.4 Close

- 功能说明：关闭当前模式对话框，返回弹出对话框的页面
- 参数定义：
    - Refresh：关闭当前对话框后，是否通知父页面刷新（父页面是否刷新由父页面配置的逻辑决定），`true`通知父页面刷新，`false`：通知父页面不刷新
- 执行结果：无分支，该flow中此procedure后的procedure不执行
- 执行位置：仅客户端执行

#### 5.2.1.5 EncodeId

- 功能说明：将指定panel中选中记录的control值传递到中间变量，支持单选和多选
- 参数定义：
    - In：需传递值得control名称，必填，格式为`panelName.controlName`
- 执行结果：
    - true：取到选中值
    - false：未取到选中值
- 执行位置：仅客户端执行

#### 5.2.1.6 DecodeId

- 功能说明：取中间变量的值，填充到指定control中
- 参数定义：
    - Out：需填充值的control名称，必填，格式为`panelName.controlName`
- 执行结果：
    - true：取到填充值
    - false：：未取到填充值
- 执行位置：仅客户端执行

### 5.2.2 表单处理类

#### 5.2.2.1 Clear

- 功能说明：根据表单的初始值（control节点的initial属性）定义，计算出当前状态的初始值，结果填充到指定的panel中
- 参数定义：
    - Out：结果填充的panel名称，普通类型，必填
    - Plugin：服务端插件service名称,需实现ILogicPlugin接口,用于在controller层处理前和返回前进行数据的加工，和逻辑不在同一事物。    
- 执行结果：无分支，固定返回`true`
- 执行位置：客户端和服务端均执行

#### 5.2.2.2 List

- 功能说明：根据自定义SQL，从数据库查询数据，结果填充到指定的control绑定的列表选择数据源中
- 参数定义：
    - Out：结果填充的control名称，普通类型，必填，格式为`panelName.controlName`
    - Sql：查询SQL的名称，普通类型，必填
    - Plugin：服务端插件service名称,需实现ILogicPlugin接口,用于在controller层处理前和返回前进行数据的加工，和逻辑不在同一事物。    
- 执行结果：无分支，固定返回`true`
- 执行位置：客户端和服务端均执行

#### 5.2.2.3 Query

- 功能说明：根据指定panel中定义的查询条件与指定的SQL，从数据库查询数据，结果填充到指定panel中
- 参数定义：
    - In：定义查询条件的panel名称，普通类型，非必填。当control带查询前缀(eq_,lt_...)且required='Y'，查询前将校验必填项，若存在空的必填项，查询结果返回空。
    - Out：结果填充的panel名称，普通类型，必填
    - Sql：查询SQL的名称，普通类型，必填。In的optimize="Y"时，需在sql中正确位置放入${where}标签，用于解释条件。
    - Plugin：服务端插件service名称,需实现ILogicPlugin接口,用于在controller层处理前和返回前进行数据的加工，和逻辑不在同一事物。
- 执行结果：无分支，固定返回`true`
- 执行位置：客户端和服务端均执行

#### 5.2.2.4 QueryById

- 功能说明：根据指定的ID（由EncodeID逻辑传入）和指定的SQL，从数据库查询出一条数据，结果填充到指定panel中
- 参数定义：
    - Out：结果填充的panel名称，普通类型，必填
    - Sql：查询SQL的名称，普通类型，必填
    - Plugin：服务端插件service名称,需实现ILogicPlugin接口,用于在controller层处理前和返回前进行数据的加工，和逻辑不在同一事物。
- 执行结果：无分支，固定返回`true`
- 执行位置：客户端和服务端均执行

#### 5.2.2.5 Save

- 功能说明：将指定panel的数据提交到服务器，根据表单定义，通过合法性验证后，保存（新增或修改）到数据库中
- 参数定义：
    - In：提交数据的panel名称，普通类型，必填
    - Msg：服务端处理后的提示信息，表达式类型，非必填
    - Plugin：服务端插件service名称,需实现ILogicPlugin接口,用于在controller层处理前和返回前进行数据的加工，和逻辑不在同一事物。
    - DAOPlugin：服务端插件service名称,需实现ILogicServicePlugin接口,用于在DAO层处理前和返回前进行数据的加工，和逻辑在同一事物。
- 执行结果：
    - true：保存成功
    - false：保存失败
- 执行位置：客户端和服务端均执行

#### 5.2.2.6 SaveCustom

- 功能说明：将指定panel的数据提交到服务器，通过合法性验证（仅验证SQL引用数据）后，根据自定义SQL，保存（新增或修改）到数据库中
- 参数定义：
    - In：提交数据的panel名称，普通类型，必填
    - Sql：自定义SQL的名称，普通类型，必填
    - Msg：服务端处理后的提示信息，表达式类型，非必填
    - Plugin：服务端插件service名称,需实现ILogicPlugin接口,用于在controller层处理前和返回前进行数据的加工，和逻辑不在同一事物。
    - DAOPlugin：服务端插件service名称,需实现ILogicServicePlugin接口,用于在DAO层处理前和返回前进行数据的加工，和逻辑在同一事物。
- 执行结果：
    - true：保存成功
    - false：保存失败
- 执行位置：客户端和服务端均执行

#### 5.2.2.7 Delete

- 功能说明：删除指定panel数据对应的数据库记录
- 参数定义：
    - In：删除记录对应的panel名称
    - Msg：服务端处理后的提示信息，表达式类型，非必填
    - Plugin：服务端插件service名称,需实现ILogicPlugin接口,用于在controller层处理前和返回前进行数据的加工，和逻辑不在同一事物。
    - DAOPlugin：服务端插件service名称,需实现ILogicServicePlugin接口,用于在DAO层处理前和返回前进行数据的加工，和逻辑在同一事物。
- 执行结果：
    - true：删除成功
    - false：删除失败
- 执行位置：客户端和服务端均执行

### 5.2.3 预置功能

#### 5.2.3.1 Find

- 功能说明：弹出列表查找对话框，选择后返回
- 参数定义：
    - param
- 执行结果：无分支，固定返回`true`
- 执行位置：客户端和服务端均执行

#### 5.2.3.2 Export

- 功能说明：根据指定panel中定义的查询条件与指定的SQL，从数据库查询数据，结果导出到Excel、CSV、HTML或PDF文件中
- 参数定义：
    - In：定义查询条件的panel名称，普通类型，非必填
    - Format：导出数据列格式依据的panel的名称，普通类型，必填
    - Sql：查询SQL的名称，普通类型，必填
- 执行结果：无分支，固定返回`true`
- 执行位置：客户端和服务端均执行

#### 5.2.3.3 InfoMsg

- 功能说明：弹出信息提示对话框
- 参数定义：
    - Msg：提示信息，表达式类型，必填，用分号结尾时视为客户端语句，以客户端语句执行结果作为显示内容
- 执行结果：无分支，固定返回`true`
- 执行位置：仅客户端执行

#### 5.2.3.4 ConfirmMsg

- 功能说明：弹出信息确认对话框
- 参数定义：
    - Msg：提示信息，表达式类型，必填
    - Mode：对话框功能模式，普通类型，必填
        - `YesNo`
        - `OkCanncel`
- 执行结果：
    - true：当点击`Yes`或`Ok`按钮时
    - false：当点击`No`或`Cancel`按钮时
- 执行位置：仅客户端执行

#### 5.2.3.5 ToastMsg

- 功能说明：右下角弹出信息提示弹窗，停留一段时间后消失
- 参数定义：
    - Msg：提示信息，表达式类型，必填
- 执行结果：无分支，固定返回`true`
- 执行位置：仅客户端执行

#### 5.2.3.6 UploadFile

- 功能说明：弹出文件选择对话框，选中文件后上传到服务器，并返回文件ID
- 参数定义：
    - Type：允许上传的文件后缀，多个文件后缀用逗号隔开，普通类型，必填
    - Size：允许上传的最大文件字节数，普通类型，必填
    - Bucket：文件的存放位置，普通类型，非必填，默认`private`。
        - `public`：可公开访问的空间
        - `private`：需有权限的用户才能访问的空间
- 执行结果：无分支，固定返回`true`
- 执行位置：客户端和服务端均执行

## 5.3 表达式

todo

### 5.3.1 数据类型

数据类型 | 常量表示方法 | 说明
:---: | --- | ---
`Long` | 十进制数字 | 整数类型
`Double` | 带小数点的十进制数字 | 浮点数类型
`String` | 单引号括起来的文本串 | 字符串类型
`Boolean` | 常量`true`和`false` | 布尔类型
`Pattern` | 以`//`括起来的字符串（如`/\d+/`），与`java.util.Pattern`定义方式一致 | 正则表达式
`Seq` | | 集合，类似`java.util.Collection`下的子类
变量 | | 变量类型

### 5.3.2 预定义常量

常量 | 说明
:---: | ---
`nil` | `nil`类似java的`null`，`nil`可参与`==、!=、>、>=、<、<=`的比较，任何类型都大于`nil`（除了`nil`本身），`nil==nil`返回`true`
`true` | `Boolean`类型，类似java的`Boolean.TRUE`
`false` | `Boolean`类型，类似java的`Boolean.False`

日期类型以`'yyyy-MM-dd HH:mm:ss:SS'`格式的字符串来表示，例如`'2017-08-31 00:00:00:00'`。

### 5.3.3 预定义变量

变量 | 说明
:---: | ---
`self` | 当前control绑定的property值
`row` | 当前表格行对象
`rows` | 当前表格数据集合
`session` | 当前用户的session对象，包括`session.userInfo`、`session.empInfo.empName`等
`logic` | 当前执行的logic发送的request中的参数，可按`logic.parameterName`形式出现在表达式中，如`logic.Mode`等
`model` | 当前model中取值，使用方式`model.objectName.propertyName`

### 5.3.4 操作符

#### 5.3.4.1 算术运算符

运算符 | 类型 | 说明 | 操作数适用数据类型
:---: | --- | --- | ---
`+` | 二元操作符 | 加号 | `Long`、`Double`、`String`，任何类型与`String`相加，结果为`String`
`-` | 二元操作符 | 减号 | `Long`、`Double`
`*` | 二元操作符 | 乘号 | `Long`、`Double`
`/` | 二元操作符 | 除号 | `Long`、`Double`
`%` | 二元操作符 | 取模 | `Long`、`Double`
`-` | 一元操作符 | 负号 | `Long`、`Double`

#### 5.3.4.2 逻辑运算符

运算符 | 说明
:---: | ---
`!` | 一元否定运算符
`&&` | 逻辑与
`||` | 逻辑或

逻辑运算符的操作数只能为`Boolean`类型。`&&`、`||`均执行短路规则，例如：
- `(表达式1)&&(表达式2)`如果表达式1为假，则表达式2不会进行运算，即表达式2“被短路”。
- `(表达式1)||(表达式2)`如果表达式1为真，则表达式2不会进行运算，即表达式2“被短路”。

#### 5.3.4.3 关系运算符

运算符 | 说明
:---: | ---
`<` | 小于
`<=` | 小于等于
`>` | 大于
`>=` | 大于等于
`==` | 等于
`!=` | 不等于

关系运算符可以作用于`Long`之间、`Double`之间、`String`之间、`Boolean`之间、`Pattern`之间、变量之间以及其他类型与`nil`之间的关系比较, 不同类型除了`nil`之外不能相互比较。

#### 5.3.4.4 位运算符

运算符 | 说明
:---: | ---
`&` | 位与
`|` | 位或
`^` | 异或
`~` | 非
`>>` | 右移
`<<` | 左移
`>>>` | 无符号右移，忽略符号位，空位都以0补齐

#### 5.3.4.5 匹配运算符

匹配运算符`=~`用于`String`和`Pattern`的匹配，它的左操作数必须为`String`,右操作数必须为`Pattern`。匹配结果返回`Boolean`值。

#### 5.3.4.6 三元运算符

三元运算符`?:`，形式为`bool ? exp1: exp2`。 其中`bool`必须为`Boolean`类型的表达式，而`exp1`和`exp2`可以为任何合法的表达式,并且不要求`exp1`和`exp2`返回的结果类型一致。

### 5.3.5 转义符

在XML文件中，需对表达式中的如下字符转义。

字符 | 转义写法
:---: | ---
`&` | `&amp;`
`<` | `&lt;`

## 5.4 函数

### 5.4.1 类型转换类

#### Long(v)
- 功能说明：将值v转换为Long类型
- 参数定义：变量类型
- 返回值：Long类型

#### Double(v)
- 功能说明：将值v转换为double类型
- 参数定义：变量类型
- 返回值：Double类型

#### Str(v)
- 功能说明：将值v转换为String类型
- 参数定义：变量类型
- 返回值：String类型

#### Date2Str(date,format)
- 功能说明：将值date转换为format格式
- 参数定义：
    - date：String类型
    - format：String类型
- 返回值：String类型

#### Str2Date(s,format)
- 功能说明：将s转化为Date类型
- 参数定义：string
- 返回值：date

### 5.4.2 数据校验类

#### CheckEmail(s)
- 功能说明：验证Email地址格式是否正确
- 参数定义：String类型
- 返回值：Boolean类型，`true`表示验证通过，`false`表示验证不通过

#### CheckIdCard(s)
- 功能说明：验证身份证号格式是否正确
- 参数定义：String类型
- 返回值：Boolean类型，`true`表示验证通过，`false`表示验证不通过

#### CheckMobile(s)
- 功能说明：验证手机号码格式是否正确
- 参数定义：String类型
- 返回值：Boolean类型，`true`表示验证通过，`false`表示验证不通过

#### CheckPhone(s)
- 功能说明：验证电话格式是否正确
- 参数定义：String类型
- 返回值：Boolean类型，`true`表示验证通过，`false`表示验证不通过

#### CheckURL(s)
- 功能说明：验证URL格式是否正确
- 参数定义：String类型
- 返回值：Boolean类型，`true`表示验证通过，`false`表示验证不通过

#### CheckPostCode(s)
- 功能说明：验证中国邮政编码格式是否正确
- 参数定义：String类型
- 返回值：Boolean类型，`true`表示验证通过，`false`表示验证不通过

#### CheckIpAddress(s)
- 功能说明：验证IP地址（包含IPV4和IPV6）格式是否正确
- 参数定义：String类型
- 返回值：Boolean类型，`true`表示验证通过，`false`表示验证不通过

#### CheckMACAddress(s)
- 功能说明：验证设备网卡MAC地址格式是否正确
- 参数定义：String类型
- 返回值：Boolean类型，`true`表示验证通过，`false`表示验证不通过

#### CheckCharAndNum(s)
- 功能说明：验证输入参数是否是数字加字母格式
- 参数定义：String类型
- 返回值：Boolean类型，`true`表示验证通过，`false`表示验证不通过

#### CheckPasswordStrength(s)
- 功能说明：验证口令强度是否符合要求
- 参数定义：String类型
- 返回值：Boolean类型，`true`表示验证通过，`false`表示验证不通过

### 5.4.3 字符串类

#### Contain(s1,s2)
- 功能说明：判断s1是否包含s2
- 参数定义：
    - s1：String类型
    - s2：String类型
- 返回值：Boolean类型

#### Length(s)
- 功能说明：返回字符串长度
- 参数定义：String类型
- 返回值：String类型

#### StartWith(s1,s2)
- 功能说明：s1是否以s2开头
- 参数定义：String类型
- 返回值：Boolean类型

#### EndWith(s1,s2)
- 功能说明：s1是否以s2结尾
- 参数定义：String类型
- 返回值：Boolean类型

#### SubString(s,begin,[end])
- 功能说明：从begin到end截取字符串s，end如果忽略的话，将从begin到结尾
- 参数定义：
    - s：String类型
    - begin：Long类型
    - end：Long类型
- 返回值：String类型

#### IndexOf(s1,s2)
- 功能说明：返回s2在s1中的起始索引位置，如果不存在为-1
- 参数定义：
    - s1：String类型
    - s2：String类型
- 返回值：String类型

#### Split(target,regex,[limit])
- 功能说明：使用分隔符regex分解字符串target
- 参数定义：
    - target：String类型
    - regex：String类型
    - limit：Long类型，表示最多分几份
- 返回值：Seq类型，元素为String类型

#### Join(seq,separator)
- 功能说明：将集合seq里的元素以separator为间隔连接起来形成字符串
- 参数定义：
    - seq：Seq类型
    - separator：String类型
- 返回值：String类型

#### ReplaceFirst(s,regex,replacement)
- 功能说明：将字符串s中第一个与regex匹配的字符串，替换为replacement字符串
- 参数定义：
    - s：String类型
    - regex：String类型
    - replacement：String类型
- 返回值：String类型

#### ReplaceAll(s,regex,replacement)
- 功能说明：将字符串s中所有与regex匹配的字符串，替换为replacement字符串
- 参数定义：
    - s：String类型
    - regex：String类型
    - replacement：String类型
- 返回值：String类型

#### LowerCase(s)
- 功能说明：将字符串s转换成小写
- 参数定义：
	- s：String类型
- 返回值：String类型

#### UpperCase(s)
- 功能说明：将字符串s转换成大写
- 参数定义：
	- s：String类型
- 返回值：String类型

#### Double2Chinese(d)
- 功能说明：将阿拉伯数字转换成中文大写数字，例如`123.456`转成`壹佰贰拾叁元肆角伍分六厘`，多于三位小数按四舍五入处理
- 参数定义：Double类型
- 返回值：String类型

#### Chinese2Double (s)
- 功能说明：将中文大写数字转换成阿拉伯数字，例如`壹佰贰拾叁元肆角伍分六厘`转成`123.456`
- 参数定义：String类型
- 返回值：Double类型

#### Chinese2Pinyin(s,type)
- 功能说明： 将汉字词语转换为拼音
- 参数定义：
    - s：String类型
    - type：Long类型，1表示全部大写，2表示全部小写，3表示拼音首字母大写
- 返回值：string

#### Md5(s)
- 功能说明：对输入字符串用MD5算法进行散列计算后返回，返回值为大写字符串
- 参数定义：
	- s：String类型
- 返回值：String类型

#### Guid()
- 功能说明：返回32个大写字符构成的全局唯一标识
- 参数定义：无
- 返回值：String类型

### 5.4.4 上下文类

#### CurrentTime()
- 功能说明：获取数据库服务器时间
- 参数定义：无
- 返回值：String类型，格式为`YYYY-MM-DD HH:mm:ss`

### 5.4.5 集合运算类

#### Sum(seq)
- 功能说明：返回集合中所有元素的和
- 参数定义：Seq类型，元素类型为Long或Double
- 返回值：Long或Double类型

#### Max(seq)
- 功能说明：返回集合中所有元素的最大值
- 参数定义：Seq类型，元素类型为Long或Double
- 返回值：Long或Double类型

#### Min(seq)
- 功能说明：返回集合中所有元素的最小值
- 参数定义：Seq类型，元素类型为Long或Double
- 返回值：Long或Double类型

#### Avg(seq)
- 功能说明：返回集合中所有元素的平均值
- 参数定义：Seq类型，元素类型为Long或Double
- 返回值：Double类型

#### Distinct(seq)
- 功能说明：去掉集合元素中的重复元素，返回处理后元素组成的集合
- 参数定义：Seq类型
- 返回值：Seq类型

#### First(seq)
- 功能说明：返回集合中的第一个元素
- 参数定义：Seq类型
- 返回值：变量类型

#### Last(seq)
- 功能说明：返回集合中的最后一个元素
- 参数定义：Seq类型
- 返回值：变量类型

#### Map(seq,func)
- 功能说明：将函数func作用到集合seq每个元素上, 返回新元素组成的集合
- 参数定义：
    - seq：Seq类型
    - func：函数名称
- 返回值：Seq类型

#### Filter(seq,predicate)
- 功能说明：将谓词predicate作用在集合的每个元上,返回谓词为true的元素组成的集合
- 参数定义：
    - seq：Seq类型
    - predicate：谓词
- 返回值：Seq类型

#### Count(seq)
- 功能说明：返回seq集合的元素数量
- 参数定义：Seq类型
- 返回值：Long类型

#### Include(seq,element)
- 功能说明：判断element是否在集合seq中，返回Boolean值
- 参数定义：
    - seq：Seq类型
    - element：变量类型
- 返回值：Boolean类型

#### Sort(seq)
- 功能说明：排序集合，仅对数组和List（返回Array$ArrayList）有效，返回升序排序后的新集合
- 参数定义：Seq类型
- 返回值：Seq类型

#### Reduce(seq,func,init)
- 功能说明：func接收两个参数，第一个是集合元素，第二个是累积的函数，本函数用于将func作用在集合每个元素和初始值上面，返回最终的init值
- 参数定义：
    - seq：Seq类型
    - func：函数名称
    - init：初始值，变量类型
- 返回值：变量类型

#### Every(seq,func)
- 功能说明：func函数接受集合的每个元素作为唯一参数，返回`true`或`false`，每个调用都为`true`，最终为`true`，否则为`false` 
- 参数定义：
    - seq：Seq类型
    - func：函数名称
- 返回值：Boolean类型

#### NotAny(seq,func)
- 功能说明：func接收集合的每个元素作为唯一参数，返回`true`或`false`，当集合里的每个元素调用func后都返回`false` 时，整个调用结果为`true`，否则为`false`
- 参数定义：
    - seq：Seq类型
    - func：函数名称
- 返回值：Boolean类型

#### Some(seq,func)
- 功能说明：func接收集合的每个元素作为唯一参数，返回`true`或`false`，当集合里的只要有一个元素调用func后返回`true`时，整个调用结果立即为`true`，否则为`false`
- 参数定义：
    - seq：Seq类型
    - func：函数名称
- 返回值：Boolean类型

#### IsEq(value)
- 功能说明：返回一个谓词，用来判断传入的参数是否跟value相等，用于filter函数，如`filter(seq,seq.eq(3))`过滤返回等于3的元素组成的集合
- 参数定义：变量类型
- 返回值：谓词

#### IsNeq(value)
- 功能说明：与SeqEq类似，返回判断不等于的谓词
- 参数定义：变量类型
- 返回值：谓词

#### IsGt(value)
- 功能说明：返回判断大于value的谓词
- 参数定义：变量类型
- 返回值：谓词

#### IsGe(value)
- 功能说明：返回判断大于等于value的谓词
- 参数定义：变量类型
- 返回值：谓词

#### IsLt(s)
- 功能说明：返回判断小于value的谓词
- 参数定义：变量类型
- 返回值：谓词

#### IsLe(s)
- 功能说明：返回判断小于等于value的谓词
- 参数定义：变量类型
- 返回值：谓词

#### IsNil(s)
- 功能说明：返回判断是否为nil的谓词
- 参数定义：变量类型
- 返回值：谓词

#### IsExists(s)
- 功能说明：返回判断不为nil的谓词
- 参数定义：变量类型
- 返回值：谓词

#### IsAnd(p1,p2,p3,……)
- 功能说明：组合多个谓词函数，返回一个新的谓词函数，当今仅当 p1、p2、p3等所有函数都返回`true`的时候，新函数返回`true`
- 参数定义：
    - p1：谓词函数
    - ……
- 返回值：Boolean类型

#### IsOr(p1,p2,p3,……)
- 功能说明：组合多个谓词函数，返回一个新的谓词函数，当 p1, p2, p3等函数其中一个返回`true`的时候，新函数立即返回 `true`，否则返回`false`
- 参数定义：
    - p1：谓词函数
    - ……
- 返回值：Boolean类型

### 5.4.6 数学运算类

#### Rand()
- 功能说明：返回一个介于 0-1 的随机数
- 参数定义：无
- 返回值：Double类型

#### Abs(d)
- 功能说明：求d的绝对值
- 参数定义：Long或Double类型
- 返回值：Long或Double类型

#### Sqrt(d)
- 功能说明：求d的平方根
- 参数定义：Long或Double类型
- 返回值：Double类型

#### Pow(d1,d2)
- 功能说明：求d1的d2次方
- 参数定义：Long或Double类型
- 返回值：Double类型

#### Log(d)
- 功能说明：求d的自然对数
- 参数定义：Long或Double类型
- 返回值：Long或Double类型

#### Log10(d)
- 功能说明：求d以10为底的对数
- 参数定义：Long或Double类型
- 返回值：Long或Double类型

#### Sin(d)
- 功能说明：正弦函数
- 参数定义：Long或Double类型
- 返回值：Long或Double类型

#### Cos(d)
- 功能说明：余弦函数
- 参数定义：Long或Double类型
- 返回值：Long或Double类型

#### Tan(d)
- 功能说明：正切函数
- 参数定义：Long或Double类型
- 返回值：Long或Double类型

## 5.5 SQL定义文件

SQL名称命名采用小驼峰法。

# 6 表单定义示例

# 7 平台配置文件

todo

# 8 开发指南 - 进阶篇

## 自定义Web界面

## 自定义显示控件

## 自定义逻辑

## 自定义函数

## 自定义插件