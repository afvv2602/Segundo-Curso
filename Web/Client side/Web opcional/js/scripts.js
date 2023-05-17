$(document).ready(function() {
    $('.add-to-cart-btn').on('click', function() {
      var productId = $(this).data('product-id');
      console.log("Producto agregado al carrito: " + productId);
    });
  });
  