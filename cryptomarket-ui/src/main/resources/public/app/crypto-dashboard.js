var websocket;
var selectedMarket = 'BTC';
var websocketEchoServerUri = "ws://localhost:8080/markets";
var chartData = [];

var chart = AmCharts.makeChart("chartdiv", {
    "type": "serial",
    "theme": "light",
    "dataDateFormat": "YYYY-MM-DD'T'HH:mm:ss",
    "valueAxes": [{
        "id": "v1",
        "position": "left"
    }],
    "graphs": [{
        "id": "g1",
        "bullet": "round",
        "bulletSize": 8,
        "lineColor": "#d1655d",
        "lineThickness": 2,
        "negativeLineColor": "#637bb6",
        "type": "smoothedLine",
        "valueField": "value",
        "balloonText": "[[category]]: [[value]]"
    }],
    "categoryField": "timestamp",
    "categoryAxis": {
        "parseDates": true,
        "equalSpacing": true,
        "dashLength": 1,
        "minorGridEnabled": true
    },
    "dataProvider": chartData
});

websocket = initWebSocket(websocketEchoServerUri);

function initWebSocket(wsUri) {
    var ws = new WebSocket(wsUri);
    ws.onopen = onConnect;
    //ws.onclose = onClose;
    //ws.onerror = onError;
    ws.onmessage = updateChart;
    return ws;
}

function onConnect(wsEvent) {
    console.log("Server connection successful. Listening for data now.");
    websocket.send(selectedMarket);
}

function updateChart(wsEvent) {
    var newData = JSON.parse(wsEvent.data);
    console.log(wsEvent.data);
    chartData.push(newData);
    if (chartData.length > 50) {
        chartData.splice(0, chartData.length - 50);
    }
    $("#current-price").html(newData.value);
    chart.validateData();
}

$(".dropdown-menu.coin li").click((e) => {
    e.preventDefault();
    websocket.close();
    chartData.splice(0, chartData.length);
    chart.validateData();
    selectedMarket = $(e.currentTarget).attr("data-coin");
    $("#coin-icon").attr("src", "assets/img/" + selectedMarket + ".png");
    websocket = initWebSocket(websocketEchoServerUri);
});