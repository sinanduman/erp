function SiparisCntrl($scope) {
    $scope.mamulJSON = mamul;
    $scope.selectMamul = function() {
        if($scope.mamulJSON.length>0){
            return $scope.mamulJSON[$scope.mamulJSON.length-1].id;
        }
        return 0;
    };
    $scope.savePlan = function(){
        let baszaman  = document.uretimtakipform.bassaat.value + document.uretimtakipform.basdakika.value;
        let bitzaman  = document.uretimtakipform.bitsaat.value + document.uretimtakipform.bitdakika.value;
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


$("#uretimekle").click(function(){
    let message="";
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
    if(is_empty(document.uretimtakipform.duruszaman.value) && is_positive(document.uretimtakipform.durussebepid.value)){
        message += "Duruş kodu seçili, duruş zamanı seçilmeli!\n";
    }
    let uretimadet = document.uretimtakipform.uretimadet.value;
    let hataliadet = is_empty(document.uretimtakipform.hataliadet.value) ? 0 : document.uretimtakipform.hataliadet.value;
    let tahminiuretim = document.uretimtakipform.tahminiuretim.value;
    if ((parseInt(uretimadet) + parseInt(hataliadet)) > parseInt(tahminiuretim)) {
        message += "Üretilen ve Hatalı Üretilen toplamı, Tahmini Üretim'den fazla olamaz!\n";
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
                islemid:        3,
                uretimplanid:	$("#uretimplanid").val(),
                mamulid:		$("#mamulid").val(),
                uretilenmiktar:	uretimadet
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

$("#uretimiptal").click(function(){

    cleartrbackground();

    $("#uretimekle").text("Ekle");
    $("#uretimekle").removeClass("btn btn-success").addClass("btn btn-danger");

    $("#uretimiptal").css("display","none");
    $("#uretimplanid").val("0");

    //$("#calisanid").val("");

    let calisanval	= 0;
    let makinaval	= 0;

    let hatasebepval= 0;
    let durussebepval=0;

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

$("#gercekuretim, #hedefuretim, #hata").change(function(){
    hedefuretim_change();
});

function hedefuretim_change(){
    if(!is_number($("#hedefuretim").val())){
        alert("Hedeflenen üretim miktarı 0'dan büyük olmalı!\n");
    }
    else{
        let temphata = parseInt($("#hata").val());
        let tempuretim = parseInt($("#gercekuretim").val());
        if(temphata > 0){
            $("#sapma").val(Math.round(((temphata * 100) / tempuretim)*100)/100.0);
        }
        else if(temphata == 0){
            let hatasebepval = 0;
            $("#hata").val("");
            $("#sapma").val("");

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
        $("#mamulad").val("");
        $("#mamulid").val("");
        $("#firmaad").val("");
        $("#hammadde").val("");
        $("#toplamsure").val("");
        $("#tahminiuretim").val("");
        $("#cevrimsure").val("");
        clearEsMamul();
    }
}

function clearEsMamul() {
    $("#esmamulid").val("");
    $("#esmamulid_shadow").val("");
    $("#div_esmamul").hide();
    $("#cxb_esmamul").prop("checked", false);
    $("#esmamulizleno option").remove();
    es_mamul_checkbox_off();
}

function es_mamul_checkbox_on() {
    $("#esmamulad").css("color", "indianred");
    $("#lbl_esmamul").text("Eş Mamül [Üretilsin]");
    $("#esmamulid").val($("#esmamulid_shadow").val());
    $("#div_esmamulizno").show();
    izlemeNoKontrol($("#esmamulid_shadow").val(), 6, 1, "#esmamulizleno", 0);
}

function es_mamul_checkbox_off() {
    $("#esmamulad").css("color", "#333333");
    $("#lbl_esmamul").text("Eş Mamül");
    $("#esmamulid").val("");
    $("#div_esmamulizno").hide();
}

function changefun_uretim(elem, guncelle) {
    let found = false;
    let option = "";
    if (elem != null) {
        $("#hammadde option").remove();
        $("#yarimamul option").remove();
        for (i in mamul) {
            // var b = elem.value.split("[");
            if (elem.value === mamul[i].mamulkod && !found) {
                clearEsMamul();
                $("#div_esmamul").show();
                globalMamul = mamul[i];
                found = true;
                let esmamulid = mamul[i].esmamulid;
                if (guncelle == undefined) {
                    if (esmamulid > 0) {
                        findmamulkod(esmamulid);
                    }
                }

                let ham_name_ar = mamul[i].hammadde.split(";");
                let ham_gkrno_ar = mamul[i].hammaddegkrno.split(";");
                let ham_agir_ar = mamul[i].hammaddeagir.split(";");
                let ham_mamulbasi_ar = mamul[i].hammaddemamulbasi.split(";");

                let yari_name_ar = mamul[i].yarimamul.split(";");
                let yari_gkrno_ar = mamul[i].yarimamulgkrno.split(";");
                let yari_agir_ar = mamul[i].yarimamulagir.split(";");
                let yari_mamulbasi_ar = mamul[i].yarimamulmamulbasi.split(";");

                let gkrno = mamul[i].gkrno;
                let cevrimsure = mamul[i].cevrimsure;

                document.uretimtakipform.mamulad.value = mamul[i].mamulad;
                document.uretimtakipform.mamulid.value = mamul[i].mamulid;
                document.uretimtakipform.firmaad.value = mamul[i].firmaad;
                document.uretimtakipform.hammadde.value = mamul[i].hammadde;

                dateDiff(); /* Difference in Minutes */

                option = "";
                let ham_name = "";
                let ham_gkrno = "";
                let ham_miktar = 0;
                let ham_mamulbasi = 0;
                for (i in ham_name_ar) {

                    ham_name = ham_name_ar[i];
                    ham_gkrno = ham_gkrno_ar[i];
                    ham_miktar = parseInt(ham_agir_ar[i], 10);
                    ham_mamulbasi = parseFloat(ham_mamulbasi_ar[i]);

                    option += "<option>" + ham_name + " (" + ham_gkrno + ") / " + ham_miktar / 1000.0 + " Kg / " + ham_mamulbasi + " Gr</option>";
                }

                if (ham_name_ar == "-1") {
                    //$("#div_hammadde").hide();
                } else {
                    //$("#div_hammadde").show();
                    $("#hammadde").append(option);
                }

                option = "";
                let yari_name = "";
                let yari_gkrno = "";
                let yari_miktar = 0;
                let yari_mamulbasi = 0;
                for (i in yari_name_ar) {
                    //console.log(i +" : " +yari_name_ar[i]);

                    yari_name = yari_name_ar[i];
                    yari_gkrno = yari_gkrno_ar[i];
                    yari_miktar = parseInt(yari_agir_ar[i], 10);
                    yari_mamulbasi = parseInt(yari_mamulbasi_ar[i], 10);

                    option += "<option>" + yari_name + " (" + yari_gkrno + ") / " + yari_miktar + " Ad / " + yari_mamulbasi + " Ad</option>";
                }
                //console.log("yari_name: " +yari_name+ " yari_miktar: " + yari_miktar + " yari_mamulbasi: " + yari_mamulbasi );
                if (yari_name == "-1") {
                    $("#div_yarimamul").hide();
                } else {
                    $("#div_yarimamul").show();
                    $("#yarimamul").append(option);
                }

                /* Mamul Izleme No Kutusu Doldurma */
                /* MamulId, Uret/Kontrol(1,6), Kullanildi/Kullanilmadi(0:1), SelectBox */
                izlemeNoKontrol($("#mamulid").val(), 6, 1, "#mamulizleno", 0);
                /*
                if (esmamulid > 0) {
                    izlemeNoKontrol(esmamulid, 6, 1, "#esmamulizleno", 0);
                }
                */
            }
        }
    }
    if (!found) {
        clearFields();
        $("#hammadde option").remove();
        $("#yarimamul option").remove();
        $("#mamulizleno option").remove();
        $("#esmamulizleno option").remove();
        globalMamul = null;
    }
}
