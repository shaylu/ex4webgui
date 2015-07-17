/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var $green = "alert-success";
var $red = "alert-danger";
var $white = "";

var getMesageClass = function (data) {
    var status = data.status;

    if (status !== undefined) {
        if (status === "Success")
            return $green;
        else if (status === "Error")
            return $red;
    }

    return $white;
};

var setMessage = function (data) {

    var messageBox = $('#ajaxMessage');
    $(messageBox).text(data.message);

    var classNameToAdd = getMesageClass(data);
    $(messageBox).removeClass($red).removeClass($green);
    $(messageBox).addClass(classNameToAdd);

    $(messageBox).show();
};

var create = function () {
    var url = 'tests/createGame';
    $.ajax({
        'url': url,
        'data': $('#frmNewGameSettings').serialize()
    }).success(function (data) {
        setMessage(data);
    }).error(function () {
        alert('Something went wrong while creating a new game.');
    });
};

function getUrlParameter(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++)
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] === sParam)
        {
            return sParameterName[1];
        }
    }
}

$(function () {
    var messageFromURL = getUrlParameter("message");
    var statusFromURL = getUrlParameter("status");
    if (messageFromURL !== "") {
        var data = {};
        data.message = unescape(messageFromURL);
        data.status = statusFromURL;
        setMessage(data);
    }

    $('#frmNewGameSettings').submit(function (e) {
        e.preventDefault();
        create();
    });

});

