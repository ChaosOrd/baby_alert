

function retrieveData() {
    $.get("/measurements", drawCurveTypes);
}

function drawCurveTypes(data, status) {
    var graphData = new google.visualization.DataTable();
    graphData.addColumn('datetime', 'Time');
    graphData.addColumn('number', 'Temperature');
    graphData.addColumn('number', 'Humidity');

    $.each(data.measurements, function(idx, measurement) {
        var measurementTimeUtc = new Date(measurement.time);
        var measurementTime = new Date(measurementTimeUtc.getTime() -
            measurementTimeUtc.getTimezoneOffset()*60*1000)
        graphData.addRow([measurementTime, Number(measurement.temperature), Number(measurement.humidity)]);
    });

    drawTemperatureChart(graphData);
    drawHumidityChart(graphData);

function drawTemperatureChart(graphData) {
    var temperatureView =  new google.visualization.DataView(graphData);
    temperatureView.setColumns([0, 1]);
    var options = {
        series: {
            1: {curveType: 'function'}
        },
        width: 1900,
        height: 1000
    };

    var chart = new google.visualization.LineChart(document.getElementById('temperatureChart'));
    chart.draw(temperatureView, options);
}

function drawHumidityChart(graphData) {
    var humidityView = new google.visualization.DataView(graphData);
    humidityView.setColumns([0, 2]);
        var options = {
        series: {
            1: {curveType: 'function'}
        },
        width: 1900,
        height: 1000
    };

    var chart = new google.visualization.LineChart(document.getElementById('humidityChart'));
    chart.draw(humidityView, options);
}

$(document).ready(function() {
    google.charts.load('current', {packages: ['corechart', 'line']});
    google.charts.setOnLoadCallback(retrieveData);
    retrieveData();
});