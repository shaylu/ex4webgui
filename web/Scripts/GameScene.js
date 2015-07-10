/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var GameData = function (logTextarea) {
    
    var players;
    var log = logTextarea;
    var lastHandledEvent = 0;

    var getEvents = function () {
        $.ajax({
            url: 'gamedata',
            data: {'type': 'events', 'lastID': lastHandledEvent}
        }).success(function (data) {
            $(log).append('proccessing events...');
            processEvents(data);
        }).error(function () {
            $(log).append('failed to get events data.');
        });
    };

    var processEvent = function (event) {
        var type = event.type;
        switch (type) {
            case 'GameStart':
                gameStarted();
                break;
            case 'GameOver':
                gameOver();
                break;
            case 'WinningNumber':
                winningNumber();
                break;
            case 'ResultsScores':
                playerWon();
                break;
            case 'PlayerResigned':
                playerResigned();
                break;
            case 'PlayerBet':
                playerBet();
                break;
            case 'PlayerFinishedBetting':
                playerFinishedBetting();
                break;
            default:
                break;
        }

        lastHandledEvent = event.id;
    };

    var processEvents = function (data) {
        for (var i = 0; i < data.length; i++) {
            processEvent(data[i]);
        }
    };

    var gameStarted = function () {
        $.ajax({
            url: 'gamedata',
            data: {'type': 'players'}
        }).success(function (data) {
            players = createAllPlayers(data);
        }).error(function () {
            $(log).append('failed to get events data.');
        });
    };
    
    var createAllPlayers = function(data){
        var array;
        for (var i = 0; i < data.length; i++) {
            var name = data.name;
            var money = data.money;
            var player = new Player(name, money);
           
            array[i] = player;
            addPlayerToGUI(player);
        }
        return array;
    };
    
    var addPlayerToGUI = function(player){
        var html = createPlayerUI(player.name, player.money);
        $('.players-div').append(html);
    };

    var createPlayerUI = function (name, money) {
        var result = "";
        result += "<div class='player' name='" + name + "'>";
        result += " <div class='playerName'>" + name + "</div>";
        result += " <div class='playerMoney'>" + money + "</div>";
        result += "<div>";
        return result;
    };
    
    var Player = function(playerName, playerMoney){
        var name = playerName;
        var money = playerMoney;
        
        var updateMoney = function(newMoney){
            $("div.player[name='" + playerName +"'] div.playerMoney").text(newMoney.toString());
        };
        var disable = function(){
            $("div.player[name='" + playerName +"']").css({ opacity: 0.5 });
        };
        
        var enable = function(){
            $("div.player[name='" + playerName +"']").css({ opacity: 1 });
        };
    };
};

var $logTextarea = $('#txtLog');
var game = new GameData($logTextarea);

$(function () {

    var hotspots = $(".hotspot");
    hotspots.click(function (event) {
        var spot = this;
        var bettype = $(spot).data("bettype");
        var numbers = $(spot).data("numbers");
        console.log(bettype + " " + numbers);
        alert(bettype + " " + numbers);
    });

    var rot = function (obj) {
        $(obj).animate({rotation: 360}, {duration: 500, step: function (now, fx) {
                $(this).css({"transform": "rotate(" + now + "deg)"});
            }});
    };

    $('img').click(rot(this));

    $('').click(function () {
        getEvents();
    })


});

