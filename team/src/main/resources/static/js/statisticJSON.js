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
                            + msg
                            + "</a></div>");
                    },
                    error : function() {
                        $("#resultStatistic").html(
                                "<div class='alert alert-danger lead'>ERROR</div>");
                    }
                });
            });
    });