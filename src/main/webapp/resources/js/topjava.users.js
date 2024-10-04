const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function enable(id, input) {
    console.log("enable", input.checked);
    $.ajax({
        type: "PATCH",
        url: ctx.ajaxUrl + `${id}` + "/enable",
        data: {enabled: input.checked}
    }).done(function () {
        $(input)
            .parents("tr")
            .removeClass("user-enable-on")
            .removeClass("user-enable-off")
            .addClass(input.checked ? "user-enable-on" : "user-enable-off");
        successNoty("enable " + (input.checked ? "On" : "Off"));
    }).fail(function (jqXHR) {
        $(input).prop('checked', !input.checked);
        failNoty(jqXHR);
    });
}