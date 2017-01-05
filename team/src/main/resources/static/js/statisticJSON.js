$(document).ready(
    function() {
        $("#showStatistic").submit(
            function(event) {
                event.preventDefault();
                $.ajax({
                    type : "GET",
                    url : $("#urlShort").val() + "+",
                    data : $(this).serialize(),
                    success : function(msg) {
                        $("#resultStatistic").html(
                            "<div class='alert alert-success lead'><a target='_blank'>"
                            + "URL: " + msg.url + "<br>Fecha: " 
                            + msg.created + "<br>Clicks: " + msg.clicks
                            + "<br>IP del creador: " + msg.ip + "<br>Visitantes: " + msg.ipVisitantes
                            + "</a></div>");
                    },
                    error : function() {
                        $("#resultStatistic").html(
                                "<div class='alert alert-danger lead'>ERROR</div>");
                    }
                });
            });
    });