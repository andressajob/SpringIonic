$(document).ready(function(){
    var get = document.URL;
    if(get.match(/categorias/i)){
        $('#mainNav li:eq(0)').addClass('active');
    }
    if(get.match(/clientes/i)){
        $('#mainNav  li:eq(1)').addClass('active');
    }
    if(get.match(/cidades/i)){
        $('#mainNav  li:eq(2) ul').addClass('show');
        $('#mainNav  li:eq(3)').addClass('active');
    }
    if(get.match(/estados/i)){
        $('#mainNav  li:eq(2) ul').addClass('show');
        $('#mainNav  li:eq(4)').addClass('active');
    }
    if(get.match(/pedidos/i)){
        $('#mainNav  li:eq(5)').addClass('active');
    }
    if(get.match(/produtos/i)){
        $('#mainNav  li:eq(6)').addClass('active');
    }
    if(get.match(/relatorios/i)){
        $('#mainNav  li:eq(7)').addClass('active');
    }
});