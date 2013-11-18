
var modalBody = $("#pressThisModal .modal-body");

$("#pressThisModal").on("show.bs.modal", function() {
    $("#pressThisModal .modal-footer button").text("Cancel");
    modalBody.html($("#modal-form-template").html());
    $("#pressThisForm").on("submit", function(event) {
        event.preventDefault();
        $(this).find("button[type=submit]").addClass("disabled").attr("disabled", true);
        modalBody.append(Handlebars.compile($("#modal-processing-template").html()));
        console.log($(this).serialize());
        submitPressThisForm(this);
    });
});



function submitPressThisForm(form) {
    $.post("/wordpress", $(form).serialize(), function (data) {
        modalBody.html('<p class="text-success">Bingo!  Go and check out your new post in Wordpress!</p>')
        $("#pressThisModal .modal-footer button").text("OK");
    })
}