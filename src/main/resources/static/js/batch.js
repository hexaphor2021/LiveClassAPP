$(document).ready(function(){
    //1. hide error section
    $("#yearError").hide();
    $("#classNameError").hide();
    $("#streamError").hide();
    $("#batchError").hide();
    $("#batch").value = "textValue";
    //2. error variable
    var yearError = false;
    var classNameError = false;
    var streamError = false;
    var batchError = false;

    //3. validate function
    function validate_year() {
        var val = $("#year").val();
        var exp = /^[0-9]{0,10}$/;
        if(val=='') {
            $("#yearError").show();
            $("#yearError").html(" year can not be empty");
            $("#yearError").css("color","red");
            yearError = false;
        } 
        else if(!exp.test(val)) {
            $("#yearError").show();
            $("#yearError").html("Please enter only number ");
            $("#yearError").css("color","red");
            yearError = false;
        }
        else{

            $("#yearError").hide();
            yearError = true;
        }
        return yearError;
    }


    function validate_classname() {
        var val = $("#className").val();
        if(val=='') {
            $("#classNameError").show();
            $("#classNameError").html("class name can't be empty ");
            $("#classNameError").css("color","red");
            classNameError = false;
        } 
       else{
            $("#classNameError").hide();
            classNameError = true;
        }
        return classNameError;
    }

    function validate_stream() {
        var val = $("#stream").val();
        var className= $("#className").val();
        var year= $("#year").val();
        var batch =year + className + val;
        if(val=='') {
            $("#streamError").show();
            $("#streamError").html("stream can't be empty ");
            $("#streamError").css("color","red");
            streamError = false;
            } 
       else{
    	   var id="";
    	   if($('#batchId').val()!==undefined){
                id=$("#batchId").val()
            }
           
           $("#streamError").hide();
           //ajax call start
           $.ajax({
               url : 'validate',
               data: {'batchName':batch,'id':id},
               success:function(resTxt) {
                   if(resTxt=='') { //no error
                       $("#streamError").hide();
                       streamError = true;
                   } else{ 
                       $("#streamError").show();
                       $("#streamError").html(resTxt);
                       $("#streamError").css("color","red");
                       streamError = false;
                   }
               }
           });
        }
        
        return streamError;
    }

    function validate_batch() {
    	var year= $("#year").val();
    	var stream = $("#stream").val();
        var className= $("#className").val();
        var batch =year + className + stream;
        $("#batch").val(batch);
        var val = batch;
        if(val=='') {
            $("#batchError").show();
            $("#batchError").html("batch can not be empty");
            $("#batchError").css("color","red");
            batchError = false;
        } 
        else{
            var id="";
            if($("#id").val()!=undefind){
                id=$("#id").val();
            }
            
            $("#batchError").hide();
            //ajax call start
            $.ajax({
                url : 'validate',
                data: {'batchName':batch,'id':id},
                success:function(resTxt) {
                    if(resTxt=='') { //no error
                        $("#batchError").hide();
                        batchError = true;
                    } else{ 
                        $("#batchError").show();
                        $("#batchError").html(resTxt);
                        $("#batchError").css("color","red");
                        batchError = false;
                    }
                }
            });
        }
        return batchError;
    }

    //4. link input with action
    $("#year").keyup(function(){
        validate_year();
    });
    $("#className").keyup(function(){
        validate_classname();
    });
    $("#stream").keyup(function(){
        validate_stream();
    });
    
    //5. submit button
    $("#batch").submit(function(){
        //call all validate functions
        validate_year();
         validate_classname();
         validate_stream();
       //  validate_batch();
        // check all error variables
        // if all true then submit form return true
        //&& instituteError && addressError
        if(yearError && classNameError && streamError )
            return true;
        // else false
        else return false;
    });

});