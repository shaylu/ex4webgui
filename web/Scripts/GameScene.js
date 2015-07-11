/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var $playersDiv = $('.players-div');
var $logTextarea = $('#txtLog');
var $gameArea = $('.game-area');
var $scoreboardArea = $('.scoreboard-area');
var $timerDiv = $('.timer');
var game = new GameData($logTextarea);
var timer = undefined;

function TimeSpan(ghours, gminutes, gseconds) {
    this.hours = ghours;
    this.minutes = gminutes;
    this.seconds = gseconds;

    this.tickDown = function () {
        if (this.hours === 0 && this.minutes === 0 && this.seconds === 0)
            return;

        if (this.seconds - 1 >= 0)
        {
            this.seconds = this.seconds - 1;
            return;
        }
        else {
            if (this.minutes - 1 >= 0) {
                this.minutes = this.minutes - 1;
                this.seconds = 59;
                return;
            }
            else {
                this.minutes = 59;
                this.seconds = 59;
                this.hours = this.hours - 1;
            }
        }
    };
    this.getTimeString = function () {
        var seconds;
        var minutes;
        var hours;

        if (this.seconds < 10) {
            seconds = '0' + this.seconds;
        }
        else {
            seconds = this.seconds;
        }

        if (this.minutes < 10) {
            minutes = '0' + this.minutes;
        }
        else {
            minutes = this.minutes;
        }

        if (this.hours < 10) {
            hours = '0' + this.hours;
        }
        else {
            hours = this.hours;
        }

        return (hours + ':' + minutes + ':' + seconds);
    };
}

function Timer(millseconds, div) {
    this.div = div;
    this.timespan = new TimeSpan(0, 0, millseconds / 1000);
    this.intervals = undefined;

    this.start = function () {
        var tick = function (timer) {
            timer.timespan.tickDown();
            $(timer.div).text(timer.timespan.getTimeString());
        };

        this.intervals = setInterval(tick(this), 1000);
    };

    this.stop = function () {
        clearInterval(this.intervals);
    };
}

function GameData(logTextarea) {
    this.players = [];
    this.log = logTextarea;
    this.lastHandledEvent = 0;
    this.roundsTime = 0;
    this.getPlayer = function (name) {
        var searchfor = name;
        var res = $.grep(this.players, function (elm) {
            return (elm.name === searchfor);
        });
        if (res.length > 0)
            return res[0];
        else
            return undefined;
    };
}

function Player(playerName, playerMoney) {
    this.name = playerName;
    this.money = playerMoney;
    this.updateMoneyOnUI = function (newAmount) {
        var player = this;
        var moneyDiv = $(".player[name='" + player.name + "'] .playerMoney");
        moneyDiv.text(newAmount);
    };
    this.disable = function () {
        var player = this;
        var playerDiv = $(".player[name='" + player.name + "']").addClass("player-resigned");
    };
}

var log = function (text) {
    $logTextarea.append(text + "\n");
};
var getEvents = function (gamedata) {
    var url = 'tests/getEvents';
    $.ajax({
        url: url,
        data: {'lastID': gamedata.lastHandledEvent}
    }).success(function (events) {
        log('proccessing events...');
        processEvents(gamedata, events);
    }).error(function () {
        log('failed to get events data.');
    });
};
var processEvents = function (gamedata, events) {
    for (var i = 0; i < events.length; i++) {
        processEvent(gamedata, events[i]);
    }
};

processEvent = function (gamedata, event) {
    var type = event.type;
    switch (type) {
        case 'GAME_START':
            gameStarted(gamedata, event);
            gamedata.lastHandledEvent = event.id;
            break;
        case 'GAME_OVER':
            gameOver(gamedata);
            gamedata.lastHandledEvent = event.id;
            break;
        case 'WINNING_NUMBER':
            winningNumber(event.winningNumber);
            gamedata.lastHandledEvent = event.id;
            break;
        case 'RESULTS_SCORES':
            playerWon(gamedata, event);
            gamedata.lastHandledEvent = event.id;
            break;
        case 'PLAYER_RESIGNED':
            playerResigned(gamedata, event);
            gamedata.lastHandledEvent = event.id;
            break;
        case 'PLAYER_BET':
            playerBet(gamedata, event);
            gamedata.lastHandledEvent = event.id;
            break;
        case 'PLAYER_FINISHED_BETTING':
            playerFinishedBetting(gamedata);
            gamedata.lastHandledEvent = event.id;
            break;
        default:
            break;
    }
};

var startTimer = function () {
    stopTimer();

    timer = new Timer(game.roundsTime, $timerDiv);
    timer.start();
};

var stopTimer = function () {
    if (timer === undefined) {
        return;
    }

    timer.stop();
};

var gameStarted = function (gamedata, event) {
    $gameArea.show();
    gamedata.roundsTime = event.timeout;
    // get and create players on UI
    var url = 'tests/getPlayersDetails';
    $.ajax({url: url}).success(function (playersDetails) {
        for (var i = 0, max = playersDetails.length; i < max; i++) {
            // create player and add to game data
            var name = playersDetails[i].name;
            var money = playersDetails[i].money;
            var player = new Player(name, money);
            gamedata.players[i] = player;
        }

        // finally after adding all players, create their UI
        createPlayersOnUI(gamedata.players);
        log("Game Started!");
        startTimer();
    });
};
var gameOver = function (gamedata) {

    stopTimer();
    var scoreboard = HTMLHelper.scoreBoardHTML(gamedata);
    $gameArea.hide();
    // TO DO
    $scoreboardArea.show();
    log("Game Over!");

    signout();
};

var signout = function () {
    var url = 'tests/leaveGame';
    $.ajax({url: url}).success(function (data) {
        // nothing
    });
};

var winningNumber = function (winningNumber) {
    stopTimer();

    // turn wheel 
    // TO DO

    log("Winning number is: " + winningNumber);
    // clear all bets

    startTimer();
};
var playerWon = function (gamedata, winningdata) {
    var name = winningdata.playerName;
    var earned = winningdata.amount;
    var player = gamedata.getPlayer(name);
    if (player !== undefined) {
        player.money = (player.money + earned);
        player.updateMoneyOnUI(player.money);
        log("'" + player.name + "' has won $" + earned + ".");
    }
};
var playerResigned = function (gamedata, resigndata) {
    var name = resigndata.playerName;
    var player = gamedata.getPlayer(name);
    if (player !== undefined) {
        player.disable();
        log("'" + player.name + "' resigned.");
    }
};
var playerBet = function (gamedata, betdata) {
    $()
};
var createPlayersOnUI = function (players) {
    for (var i = 0, max = players.length; i < max; i++) {
        var player = players[i];
        var html = HTMLHelper.playerHTML(player);
        $playersDiv.append(html);
    }
};
var HTMLHelper = {
    playerHTML: function (player) {
        var result = "";
        result += "<div class='player' name='" + player.name + "'>";
        result += " <div class='playerName'>" + player.name + "</div>";
        result += " <div class='playerMoney'>" + player.money + "</div>";
        result += "<div>";
        return result;
    },
    scoreBoardHTML: function (gamedata) {

    }
};

var resign = function () {
    var url = 'tests/resign';
    $.ajax({url: url}).success(function (data) {
        if (data.status === 'Error') {
            log('Resign failed, ' + data.message);
        }
        else {
            log('You have clicked on the resign button and posted a resign request successfully.');
        }
    });
};

$(function () {
    var hotspots = $(".hotspot");
    hotspots.click(function (event) {
        var spot = this;
        var bettype = $(spot).data("bettype");
        var numbers = $(spot).data("numbers");
        console.log(bettype + " " + numbers);
        alert(bettype + " " + numbers);
    });
    $('#getEvents').click(function () {
        getEvents(game);
    });

    $('#resign').click(function () {
        resign();
    });
});

