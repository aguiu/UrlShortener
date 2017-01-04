$(document).ready(
    function() {
        $("#showStatistic").submit(
            function(event) {
                event.preventDefault();
                $.ajax({
                    type : "GET",
                    url : $("#urlShort").val() + "+",
                    success : function(msg) {
                        $("#resultStatistic").html(
                            "<div class='alert alert-success lead'><a target='_blank'>"
                            + "URL: " + msg.url + "<br>Fecha: " 
                            + msg.created + "<br>Clicks: " + msg.clicks
                            + "<br>IP del creador: " + msg.ip
                            + "</a></div>");
                    },
                    error : function() {
                        $("#resultStatistic").html(
                                "<div class='alert alert-danger lead'>ERROR</div>");
                    }
                });
            });
    });