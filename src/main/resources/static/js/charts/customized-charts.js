 /* <![CDATA[*/
        var partyNamesList = [[${partiesNamesList}]];
        var partiesBallotsCount =  [[${ballotsCountList}]];
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
                        text:'Графика - брой гласове на партия'
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
        var basiclinechart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: ["9:00", "10:00", "11:00", "12:00", "13:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"],
                datasets: [{
                    label: "Гласове през първи ден на кампанията.",
                    fill: false,
                    backgroundColor: '#00c292',
                    borderColor: '#00c292',
                    data: [16, 344, 445, 442, 155, 820, 433, 20, 150, 150, 3]
                }, {
                    label: "Гласове през втори ден на кампанията.",
                    fill: false,
                    backgroundColor: '#fb9678',
                    borderColor: '#fb9678',
                    data: [12, 34, 45, 42, 100, 80, 43, 200, 150, 300, 500]

            }]
            },
            options: {
                responsive: true,
                title:{
                    display:true,
                    text:'Графика за гласове по час и ден'
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

    /*]]>*/
