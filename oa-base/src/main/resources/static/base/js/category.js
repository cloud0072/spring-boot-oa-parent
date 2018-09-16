var tree = $("#main-tree");
var form = $('#main_form');

function validateAll() {
    return form.valid();
}

$(function () {
    form.validate({
        rules: {
            name: {
                required: true,
                rangelength: [2, 10]
            },
            categoryOrder: {
                required: true,
                digits: true,
                min: 0
            }
        },
        messages: {
            name: {
                required: "请先填写名称!",
                rangelength: $.validator.format("名称长度必须在 {0} 和 {1} 之间!")
            },
            categoryOrder: {
                required: "请填写分类顺序!",
                digits: "请填写一个整数!",
                min: "顺序最小值为0!"
            }
        },
        /* 更改错误信息显示的位置 */
        errorPlacement: function (error, element) {
            // Add the `help-block` class to the error element
            error.addClass("help-block");

            if (element.prop("type") === "checkbox") {
                error.insertAfter(element.parent("label"));
            } else {
                error.insertAfter(element);
            }
        },
        highlight: function (element, errorClass, validClass) {
            $(element).parents(".col-sm-5").addClass("has-error").removeClass("has-success");
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).parents(".col-sm-5").addClass("has-success").removeClass("has-error");
        }
    });

    if (root) {
        var nodes = [];
        nodes.push(root);
        tree.treeview({
            data: nodes,
            loadingIcon: "fa fa-hourglass",
            lazyLoad: loadNodes,
            onNodeSelected: treeviewSelected
        });
    }
});

function treeviewSelected(event, node) {
    $("#id").val(node.id);
    $('#pid').val(node.pid);
    $("#name").val(node.text);
    $("#categoryOrder").val(node.categoryOrder);
    $("#icon").val(node.icon);
    $("#url").val(node.url);
}

function loadNodes(node, func) {
    $.ajax({
        url: baseUrl + "/findByParentId/" + node.id,
        type: "GET",
        success: function (result) {
            func(result);
        }
    });
}

//新建一个节点
$(".btn-createNode").on('click', function () {
    var select = tree.treeview("getSelected");
    if (select && select.length > 0) {
        $("#edit-title").html("新建节点");
        form[0].reset();
        var id = $('#id');
        $('#pid').val(id.val());
        id.val("");
    } else {
        bootbox.alert({
            message: "请先选择一个父节点进行创建!",
            size: 'small',
            buttons: {
                ok: {
                    label: "确认",
                    className: "btn-info"
                }
            }
        });
    }
});

//提交
$('.btn-submitNode').on('click', function () {
    var select = tree.treeview("getSelected");
    var id = $("#id").val();
    var method = id ? "PUT" : "POST";
    if (form.valid()) {
        $.ajax({
            type: method,
            url: baseUrl + "/" + (id ? id : ""),
            data: form.serialize(),
            success: function (data, status) {
                console.log(data);
                if (data && data.newnode) {
                    if (id) {
                        //更新
                        tree.treeview('updateNode', [select, data.newnode, {silent: true}]);
                        bootbox.alert({
                            message: data.message,
                            size: 'small',
                            buttons: {
                                ok: {
                                    label: "确认",
                                    className: "btn-info"
                                }
                            }
                        });
                    } else {
                        //新建
                        tree.treeview("addNode", [data.newnode, select]);
                        bootbox.alert({
                            message: data.message,
                            size: 'small',
                            buttons: {
                                ok: {
                                    label: "确认",
                                    className: "btn-info"
                                }
                            }
                        });
                    }
                }
                $("#edit-title").html("编辑节点");
            }
        });
    } else {
        //请输入数据
        bootbox.alert({
            title: "保存失败",
            message: "请输入正确的数据后才能提交!",
            size: 'small',
            buttons: {
                ok: {
                    label: "确认",
                    className: "btn-info"
                }
            }
        });
    }
});

//删除节点
$(".btn-deleteNode").on('click', function () {
    var select = tree.treeview("getSelected");
    if (select.length === 0) {
        //请选择要删除的对象
        bootbox.alert({
            title: "删除失败",
            message: "请选择要删除的对象!",
            size: 'small',
            buttons: {
                ok: {
                    label: "确认",
                    className: "btn-info"
                }
            }
        });
        return;
    } else if (select[0].pid == null) {
        bootbox.alert({
            title: "删除失败",
            message: "请在分类一览中进行删除根节点!",
            size: 'small',
            buttons: {
                ok: {
                    label: "确认",
                    className: "btn-info"
                }
            }
        });
        return;
    }
    var idArr = [];
    for (var i = 0; i < select.length; i++) {
        idArr.push(select[i].id);
    }
    var ids = idArr.join(",");
    console.log(ids);
    $.ajax({
        url: baseUrl + "/" + select[0].id,
        type: "POST",
        data: {_method: "DELETE", ids: ids},
        success: function (data, status) {
            if (data && data.message) {
                console.log(data.message);
                tree.treeview('removeNode', [select, {silent: true}]);
                bootbox.alert({
                    title: "提示",
                    message: "删除成功",
                    size: 'small',
                    buttons: {
                        ok: {
                            label: "确认",
                            className: "btn-info"
                        }
                    }
                });
            }
        }
    });
});