// APPLY FOR LICENSE!
// https://canvasjs.com/apply/student/

window.onload = function () {

$.each(PCD, function(key, value) {
    pieChartData.push({y: (key/totalVoteCount) * 100, name: value,exploded:true});
});
if (pieChartData.length < 2) {
	pieChartData = [
		{ y: 10, name: "ГЕРБ", exploded: true },
		{ y: 21, name: "БСП" },
		{ y: 30, name: "БЗНС" },
		{ y: 30, name: "БСК" },
		{ y: 3, name: "СДС" },
		{ y: 3, name: "ВМРО" },
		{ y: 3, name: "НИЕ"}
	];
}
var chart = new CanvasJS.Chart("chartContainer", {
	exportEnabled: true,
	animationEnabled: true,
	title:{
		text: "Процент гласове по партия"
	},
	legend:{
		cursor: "pointer",
		itemclick: explodePie
	},
	data: [{
		type: "pie",
		showInLegend: true,
		toolTipContent: "{name}: <strong>{y}%</strong>",
		indexLabel: "{name} - {y}%",
        dataPoints: pieChartData
	}]
});
chart.render();
};
     var container = document.getElementById("chartContainer");
        var content = document.createElement("span");
                container.appendChild(pieChartData);
document.write(pieChartData);
function explodePie (e) {
	if(typeof (e.dataSeries.dataPoints[e.dataPointIndex].exploded) === "undefined" || !e.dataSeries.dataPoints[e.dataPointIndex].exploded) {
		e.dataSeries.dataPoints[e.dataPointIndex].exploded = true;
	} else {
		e.dataSeries.dataPoints[e.dataPointIndex].exploded = false;
	}
	e.chart.render();

}
