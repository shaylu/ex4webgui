/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
});

