<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="erp.entity.*,erp.util.Util" %>
<%@ page import="java.text.SimpleDateFormat,java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="img/favicon.ico">
    <title><%=Genel.TITLE%>
    </title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="css/bootstrap.css?<%=Util.nanoTime%>">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="css/irfan.css?<%=Util.nanoTime%>">
    <link rel="stylesheet" href="css/fonts.css">
    <link rel="stylesheet" href="css/all.css">
    <link rel="stylesheet" href="css/jquery-ui-1.10.0.custom.css">
    <link rel="stylesheet/less" type="text/css" href="less/datepicker.less">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="assets/js/html5shiv.js"></script>
    <script src="assets/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<%@ include file="logincheck.jsp" %>

<%
    List<Irsaliye> irsaliye = (List<Irsaliye>) request.getAttribute("irsaliye");
    List<Irsaliye> irsaliyekapali = (List<Irsaliye>) request.getAttribute("irsaliyekapali");
    List<IrsaliyeBilesen> irsaliyebilesenopen = (List<IrsaliyeBilesen>) request.getAttribute("irsaliyebilesenopen");
    List<IrsaliyeBilesen> irsaliyebilesencompleted = (List<IrsaliyeBilesen>) request.getAttribute("irsaliyebilesencompleted");
    List<Stok> stok = (List<Stok>) request.getAttribute("stok");
    List<Firma> firma = (List<Firma>) request.getAttribute("firma");
    StringBuffer pageflash = (StringBuffer) request.getAttribute("pageflash");
    String admin = (String) session.getAttribute("admin");
%>

<%
    int acikirsaliyeid = 0;
    int acikirsaliyefirmaid = 0;
    String kapaliirsaliyeno = "";
    String acikirsaliyeno = "";
    String acikirsaliyeotarihi = (String) (new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
    for (IrsaliyeBilesen ib : irsaliyebilesenopen) {
        acikirsaliyeid = ib.getIrsaliyeid();
        acikirsaliyefirmaid = ib.getIrsaliyefirmaid();
        acikirsaliyeno = ib.getIrsaliyeno();
        acikirsaliyeotarihi = (String) (new SimpleDateFormat("dd-MM-yyyy").format(ib.getOlusturmatarihi()));
        break;
    }

    for (Irsaliye ik : irsaliyekapali) {
        kapaliirsaliyeno = ik.getIrsaliyeno();
        break;
    }
%>

<script>
    let mamul = [
        <%String delimeter = "";
        for (Stok s : stok) {
            out.println(delimeter
            + " { id:" + s.getId()
            + ",mamulid:" + s.getBilesenid()
            + ",mamulad:'" + StringUtils.replace(s.getBilesenad(), "'", "\"") + "'"
            + ",mamulkod:'" + s.getBilesenkod() + "'"
            + ",gkrno:'" + s.getGkrnostr() + "'"
            + ",miktar:'" + s.getMiktarstr() + "'"
            + ",stokid:'" + s.getStokidstr() + "'"
            + ",firmaid:" + s.getFirmaid()
            + ",firmaad:'" + StringUtils.replace(s.getFirmaad(), "'", "\"") + "'}");
            delimeter = ",";
        }%>];

    let mamul2 = [
        <%delimeter = "";
        for (Stok s : stok) {
            out.println(delimeter + " { value:'"+ s.getBilesenkod() + "', label:'" + s.getBilesenkod() + " ["+ StringUtils.replace(s.getBilesenad(), "'", "\"")  +"]',id:" + s.getBilesenid() + "}");
            delimeter = ",";
        }%>];
</script>

<div class="container">
    <div class="row text-warning" style="text-align:center;">
        <label class="text-danger rounded">Ürün Sevkiyatı</label>
    </div>
    <div class="row">
        <form class="form-inline" name="irsaliyeform" id="irsaliyeform" method="post" action="irsaliye"
              enctype="multipart/form-data">
            <%
                String disabled = "";
                String readonly = "";
                if (!acikirsaliyeno.equals("")) {
                    disabled = "disabled='disabled'";
                    readonly = "readonly='readonly'";
                }
            %>
            <div class="form-group">
                <label for="tarih" class="text-baslik">Tarih</label>
                <div>
                    <input type="text" class="form-control xm" name="tarih" id="tarih"
                           placeholder="Tarih" <%=disabled%> <%=readonly%>>
                </div>
            </div>

            <div class="form-group">
                <label for="firmaid" class="text-baslik">Müşteri</label>
                <div>
                    <select class="form-control big" name="firmaid" id="firmaid" <%=disabled%> <%=readonly%> required
                            pattern="^[1-9][0-9]*$">
                        <%
                            out.println("<option value='0'>... Müşteri Seçiniz ...</option>");
                            for (Firma f : firma) {
                                if (acikirsaliyefirmaid == f.getId()) {
                                    out.println("<option value='" + f.getId() + "' selected>" + f.getAd() + "</option>");
                                } else {
                                    out.println("<option value='" + f.getId() + "'>" + f.getAd() + "</option>");
                                }
                            }
                        %>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label for="irsaliyeno" class="text-baslik">İrsaliye No</label>
                <div>
                    <input type="number" required class="form-control xm" name="irsaliyeno" id="irsaliyeno"
                           value="<%=acikirsaliyeno%>" <%=readonly%> placeholder="İrsaliye No" autocomplete="off">
                    <input type="hidden" name="acikirsaliyeno" id="acikirsaliyeno" value="<%=acikirsaliyeno%>">
                </div>
            </div>

            <div class="form-group">
                <label for="sonirsaliyeno" class="text-baslik">Son İrsaliye No</label>
                <div>
                    <input type="text" class="form-control xm" name="sonirsaliyeno" id="sonirsaliyeno"
                           value="<%=kapaliirsaliyeno%>" readonly="readonly" disabled="disabled">
                </div>
            </div>

            <div class="clearfix"></div>

            <div class="form-group">
                <label for="mamulkod" class="text-baslik">Mamül Kodu</label>
                <div>
                    <input type="text" class="form-control small" name="mamulkod" id="mamulkod" autocomplete="off">
                    <input type="hidden" name="mamulid" id="mamulid">
                </div>
            </div>

            <div class="form-group">
                <label for="mamulad" class="text-baslik">Mamül Adı</label>
                <div>
                    <input type="text" class="form-control small" name="mamulad" id="mamulad" readonly="readonly">
                </div>
            </div>

            <div class="form-group">
                <label for="mamulad" class="text-baslik">Firma Adı</label>
                <div>
                    <input type="text" class="form-control small" name="bilesenfirmaad" id="bilesenfirmaad"
                           readonly="readonly">
                    <input type="hidden" class="form-control small" name="bilesenfirmaid" id="bilesenfirmaid">
                </div>
            </div>

            <div class="form-group">
                <label for="gkrno" class="text-baslik">İzl.No / Toplam Ad.</label>
                <div>
                    <select class="form-control small" name="gkrno" id="gkrno" <%=readonly%>></select>
                    <input type="hidden" name="stokid" id="stokid">
                    <input type="hidden" name="miktarbox" id="miktarbox">
                </div>
            </div>

            <div class="form-group">
                <label for="miktar" class="text-baslik">Sevk Adedi</label>
                <div>
                    <input type="number" class="form-control xs text-right" name="miktar" id="miktar"
                           autocomplete="off" required pattern="^[1-9][0-9]*$"><span class="birim">Ad.</span>
                </div>
            </div>

            <div class="form-group">
                <label for="not" class="control-label text-baslik">Açıklama</label>
                <div>
                    <input type="text" class="form-control normal" name="not" id="not" autocomplete="off">
                </div>
            </div>

            <div class="clearfix"></div>
            <div class="form-group">

                <label for="irsaliye_yukle" class="text-baslik">Excel'den Yükle</label>
                <div>
                    <input type="file" class="form-control-file normal" name="irsaliye_yukle"
                           id="irsaliye_yukle" required pattern=".*\.xls[x]?"
                            <%= (acikirsaliyeid == 0) ? "disabled" : "" %> />
                </div>

            </div>

            <div class="form-group">
                <label for="irsaliye_reset" class="text-baslik">&nbsp;</label>
                <div>
                    <button type="button" class="btn btn-success xs" name="irsaliye_reset" id="irsaliye_reset"
                            style="margin-left:4px; display:none;">Reset
                    </button>
                </div>
            </div>

            <div class="clearfix"></div>
            <%
                if (admin != null && (admin.equals("1") || (admin.equals("3")))) {
            %>
            <div class="form-group" style="margin:20px 0;">
                <div>
                    <button type="button" class="btn btn-danger" name="irsaliyeekle" id="irsaliyeekle"><i
                            class="fas fa-upload iki-bosluk"></i>İrsaliyeye Ekle
                    </button>
                    <button type="button" class="btn btn-danger" name="irsaliyeiptal" id="irsaliyeiptal"
                            style="display:none;">İptal
                    </button>
                    <input type="hidden" name="islemid" id="islemid" value="0">
                    <input type="hidden" name="irsaliyeid" id="irsaliyeid" value="<%=acikirsaliyeid%>">
                    <input type="hidden" name="irsaliyebilesenid" id="irsaliyebilesenid" value="0">
                </div>
            </div>
            <div class="clearfix"></div>
            <%
                }
            %>
        </form>
    </div>

    <% if (pageflash != null && !pageflash.toString().isEmpty()) { %>
    <div class="row alert alert-warning alert-dismissible" role="alert" style="background-color: darkkhaki;">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
        <strong style="color:green;">BULUNAMAYAN MAMÜL KODLARI</strong>
        <hr/>
        <span style="color: #333333;"><%= pageflash %></span>
    </div>
    <% } %>

    <div class="row" style="font-size:12px;">
        <table class="tableplan">
            <script type="text/javascript" charset="utf-8">
                let paket = {};
                let pakettemp = {};
            </script>
            <%
                int syc = 0;
                String not = "";
                for (IrsaliyeBilesen ib : irsaliyebilesenopen) {
            %>
            <script type="text/javascript" charset="utf-8">
                pakettemp = {
                    id: "<%=ib.getId()%>",
                    irsaliyeid: "<%=ib.getIrsaliyeid()%>",
                    tarihtr: "<%=Util.getTarihTR(ib.getOlusturmatarihi())%>",
                    tarihtrshort: "<%=Util.getTarihTRShort(ib.getOlusturmatarihi())%>",
                    firmaad: "<%=ib.getFirmaad()%>",
                    firmaid: "<%=ib.getFirmaid()%>",
                    mamulad: "<%=ib.getMamulad()%>",
                    mamulid: "<%=ib.getMamulid()%>",
                    mamulkod: "<%=ib.getMamulkod()%>",
                    miktar: "<%=ib.getMiktar()%>",
                    gkrno: "<%=ib.getGkrno()%>",
                    stokid: "<%=ib.getStokid()%>",
                    not: "<%=(ib.getNot() == null) ? "" : StringUtils.replace(ib.getNot(), "'", "\"") %>"
                };
                paket[<%=ib.getId()%>] = pakettemp;
            </script>
            <%
                if (syc++ == 0) {
            %>
            <tr>
                <th>Tarih</th>
                <th>İrsaliye No</th>
                <th>Firma</th>
                <th>Ürün Kodu</th>
                <th>Ürün Adı</th>
                <th>İzleme No</th>
                <th>Sevk Adedi</th>
                <th>Açıklama</th>
                <%
                    if (admin != null && (admin.equals("1") || (admin.equals("3")))) {
                %>
                <th class="text-center">Aksiyon</th>
                <%
                    }
                %>
            </tr>
            <%
                }
            %>
            <tr id="tr<%=ib.getId()%>">
                <%
                    not = (ib.getNot() == null) ? "" : StringUtils.replace(ib.getNot(), "'", "\"");
                %>
                <td nowrap="nowrap"><%=Util.getTarihTR(ib.getOlusturmatarihi())%>
                </td>
                <td><%=ib.getIrsaliyeno()%>
                </td>
                <td><%=ib.getFirmaad()%>
                </td>
                <td><%=ib.getMamulkod()%>
                </td>
                <td><%=ib.getMamulad()%>
                </td>
                <td class="text-right"><%=ib.getGkrno()%>
                </td>
                <td class="text-right"><%=ib.getMiktar()%>
                </td>
                <td><%=not%>
                </td>
                <%
                    if (admin != null && (admin.equals("1") || (admin.equals("3")))) {
                %>
                <td class="text-center">
                    <div id="div<%=ib.getId()%>">
                        <a href="javascript:irsaliyepaketguncelle('#tr<%=ib.getId()%>',<%=ib.getId()%>);"
                           title="Güncelle"><span class="fa fa-sync fa-lg text-warning"></span></a>
                        <a href="javascript:irsaliyebilesensil('#tr<%=ib.getId()%>',<%=ib.getGkrno()%>,<%=ib.getMiktar()%>,<%=ib.getId()%>);"
                           title="Sil"><span class="fa fa-minus-circle fa-lg text-danger"></span></a>
                    </div>
                </td>
                <%
                    }
                %>
            </tr>
            <%
                }
            %>
        </table>

    </div>

    <%
        if (syc > 0) {
            if (admin != null && (admin.equals("1") || (admin.equals("3")))) {
    %>
    <div class="row size12px">
        <div class="form-group">
            <div class="text-right">
                <button type="button" class="btn btn-sevk" name="irsaliyetamamla" id="irsaliyetamamla">İrsaliyeyi
                    Tamamla
                </button>
            </div>
        </div>
    </div>
    <%
            }
        }
    %>

    <div class="row" style="font-size:12px;">
        <div>
            <table class="tableplan">
                <%
                    int sayac = 0;
                %>
                <%
                    for (Irsaliye i : irsaliye) {
                %>
                <%
                    if (sayac++ == 0) {
                %>
                <tr>
                    <td><label class="text-success">İrsaliye No</label></td>
                    <td><label class="text-success">Oluşturma Tarihi</label></td>
                    <td><label class="text-success">Gönderim Tarihi</label></td>
                    <td><label class="text-success">Müşteri</label></td>
                    <td class="text-center"><label class="text-success">Aksiyon</label></td>
                </tr>
                <%
                    }
                %>
                <form name=action_form<%=i.getId()%> id="action_form<%=i.getId()%>">
                    <tr id="tr<%=i.getId()%>">
                        <td><%=i.getIrsaliyeno()%>
                        </td>
                        <td><%=Util.getTarihTR(i.getOlusturmatarihi())%>
                        </td>
                        <td><%=Util.getTarihTR(i.getGonderimtarihi())%>
                        </td>
                        <td><%=(i.getFirmaad() == null) ? "" : i.getFirmaad() %>
                        </td>
                        <td class="text-center">
                            <div id="divirsaliye<%= i.getId() %>">
                                <a href="javascript:hideshow('#tr_irs_detay<%= i.getId() %>');" title="İçerik"><span
                                        class="fa fa-chevron-down fa-lg text-success"></span></a>
                                <%
                                    if (admin != null && admin.equals("1")) {
                                %>
                                <a href="javascript:okGoIrsaliyePaket('irsaliye',<%=i.getId()%>,'<%=i.getIrsaliyeno() %>',4);"
                                   title="Onayla"><span class="fa fa-check-circle fa-lg text-success"></span></a>
                                <a href="javascript:deleteGoIrsaliyePaket('irsaliye',<%=i.getId()%>,'<%=i.getIrsaliyeno() %>',3);"
                                   title="Sil"><span class="fa fa-minus-circle fa-lg text-danger"></span></a>
                                <% } %>
                            </div>

                        </td>
                    </tr>
                    <tr id="tr_irs_detay<%= i.getId() %>" style="display:none;">
                        <td colspan="5">
                            <table style="width:100%;" class="tableplan">
                                <% int sayacbilesen = 0; %>
                                <% for (IrsaliyeBilesen j : irsaliyebilesencompleted) {%>
                                <% if (j.getIrsaliyeid().intValue() == i.getId().intValue()) { %>
                                <% if ((sayacbilesen++ == 0)) { %>
                                <tr>
                                    <td><label class="text-danger">Sıra No</label></td>
                                    <td><label class="text-danger">İrsaliye İd</label></td>
                                    <td><label class="text-danger">Mamül Adı</label></td>
                                    <td><label class="text-danger">Mamül Kodu</label></td>
                                    <td><label class="text-danger">Firma</label></td>
                                    <td><label class="text-danger">GKR.No</label></td>
                                    <td><label class="text-danger">Miktarı</label></td>
                                </tr>
                                <% } %><%-- if tablo baslik --%>
                                <tr>
                                    <td><%= sayacbilesen %>
                                    </td>
                                    <td><%= j.getIrsaliyeid() %>
                                    </td>
                                    <td><%= j.getMamulad() %>
                                    </td>
                                    <td><%= j.getMamulkod() %>
                                    </td>
                                    <td><%= j.getFirmaad() %>
                                    </td>
                                    <td><%= j.getGkrno() %>
                                    </td>
                                    <td><%= j.getMiktar() %>
                                    </td>
                                </tr>
                                <% } %><%-- if  i== j --%>
                                <% } %><%-- irsaliyebilesen --%>
                            </table>
                        </td>
                    </tr>
                </form>
                <% } %> <%-- irsaliye --%>
            </table>
        </div>
    </div>

</div>

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/jquery-ui.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/irfan.js?<%=Util.nanoTime%>" type="text/javascript"></script>
<script src="js/irsaliye.js?<%=Util.nanoTime%>" type="text/javascript"></script>
<script>
    let today = '<%= acikirsaliyeotarihi.equals("")?"today":acikirsaliyeotarihi %>';
    $(function () {

        $('#tarih').datepicker({
            inline: true,
            format: 'dd-mm-yy',
            firstDay: 1,
        }).datepicker("option", "dateFormat", "dd-mm-yy").datepicker("setDate", today);

        $("#mamulkod").autocomplete({
            source: mamul2,
            minLength: 1,
            select: function (event, ui) {
                //$("#go").removeAttr("disabled");
                //console.log(ui.item.id);
                //console.log(ui.item.value);
                //console.log(event);
                changefun(ui.item);
                $(this).val(ui.item.value);
            }
        });

        $("#mamulkod").change(function () {
            changefun(this);
        });
    });

    function changefun(elem) {
        let found = false;
        if (elem != null) {
            $("#gkrno option").remove();
            for (i in mamul) {
                if (elem.value == mamul[i].mamulkod) {
                    found = true;
                    document.irsaliyeform.mamulad.value = mamul[i].mamulad;
                    document.irsaliyeform.mamulid.value = mamul[i].mamulid;
                    document.irsaliyeform.bilesenfirmaid.value = mamul[i].firmaid;
                    document.irsaliyeform.bilesenfirmaad.value = mamul[i].firmaad;
                    if ($("#firmaid").val() == 0) {
                        document.irsaliyeform.firmaid.value = mamul[i].firmaid;
                    }
                    ;
                    //console.log(mamul[i].gkrno);
                    let gkrno = mamul[i].gkrno.split(";");
                    let miktar = mamul[i].miktar.split(";");
                    let stokid = mamul[i].stokid.split(";");
                    let option = "";
                    let total_miktar = 0;
                    for (let j = 0; j < gkrno.length; j++) {
                        option += '<option value="' + gkrno[j] + '">' + gkrno[j] + ", Adet:" + miktar[j] + '</option>';
                        total_miktar += parseInt(miktar[j]);
                    }
                    $("#gkrno").append(option);
                    $("#stokid").val(stokid[0]);
                    $("#miktarbox").val(total_miktar);
                }
            }
        }
        if (!found) {
            document.irsaliyeform.mamulad.value = "";
            document.irsaliyeform.mamulid.value = "";
            document.irsaliyeform.bilesenfirmaad.value = "";
            document.irsaliyeform.bilesenfirmaid.value = "";
            document.irsaliyeform.miktar.value = "";
            document.irsaliyeform.not.value = "";
            $("#gkrno option").remove();
            $('#stokid').val("");
            $('#miktarbox').val("");
            //document.irsaliyeform.gkrno.value	= "";
        }
    }
</script>
</body>
</html>