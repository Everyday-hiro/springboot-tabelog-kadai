 const stripe = Stripe('pk_test_51PJQXjIzVhtPxktW2YcMOqMVWrLYWy7axjOaTWurSVstrY8ft9NJlNP5NJjOeH6fgne4ys1J2sEDBvbweYuXQ8O500kaopSPBe');
 const paymentButton = document.querySelector('#paymentButton');
 
 paymentButton.addEventListener('click', () => {
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });
