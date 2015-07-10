/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//    WAITING,
//    ACTIVE,
//    FINISHED;
var $errorDiv = $('#joinError');

var joinGame = function (name) {
    $errorDiv.hide();
    $.ajax({
        url: 'joingame',
        data: {"name": name}
    }).success(function (data) {
        if (data !== undefined && data.status === 'Success') {
            window.location = 'game';
        }
        else {
            $errorDiv.text(data.message);
            $errorDiv.show();
        }
    }).error(function () {
        $errorDiv.text("Oops somthing went wrong.");
        $errorDiv.show();
    });
}
var getGameDetails = function () {
    $.ajax({
        url: 'gamedata?type=gameDetails'
    }).success(function (data) {
        if (data.status === undefined) {
            $('#gameDetails').text("Error with ajax response.");
            $('#joinGame').hide();
        }
        if (data.status === 'WAITING') {
            $('#gameDetails').text("Game initialized and waiting for players.");
            $('#joinGame').show();
        }
        else if (data.status === 'ACTIVE') {
            $('#gameDetails').text("Game running.");
            $('#joinGame').hide();
        }
        else if (data.status === 'FINISHED') {
            $('#gameDetails').text("Game finished. you can start a new game.");
            $('#joinGame').hide();
        }
        else {
            $('#gameDetails').text("Game wasn't initialized yet.");
            $('#joinGame').hide();
        }

    }).error(function () {
        $('#gameDetails').text("Error while trying to get game data.");
        $('#joinGame').hide();
    });
};
$(document).ready(function () {
//    var gameDetailsIntervals = setInterval(getGameDetails, 5000);
//    getGameDetails();

    $('#frmJoinGame').submit(function () {
        var name = $('#txtPlayerName').val();
        joinGame(name);
        return false;
    });

    $('#btnRefreshGameDetails').on('click', function () {
        getGameDetails();
    });
});
