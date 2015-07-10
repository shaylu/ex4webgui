/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var $errorDiv = $('#joinError');
var $joinDiv = $('#joinGame');


var canCreateAGame = function (){
    var url = 'tests/getGameDetails';
    $.ajax({
        url: url
    }).success(function (data) {
        if (data.status !== 'Error') {
            // a game is initialized
            var status = data.status;
            if (status === 'FINISHED')
                return true;
            else 
                return false;
        }
        else {
            return true;
        };
    });
};


var getWaitingGames = function () {
    var url = 'tests/getWaitingGames';

    var noWaitingGames = function () {
        var canUserCreateNewGame = canCreateAGame();
        if (canUserCreateNewGame === true){
            $('#gameDetails').text("No waiting games but you can create one!");
        }
        else {
             $('#gameDetails').text("No waiting games, please wait for a game to finish.");
        }
        
        $joinDiv.hide();
    };

    var waitingGameExist = function () {
        $('#gameDetails').text("There is a waiting game for you!");
        $joinDiv.show();
    };

    $.ajax({
        url: url
    }).success(function (data) {
        if (data === null || data === undefined || data.length === 0) {
            // no waiting games
            noWaitingGames();
        }
        else {
            waitingGameExist();
        };
    });
};

$(document).ready(function () {
    var waitingGamesIntervals = setInterval(getWaitingGames, 5000);
    getWaitingGames();
    
//
//    $('#frmJoinGame').submit(function () {
//        var name = $('#txtPlayerName').val();
//        joinGame(name);
//        return false;
//    });
//
//    $('#btnRefreshGameDetails').on('click', function () {
//        getGameDetails();
//    });
});
