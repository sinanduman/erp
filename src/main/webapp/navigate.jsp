<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="erp.entity.User" %>
<%
    User user = (User) request.getSession().getAttribute("user");
    int bakimZamani = (int) request.getSession().getAttribute("bakimzamani");
%>
<div class="container" style="margin-top:20px;">

    <div class="row">
        <!-- /btn-group -->
        <div class="btn-group">
            <button type="button" class="btn btn-primary">Üretim</button>
            <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
                <span class="caret"></span>
                <span class="sr-only">Toggle Dropdown</span>
            </button>
            <ul class="dropdown-menu">
                <li>
                    <a href="uretimtakip">
                        <span class="fas fa-bullseye text-primary"></span> Üretim Takibi</a>
                </li>
                <li>
                    <a href="uretimplan">
                        <span class="fas fa-bullseye text-primary"></span> Üretim Planlama</a>
                </li>
            </ul>
        </div>
        <!-- /btn-group -->
        <div class="btn-group">
            <button type="button" class="btn btn-success">Stok</button>
            <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
                <span class="caret"></span>
                <span class="sr-only">Toggle Dropdown</span>
            </button>
            <ul class="dropdown-menu">
                <li>
                    <a href="hammaddestok"><span class="fas fa-bullseye text-success"></span> Hammadde ve Yarı Mamül</a>
                </li>
                <!-- li>
                    <a href="yarimamulstok"><span class="fas fa-bullseye text-success"></span> Yarı Mamül</a>
                </li-->
                <li>
                    <a href="mamulstok"><span class="fas fa-bullseye text-success"></span> Mamül</a>
                </li>
            </ul>
        </div>
        <!-- /btn-group -->
        <div class="btn-group">
            <button type="button" class="btn btn-orange">Tanımlar</button>
            <button type="button" class="btn btn-orange dropdown-toggle" data-toggle="dropdown">
                <span class="caret"></span>
                <span class="sr-only">Toggle Dropdown</span>
            </button>
            <ul class="dropdown-menu">
                <li>
                    <a href="hammadde">
                        <span class="fas fa-bullseye text-orange"></span> Hammadde ve Yarı Mamül</a>
                </li>
                <li>
                    <a href="mamul">
                        <span class="fas fa-bullseye text-orange"></span> Mamül</a>
                </li>
                <li class="divider"></li>
                <li>
                    <a href="calisan">
                        <span class="fas fa-bullseye text-orange"></span> Çalışan</a>
                </li>
                <li>
                    <a href="makina">
                        <span class="fas fa-bullseye text-orange"></span> Makina/Bant</a>
                </li>
                <li>
                    <a href="firma">
                        <span class="fas fa-bullseye text-orange"></span> Firma</a>
                </li>
            </ul>
        </div>
        <!-- /btn-group -->
        <div class="btn-group">
            <button type="button" class="btn btn-danger">Raporlar</button>
            <button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown">
                <span class="caret"></span>
                <span class="sr-only">Toggle Dropdown</span>
            </button>
            <ul class="dropdown-menu">
                <li>
                    <a href="stokrapor">
                        <span class="fas fa-bullseye text-danger"></span> Stok</a>
                </li>
                <li>
                    <a href="uretimrapor">
                        <span class="fas fa-bullseye text-danger"></span> Üretim</a>
                </li>
                <li>
                    <a href="sevkrapor">
                        <span class="fas fa-bullseye text-danger"></span> Sevk</a>
                </li>
                <li>
                    <a href="gkrliste">
                        <span class="fas fa-bullseye text-danger"></span> GKR Listesi</a>
                </li>
                <li>
                    <a href="izlemeno">
                        <span class="fas fa-bullseye text-danger"></span> Mamül İzleme No</a>
                </li>

                <li>
                    <a href="bakimrapor">
                        <span class="fas fa-bullseye text-danger"></span> Kalıp Bakım</a>
                </li>
            </ul>
        </div>
        <!-- /btn-group -->
        <div class="btn-group">
            <button type="button" class="btn btn-sevk">Sevk</button>
            <button type="button" class="btn btn-sevk dropdown-toggle" data-toggle="dropdown">
                <span class="caret"></span>
                <span class="sr-only">Toggle Dropdown</span>
            </button>
            <ul class="dropdown-menu">
                <li>
                    <a href="irsaliye">
                        <span class="fas fa-bullseye text-sevk"></span> İrsaliye Hazırla</a>
                </li>
                <li>
                    <a href="irsaliyegonder">
                        <span class="fas fa-bullseye text-sevk"></span> Gönderilen İrsaliyeler</a>
                </li>
            </ul>
        </div>

        <div class="btn-group" style="width:10px;"> </div>

        <div class="btn-group">
            <button type="button" class="btn btn-info dropdown-toggle"
                    style="background-color:#E1E1C8;border:1px solid darkkhaki; color:darkkhaki;"
                    data-toggle="dropdown">
                <%= user.getFullName()  %> <span class="caret" style="border-top-color:darkkhaki;"></span>
            </button>
            <ul class="dropdown-menu">
                <li>
                    <a href="logout">
                        <span class="fa fa-sign-out-alt" style="color:darkkhaki;"></span> Çıkış</a>
                </li>
            </ul>
        </div>

        <div class="btn-group" style="width:10px;"> </div>

        <div class="btn-group">
            <ul class="nav nav-pills" role="tablist">
                <% if (bakimZamani > 0) { %>
                <li role="presentation" class="active"><a href="bakimrapor">Bakım<span class="badge"><%= bakimZamani %></span></a></li>
                <% } %>
            </ul>
            <!--
            <a href="#"><span class="text-danger" style="font-size:24px;margin-left:10px;" title="Kalıp Bakımı Gelen Mamüller Var!"><i class="fas fa-compass fa-spin"></i></span></a>
            <a href="#"><span class="text-success" style="font-size:24px;margin-left:10px;" title="Stoğu Azalan Hammaddeler Var!"><i class="fas fa-compass fa-spin"></i></span></a>
            <a href="#"><span class="text-sevk" style="font-size:24px;margin-left:10px;" title="Stoğu Azalan Hammaddeler Var!"><i class="fas fa-compass fa-spin"></i></span></a>
            -->
        </div>
    </div>
</div>
<hr/>
<!-- container -->
