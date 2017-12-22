$(document).ready(function() {
    $('.nav-item').click(function(eventObject) {
        $('.nav-item').removeClass('active');
        $(eventObject.currentTarget).addClass('active');
    });
});