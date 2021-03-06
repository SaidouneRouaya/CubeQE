<%--
  Created by IntelliJ IDEA.
  User: pc
  Date: 21/04/2019
  Time: 16:43
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html>
<head>


    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>LogLinc | Scenario 2 </title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.7 -->
    <link rel="stylesheet" href="../bower_components/bootstrap/dist/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="../bower_components/font-awesome/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="../bower_components/Ionicons/css/ionicons.min.css">
    <!-- jvectormap -->
    <link rel="stylesheet" href="../bower_components/jvectormap/jquery-jvectormap.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="../dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="../dist/css/skins/_all-skins.min.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- Google Font -->
    <link rel="stylesheet"
          href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <!-- contains the header -->
    <%@ include file="header.jsp" %>

    <%@ include file="menu.jsp" %>

    <!-- Left side column. contains the logo and sidebar -->

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                Graphs Exploration
                <small>Version 1.0</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li class="active">Graphs Exploration</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <!-- Main row -->
            <div class="row">

                <div class="col-md-12">
                    <!-- TABLE: LATEST ORDERS -->


                    <!-- box -->
                    <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">Results of MD graph construction</h3>

                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                        class="fa fa-minus"></i>
                                </button>

                            </div>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="table-responsive">
                                <table class="table no-margin">
                                    <thead>
                                    <tr>

                                        <th>Number of queries</th>
                                        <th>Number of queries constructed</th>
                                        <th>Number of queries non constructed</th>
                                        <th>Construction time</th>
                                        <th>Percentage of constructed queries</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>

                                        <td>${queriesNumbersMap.get("Syntactical_Validation")}</td>
                                        <td>${queriesNumbersMap.get("ConstructMSGraphs_nbQueriesConstructed")}</td>
                                        <td>${queriesNumbersMap.get("Syntactical_Validation")-queriesNumbersMap.get("ConstructMSGraphs_nbQueriesConstructed")}</td>
                                        <td>
                                            <div class="text-muted">
                                                <i class="fa fa-clock-o"></i>   ${timesMap.get("ConstructMSGraphs")} seconds
                                            </div>
                                        </td>

                                        <td><span class="badge bg-red">
                                 <fmt:formatNumber value="${queriesNumbersMap.get('ConstructMSGraphs_nbQueriesConstructed')
                                 / queriesNumbersMap.get('Syntactical_Validation')*100}" maxFractionDigits="2"/>
                                    %</span></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <!-- /.table-responsive -->
                        </div>
                        <!-- /.box-body -->
                        <div class="box-footer clearfix">
                         <!--   <a href="javascript:void(0)" class="btn  btn-default bg-purple-gradient pull-right">Construct
                                MD graphs</a>-->
                        </div>
                        <!-- /.box-footer -->
                    </div>
                    <!-- /.box -->
                    <!-- *************************************** -->

                    <!-- box -->
                    <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">Results of Execution</h3>

                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                        class="fa fa-minus"></i>
                                </button>
                                <button type="button" class="btn btn-box-tool" data-widget="remove"><i
                                        class="fa fa-times"></i></button>
                            </div>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="table-responsive">
                                <table class="table no-margin">
                                    <thead>
                                    <tr>

                                        <th>Number of queries executed</th>
                                        <th>Nb queries with non null models returned</th>
                                        <th>Nb queries with null models returned</th>
                                        <th>Number of queries non executed</th>
                                        <th>Execution time</th>
                                        <th>Percentage of executed queries</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>

                                        <td>${queriesNumbersMap.get("ConstructMSGraphs_nbQueriesConstructed")}</td>
                                        <td>${queriesNumbersMap.get("Execution_nbQueriesExecutedWithModels")}</td>
                                        <td>${queriesNumbersMap.get("Execution_nbQueriesExecutedWithNullModels")}</td>
                                        <td>${queriesNumbersMap.get("Execution_nbQueriesNonExecuted")}</td>
                                        <td>
                                            <div class="text-muted">
                                                <i class="fa fa-clock-o"></i>  ${timesMap.get("Execution")} seconds
                                            </div>
                                        </td>
                                        <td><span class="badge bg-yellow">
                                                <fmt:formatNumber
                                                        value=" ${((queriesNumbersMap.get('Execution_nbQueriesExecutedWithModels')
                                                        + queriesNumbersMap.get('Execution_nbQueriesExecutedWithNullModels'))
                                                        / queriesNumbersMap.get('ConstructMSGraphs_nbQueriesConstructed'))*100} "
                                                        maxFractionDigits="2"/>

                                                %</span></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <!-- /.table-responsive -->
                        </div>
                        <!-- /.box-body -->
                        <div class="box-footer clearfix">
                            <!--<a href="javascript:void(0)" class="btn  btn-default bg-purple-gradient pull-right">Consolidate
                                MD graphs</a>-->
                        </div>
                        <!-- /.box-footer -->
                    </div>
                    <!-- /.box -->
                    <!-- *************************************** -->
                    <!-- box -->
                    <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">Results of consolidation</h3>

                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i
                                        class="fa fa-minus"></i>
                                </button>
                                <button type="button" class="btn btn-box-tool" data-widget="remove"><i
                                        class="fa fa-times"></i></button>
                            </div>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="table-responsive">
                                <table class="table no-margin">
                                    <thead>
                                    <tr>

                                        <th>Number of models</th>
                                        <th>Number of models after consolidation</th>
                                        <th>Execution time</th>
                                        <th>Percentage decrease</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>${queriesNumbersMap.get("Consolidation_nbModelsB4Consolidation")}</td>
                                        <td>${queriesNumbersMap.get("Alleviation_nbModels")}</td>
                                        <td>
                                            <div class="text-muted">
                                                <i class="fa fa-clock-o"></i>    ${timesMap.get("Consolidation")
                                                +timesMap.get("Alleviation_UselessProperties")+timesMap.get("Alleviation")} seconds
                                            </div>
                                        </td>
                                        <td>
                                            <span class="badge bg-light-blue">
                                                 <fmt:formatNumber
                                                         value="${( (queriesNumbersMap.get('Consolidation_nbModelsB4Consolidation')- queriesNumbersMap.get('Alleviation_nbModels') )/ queriesNumbersMap.get('Consolidation_nbModelsB4Consolidation'))*100}"
                                                         maxFractionDigits="2"/>
                                            %</span></td>

                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <!-- /.table-responsive -->
                        </div>
                        <!-- /.box-body -->
                        <div class="box-footer clearfix">
                            <a href="subjectsBlocks.j" class="btn  btn-default bg-purple-gradient pull-right">Annotation</a>
                        </div>
                        <!-- /.box-footer -->
                    </div>
                    <!-- /.box -->

                </div>
                <!-- /.col -->


                <!-- /.col-->

            </div>
            <!-- /.row -->
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->


    <%@ include file="footer.jsp" %>


    <%@ include file="menu-side.jsp" %>


    <!-- Add the sidebar's background. This div must be placed
         immediately after the control sidebar -->
    <div class="control-sidebar-bg control-sidebar-dark "></div>

</div>
<!-- ./wrapper -->

<!-- jQuery 3 -->
<script src="../bower_components/jquery/dist/jquery.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<script src="../bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<!-- FastClick -->
<script src="../bower_components/fastclick/lib/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="../dist/js/adminlte.min.js"></script>
<!-- Sparkline -->
<script src="../bower_components/jquery-sparkline/dist/jquery.sparkline.min.js"></script>
<!-- jvectormap  -->
<script src="../plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
<script src="../plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
<!-- SlimScroll -->
<script src="../bower_components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<!-- ChartJS -->
<script src="../bower_components/chart.js/Chart.js"></script>
<!-- AdminLTE dashboard demo (This is only for demo purposes) -->
<script src="../dist/js/pages/dashboard2.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="../dist/js/demo.js"></script>
</body>
</html>
