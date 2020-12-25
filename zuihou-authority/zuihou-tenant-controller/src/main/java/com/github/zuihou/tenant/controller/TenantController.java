package com.github.zuihou.tenant.controller;


import com.github.zuihou.base.R;
import com.github.zuihou.base.controller.SuperCacheController;
import com.github.zuihou.database.mybatis.conditions.Wraps;
import com.github.zuihou.log.annotation.SysLog;
import com.github.zuihou.tenant.dto.TenantSaveDTO;
import com.github.zuihou.tenant.dto.TenantUpdateDTO;
import com.github.zuihou.tenant.entity.Tenant;
import com.github.zuihou.tenant.enumeration.TenantStatusEnum;
import com.github.zuihou.tenant.service.TenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.github.zuihou.tenant.enumeration.TenantStatusEnum.NORMAL;

/**
 * <p>
 * 前端控制器
 * 企业
 * </p>
 *
 * @author zuihou
 * @date 2019-10-24
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/tenant")
@Api(value = "Tenant", tags = "企业")
@SysLog(enabled = false)
public class TenantController extends SuperCacheController<TenantService, Long, Tenant, Tenant, TenantSaveDTO, TenantUpdateDTO> {


    @ApiOperation(value = "查询所有企业", notes = "查询所有企业")
    @GetMapping("/all")
    public R<List<Tenant>> list() {
        return success(baseService.list(Wraps.<Tenant>lbQ().eq(Tenant::getStatus, NORMAL)));
    }

    @Override
    public R<Tenant> handlerSave(TenantSaveDTO model) {
        Tenant tenant = baseService.save(model);
        return success(tenant);
    }

    @ApiOperation(value = "检测租户是否存在", notes = "检测租户是否存在")
    @GetMapping("/check/{code}")
    public R<Boolean> check(@PathVariable("code") String code) {
        return success(baseService.check(code));
    }


    @Override
    public R<Boolean> handlerDelete(List<Long> ids) {
        // 这个操作相当的危险，请谨慎操作！！!
        return success(baseService.delete(ids));
    }

    @ApiOperation(value = "修改租户状态", notes = "修改租户状态")
    @PostMapping("/status")
    public R<Boolean> updateStatus(@RequestParam("ids[]") List<Long> ids,
                                   @RequestParam(defaultValue = "FORBIDDEN") @NotNull(message = "状态不能为空") TenantStatusEnum status) {
        return success(baseService.update(Wraps.<Tenant>lbU().set(Tenant::getStatus, status).in(Tenant::getId, ids)));
    }
}
