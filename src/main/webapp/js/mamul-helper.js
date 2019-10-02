function clearId(id) {
    var currentId = "#liste_esmamulkod" + id;
    if ($.trim($(currentId).val()) == "") {
        $("#liste_esmamulid" + id).val("0");
        $("#liste_esmamulad" + id).val("");
    }
}

function changeFocusId(id) {
    /* Fokus oldugunda autocompleted aktif olsun */
    var autocompleted = "#autocompleted" + id;
    var currentId = "#liste_esmamulkod" + id;
    if ($(autocompleted).val() == "0") {
        $(autocompleted).val("1");
        $(currentId).autocomplete({
            source: mamul2,
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

function changefunes(elem, id) {
    var found = false;
    var mamulKodObje = "#liste_esmamulkod" + id;
    var mamulIdObje = "#liste_esmamulid" + id;
    var mamulAdObje = "#liste_esmamulad" + id;
    if (elem != null) {
        for (j in mamul) {
            /* Ayni mamul es olamaz */
            if (id == mamul[j].mamulid)
                continue;
            /* autocomplete alanina birden fazla alan ekler */
            var b = elem.value.split("[");
            if (b[0].trim() === mamul[j].mamulkod) {
                found = true;
                $(mamulKodObje).val(mamul[j].mamulkod);
                $(mamulIdObje).val(mamul[j].mamulid);
                $(mamulAdObje).text(mamul[j].mamulad);
            }
        }
    }
    if (!found) {
        $(mamulKodObje).val("");
        $(mamulIdObje).val("");
        $(mamulAdObje).text("");
    }
}