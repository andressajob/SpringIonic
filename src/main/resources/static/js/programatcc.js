/**
 * Created by VictorJr on 26/03/2017.
 */

$(document).ready(function () {
    $('#discount').val(0);
    $('#sellingTotal').val(0);
});

$(document).ready(function() {
    $("#discount, #qtdSelling, #selectProd").change(function(e) {
        var product = $('#selectProd').val();
        $.ajax({
            type:'GET',
            url: '/pedidos/preco/'+ product,
            //contentType: 'application/json',
            success: function (result) {


                $("#sprice").val(result);
                var qtdSelling = parseInt($('#qtdSelling').val());
                var sellingPrice = parseFloat($('#sprice').val());
                var productName = $('#selectProd :selected').text();
                var discount = ($('#discount').val());
                var sellingTotal = (qtdSelling*sellingPrice)-discount;
                $("#totProd").val(sellingTotal.toFixed(2));
            }
        });

    });


    $(calculateSum);

    function calculateSum() {

        var sum = 0;
        //iterate through each td based on class and add the values
        $(".itemTot").each(function() {

            var value = $(this).text();
            // add only if the value is number
            if(!isNaN(value) && value.length != 0) {
                sum += parseFloat(value);
            }

        });
        $('#sellingTotal').val(sum);
    }

});

$("#sumTotal").onclick(function () {
    var sell = $('#sell').val();
    var total = $('#sellingTotal').val();
    $.ajax({
        type:'GET',
        url: '/pedidos/total/'+ sell + '/' + total,
        //contentType: 'application/json',
        success: function (result) {
        }
    })
});










