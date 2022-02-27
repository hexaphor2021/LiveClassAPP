$(document).ready(function(){
    //1. hide error section
    $("#nameError").hide();
    $("#emailError").hide();
    $("#mobileNumberError").hide();
    $("#subjectError").hide();
    $("#passwordError").hide();
    $("#passwordError").hide();

    //2. error variable
    var nameError = false;
    var emailError = false;
   var mobileNumberError = false;
    var subjectError = false;
    var passwordError=false;

    //3. validate function
    function validate_name() {
        var val = $("#name").val();
        
        if(val=='') {
            $("#nameError").show();
            $("#nameError").html("  name can not be empty");
            $("#nameError").css("color","red");
            nameError = false;
        } 
        else{
        $("#nameError").hide();
          nameError = true;
        }
        return nameError;
    }
    
    function validate_password() {
        var val = $("#password").val();
        
        if(val=='') {
            $("#passwordError").show();
            $("#passwordError").html("Password can not be empty");
            $("#passwordError").css("color","red");
            passwordError = false;
        } 
        else{
        $("#passwordError").hide();
          passwordError = true;
        }
        return passwordError;
    }
    
     function validate_number() {
        var val = $("#mobileNumber").val();
        var exp = /^[0-9\s]{10,15}$/;
        if(val=='') {
            $("#mobileNumberError").show();
            $("#mobileNumberError").html("Mobile number can not be empty");
            $("#mobileNumberError").css("color","red");
            mobileNumberError = false;
        } 
         else if(!exp.test(val)){
			        $("#mobileNumberError").show();
		            $("#mobileNumberError").html("Please enter number only minimum 10 maximum 15 digit");
		            $("#mobileNumberError").css("color","red");
		            mobileNumberError = false;
			}
        else{
           var id="";
    	   if($('#teacherId').val()!==undefined){
                id=$("#teacherId").val()
            }
           
           $("#streamError").hide();
           //ajax call start
           $.ajax({
               url : 'validate',
               data: {'mobile':val,'id':id},
               success:function(resTxt) {
                   if(resTxt=='') { //no error
                       $("#mobileNumberError").hide();
                       mobileNumberError = true;
                   } else{ 
                       $("#mobileNumberError").show();
                       $("#mobileNumberError").html(resTxt);
                       $("#mobileNumberError").css("color","red");
                       mobileNumberError = false;
                   }
               }
           });
        }
        return mobileNumberError;
    }


    function validate_email() {
        var val = $("#email").val();
         var exp = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
        if(val=='') {
            $("#emailError").show();
            $("#emailError").html("Email can't be empty ");
            $("#emailError").css("color","red");
            emailError = false;
        }
          else if(!exp.test(val)){
            $("#emailError").show();
            $("#emailError").html("Please enter correct format email");
            $("#emailError").css("color","red");
            emailError = false;
        } 
       else{
            $("#emailError").hide();
                emailError = true;
        }
        return emailError;
    }
    function validate_subject() {
        var val = $("#subject").val();
        if(val=='') {
            $("#subjectError").show();
            $("#subjectError").html("Please choose subject ");
            $("#subjectError").css("color","red");
            subjectError = false;
        } 
       else{
            $("#subjectError").hide();
            subjectError = true;
        }
        return subjectError;
    }

    //4. link input with action
    $("#name").keyup(function(){
        validate_name();
    });
    $("#mobileNumber").keyup(function(){
        validate_number();
    });
     $("#email").keyup(function(){
        validate_email();
    });
    
    $("#password").keyup(function(){
        validate_password();
    });
    
    $("#subject").change(function(){
        validate_subject();
    });

    
    //5. submit button
    $("#teacher").submit(function(){
        //call all validate functions
        validate_name();
        validate_number();
         validate_email();
         validate_subject();
          validate_password();

        if(subjectError && mobileNumberError && emailError && nameError && passwordError)
            return true;
        // else false
        else return false;
    });

});