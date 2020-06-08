var $post = $("body");

setInterval(function(){
    $post.toggleClass("wink");
}, 2000);

//// Your delay in milliseconds
var delay = 5000;
setTimeout(function(){ window.location = "/vote-successs" }, delay);