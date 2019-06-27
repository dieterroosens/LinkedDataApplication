//Include StringScript
function adaptPredicate(prop){
    //inDataset (VoID)filterDiv
    var prefix = prop.substring(prop.indexOf("(")+1, prop.indexOf(")"));
    var pred = prop.substring(0, prop.indexOf("("));

    //alert("prefix:"+prefix+" pred:"+pred+" input: "+prop);
    if(prefix!=0){
        return prefix+":"+pred;
    }
    else {
        return prop.substr(0, prop.indexOf("filterDiv"));
    }
}