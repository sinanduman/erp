function tr_uretimplanguncelle(sender,planid){

    cleartrbackground();
    $(sender).css("background-color","darkkhaki");

    $("#hedefuretim, #gercekuretim, #hata").removeAttr("readonly");

    $("#uretimplanguncelle").removeAttr("disabled");
    $("#uretimplanguncelle").removeClass("btn btn-danger").addClass("btn btn-success");

    $("#uretimplaniptal").css("display","");

    //changefun_uretim(plan[planid].mamulkod);
    $("#tarih").val(plan[planid].tarihtr);
    $("#calisanid").val(plan[planid].calisanid);
    $("#makinaid").val(plan[planid].makinaid);

    $("#mamulkod").val(plan[planid].mamulkod);
    $("#hedefuretim").val(plan[planid].hedefuretim);
    $("#hedefuretimhidden").val(plan[planid].hedefuretim);

    $("#gercekuretim").val(plan[planid].uretimadet);
    $("#gercekuretim_hidden").val(plan[planid].uretimadet);
    $("#hata").val(plan[planid].hataliadet);
    $("#sapma").val(plan[planid].sapma);
    $("#uretimplanid").val(plan[planid].id);

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
    $("#hatasebepid").val(plan[planid].hataid);

    /* 1:update, 2:insert */
    $("#uretimplanid").val(planid);
    changefun($("#mamulkod"));
}

$("#uretimplanliste").click(function(){
    $("#uretimplanform").submit();
});

$("#uretimplaniptal").click(function(){

    cleartrbackground();

    $("#hedefuretim, #gercekuretim, #hata").attr("readonly","readonly");

    $("#uretimplanguncelle").removeClass("btn btn-success").addClass("btn btn-danger").attr("disabled","disabled");

    $("#uretimplaniptal").css("display","none");

    clearUretimPlanFields();
});

$("#uretimplanguncelle").click(function(){
    //console.log("gercekuretim: " + $("#gercekuretim").val()  + " hedefuretim: " + $("#hedefuretim").val()  +	"fark: " + $("#fark").val() );
    if(!is_number($("#hedefuretim").val())){
        alert("Hedeflenen üretim girilmeli!\n");
    }
    else if(!is_number($("#gercekuretim").val())){
        alert("Gerçekleşen üretim girilmeli!\n");
    }
    else if($("#hata").val()!="" && !is_positive($("#hatasebepid").val())){
        alert("Hedeflenen üretim, Gerçekleşen üretimden yüksek.\nAçıklama seçilmeli!\n");
    }
    else if($("#hata").val()=="" && is_positive($("#hatasebepid").val())){
        alert("Açıklama seçili!\n\Hedeflenen üretim, Gerçekleşen üretimden yüksek olmalı!\n");
    }
    else{
        document.uretimplanform.submit();
    }
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

function clearUretimPlanFields() {
    $("#hedefuretim").val("");
    $("#hedefuretimhidden").val("");
    $("#gercekuretim").val("");
    $("#hata").val("");
    $("#sapma").val("");
    $("#hatasebepid").val("0");

    $("#mamulkod").val("");
    $("#mamulad").val("");
    $("#hammadde").val("");
    $("#agirlik").val("");
    $("#cevrimsure").val("");
    $("#firmaad").val("");
    $("#uretimplanid").val("0");
    $("#makinaid").val("");
}
