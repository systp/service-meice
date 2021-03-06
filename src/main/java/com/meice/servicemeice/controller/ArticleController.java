package com.meice.servicemeice.controller;

import com.ikuijia.toolkit.common.StringUtil;
import com.ikuijia.webmvc.support.builder.JsonResultBuilder;
import com.ikuijia.webmvc.support.result.PageJsonResult;
import com.ikuijia.webmvc.support.result.Result;
import com.meice.servicemeice.entity.Article;
import com.meice.servicemeice.entity.ArticleExample;
import com.meice.servicemeice.entity.From.ArticleFrom;
import com.meice.servicemeice.entity.VO.ArticleVo;
import com.meice.servicemeice.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/ArticleController")
@Api(description = "文章接口")
public class ArticleController {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    ArticleService articleService;

    @PostMapping("/queryArticle")
    @ApiOperation(value="分页查询文章",notes = "分页查询文章")
    public PageJsonResult<List<ArticleVo>> queryPage(@RequestBody @Valid ArticleFrom articleFrom){
        ArticleExample articleExample =new ArticleExample();
        //查询条件:文章ID
        if (StringUtil.isNotBlank(articleFrom.getArticleid())){
            articleExample.createCriteria().andArticleidEqualTo(articleFrom.getArticleid());
        }
        articleExample.setOffset(articleFrom.start());
        articleExample.setLimit(articleFrom.getPageSize());
        List<ArticleVo> articleVos = articleService.selectArticleInnerLable(articleExample);
        //pageSize每条页数   page当前页   totalSize 总页数
        return JsonResultBuilder.pageSucc(articleVos,articleFrom.getPage(),articleFrom.getPageSize(),articleService.countByExample(articleExample));
    }





    @PostMapping("/queryArticleById")
    @ApiOperation(value="根据ID查询文章",notes = "根据ID查询文章")
    public Result<ArticleVo> queryArticleById(@RequestBody @Valid String articleId){
        return  JsonResultBuilder.pageSucc(articleService.selectByPrimaryKey(articleId));
    }


    @PostMapping("/updateArticle")
    @ApiOperation(value="修改文章",notes = "修改文章")
    public Result<Boolean> updateArticle(@RequestBody  Article article){
        try {
            if(articleService.updateByPrimaryKey(article)>0)
                return  JsonResultBuilder.simpleFail("200","修改成功！");
        }catch (Exception e){
            LOG.info(e.getMessage());
            e.printStackTrace();
        }
        return  JsonResultBuilder.simpleFail("200","操作失败！");
    }


    @PostMapping("/addArticle")
    @ApiOperation(value="新增文章",notes = "新增文章")
    public Result<Boolean> addArticle(@RequestBody  Article article){
        try {
            if(articleService.insert(article)>0)
                return  JsonResultBuilder.simpleFail("200","新增成功！");
        }catch (Exception e){
            LOG.info(e.getMessage());
            e.printStackTrace();
        }
        return  JsonResultBuilder.simpleFail("200","操作失败！");
    }

    @PostMapping("/deleteArticle")
    @ApiOperation(value="删除文章",notes = "删除文章")
    public Result<Boolean> deleteArticle(@RequestBody @Valid String articleId){
        ArticleExample articleExample =new ArticleExample();
        articleExample.createCriteria().andArticleidEqualTo(articleId);
        try {
            if(articleService.deleteByExample(articleExample)>0)
                return  JsonResultBuilder.simpleFail("200","删除成功！");
        }catch (Exception e){
            LOG.info(e.getMessage());
            e.printStackTrace();
        }
        return  JsonResultBuilder.simpleFail("200","操作失败！");
    }

}
