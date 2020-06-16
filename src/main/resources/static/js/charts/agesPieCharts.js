// window.onload = function generatePieChartForAges() {
//
//     $.each(agesPCD, function(key, value) {
//         agesPieChartData.push({y: (key/value) * 100, name: value,exploded:true});
//     });
//     if (agesPieChartData.length < 2) {
//         agesPieChartData = [
//             { y: 10, name: "2", exploded: true },
//             { y: 21, name: "3" },
//             { y: 30, name: "4" },
//             { y: 30, name: "5" },
//             { y: 3, name: "1" },
//             { y: 3, name: "3" },
//             { y: 3, name: "9"}
//         ];
//     }
//     var chart2 = new CanvasJS.Chart("agesChartContainer", {
//         exportEnabled: true,
//         animationEnabled: true,
//         title:{
//             text: "Процент гласове по възраст"
//         },
//         legend:{
//             cursor: "pointer",
//             itemclick: explodePie
//         },
//         data: [{
//             type: "pie",
//             showInLegend: true,
//             toolTipContent: "{name}: <strong>{y}%</strong>",
//             indexLabel: "{name} - {y}%",
//             dataPoints: agesPieChartData
//         }]
//     });
//     chart2.render();
// };
// var container2 = document.getElementById("agesChartContainer");
// var content = document.createElement("span");
// container2.appendChild(agesPieChartData);
// document.write(agesPieChartData);
// function explodePie (e) {
//     if(typeof (e.dataSeries.dataPoints[e.dataPointIndex].exploded) === "undefined" || !e.dataSeries.dataPoints[e.dataPointIndex].exploded) {
//         e.dataSeries.dataPoints[e.dataPointIndex].exploded = true;
//     } else {
//         e.dataSeries.dataPoints[e.dataPointIndex].exploded = false;
//     }
//     e.chart2.render();
// }