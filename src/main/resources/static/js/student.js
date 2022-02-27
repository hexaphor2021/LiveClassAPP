$(document).ready(function(){
    //1. hide error section
    $("#nameError").hide();
    $("#emailError").hide();
    $("#mobileError").hide();
    $("#batchError").hide();
    $("#passwordError").hide();
    $("#registrationNoError").hide();
    $("#parentNameError").hide();
    $("#addressError").hide();

    //2. error variable
    var nameError = false;
    var emailError = false;
   var mobileError = false;
    var batchError = false;
    var passwordError=false;
     var registrationNoError = false;
    var parentNameError=false;
    var addressError=false;

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
        var val = $("#mobile").val();
        var exp = /^[0-9\s]{10,15}$/;
        if(val=='') {
            $("#mobileError").show();
            $("#mobileError").html("Mobile number can not be empty");
            $("#mobileError").css("color","red");
            mobileError = false;
        } 
         else if(!exp.test(val)){
			        $("#mobileError").show();
		            $("#mobileError").html("Please enter number only minimum 10 maximum 15 digit");
		            $("#mobileError").css("color","red");
		            mobileError = false;
			}
        else{
          var id="";
    	   if($('#studentId').val()!==undefined){
                id=$("#studentId").val()
            }
           
           $("#mobileError").hide();
           //ajax call start
           $.ajax({
               url : 'validatemobile',
               data: {'mobile':val,'id':id},
               success:function(resTxt) {
                   if(resTxt=='') { //no error
                       $("#mobileError").hide();
                       mobileError = true;
                   } else{ 
                       $("#mobileError").show();
                       $("#mobileError").html(resTxt);
                       $("#mobileError").css("color","red");
                       mobileError = false;
                   }
               }
           });
        }
        return mobileError;
    }
    
     function validate_registrationNo() {
        var val = $("#registrationNo").val();
        var exp = /^[0-9\s]{10,15}$/;
        if(val=='') {
            $("#registrationNoError").show();
            $("#registrationNoError").html("Registration number can not be empty");
            $("#registrationNoError").css("color","red");
            registrationNoError = false;
        } 
        else{
           var id="";
    	   if($('#studentId').val()!==undefined){
                id=$("#studentId").val()
            }
           
           $("#registrationNoError").hide();
           //ajax call start
           $.ajax({
               url : 'validate',
               data: {'registrationNo':val,'id':id},
               success:function(resTxt) {
                   if(resTxt=='') { //no error
                       $("#registrationNoError").hide();
                       registrationNoError = true;
                   } else{ 
                       $("#registrationNoError").show();
                       $("#registrationNoError").html(resTxt);
                       $("#registrationNoError").css("color","red");
                       registrationNoError = false;
                   }
               }
           });
        }
        return registrationNoError;
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
    
    function validate_batch() {
        var val = $("#batch").val();
        if(val=='') {
            $("#batchError").show();
            $("#batchError").html("Please choose batch ");
            $("#batchError").css("color","red");
            batchError = false;
        } 
       else{
            $("#batchError").hide();
            batchError = true;
        }
        return batchError;
    }
    
     function validate_parent() {
        var val = $("#parentName").val();
        if(val=='') {
            $("#parentNameError").show();
            $("#parentNameError").html("Parent name not empty ");
            $("#parentNameError").css("color","red");
            parentNameError = false;
        } 
       else{
            $("#parentNameError").hide();
            parentNameError = true;
        }
        return parentNameError;
    }
    
     function validate_address() {
        var val = $("#address").val();
        if(val=='') {
            $("#addressError").show();
            $("#addressError").html("Address can not be empty ");
            $("#addressError").css("color","red");
            addressError = false;
        } 
       else{
            $("#addressError").hide();
            addressError = true;
        }
        return addressError;
    }

    //4. link input with action
    $("#name").keyup(function(){
        validate_name();
    });
    $("#mobile").keyup(function(){
        validate_number();
    });
     $("#email").keyup(function(){
        validate_email();
    });
    
    $("#password").keyup(function(){
        validate_password();
    });
    
    $("#batch").change(function(){
        validate_batch();
    });
    
     $("#parentName").keyup(function(){
        validate_parent();
    });
    
     $("#address").keyup(function(){
        validate_address();
    });
    $("#registrationNo").keyup(function(){
    validate_registrationNo();
    });
    

    
    //5. submit button
    $("#student").submit(function(){
        //call all validate functions
        validate_name();
        validate_number();
         validate_email();
          validate_batch();
          validate_password();
           validate_parent();
           validate_address();
           validate_registrationNo();
           

        if(registrationNoError && mobileError && emailError && nameError && passwordError && parentNameError && addressError && passwordError && batchError)
            return true;
        // else false
        else return false;
    });

});