const mealAjaxUrl = "meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
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
                    0, "desc"
                ]
            ]
        })
    );
});

function filter() {
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + "filter",
        data: $('#filterForm').serialize()
    }).done(function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
        successNoty("Filtered");
    });
}

function filterReset() {
    $('#filterForm').each(function (index, form) {
        console.log(form);
        form.reset();
    })
}