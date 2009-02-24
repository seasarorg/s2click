<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%--
 Copyright 2006-2009 the Seasar Foundation and the Others.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 either express or implied. See the License for the specific language
 governing permissions and limitations under the License.
--%>
<jsp:useBean id="format" class="org.seasar.s2click.util.S2ClickFormat" scope="request" />
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link href="${context}/style.css" rel="stylesheet" type="text/css" media="all">
  ${imports}
  <title>${title}</title>
</head>
<body>
<table class="contents" cellspacing="0">
<tr>
  <td colspan="2" class="app-title">
    <img src="${context}/images/click.png"><span class="title">S2Click Examples</span>
  </td>
</tr>
<tr><td colspan="2" class="page-title">JSPのサンプル</td></tr>
<tr>
  <td valign="top" class="menu">
    <ul>
      <li><a href="${context}/add.htm">足し算</a></li>
      <li><a href="${context}/controls.htm">コントロール</a></li>
      <li><a href="${context}/tooltip.htm">ツールチップ</a></li>
      <li><a href="${context}/graph.htm">グラフ</a></li>
      <li><a href="${context}/json.htm">JSON</a></li>
      <li><a href="${context}/file-upload.htm">ファイルアップロード</a></li>
      <li><a href="${context}/jdbc.htm">S2JDBC</a></li>
      <li><a href="${context}/greybox.htm">GreyBox</a></li>
      <li><a href="${context}/user/select/001">URLパターン</a></li>
      <li><a href="${context}/jsp-example.htm">JSP</a></li>
      <li><a href="${context}/mobile-sample.htm">モバイル</a></li>
      <li><a href="${context}/error-sample.htm">カスタムエラーページ</a></li>
    </ul>
  </td>
  <td valign="top" class="main">
    <div class="menu">
      [<a href="source-viewer.htm?filename=/jsp-example.jsp">JSP</a>]
      [<a href="source-viewer.htm?filename=${sourcePath}">Java</a>]
    </div>
    <jsp:include page='${forward}'/>
  </td>
</tr>
</table>
</body>
</html>