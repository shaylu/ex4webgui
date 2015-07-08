/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//    WAITING,
//    ACTIVE,
//    FINISHED;
var getGameDetails = function () {
    $.ajax({
        url: 'gamedata?type=gameDetails'
    }).success(function (data) {
        if (data.status === 'WAITING') {
            $('#gameDetails').text("Game initialized and waiting for players.");
        }
        else if (data.status === 'ACTIVE') {
            $('#gameDetails').text("Game running.");
        }
        else if (data.status === 'FINISHED') {
            $('#gameDetails').text("Game finished. you can start a new game.");
        }
        else {
            $('#gameDetails').text("Game wasn't initialized yet.");
        }

    }).error(function () {
        $('#gameDetails').text("Error while trying to get game data.");
    });
};

$(document).ready(function () {
//    var gameDetailsIntervals = setInterval(getGameDetails, 5000);
//    getGameDetails();

    $('#btnRefreshGameDetails').on('click', function () {
        getGameDetails();
    });
});
