<%@ tag description="Page without navbar and footer" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ attribute name="jsIncludes" fragment="true" %>
<%@ attribute name="header" fragment="true" %>
<%@ attribute name="bodyTitle" required="true" %>

<!DOCTYPE html>
<html>
<head>
    <title>${pageTitle}</title>

    <link rel="shortcut icon" href="/favicon.png" />

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link type="text/css" href="/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
    <link type="text/css" href="/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet"/>
    <link type="text/css" href="/stylesheets/teammatesCommon.css" rel="stylesheet"/>

    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script type="text/javascript" src="/js/googleAnalytics.js"></script>
    <script type="text/javascript" src="/js/jquery-minified.js"></script>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
    <script type="text/javascript" src="/js/common.js"></script>
    <script type="text/javascript" src="/bootstrap/js/bootstrap.min.js"></script>

    <t:enableJS />
    
    <jsp:invoke fragment="jsIncludes" />    
</head>
<body>
    <jsp:invoke fragment="header" />
    <div class="container" id="frameBodyWrapper">
        <t:bodyHeader title="${bodyTitle}" />
        <jsp:doBody />
    </div>
</body>
</html>