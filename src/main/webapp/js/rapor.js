$("#raporgetir").click(function(){
    $("#raporgetirid").val("1");
    $("#raporform").submit();
});

$("#mamulliste").click(function(){
    $("#mamullisteid").val(1);
    $("#uretimplanform").submit();
});
$("#cbx_azalan").on("change", function () {
    if ($(this).prop("checked")) {
        azalan_checkbox_on();
    }
    else{
        azalan_checkbox_off();
    }
});

function azalan_checkbox_on() {
    $("#azalan").val("1");
}
function azalan_checkbox_off() {
    $("#azalan").val("0");
}