/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var $playerNameInput = $('#txtPlayerName');
var $waitingGamesArea = $('.waiting-games-area');
var waitingGamesIntervals;



var joinGame = function (gameName, playerName) {
    var url = 'tests/joinGame';
    $.ajax({
        url: url,
        data: {'playerName': playerName, 'gameName': gameName}
    }).success(function (data) {
        if (data.status !== 'Error') {
            window.location = 'game';
        }
        else {
            alert('Failed to join game.');
        }
    }).error(function(){
        alert('Failed to join game.');
    });
};

var getWaitingGamesHTML = function () {
    var url = 'tests/getWaitingGamesHTML';

    $.ajax({url: url}).success(function (data) {
        $waitingGamesArea.children('.list').html(data);
    }).error(function(){
        $waitingGamesArea.children('.list').html("Oops, Something went Wrong...");
    });
};

var showJoinForm = function(place, gameName){
    var url = 'tests/getJoinForm';
    $.ajax({'url': url, 'data': {'gameName': gameName}}).success(function (data) {
        $(place).parent().html(data);
    }).error(function(){
        $(place).parent().html("Oops, Something went Wrong...");
        $('.waiting-games-area .list .li').removeClass("clicked");
    });
};

$(document).ready(function () {
    $('#refreshWaitingGamesList').on('click', function(){
        getWaitingGamesHTML();
    });
    
    getWaitingGamesHTML();
    
    $(document).on('click', '.waiting-games-area li>div', function(event){
        var name = $(this).text();
        $('.waiting-games-area .li').removeClass("clicked");
        $(this).addClass("clicked");
        showJoinForm($(this), name);
    });
    
    $(document).on('submit', '.waiting-games-area form', function(e){
        e.preventDefault();
        var playerName = $(this).children("input[name='txtPlayerName']").val();
        var gameName = $(this).data("gamename");
        joinGame(gameName, playerName);
    });

    $(document).on('click', '#joinGame form', function (e) {
        e.preventDefault();
        joinGame();
    });
});
