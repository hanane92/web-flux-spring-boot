let courbes= [];
var chart = new SmoothieChart();
chart.streamTo(document.getElementById("chart"),500);
var colors = [
    {strokeStyle: 'rgba(255, 0, 0, 1)',fillStyle: 'rgba(255, 0, 0, 0.2)',lineWidth: 2},
    {strokeStyle: 'rgba(0, 255, 0, 1)',fillStyle: 'rgba(0, 255, 0, 0.2)',lineWidth: 2},
    {strokeStyle: 'rgba(0, 0, 255, 1)',fillStyle: 'rgba(0, 0, 255, 0.2)',lineWidth: 2}
]

let index = -1;
function onConnect(id) {
    if(courbes[id]==undefined){
        courbes[id] = new TimeSeries();
        let color = colorRandom();
        chart.addTimeSeries(courbes[id], color);

        var connection= new EventSource("/events/"+id); //etabir connexion avec sreveur
        connection.onmessage = function(resp) { //pr chaque message je recois j'attend des events de la part du serveur
            var transaction = JSON.parse(resp.data);//parser la reponse en JSON [il est du format TEXT]
            courbes[id].append(new Date().getTime(),transaction.price);
        };

    }

    function colorRandom() {

        index++;
        if(index>=colors.length) index = 0;
        return colors[index];
    }

}