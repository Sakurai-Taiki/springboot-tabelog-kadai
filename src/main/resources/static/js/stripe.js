const stripe = Stripe('sk_test_51QJQLVFoKQ3torxMzQAcfUd95ChrnGKHJQexrL3QFq6R0kvvk7Bb5unnzI95qeyjZD9AMmdVTbtggkCPBFOQE1IN00dwLQ0v1d');
 const paymentButton = document.querySelector('#paymentButton');
 
 paymentButton.addEventListener('click', () => {
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });