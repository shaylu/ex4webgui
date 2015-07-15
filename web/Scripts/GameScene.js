/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var $players;
var $logTextarea;
var $gameArea;
var $timerSpan;
var $timer = undefined;
var $rouletteImage;
var $gameName;
var $playerName;
var $rouletteType;
var $gameTimer;
var $eventsIntervals;
var $lastHandledEvent = 0;
var $gameAreaHTML;
var $isBoardInitialized = false;
//
//var game = new GameData($logTextarea);
//var timer = undefined;
//var eventsIntervals;

var getCurrentPlayer = function () {
    return $playerName;
};

var getRouletteType = function () {
    return $rouletteType;
};

var getRotateDegree = function (num) {
    var american = [181, 11, 190, 48, 228, 87, 266, 124, 303, 162, 341, 134, 314, 20, 199, 58, 238, 95, 274, 294, 115, 256, 77, 218, 39, 331, 152, 350, 172, 322, 143, 285, 104, 247, 67, 209, 29, 0];
    var french = [0, 135, 302, 20, 321, 174, 263, 60, 204, 96, 185, 225, 40, 244, 116, 341, 154, 283, 77, 332, 125, 312, 87, 195, 165, 293, 11, 254, 50, 68, 214, 106, 351, 144, 273, 31, 234];

    var type = getRouletteType();
    if (type === 'AMERICAN') {
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
            $timer.timespan.tickDown();
            $($timer.div).text($timer.timespan.getTimeString());
        };

        this.intervals = setInterval(function () {
            tick();
        }, 1000);
    };

    this.stop = function () {
        clearInterval(this.intervals);
    };
}


//function Player(playerName, playerMoney) {
//    this.name = playerName;
//    this.money = playerMoney;
//    this.updateMoneyOnUI = function (newAmount) {
//        var player = this;
//        var moneyDiv = $(".player[name='" + player.name + "'] .playerMoney");
//        moneyDiv.text(newAmount);
//    };
//    this.disable = function () {
//        var player = this;
//        var playerDiv = $(".player[name='" + player.name + "']").addClass("player-resigned");
//    };
//}

var log = function (text) {

    $logTextarea.append(text + "\n");
    if ($logTextarea.length)
        $logTextarea.scrollTop($logTextarea[0].scrollHeight - $logTextarea.height());
};

var getEvents = function () {
    var url = 'tests/getEvents';
    $.ajax({
        url: url,
        data: {'lastID': $lastHandledEvent}
    }).success(function (events) {
        if (events.status !== 'Error') {
            processEvents(events);
        }
        else {
            log('failed to get events data, ' + events.message);
        }
    }).error(function () {
        log('failed to get events data.');
    });
};
var processEvents = function (events) {
    for (var i = 0; i < events.length; i++) {
        processEvent(events[i]);
    }
};

processEvent = function (event) {
    var type = event.type;
    switch (type) {
        case 'GAME_START':
            $lastHandledEvent = event.id;
            gameStarted(event);
            break;
        case 'GAME_OVER':
            $lastHandledEvent = event.id;
            gameOver();
            break;
        case 'WINNING_NUMBER':
            $lastHandledEvent = event.id;
            winningNumber(event.winningNumber);
            break;
        case 'RESULTS_SCORES':
            $lastHandledEvent = event.id;
            playerWon(event);
            break;
        case 'PLAYER_RESIGNED':
            $lastHandledEvent = event.id;
            playerResigned(event);
            break;
        case 'PLAYER_BET':
            $lastHandledEvent = event.id;
            playerBet(event);
            break;
        case 'PLAYER_FINISHED_BETTING':
            $lastHandledEvent = event.id;
            playerFinishedBetting(event);
            break;
        default:
            break;
    }
};

var playerFinishedBetting = function (event) {
    var name = event.playerName;
    log("'" + name + "' finished betting.");
};

var startTimer = function (millsec) {
    stopTimer();

    $timer = new Timer(millsec, $timerSpan);
    $timer.start();
};

var stopTimer = function () {
    if ($timer === undefined) {
        return;
    }

    $timer.stop();
};

var enableBetting = function () {
    $('.game-table').css('opacity', '1');
    $('.hotspot').droppable("option", "disabled", false);
    $('#btnResign').prop("disabled", false);
    $('#btnQuit').prop("disabled", false);
};

var disableBetting = function () {
    $('.game-table').css('opacity', '0.5');
    $('.hotspot').droppable("option", "disabled", true);
    $('#btnResign').prop("disabled", true);
    $('#btnQuit').prop("disabled", true);
};

var gameStarted = function (event) {
    // get players panel
    var url = 'tests/getPlayersPanel';
    if ($isBoardInitialized === false) {
        $.ajax({'url': url, 'data': {'gameName': $gameName}}).success(
                function (html) {
                    $('.players').html(html);
                    showGameAreaWhenGameStarts();
                    $isBoardInitialized = true;
                    startRound(event);
                }).error(function () {
            alert('failed to load players from server, game can not start.');
            window.location = 'index.html';
        });
    }
    else {
        startRound(event);
    }
};

var startRound = function (event) {
    log('Round started.');
    removeAllCoins();
    enableBetting();
    startTimer(event.timeout);
};

var removeAllCoins = function () {
    $('.coin.bet').remove();
};
var gameOver = function () {
    clearInterval($eventsIntervals);
    stopTimer();

    showScoreBoard();
    signout();
};

var showScoreBoard = function () {
    var url = 'tests/getScoreBoard';
    $.ajax({url: url}).success(function (data) {
        $($gameArea).html(data);
    }).error(function () {
        alert('Failed to get score board from server.');
        window.location = 'index.html';
    });
};

var signout = function () {
    var url = 'tests/leaveGame';
    $.ajax({url: url}).success(function (data) {
        // nothing
    });
};
var winningNumber = function (winningNumber) {
    stopTimer();
    disableBetting();

    // turn wheel 
    var degree = getRotateDegree(winningNumber);
    var degStr = degree + 'deg';
    $rouletteImage.animate({'rotate': degStr}, 1000);
    degStr = '0deg';
    $rouletteImage.delay(3000).animate({'rotate': degStr}, 500);

    log("Winning number is: " + winningNumber);
    // clear all bets
};

var setPlayerMoney = function (playerName, money) {
    var player = $('.players').find("[name='" + playerName + "']");
    $(player).children('.playerMoney').text(money);
};

var getPlayerMoney = function (playerName) {
    var player = $('.players').find("[name='" + playerName + "']");
    return $(player).children('.playerMoney').text();
};

var disablePlayer = function (playerName) {
    var player = $('.players').find("[name='" + playerName + "']");
    player.css('opacity', '0.5');
};

var playerWon = function (winningdata) {
    var name = winningdata.playerName;
    var earned = winningdata.amount;
    var newAmount = parseInt(getPlayerMoney(name)) + parseInt(earned);
    setPlayerMoney(name, newAmount);
    log("'" + player.name + "' has won $" + earned + ".");
};

var playerResigned = function (resigndata) {
    var name = resigndata.playerName;
    disablePlayer(name);
    log("'" + name + "' has resigned.");
};
var playerBet = function (betdata) {
    var name = betdata.playerName;
    var newAmount = parseInt(getPlayerMoney(name)) - parseInt(betdata.amount);
    setPlayerMoney(name, newAmount);
    log("'" + betdata.playerName + "' placed a '" + betdata.betType + "' bet of $" + betdata.amount);
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
            alert('Resign failed, ' + data.message);
            return false;
        }
        else {
            log('Successfully resigned.');
            return true;
        }
    }).error(function () {
        alert('Resign failed.');
        return false;
    });
};

var makeBet = function (playername, coin, amount, betType, numbers) {
    var url = 'tests/makeBet';
    log('trying to place a bet for ' + betType + " of $" + amount);

    $.ajax({url: url, data: {'amount': amount, 'betType': betType, 'numbers': numbers}}).success(function (data) {
        if (data.status === 'Error') {
            $(coin).remove();
            alert('Failed to place your bet, ' + data.message);
        }
    }).error(function () {
        $(coin).remove();
        alert('Failed to place your bet.');
    });
};

var showWaitingForGameToStart = function () {
    $($gameArea).html("Plaese wait for game to start....");
};

var showGameAreaWhenGameStarts = function () {
    $('.waiting-area').hide();
    ($gameArea).show();
};

var clonethis = function () {
    return $(this).clone().addClass('bet');
    $.ui.ddmanager.current.cancelHelperRemoval = true;
};

var finishBetting = function () {
    var url = 'tests/finishBetting';

    $.ajax({url: url}).success(function (data) {
        if (data.status === 'Error') {
            alert('Failed to finish betting, ' + data.message);
        }
    }).error(function () {
        alert('Failed to finish betting.');
    });
};

$(function () {
    $players = $('.players');
    $logTextarea = $('#txtLog');
    $gameArea = $('.game-area');
    $timerSpan = $('#lblTimer');
    $rouletteImage = $('.roulette');
    $gameName = $('.game-area').data('gamename');
    $playerName = $('.game-area').data('playername');
    $rouletteType = $('.game-area').data('roulettetype');

    $('.coin').draggable({helper: clonethis, revert: 'invalid'});
    $('.hotspot').droppable({
        drop: function (event, ui) {
            var spot = this;
            var bettype = $(spot).data("bettype");
            var numbers = $(spot).data("numbers");
            $.ui.ddmanager.current.cancelHelperRemoval = true;

            var coin = ui.helper;
            var money = $(coin).data('amount');
            var playername = getCurrentPlayer();

            makeBet(playername, coin, money, bettype, numbers);
        }
    });

    $('#btnResign').click(function () {
        resign();
    });

    $('#btnQuit').click(function () {
        resign();
        $.delay(3000, function () {
            window.location = 'index.html';
        });
    });

    $(document).on('click', '.home-button', function () {
        window.location = 'index.html';
    });

    $('#btnFinishBetting').click(function () {
        finishBetting();
    });

//
//    $gameAreaHTML = $($gameArea).html();
//    showWaitingForGameToStart();

    $eventsIntervals = setInterval(function () {
        getEvents();
    }, 1000);


});

