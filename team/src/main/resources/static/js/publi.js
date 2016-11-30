$(document).ready(
    function() {
        $("#redirectToPubli").submit(
            function(event) {
                event.preventDefault();
                $.ajax({
                    type : "GET",
                    url : $("#urlShort").val() + "+",
                    success : function(msg) {
                        $("#publi").html(
                        		"<div id="+"number"+"></div>"
                        );
                    },
                    error : function() {
                        $("#resultPubli").html(
                                "<div class='alert alert-danger lead'>ERROR</div>");
                    }
                });
            });
    });