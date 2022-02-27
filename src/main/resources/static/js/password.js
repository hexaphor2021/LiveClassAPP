 $(document).ready(function(){
    	 debugger;
    	$("#confirmPasswordError").hide();
    	
    	var confirmPasswordError=false;
    	
    	function checkPasswordMatch() {
    		 debugger;
            var password = $("#newPassword").val();
            var confirmPassword = $("#ConfirmPassword").val();
            if (password != confirmPassword){
            	$("#confirmPasswordError").show();
                $("#confirmPasswordError").html("Passwords does not match!");
                $("#confirmPasswordError").css("color","red");
                confirmPasswordError=false;
            }
            	
            else{
            	$("#confirmPasswordError").hide();
            	confirmPasswordError=true;
            }
               return  confirmPasswordError;
        }
    	
    	$("#ConfirmPassword").keyup(function(){
    		checkPasswordMatch();
    	});
    	
    	$("#password").submit(function(){
    		checkPasswordMatch();
    		 debugger;
    		if(confirmPasswordError){
    			return true;
    		}
    		else{
    			return false;
    		}
    	});
    });