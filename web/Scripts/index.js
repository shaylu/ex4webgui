/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var $errorDiv = $('#joinError');
var $joinDiv = $('#joinGame');
var waitingGamesIntervals;

var canCreateAGame = function () {
    var url = 'tests/getGameDetails';
    $.ajax({
        url: url
    }).success(function (data) {
        var res;
        
        if (data.status !== 'Error') {
            // a game is initialized
            var status = data.status;
            if (status === 'FINISHED')
                res = true;
            else
                res = false;
        }
        else {
            res = true;
        }
        
        if (res === true) {
            $('#gameDetails').text("No waiting games but you can create one!");
        }
        else {
            $('#gameDetails').text("No waiting games, please wait for a game to finish.");
        }

        $joinDiv.hide();
    });
};

var getWaitingGames = function () {
    var url = 'tests/getWaitingGames';

    var noWaitingGames = function () {
        canCreateAGame();
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
        }
        ;
    });
};

var joinGame = function (name){
    var url = 'tests/joinGame';
    
    $.ajax({
        url: url,
        data: { 'name' : name}
    }).success(function (data) {
        
    });
}

$(document).ready(function () {
    waitingGamesIntervals = setInterval(getWaitingGames, 5000);
    getWaitingGames();

    $('#frmJoinGame').submit(function () {
        var name = $('#txtPlayerName').val();
        joinGame(name);
        return false;
    });
//
//    $('#btnRefreshGameDetails').on('click', function () {
//        getGameDetails();
//    });
});
