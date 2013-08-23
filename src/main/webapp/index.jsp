<%--
  Created by IntelliJ IDEA.
  User: gbegley
  Date: 8/22/13
  Time: 11:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>QRestic Demo</title>
    <style>
        body {padding:50px;padding-left:250px;}
        .right-panel {float:right;position: absolute;top:50;right:50;width:120;padding:10px 5px;background: #eee;border:1px groove slategray;}
        h1, h2, h3, h4 { color:#555; width:55%;padding-bottom: 8px;padding-left:15px; margin-bottom: 0px;}
        h1, h2, h3 { border-bottom: 2px inset #666699;}
        p {padding-left: 25px;color: #333;border: 1px solid #eee;padding:15px;font:normal 14px Helvetica; width:55%;}
    </style>
  </head>
  <body>

  <h1>QRestic - Query restfully</h1>


  <h2>Models</h2>

      <h3>Input</h3>
      <p>Request made of the system</p>
      <h4>GET</h4><p>Access to results by identity, or by a parameter relation</p>

      <h4>POST</h4><p>GET + ???</p>

      <h4>PUT</h4><p>Add items</p>

      <h4>DELETE</h4><p>Remove items, either by identity or a parameter relation</p>

      <h3>Ouptut</h3>
      <p>Row Set, or system response</p>


  <h2>Views</h2>

      <h4>CSV</h4>
      <h4>JSON</h4>
      <h4>XML</h4>
      <h4>XML</h4>

  <h2>Controller</h2>


  <h3>Request</h3>
  <p>
      *from source where criteria, get RowSet*
  </p>

  <h3>View</h3>
  <p>
      Process RowSet, generating content
  </p>

  <div class="right-panel">
      <h2>Links</h2>
      <ul>
          <li><a href="/data/data/table/job">Jobs</li>
          <li><a href="/data/data/table/entity">Entity</li>
          <li><a href="/data/data/table/matrix">Matrix</li>
      </ul>
  </div>

  </body>
</html>