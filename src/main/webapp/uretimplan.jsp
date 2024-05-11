<%@ page import="erp.entity.*" %>
<%@ page import="erp.util.Util,java.text.SimpleDateFormat,java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.text.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="css/irfan.css?<%=Util.nanoTime%>">
    <link rel="stylesheet" href="css/fonts.css">
    <link rel="stylesheet" href="css/all.css">
    <link rel="stylesheet" href="css/jquery-ui-1.10.0.custom.css">
    <link rel="stylesheet/less" type="text/css" href="less/datepicker.less">

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="css/bootstrap.css?<%=Util.nanoTime%>">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="assets/js/html5shiv.js"></script>
    <script src="assets/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<%@ include file="logincheck.jsp" %>
<%
    List<HataSebep> hatasebep = (List<HataSebep>) request.getAttribute("hatasebep");
    List<Calisan> calisan = (List<Calisan>) request.getAttribute("calisan");
    List<Makina> makina = (List<Makina>) request.getAttribute("makina");
    List<Mamul> mamul = (List<Mamul>) request.getAttribute("mamul");
    List<UretimPlan> uretimplan = (List<UretimPlan>) request.getAttribute("uretimplan");

    String tarih = (String) ((request.getAttribute("tarih") == null) ? (new SimpleDateFormat("dd-MM-yyyy").format(new Date())) : Util.date_eng_to_tr((String) request.getAttribute("tarih")));
    String makinaid = (String) ((request.getAttribute("makinaid") == null || request.getAttribute("makinaid") == "") ? "" : request.getAttribute("makinaid"));
    String excelsql = (String) request.getAttribute("excelsql");
    String admin = (String) session.getAttribute("admin");
%>

<script>
    let mamul = [
        <%String delimeter = "";
        for (Mamul m : mamul) {
            out.println(delimeter
        + " { id:" + m.getId()
        + ",mamulid:" + m.getId()
        + ",mamulad:'" + StringEscapeUtils.escapeEcmaScript(m.getAd()) + "'"
        + ",mamulkod:'" + m.getKod() + "'"
        + ",hammadde:'" + StringEscapeUtils.escapeEcmaScript(m.getHammadde()) + "'"
        + ",hammaddegkrno:'" + m.getHammaddegkrno() + "'"
        + ",yarimamul:'" + StringEscapeUtils.escapeEcmaScript(m.getYarimamul()) + "'"
        + ",yarimamulgkrno:'" + m.getYarimamulgkrno() + "'"
        + ",firmaad:'" + StringEscapeUtils.escapeEcmaScript(m.getFirmaad()) + "'"
        + ",cevrimsure:" + m.getCevrimsuresi()
        + ",agirlik:" + m.getAgirlik() + "}");
            delimeter = ",";
        }%>];
    let mamul2 = [
        <%delimeter = "";
        for (Mamul m : mamul) {
            out.println(delimeter + " { label:'" + m.getKod() + "'" + ",id:" + m.getId() + "}");
            delimeter = ",";
        }%>];

    let hatasebep = {
        <%delimeter = "";
        for (HataSebep h : hatasebep) {
            out.println(delimeter + "'" + h.getKod() + "'" +": { id:" + h.getId()
        + ",hataid:" + h.getId()
        + ",hataad:'" + h.getAd() + "'"
        + ",hatakod:'" + h.getKod() + "'" + "}");
            delimeter = ",";
        }%>
    };

</script>


<div class="container">
    <div class="row text-warning" style="text-align: center;">
        <label class="text-danger rounded">Üretim Planlama</label>
    </div>
    <div class="row">
        <form class="form-inline" name="uretimplanform" id="uretimplanform" method="post" action="uretimplan">
            <div class="form-group">
                <label for="tarih" class="text-baslik">Tarih</label>
                <div>
                    <input type="text" style="width:120px;" class="form-control" name="tarih" id="tarih"
                           placeholder="Tarih">
                </div>
            </div>

            <div class="form-group">
                <label for="makinaid" class="text-baslik">Makina/Band</label>
                <div>
                    <select class="form-control" id="makinaid" name="makinaid">
                        <%
                            out.println("<option value=''>...Tüm...</option>");
                            for (Makina m : makina) {
                                if (m.getId().toString().equals(makinaid)) {
                                    out.print("<option value='" + m.getId() + "' selected>" + m.getAd() + "</option>");
                                } else {
                                    out.print("<option value='" + m.getId() + "'>" + m.getAd() + "</option>");
                                }
                            }
                        %>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label for="uretimplanliste" class="text-success bgsaydam">&nbsp;&nbsp;</label>
                <div>
                    <button type="button" class="btn btn-danger" name="uretimplanliste" id="uretimplanliste">Plan Listesini Getir</button>
                </div>
            </div>

            <div class="clearfix"></div>


            <div class="form-group">
                <label for="mamulkod" class="text-baslik">Mamül Kodu</label>
                <div>
                    <input type="text" class="form-control small" name="mamulkod" id="mamulkod" readonly="readonly"
                           autocomplete="off">
                    <input type="hidden" name="mamulid" id="mamulid">
                    <input type="hidden" name="yarimamulgkrno" id="yarimamulgkrno">
                </div>
            </div>

            <div class="form-group">
                <label for="mamulad" class="text-baslik">Mamül Adı</label>
                <div>
                    <input type="text" class="form-control small" name="mamulad" id="mamulad" readonly="readonly">
                </div>
            </div>

            <div class="form-group">
                <label for="hammadde" class="text-baslik">Hammaddesi</label>
                <div>
                    <input type="text" class="form-control small" name="hammadde" id="hammadde" readonly="readonly">
                </div>
            </div>

            <div class="form-group">
                <label for="agirlik" class="text-baslik">Ağırlık</label>
                <div>
                    <input type="text" class="form-control xs text-right" name="agirlik" id="agirlik"
                           readonly="readonly"><span class="birim">gr.</span>
                </div>
            </div>

            <div class="form-group">
                <label for="cevrimsure" class="text-baslik">Çevrim</label>
                <div>
                    <input type="text" class="form-control xs text-right" name="cevrimsure" id="cevrimsure"
                           readonly="readonly"><span class="birim">sn.</span>
                </div>
            </div>

            <div class="form-group">
                <label for="firmaad" class="text-baslik">Firma</label>
                <div>
                    <input type="text" class="form-control small" name="firmaad" id="firmaad" readonly="readonly">
                </div>
            </div>

            <div class="form-group">
                <label for="calisanid" class="text-baslik">Operatör</label>
                <div>
                    <select class="form-control" id="calisanid" name="calisanid" disabled="disabled">
                        <%
                            for (Calisan c : calisan) {
                                out.println("<option value='" + c.getId() + "'>" + c.getFullName() + "</option>");
                            }
                        %>
                    </select>
                </div>
            </div>

            <div class="clearfix"></div>

            <div class="form-group">
                <label for="hedefuretim" class="text-baslik">Hedef Ürt.</label>
                <div>
                    <input type="text" class="form-control xs text-right" name="hedefuretim" id="hedefuretim"
                           readonly autocomplete="off" disabled><span class="birim">Ad.</span>
                    <input type="hidden" name="hedefuretimhidden" id="hedefuretimhidden">
                </div>
            </div>

            <div class="form-group">
                <%
                    String readonly = "readonly='readonly'";
                    if (admin != null && admin.equals("1")) {
                        readonly = "readonly='readonly'";
                    }
                %>
                <label for="gercekuretim" class="text-baslik">Gerçek Ürt.</label>
                <div>
                    <input type="text" class="form-control xs text-right" name="gercekuretim"
                           id="gercekuretim" <%=readonly%>><span class="birim">Ad.</span>
                    <input type="hidden" mamullistename="gercekuretim_hidden" id="gercekuretim_hidden">
                </div>
            </div>

            <div class="form-group">
                <label for="hata" class="text-baslik">Hata</label>
                <div>
                    <input type="text" class="form-control xss text-right" name="hata" id="hata" <%=readonly%>><span class="birim">Ad.</span>
                </div>
            </div>

            <div class="form-group">
                <label for="sapma" class="text-baslik">Sapma</label>
                <div>
                    <input type="text" class="form-control xss text-right" name="sapma" id="sapma"
                           readonly="readonly"><span class="birim">%</span>
                </div>
            </div>

            <div class="form-group">
                <label for="hatasebepid" class="text-baslik">Açıklama</label>
                <div>
                    <select name="hatasebepid" id="hatasebepid" class="form-control small">
                        <%
                            out.println("<option value='0' selected>...Seçiniz...</option>");
                            for (HataSebep hs : hatasebep) {
                                out.println("<option value='" + hs.getId() + "'>" + hs.getAd() + " (" + hs.getKod() + ")</option>");
                            }
                        %>
                    </select>
                </div>
            </div>

            <div class="clearfix"></div>

            <%
                if (admin != null && (admin.equals("1") || (admin.equals("2")))) {
            %>
            <div class="form-group" style="margin:10px 0;">
                <div>
                    <input type="hidden" name="uretimplanid" id="uretimplanid" value="0">
                    <button type="button" class="btn btn-danger" name="uretimplanguncelle" id="uretimplanguncelle"
                            disabled="disabled">Güncelle
                    </button>
                    <button type="button" class="btn btn-danger" name="uretimplaniptal" id="uretimplaniptal"
                            style="display:none;">İptal
                    </button>
                </div>
            </div>

            <div class="clearfix"></div>
            <%
                }
            %>

        </form>
    </div>

    <%
        if (uretimplan.size() > 0) {
    %>
    <div class="row text-warning" style="text-align: center;">
        <label class="text-danger roundedmini"><%=tarih%> Tarihli Üretim Planlaması</label>
    </div>
    <%
        }
    %>
    <%
        int sayac = 0;
    %>
    <div class="row" style="font-size:12px;">
        <table class="tableplan">
            <script type="text/javascript" charset="utf-8">
                let plan = {};
                let plantemp = {};
            </script>
            <%
                for (UretimPlan u : uretimplan) {
            %>
            <script type="text/javascript" charset="utf-8">
                plantemp = {
                    id: "<%=u.getId()%>",
                    tarihtr: "<%=u.getTarihTR()%>",
                    tarihtrshort: "<%=u.getTarihTRShort()%>",
                    baslangic: "<%=u.getBasZaman()%>",
                    bitis: "<%=u.getBitZaman()%>",
                    makinaad: "<%=u.getMakinaad()%>",
                    makinaid: "<%=u.getMakinaid()%>",
                    calisanad: "<%=u.getCalisanShortName()%>",
                    calisanid: "<%=u.getCalisanid()%>",
                    firmaad: "<%=u.getFirmaad()%>",
                    firmaid: "<%=u.getFirmaid()%>",
                    hammadde: "<%=u.getHammaddead()%>",
                    gkrno: "<%=u.getHammaddeizlno()%>",
                    mamulad: "<%=u.getMamulad()%>",
                    mamulid: "<%=u.getMamulid()%>",
                    mamulkod: "<%=u.getMamulkod()%>",
                    uretimadet: "<%=u.getUretilenmiktar()%>",
                    hedefuretim: "<%=(u.getPlanlananmiktar()==0) ? "" : u.getPlanlananmiktar()%>",
                    fark: "<%=(u.getFark()==0)? "" : u.getFark()%>",
                    sapma: "<%=(u.getSapma()==0)? "" : u.getSapma()%>",
                    hataliadet: "<%=(u.getHatalimiktar()==0) ? "" : u.getHatalimiktar()%>",
                    hataid: "<%=u.getHataid()%>",
                    hatakod: "<%=u.getHatakodu()%>",
                    duruszaman: "<%=(u.getDuruszaman()==0)? "" : u.getDuruszaman()%>",
                    durusid: "<%=u.getDurusid()%>",
                    duruskod: "<%=u.getDuruskodu()%>",
                    izlemeno: "<%=u.getMamulizlno()%>",
                    agirlik: "<%=u.getAgirlik()%>"
                };
                plan[<%=u.getId()%>] = plantemp;
            </script>
            <%
                if (sayac++ == 0) {
            %>
            <tr>
                <th>Tarih</th>
                <th>Başl.</th>
                <th>Bit.</th>
                <th>Makina</th>
                <th>Operatör</th>
                <th>Müşteri</th>
                <th>Hammadde</th>
                <th>GKR.No</th>
                <th>Ürün Kodu</th>
                <th>Ürün Adı</th>
                <th>İzl.No</th>
                <th>Planlanan</th>
                <th>Üretilen</th>
                <th>Hata</th>
                <th>Sapma</th>
                <th>Açıklama</th>
                <th>Aksiyon</th>
            </tr>
            <%
                }
            %>
            <tr id="tr<%=u.getId()%>">
                <td nowrap><%=u.getTarihTRShort()%></td>
                <td><%=u.getBasZaman()%></td>
                <td><%=u.getBitZaman()%></td>
                <td nowrap><%=u.getMakinaad()%></td>
                <td><%=u.getCalisanShortName()%></td>
                <td><%=u.getFirmaad()%></td>
                <td><%=Util.splitLine(u.getHammaddead(), ";", "<br>")%></td>
                <td><%=Util.splitLine(u.getHammaddeizlno(), ";", "<br>")%></td>
                <td><%= u.getMamulkod() %></td>
                <td><%= u.getMamulad() %></td>
                <td><%= (u.getMamulizlno() == 0) ? "" : u.getMamulizlno() %></td>
                <td class="text-right" nowrap><%= (u.getPlanlananmiktar() == 0) ? "" : u.getPlanlananmiktar() + " Ad." %></td>
                <td class="text-right" nowrap><%= u.getUretilenmiktar() + " Ad." %></td>
                <td class="text-right" nowrap><%= (u.getHatalimiktar() == 0) ? "" : u.getHatalimiktar() + " Ad." %></td>
                <td class="text-right" nowrap><%= (u.getSapma() == 0) ? "" : "% " + u.getSapma() %></td>
                <td><%= u.getHataad() %></td>
                <td>
                    <div class="text-center" id="div<%= u.getId() %>">
                        <a href="javascript:tr_uretimplanguncelle('#tr<%= u.getId() %>',<%= u.getId() %>);"
                           title="Güncelle"><span class="fa fa-sync fa-lg text-warning"></span></a>
                    </div>
                </td>
            </tr>
            <% } %>
        </table>

        <%--For displaying Previous link except for the 1st page --%>
        <%--For displaying Page numbers.
        The when condition does not display a link for the current page--%>

        <%
            String param1 = (tarih == null || tarih.equals("")) ? "" : "&amp;tarih=" + tarih;
            String param2 = (makinaid == null || makinaid.equals("")) ? "" : "&amp;makinaid=" + makinaid;
        %>
        <jsp:include page="paging.jsp">
            <jsp:param value="noofpages" name="noofpages"/>
            <jsp:param value="currentpage" name="currentpage"/>
            <jsp:param value="uretimplan" name="pagename"/>
            <jsp:param value="<%=param1 %>" name="param1"/>
            <jsp:param value="<%=param2 %>" name="param2"/>
        </jsp:include>
    </div>

    <%
        if (sayac > 0) {
    %>
    <div class="row text-right">
        <div>
            <form method="post" name="excelform" id="excelform" action="excel">
                <button type="button" class="btn btn-danger" name="exceleaktar" id="exceleaktar">Excele Aktar</button>
                <input type="hidden" value="0" name="excelegonder" id="excelegonder">
                <input type="hidden" name="exceltarih" id="exceltarih">
                <input type="hidden" name="excelsql" id="excelsql" value="<%=excelsql%>">
            </form>
        </div>
    </div>
    <%
        }
    %>
    <div class="row" style="font-size:12px;margin-top:10px;">
        <label class="text-danger">Not:</label>
        <br>
        <span class="text-danger"><i class="fas fa-paperclip"></i></span> <span class="text-success">Buradaki veriler herhangi bir sayfaya etki etmez, değiştirmez. (Stok girmek, düşmek, vs.)</span>
        <br>
        <span class="text-danger"><i class="fas fa-paperclip"></i></span> <span class="text-success">Sadece Hedef Üretim ve Fark varsa Açıklama girilebilir.</span>
        <br>
    </div>
    <div id="okdiv" class="bb-alert alert alert-success" style="display:none;">
        <span>Confirm result: false</span>
    </div>
    <div id="errordiv" class="bb-alert alert alert-danger" style="display:none;">
        <span>Confirm result: false</span>
    </div>

</div>

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/jquery-ui.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/bootbox.js" type="text/javascript"></script>
<script src="js/irfan.js?<%=Util.nanoTime%>" type="text/javascript"></script>
<script src="js/uretim-plan.js?<%=Util.nanoTime%>" type="text/javascript"></script>
<script>
    let today = '<%= tarih.equals("")?"today":tarih %>';
    $(function () {
        $('#tarih').datepicker({
            inline: true,
            format: 'dd-mm-yy',
            firstDay: 1
        }).datepicker("option", "dateFormat", "dd-mm-yy").datepicker("setDate", today);
    });
    $("#mamulkod").autocomplete({
        source: mamul2,
        minLength: 1,
        select: function (event, ui) {
            changefun(ui.item);
        }
    });
    $("#mamulkod").change(function () {
        changefun(this);
    });

    function changefun(elem) {
        let found = false;
        if (elem != null) {
            for (let j=0; j < mamul.length && !found; j++) {
                if ($(elem).val() == mamul[j].mamulkod) {
                    found = true;
                    $("#mamulad").val(mamul[j].mamulad);
                    $("#mamulid").val(mamul[j].mamulid);
                    $("#firmaad").val(mamul[j].firmaad);
                    $("#hammadde").val(mamul[j].hammadde);
                    $("#agirlik").val(mamul[j].agirlik);
                    $("#cevrimsure").val(mamul[j].cevrimsure);
                }
            }
        }
        if (!found) {
            $("#mamulad").val("");
            $("#mamulid").val("");
            $("#firmaad").val("");
            $("#hammadde").val("");
            $("#agirlik").val("");
            $("#cevrimsure").val("");
        }
    }
</script>
</body>
</html>