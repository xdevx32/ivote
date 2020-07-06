

(function ($) {
     "use strict";
         /*----------------------------------------*/
        /*  1.  Bar Chart
        /*----------------------------------------*/

        var partiesList = [];
        var ctx = document.getElementById("barchart1");
        var barchart1 = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: partyNamesList /*["ГЕРБ", "ВМРО", "БСП", "СДС", "АТАКА", "Марешки", "Спаси София"]*/,
                datasets: [{
                    label: 'Гласове за кандидати',
                    data: partiesBallotsCount,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgb(50,205,50, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255,99,132,1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                title:{
                    display:true,
                    text:'Общ брой гласове за партия'
                },
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero:true
                        }
                    }]
                }
            }
        });

    var ctx2 = document.getElementById("barchart2");
    var barchart2 = new Chart(ctx2, {
        type: 'bar',
        data: {
            labels:  ["18", "1", "19", "20", "21", "22", "23"],
            datasets: [{
                label: 'Гласове за кандидати',
                data: citizenAgesListCount,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgb(50,205,50, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)'
                ],
                borderColor: [
                    'rgba(255,99,132,1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            title:{
                display:true,
                text:'Избирателна активност по възраст'
            },
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero:true
                    }
                }]
            }
        }
    });

    var ctx3 = document.getElementById("barchart3");
    var barchart3 = new Chart(ctx3, {
        type: 'bar',
        data: {
            labels: ["Burgas", "Sofia", "Plovdiv", "Varna", "Dobrich", "Veliko Turnovo", "Stara Zagora"],
            datasets: [{
                label: 'Процент гласове',
                data: citiesCountList,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgb(50,205,50, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)'
                ],
                borderColor: [
                    'rgba(255,99,132,1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            title:{
                display:true,
                text:'Избирателна активност по населени места.'
            },
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero:true
                    }
                }]
            }
        }
    });
       //TODO: Pass hours and votes to fill arrays
     /*----------------------------------------*/
    /*  1.  Basic Line Chart
    /*----------------------------------------*/
    var ctx = document.getElementById("basiclinechart");
    if(ballotsTimelineListFirstYear.length == null) {
        ballotsTimelineListFirstYear = [
            16, 344, 445, 442, 155, 820, 433, 20, 150,
            150, 3, 22,12,12,24,5,6,7,8,4,3,2,23,42
        ];
    }

    if(ballotsTimelineListSecondYear.length == null) {
        ballotsTimelineListSecondYear = [
            12, 34, 45, 42, 100, 80, 43, 200, 150, 300, 500, 321,
            12, 34, 55, 22, 100, 44, 43, 200, 150, 123, 500, 321
        ];
    }
    var basiclinechart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: [
                "1:00",
                "2:00",
                "3:00",
                "4:00",
                "5:00",
                "6:00",
                "7:00",
                "8:00",
                "9:00",
                "10:00",
                "11:00",
                "12:00",
                "13:00",
                "14:00",
                "15:00",
                "16:00",
                "17:00",
                "18:00",
                "19:00",
                "20:00",
                "21:00",
                "22:00",
                "23:00",
                "24:00"
            ],
            datasets: [{
                label: "Гласове за текущата година.",
                fill: false,
                backgroundColor: '#00c292',
                borderColor: '#00c292',
                data: ballotsTimelineListFirstYear /*[16, 344, 445, 442, 155, 820, 433, 20, 150, 150, 3]*///TestData First day
            }, {
                label: "Гласове миналата година.",
                fill: false,
                backgroundColor: '#fb9678',
                borderColor: '#fb9678',
                data: ballotsTimelineListSecondYear /*[12, 34, 45, 42, 100, 80, 43, 200, 150, 300, 500]*///TestData Second day

        }]
        },
        options: {
            responsive: true,
            title:{
                display:true,
                text:'Активност по ден час и година. ' + dateOfVote
            },
            tooltips: {
                mode: 'index',
                intersect: false,
            },
            hover: {
                mode: 'nearest',
                intersect: true
            },
            scales: {
                xAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: 'Час за подаден вот'
                    }
                }],
                yAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: 'Брой гласували'
                    }
                }]
            }
        }
    });

    })(jQuery);





var canvas=document.getElementById("basiclinechart");
var canvas2=document.getElementById("barchart1");

function printLineChart()
{
    printJS({printable: document.querySelector("#basiclinechart").toDataURL(), type: 'image', imageStyle: 'width:100%'});
}

function printBarChart(){
    printJS({printable: document.querySelector("#barchart1").toDataURL(), type: 'image', imageStyle: 'width:100%'});
}

function printBarChartАгес(){
    printJS({printable: document.querySelector("#barchart2").toDataURL(), type: 'image', imageStyle: 'width:100%'});
}



function printBothCharts(){
//    var win=window.open();
//    win.document.write("<br><img src='"+canvas.toDataURL()+"'/>");
//    win.document.write("<br><img src='"+canvas2.toDataURL()+"'/>");
//    win.focus();
//    win.print();
     printJS({
      printable: [document.querySelector("#basiclinechart").toDataURL(), document.querySelector("#barchart1").toDataURL()],
      type: 'image',
      header: 'Всички доклади',
      imageStyle: 'width:100%;margin-bottom:20px;'
     });
}