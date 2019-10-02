function SiparisCntrl($scope) {
    $scope.mamulJSON = mamul;
    $scope.selectMamul = function() {
        if($scope.mamulJSON.length>0){
            return $scope.mamulJSON[$scope.mamulJSON.length-1].id;
        }
        return 0;
    };
    $scope.savePlan = function(){
        var baszaman  = document.uretimtakipform.bassaat.value + document.uretimtakipform.basdakika.value;
        var bitzaman  = document.uretimtakipform.bitsaat.value + document.uretimtakipform.bitdakika.value;
        if(!is_positive(document.uretimtakipform.miktar.value)){
            alert("Planlanan Miktar NÜMERİK bir değer olmalıdır!");
            return false;
        }
        else if (baszaman == bitzaman ){
            alert("Başlangıç ve Bitiş zamanları AYNI olamaz!");
            return false;
        }
        else{
            if(parseInt(baszaman,10) > parseInt(bitzaman,10)){
                if(confirm("GECE çalışması olacak. Onaylıyor musun?")){
                    document.uretimtakipform.submit();
                }
                else{
                    return false;
                }
            }
            else{
                document.uretimtakipform.submit();
            }
        }
    };
}

function SiparisEkleCtrl($scope) {
    $scope.saveSiparis = function(){
        if(!is_positive(document.siparisform.miktar.value)){
            alert("Sipariş Miktarı NÜMERİK bir değer olmalıdır!");
            return false;
        }
        else{
            document.siparisform.submit();
        }
    };
}

function updateGo(url,id,action_form,islem){
    var f_siparisplanid	= id;
    var f_bilesen		= action_form.bilesen.value;
    var f_miktar		= $.trim(action_form.miktar.value);
    var f_makinaid		= action_form.makina.value;
    var f_calisanid		= action_form.calisan.value;
    var f_tarih			= action_form.tarih.value;
    var f_bassaat		= action_form.bassaat.value;
    var f_basdakika		= action_form.basdakika.value;
    var f_bitsaat		= action_form.bitsaat.value;
    var f_bitdakika		= action_form.bitdakika.value;
    var f_hataid		= action_form.hataid.value;
    var f_hatamiktar	= $.trim(action_form.hatamiktar.value);
    var f_durusid		= action_form.durusid.value;
    var f_not			= action_form.not.value;
    var f_islemid		= islem;
    var alert_mesaj		= f_siparisplanid + " Numaralı " + f_bilesen;

    if(!is_empty(f_hataid) && !is_positive(f_hatamiktar)){
        alert("Hata miktarı, eğer varsa, NÜMERİK olmalıdır!");
        return false;
    }
    if(is_empty(f_hataid) && is_positive(f_hatamiktar)){
        alert("Hata SEBEBİ seçmelisin!");
        return false;
    }
    if (is_positive(f_miktar)){
        if(confirm(alert_mesaj +"\n\n" + "Üretim Plan bilgisini GÜNCELLEMEK istediğinden"+"\n\n"+"Emin misin?")){
            $.ajax({
                url: url,
                type: "POST",
                data: {
                    siparisplanid: f_siparisplanid,
                    miktar: f_miktar,
                    makinaid: f_makinaid,
                    calisanid: f_calisanid,
                    tarih: f_tarih,
                    bassaat: f_bassaat,
                    basdakika: f_basdakika,
                    bitsaat: f_bitsaat,
                    bitdakika: f_bitdakika,
                    hataid: f_hataid,
                    hatamiktar: f_hatamiktar,
                    durusid: f_durusid,
                    not: f_not,
                    islemid: f_islemid
                },
                dataType: 'html',
                beforeSend: function ( xhr ) {
                },
                success: function(data, textStatus, xhr) {
                },
                error: function(xhr, textStatus, errorThrown) {
                    alert("Hata Oluştu: " + textStatus + " , " + errorThrown);
                }
            }).done(function( msg ) {
                if(is_number( msg )){
                    $("#tr"+id).css('background-color','darkkhaki');
                    alert( f_bilesen + " ürünün Plan bilgisi başarıyla GÜNCELLENDİ" );
                }
                else{
                    alert("Hata:"+ msg + ":" );
                }
            });
        }
    }
    else{
        alert("HATA! Miktar Bilgisine 0'dan farklı SAYISAL bir değer girin!");
    }
}
function okGo(url,id,action_form,islem){
    var f_siparisplanid	= id;
    var f_bilesen		= action_form.bilesen.value;
    var f_miktar		= action_form.miktar.value;
    var f_islemid		= islem;
    var alert_mesaj		= f_siparisplanid + " Numaralı " + f_bilesen;
    if (is_positive(f_miktar)){
        if(confirm(alert_mesaj +"\n\n" + "Plan bilgisinin TAMAMLANDIĞINDAN" +"\n\n" + "Emin misin?")){
            $.ajax({
                url: url,
                type: "POST",
                data: {
                    siparisplanid: f_siparisplanid,
                    islemid: f_islemid
                },
                dataType: "html",
                beforeSend: function ( xhr ) {
                },
                success: function(data, textStatus, xhr) {
                },
                error: function(xhr, textStatus, errorThrown) {
                    alert("Hata Oluştu: " + textStatus + " , " + errorThrown);
                }
            }).done(function( msg ) {
                if(is_number( msg )){
                    $("#tr"+id).css("background-color","E6E6E6");
                    disableFormElements(action_form);
                    //$("#tr"+id).css("display","none");
                    //$("#tr"+id).css("display","none");
                    //$("#tr"+id).css("display","none");
                    //$("#tr"+id).css("display","none");
                    alert( f_bilesen + " ürünün Üretim Planı TAMAMLANDI" );
                }
                else{
                    //$("#tr_"+msg).css('visibility','hidden');
                    alert("Hata: "+ msg );
                }
            });
        }
    }
    else{
        alert("HATA! Miktar Bilgisine 0'dan farklı SAYISAL bir değer girin!");
    }
}
function deleteGo(url,id,action_form,islem){
    var f_siparisplanid	= id;
    var f_bilesen		= action_form.bilesen.value;
    var f_miktar		= action_form.miktar.value;
    var f_islemid		= islem;
    var alert_mesaj		= f_siparisplanid + " Numaralı " + f_bilesen;
    if (is_positive(f_miktar)){
        if(confirm(alert_mesaj +"\n\n" + "Plan bilgisini SİLMEK istediğinden" +"\n\n" + "Emin misin?")){
            $.ajax({
                url: url,
                type: "POST",
                data: {
                    siparisplanid: f_siparisplanid,
                    islemid: f_islemid
                },
                dataType: "html",
                beforeSend: function ( xhr ) {
                },
                success: function(data, textStatus, xhr) {
                },
                error: function(xhr, textStatus, errorThrown) {
                    alert("Hata Oluştu: " + textStatus + " , " + errorThrown);
                }
            }).done(function( msg ) {
                if(is_number( msg )){
                    //disableFormElements(action_form);
                    //$("#tr"+id).css("display","none");
                    //$("#tr"+id).css("display","none");
                    //$("#tr"+id).css("display","none");
                    $("#tr"+id).css("display","none");
                    $("#tr_urtpl_detay"+id).css("display","none");
                    alert( f_bilesen + " ürünün Üretim Planı SİLİNDİ" );
                }
                else{
                    alert("Hata: "+ msg );
                }
            });
        }
    }
    else{
        alert("HATA! Miktar Bilgisine 0'dan farklı SAYISAL bir değer girin!");
    }
}

$("#uretimekle").click(function(){
    var message="";
    if(document.uretimtakipform.mamulid.value==""){
        message += "Mamül seçiniz!\n";
    }
    if(!is_positive(document.uretimtakipform.mamulizleno.value) ){
        message += "Mamül İzleme No girilmeli!\n";
    }
    if(!is_positive(document.uretimtakipform.uretimadet.value)){
        message += "Üretim miktarı seçiniz!\n";
    }
    if(!is_empty(document.uretimtakipform.hataliadet.value) && !is_number(document.uretimtakipform.hataliadet.value)){
        message += "Hata adedini doğru seçiniz!\n";
    }
    if(is_positive(document.uretimtakipform.hataliadet.value) && !is_positive(document.uretimtakipform.hatasebepid.value)){
        message += "Hata kodunu seçiniz!\n";
    }
    if(!is_empty(document.uretimtakipform.duruszaman.value) && !is_number(document.uretimtakipform.duruszaman.value)){
        message += "Duruş zamanını doğru seçiniz!\n";
    }
    if(is_positive(document.uretimtakipform.duruszaman.value) && !is_positive(document.uretimtakipform.durussebepid.value)){
        message += "Duruş kodunu seçiniz!\n";
    }
    if(is_empty(document.uretimtakipform.duruszaman.value) && is_positive(document.uretimtakipform.durussebepid.value)){
        message += "Duruş kodu seçili, duruş zamanı seçilmeli!\n";
    }
    if(message!=""){
        alert(message);
    }
    else{
        //alert($("#uretimadet").val() + " : " + $("#hataliadet").val()+ " : " +  $("#uretimplanid").val() );
        // stok kontrolu yap
        $.ajax({
            url: "uretimtakip",
            type: "POST",
            data: {
                uretimplanid:	$("#uretimplanid").val(),
                mamulid:		$("#mamulid").val(),
                uretilenmiktar:	$("#uretimadet").val(),
                hatalimiktar:	$("#hataliadet").val(),
                islemid: 3
            },
            beforeSend: function ( xhr ) {
            },
            success: function(data, textStatus, xhr) {
            },
            error: function(xhr, textStatus, errorThrown) {
                alert("Hata Oluştu: " + textStatus + " , " + errorThrown);
            }
        }).done(function( msg ) {
            if(msg == "0"){
                if($("#uretimplanid").val()=="0"){
                    if(confirm("Eklemek istediğine emin misin?")){
                        document.uretimtakipform.submit();
                    }
                }
                else{
                    document.uretimtakipform.submit();
                }
            }
            else{
                alert( msg );
            }
        });
    }
});

$("#mamulliste").click(function(){
    $("#mamullisteid").val(1);
    $("#uretimplanform").submit();
});


function uretimtakipsil(planid){
    bootbox.confirm(planid + " No'lu Üretim Takip bilgisini SİLMEK istediğinden" +"\n\n" + "Emin misin?", function(result) {
        if(result){
            $.ajax({
                url: "uretimtakip",
                type: "POST",
                data: {
                    uretimplanid: planid,
                    islemid: 2
                },
                beforeSend: function ( xhr ) {
                },
                success: function(data, textStatus, xhr) {
                },
                error: function(xhr, textStatus, errorThrown) {
                    alert("Hata Oluştu: " + textStatus + " , " + errorThrown);
                }
            }).done(function( msg ) {
                if( msg == "0"){
                    //disableFormElements(action_form);
                    //$("#tr"+id).css("display","none");
                    $("#tr"+planid).css("display","none");
                    OK.show(planid + " No'lu Üretim Takibi SİLİNDİ" );
                }
                else{
                    Error.show( msg );
                }
            });
        }
    });
    /*
    $.confirm({
        title: "Silme Onayı",
        text: planid + " No'lu Üretim Plan bilgisini SİLMEK istediğinden" +"\n\n" + "Emin misin?",
        confirm: function(button) {

        },
        cancel: function(button) {
        },
        confirmButton: "Evet",
        cancelButton: "Hayır",
    });
    */
}

function uretimtakipok(planid){
    bootbox.confirm(planid + " No'lu Üretim Takip bilgisini ONAYLAMAK istediğinden" +"\n\n" + "Emin misin?", function(result) {
        if(result){
            $.ajax({
                url: "uretimtakip",
                type: "POST",
                data: {
                    uretimplanid: planid,
                    islemid: 1
                },
                beforeSend: function ( xhr ) {
                },
                success: function(data, textStatus, xhr) {
                },
                error: function(xhr, textStatus, errorThrown) {
                    alert("Hata Oluştu: " + textStatus + " , " + errorThrown);
                }
            }).done(function( msg ) {
                if(msg == "0"){
                    //disableFormElements(action_form);
                    //$("#tr"+planid).css("display","none");
                    //$("#tr"+planid).attr("disabled","disabled").css( "background-color", "darkkhaki");
                    //$("#div"+planid).css("display","none");
                    OK.show( planid + " No'lu Üretim Takibi ONAYLANDI" );
                    $("#div"+planid).html("<span class='text-danger'><strong>onaylandı</strong></span>");
                }
                else{
                    alert( msg );
                }
            });
        }
    });
}

function uretimtakipguncelle(sender,planid){

    cleartrbackground();
    $(sender).css("background-color","darkkhaki");

    $("#uretimekle").text("Güncelle");
    $("#uretimekle").removeClass("btn btn-danger").addClass("btn btn-success");
    $("#uretimiptal").css("display","");
    //changefun_uretim(plan[planid].mamulkod);
    document.uretimtakipform.tarih.value		= plan[planid].tarihtr;
    document.uretimtakipform.bassaat.value		= plan[planid].baslangic.substring(0,2);
    document.uretimtakipform.basdakika.value	= plan[planid].baslangic.substring(3,5);
    document.uretimtakipform.bitsaat.value		= plan[planid].bitis.substring(0,2);
    document.uretimtakipform.bitdakika.value	= plan[planid].bitis.substring(3,5);
    document.uretimtakipform.calisanid.value	= plan[planid].calisanid;
    document.uretimtakipform.makinaid.value		= plan[planid].makinaid;
    document.uretimtakipform.mamulkod.value		= plan[planid].mamulkod;
    document.uretimtakipform.uretimadet.value	= plan[planid].uretimadet;
    document.uretimtakipform.hataliadet.value	= plan[planid].hataliadet;
    document.uretimtakipform.hatasebepid.value	= plan[planid].hataid;
    document.uretimtakipform.durussebepid.value	= plan[planid].durusid;
    document.uretimtakipform.duruszaman.value	= plan[planid].duruszaman;
    document.uretimtakipform.mamulizleno.value	= plan[planid].izlemeno;
    //findbilesenkod(plan[planid].esid);

    if(plan[planid].hataid=="0"){
        $("#hatasebepid option").each(function(index) {
            //console.log($(this).val());
            if(index == 0){
                $("#hatasebepid").val($(this).val());
            }
        });
    }

    if(plan[planid].durusid=="0"){
        $("#durussebepid option").each(function(index) {
            if(index == 0){
                $("#durussebepid").val($(this).val());
            }
        });
    }

    /* 1:update, 2:insert */
    $("#uretimplanid").val(planid);
    changefun_uretim(document.uretimtakipform.mamulkod, 1);

    //console.log($(sender));
    //console.log($(sender).children("0"));

    //"01:23".substr(0,2)
    //"01:23".substr(3,5)
}

function uretimplanguncelle(sender,planid){

    cleartrbackground();
    $(sender).css("background-color","darkkhaki");

    $("#hedefuretim").removeAttr("readonly");
    $("#gercekuretim").removeAttr("readonly");

    $("#uretimplanguncelle").removeAttr("disabled");
    $("#uretimplanguncelle").removeClass("btn btn-danger").addClass("btn btn-success");

    $("#uretimplaniptal").css("display","");

    //changefun_uretim(plan[planid].mamulkod);
    document.uretimplanform.tarih.value			= plan[planid].tarihtr;
    document.uretimplanform.calisanid.value		= plan[planid].calisanid;
    document.uretimplanform.makinaid.value		= plan[planid].makinaid;
    document.uretimplanform.mamulkod.value		= plan[planid].mamulkod;
    document.uretimplanform.hedefuretim.value		= plan[planid].hedefuretim;
    document.uretimplanform.hedefuretimhidden.value	= plan[planid].hedefuretim;
    document.uretimplanform.gercekuretim.value		= plan[planid].uretimadet;
    document.uretimplanform.gercekuretim_hidden.value= plan[planid].uretimadet;
    document.uretimplanform.fark.value				= plan[planid].fark;
    document.uretimplanform.sapma.value				= plan[planid].sapma;

    if(!is_number(plan[planid].hedefuretim)){
        if(is_number(plan[planid].fark)){
            $("#hedefuretim").val(parseInt(plan[planid].uretimadet) + parseInt(plan[planid].fark) );
            $("#hedefuretimhidden").val(parseInt(plan[planid].uretimadet) + parseInt(plan[planid].fark) );
        }
        else{
            $("#hedefuretim").val(parseInt(plan[planid].uretimadet));
            $("#hedefuretimhidden").val(parseInt(plan[planid].uretimadet) );
        }
        hedefuretim_change();
    }

    if(plan[planid].hataid=="0"){
        $("#hatasebepid option").each(function(index) {
            if(index == 0){
                $("#hatasebepid").val($(this).val());
            }
        });
    }
    document.uretimplanform.hatasebepid.value = plan[planid].hataid;

    /* 1:update, 2:insert */
    $("#uretimplanid").val(planid);
    changefun_uretim(document.uretimplanform.mamulkod);

}

$("#uretimiptal").click(function(){

    cleartrbackground();

    $("#uretimekle").text("Ekle");
    $("#uretimekle").removeClass("btn btn-success").addClass("btn btn-danger");

    $("#uretimiptal").css("display","none");
    $("#uretimplanid").val("0");

    //$("#calisanid").val("");

    var calisanval	= 0;
    var makinaval	= 0;

    var hatasebepval= 0;
    var durussebepval=0;

    $("#calisanid option").each(function(index) {
        if(index == 0){
            calisanval = $(this).val();
        }
    });

    $("#makinaid option").each(function(index) {
        if(index == 0){
            makinaval = $(this).val();
        }
    });

    $("#hatasebepid option").each(function(index) {
        if(index == 0){
            hatasebepval = $(this).val();
        }
    });

    $("#durussebepid option").each(function(index) {
        if(index == 0){
            durussebepval = $(this).val();
        }
    });

    $("#calisanid").val(calisanval);
    $("#makinaid").val(makinaval);

    $("#hatasebepid").val(hatasebepval);
    $("#durussebepid").val(durussebepval);

    $('#tarih').datepicker("setDate","today");

    $("#bassaat").val("00");
    $("#basdakika").val("00");

    $("#bitsaat").val("00");
    $("#bitdakika").val("00");

    $("#mamulkod").val("");
    $("#mamulad").val("");
    $("#mamulid").val("");
    $("#mamulizleno").val("");
    //changefun(document.uretimtakipform.mamulkod);

    $("#uretimadet").val("");
    $("#hataliadet").val("");
    $("#duruszaman").val("");

    $("#hammadde option").remove();
    $("#yarimamul option").remove();

    $("#div_hammadde").show();
    $("#div_yarimamul").show();

    $("#firmaad").val("");
    $("#cevrimsure").val("");
    $("#toplamsure").val("");
    $("#tahminiuretim").val("");
});


$("#uretimplaniptal").click(function(){

    cleartrbackground();

    $("#hedefuretim").attr("readonly","readonly");
    $("#gercekuretim").attr("readonly","readonly");

    $("#uretimplanguncelle").removeClass("btn btn-success").addClass("btn btn-danger");
    $("#uretimplanguncelle").attr("disabled","disabled");

    $("#uretimplaniptal").css("display","none");
    $("#uretimplanid").val("0");

    $("#hedefuretim").val("");
    $("#hedefuretimhidden").val("");
    $("#gercekuretim").val("");
    $("#fark").val("");
    $("#sapma").val("");
    $("#hatasebepid").val("0");

    $("#mamulkod").val("");
    $("#mamulizleno").val("");
    changefun_uretim(document.uretimplanform.mamulkod);

    //hedefuretim_change();
});


$("#uretimplanguncelle").click(function(){
    //console.log("gercekuretim: " + $("#gercekuretim").val()  + " hedefuretim: " + $("#hedefuretim").val()  +	"fark: " + $("#fark").val() );
    if(!is_number($("#hedefuretim").val())){
        alert("Hedeflenen üretim girilmeli!\n");
    }
    else if(!is_number($("#gercekuretim").val())){
        alert("Gerçekleşen üretim girilmeli!\n");
    }
    else if($("#fark").val()!="" && !is_positive($("#hatasebepid").val())){
        alert("Hedeflenen üretim, Gerçekleşen üretimden yüksek.\nAçıklama seçilmeli!\n");
    }
    else if($("#fark").val()=="" && is_positive($("#hatasebepid").val())){
        alert("Açıklama seçili!\n\Hedeflenen üretim, Gerçekleşen üretimden yüksek olmalı!\n");
    }
    else{
        console.log("tetstststs");
        if(parseInt($("#gercekuretim").val())!=parseInt($("#gercekuretim_hidden").val()) ){
            if(!confirm("Gerçekleşen Üretim DEĞİŞTİ, Onaylıyor musun?")){
                return false;
            }
        }
        console.log("gercekuretim: " + parseInt($("#gercekuretim").val()));
        console.log("gercekuretim_hidden: " + parseInt($("#gercekuretim_hidden").val()));
        var uretimfark = (parseInt($("#gercekuretim").val()) - parseInt($("#gercekuretim_hidden").val()));
        console.log(uretimfark);
        /* Malzeme yeterli mi*/
        $.ajax({
            url: "uretimtakip",
            type: "POST",
            data: {
                uretimplanid:	0,
                mamulid:		$("#mamulid").val(),
                uretilenmiktar:	uretimfark,
                hatalimiktar:	0,
                islemid: 3
            },
            error: function(xhr, textStatus, errorThrown) {
                alert("Hata Oluştu: " + textStatus + " , " + errorThrown);
            }
        }).done(function( msg ) {
            if(msg == "0"){
                //console.log(1);
                if($("#uretimplanid").val()=="0"){
                    //console.log(2);
                    if(confirm("Güncellemek istediğine emin misin?")){
                        document.uretimplanform.submit();
                    }
                }
                else{
                    //console.log(3);
                    document.uretimplanform.submit();
                }
            }
            else{
                //console.log(4);
                alert( msg );
            }
        });
        /* Malzeme yeterli mi*/
    }
});

$("#gercekuretim").change(function(){
    hedefuretim_change();
});
$("#hedefuretim").change(function(){
    hedefuretim_change();
});

function hedefuretim_change(){
    if(!is_number($("#hedefuretim").val())){
        alert("Hedeflenen üretim miktarı 0'dan büyük olmalı!\n");
    }
    else{
        var tempfark = parseInt($("#hedefuretim").val()) - parseInt($("#gercekuretim").val());
        if(tempfark>0){
            $("#fark").val(tempfark);
            $("#sapma").val( Math.round(( ( tempfark )*100 / $("#hedefuretim").val())*100)/100.0 );
            $("#hedefuretimhidden").val($("hedefuretim").val());
        }
        else if(tempfark==0){
            var hatasebepval = 0;
            $("#fark").val("");
            $("#sapma").val("");
            $("#hedefuretimhidden").val($("hedefuretim").val());

            $("#hatasebepid option").each(function(index) {
                if(index == 0){
                    hatasebepval = $(this).val();
                }
            });
            $("#hatasebepid").val(hatasebepval);
        }
        else{
            alert("Hedeflenen üretim, Gerçekleşen üretimden düşük olamaz!\n\n");
        }
    }
}

function findbilesenkod(elemval) {
    if (elemval != null && elemval != "") {
        for (j in bilesen) {
            if (elemval == bilesen[j].esid) {
                $("#esmamulid_shadow").val(bilesen[j].esid);
                $("#esmamulad").text(bilesen[j].esad);
                return;
            }
        }
    }
}


function findmamulkod(elemval) {
    if (elemval != null && elemval != "") {
        for (k in mamul) {
            if (elemval == mamul[k].mamulid) {
                $("#esmamulid_shadow").val(mamul[k].mamulid);
                $("#esmamulad").text(mamul[k].mamulad);
                return;
            }
        }
    }
}

function clearFields(elem) {
    if($("#mamulkod").val() == "") {
        document.uretimtakipform.mamulad.value = "";
        document.uretimtakipform.mamulid.value = "";
        document.uretimtakipform.firmaad.value = "";
        document.uretimtakipform.hammadde.value = "";
        document.uretimtakipform.toplamsure.value = "";
        document.uretimtakipform.tahminiuretim.value = "";
        document.uretimtakipform.cevrimsure.value = "";
        clearEsMamul();
    }
}

function clearEsMamul() {
    document.uretimtakipform.esmamulid.value = "";
    document.uretimtakipform.esmamulid_shadow.value = "";
    $("#div_esmamul").hide();
}