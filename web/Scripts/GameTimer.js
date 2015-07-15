/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

        this.intervals = setInterval(function () {
            tick(this);
        }, 1000);
    };

    this.stop = function () {
        clearInterval(this.intervals);
    };
}