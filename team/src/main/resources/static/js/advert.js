function cuenta() {  
	var interval = window.setInterval(function(msg) {
		var timeLeft    = $("#timeLeft").html();                                
		if(eval(timeLeft) == 0){
			var url = window.location.href;
		    var array = url.split('/');
		    var id = array[array.length - 1];
		    $.getJSON("http://localhost:8080/uri/"+id, function(data){
		    	window.location = (data.target);   
		    });
		        clearInterval(interval);
		 }else{              
			 $("#timeLeft").html(eval(timeLeft)- eval(1));
		 }
	}, 1000); 
} 