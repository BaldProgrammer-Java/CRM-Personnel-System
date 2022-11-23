layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });


   /* $.post(ctx+"/user/queryAllCustomerManager",function (res) {
        for (var i = 0; i < res.length; i++) {
            if($("input[name='man']").val() == res[i].id ){
                $("#assignMan").append("<option value=\"" + res[i].id + "\" selected='selected' >" + res[i].name + "</option>");
            }else {
                $("#assignMan").append("<option value=\"" + res[i].id + "\">" + res[i].name + "</option>");
            }
        }
        //重新渲染
        layui.form.render("select");
    });*/


    form.on("submit(addOrUpdateSaleChance)", function (data) {
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        //弹出loading
        var url=ctx + "/sale_chance/add";

        var saleChanceId = $("[name='id']").val();

        if (saleChanceId != null && saleChanceId != '') {
            url = ctx + "/sale_chance/update";
        }

        $.post(url, data.field, function (res) {
            console.log(data.field);
            if (res.code == 200) {
                setTimeout(function () {
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    layer.closeAll("iframe");
                    //刷新父页面
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(
                    res.msg, {
                        icon: 5
                    }
                );
            }
        });
        return false;
    });


    /**
     * 加载指派人的下拉框
     */
    $.ajax({
        type:"get",
        url:ctx + "/user/queryAllSales",
        data:{},
        success:function (data) {
            // console.log(data);
            // 判断返回的数据是否为空
            if (data != null) {
                // 获取隐藏域设置的指派人ID
                var assignManId = $("#assignManId").val();
                // 遍历返回的数据
                for(var i = 0; i < data.length; i++) {
                    var opt = "";
                    // 如果循环得到的ID与隐藏域的ID相等，则表示被选中
                    if (assignManId == data[i].id) {
                        // 设置下拉选项  设置下拉选项选中
                        opt = "<option value='"+data[i].id+"' selected>"+data[i].uname+"</option>";
                    } else {
                        // 设置下拉选项
                        opt = "<option value='"+data[i].id+"'>"+data[i].uname+"</option>";
                    }

                    // 将下拉项设置到下拉框中
                    $("#assignMan").append(opt);
                }
            }
            // 重新渲染下拉框的内容
            layui.form.render("select");
        }
    });

});