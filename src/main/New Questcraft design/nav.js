
let x = document.getElementById("nav-mobile");
let y = document.getElementById("nav")
let z = document.getElementById("nav-bar")
let logo = document.getElementById("logo-menu");
let center = document.getElementById("center");
x.addEventListener('click', () => {
    if (y.className == "nav") {
        y.className = "nav-mobile";
        z.setAttribute('style', 'height: 390px');
        logo.setAttribute('style', 'border-bottom: 5px solid rgba(236, 236, 236, 0.5);');
        center.setAttribute('style', 'top: 540px');
    } else {
        y.className = "nav";
        z.setAttribute('style', 'height: 100px');
        logo.setAttribute('style', 'border-bottom: 0;');
        center.setAttribute('style', 'top: 250px');
    }
}, false)