/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(function(){
    var hotspots = $(".hotspot");
    hotspots.click(function(event){
        var spot = this;
        var bettype = $(spot).data("bettype");
        var numbers = $(spot).data("numbers");
        console.log(bettype + " " + numbers);
        alert(bettype + " " + numbers);
    });
});

