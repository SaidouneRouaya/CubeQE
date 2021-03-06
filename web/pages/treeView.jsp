
<!-- Styles -->
<style>
    #chartdiv {
        width: 100%;
        max-width: 100%;
        height:550px;
    }
</style>

<!-- Resources -->
<script src="../plugins/amcharts4/core.js"></script>
<script src="../plugins/amcharts4/charts.js"></script>
<script src="../plugins/amcharts4/plugins/forceDirected.js"></script>
<script src="../plugins/amcharts4/themes/animated.js"></script>

<!-- Chart code -->
<script>
    am4core.ready(function() {

// Themes begin
        am4core.useTheme(am4themes_animated);
// Themes end



        var chart = am4core.create("chartdiv", am4plugins_forceDirected.ForceDirectedTree);
        //chart.legend = new am4charts.Legend(); // legends of the graph

        var networkSeries = chart.series.push(new am4plugins_forceDirected.ForceDirectedSeries());


        networkSeries.data = ${models.toJSONString()}



       // networkSeries.dataFields.linkWith = "linkWith";
        networkSeries.dataFields.name = "name";
        networkSeries.dataFields.id = "id";
        networkSeries.dataFields.value = "value";
        networkSeries.dataFields.children = "children"; // Permet de rajouter les relations avec les fils
        networkSeries.dataFields.color = "color";

        networkSeries.nodes.template.tooltipText = "Property : {name}\nObject : {id}";// Représente l'étiquette qui sort de la bulle
        networkSeries.nodes.template.fillOpacity = 1;


        networkSeries.nodes.template.label.text = "{id}";// Représente le nom dans la bulle
        networkSeries.fontSize = 15;
        networkSeries.maxLevels = 3;
        //networkSeries.maxRadius = am4core.percent(10);
        networkSeries.manyBodyStrength = -16;
        networkSeries.nodes.template.label.hideOversized = true;
        networkSeries.nodes.template.label.truncate = true;



    }); // end am4core.ready()
</script>

<!-- HTML -->
<div id="chartdiv"></div>

