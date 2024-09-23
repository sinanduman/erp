let mamulislemtip = {
    add:"Bileşen Ekle",
    update:"Bileşen Güncelle"
};
let mamulApp = angular.module('mamulApp', []);
mamulApp.controller('MamulCtrl',['$scope', function($scope){
    let show = true;
    let uid = 0;

    $scope.bilesenler = [];
    $scope.tip = [
        {id:1, ad:"Hammadde"},
        {id:2, ad:"Yarımamül"}
    ];
    $scope.birimler = [
        {id:0, ad:"Gram"},
        {id:1, ad:"Adet"},
        {id:2, ad:"KG"}
    ];
    $scope.on = function(){
        document.mamulform.birimid_select.selectedIndex = $scope.birimler[0].id;
        document.mamulform.birimid.value                = document.mamulform.birimid_select.value;
        document.mamulform.miktar.value                 = "";
        show = true;
    };
    $scope.off = function(){
        document.mamulform.birimid_select.selectedIndex = $scope.birimler[1].id;
        document.mamulform.birimid.value                = document.mamulform.birimid_select.value;
        document.mamulform.miktar.value                 = "";
        show = false;
    };
    $scope.showState = function(){
        return show;
    };
    $scope.saveBilesen = function() {
        if(!is_ondalik(document.mamulform.miktar.value)){
            alert("Bileşen Miktarı NÜMERİK bir değer olmalı!");
            return false;
        }
        if (!(is_positive(document.mamulform.hammaddeid.value) || is_positive(document.mamulform.yarimamulid.value))) {
            alert("Bileşen Bilgisi BOŞ olamaz!");
            return false;
        }
        if($scope.yenibilesen.id == null) {
            $scope.yenibilesen.id = uid++;
            $scope.yenibilesen.hammaddead  = document.mamulform.hammadde.value;
            $scope.yenibilesen.hammaddeid  = document.mamulform.hammaddeid.value;
            $scope.yenibilesen.yarimamulad = document.mamulform.yarimamul.value;
            $scope.yenibilesen.yarimamulid = document.mamulform.yarimamulid.value;
            $scope.yenibilesen.birimad     = document.mamulform.birimid_select.options[document.mamulform.birimid_select.selectedIndex].text;
            $scope.yenibilesen.birimid     = document.mamulform.birimid_select.options[document.mamulform.birimid_select.selectedIndex].value;
            if(show){
                $scope.yenibilesen.uretimtipid  = $scope.tip[0].id;
                $scope.yenibilesen.uretimtip    = $scope.tip[0].ad;
                $scope.yenibilesen.bilesenid    = $scope.yenibilesen.hammaddeid;
                $scope.yenibilesen.bilesenad    = $scope.yenibilesen.hammaddead;
            }
            else{
                $scope.yenibilesen.uretimtipid  = $scope.tip[1].id;
                $scope.yenibilesen.uretimtip    = $scope.tip[1].ad;
                $scope.yenibilesen.bilesenid    = $scope.yenibilesen.yarimamulid;
                $scope.yenibilesen.bilesenad    = $scope.yenibilesen.yarimamulad;
            }
            $scope.bilesenler.push($scope.yenibilesen);
        } else {
            for(i in $scope.bilesenler) {
                if($scope.bilesenler[i].id == $scope.yenibilesen.id) {
                    $scope.yenibilesen.hammaddead   = document.mamulform.hammadde.options[document.mamulform.hammadde.selectedIndex].text;
                    $scope.yenibilesen.hammaddeid   = document.mamulform.hammadde.options[document.mamulform.hammadde.selectedIndex].value;
                    $scope.yenibilesen.yarimamulad  = document.mamulform.yarimamul.options[document.mamulform.yarimamul.selectedIndex].text;
                    $scope.yenibilesen.yarimamulid  = document.mamulform.yarimamul.options[document.mamulform.yarimamul.selectedIndex].value;
                    $scope.yenibilesen.birimad      = document.mamulform.birimid_select.options[document.mamulform.birimid_select.selectedIndex].text;
                    $scope.yenibilesen.birimid      = document.mamulform.birimid_select.options[document.mamulform.birimid_select.selectedIndex].value;
                    if(show){
                        $scope.yenibilesen.uretimtipid  = $scope.tip[0].id;
                        $scope.yenibilesen.uretimtip    = $scope.tip[0].ad;
                        $scope.yenibilesen.bilesenid    = $scope.yenibilesen.hammaddeid;
                        $scope.yenibilesen.bilesenad    = $scope.yenibilesen.hammaddead;
                    }
                    else{
                        $scope.yenibilesen.uretimtipid  = $scope.tip[1].id;
                        $scope.yenibilesen.uretimtip    = $scope.tip[1].ad;
                        $scope.yenibilesen.bilesenid    = $scope.yenibilesen.yarimamulid;
                        $scope.yenibilesen.bilesenad    = $scope.yenibilesen.yarimamulad;
                    }
                    $scope.bilesenler[i] = $scope.yenibilesen;
                }
            }
        }
        $scope.yenibilesen = {};
        $("#hammadde").val("");
        $("#hammaddeid").val("");
        $("#yarimamul").val("");
        $("#yarimamulid").val("");
        document.mamulform.bilesen_ekle.textContent = mamulislemtip.add;
    };

    $scope.saveMamul = function() {
        if($scope.bilesenler.length==0) {
            alert("En az BİR bileşen eklenmelidir!");
        } else {
            let mamulad = document.mamulform.mamulad.value;
            let mamulkod = document.mamulform.mamulkod.value;
            let mamulsonbakim = document.mamulform.mamulsonbakim.value;

            if($.trim(mamulad) === "" || $.trim(mamulkod) === "" || $.trim(mamulsonbakim) === "" ){
                alert("Mamül ADI, KODU ve SONBAKIM tarihi boş olamaz!");
                return false;
            }
            else {
                if (!is_trdate(mamulsonbakim)){
                    alert("SONBAKIM tarihi geçerli bir tarih değil!");
                    return false;
                }
                else{
                    document.mamulform.submit();
                }
            }
        }
    };

    $scope.edit = function(id) {
        for(i in $scope.bilesenler) {
            if($scope.bilesenler[i].id == id) {
                document.mamulform.birimid_select.value = $scope.bilesenler[i].birimid;
                document.mamulform.birimid.value        = $scope.bilesenler[i].birimid;

                /*1:Hammadde, 2:Yarimamul */
                if($scope.bilesenler[i].uretimtipid==2){
                    $scope.bilesenler[i].uretimtip      = $scope.tip[1].ad;
                    $scope.bilesenler[i].uretimtipid    = $scope.tip[1].id;
                    document.mamulform.yarimamul.value  = $scope.bilesenler[i].yarimamulid;
                    document.mamulform.bilesenTipRadio[1].checked = true;
                    $scope.off();
                    $scope.showState();
                }
                else{
                    $scope.bilesenler[i].uretimtip      = $scope.tip[0].ad;
                    $scope.bilesenler[i].uretimtipid    = $scope.tip[0].id;
                    document.mamulform.hammadde.value   = $scope.bilesenler[i].hammaddeid;
                    document.mamulform.bilesenTipRadio[0].checked = true;
                    $scope.on();
                    $scope.showState();
                }
                $scope.yenibilesen = angular.copy($scope.bilesenler[i]);
                document.mamulform.bilesen_ekle.textContent = mamulislemtip.update;
            }
        }
    };
    $scope.del = function(id) {
        for(i in $scope.bilesenler) {
            if($scope.bilesenler[i].id == id) {
                $scope.bilesenler.splice(i,1);
                $scope.yenibilesen = {};
                document.mamulform.bilesen_ekle.textContent = mamulislemtip.add;
            }
        }
    };
}]);

$("#hammadde").autocomplete({
    source: hammaddeAuto,
    minLength: 1,
    select: function (event, ui) {
        //:: console.log(ui.item)
        //:: console.log(ui.item.value)
        changefunBilesen(ui.item, 1);
    },
    change: function (event, ui) {
        if (!(ui.item)) {
            $("#hammadde").val("")
            $("#hammaddeid").val("")
        }
    }
});

$("#yarimamul").autocomplete({
    source: yarimamulAuto,
    minLength: 1,
    select: function (event, ui) {
        //:: console.log(ui.item)
        //:: console.log(ui.item.value)
        changefunBilesen(ui.item, 2);
    },
    change: function (event, ui) {
        if (!(ui.item)) {
            $("#yarimamul").val("")
            $("#yarimamulid").val("")
        }
    }
});

$("#hammadde, #yarimamul").on("change", function (ui, event) {
    if ($(this).val().trim().length === 0) {
        $("#hammadde").val("");
        $("#hammaddeid").val("");
        $("#yarimamul").val("");
        $("#yarimamulid").val("");
    }
});

function changefunBilesen(elem, tip) {
    let found = false;
    if (tip === 1) {
        if (elem != null) {
            for (i in hammaddeAuto) {
                /* autocomplete alanina birden fazla alan ekler */
                if (elem.id === hammaddeAuto[i].id) {
                    found = true;
                    $("#hammaddeid").val(hammaddeAuto[i].id);
                }
            }
        }
    } else {
        if (elem != null) {
            for (i in yarimamulAuto) {
                /* autocomplete alanina birden fazla alan ekler */
                if (elem.id === yarimamulAuto[i].id) {
                    found = true;
                    $("#yarimamulid").val(yarimamulAuto[i].id);
                }
            }
        }
    }

    if (!found) {
        $("#hammaddeid").val("");
        $("#yarimamulid").val("");
    }
}

function updateMamulGo(url,id,action_form,islem){
    let f_bilesenid	= id;
    let f_esmamulid	= $.trim($("#liste_esmamulid"+id).val());
    let f_firmaid	= $.trim($("#liste_firmaid"+id).val());
    let f_kod		= $.trim($("#liste_kod"+id).val());
    let f_ad		= $.trim($("#liste_ad"+id).val());
    let f_figur		= $.trim($("#liste_figurid"+id).val());
    let f_cevrimsure= $.trim($("#liste_cevrimsuresi"+id).val());
    let f_durum     = $.trim($("#liste_mamuldurum"+id).val());
    let f_kritikesik= $.trim($("#liste_kritikesik"+id).val());
    let f_bakimtarihi=$.trim($("#liste_bakimtarihi"+id).val());
    let f_baskisayisi=$.trim($("#liste_baskisayisi"+id).val());
    let f_calisanid  =$.trim($("#liste_calisanid"+id).val());
    let f_islemid	= islem;
    let alert_mesaj	= f_kod + " Kodlu " + f_ad;
    if(f_kod=="" || f_ad==""  || f_cevrimsure=="" ){
        alert("Mamül ADI, KODU veya ÇEVRİMSÜRESİ boş olamaz!");
        return false;
    }
    if(!is_positive(f_cevrimsure)){
        alert("Çevrimsüresi NÜMERİK bir değer olmalı!");
        return false;
    }
    if(confirm(alert_mesaj +"\n\n" + "Mamül bilgisini GÜNCELLEMEK istediğinden"+"\n\n"+"Emin misin?")){
        $.ajax({
            url: url,
            type: "POST",
            data: {
                bilesenid	: f_bilesenid,
                firmaid		: f_firmaid,
                bilesenkod	: f_kod,
                bilesenad	: f_ad,
                islemid		: f_islemid,
                figur		: f_figur,
                cevrimsuresi: f_cevrimsure,
                esmamulid	: f_esmamulid,
                durum       : f_durum,
                kritikesik  : f_kritikesik,
                bakimtarihi : f_bakimtarihi,
                baskisayisi : f_baskisayisi,
                calisanid   : f_calisanid
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
                alert(alert_mesaj +" Mamül bilgisi başarıyla GÜNCELLENDİ" );
            }
            else{
                alert("Hata:"+ msg + ":" );
            }
        });
    }
}

function updateMamulBilesenGo(url, mamulid, bilesenid, bilesen_miktar, islem){
    let f_mamulid	= mamulid;
    let f_bilesenid	= bilesenid;
    let f_miktar	= $.trim(bilesen_miktar.value);
    let f_islemid	= islem;
    let alert_mesaj	= "";

    if(f_miktar==""){
        alert("Miktar BOŞ olamaz!");
        return false;
    }

    if(!is_ondalik(f_miktar)){
        alert("Miktar NÜMERİK bir değer olmalıdır!");
        return false;
    }

    if(confirm("Mamül Bileşen Bilgisini GÜNCELLEMEK istediğinden"+"\n\n"+"Emin misin?")){
        $.ajax({
            url: url,
            type: "POST",
            data: {
                mamulid		: f_mamulid,
                bilesenid	: f_bilesenid,
                miktar		: f_miktar,
                islemid		: f_islemid
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
                $("#tr_mam_bilesen_detay_"+mamulid+"_"+bilesenid).css('background-color','darkkhaki');
                alert(alert_mesaj +" Mamül Bileşen Bilgisi başarıyla GÜNCELLENDİ" );
            }
            else{
                alert("Hata:"+ msg + ":" );
            }
        });
    }
}

function deleteMamulGo(url,id,action_form,islem){
    let f_bilesenid	= id;
    let f_kod		= $.trim($("#liste_kod"+id).val());
    let f_ad		= $.trim($("#liste_ad"+id).val());
    let f_islemid	= islem;
    let alert_mesaj	= f_kod + " Kodlu " + f_ad;
    if(confirm(alert_mesaj +"\n\n" + "Mamül bilgisini SİLMEK istediğinden" +"\n\n" + "Emin misin?")){
        $.ajax({
            url: url,
            type: "POST",
            data: {
                bilesenid:	f_bilesenid,
                islemid:	f_islemid
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
                $("#tr"+id).css("display","none");
                $("#tr_mam_detay"+id).css("display","none");
                alert( f_kod + " Kodlu " + f_ad +" Mamül bilgisi başarıyla SİLİNDİ" );
            }
            else{
                alert("Hata: "+ msg );
            }
        });
    }
}