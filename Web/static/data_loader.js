

function retrieveData() {
    $.get("/measurements", drawCurveTypes);
}

function drawCurveTypes(data, status) {
    var graphData = new google.visualization.DataTable();
    graphData.addColumn('datetime', 'Time');
    graphData.addColumn('number', 'Temperature');
    graphData.addColumn('number', 'Humidity');

    $.each(data.measurements, function(idx, measurement) {
        var measurementTime = new Date(measurement.time +
            measurement.time.getTimezoneOffset.getTimezoneOffset()*60*1000);
        graphData.addRow([measurementTime, Number(measurement.temperature), Number(measurement.humidity)]);
    });

    var temperatureView =  new google.visualization.DataView(graphData);
    temperatureView.setColumns([0, 1]);
    var options = {
        vAxis: {
            title: 'Temperature'
        },
        series: {
            1: {curveType: 'function'}
        },
        width: 1900,
        height: 1000
    };

    var chart = new google.visualization.LineChart(document.getElementById('temperatureChart'));
    chart.draw(temperatureView, options);
}


$(document).ready(function() {
    google.charts.load('current', {packages: ['corechart', 'line']});
    google.charts.setOnLoadCallback(retrieveData);
    retrieveData();
});