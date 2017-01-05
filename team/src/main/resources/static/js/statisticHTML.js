$(document).ready(
    function() {
        $("#showStatisticHTML").submit(
            function(event) {
                event.preventDefault();
                $.ajax({
                    type : "GET",
                    url : $("#urlShortHtml").val() + "+html",
                    success : function(msg) {
                        $("#resultStatistic").html(
                            "<div class='alert alert-success lead'><a target='_blank'>"
                            + msg +"</a></div>");
                    },
                    error : function() {
                        $("#resultStatistic").html(
                                "<div class='alert alert-danger lead'>ERROR</div>");
                    }
                });
            });
    });