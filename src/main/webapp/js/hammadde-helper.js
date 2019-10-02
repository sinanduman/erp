function clearId(id) {
    var currentId = "#es_liste_kod" + id;
    if ($.trim($(currentId).val()) == "") {
        $("#es_liste_id" + id).val("0");
        $("#es_liste_ad" + id).text("");
    }
}

function changeFocusId(id) {
    /* Fokus oldugunda autocompleted aktif olsun */
    var autocompleted = "#autocompleted" + id;
    var currentId = "#es_liste_kod" + id;
    if ($(autocompleted).val() == "0") {
        $(autocompleted).val("1");
        $(currentId).autocomplete({
            source: bilesen2,
            minLength: 1,
            select: function (event, ui) {
                changefunes(ui.item, id);
            }
        });
        $(currentId).change(function () {
            changefunes(this, id);
        });
    }
}

$("#bilesenkodara").autocomplete({
    source: bilesen2,
    minLength: 1,
    select: function (event, ui) {
        changefun(ui.item);
    }
});
$("#bilesenkodara").change(function () {
    changefun(this);
});

function changefunes(elem, id) {
    var found = false;
    if (elem != null) {
        for (i in bilesen) {
            /* autocomplete alanina birden fazla alan ekler */
            var b = elem.value.split("[");
            /* Ayni hammadde es olamaz. Hammadde ve Yarimamul birbirlerinin es maddesi olamaz */
            if (b[0].trim() == bilesen[i].bilesenkod && id != bilesen[i].bilesenid && $("#liste_bilesentipid"+id).val() == bilesen[i].bilesentipid) {
            	//console.log("es bilesentip: " + bilesen[i].bilesentipid);
				//console.log("oz bilesentip: " + $("#liste_bilesentipid"+id).val());
                found = true;
                var bilesenIdObje = "#es_liste_id" + id;
                var bilesenAdObje = "#es_liste_ad" + id;
                $(bilesenIdObje).val(bilesen[i].bilesenid);
                $(bilesenAdObje).text(bilesen[i].bilesenad);
            }
        }
    }
    if (!found) {
        $(bilesenIdObje).val("");
        $(bilesenAdObje).text("");
    }
}

function changefun(elem) {
    var found = false;
    if (elem != null) {
        for (i in bilesen) {
            /* autocomplete alanina birden fazla alan ekler */
            var b = elem.value.split("[");
            if (b[0].trim() == bilesen[i].bilesenkod) {
                //console.log("elem: " + elem.value);
                //console.log("bilesen: " + bilesen[i]);
                found = true;
                $("#bilesenidara").val(bilesen[i].bilesenid);
                $("#bilesenkodara").val(bilesen[i].bilesenkod);
                $("#stokliste").removeAttr("disabled");
            }
        }
    }
    if (!found) {
        $("#bilesenidara").val("");
        $("#bilesenkodara").val("");
        $("#stokliste").attr("disabled", "disabled");
    }
}


$("#stokliste").click(function () {
    $("#stoklisteid").val("1");
    $("#hammadde_form").submit();
});

$(function () {
    if ($.trim($("#bilesenidara").val()) != "") {
        findbilesenkod($("#bilesenidara").val());
        $("#bilesenkodara").change(function () {
            changefun(this);
        });
    }
});

function findbilesenkod(elemval) {
    //console.log("bilesenid : " + elemval);
    if (elemval != null) {
        for (i in bilesen) {
            if (elemval == bilesen[i].bilesenid) {
                //console.log("elem: " + elemval);
                //console.log("bilesen: " + bilesen[i]);
                found = true;
                $("#bilesenidara").val(bilesen[i].bilesenid);
                $("#bilesenkodara").val(bilesen[i].bilesenkod);
                $("#stokliste").removeAttr("disabled");
            }
        }
    }
}