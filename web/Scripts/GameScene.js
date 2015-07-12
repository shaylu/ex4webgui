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
var $roulettImage = $('.roulette');
var game = new GameData($logTextarea);
var timer = undefined;
var eventsIntervals;

var getCurrentPlayer = function () {
    return $('.game-area').data('playername');
};

var getRouletteType = function () {
    return $('.game-area').data('roulettetype');
};

var getRotateDegree = function (num) {
    var american = [181, 11, 190, 48, 228, 87, 266, 124, 303, 162, 341, 134, 314, 20, 199, 58, 238, 95, 274, 294, 115, 256, 77, 218, 39, 331, 152, 350, 172, 322, 143, 285, 104, 247, 67, 209, 29, 0];
    var  french = [0, 135, 302, 20, 321, 174, 263, 60, 204, 96, 185, 225, 40, 244, 116, 341, 154, 283, 77, 332, 125, 312, 87, 195, 165, 293, 11, 254, 50, 68, 214, 106, 351, 144, 273, 31, 234];
    
    var type = getRouletteType();
    if (type === 'AMERICAN'){
        return american[num];
    }
    else {
        return french[num];
    }
};

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
        var tick = function () {
            timer.timespan.tickDown();
            $(timer.div).text(timer.timespan.getTimeString());
        };

        this.intervals = setInterval(function () {
            tick();
        }, 1000);
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
    this.boardInitialized = false;
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
            gamedata.lastHandledEvent = event.id;
            gameStarted(gamedata, event);
            break;
        case 'GAME_OVER':
            gamedata.lastHandledEvent = event.id;
            gameOver(gamedata);
            break;
        case 'WINNING_NUMBER':
            gamedata.lastHandledEvent = event.id;
            winningNumber(event.winningNumber);
            break;
        case 'RESULTS_SCORES':
            gamedata.lastHandledEvent = event.id;
            playerWon(gamedata, event);
            break;
        case 'PLAYER_RESIGNED':
            gamedata.lastHandledEvent = event.id;
            playerResigned(gamedata, event);
            break;
        case 'PLAYER_BET':
            gamedata.lastHandledEvent = event.id;
            playerBet(gamedata, event);
            break;
        case 'PLAYER_FINISHED_BETTING':
            gamedata.lastHandledEvent = event.id;
            playerFinishedBetting(gamedata);
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
        if (gamedata.boardInitialized === false) {
            for (var i = 0, max = playersDetails.length; i < max; i++) {
                // create player and add to game data
                var name = playersDetails[i].name;
                var money = playersDetails[i].money;
                var player = new Player(name, money);
                gamedata.players[i] = player;
            }
            // finally after adding all players, create their UI
            createPlayersOnUI(gamedata.players);
            gamedata.boardInitialized = true;
        }

        log("Game Started!");
        removeAllCoins();
        startTimer();
    });
};

var removeAllCoins = function () {
    $('.coin.bet').remove();
};

var gameOver = function (gamedata) {

    clearInterval(eventsIntervals);
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
//    var curr = $roulettImage.css('rotate').slice(0,-3);
    var degree = getRotateDegree(winningNumber);
    var degStr = degree + 'deg';
    $roulettImage.animate({'rotate': degStr}, 1000);
    degStr = '0deg';
    $roulettImage.delay(3000).animate({'rotate': degStr}, 500);

    log("Winning number is: " + winningNumber);
    // clear all bets
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
    //amount: 5
//betType: "STRAIGHT"
//id: 10
//numbers: [12]
//playerName: "Name"
//timeout: 0
//type: "PLAYER_BET"
//winningNumber: 0

    log("'" + betdata.playerName + "' placed a '" + betdata.betType + "' bet of $" + betdata.amount);

    if (betdata.playerName !== getCurrentPlayer()) {
        var player = game.getPlayer(betdata.playerName);
        if (player !== undefined) {
            var newAmount = player.money - betdata.amount;
            player.money = newAmount;
            player.updateMoneyOnUI(newAmount);
        }
    }
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

var makeBet = function (playername, coin, amount, betType, numbers) {
    var url = 'tests/makeBet';
    $.ajax({url: url, data: {'amount': amount, 'betType': betType, 'numbers': numbers}}).success(function (data) {
        if (data.status === 'Error') {
            $(coin).remove();
        }
        else {
            var player = game.getPlayer(playername);
            if (player !== undefined) {
                var newAmount = player.money - amount;
                player.money = newAmount;
                player.updateMoneyOnUI(newAmount);
            }
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

    eventsIntervals = setInterval(function () {
        getEvents(game);
    }, 1000);

    var clonethis = function () {
        return $(this).clone().addClass('bet');
        $.ui.ddmanager.current.cancelHelperRemoval = true;
    };

    $('.coin').draggable({helper: clonethis, revert: 'invalid'});
    $('.hotspot').droppable({
        drop: function (event, ui) {
            var spot = this;
            var bettype = $(spot).data("bettype");
            var numbers = $(spot).data("numbers");
            console.log(bettype + " " + numbers);
            alert(bettype + " " + numbers);
            $.ui.ddmanager.current.cancelHelperRemoval = true;
            var coin = ui.helper;
            var money = $(coin).data('amount');
            var playername = getCurrentPlayer();
            makeBet(playername, coin, money, bettype, numbers);
            log('trying to place a bet for ' + bettype + " of $" + money);
        }
    });
});

