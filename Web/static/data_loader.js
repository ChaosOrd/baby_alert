

function retrieveData() {
    $.get("/measurements", drawCurveTypes);
}

function drawCurveTypes(data, status) {
    var graphData = new google.visualization.DataTable();
    graphData.addColumn('datetime', 'Time');
    graphData.addColumn('number', 'Temperature');

    $.each(data.measurements, function(idx, measurement) {
        var measurementTime = new Date(measurement.time);
        graphData.addRow([measurementTime, measurement.temperature, measurement.humidity]);
    });

    var temperatureView =  new google.visualization.DataView(graphData);
    temperatureView.setColumns([1]);
    var options = {
    hAxis: {
      title: 'Time'
    },
    vAxis: {
      title: 'Temperature'
    },
    series: {
      1: {curveType: 'function'}
    }
    };

    var chart = new google.visualization.LineChart($('#temperatureChart'));
    chart.draw(temperatureView, options); */
}


$(document).ready(function() {
    google.charts.load('current', {packages: ['corechart', 'line']});
    google.charts.setOnLoadCallback(retrieveData);
    retrieveData();
});