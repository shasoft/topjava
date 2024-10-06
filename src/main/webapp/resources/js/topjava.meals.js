const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

function initDateTimePickerForDateField(id) {
    $('#' + id).datetimepicker({timepicker: false, format: "Y-m-d"});
}

function initDateTimePickerForTimeField(id) {
    $('#' + id).datetimepicker({datepicker: false, format: "H:i"});
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return StringJavaLocalDateTimeToStringUI(data);
                        }
                        return data;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "render": renderEditBtn,
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "render": renderDeleteBtn,
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                if (!data.enabled) {
                    $(row).attr("data-meal-excess", data.excess);
                }
            }
        })
    );

    $("#dateTime").datetimepicker({format: "Y-m-d H:i"});
    initDateTimePickerForDateField("startDate");
    initDateTimePickerForDateField("endDate");
    initDateTimePickerForTimeField("startTime");
    initDateTimePickerForTimeField("endTime");
});