$(document).ready(function(){
    //1. hide error section
    $("#subjectNameError").hide();
    $("#descriptionError").hide();

    //2. error variable
    var descriptionError = false;
    var subjectNameError = false;


    //3. validate function
    function validate_subjectName() {
        var val = $("#subjectName").val();
        
        if(val=='') {
            $("#subjectNameError").show();
            $("#subjectNameError").html(" Subject name can not be empty");
            $("#subjectNameError").css("color","red");
            subjectNameError = false;
        } 
        else{
           var id="";
    	   if($('#subjectId').val()!==undefined){
                id=$("#subjectId").val()
            }
           
           $("#streamError").hide();
           //ajax call start
           $.ajax({
               url : 'validate',
               data: {'subjectName':val,'id':id},
               success:function(resTxt) {
                   if(resTxt=='') { //no error
                       $("#subjectNameError").hide();
                       subjectNameError = true;
                   } else{ 
                       $("#subjectNameError").show();
                       $("#subjectNameError").html(resTxt);
                       $("#subjectNameError").css("color","red");
                       subjectNameError = false;
                   }
               }
           });
        }
        return subjectNameError;
    }


    function validate_description() {
        var val = $("#description").val();
        if(val=='') {
            $("#descriptionError").show();
            $("#descriptionError").html("Description can't be empty ");
            $("#descriptionError").css("color","red");
            descriptionError = false;
        } 
       else{
            $("#descriptionError").hide();
            descriptionError = true;
        }
        return descriptionError;
    }

    //4. link input with action
    $("#description").keyup(function(){
        validate_description();
    });
    $("#subjectName").keyup(function(){
        validate_subjectName();
    });

    
    //5. submit button
    $("#subject").submit(function(){
        //call all validate functions
        validate_description();
         validate_subjectName();

        if(descriptionError && streamError )
            return true;
        // else false
        else return false;
    });

});