package com.gzuniversity.eduservice.controller;



import com.gzuniversity.commonutils.R;
import com.gzuniversity.eduservice.entity.EduTeacher;
import com.gzuniversity.eduservice.entity.vo.TeacherQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzuniversity.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author zzx
 * @since 2022-08-19
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {
    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher(){
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }

    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name = "id", value = "讲师id", required = true) @PathVariable String id){
        boolean removeById = teacherService.removeById(id);
        if (removeById) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "分页查询讲师")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageTeacher(@PathVariable Long current, @PathVariable Long limit) {
        //创建page
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);

//        try {
//            int i = 10 / 0;
//        } catch (Exception e) {
//            throw new GuliException(20001, "方法执行GuliException异常");
//        }

        //调用方法，把所有数据封装到pageTeacher中
        teacherService.page(pageTeacher, null);

        long total = pageTeacher.getTotal();//获取总记录数
        List<EduTeacher> records = pageTeacher.getRecords();//数据集合

        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", records);
        return R.ok().data(map);
    }

    //条件查询
    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable Long current, @PathVariable Long limit, @RequestBody(required = false) TeacherQuery teacherQuery) {
        //创建page
        Page<EduTeacher> pageCondition = new Page<>(current, limit);

        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询，动态sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件是否为空，拼接条件
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        wrapper.orderByDesc("gmt_create");

        //调用方法，实现分页查询
        teacherService.page(pageCondition, wrapper);

        long total = pageCondition.getTotal();//获取总记录数
        List<EduTeacher> records = pageCondition.getRecords();//获取分页后的list集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", records);
        return R.ok().data(map);
    }

    @ApiOperation("添加教师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean save = teacherService.save(eduTeacher);
        if (save) {
            return R.ok();
        } else
            return R.error();
    }

    @ApiOperation("根据ID查询教师")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher byId = teacherService.getById(id);
        return R.ok().data("teacher", byId);
    }

    @ApiOperation("修改教师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher) {
        boolean b = teacherService.updateById(eduTeacher);
        if (b) {
            return R.ok();
        } else
            return R.error();
    }

}

