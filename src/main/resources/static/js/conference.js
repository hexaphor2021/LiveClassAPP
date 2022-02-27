$(document).ready(function(){
    
    $("#subject").change(function(){
     var val=$("#subject").val();
        $.ajax({
               url : 'getTeacherBySubjectId',
               data: {'subjectId':val},
               success:function(resTxt) {

               for (const [key, value] of Object.entries(resTxt)) {
                    console.log(key);
                    console.log(value);
                  $('#teacher').append(`<option value="${key}">
                                       ${value}
                                  </option>`);
                   }
               
               }
           });
    });


$("#batch").change(function(){
     var val=$("#batch").val();
        $.ajax({
               url : 'getStudentByBatchId',
               data: {'batchId':val},
               success:function(resTxt) {
               debugger;
               for (const [key, value] of Object.entries(resTxt)) {
                    console.log(key);
                    console.log(value);
                  $('#student').append(`<option value="${key}">
                                       ${value}
                                  </option>`);
                   }
               
               }
           });
    });
    
});