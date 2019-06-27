$(document).ready(function () {
    var counter = 0;

        $('#addrow').on('click', function () {
            console.log('add row!');
            var newRow = $('<tr>');
            var cols = "";

            cols += '<td><input type=\'text\' class=\'form-control\' name=\'predicate' + counter + '\' list=\'interlinkingPredicates\'/></td>';
            cols += '<td><input type=\'text\' class=\'form-control\' name=\'object' + counter + '\' title=\'example: <http://dbpedia.org/resource/Dublin>\'/></td>';

            cols += '<td><input type=\'button\' class=\'ibtnDel btn btn-md btn-danger \'  value=\'Delete\'></td>';
            newRow.append(cols);
            $('table.order-list').append(newRow);
            counter++;
        });



    $('table.order-list').on('click', '.ibtnDel', function (event) {
        console.log('delete row!');
        $(this).closest('tr').remove();
        counter -= 1
    });

});

function copyValueToClipboard() {
    var copyText = document.getElementById("myURI");
    copyText.select();
    document.execCommand("copy");
}

function getElementValue(form, name){
    console.log("searching for: "+name);
    for (i = 0; i < form.elements.length; i++){
        console.log("elementname:"+form.elements[i].name);
        if(form.elements[i].name==name){
            return form.elements[i].value;
        }
    }
}

//called in popup-screen to create new interlink
function validateForm(form) {
    console.log("validateForm"+form.elements.length);
    for (i = 0; i < form.elements.length; i++){
        console.log("elementname:"+form.elements[i].name);
        if(form.elements[i].name.includes("object")|| form.elements[i].name.includes("predicate")){
             if(!isNotEmpty(form.elements[i].value)){
                 return false;
             }
        }
    }
    return true;
}

function isNotEmpty(field) {

    var fieldData = field;
    if (fieldData.length == 0 || fieldData == "") {
        console.log("7. if");
        //field.className = "FieldError"; //Classs to highlight error
        alert("Please fill in all fields or delete row to continue.");
        return false;
    } else {
        console.log("8. else");
        //field.className = "FieldOk"; //Resets field back to default
        return true; //Submits form
    }
}