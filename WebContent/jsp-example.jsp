<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link href="${context}/style.css" rel="stylesheet" type="text/css" media="all">
  ${imports}
  <title>JSPのサンプル</title>
</head>
<body>
<table class="contents" cellspacing="0">
<tr><td colspan="2" class="app-title">S2Click Examples</td></tr>
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
    </ul>
  </td>
  <td valign="top" class="main">
    <div class="menu">
      [<a href="source-viewer.htm?filename=/jsp-example.jsp">JSP</a>]
      [<a href="source-viewer.htm?filename=/WEB-INF/classes/org/seasar/s2click/example/page/JSPExamplePage.java">Java</a>]
    </div>
    ${form}
  </td>
</tr>
</table>
</body>
</html>