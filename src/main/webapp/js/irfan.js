const numpattern	= /^[0-9][0-9]*$/;
const numpattern2	= /^[1-9][0-9]*$/; /* Sayilabilir miktar icin */
const numpattern3	= /^[\-]?[1-9][0-9]*$/; /* Stok iade için miktar icin */
const numpattern4	= /^(([1-9]*[0-9]*([.][0-9]+))|([1-9][0-9]*))$/; /* Hammadde gramaj icin */
const numpattern5   = /^[0-9]{2}-[0-9]{2}-[0-9]{4}$/; /* Tarih girisi icin */
function is_number(formval){
    if($.trim(formval)==""){
        return false;
    }
    return numpattern.test(formval);
}
function is_positive(formval){
    if($.trim(formval)==""){
        return false;
    }
    return numpattern2.test(formval);
}
function is_ondalik(formval){
    if($.trim(formval)==""){
        return false;
    }
    return numpattern4.test(formval);
}
function is_iade(formval){
    if($.trim(formval)==""){
        return false;
    }
    return numpattern3.test(formval);
}
function is_empty(formval) {
    if($.trim(formval) == "")
        return true;
    else
        return false;
}
function is_trdate(formval){
    if($.trim(formval)==""){
        return false;
    }
    return numpattern5.test(formval);
}
function disableFormElements(action_form){
    let elements = action_form.elements;
    for(let i=0; i< action_form.length; i++ ){
        elements[i].disabled = true;
    }
}
function hideshow(trid){
    $(trid).toggle("fast");
}
$("#go").click(function(){
    let message="";
    if(document.kategori.customerid.value==""){
        message += "Müşteri seçiniz!\n";
    }
    if(document.kategori.branchid.value==""){
        message += "Lokasyon seçiniz!\n";
    }
    if(document.kategori.channelid.value==""){
        message += "Mecra seçiniz!\n";
    }
    if(document.kategori.productid1.value=="" &&
        document.kategori.productid2.value=="" &&
        document.kategori.productid3.value==""
    ) {
        message += "En az bir ürün seçiniz!\n";
    }
    if(message!=""){
        alert(message);
    }
    else{
        document.kategori.submit();
    }
});
function cleartrbackground(){
    $(".tableplan tr").css("background-color","");
}
var OK = (function() {
    "use strict";

    let elem,
        hideHandler,
        that = {};

    that.init = function(options) {
        elem = $(options.selector);
    };

    that.show = function(text) {
        clearTimeout(hideHandler);

        $("#okdiv").html(text);
        $("#okdiv").delay(200).fadeIn().delay(2000).fadeOut();
    };
    return that;
}());
let Error = (function() {
    "use strict";

    let elem,
        hideHandler,
        that = {};

    that.init = function(options) {
        elem = $(options.selector);
    };

    that.show = function(text) {
        clearTimeout(hideHandler);

        $("#errordiv").html(text);
        $("#errordiv").delay(200).fadeIn().delay(2000).fadeOut();
    };
    return that;
}());
$("#exceleaktar").click(function(){
    if(confirm("Üretim Planı Excel'e aktarılsın mı?\n")){
        $("#excelegonder").val(1);
        $("#exceltarih").val($("#tarih").val());
        $("#excelform").submit();
    }
});
$("#exceleaktar_stok").click(function(){
    if(confirm("Stok Raporu Excel'e aktarılsın mı?\n")){
        $("#excelegonder").val(1);
        $("#excelform").submit();
    }
});
$("#exceleaktar_uretimrapor").click(function(){
    if(confirm("Üretim Plan Raporu Excel'e aktarılsın mı?\n")){
        $("#excelegonder").val(1);
        $("#excelform").submit();
    }
});
$("#exceleaktar_sevk").click(function(){
    if(confirm("Sevk Raporu Excel'e aktarılsın mı?\n")){
        $("#excelegonder").val(1);
        $("#excelform").submit();
    }
});
$("#exceleaktar_mamul").click(function(){
    if(confirm("Mamul Stok Raporu Excel'e aktarılsın mı?\n")){
        $("#excelegonder").val(1);
        $("#excelform").submit();
    }
});
$("#exceleaktar_gkrliste").click(function(){
    if(confirm("GKR Liste Raporu Excel'e aktarılsın mı?\n")){
        $("#excelegonder").val(1);
        $("#excelform").submit();
    }
});

$("#exceleaktar_izlemeno").click(function(){
    if(confirm("İzleme No Raporu Excel'e aktarılsın mı?\n")){
        $("#excelegonder").val(1);
        $("#excelform").submit();
    }
});
$("#exceleaktar_bakimrapor").click(function(){
    if(confirm("Mamül Kalıp Bakım Raporu Excel'e aktarılsın mı?\n")){
        $("#excelegonder").val(1);
        $("#excelform").submit();
    }
});
function izlemeNoTest(mamulid, islemid, kullanildi, inputbox, gkrno){
    if(confirm(gkrno + " ID'li İzleme No'yu SİLMEK istediğinden emin misin?")){
        /* Uretim No Sil */
        izlemeNoKontrol(mamulid, islemid, kullanildi, inputbox, gkrno);
    }
}
function izlemeNoKontrol(mamulid, islemid, kullanildi, inputbox, gkrno){
    let f_mamulid	= mamulid;
    let f_islem		= islemid; /* 1:Yeni Kayit, 3:Kayit Sil, 6:Kontrol */
    let f_kullanildi= kullanildi; /* 0:Kullanilmadi, 1:Kullanildi */
    let f_gkrno		= gkrno; /* Sadece Silme Isleminde Anlamli, Digerlerinde degeri: 0*/

    if(!is_empty(mamulid)){
        $.ajax({
            url: "izlemeno",
            type: "POST",
            data: {
                mamulid:	f_mamulid,
                islemid:	f_islem,
                kullanildi:	f_kullanildi,
                gkrno:		f_gkrno
            },
            beforeSend: function ( xhr ) {
            },
            success: function(data, textStatus, xhr) {
            },
            error: function(xhr, textStatus, errorThrown) {
                alert("Hata Oluştu: " + textStatus + " , " + errorThrown);
            }
        }).done(function( msg ) {

            let obj = $.parseJSON( msg );

            if(f_islem == "3"){
                let trbox = eval("'#tr" + f_mamulid +"_" + f_gkrno + "'");
                $(trbox).hide();
            }

            if(f_islem == "1"){

                let strTR = "\"<tr id='tr" + obj[0].mamulid + "_" +obj[0].gkrno + "'>" +
                    "<td>" + obj[0].kullanildi_tarih + "</td>" +
                    "<td>" + obj[0].gkrno + "</td>" +
                    "<td>" + obj[0].mamulkod + "</td>" +
                    "<td>" + obj[0].mamulad + "</td>" +
                    "<td class='text-right'>" + "0 Ad." + "</td>" +
                    "<td class='text-center'>" +
                    "<a href=\"javascript:izlemeNoTest(" + obj[0].mamulid +","+3+","+0+ ",'#gkrno',"+ obj[0].gkrno+");\" title='İzleme No Sil'><span class='fa fa-minus-circle fa-lg text-danger'></span></a>" +
                    "</td></tr>\"";
                //console.log(obj[0]);
                //console.log(strTR);
                $("#izlemenotable tr:first").after(strTR);
            }

            let inputbox_option = eval("'" + inputbox + " option'");
            //console.log(inputbox_option);
            $(inputbox_option).remove();

            //var gkrno  = mamul[i].gkrno.split(";");
            //var miktar = mamul[i].miktar.split(";");
            //var used = jQuery.parseJSON('{"0":"Kullanılmadı", "1":"Kullanıldı"}');
            let option = "";
            for(let i = 0; i < obj.length; i++) {
                // console.log(obj[i]);
                let kullanildi ="";
                if(obj[i].kullanildi=="1"){
                    kullanildi = " (*)";
                }
                option += '<option value="'+ obj[i].gkrno + '">' + obj[i].gkrno + " -> " + obj[i].kullanildi_tarih + kullanildi + '</option>';
            }
            $(inputbox).append(option);
        });
    }
}